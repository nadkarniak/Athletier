package com.CS5520.athletier.ui.Map;

import android.content.Intent;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeKeys;

import java.util.ArrayList;
import java.util.List;

public class MapTabViewModel extends ViewModel {

    private Location userLocation;
    private MutableLiveData<List<Location>> challengeLocations = new MutableLiveData<>();
    private MutableLiveData<List<Challenge>> challenges = new MutableLiveData<>();

    void setUserLocation(Location location) {
        userLocation = location;
    }

    void addCreatedChallenge(Intent challengeData) {
        Challenge newChallenge = challengeData.getParcelableExtra(
                CreateChallengeKeys.CREATED_CHALLENGE);
        if (newChallenge != null) {
            List<Challenge> currentChallenges = challenges.getValue() != null ?
                    challenges.getValue() : new ArrayList<Challenge>();

            currentChallenges.add(newChallenge);
            challenges.postValue(currentChallenges);
        }
    }

    Location getUserLocation() {
        return userLocation;
    }

    LiveData<List<Location>> getChallengeLocations() {
        // TODO: Query challenges from Firebase and set challenges = to the returned stream of live
        //  data
        return challengeLocations;
    }

    public LiveData<List<Challenge>> getMapChallenges() { return challenges; }


}