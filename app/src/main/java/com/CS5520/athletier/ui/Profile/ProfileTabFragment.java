package com.CS5520.athletier.ui.Profile;

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

public class ProfileTabFragment extends Fragment {

    private ProfileTabViewModel profileTabViewModel;
    private User user;
    private TextView username = (TextView)getView().findViewById(R.id.userName);
    private TextView record = (TextView)getView().findViewById(R.id.record);
    private RatingBar sportsmanship = getView().findViewById(R.id.ratingBar);
    private ListView sports = getView().findViewById(R.id.sports_view);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        username.setText(user.getUsername());
        record.setText(user.getRecord());
        sportsmanship.setRating(user.getAvgSportsmanshipRating());
        ArrayList<String> sportsList = new ArrayList<>();
        sportsList.add(Sport.GOLF.toString());
        sportsList.add(Sport.ONE_V_ONE_BASKETBALL.toString());
        sportsList.add(Sport.SPIKEBALL.toString());
        sportsList.add(Sport.TENNIS.toString());
        sportsList.add(Sport.SQUASH.toString());

        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1,
                sportsList);

        sports.setAdapter(adapter);

        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileTabViewModel =
                ViewModelProviders.of(this).get(ProfileTabViewModel.class);
    }



}