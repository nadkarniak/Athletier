package com.CS5520.athletier.ui.Challenges;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;

public class SegmentedSelectorFragment extends Fragment {

    private SegmentedSelectorViewModel viewModel;
    private ToggleButton leftButton;
    private ToggleButton middleButton;
    private ToggleButton rightButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_segmented_selector, container, false);
        leftButton = view.findViewById(R.id.leftButton);
        middleButton = view.findViewById(R.id.middleButton);
        rightButton = view.findViewById(R.id.rightButton);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SegmentedSelectorViewModel.class);
        setupButtonListeners();

        // Default to left-most button as checked
        leftButton.setChecked(true);
        viewModel.setSelectedPosition(0);
        styleButtonText(leftButton, middleButton, rightButton);
    }

    void setButtonTitles(String left, String middle, String right) {
        setOnOffTextForButton(leftButton, left);
        setOnOffTextForButton(middleButton, middle);
        setOnOffTextForButton(rightButton, right);
    }

    LiveData<Integer> getSelectedPosition() {
        if (viewModel != null) {
            return viewModel.getSelectedPosition();
        } else {
            return null;
        }
    }

    private void setOnOffTextForButton(ToggleButton button, String text) {
        button.setTextOn(text);
        button.setTextOff(text);
        button.setText(text);
    }

    private void setupButtonListeners() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSelectedPosition(0);
                uncheckButtons(middleButton, rightButton);
                styleButtonText(leftButton, middleButton, rightButton);
            }
        });

        middleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSelectedPosition(1);
                uncheckButtons(leftButton, rightButton);
                styleButtonText(leftButton, middleButton, rightButton);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSelectedPosition(2);
                uncheckButtons(leftButton, middleButton);
                styleButtonText(leftButton, middleButton, rightButton);
            }
        });
    }

    private void uncheckButtons(ToggleButton...buttons) {
        for (ToggleButton button : buttons) {
            button.setChecked(false);
        }
    }

    private void styleButtonText(ToggleButton... buttons) {
        for (ToggleButton button : buttons) {
            if (button.isChecked()) {
                button.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                button.setTypeface(Typeface.DEFAULT);
            }
        }
    }
}
