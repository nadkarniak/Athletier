package com.CS5520.athletier.ui.Challenges;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ResultStatus;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.ChallengeButtonAction;
import com.CS5520.athletier.Utilities.ChallengeUpdater;
import com.CS5520.athletier.ui.Map.SpinnerInputFragment;
import com.CS5520.athletier.ui.Search.FindUser.FindUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecyclerFragment extends Fragment implements
        SelectWinnerDialogFragment.SelectedWinnerDialogListener {

    private ChallengeRecyclerViewModel viewModel;
    private DatabaseReference databaseReference;
    private ChallengeRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_recycler, container, false);
        recyclerView = view.findViewById(R.id.challengeRecycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChallengeRecyclerViewModel.class);
        setupRecyclerView();
        observeForProfileImageClicks();
        setupUpdateObservers();
    }

    void updateAsHost(boolean asHost) {
        adapter.updateAsHost(asHost);
    }

    void updateChallenges(List<Challenge> challenges) {
        adapter.updateChallenges(challenges);
    }

    private void setupRecyclerView() {
        adapter = new ChallengeRecyclerAdapter(
                new ArrayList<Challenge>(),
                true,
                getContext()
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        observeChallengeAdapterActions();
    }

    private void observeChallengeAdapterActions() {
        adapter.getChallengeAndAction().observe(getViewLifecycleOwner(),
                new Observer<Pair<Challenge, ChallengeButtonAction>>() {
            @Override
            public void onChanged(Pair<Challenge, ChallengeButtonAction> challengeActionPair) {
                Challenge challenge = challengeActionPair.first;
                ChallengeButtonAction action = challengeActionPair.second;
                if (action != ChallengeButtonAction.CANCEL) {
                    viewModel.setSelectedChallenge(challenge);
                }

                switch (action) {
                    // The user is reporting the winner as the host
                    case HOST_REPORT:
                        launchSelectWinnerDialog(challenge.getId(), true);
                        break;
                    // The user is reporting the winner as the opponent
                    case OPPONENT_REPORT:
                        launchSelectWinnerDialog(challenge.getId(), false);
                        break;
                    // The user is accepting a challenge
                    case ACCEPT:
                        // Change status of challenge to accepted
                        viewModel.updateChallengeAcceptanceStatus(challenge.getId(),
                                AcceptanceStatus.ACCEPTED);
                        break;
                    // The user hit the cancel button on the challenge
                    case CANCEL:
                        // Delete challenge
                        viewModel.deleteChallenge(challenge.getId());
                        break;
                    // The user is the host and is rating the opponent
                    case HOST_RATE:
                        // TODO: Trigger dialog for rating opponent and awarding badges
                        break;
                    // The user is the opponent and rating the host
                    case OPPONENT_RATE:
                        // TODO: Trigger dialog for rating host and awarding badges
                        break;
                }
            }
        });
    }

    private void setupUpdateObservers() {
        viewModel.getUserAwardedExp().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer expGained) {
                showToastMessage("Congratulations! You gained " + expGained + "pts!");
            }
        });
    }

    private void showToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void observeForProfileImageClicks() {
        adapter.getUserImageClickEventStream().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String userEmail) {
                Activity currentActivity = getActivity();
                if (currentActivity != null) {
                    Intent intent = new Intent(currentActivity, FindUserActivity.class);
                    intent.putExtra("email", userEmail);
                    startActivity(intent);
                    currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
                }
            }
        });
    }

    private void launchSelectWinnerDialog(String challengeId, boolean reportingAsHost) {
        DialogFragment dialogFragment = new SelectWinnerDialogFragment();
        // Pass challengeId to dialog fragment
        Bundle bundle = new Bundle();
        bundle.putString(Challenge.challengeKey, challengeId);
        bundle.putBoolean(Challenge.hostIdKey, reportingAsHost);
        dialogFragment.setArguments(bundle);
        // Show dialog
        dialogFragment.show(getChildFragmentManager(), "SelectWinnerDialogFragment");
    }


    @Override
    public void onDialogPositiveClick(final boolean reportingAsHost,
                                      final boolean hostSelectedAsWinner) {
        // Update the result of the challenge with the input Id
        viewModel.updateChallengeWinner(reportingAsHost, hostSelectedAsWinner );
    }

}


