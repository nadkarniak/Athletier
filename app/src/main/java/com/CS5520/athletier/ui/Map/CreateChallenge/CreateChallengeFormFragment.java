package com.CS5520.athletier.ui.Map.CreateChallenge;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.State;
import com.CS5520.athletier.R;
import com.CS5520.athletier.ui.Map.SpinnerInputFragment;
import com.CS5520.athletier.ui.Map.TextInputFragment;


public class CreateChallengeFormFragment extends Fragment {

    // View Model
    private CreateChallengeFormViewModel viewModel;

    // Views
    private SpinnerInputFragment sportSpinnerInput;
    private TextInputFragment streetInput;
    private TextInputFragment cityInput;
    private SpinnerInputFragment stateSpinnerInput;
    private TextInputFragment zipCodeInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_challenge_form, container, false);
        findChildFragments();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CreateChallengeFormViewModel.class);
        setupSpinnerInputs(getContext());
        setupTextInputs();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupSpinnerObservers();
        setupTextInputObservers();
    }

    public LiveData<Boolean> getHasRequiredFields() {
        return viewModel.getHasRequiredFields();
    }

    private void findChildFragments() {
        FragmentManager manager = getChildFragmentManager();
        sportSpinnerInput = (SpinnerInputFragment) manager.findFragmentById(R.id.sportSpinnerFragment);
        streetInput = (TextInputFragment) manager.findFragmentById(R.id.streetAddressInputFragment);
        cityInput = (TextInputFragment) manager.findFragmentById(R.id.cityInputFragment);
        stateSpinnerInput = (SpinnerInputFragment) manager.findFragmentById(R.id.stateSpinnerFragment);
        zipCodeInput = (TextInputFragment) manager.findFragmentById(R.id.zipCodeInputFragment);
    }

    private void setupSpinnerInputs(Context context) {
        sportSpinnerInput.setLabelText("Sport:");
        sportSpinnerInput.setSpinnerOptions(context, Sport.getAllSportsNames());

        stateSpinnerInput.setLabelText("State:");
        stateSpinnerInput.setSpinnerOptions(context, State.getAllStateNames());
    }

    private void setupTextInputs() {
        streetInput.setTitleTextView("Street Address:");
        streetInput.setInputEditTextType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

        cityInput.setTitleTextView("City:");
        cityInput.setInputEditTextType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

        zipCodeInput.setTitleTextView("Zip:");
        zipCodeInput.setInputEditTextType(InputType.TYPE_CLASS_PHONE);
    }

    private void setupSpinnerObservers() {
        sportSpinnerInput.getSelectedItem().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String sportName) {
                viewModel.setSelectedSport(sportName);
            }
        });

        stateSpinnerInput.getSelectedItem().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String stateName) {
                viewModel.setSelectedState(stateName);
            }
        });
    }

    private void setupTextInputObservers() {
        setupObserverForTextInput(streetInput, cityInput, zipCodeInput);
        setupObserverForTextInput(cityInput, streetInput, zipCodeInput);
        setupObserverForTextInput(zipCodeInput, streetInput, cityInput);

    }

    private void setupObserverForTextInput(TextInputFragment primaryInput,
                                           final TextInputFragment accessoryInputOne,
                                           final TextInputFragment accessoryInputTwo) {
        primaryInput.getHasNonEmptyInput().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean primaryHasInput) {
                if (accessoryInputOne.getHasNonEmptyInput().getValue() == null ||
                        accessoryInputTwo.getHasNonEmptyInput().getValue() == null) {
                    viewModel.setHasRequiredFields(false);
                } else {
                    viewModel.setHasRequiredFields(
                            primaryHasInput,
                            accessoryInputOne.getHasNonEmptyInput().getValue(),
                            accessoryInputTwo.getHasNonEmptyInput().getValue()
                    );
                }
            }
        });
    }

}
