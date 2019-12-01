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
    private ChallengeCellFragment cellFragment;

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
    }

    private void setupFragments() {
        cellFragment = (ChallengeCellFragment)
                getChildFragmentManager().findFragmentById(R.id.challengeCellFragment);
    }

    private void setupObservers() {
        viewModel.getSelectedChallenge().observe(getViewLifecycleOwner(), new Observer<Challenge>() {
            @Override
            public void onChanged(Challenge challenge) {
                setPreviousAndNextButtonVisibility();
                cellFragment.setCurrentChallenge(challenge);
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
