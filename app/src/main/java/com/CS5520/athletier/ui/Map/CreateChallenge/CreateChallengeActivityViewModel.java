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

import java.util.Date;

public class CreateChallengeActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> challengeCreationSucceeded;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    public CreateChallengeActivityViewModel(@NonNull Application application) {
        super(application);
        this.challengeCreationSucceeded = new MutableLiveData<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    void makeChallenge(Sport sport,
                       Date date,
                       String streetAddress,
                       String city, State state,
                       String zipCode,
                       LatLng latLng) {

        if (user == null) {
            return;
        }

        // Create challenge and pass to createdChallenge LiveData
        final Challenge newChallenge = new Challenge(
                user.getUid(),
                user.getDisplayName(),
                sport,
                date,
                streetAddress,
                city,
                state,
                zipCode,
                latLng.latitude,
                latLng.longitude
        );

        databaseReference.child("challenges").child(newChallenge.getId()).setValue(newChallenge)
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


}
