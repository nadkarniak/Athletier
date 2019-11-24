package com.CS5520.athletier.ui.Leaderboards;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;

import java.util.List;

public class LeaderboardsTabFragment extends Fragment {

    private LeaderboardsTabViewModel leaderboardsTabViewModel;
    private Spinner sportsList;

    public static LeaderboardsTabFragment newInstance() {
        return new LeaderboardsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboards_tab, container, false);
        setupViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leaderboardsTabViewModel =
                ViewModelProviders.of(this).get(LeaderboardsTabViewModel.class);
        setupSportsList(getContext());
    }

    private void setupViews(View view) {
        // Find views using id's
        sportsList = ((LinearLayout)view).findViewById(R.id.sportsSpinner);
    }

    private void setupSportsList(Context context) {
        List<String> sports = Sport.getAllSportsNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                sports
        );
        sportsList.setAdapter(adapter);
    }

}
