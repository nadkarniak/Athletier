package com.CS5520.athletier.Models;

import android.content.Context;
import android.location.Geocoder;

public class Location {
    private double longitude;
    private double latitude;
    private String streetName;
    private String city;
    private State state;
    private String zip;

    public Location(String streetName,
                    String city,
                    State state,
                    String zip) {
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    private void setLatAndLong(Context context) {
        Geocoder geocoder = new Geocoder(context);
    }

}
