package com.CS5520.athletier.ui.Challenges.Rating;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.LogTags;
import com.squareup.picasso.Picasso;

public class RateUserActivity extends AppCompatActivity {

    private RateUserActivityViewModel viewModel;
    private ImageView opponentImageView;
    private TextView opponentNameText;
    private RatingBar sportsmanshipBar;
    private RecyclerView addBadgeRecyclerView;
    private AddBadgeRecyclerAdapter adapter;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);
        viewModel = ViewModelProviders.of(this).get(RateUserActivityViewModel.class);

        // Parse the selected Challenge from the Intent that started this Activity
        Challenge challenge = getIntent().getParcelableExtra(Challenge.challengeKey);
        if (challenge != null) {
            viewModel.setChallenge(challenge);
            setupUserObserver();
            setupActionBar();
            findViews();
            setupRatingBar();
            setupAddBadgeRecyclerView(challenge.getSport());
            setupDoneButton();
        } else {
            Log.i(LogTags.USER_RATING_ERROR,
                    "Passed challenge in RateUserActivity is null...");
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_slide, R.anim.slide_down);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.rate_opponent);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
    }

    private void findViews() {
        opponentImageView = findViewById(R.id.opponentImageView);
        opponentNameText = findViewById(R.id.opponentNameTextView);
        sportsmanshipBar = findViewById(R.id.rateOpponentSportsmanshipBar);
        addBadgeRecyclerView = findViewById(R.id.addBadgeRecycler);
        doneButton = findViewById(R.id.rateUserDoneButton);
    }

    private void setupAddBadgeRecyclerView(String sportString) {
        Sport sport = Sport.fromString(sportString);
        if (sport != null) {
            adapter = new AddBadgeRecyclerAdapter(sport);
            addBadgeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            addBadgeRecyclerView.setAdapter(adapter);
        }
    }

    private void setupRatingBar() {
        sportsmanshipBar.setStepSize(1);
        sportsmanshipBar.setRating(5);
    }

    private void setupDoneButton() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addRatingAndBadges(
                        sportsmanshipBar.getRating(),
                        adapter.getAwardedBadges()
                );
                finish();
            }
        });
    }

    private void setupUserObserver() {
        viewModel.getUserToRate().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUsernameAndPhoto(user.getUsername(), user.getPhotoUrl());
            }
        });
    }

    private void setUsernameAndPhoto(String username, String photoUrl) {
        opponentNameText.setText(username);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(opponentImageView);
        }
    }


}
