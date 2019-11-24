package com.CS5520.athletier.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Challenge implements Parcelable {
    private String hostId;
    private String hostName;
    private String opponentId;
    private String opponentName;

    private String sport;
    private String challengeStatus;
    private String acceptanceStatus;
    private String resultStatus;
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
                     String hostName,
                     String opponentId,
                     String opponentName,
                     Sport sport,
                     ChallengeStatus challengeStatus,
                     AcceptanceStatus acceptanceStatus,
                     ResultStatus resultStatus,
                     boolean hostIsWinner,
                     Date date,
                     double longitude,
                     double latitude,
                     String streetName,
                     String city,
                     State state,
                     String zip) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.opponentId = opponentId;
        this.opponentName = opponentName;
        this.sport = sport.toString();
        this.challengeStatus = challengeStatus.name();
        this.acceptanceStatus = acceptanceStatus.name();
        this.resultStatus = resultStatus.name();
        this.hostIsWinner = hostIsWinner;
        this.date = date.getTime();
        this.longitude = longitude;
        this.latitude = latitude;
        this.streetName = streetName;
        this.city = city;
        this.state = state.name();
        this.zip = zip;
    }

    // Constructor for newly created challenge from Map Screen
    public Challenge(String hostId,
                     String hostName,
                     Sport sport,
                     Date date,
                     String streetName,
                     String city,
                     State state,
                     String zip,
                     double latitude,
                     double longitude) {
        this.hostId = hostId;
        this.hostName = hostName;
        this.sport = sport.toString();
        this.challengeStatus = ChallengeStatus.AWAITING_PLAYERS.name();
        this.acceptanceStatus = AcceptanceStatus.PENDING.name();
        this.resultStatus = ResultStatus.NONE.name();
        this.date = date.getTime();
        this.streetName = streetName;
        this.city = city;
        this.state = state.name();
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Challenge(Parcel in) {
        hostId = in.readString();
        opponentId = in.readString();
        sport = in.readString();
        challengeStatus = in.readString();
        acceptanceStatus = in.readString();
        resultStatus = in.readString();
        hostIsWinner = in.readByte() != 0;
        date = in.readLong();
        longitude = in.readDouble();
        latitude = in.readDouble();
        streetName = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
    }

    // Public Getters

    public String getHostId() { return hostId; }

    public String getHostName() { return hostName; }

    public String getOpponentId() { return opponentId; }

    public String getOpponentName() { return opponentName; }

    public String getSport() { return sport; }

    public String getChallengeStatus() { return challengeStatus; }

    public String getAcceptanceStatus() { return acceptanceStatus; }

    public String getResultStatus() { return resultStatus; }

    public boolean getHostIsWinner() { return hostIsWinner; }

    public long getDate() { return date; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }

    public String getStreetName() { return streetName; }

    public String getCity() { return city; }

    public String getState() { return  state; }

    public String getZip() { return zip; }

    public String getFormattedDate() {
        DateFormat formatter = new SimpleDateFormat("MM-dd-YYYY", Locale.US);
        Date dateObj = new Date(this.date);
        return formatter.format(dateObj);
    }

    public String getFormattedAddress() {
        return streetName + "\n" + city + ", " + state + ", " + zip;
    }

    // Public Setters

    public void setChallengeStatus(ChallengeStatus status) {
        this.challengeStatus = status.name();
    }

    public void setAcceptanceStatus(AcceptanceStatus status) {
        this.acceptanceStatus = status.name();
    }

    public void setResultStatus(ResultStatus status) {
        this.resultStatus = status.name();
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

    @Override
    public boolean equals(Object other) {
        if (other == this) { return true; }
        if (other instanceof Challenge) {
            Challenge otherChallenge = (Challenge) other;
            return this.hostId.equals(otherChallenge.hostId)
                    && this.opponentId.equals(otherChallenge.hostId)
                    && this.sport.equals(otherChallenge.sport)
                    && this.hostIsWinner == otherChallenge.hostIsWinner
                    && this.challengeStatus.equals(otherChallenge.challengeStatus)
                    && this.acceptanceStatus.equals(otherChallenge.acceptanceStatus)
                    && this.resultStatus.equals(otherChallenge.resultStatus)
                    && this.streetName.equals(otherChallenge.streetName)
                    && this.city.equals(otherChallenge.city)
                    && this.zip.equals(otherChallenge.zip)
                    && this.state.equals(otherChallenge.state)
                    && this.date == otherChallenge.date;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.hostId,
                this.opponentId,
                this.sport,
                this.challengeStatus,
                this.acceptanceStatus,
                this.resultStatus,
                this.date,
                this.streetName,
                this.city,
                this.state,
                this.zip);
    }


    // Parcelable implementation so Challenges can be passed between Activities and Fragments
    public static final Creator<Challenge> CREATOR = new Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hostId);
        parcel.writeString(opponentId);
        parcel.writeString(sport);
        parcel.writeString(challengeStatus);
        parcel.writeString(acceptanceStatus);
        parcel.writeString(resultStatus);
        parcel.writeByte((byte) (hostIsWinner ? 1 : 0));
        parcel.writeLong(date);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(streetName);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(zip);
    }
}
