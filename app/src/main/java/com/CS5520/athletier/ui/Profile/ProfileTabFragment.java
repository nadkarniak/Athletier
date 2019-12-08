package com.CS5520.athletier.ui.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Challenges.ColoredSpinnerFragment;
import com.CS5520.athletier.ui.Leaderboards.BadgeRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileTabFragment extends Fragment {

    private ProfileTabViewModel profileTabViewModel;
    private ColoredSpinnerFragment sportsSpinner;
    private TextView usernameText;
    private TextView expText;
    private TextView followersText;
    private TextView followingText;
    private RatingBar sportsmanshipBar;
//    private ImageView firstBadge;
//    private ImageView secondBadge;
//    private ImageView thirdBadge;
//    private ImageView fourthBadge;
//    private ImageView fifthBadge;
    private ImageView profilePicture;
    private Button logOut;
    private LinearLayout backgroundPicture;
    private RecyclerView badgeRecycler;
    private BadgeRecyclerAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        setupViews(view);
        setupSportsSpinner();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileTabViewModel = ViewModelProviders.of(this).get(ProfileTabViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupObservers();
        observeSpinnerSelection();
        observeSelectedAchievement();
//        setupBadges(getContext(), "1v1 Basketball");
//        setUpExp(Sport.fromString("1v1 Basketball"));
    }

    private void setupViews(View view) {
        // Find views using id's
        FragmentManager manager = getChildFragmentManager();
        sportsSpinner = (ColoredSpinnerFragment)
                manager.findFragmentById(R.id.sportsSpinnerFragment);

        logOut = view.findViewById(R.id.signout);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        backgroundPicture = view.findViewById(R.id.profile_background);
        usernameText = view.findViewById(R.id.userName);
        expText = view.findViewById(R.id.exp_search);
        followersText = view.findViewById(R.id.followers);
        followingText = view.findViewById(R.id.following);
        sportsmanshipBar = view.findViewById(R.id.ratingBar);
//        firstBadge = view.findViewById(R.id.first_badge);
//        secondBadge = view.findViewById(R.id.second_badge);
//        thirdBadge = view.findViewById(R.id.third_badge);
//        fourthBadge = view.findViewById(R.id.fourth_badge);
//        fifthBadge = view.findViewById(R.id.fifth_badge);
        profilePicture = view.findViewById(R.id.profilePic);
        badgeRecycler = view.findViewById(R.id.profileBadgeRecycler);
    }

    private void setupSportsSpinner() {
        sportsSpinner = (ColoredSpinnerFragment)
                getChildFragmentManager().findFragmentById(R.id.sportsSpinnerFragment);
        if (sportsSpinner == null) {
            System.out.println("null");
        }

        List<String> sports = Sport.getAllSportsNames();
        sportsSpinner.setSpinnerOptions(getContext(), sports);
    }

    private void observeSpinnerSelection() {
        if(sportsSpinner == null) {
            System.out.println("null");
        }
        sportsSpinner.getSelectedItem().observe(getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Sport selectedSport = Sport.fromString(s);
                        if (selectedSport != null) {
                            profileTabViewModel.setSelectedSport(selectedSport);
                        }
//                        setupBadges(getContext(), s);
//                        setUpExp(Sport.fromString(s));
                    }
                });
    }


//    private List<SportsBadge> getBadgeList(Context context, String s) {
//        switch(s) {
//            case("1v1 Basketball"):
//                backgroundPicture.setBackgroundResource(R.drawable.basketball_background);
//                return Sport.ONE_V_ONE_BASKETBALL.getBadgeOptions();
//            case("Golf"):
//                backgroundPicture.setBackgroundResource(R.drawable.golf_background);
//                return Sport.GOLF.getBadgeOptions();
//            case("Tennis"):
//                backgroundPicture.setBackgroundResource(R.drawable.tennis_background);
//                return Sport.TENNIS.getBadgeOptions();
//            default:
//                return new ArrayList<>();
//        }
//    }
//
//    // TODO: Change this so it comes from SportsAchievementSummary
//    private void setupBadges(Context context, String s) {
//        List<SportsBadge> badges = getBadgeList(context, s);
//        if(badges.size() > 0) {
//            firstBadge.setImageResource(badges.get(0).getResId());
//            secondBadge.setImageResource(badges.get(1).getResId());
//            thirdBadge.setImageResource(badges.get(2).getResId());
//            fourthBadge.setImageResource(badges.get(3).getResId());
//            fifthBadge.setImageResource(badges.get(4).getResId());
//        }
//    }


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

//    private void setUpExp(Sport s) {
//        profileTabViewModel.getExp(s).observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                expText.setText(s);
//            }
//        });
//    }

    private void observeSelectedAchievement() {
        profileTabViewModel.getSelectedAchievement().observe(getViewLifecycleOwner(),
                new Observer<SportsAchievementSummary>() {
            @Override
            public void onChanged(SportsAchievementSummary sportsAchievementSummary) {
                expText.setText(String.valueOf(sportsAchievementSummary.getExp()));
                adapter = new BadgeRecyclerAdapter(sportsAchievementSummary);
                badgeRecycler.setAdapter(adapter);
                badgeRecycler.setLayoutManager(new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
                );
            }
        });
    }

}