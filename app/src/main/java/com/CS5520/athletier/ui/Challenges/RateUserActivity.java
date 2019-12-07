package com.CS5520.athletier.ui.Challenges;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.LogTags;

public class RateUserActivity extends AppCompatActivity {

    private ImageView opponentImageView;
    private TextView opponentNameText;
    private RatingBar sportsmanshipBar;
    private RecyclerView addBadgeRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);
        Challenge challenge = savedInstanceState.getParcelable(Challenge.challengeKey);
        if (challenge == null) {
            Log.i(LogTags.ERROR, "Passed challenge in RateUserActivity is null...");
            finish();
        }

        setupActionBar();

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

    private void setupViews() {
        opponentImageView = findViewById(R.id.opponentImageView);
        opponentNameText = findViewById(R.id.opponentNameTextView);
        sportsmanshipBar = findViewById(R.id.rateOpponentSportsmanshipBar);
        addBadgeRecyclerView = findViewById(R.id.addBadgeRecycler);

    }



}
