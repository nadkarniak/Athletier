package com.CS5520.athletier.ui.Challenges;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ExpCalculator;
import com.CS5520.athletier.Models.ResultStatus;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Utilities.LogTags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class ChallengeRecyclerViewModel extends AndroidViewModel {

    private DatabaseReference databaseReference;
    private Challenge selectedChallenge;
    private MutableLiveData<SportsAchievementSummary> hostAchievement;
    private MutableLiveData<SportsAchievementSummary> opponentAchievement;
    private MutableLiveData<Boolean> reportWinnerSucceeded;
    private MutableLiveData<Integer> userAwardedExp;


    public ChallengeRecyclerViewModel(@NonNull Application application) {
        super(application);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.selectedChallenge = null;
        this.hostAchievement = new MutableLiveData<>();
        this.opponentAchievement = new MutableLiveData<>();
        this.reportWinnerSucceeded = new MutableLiveData<>();
        this.userAwardedExp = new MutableLiveData<>();
    }

    void setSelectedChallenge(Challenge selectedChallenge) {
        this.selectedChallenge = selectedChallenge;
        Sport sport = Sport.fromString(selectedChallenge.getSport());

        // Get the SportsAchievementSummarys of the Users in the selected challenge
        if (sport != null) {
            querySportsAchievementSummary(sport, selectedChallenge.getHostId(), true);
            querySportsAchievementSummary(sport, selectedChallenge.getOpponentId(), false);
        }
    }

    void updateChallengeAcceptanceStatus(String challengeId, final AcceptanceStatus status) {
        databaseReference
                .child(Challenge.challengeKey)
                .child(challengeId)
                .runTransaction(new Transaction.Handler() {
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
                        Log.i(LogTags.ERROR, "Error updating challenge acceptance status: "
                                + databaseError);
                    }
                });
    }

    void deleteChallenge(String challengeId) {
        databaseReference
                .child(Challenge.challengeKey)
                .child(challengeId)
                .removeValue();
    }

    LiveData<Boolean> getReportWinnerSucceeded() {
        return reportWinnerSucceeded;
    }

    LiveData<Integer> getUserAwardedExp() {
        return userAwardedExp;
    }


    private void querySportsAchievementSummary(Sport sport, String ownerId, final boolean isHost) {
        databaseReference
                .child(SportsAchievementSummary.sportsAchievementKey)
                .child(sport.name())
                .child(ownerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SportsAchievementSummary achievement =
                                dataSnapshot.getValue(SportsAchievementSummary.class);
                        if (isHost) {
                            hostAchievement.postValue(achievement);
                        } else {
                            opponentAchievement.postValue(achievement);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(LogTags.ERROR, "Error fetching achievement: " + databaseError);
                    }
                });
    }

    void updateChallengeWinner(final boolean asHost,
                               final boolean reportedHostWon) {
        if (selectedChallenge == null) { return; }

        databaseReference
                .child(Challenge.challengeKey)
                .child(selectedChallenge.getId())
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Challenge challenge = mutableData.getValue(Challenge.class);
                        if (challenge == null) {
                            return Transaction.success(mutableData);
                        }

                        // Set the appropriate reported winner
                        String winnerId = reportedHostWon ? challenge.getHostId() :
                                challenge.getOpponentId();
                        if (asHost) {
                            challenge.setHostReportedWinner(winnerId);
                        } else {
                            challenge.setOpponentReportedWinner(winnerId);
                        }
                        mutableData.setValue(challenge);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           boolean b,
                                           @Nullable DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null || databaseError != null) {
                            reportWinnerSucceeded.setValue(false);
                            return;
                        }

                        Challenge challenge = dataSnapshot.getValue(Challenge.class);
                        if (challenge != null) {
                            String status = challenge.getResultStatus();
                            Sport sport = Sport.fromString(challenge.getSport());
                            SportsAchievementSummary hostSAS = hostAchievement.getValue();
                            SportsAchievementSummary opponentSAS = opponentAchievement.getValue();


                            // If the Challenge result was just confirmed, award the winner points
                            // and complete challenge
                            if (status.equals(ResultStatus.CONFIRMED.name())
                                    && hostSAS != null
                                    && opponentSAS != null
                                    && sport != null) {

                                String winnerId = challenge.getHostReportedWinner();
                                int loserTier = challenge.getHostIsWinner() ? opponentSAS.getTier()
                                        : hostSAS.getTier();
                                awardWinnerExp(winnerId, sport, loserTier);

                            }
                            reportWinnerSucceeded.setValue(true);
                        } else {
                            reportWinnerSucceeded.setValue(false);
                        }
                    }
                });
    }

    private void awardWinnerExp(String winnerId, Sport sport, final int loserTier) {
        databaseReference
                .child(SportsAchievementSummary.sportsAchievementKey)
                .child(sport.name())
                .child(winnerId)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        SportsAchievementSummary sas =
                                mutableData.getValue(SportsAchievementSummary.class);
                        if (sas == null) {
                            return Transaction.success(mutableData);
                        }

                        userAwardedExp.postValue(
                                ExpCalculator.calculateExpEarned(sas.getTier(), loserTier)
                        );
                        sas.awardExp(loserTier);
                        mutableData.setValue(sas);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           boolean b,
                                           @Nullable DataSnapshot dataSnapshot) {
                        if (databaseError != null) {
                            Log.i(LogTags.ERROR, "Failed to award exp:" + databaseError);
                        }
                    }
                });
    }




}
