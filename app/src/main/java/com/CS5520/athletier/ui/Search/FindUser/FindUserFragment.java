package com.CS5520.athletier.ui.Search.FindUser;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsBadge;
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

import javax.annotation.Nullable;

public class FindUserFragment extends Fragment {

    private FindUserViewModel viewModel;
    private Button follow;
    private String email;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRef;
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


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = getArguments().getString("email");
        View view = inflater.inflate(R.layout.fragment_find_user, container, false);
        setupViews(view);
        Log.i("email", email);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FindUserViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        viewModel.findUserWithEmail(email);
        setupFollowButtonListener();
        setObservers();
        setupSportsList(getContext());
        setupBadges(getContext());
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

    private void setupBadges(Context context) {
        List<SportsBadge> badges = getBadgeList(context);
        firstBadge.setImageResource(badges.get(0).getResId());
        secondBadge.setImageResource(badges.get(1).getResId());
        thirdBadge.setImageResource(badges.get(2).getResId());
        fourthBadge.setImageResource(badges.get(3).getResId());
        fifthBadge.setImageResource(badges.get(4).getResId());
    }

    private void setupViews(View view) {
        // Find views using id's
        usernameText = ((LinearLayout)view).findViewById(R.id.userName);
        recordText = ((LinearLayout)view).findViewById(R.id.record);
        followersText = ((LinearLayout)view).findViewById(R.id.followers);
        followingText = ((LinearLayout)view).findViewById(R.id.following);
        sportsmanshipBar = ((LinearLayout)view).findViewById(R.id.ratingBar);
        sportsList = ((LinearLayout)view).findViewById(R.id.sportsSpinner);
        firstBadge = ((LinearLayout)view).findViewById(R.id.first_badge);
        secondBadge = ((LinearLayout)view).findViewById(R.id.second_badge);
        thirdBadge = ((LinearLayout)view).findViewById(R.id.third_badge);
        fourthBadge = ((LinearLayout)view).findViewById(R.id.fourth_badge);
        fifthBadge = ((LinearLayout)view).findViewById(R.id.fifth_badge);
        profilePicture = ((LinearLayout)view).findViewById(R.id.profilePic);
        follow = view.findViewById(R.id.follow);
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

    private void setObservers() {
        viewModel.getOtherUid().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("uid", s);
                mRef = FirebaseDatabase.getInstance().getReference().child("users").child(s);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("username").getValue().toString();
                        String photo = dataSnapshot.child("photoUrl").getValue().toString();
                        String uid = dataSnapshot.child("id").getValue().toString();
                        String email = dataSnapshot.child("emailAddress").getValue().toString();
                        String record = dataSnapshot.child("record").getValue().toString();
                        String sportsmanship = dataSnapshot.child("avgSportsmanshipRating").getValue().toString();
                        usernameText.setText(name);
                        recordText.setText(record);
                        followersText.setText(String.valueOf(0));
                        followingText.setText(String.valueOf(0));
                        sportsmanshipBar.setRating(Integer.parseInt(sportsmanship));
                        Picasso.get().load(photo).into(profilePicture);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
