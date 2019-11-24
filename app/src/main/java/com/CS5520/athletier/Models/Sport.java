package com.CS5520.athletier.Models;

import androidx.annotation.NonNull;

import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Sport {
    ONE_V_ONE_BASKETBALL,
    SQUASH,
    TENNIS,
    GOLF,
    SPIKEBALL;


    // Returns a list of possible badges for the input sport
    public List<SportsBadge> getBadgeOptions() {
        SportsBadge ballHandler =  new SportsBadge("Ball Handler",
                ONE_V_ONE_BASKETBALL, "Good at dribbling.",
                R.drawable.ball_handler);
        SportsBadge rebounder =  new SportsBadge("Gets Boards",
                ONE_V_ONE_BASKETBALL, "Good at rebounding.",
                R.drawable.boards);
        SportsBadge shooter =  new SportsBadge("Sharp Shooter",
                ONE_V_ONE_BASKETBALL, "Good at shooting.",
                R.drawable.shooter);
        SportsBadge defender =  new SportsBadge("Lock Down",
                ONE_V_ONE_BASKETBALL, "Good defender.",
                R.drawable.defender);
        SportsBadge sportsman =  new SportsBadge("Good Sport",
                ONE_V_ONE_BASKETBALL, "Plays fair.",
                R.drawable.good_sport);
        SportsBadge[] basketball;
        basketball = new SportsBadge[] {ballHandler, rebounder, shooter, defender, sportsman};

        // TODO: Add badges for each sport below...
        switch (this) {
            case ONE_V_ONE_BASKETBALL:
                return Arrays.asList(basketball);
            case SQUASH:
                return new ArrayList<>();
            case TENNIS:
                return new ArrayList<>();
            case GOLF:
                return new ArrayList<>();
            case SPIKEBALL:
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }


    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case ONE_V_ONE_BASKETBALL:
                return "1v1 Basketball";
            case SQUASH:
                return "Squash";
            case TENNIS:
                return "Tennis";
            case GOLF:
                return "Golf";
            case SPIKEBALL:
                return "Spikeball";
            default:
                return "Unknown";
        }
    }

    public static Sport fromString(String sportString) {
        if (sportString.equals(ONE_V_ONE_BASKETBALL.toString())) {
            return ONE_V_ONE_BASKETBALL;
        } else if (sportString.equals(SQUASH.toString())) {
            return SQUASH;
        } else if (sportString.equals(TENNIS.toString())) {
            return TENNIS;
        } else if (sportString.equals(GOLF.toString())) {
            return GOLF;
        } else if (sportString.equals(SPIKEBALL.toString())) {
            return SPIKEBALL;
        } else {
            return null;
        }
    }


    public static List<String> getAllSportsNames() {
        Sport[] sportsList = Sport.values();
        List<String> stringList = new ArrayList<>();
        for (Sport sport : sportsList) {
            stringList.add(sport.toString());
        }
        return stringList;
    }

}