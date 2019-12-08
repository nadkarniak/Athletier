package com.CS5520.athletier.ui.Search.FindUser;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.CS5520.athletier.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindUserViewModel extends AndroidViewModel {

    private MutableLiveData<User> userResult;
    private MutableLiveData<SportsAchievementSummary> achievement;
    private Sport selectedSport;
    private DatabaseReference mRef;
    private FirebaseUser currentUser;

    public FindUserViewModel(@NonNull Application application) {
        super(application);
        this.userResult = new MutableLiveData<>();
        this.achievement = new MutableLiveData<>();
        this.mRef = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    void setSelectedSport(Sport sport) {
        this.selectedSport = sport;
        User currentUser = userResult.getValue();
        if (currentUser != null) {
            queryExp(currentUser.getId());
        }
    }

    void findUserWithEmail(final String email) {

        mRef.child(User.userKey)
                .orderByChild("emailAddress")
                .equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                            User foundUser = snapshotChild.getValue(User.class);
                            if (foundUser != null) {
                                userResult.setValue(foundUser);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    void queryExp(String userId) {
        if (selectedSport == null) { return; }
        mRef.child(SportsAchievementSummary.sportsAchievementKey)
                .child(selectedSport.name())
                .child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SportsAchievementSummary sas =
                        dataSnapshot.getValue(SportsAchievementSummary.class);
                if (sas != null) {
                    achievement.setValue(sas);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    LiveData<User> getUserResult() {
        return userResult;
    }

    LiveData<SportsAchievementSummary> getSportsAchievementSummary() {
        return achievement;
    }

    String getCurrentUserId() {
        return currentUser.getUid();
    }
}
