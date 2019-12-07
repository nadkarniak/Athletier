package com.CS5520.athletier.Models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Name of the sport this SportsAchievementSummary pertains to
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
    private Map<String, Integer> badgeMap;

    // A Map of ids of completed Challenges (keys) and the experience points awarded (values)
    private Map<String, Integer> challengeIdAndPtsMap;


    //region - Initialization

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
        this.challengeIdAndPtsMap = new HashMap<>();

        // Fill badge map with every possible badge name for the input sport as the keys and 0
        // for the initial value
        List<SportsBadge> badges = sport.getBadgeOptions();
        for (SportsBadge badge : badges) {
            badgeMap.put(badge.getName(), 0);
        }
    }
    //endregion


    //region - Public Getters

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

    public Map<String, Integer> getBadgeMap() {
        return this.badgeMap;
    }

    public Map<String, Integer> getChallengeIdAndPtsMap() { return this.challengeIdAndPtsMap; }

    public void setRank(int newRank) {
        this.rank = newRank;
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

    //endregion

    //region - Public Setters
    @Exclude
    public void awardExp(String challengeId, int opponentTier) {
        int ptsEarned = ExpCalculator.calculateExpEarned(tier, opponentTier);
        // Award exp based on the opponents Tier
        System.out.println("Points awarded");
        exp += ptsEarned;
        // Update tier
        tier = Tier.fromExp(exp).toInt();

        // Add challengeId and ptsEarned to challengeIdsAndPts map
        if (challengeIdAndPtsMap == null) {
            challengeIdAndPtsMap = new HashMap<>();
        }
        if (!challengeIdAndPtsMap.containsKey(challengeId)) {
            challengeIdAndPtsMap.put(challengeId, ptsEarned);
        }


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

    //endregion

}
