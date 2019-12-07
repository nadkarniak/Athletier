package com.CS5520.athletier.ui.Leaderboards;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Challenges.ColoredSpinnerFragment;

import java.util.List;

public class LeaderboardsTabFragment extends Fragment {

    private LeaderboardsTabViewModel leaderboardsTabViewModel;
//    private Spinner sportsList;
    private ColoredSpinnerFragment sportSpinnerFrag;
    private RecyclerView leaderboardRecycler;
    private LeaderboardRecyclerAdapter adapter;

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
        setupFragments();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupObservers();
    }

    private void setupViews(View view) {
        // Find views using id's
        leaderboardRecycler = view.findViewById(R.id.leaderboardRecyclerView);
//        sportsList = view.findViewById(R.id.sportsSpinner);
    }

    private void setupFragments() {
        Context context = getContext();
        sportSpinnerFrag = (ColoredSpinnerFragment)
                getChildFragmentManager().findFragmentById(R.id.leaderboardSportsSpinner);
        if (sportSpinnerFrag != null && context != null ) {
            sportSpinnerFrag.setSpinnerOptions(context, Sport.getAllSportsNames());
            adapter = new LeaderboardRecyclerAdapter(Sport.fromString(Sport.getAllSportsNames().get(0)));
            leaderboardRecycler.setAdapter(adapter);
            leaderboardRecycler.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    private void setupObservers() {
        sportSpinnerFrag.getSelectedItem().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String sportName) {
                Sport sport = Sport.fromString(sportName);
                if (adapter != null && sport != null) {
                    adapter.setSelectedSport(sport);
                } else  if (sport != null) {
                    adapter = new LeaderboardRecyclerAdapter(sport);
                }
            }
        });
    }

//    private void setupSportsList(Context context) {
//        List<String> sports = Sport.getAllSportsNames();
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                context,
//                android.R.layout.simple_spinner_dropdown_item,
//                sports
//        );
//        sportsList.setAdapter(adapter);
//    }

}
