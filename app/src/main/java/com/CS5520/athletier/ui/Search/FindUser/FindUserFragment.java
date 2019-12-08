package com.CS5520.athletier.ui.Search.FindUser;


import android.app.Activity;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Challenges.ColoredSpinnerFragment;
import com.CS5520.athletier.ui.Leaderboards.BadgeRecyclerAdapter;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

public class FindUserFragment extends Fragment {

    private FindUserViewModel viewModel;
    private Button follow;
    private Button challengeButton;
    private String email;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRef;
    private TextView usernameText;
    private TextView expText;
    private TextView followersText;
    private TextView tierText;
    private RatingBar sportsmanshipBar;
    private ColoredSpinnerFragment sportsSpinner;
    private ImageView profilePicture;
    private LinearLayout backgroundPicture;
    private BadgeRecyclerAdapter adapter;
    private RecyclerView badgeRecycler;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            email = arguments.getString("email");
        }
        View view = inflater.inflate(R.layout.fragment_find_user, container, false);
        setupViews(view);
        setupSportsSpinner();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FindUserViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        viewModel.findUserWithEmail(email);
        setupChallengeButton();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFollowButtonListener();
        setObservers();
        observeSpinnerSelection();
//        setupBadges(getContext(), "1v1 Basketball");
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
                        Sport sport = Sport.fromString(s);
                        if (sport != null) {
                            viewModel.setSelectedSport(sport);
                            switch (sport) {
                                case ONE_V_ONE_BASKETBALL:
                                    backgroundPicture.setBackgroundResource(
                                            R.drawable.basketball_background
                                    );
                                    break;
                                case TENNIS:
                                    backgroundPicture.setBackgroundResource(
                                            R.drawable.tennis_background
                                    );
                                    break;
                                case GOLF:
                                    backgroundPicture.setBackgroundResource(
                                            R.drawable.golf_background
                                    );
                            }
                        }
                    }
                });
    }

    private void setupViews(View view) {
        // Find views using id's
        FragmentManager manager = getFragmentManager();
        if (manager == null) { return; }

        sportsSpinner = (ColoredSpinnerFragment)
                manager.findFragmentById(R.id.sportsSpinnerFragment);

        usernameText = ((LinearLayout)view).findViewById(R.id.userName);
        backgroundPicture = view.findViewById(R.id.searchBackground);
        expText = ((LinearLayout)view).findViewById(R.id.exp_search);
        followersText = ((LinearLayout)view).findViewById(R.id.followers);
        tierText = ((LinearLayout)view).findViewById(R.id.following);
        sportsmanshipBar = ((LinearLayout)view).findViewById(R.id.ratingBar);
        profilePicture = ((LinearLayout)view).findViewById(R.id.profilePic);
        follow = view.findViewById(R.id.follow);
        challengeButton = view.findViewById(R.id.challengeMeButton);
        badgeRecycler = view.findViewById(R.id.findUserBadgeRecycler);
    }


    private void setupFollowButtonListener() {
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow.setText(R.string.following);
                int x = Integer.parseInt(followersText.getText().toString()) + 1;
                followersText.setText(String.valueOf(x));
            }
        });
    }

    private void setupChallengeButton() {
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateChallengeActivity();
            }
        });
    }

    private void launchCreateChallengeActivity() {
        Activity currentActivity = getActivity();
        User opponentUser = viewModel.getUserResult().getValue();
        if (currentActivity != null && opponentUser != null) {
            Intent intent = new Intent(currentActivity, CreateChallengeActivity.class);
            intent.putExtra(Challenge.opponentIdKey, opponentUser.getId());
            startActivity(intent);
            currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
        }
    }

    private void setObservers() {
        viewModel.getUserResult().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameText.setText(user.getUsername());
                followersText.setText(user.getFollowers() != null ?
                        "" + user.getFollowers().size() : "" + 0);
                sportsmanshipBar.setRating(user.getAvgSportsmanshipRating());
                Picasso.get().load(user.getPhotoUrl()).into(profilePicture);
                viewModel.queryExp(user.getId());

                // Disable follow me buttons if current User is the User displayed in Activity
                follow.setEnabled(!user.getId().equals(viewModel.getCurrentUserId()));
                challengeButton.setEnabled(!user.getId().equals(viewModel.getCurrentUserId()));
                follow.getBackground().setAlpha(follow.isEnabled() ? 255 : 128);
                challengeButton.getBackground().setAlpha(challengeButton.isEnabled() ? 255 : 128);
            }
        });

        viewModel.getSportsAchievementSummary().observe(getViewLifecycleOwner(),
                new Observer<SportsAchievementSummary>() {
            @Override
            public void onChanged(SportsAchievementSummary sas) {
                expText.setText(String.valueOf(sas.getExp()));
                tierText.setText(String.valueOf(sas.getTier()));
                adapter = new BadgeRecyclerAdapter(sas);
                badgeRecycler.setAdapter(adapter);
                badgeRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
                );
            }
        });

    }
}
