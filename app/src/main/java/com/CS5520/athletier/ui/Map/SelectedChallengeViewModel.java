package com.CS5520.athletier.ui.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.User;

import java.util.List;

public class SelectedChallengeViewModel extends ViewModel {
    private int selectedChallengeIndex = 0;
    private MutableLiveData<Challenge> selectedChallenge = new MutableLiveData<>();
    private MutableLiveData<User> selectedChallengeHost = new MutableLiveData<>();

    // MutableLiveData containing a List of all Challenges at the selected marker location
    private List<Challenge> challengesAtLocation;

    void setChallengesAtLocation(List<Challenge> challenges) {
        challengesAtLocation = challenges;

        if (challenges != null && challenges.size() > 0) {
            // Default selected challenge to the first
            selectedChallengeIndex = 0;
            selectedChallenge.setValue(challenges.get(selectedChallengeIndex));
            // TODO: Get the user for the selected challenge  using the user's Id
        }
    }

    void selectNextChallenge() {
        if (challengesAtLocation!= null) {
            selectedChallengeIndex = (selectedChallengeIndex + 1) % challengesAtLocation.size();
            selectedChallenge.setValue(challengesAtLocation.get(selectedChallengeIndex));
        }
    }

    void selectPreviousChallenge() {
        if (challengesAtLocation!= null) {
            if (selectedChallengeIndex - 1 < 0) {
                selectedChallengeIndex = challengesAtLocation.size() - 1;
            } else {
                selectedChallengeIndex -= 1;
            }
            selectedChallenge.setValue(challengesAtLocation.get(selectedChallengeIndex));
        }
    }

    LiveData<Challenge> getSelectedChallenge() {
        return selectedChallenge;
    }

    int getNumberOfChallengesAtLocation() {
        return challengesAtLocation.size();
    }

    int getSelectedChallengeIndex() {
        return selectedChallengeIndex;
    }

}
