package com.CS5520.athletier.ui.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.ChallengeStatus;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengesTabFragment extends Fragment {

    private ChallengesTabViewModel challengesTabViewModel;
    private SegmentedSelectorFragment statusSelector;
    private List<String> selectorTitles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges_tab, container, false);
        findFragments();
        setupStatusSelector();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        challengesTabViewModel = ViewModelProviders.of(this).get(ChallengesTabViewModel.class);
        setupStatusSelector();
    }

    private void findFragments() {
        FragmentManager manager = getChildFragmentManager();
        statusSelector = (SegmentedSelectorFragment) manager.findFragmentById(R.id.statusSelector);;
    }

    private void setupStatusSelector() {
        if (statusSelector == null) { return; }

        selectorTitles = new ArrayList<>();
        selectorTitles.add(AcceptanceStatus.ACCEPTED.name());
        selectorTitles.add(AcceptanceStatus.PENDING.name());
        selectorTitles.add(ChallengeStatus.COMPLETE.name());

        statusSelector.setButtonTitles(
                selectorTitles.get(0),
                selectorTitles.get(1),
                selectorTitles.get(2)
        );
    }


}