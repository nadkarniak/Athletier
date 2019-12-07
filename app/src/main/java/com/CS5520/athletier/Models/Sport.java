package com.CS5520.athletier.Models;

import androidx.annotation.NonNull;

import com.CS5520.athletier.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Sport {
    ONE_V_ONE_BASKETBALL,
    TENNIS,
    GOLF;


    // Returns a list of possible badges for the input sport
    public List<SportsBadge> getBadgeOptions() {
        // Basketball badges.
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

        // Tennis badges.
        SportsBadge backhand_specialist =  new SportsBadge("Backhand Specialist",
                TENNIS, "Has a good backhand.",
                R.drawable.backhand);
        SportsBadge agile =  new SportsBadge("Agility",
                TENNIS, "Gets to everything.",
                R.drawable.gets_to_everything);
        SportsBadge serve =  new SportsBadge("Nice Serve",
                TENNIS, "Nice serve.",
                R.drawable.shooter);
        SportsBadge net_player =  new SportsBadge("Net Player",
                TENNIS, "Good at the net.",
                R.drawable.net_player);
        SportsBadge slice =  new SportsBadge("Slicer",
                TENNIS, "Slices a lot.",
                R.drawable.slice);
        SportsBadge[] tennis;
        tennis = new SportsBadge[] {backhand_specialist, agile, serve, net_player, slice};

        // Golf badges.
        SportsBadge sand_specialist =  new SportsBadge("Sand Specialist",
                GOLF, "Good out of the sand.",
                R.drawable.good_sand);
        SportsBadge long_ball =  new SportsBadge("Long Ball",
                GOLF, "Drives the ball far.",
                R.drawable.long_ball);
        SportsBadge putter =  new SportsBadge("Putting Master",
                GOLF, "Good at putting.",
                R.drawable.putter);
        SportsBadge quick_round =  new SportsBadge("Quick Round",
                GOLF, "Plays fast.",
                R.drawable.quick_round);
        SportsBadge straight_drives =  new SportsBadge("Straight Drives",
                GOLF, "Drives the ball straight.",
                R.drawable.straight_drives);
        SportsBadge[] golf;
        golf = new SportsBadge[] {sand_specialist, long_ball, putter, quick_round, straight_drives};


        switch (this) {
            case ONE_V_ONE_BASKETBALL:
                return Arrays.asList(basketball);
            case TENNIS:
                return Arrays.asList(tennis);
            case GOLF:
                return Arrays.asList(golf);
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
            case TENNIS:
                return "Tennis";
            case GOLF:
                return "Golf";
            default:
                return "Unknown";
        }
    }

    public static Sport fromString(String sportString) {
        if (sportString.equals(ONE_V_ONE_BASKETBALL.toString())) {
            return ONE_V_ONE_BASKETBALL;
        } else if (sportString.equals(TENNIS.toString())) {
            return TENNIS;
        } else if (sportString.equals(GOLF.toString())) {
            return GOLF;
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

    public int getIconId() {
        switch (this) {
            case ONE_V_ONE_BASKETBALL:
                return R.drawable.basketball;
            case TENNIS:
                return R.drawable.tennis_ball;
            case GOLF:
                return R.drawable.golf_ball;
            default:
                return R.drawable.basketball;
        }
    }

}