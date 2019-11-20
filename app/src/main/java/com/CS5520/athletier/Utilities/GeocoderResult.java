package com.CS5520.athletier.Utilities;

import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class GeocoderResult {
    private LatLng latLng;
    private boolean geocodingSucceeded;

    public GeocoderResult(LatLng latLng, boolean geocodingSucceeded) {
        this.latLng = latLng;
        this.geocodingSucceeded = geocodingSucceeded;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public boolean getGeocodingSucceeded() {
        return geocodingSucceeded;
    }
}
