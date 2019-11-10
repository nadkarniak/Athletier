package com.CS5520.athletier.ui.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationRequester {
    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;
    public static final String requiredPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    public LocationRequester(Context context, final LocationUpdateListener listener) {
        this.locationClient = LocationServices.getFusedLocationProviderClient(context);
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location: locationResult.getLocations()) {
                        listener.updateWithLocation(location);
                    }
                }
            }
        };
    }

    public void startUpdatingLocation(LocationRequest request) {
        locationClient.requestLocationUpdates(
                    request, locationCallback, Looper.getMainLooper()
        );
    }

    public void stopUpdatingLocation() {
        locationClient.removeLocationUpdates(locationCallback);
    }

}
