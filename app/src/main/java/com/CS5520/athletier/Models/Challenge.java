package com.CS5520.athletier.Models;

import java.util.Date;

public class Challenge {
    private String hostId;
    private String opponentId;

    private String sport;
    private String challengeStatus;
    private String acceptanceStatus;
    private boolean hostIsWinner;

    // Store as time in milliseconds
    private long date;

    // Location
    private double longitude;
    private double latitude;
    private String streetName;
    private String city;
    private String state;
    private String zip;

    // Constructor needed to parse object from Firebase
    public Challenge() { }

    public Challenge(String hostId,
                     String opponentId,
                     String sport,
                     ChallengeStatus challengeStatus,
                     AcceptanceStatus acceptanceStatus,
                     boolean hostIsWinner,
                     Date date,
                     double longitude,
                     double latitude,
                     String streetName,
                     String city,
                     State state,
                     String zip) {
        this.hostId = hostId;
        this.opponentId = opponentId;
        this.sport = sport;
        this.challengeStatus = challengeStatus.name();
        this.acceptanceStatus = acceptanceStatus.name();
        this.hostIsWinner = hostIsWinner;
        this.date = date.getTime();
        this.longitude = longitude;
        this.latitude = latitude;
        this.streetName = streetName;
        this.city = city;
        this.state = state.name();
        this.zip = zip;
    }


    // Public Getters

    public String getHostId() { return hostId; }

    public String getOpponentId() { return opponentId; }

    public String getSport() { return sport; }

    public String getChallengeStatus() { return challengeStatus; }

    public String getAcceptanceStatus() { return acceptanceStatus; }

    public boolean getHostIsWinner() { return hostIsWinner; }

    public long getDate() { return date; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }

    public String getStreetName() { return streetName; }

    public String getCity() { return city; }

    public String getState() { return  state; }

    public String getZip() { return zip; }

    // Public Setters

    public void setChallengeStatus(ChallengeStatus status) {
        this.challengeStatus = status.name();
    }

    public void setAcceptanceStatus(AcceptanceStatus status) {
        this.acceptanceStatus = status.name();
    }

    public void setHostIsWinner(boolean hostIsWinner) {
        this.hostIsWinner = hostIsWinner;
    }

    public void setDate(Date date) {
        this.date = date.getTime();
    }

    public void setLocation(String streetName,
                            String city,
                            String state,
                            String zip,
                            double latitude,
                            double longitude) {
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
