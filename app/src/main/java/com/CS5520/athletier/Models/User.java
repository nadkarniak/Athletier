package com.CS5520.athletier.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    // TODO: From Firebase
    private String id;
    private String username;
    private String photoUrl;
    private int totalSportmanshipRating;

    private List<Challenge> challenges = new ArrayList<>();
    private List<SportsAchievementSummary> sportsAchievementSummaries = new ArrayList<>();

    private List<User> following = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    private int wins;
    private int losses;

    // Empty public constructor required by Firebase
    public User() { }

    public float getAvgSportsmanshipRating() {
        // TODO: We should only be counting Challenges that are completed where the user has been rated
        if (challenges.size() == 0) {
            return 0;
        }
        return 5;
        //return totalSportmanshipRating / challenges.size();
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getRecord(){
        String record = wins + " - " + losses;
        return record;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<User> getFollowers() {
        return followers;
    }

}
