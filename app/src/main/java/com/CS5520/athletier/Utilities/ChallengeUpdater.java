package com.CS5520.athletier.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class ChallengeUpdater {


    public static void addOppponent(DatabaseReference databaseReference,
                                    String id,
                                    final String opponentId) {
        databaseReference
                .child(Challenge.challengeKey)
                .child(id).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Challenge challenge = mutableData.getValue(Challenge.class);
                if (challenge == null) {
                    return Transaction.success(mutableData);
                }
                challenge.setOpponentId(opponentId);
                mutableData.setValue(challenge);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   boolean b,
                                   @Nullable DataSnapshot dataSnapshot) {
                Log.i(LogTags.DATABASE_UPDATE,
                        "Update Challenge Opponent Id Complete: " + databaseError);
            }
        });
    }


    // Update the AcceptanceStatus of a child with the input ID
    public static void updateAcceptanceStatus(DatabaseReference databaseReference,
                                              String id,
                                              final AcceptanceStatus status) {
        databaseReference
                .child(Challenge.challengeKey)
                .child(id).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Challenge challenge = mutableData.getValue(Challenge.class);
                if (challenge == null) {
                    return Transaction.success(mutableData);
                }
                challenge.setAcceptanceStatus(status);
                mutableData.setValue(challenge);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   boolean b,
                                   @Nullable DataSnapshot dataSnapshot) {
                Log.i(LogTags.DATABASE_UPDATE,
                        "Update Challenge Acceptance Status Complete: " + databaseError);
            }
        });
    }

    // Delete a Challenge with the input ID from the database
    public static void deleteChallenge(DatabaseReference databaseReference,
                                       String id) {
        databaseReference
                .child(Challenge.challengeKey)
                .child(id)
                .removeValue();
    }


}
