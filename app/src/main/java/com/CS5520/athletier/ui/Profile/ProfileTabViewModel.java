package com.CS5520.athletier.ui.Profile;

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
import com.CS5520.athletier.Utilities.DataSnapShotParser;
import com.CS5520.athletier.Utilities.LogTags;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileTabViewModel extends AndroidViewModel {

    private MutableLiveData<User> currentUser;
    private MutableLiveData<List<SportsAchievementSummary>> achievements;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    public ProfileTabViewModel(@NonNull Application application) {
        super(application);
        this.currentUser = new MutableLiveData<>();
        this.achievements = new MutableLiveData<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Query Firebase for current User
        queryCurrentUser();
        // Query Firebase for current User's SportsAchievementSummary's
        queryAchievements();
    }

    // Query the Firebase Database for User with the firebaseUser's ID
    private void queryCurrentUser() {
        databaseReference
                .child(User.userKey)
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            currentUser.setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(LogTags.ERROR, "Error querying User: " + databaseError);
                    }
                });
    }

    private void queryAchievements() {
        databaseReference
                .child(SportsAchievementSummary.sportsAchievementKey)
                // TODO: Come up with a better way of determining if a User has achievements...
                .child(Sport.ONE_V_ONE_BASKETBALL.name())
                .orderByChild(SportsAchievementSummary.ownerIdKey)
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // If SportsAchievementSummaries exist, pass them to achievements LiveData
                        // stream
                        if (dataSnapshot.getChildrenCount() > 0) {
                            achievements.setValue(
                                    DataSnapShotParser.parseToSummaryList(dataSnapshot)
                            );
                        // Else, create a SportsAchievementSummary for each sport for currentUser
                        } else {
                            createNewAchievements();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(LogTags.ERROR, "Unable to query SportsAchievementSummaries: "
                                + databaseError);
                    }
                });

    }

    private void createNewAchievements() {
        for (Sport sport : Sport.values()) {
            SportsAchievementSummary summary = new SportsAchievementSummary(
                    sport,
                    firebaseUser.getUid()
            );
            databaseReference
                    .child(SportsAchievementSummary.sportsAchievementKey)
                    .child(summary.getSportName())
                    .child(summary.getOwnerId())
                    .setValue(summary)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(LogTags.DATABASE_UPDATE,
                                    "Created new SportsAchievementSummary");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(LogTags.ERROR, "Failed to create SportsAchievementSummary: "
                                    + e.getLocalizedMessage());
                        }
                    })
            ;

        }
    }


    LiveData<User> getCurrentUser() {
        return currentUser;
    }

    LiveData<List<SportsAchievementSummary>> getAchievements() {
        return achievements;
    }


}