package com.CS5520.athletier.ui.Challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;

public class ChallengesTabFragment extends Fragment {

    private ChallengesTabViewModel challengesTabViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenges_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        challengesTabViewModel = ViewModelProviders.of(this).get(ChallengesTabViewModel.class);
    }

}