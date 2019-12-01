package com.CS5520.athletier.ui.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ChallengeStatus;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Search.FindUser.FindUserActivity;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

public class ChallengeCellFragment extends Fragment {

    // View Model
    private ChallengeCellViewModel viewModel;

    // Views
    private ImageView imageView;
    private TextView hostNameText;
    private Chip sportChip;
    private TextView dateText;
    private TextView addressText;
    private TextView statusText;
    private Button viewProfileButton;
    private Button joinButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenge_cell, container, false);
        imageView = view.findViewById(R.id.userImageView);
        hostNameText = view.findViewById(R.id.usernameText);
        sportChip = view.findViewById(R.id.sportChip);
        dateText = view.findViewById(R.id.dateText);
        addressText = view.findViewById(R.id.addressText);
        statusText = view.findViewById(R.id.statusText);
        viewProfileButton = view.findViewById(R.id.cellLeftButton);
        joinButton = view.findViewById(R.id.cellRightButton);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChallengeCellViewModel.class);
        setupObservers();
        setupButtons();
    }

    void setCurrentChallenge(Challenge challenge) {
        viewModel.setCurrentChallenge(challenge);
    }

    private void setupObservers() {
        viewModel.getCurrentChallenge().observe(getViewLifecycleOwner(), new Observer<Challenge>() {
            @Override
            public void onChanged(final Challenge challenge) {
                Context context = getContext();
                if (context == null) { return; }
                Sport sport = Sport.fromString(challenge.getSport());
                sportChip.setText(challenge.getSport());
                if (sport != null) {
                    sportChip.setChipIcon(ContextCompat.getDrawable(getContext(), sport.getIconId()));
                }
                dateText.setText(challenge.getFormattedDate());
                addressText.setText(challenge.getFormattedAddress());
                statusText.setText(ChallengeStatus.valueOf(challenge.getChallengeStatus()).toString());

                // Disable join button if selected challenge is hosted by the current user or
                // challenge is already full
                joinButton.setEnabled(!challenge.getHostId().equals(viewModel.getCurrentUserId()) ||
                        challenge.getAcceptanceStatus().equals(AcceptanceStatus.ACCEPTED.name())
                );
                joinButton.getBackground().setAlpha(joinButton.isEnabled() ? 255 : 128);
            }
        });

        viewModel.getCurrentHostUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String photoUrl = user.getPhotoUrl();
                if (photoUrl != null) {
                    Picasso.get().load(photoUrl).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.ic_person_black_24dp);
                }
                hostNameText.setText(user.getUsername());
            }
        });
    }

    private void setupButtons() {
        joinButton.setText(R.string.join_request_title);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.joinChallenge();
            }
        });

        viewProfileButton.setText(R.string.view_profile_title);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch FindUserActivity for selected host user
                Activity currentActivity = getActivity();
                String hostEmail = viewModel.getHostUserEmail();
                if (currentActivity != null && hostEmail != null) {
                    Intent intent = new Intent(currentActivity, FindUserActivity.class);
                    intent.putExtra("email", hostEmail);
                    startActivity(intent);
                    currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
                }
            }
        });
    }
}
