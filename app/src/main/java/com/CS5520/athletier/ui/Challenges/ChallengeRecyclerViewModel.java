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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ChallengeRecyclerViewModel extends AndroidViewModel {

    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private Challenge selectedChallenge;
    private MutableLiveData<Integer> userAwardedExp;

    public ChallengeRecyclerViewModel(@NonNull Application application) {
        super(application);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.selectedChallenge = null;
        this.userAwardedExp = new MutableLiveData<>();
    }

    void setSelectedChallenge(Challenge selectedChallenge) {
        this.selectedChallenge = selectedChallenge;
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

    LiveData<Integer> getUserAwardedExp() {
        return userAwardedExp;
    }

    void updateChallengeWinner(final boolean asHost, final boolean reportedHostWon) {
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
                            Log.i(LogTags.RESULT_REPORT_ERROR,
                                    "Unable to update challenge winner: " + databaseError);
                            return;
                        }

                        Challenge challenge = dataSnapshot.getValue(Challenge.class);
                        if (challenge != null) {
                            String status = challenge.getResultStatus();
                            Sport sport = Sport.fromString(challenge.getSport());

                            // If the Challenge result was just confirmed, award the winner points
                            // and complete challenge
                            if (status.equals(ResultStatus.CONFIRMED.name()) && sport != null) {
                                String winnerId = challenge.getHostReportedWinner();
                                String loserId = challenge.getHostIsWinner() ?
                                        challenge.getHostId() :
                                        challenge.getOpponentId();

                                // Get the tier of the loser
                                queryLoserTier(loserId, winnerId, sport, challenge.getId());
                            }
                        }
                    }
                });
    }

    private void queryLoserTier(String loserId,
                                final String winnerId,
                                final Sport sport,
                                final String challengeId) {
        databaseReference
                .child(SportsAchievementSummary.sportsAchievementKey)
                .child(sport.name())
                .child(loserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SportsAchievementSummary achievement =
                                dataSnapshot.getValue(SportsAchievementSummary.class);
                        if (achievement != null) {
                            int loserTier = achievement.getTier();

                            // Award the winning User points
                            awardWinnerExp(winnerId, sport, loserTier, challengeId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(LogTags.RESULT_REPORT_ERROR,
                                "Failed to fetch loser Tier: " + databaseError);
                    }
                });
    }

    private void awardWinnerExp(final String winnerId,
                                Sport sport,
                                final int loserTier,
                                final String challengeId) {
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
                        // Add exp to winner's SportsAchievementSummary
                        sas.awardExp(challengeId, loserTier);
                        mutableData.setValue(sas);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           boolean b,
                                           @Nullable DataSnapshot dataSnapshot) {
                        if (databaseError != null || dataSnapshot == null) {
                            Log.i(LogTags.ERROR, "Failed to award exp:" + databaseError);
                            return;
                        }

                        // If current User won the challenge, pass pts gained to userAwardedExp
                        if (currentUser != null && currentUser.getUid().equals(winnerId)) {
                            SportsAchievementSummary updatedSAS =
                                    dataSnapshot.getValue(SportsAchievementSummary.class);
                            if (updatedSAS != null) {
                                Map<String, Integer> ptsMap = updatedSAS.getChallengeIdAndPtsMap();
                                if (ptsMap.containsKey(challengeId)) {
                                    userAwardedExp.setValue(ptsMap.get(challengeId));
                                }
                            }
                        }
                    }
                });
    }




}
