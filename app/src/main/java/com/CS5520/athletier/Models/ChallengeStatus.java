package com.CS5520.athletier.Models;

import androidx.annotation.NonNull;

public enum ChallengeStatus {
    AWAITING_PLAYERS,
    FULL;

    @Override
    @NonNull
    public String toString() {
        switch (this) {
            case AWAITING_PLAYERS:
                return "Awaiting Players";
            default:
                return "Full";
        }

    }
}
