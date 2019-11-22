package com.CS5520.athletier.ui.Profile;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.DataBase;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileTabFragment extends Fragment {

    private ProfileTabViewModel profileTabViewModel;
    private User user;
    private TextView usernameText;
    private TextView recordText;
    private RatingBar sportsmanshipBar;
    private ListView sportsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        setupViews(view);
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileTabViewModel =
                ViewModelProviders.of(this).get(ProfileTabViewModel.class);
        profileTabViewModel.findUserWithId("Id which should come from login");
        setupObservers();
        setupSportsList(getContext());
    }

    private void setupViews(View view) {
        // Find views using id's
        usernameText = view.findViewById(R.id.userName);
        recordText = view.findViewById(R.id.record);
        sportsmanshipBar = view.findViewById(R.id.ratingBar);
        sportsList = view.findViewById(R.id.sports_view);
    }

    private void setupSportsList(Context context) {
        List<String> sports = Sport.getAllSportsNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_expandable_list_item_1,
                sports
        );
        sportsList.setAdapter(adapter);
    }

    private void setupObservers() {
        profileTabViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameText.setText(user.getUsername());
                recordText.setText(user.getRecord());
                sportsmanshipBar.setRating(user.getAvgSportsmanshipRating());
            }
        });
    }

}