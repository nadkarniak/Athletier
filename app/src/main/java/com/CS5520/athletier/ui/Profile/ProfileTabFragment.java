package com.CS5520.athletier.ui.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileTabFragment extends Fragment {

    private ProfileTabViewModel profileTabViewModel;
    private TextView usernameText;
    private TextView recordText;
    private TextView followersText;
    private TextView followingText;
    private RatingBar sportsmanshipBar;
    private Spinner sportsList;
    private ImageView firstBadge;
    private ImageView secondBadge;
    private ImageView thirdBadge;
    private ImageView fourthBadge;
    private ImageView fifthBadge;
    private ImageView profilePicture;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        setupViews(view);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileTabViewModel = ViewModelProviders.of(this).get(ProfileTabViewModel.class);
        setupObservers();
        setupSportsList(getContext());
        setupBadges(getContext());
    }

    private void setupViews(View view) {
        // Find views using id's
        usernameText = view.findViewById(R.id.userName);
        recordText = view.findViewById(R.id.record);
        followersText = view.findViewById(R.id.followers);
        followingText = view.findViewById(R.id.following);
        sportsmanshipBar = view.findViewById(R.id.ratingBar);
        sportsList = view.findViewById(R.id.sportsSpinner);
        firstBadge = view.findViewById(R.id.first_badge);
        secondBadge = view.findViewById(R.id.second_badge);
        thirdBadge = view.findViewById(R.id.third_badge);
        fourthBadge = view.findViewById(R.id.fourth_badge);
        fifthBadge = view.findViewById(R.id.fifth_badge);
        profilePicture = view.findViewById(R.id.profilePic);
    }

    private void setupSportsList(Context context) {
        List<String> sports = Sport.getAllSportsNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                sports
        );
        sportsList.setAdapter(adapter);
        sportsList.setSelection(0);
    }

    private List<SportsBadge> getBadgeList(Context context) {
        switch(sportsList.getSelectedItem().toString()) {
            case("1v1 Basketball"):
                return Sport.ONE_V_ONE_BASKETBALL.getBadgeOptions();
            default:
                return new ArrayList<>();
        }
    }

    // TODO: Change this so it comes from SportsAchievementSummary
    private void setupBadges(Context context) {
        List<SportsBadge> badges = getBadgeList(context);
        firstBadge.setImageResource(badges.get(0).getResId());
        secondBadge.setImageResource(badges.get(1).getResId());
        thirdBadge.setImageResource(badges.get(2).getResId());
        fourthBadge.setImageResource(badges.get(3).getResId());
        fifthBadge.setImageResource(badges.get(4).getResId());
    }

    private void setupObservers() {
        profileTabViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameText.setText(user.getUsername());
                followersText.setText(String.valueOf(0));
                followingText.setText(String.valueOf(0));
                sportsmanshipBar.setRating(user.getAvgSportsmanshipRating());
                Picasso.get().load(user.getPhotoUrl()).into(profilePicture);
            }
        });

//        profileTabViewModel.getAchievements().observe(getViewLifecycleOwner(),
//                new Observer<List<SportsAchievementSummary>>() {
//            @Override
//            public void onChanged(List<SportsAchievementSummary> sportsAchievementSummaries) {
//                // Set badge resources
//            }
//        });
    }

}