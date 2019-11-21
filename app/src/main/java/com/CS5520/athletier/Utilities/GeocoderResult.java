package com.CS5520.athletier.Utilities;

import com.CS5520.athletier.Models.State;
import com.google.android.gms.maps.model.LatLng;

public class GeocoderResult {
    private GeocoderInput input;
    private LatLng latLng;
    private boolean geocodingSucceeded;

    public GeocoderResult(GeocoderInput input, LatLng latLng, boolean geocodingSucceeded) {
        this.input = input;
        this.latLng = latLng;
        this.geocodingSucceeded = geocodingSucceeded;
    }

    public String getInputStreet() {
        return input.getStreetAddress();
    }

    public String getInputCity() {
        return input.getCity();
    }

    public State getInputState() {
        return input.getState();
    }

    public String getInputZipCode() {
        return input.getZipCode();
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public boolean getGeocodingSucceeded() {
        return geocodingSucceeded;
    }
}
