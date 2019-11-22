package com.CS5520.athletier.Utilities;

import com.CS5520.athletier.Models.State;

public class GeocoderInput {
    private String streetAddress;
    private String city;
    private State state;
    private String zipCode;

    public GeocoderInput(String streetAddress, String city, State state, String zipCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public State getState() {
        return this.state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getLocationName() {
        return streetAddress + ", " + city + ", " + state.name() + ", " + zipCode;
    }
}
