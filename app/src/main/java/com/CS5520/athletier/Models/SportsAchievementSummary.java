package com.CS5520.athletier.Models;

import java.util.HashMap;
import java.util.List;

public class SportsAchievementSummary {

    // Name of the sport this achievement pertains to
    private String sportName;

    // The id of the User who owns this SportsAchievementSummary.
    private String ownerId;

    // The rank of the owning user in the sport for this SportsAchievementSummary
    private Integer rank;

    // The experience points accrued by the owning user in the sport for this SportsAchievementSummary
    private int exp;

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
        this.exp = 0;
        this.badgeMap = new HashMap<>();

        // Fill badge map with every possible badge name for the input sport as the keys and 0
        // for the initial value
        List<SportsBadge> badges = sport.getBadgeOptions();
        for (SportsBadge badge : badges) {
            badgeMap.put(badge.getName(), 0);
        }
    }

    public String getSportName() {
        return this.sportName;
    }

    public Integer getRank() {
        return this.rank;
    }

    public int getExp() {
        return this.exp;
    }

    public HashMap<String, Integer> getBadgeMap() {
        return this.badgeMap;
    }

    public void setRank(int newRank) {
        this.rank = newRank;
    }

    public void addExp(int expGained) {
        this.exp += expGained;
    }

    public void addBadge(String badgeName) {
       if (badgeMap != null && badgeMap.containsKey(badgeName)) {
           Integer currentCount = badgeMap.get(badgeName);
           if (currentCount != null) {
               badgeMap.put(badgeName, currentCount + 1);
           }
       }
    }

}
