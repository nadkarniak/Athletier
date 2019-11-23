package com.CS5520.athletier.ui.Map;

import com.CS5520.athletier.Models.Challenge;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeMarker {
    private List<Challenge> challengesAtLocation;
    private Marker marker;

    public ChallengeMarker(Marker marker, Challenge... challenges) {
        this.marker = marker;
        this.challengesAtLocation = new ArrayList<>();
        challengesAtLocation.addAll(Arrays.asList(challenges));
    }

    public void addChallengeToMarker(Challenge challenge) {
        challengesAtLocation.add(challenge);
    }

    public void removeChallenge(Challenge challenge) {
        challengesAtLocation.remove(challenge);
    }

    public List<Challenge> getChallengesAtLocation() {
        return challengesAtLocation;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public void removeMarker() {
        this.marker.remove();
        this.challengesAtLocation = null;
    }

}
