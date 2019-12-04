package com.CS5520.athletier.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

public class SportsAchievementSummary {

    // The parent key for storing SportsAchievementSummaries in Firebase
    @Exclude
    public static final String sportsAchievementKey = "sports_achievements";
    @Exclude
    public static final String ownerIdKey = "ownerId";
    @Exclude
    public static final String expKey = "exp";

    // The id of this SportsAchievementSummary's owning User
    private String ownerId;

    // Name of the sport this achievement pertains to
    private String sportName;

    // The rank of the owning User in the Sport of this SportsAchievementSummary
    private Integer rank;

    // The tier of the owning User in the Sport of this SportsAchievementSummary
    private Integer tier;

    // The experience points accrued by the owning user in the sport of this
    // SportsAchievementSummary
    private Integer exp;

    // Keys are Strings representing the badge name, values are Integers representing number of
    // times badge has been awarded
    private HashMap<String, Integer> badgeMap;


    // Constructor for Firebase
    public SportsAchievementSummary() {}

    // Constructor for creating a completely new SportsAchievementSummary
    public SportsAchievementSummary(Sport sport, String ownerId) {
        this.sportName = sport.name();
        this.ownerId = ownerId;
        this.rank = null; // Set once user has gained experience points
        this.tier = 1; // User's start at Tier 1
        this.exp = 0;
        this.badgeMap = new HashMap<>();

        // Fill badge map with every possible badge name for the input sport as the keys and 0
        // for the initial value
        List<SportsBadge> badges = sport.getBadgeOptions();
        for (SportsBadge badge : badges) {
            badgeMap.put(badge.getName(), 0);
        }
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getSportName() {
        return this.sportName;
    }

    public Integer getRank() {
        return this.rank;
    }

    public Integer getTier() { return this.tier; }

    public int getExp() {
        return this.exp;
    }

    public HashMap<String, Integer> getBadgeMap() {
        return this.badgeMap;
    }

    public void setRank(int newRank) {
        this.rank = newRank;
    }

    @Exclude
    public void addExp(int expGained) {
        this.exp += expGained;
    }

    @Exclude
    public void addBadge(String badgeName) {
       if (badgeMap != null && badgeMap.containsKey(badgeName)) {
           Integer currentCount = badgeMap.get(badgeName);
           if (currentCount != null) {
               badgeMap.put(badgeName, currentCount + 1);
           }
       }
    }

    @Exclude
    public Sport getSport() {
        return Sport.valueOf(sportName);
    }

    @Exclude
    public Integer getBadgeCount(SportsBadge badge) {
        if (badgeMap != null && badgeMap.containsKey(badge.getName())) {
            return badgeMap.get(badge.getName());
        } else {
            return null;
        }
    }

}
