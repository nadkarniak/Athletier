package com.CS5520.athletier.ui.Map;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ChallengeStatus;
import com.CS5520.athletier.Models.User;
import com.CS5520.athletier.Utilities.ChallengeUpdater;
import com.CS5520.athletier.Utilities.LogTags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class ChallengeCellViewModel extends AndroidViewModel {
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private MutableLiveData<Challenge> currentChallenge;
    private MutableLiveData<User> currentHostUser;
    private MutableLiveData<String> currentStatusDisplay;

    public ChallengeCellViewModel(@NonNull Application application) {
        super(application);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.currentChallenge = new MutableLiveData<>();
        this.currentHostUser = new MutableLiveData<>();
        this.currentStatusDisplay = new MutableLiveData<>();
    }

    void setCurrentChallenge(Challenge challenge) {
        currentChallenge.setValue(challenge);
        databaseReference.child(User.userKey)
                .child(challenge.getHostId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    currentHostUser.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(LogTags.ERROR, "Error fetching user: " + databaseError.getMessage());
            }
        });

    }

    void joinChallenge() {
        Challenge challenge = currentChallenge.getValue();
        if (challenge != null) {
            databaseReference
                    .child(Challenge.challengeKey)
                    .child(challenge.getId()).runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Challenge challengeToUpdate = mutableData.getValue(Challenge.class);
                    if (challengeToUpdate == null) {
                        return Transaction.success(mutableData);
                    }

                    challengeToUpdate.setOpponentId(currentUser.getUid());
                    mutableData.setValue(challengeToUpdate);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError,
                                       boolean b,
                                       @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError == null && dataSnapshot != null) {
                        Challenge updatedChallenge = dataSnapshot.getValue(Challenge.class);
                        if (updatedChallenge != null) {
                            ChallengeStatus status =
                                    ChallengeStatus.valueOf(updatedChallenge.getChallengeStatus());
                            currentStatusDisplay.setValue(status.toString());
                        }
                    }
                }
            });


        }
    }

    LiveData<Challenge> getCurrentChallenge() {
        return currentChallenge;
    }

    LiveData<User> getCurrentHostUser() {
        return currentHostUser;
    }

    LiveData<String> getCurrentStatusDisplay() {
        return currentStatusDisplay;
    }

    String getCurrentUserId() {
        return currentUser.getUid();
    }

    String getHostUserEmail() {
        User hostUser = currentHostUser.getValue();
        if (hostUser != null) {
            return hostUser.getEmailAddress();
        } else {
            return null;
        }
    }
}
