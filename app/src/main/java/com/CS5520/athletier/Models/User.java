package com.CS5520.athletier.Models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    @Exclude
    public static final String userKey = "users";

    // The unique ID of the User
    private String id;

    // The username of the User
    private String username;

    // The url of the User's profile image as a String
    private String photoUrl;

    // The emailAddress of the User
    private String emailAddress;

    // The total sportsmanship rating of the User
    private int totalSportsmanshipRating;

    // The number of Sportsmanship ratings the User has received
    private int numberOfRatings;

    private List<Team> userTeams = new ArrayList<>();

    private List<User> following = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    // TODO: Remove
    private int wins;
    private int losses;

    // Empty public constructor required by Firebase
    public User() { }

    public User(String id,
                String username,
                String photoUrl,
                String emailAddress) {
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl;
        this.emailAddress = emailAddress;
        this.numberOfRatings = 1;
        this.totalSportsmanshipRating = 5;
    }

    public float getAvgSportsmanshipRating() {
        if (numberOfRatings > 0) {
            return totalSportsmanshipRating / numberOfRatings;
        } else {
            return 0;
        }
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

    // TODO: Remove
    public String getRecord(){
        String record = wins + " - " + losses;
        return record;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public int getTotalSportsmanshipRating() {
        return this.totalSportsmanshipRating;
    }

    public int getNumberOfRatings() {
        return this.numberOfRatings;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<Team> getUserTeams() {
        return userTeams;
    }

}
