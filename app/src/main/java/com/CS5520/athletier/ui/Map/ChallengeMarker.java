package com.CS5520.athletier.ui.Map;

import com.CS5520.athletier.Models.Challenge;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class ChallengeMarker {
    private List<Challenge> challenge;
    private Marker marker;

    public ChallengeMarker(List<Challenge> challenge) {
        this.challenge = challenge;
    }
}
