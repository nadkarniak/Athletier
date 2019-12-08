package com.CS5520.athletier.ui.Challenges.Rating;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.User;
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

import java.util.List;

public class RateUserActivityViewModel extends AndroidViewModel {

    // The Challenge selected in the ChallengeTab
    private Challenge selectedChallenge;
    // The current FirebaseUser of the app
    private FirebaseUser currentUser;
    // Reference to FirebaseDatabase
    private DatabaseReference databaseReference;
    // MutableLiveData that emits the opponent User to rate
    private MutableLiveData<User> userToRate;
    // The Id of the User that is being rated
    private String userToRateId;
    // MutableLiveData that emits if the selected sportsmanship rating was successfully added
    private MutableLiveData<Boolean> addedSportsmanshipRating;
    // MutableLiveData that emits if the awarded badges were successfully added
    private MutableLiveData<Boolean> addedBadges;

    public RateUserActivityViewModel(@NonNull Application application) {
        super(application);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.userToRate = new MutableLiveData<>();
        this.addedSportsmanshipRating = new MutableLiveData<>();
        this.addedBadges = new MutableLiveData<>();
    }

    //region - Setters
    void setChallenge(Challenge challenge) {
        this.selectedChallenge = challenge;

       userToRateId = challenge.getHostId().equals(currentUser.getUid()) ?
                challenge.getOpponentId() : challenge.getHostId();
        // Need to query User to rate to get their username and photoUrl
        queryUser(userToRateId);
    }

    void addRatingAndBadges(float rating, List<String> badgeNames) {
        awardBadges(badgeNames);
        addSportsmanshipRating(Math.round(rating));
    }
    //endregion

    //region - Getters

    LiveData<User> getUserToRate() {
        return userToRate;
    }

    LiveData<Boolean> getAddedSportsmanshipRating() {
        return addedSportsmanshipRating;
    }

    LiveData<Boolean> addedBadges() {
        return addedBadges;
    }

    //endregion

    private void queryUser(String id) {
        databaseReference
                .child(User.userKey)
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            userToRate.setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void awardBadges(final List<String> awardedBadgeNames) {
        Sport sport = Sport.fromString(selectedChallenge.getSport());
        if (sport != null) {
            databaseReference
                    .child(SportsAchievementSummary.sportsAchievementKey)
                    .child(sport.name())
                    .child(userToRateId).runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    SportsAchievementSummary sas =
                            mutableData.getValue(SportsAchievementSummary.class);
                    if (sas == null) {
                        return Transaction.success(mutableData);
                    }
                    // Add the awarded badges to the SportsAchievementSummary
                    for (String badgeName : awardedBadgeNames) {
                        sas.addBadge(badgeName);
                    }
                    mutableData.setValue(sas);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError,
                                       boolean b,
                                       @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        Log.i(LogTags.USER_RATING_ERROR,
                                "Unable to add badges: " + databaseError);
                    } else {
                        toggleChallengeRatingFlag();
                    }
                    addedBadges.setValue(databaseError != null);
                }
            });
        }
    }

    private void addSportsmanshipRating(final int rating) {
        databaseReference
                .child(User.userKey)
                .child(userToRateId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null) {
                    return Transaction.success(mutableData);
                }
                user.addSportsmanshipRating(rating);
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   boolean b,
                                   @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.i(LogTags.USER_RATING_ERROR,
                            "Unable to add sportsmanship rating: " + databaseError);
                } else {
                    toggleChallengeRatingFlag();
                }
                addedSportsmanshipRating.setValue(databaseError != null);
            }
        });
    }

    private void toggleChallengeRatingFlag() {
        final boolean userIsHost = selectedChallenge.getHostId().equals(currentUser.getUid());

        databaseReference
                .child(Challenge.challengeKey)
                .child(selectedChallenge.getId()).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Challenge challenge = mutableData.getValue(Challenge.class);
                if (challenge == null) {
                    return Transaction.success(mutableData);
                }
                if (userIsHost) {
                    challenge.setHostDidRate(true);
                } else {
                    challenge.setOpponentDidRate(true);
                }
                mutableData.setValue(challenge);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   boolean b,
                                   @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.i(LogTags.USER_RATING_ERROR,
                            "Unable to change challenge rating flag: " + databaseError);
                }
            }
        });


    }



}
