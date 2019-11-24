package com.CS5520.athletier.ui.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.R;
import java.util.List;

public class SelectedChallengeFragment extends Fragment {
    private SelectedChallengeViewModel viewModel;

    private TextView titleTextView;
    private Button previousButton;
    private Button nextButton;
    private TextDisplayFragment hostUserTextFrag;
    private TextDisplayFragment sportTextFrag;
    private TextDisplayFragment dateDisplayFrag;
    private TextDisplayFragment addressTextFrag;
    private TextDisplayFragment statusTextFrag;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_challenge, container,
                false);
        setupViews(view);
        setupFragments();
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SelectedChallengeViewModel.class);
        setupObservers();
    }

    void setChallengesAtSelectedLocation(List<Challenge> challenges) {
        viewModel.setChallengesAtLocation(challenges);
    }

    private void setupViews(View view) {
        titleTextView = view.findViewById(R.id.selectedChallengeTitleText);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.selectPreviousChallenge();
                titleTextView.setText(viewModel.getChallengeTitle());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.selectNextChallenge();
                titleTextView.setText(viewModel.getChallengeTitle());
            }
        });


        Button joinButton = view.findViewById(R.id.joinRequestButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request to join challenge
            }
        });

        Button viewProfileButton = view.findViewById(R.id.viewHostProfileButton);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // View host profile
            }
        });
    }

    private void setupFragments() {
        FragmentManager manager = getChildFragmentManager();
        hostUserTextFrag = (TextDisplayFragment) manager.findFragmentById(R.id.hostUserTextDisplay);
        sportTextFrag = (TextDisplayFragment) manager.findFragmentById(R.id.sportTextDisplay);
        dateDisplayFrag = (TextDisplayFragment) manager.findFragmentById(R.id.dateTextDisplay);
        addressTextFrag = (TextDisplayFragment) manager.findFragmentById(R.id.addressTextDisplay);
        statusTextFrag = (TextDisplayFragment) manager.findFragmentById(R.id.statusTextDisplay);

        hostUserTextFrag.setTitleText(getResources().getString(R.string.host_title));
        sportTextFrag.setTitleText(getResources().getString(R.string.sport_title));
        dateDisplayFrag.setTitleText(getResources().getString(R.string.date_title));
        addressTextFrag.setTitleText(getResources().getString(R.string.address_title));
        statusTextFrag.setTitleText(getResources().getString(R.string.status_title));

        hostUserTextFrag.setDetailsText("");
        sportTextFrag.setDetailsText("");
        dateDisplayFrag.setDetailsText("");
        addressTextFrag.setDetailsText("");
        statusTextFrag.setDetailsText("");
    }

    private void setupObservers() {
        viewModel.getSelectedChallenge().observe(getViewLifecycleOwner(), new Observer<Challenge>() {
            @Override
            public void onChanged(Challenge challenge) {
                setPreviousAndNextButtonVisibility();

                // Update the displayed challenge information once a challenge is selected
                hostUserTextFrag.setDetailsText(challenge.getHostName());
                sportTextFrag.setDetailsText(challenge.getSport());
                dateDisplayFrag.setDetailsText(challenge.getFormattedDate());
                addressTextFrag.setDetailsText(challenge.getFormattedAddress());
                statusTextFrag.setDetailsText(challenge.getChallengeStatus());
            }
        });
    }

    private void setPreviousAndNextButtonVisibility() {
        titleTextView.setText(viewModel.getChallengeTitle());
        if (viewModel.getNumberOfChallengesAtLocation() > 1) {
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setEnabled(false);
            nextButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }
    }
}
