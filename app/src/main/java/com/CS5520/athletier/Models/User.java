package com.CS5520.athletier.Models;

import java.util.HashMap;
import java.util.List;

public class User {

    // TODO: From Firebase
    private String id;
    private String authToken;

    private String username;
    private String photoUrl;

    private int totalSportmanshipRating;
    private List<Challenge> challenges;
    private HashMap<Sport, Integer> rankings;
    private HashMap<Sport, Integer> sportsExperienceMap;
    private HashMap<SportsBadge, Integer> badges;

    public float getAvgSportsmanshipRating() {
        if (challenges.size() == 0) {
            return 0;
        }
        return totalSportmanshipRating / challenges.size();
    }

}
