package com.CS5520.athletier.ui.Map.CreateChallenge;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.Sport;
import com.CS5520.athletier.Models.State;
import com.CS5520.athletier.Utilities.LogTags;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class CreateChallengeActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> challengeCreationSucceeded;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String opponentId;

    public CreateChallengeActivityViewModel(@NonNull Application application) {
        super(application);
        this.challengeCreationSucceeded = new MutableLiveData<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    void makeChallenge(Sport sport,
                       Date date,
                       Date time,
                       String streetAddress,
                       String city, State state,
                       String zipCode,
                       LatLng latLng) {
        // If the opponentId is null, this is a Challenge created on the Map so the host is the
        // current User. Otherwise, the current User is challenging another User and is thus the
        // opponent (not the host)
        String hostId = opponentId == null ? user.getUid() : opponentId;
        String challengerId = opponentId != null ? user.getUid() : opponentId;

        // Create new challenge (Note: opponentId may be null if challenge created on map)
        Challenge newChallenge = new Challenge(
                hostId,
                challengerId,
                sport,
                combineDateAndTime(date, time),
                streetAddress,
                city,
                state,
                zipCode,
                latLng.latitude,
                latLng.longitude
        );

        databaseReference.child(Challenge.challengeKey)
                .child(newChallenge.getId())
                .setValue(newChallenge)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        challengeCreationSucceeded.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        challengeCreationSucceeded.setValue(false);
                        Log.i(LogTags.ERROR,
                                "Challenge Creation Error: " + e.getLocalizedMessage());
                    }
                })
        ;
    }

    LiveData<Boolean> getChallengeCreationSucceeded() {
        return challengeCreationSucceeded;
    }

    private Date combineDateAndTime(Date date, Date time) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        // Extract hour and min from time
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        int hours = timeCalendar.get(Calendar.HOUR_OF_DAY);
        int min = timeCalendar.get(Calendar.MINUTE);

        // Set hour and min of dateCalendar to extracted hours and min
        dateCalendar.set(Calendar.HOUR_OF_DAY, hours);
        dateCalendar.set(Calendar.MINUTE, min);

        return dateCalendar.getTime();
    }
}
