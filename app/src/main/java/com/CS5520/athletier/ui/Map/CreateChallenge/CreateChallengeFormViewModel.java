package com.CS5520.athletier.ui.Map.CreateChallenge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.State;

public class CreateChallengeFormViewModel extends ViewModel {
    // Indicates if all of the required inputs in the form have been filled in
    private MutableLiveData<Boolean> hasRequiredFields = new MutableLiveData<>(false);
    private Sport sport;
    private State state;


    public CreateChallengeFormViewModel() { }

    LiveData<Boolean> getHasRequiredFields() {
        return hasRequiredFields;
    }

    void setHasRequiredFields(boolean... hasFields) {
        boolean hasAllFields = false;
        for (boolean hasField : hasFields) {
            hasAllFields = hasField;
            if (!hasAllFields) {
                break;
            }
        }
        hasRequiredFields.setValue(hasAllFields);
    }

    void setSelectedSport(String sportName) {
        this.sport = Sport.fromString(sportName);
    }

    void setSelectedState(String stateName) {
        this.state = State.valueOf(stateName);
    }

}
