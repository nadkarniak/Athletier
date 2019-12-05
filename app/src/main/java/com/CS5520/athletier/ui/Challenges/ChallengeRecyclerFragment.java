package com.CS5520.athletier.ui.Challenges;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ResultStatus;
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

    private DatabaseReference databaseReference;
    private SpinnerInputFragment spinner;
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
        setupRecyclerView();
        observeForProfileImageClicks();
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

                switch (action) {
                    case HOST_REPORT:
                        launchSelectWinnerDialog(challenge.getId(), true);
                        break;
                    case OPPONENT_REPORT:
                        launchSelectWinnerDialog(challenge.getId(), false);
                        break;
                    case ACCEPT:
                        // Change status of challenge to accepted
                        ChallengeUpdater.updateAcceptanceStatus(
                                databaseReference,
                                challenge.getId(),
                                AcceptanceStatus.ACCEPTED
                        );
                        break;
                    case CANCEL:
                        // Delete challenge
                        ChallengeUpdater.deleteChallenge(databaseReference, challenge.getId());
                        break;
                    case HOST_RATE:
                        break;
                    case OPPONENT_RATE:
                        break;
                }
            }
        });
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
    public void onDialogPositiveClick(final String challengeId,
                                      final boolean reportingAsHost,
                                      final boolean hostSelectedAsWinner) {
        // Update the result of the challenge with the input Id
        databaseReference
                .child(Challenge.challengeKey)
                .child(challengeId)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Challenge challenge = mutableData.getValue(Challenge.class);
                        if (challenge == null) {
                            return Transaction.success(mutableData);
                        }

                        // Determine the id of the reported winner
                        String winnerId = hostSelectedAsWinner ? challenge.getHostId()
                                : challenge.getOpponentId();

                        // If the reporter was the host, set the hostReportedWinner, otherwise set
                        // the opponentReportedWinner
                        if (reportingAsHost) {
                            challenge.setHostReportedWinner(winnerId);
                        } else {
                            challenge.setOpponentReportedWinner(winnerId);
                        }

                        mutableData.setValue(challenge);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           boolean b,
                                           @Nullable DataSnapshot dataSnapshot) {
                        // TODO: Launch activity for rating opposing player
                    }
                });
    }
}
