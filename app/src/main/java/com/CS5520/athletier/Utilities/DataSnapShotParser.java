package com.CS5520.athletier.Utilities;

import androidx.annotation.NonNull;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataSnapShotParser {

    public static List<Challenge> parseToChallengeList(@NonNull DataSnapshot snapshot) {
        List<Challenge> challenges = new ArrayList<>();
        for (DataSnapshot child : snapshot.getChildren()) {
            Challenge challenge = child.getValue(Challenge.class);
            if (challenge != null) {
                challenges.add(challenge);
            }
        }
        return challenges;
    }

    public static List<SportsAchievementSummary> parseToSummaryList(@NonNull DataSnapshot snapshot) {
        List<SportsAchievementSummary> summaries =  new ArrayList<>();
        for (DataSnapshot child : snapshot.getChildren()) {
            System.out.println("Test");

            SportsAchievementSummary summary = child.getValue(SportsAchievementSummary.class);
            if (summary != null) {
                summaries.add(summary);
            }
        }
        return summaries;
    }
}
