package com.CS5520.athletier.ui.Map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.LocationRequest;

public class MapTabViewModel extends ViewModel {

    private Location userLocation;



    public void setUserLocation(Location location) {
        userLocation = location;
    }

    public Location getUserLocation() {
        return userLocation;
    }


}