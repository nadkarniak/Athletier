package com.CS5520.athletier.ui.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengesTabFragment extends Fragment {

    private ChallengesTabViewModel viewModel;
    private SegmentedSelectorFragment statusSelector;
    private ChallengeRecyclerFragment recyclerFragment;
    private List<String> selectorTitles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges_tab, container, false);
        findFragments();
        setupStatusSelector();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChallengesTabViewModel.class);
        // TODO: Delete after implementing Firebase database User
        viewModel.setCurrentUser(
                new User("1",
                "Dummy User 1",
                null, "dummy@gmail.com",
                "1111111")
        );
        setupStatusSelector();
    }

    @Override
    public void onStart() {
        super.onStart();
        observeStatusSelector();
        observeChallenges();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.challenge_top_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void findFragments() {
        FragmentManager manager = getChildFragmentManager();
        statusSelector = (SegmentedSelectorFragment)
                manager.findFragmentById(R.id.statusSelector);
        recyclerFragment = (ChallengeRecyclerFragment)
                manager.findFragmentById(R.id.acceptedChallengeRecycler);
    }

    private void setupStatusSelector() {
        if (statusSelector == null) { return; }

        // Set selector button titles
        selectorTitles = new ArrayList<>();
        selectorTitles.add(AcceptanceStatus.ACCEPTED.name());
        selectorTitles.add(AcceptanceStatus.PENDING.name());
        selectorTitles.add(AcceptanceStatus.COMPLETE.name());

        statusSelector.setButtonTitles(
                selectorTitles.get(0),
                selectorTitles.get(1),
                selectorTitles.get(2)
        );
    }

    private void observeStatusSelector() {
        statusSelector.getSelectedPosition().observe(getViewLifecycleOwner(),
                new Observer<Integer>() {
            @Override
            public void onChanged(Integer selectedPosition) {
                viewModel.setSelectedStatusFilter(selectedPosition);

                // Update the recyclerView adapter with the current challenge data stored in the
                // view model
                List<Challenge> challenges = viewModel.getShouldFilterAsHost() ?
                        viewModel.getHostedChallenges().getValue() :
                        viewModel.getChallengesAsOpponent().getValue();
                if (challenges != null) {
                    recyclerFragment.updateChallenges(getStatusFilteredChallenges(challenges));
                }
            }
        });
    }


    // Observe streams of challenges from view model
    private void observeChallenges() {
        viewModel.getHostedChallenges().observe(getViewLifecycleOwner(),
                new Observer<List<Challenge>>() {
            @Override
            public void onChanged(List<Challenge> challenges) {
                if (viewModel.getShouldFilterAsHost()) {
                    recyclerFragment.updateChallenges(getStatusFilteredChallenges(challenges));
                }
            }
        });

        viewModel.getChallengesAsOpponent().observe(getViewLifecycleOwner(),
                new Observer<List<Challenge>>() {
            @Override
            public void onChanged(List<Challenge> challenges) {
                if (!viewModel.getShouldFilterAsHost()) {
                    recyclerFragment.updateChallenges(getStatusFilteredChallenges(challenges));
                }
            }
        });
    }

    // Filter a list of challenges by the currently selected AcceptanceStatus
    private List<Challenge> getStatusFilteredChallenges(List<Challenge> challenges) {
        List<Challenge> filteredChallenges = new ArrayList<>();
        for (Challenge challenge : challenges) {
            if (challenge.getAcceptanceStatus().equals(viewModel.getStatusFilter())) {
                filteredChallenges.add(challenge);
            }
        }
        return filteredChallenges;
    }
}