package com.CS5520.athletier.ui.Leaderboards;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.CS5520.athletier.R;

public class LeaderboardsTabFragment extends Fragment {

    private LeaderboardsTabViewModel leaderboardsTabViewModel;

    public static LeaderboardsTabFragment newInstance() {
        return new LeaderboardsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboards_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leaderboardsTabViewModel =
                ViewModelProviders.of(this).get(LeaderboardsTabViewModel.class);
    }

}
