package com.CS5520.athletier.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Sport {
    ONE_V_ONE_BASKETBALL,
    SQUASH,
    TENNIS,
    GOLF,
    SPIKEBALL;

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

    public static List<String> getAllSportsNames() {
        Sport[] sportsList = Sport.values();
        List<String> stringList = new ArrayList<>();
        for (Sport sport : sportsList) {
            stringList.add(sport.name());
        }
        return stringList;
    }

}