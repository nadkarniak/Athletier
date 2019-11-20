package com.CS5520.athletier.ui.Map.CreateChallenge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.State;

import java.util.Date;
import java.util.List;

public class CreateChallengeFormViewModel extends ViewModel {
    // Indicates if all of the required inputs in the form have been filled in. The required fields
    // only include the text inputs because a default value is provided for the spinner and date
    // input fields.
    private MutableLiveData<Boolean> hasRequiredFields = new MutableLiveData<>(false);
    private Sport sport;
    private State state;
    private Date date;


    public CreateChallengeFormViewModel() { }

    LiveData<Boolean> getHasRequiredFields() {
        return hasRequiredFields;
    }

    void setHasRequiredFields(List<Boolean> hasFields) {
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

    void setSelectedDate(Date date) {
        this.date = date;
    }

    Sport getSport() {
        return this.sport;
    }

    State getState() {
        return this.state;
    }

    Date getDate() {
        return this.date;
    }

}
