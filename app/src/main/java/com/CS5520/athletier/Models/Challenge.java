package com.CS5520.athletier.Models;

import java.util.Date;

public class Challenge {
    private int id;
    private Sport sport;
    private int hostId;
    private int opponentId;
    private Location location;
    private ChallengeStatus challengeStatus;
    private ChallengeAcceptanceStatus acceptanceStatus;
    private Date date;
    private boolean hostIsWinner;
}
