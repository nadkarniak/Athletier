package com.CS5520.athletier.ui.Search.FindUser;


import android.app.Activity;
import android.content.Context;
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

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsBadge;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Challenges.ColoredSpinnerFragment;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeActivity;
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
    private TextView followingText;
    private RatingBar sportsmanshipBar;
    private ColoredSpinnerFragment sportsSpinner;
    private ImageView firstBadge;
    private ImageView secondBadge;
    private ImageView thirdBadge;
    private ImageView fourthBadge;
    private ImageView fifthBadge;
    private ImageView profilePicture;
    private LinearLayout backgroundPicture;
    private String UID;


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
        setupBadges(getContext(), "1v1 Basketball");
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
                        setupBadges(getContext(), s);
                        Sport sport = Sport.fromString(s);
                        if (sport != null) {
                            setUpExp(sport);
                        }
                    }
                });
    }

    private List<SportsBadge> getBadgeList(Context context, String s) {
        switch(s) {
            case("1v1 Basketball"):
                backgroundPicture.setBackgroundResource(R.drawable.basketball_background);
                return Sport.ONE_V_ONE_BASKETBALL.getBadgeOptions();
            case("Golf"):
                backgroundPicture.setBackgroundResource(R.drawable.golf_background);
                return Sport.GOLF.getBadgeOptions();
            case("Tennis"):
                backgroundPicture.setBackgroundResource(R.drawable.tennis_background);
                return Sport.TENNIS.getBadgeOptions();
            default:
                return new ArrayList<>();
        }
    }

    private void setupBadges(Context context, String s) {
        List<SportsBadge> badges = getBadgeList(context, s);
        if(badges.size() > 0) {
            firstBadge.setImageResource(badges.get(0).getResId());
            secondBadge.setImageResource(badges.get(1).getResId());
            thirdBadge.setImageResource(badges.get(2).getResId());
            fourthBadge.setImageResource(badges.get(3).getResId());
            fifthBadge.setImageResource(badges.get(4).getResId());
        }
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
        followingText = ((LinearLayout)view).findViewById(R.id.following);
        sportsmanshipBar = ((LinearLayout)view).findViewById(R.id.ratingBar);
        firstBadge = ((LinearLayout)view).findViewById(R.id.first_badge);
        secondBadge = ((LinearLayout)view).findViewById(R.id.second_badge);
        thirdBadge = ((LinearLayout)view).findViewById(R.id.third_badge);
        fourthBadge = ((LinearLayout)view).findViewById(R.id.fourth_badge);
        fifthBadge = ((LinearLayout)view).findViewById(R.id.fifth_badge);
        profilePicture = ((LinearLayout)view).findViewById(R.id.profilePic);
        follow = view.findViewById(R.id.follow);
        challengeButton = view.findViewById(R.id.challengeMeButton);
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
        String opponentId = viewModel.getOtherUid().getValue();
        if (currentActivity != null && opponentId != null) {
            Intent intent = new Intent(currentActivity, CreateChallengeActivity.class);
            intent.putExtra(Challenge.opponentIdKey, opponentId);
            startActivity(intent);
            currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
        }
    }

    private void setObservers() {
        viewModel.getOtherUid().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mRef = FirebaseDatabase.getInstance().getReference().child("users").child(s);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("username").getValue().toString();
                        String photo = dataSnapshot.child("photoUrl").getValue().toString();
                        UID = dataSnapshot.child("id").getValue().toString();
                        String email = dataSnapshot.child("emailAddress").getValue().toString();
                        String sportsmanship = dataSnapshot.child("avgSportsmanshipRating").getValue().toString();
                        usernameText.setText(name);
                        followersText.setText(String.valueOf(0));
                        followingText.setText(String.valueOf(0));
                        sportsmanshipBar.setRating(Integer.parseInt(sportsmanship));
                        Picasso.get().load(photo).into(profilePicture);
                        setUpExp(Sport.fromString("1v1 Basketball"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void setUpExp(Sport s) {
        System.out.println(s.name());
        viewModel.getExp(s, UID).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                expText.setText(s);
            }
        });
    }


}
