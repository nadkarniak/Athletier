package com.CS5520.athletier.Utilities;

import androidx.annotation.NonNull;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataSnapShotParser {

    public static List<Challenge> parseToChallengeList(@NonNull DataSnapshot snapshots) {
        List<Challenge> challenges = new ArrayList<>();
        for (DataSnapshot snapshot : snapshots.getChildren()) {
            Challenge challenge = snapshot.getValue(Challenge.class);
            if (challenge != null) {
                challenges.add(challenge);
            }
        }
        return challenges;
    }
}
