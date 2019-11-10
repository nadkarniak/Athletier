package com.CS5520.athletier.Models;

import java.util.List;

public class Team {
    private int teamId;
    private int captainId;
    private List<User> teamMembers;
    private String teamName;
    private Sport sport;
    private int totalSportsmanshipRating;
    private int expPoints;
    private int rank;
    private List<Challenge> challenges;

}
