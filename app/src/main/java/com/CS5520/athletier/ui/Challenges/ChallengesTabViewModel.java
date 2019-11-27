package com.CS5520.athletier.ui.Challenges;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.Utilities.DataSnapShotParser;
import com.CS5520.athletier.Utilities.LogTags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChallengesTabViewModel extends AndroidViewModel {
    private User user;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<Challenge>> challengesAsHost;
    private MutableLiveData<List<Challenge>> challengesAsOpponent;
    private String statusFilter;
    private boolean shouldFilterAsHost;

    public ChallengesTabViewModel(@NonNull Application application) {
        super(application);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.challengesAsHost = new MutableLiveData<>();
        this.challengesAsOpponent = new MutableLiveData<>();
        this.statusFilter = AcceptanceStatus.ACCEPTED.name();
        this.shouldFilterAsHost = true;
    }

    void setCurrentUser(User user) {
        this.user = user;
        // Query challenges where user is host
        queryChallenges(true);

        // Query challenges where user is opponent
        queryChallenges(false);
    }

    LiveData<List<Challenge>> getHostedChallenges() {
        return challengesAsHost;
    }

    LiveData<List<Challenge>> getChallengesAsOpponent() {
        return challengesAsOpponent;
    }

    void setSelectedStatusFilter(int id) {
        System.out.println(id);
        switch (id) {
            case 0:
                statusFilter = AcceptanceStatus.ACCEPTED.name();
                break;
            case 1:
                statusFilter = AcceptanceStatus.PENDING.name();
                break;
            case 2:
                statusFilter = AcceptanceStatus.COMPLETE.name();
                break;
        }
    }

    String getStatusFilter() {
        return statusFilter;
    }

    boolean getShouldFilterAsHost() {
        return shouldFilterAsHost;
    }


    private void queryChallenges(final boolean userIsHost) {
        databaseReference.child(Challenge.challengeKey)
                // If userIsHost is true, look for challenges where userId matches hostId, otherwise
                // look for challenges where userId matches opponentId
                .orderByChild(userIsHost ? Challenge.hostIdKey : Challenge.opponentIdKey)
                .equalTo(user.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Parse dataSnapShot into List of Challenges
                        List<Challenge> challenges =
                                DataSnapShotParser.parseToChallengeList(dataSnapshot);

                        // Sort challenges by date
                        sortChallengesByDate(challenges);

                        // Pass challenges to appropriate MutableLiveData stream
                        if (userIsHost) {
                            challengesAsHost.setValue(challenges);
                        } else {
                            challengesAsOpponent.setValue(challenges);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(LogTags.ERROR, databaseError.getMessage());
                    }
                });
    }


    private void sortChallengesByDate(List<Challenge> challenges) {
        Collections.sort(challenges, new Comparator<Challenge>() {
            public int compare(Challenge c1, Challenge c2) {
                long c1Date = c1.getDate();
                long c2Date = c2.getDate();

                // Compare such that dates will be sorted in descending order
                if (c1Date < c2Date) {
                    return  1;
                } else if (c1Date > c2Date) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

    }

}