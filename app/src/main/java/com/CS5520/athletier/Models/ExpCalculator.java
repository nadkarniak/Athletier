package com.CS5520.athletier.Models;

public class ExpCalculator {

    public static int calculateExpEarned(int tier, int opponentTier) {
        if (opponentTier < tier) {
            return 1;
        } else if (opponentTier == tier) {
            return 2;
        } else {
            return 3;
        }
    }

}
