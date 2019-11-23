package com.CS5520.athletier.ui.Map.CreateChallenge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.State;
import com.CS5520.athletier.Models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class CreateChallengeActivityViewModel extends ViewModel {
    private User currentUser;
    private MutableLiveData<Challenge> createdChallenge = new MutableLiveData<>();


    void setCurrentUser(String userId) {
        // TODO: Get User from Firebase using input userId
        this.currentUser = new User();
    }

    void makeChallenge(Sport sport,
                       Date date,
                       String streetAddress,
                       String city, State state,
                       String zipCode,
                       LatLng latLng) {

        if (currentUser == null) {
            return;
        }

        // Create challenge and pass to createdChallenge LiveData
        Challenge newChallenge = new Challenge(
                currentUser.getId(),
                sport,
                date,
                streetAddress,
                city,
                state,
                zipCode,
                latLng.latitude,
                latLng.longitude
        );

        createdChallenge.setValue(newChallenge);
        // TODO: Save created challenge to Firebase

    }

    LiveData<Challenge> getCreatedChallenge() {
        return createdChallenge;
    }


}
