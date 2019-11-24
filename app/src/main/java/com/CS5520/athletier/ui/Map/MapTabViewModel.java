package com.CS5520.athletier.ui.Map;

import android.content.Intent;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeKeys;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;
import java.util.List;

public class MapTabViewModel extends ViewModel {
    private Location userLocation;
    private MutableLiveData<List<Challenge>> challenges = new MutableLiveData<>();

    void setChallenges(List<Challenge> newChallenges) {
        challenges.postValue(newChallenges);
    }

    void setUserLocation(Location location) {
        userLocation = location;
    }

    Location getUserLocation() {
        return userLocation;
    }

    LiveData<List<Challenge>> getMapChallenges() {
        return challenges;
    }

    List<Challenge> getChallengesAtLatLng(LatLng latLng) {
        if (challenges.getValue() == null) {
            return null;
        }

        List<Challenge> challengesAtLatLng = new ArrayList<>();
        for (Challenge challenge : challenges.getValue()) {
            if (challenge.getLatitude() == latLng.latitude
                    && challenge.getLongitude() == latLng.longitude) {
                challengesAtLatLng.add(challenge);
            }
        }

        return challengesAtLatLng;
    }
}