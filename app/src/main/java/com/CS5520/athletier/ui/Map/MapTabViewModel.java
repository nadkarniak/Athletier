package com.CS5520.athletier.ui.Map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MapTabViewModel extends ViewModel {

    private Location userLocation;
    private MutableLiveData<List<Location>> challengeLocations;


    public void setUserLocation(Location location) {
        userLocation = location;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public LiveData<List<Location>> getChallengeLocations() {
        return challengeLocations;
    }


}