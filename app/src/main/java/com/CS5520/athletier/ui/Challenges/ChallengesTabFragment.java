package com.CS5520.athletier.ui.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengesTabFragment extends Fragment {

    private ChallengesTabViewModel viewModel;
    private ColoredSpinnerFragment hostChallengerSpinner;
    private SegmentedSelectorFragment statusSelector;
    private ChallengeRecyclerFragment recyclerFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges_tab, container, false);
        findFragments();
        setupSpinner();
        setupStatusSelector();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChallengesTabViewModel.class);
        setupStatusSelector();
    }

    @Override
    public void onStart() {
        super.onStart();
        observeSpinnerSelections();
        observeStatusSelector();
        observeChallenges();
    }

    private void findFragments() {
        FragmentManager manager = getChildFragmentManager();
        hostChallengerSpinner = (ColoredSpinnerFragment)
                manager.findFragmentById(R.id.hostSpinnerFragment);
        statusSelector = (SegmentedSelectorFragment)
                manager.findFragmentById(R.id.statusSelector);
        recyclerFragment = (ChallengeRecyclerFragment)
                manager.findFragmentById(R.id.acceptedChallengeRecycler);
    }

    private void setupSpinner() {
        hostChallengerSpinner = (ColoredSpinnerFragment)
                getChildFragmentManager().findFragmentById(R.id.hostSpinnerFragment);
        if (hostChallengerSpinner == null) { System.out.println("null");}

        List<String> spinnerOptions =  new ArrayList<>();
        spinnerOptions.add(getString(R.string.as_host));
        spinnerOptions.add(getString(R.string.as_challenger));
        hostChallengerSpinner.setSpinnerOptions(getContext(), spinnerOptions);
    }

    private void setupStatusSelector() {
        if (statusSelector == null || viewModel == null) { return; }

        statusSelector.setButtonTitles(
                viewModel.getSelectorTitles().get(0),
                viewModel.getSelectorTitles().get(1),
                viewModel.getSelectorTitles().get(2)
        );
    }

    private void observeSpinnerSelections() {
        hostChallengerSpinner.getSelectedItem().observe(getViewLifecycleOwner(),
                new Observer<String>() {
            @Override
            public void onChanged(String selection) {
                boolean shouldDisplayHostedChallenges =
                        selection.equals(getString(R.string.as_host));
                viewModel.setShouldDisplayHostedChallenges(shouldDisplayHostedChallenges);
                recyclerFragment.updateAsHost(shouldDisplayHostedChallenges);
                refreshChallengeData();
            }
        });
    }


    private void observeStatusSelector() {
        statusSelector.getSelectedPosition().observe(getViewLifecycleOwner(),
                new Observer<Integer>() {
            @Override
            public void onChanged(Integer selectedPosition) {
                viewModel.setSelectedStatusFilter(selectedPosition);
                System.out.println(viewModel.getStatusFilter());
                // Update the recyclerView adapter with the current challenge data stored in the
                // view model
                refreshChallengeData();
            }
        });
    }

    private void refreshChallengeData() {
        List<Challenge> challenges = viewModel.getShouldDisplayHostedChallenges() ?
                viewModel.getHostedChallenges().getValue() :
                viewModel.getChallengesAsOpponent().getValue();
        if (challenges != null) {
            recyclerFragment.updateChallenges(getStatusFilteredChallenges(challenges));
        }
    }

    // Observe streams of challenges from view model
    private void observeChallenges() {
        viewModel.getHostedChallenges().observe(getViewLifecycleOwner(),
                new Observer<List<Challenge>>() {
            @Override
            public void onChanged(List<Challenge> challenges) {
                if (viewModel.getShouldDisplayHostedChallenges()) {
                    recyclerFragment.updateChallenges(getStatusFilteredChallenges(challenges));
                }
            }
        });

        viewModel.getChallengesAsOpponent().observe(getViewLifecycleOwner(),
                new Observer<List<Challenge>>() {
            @Override
            public void onChanged(List<Challenge> challenges) {
                if (!viewModel.getShouldDisplayHostedChallenges()) {
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