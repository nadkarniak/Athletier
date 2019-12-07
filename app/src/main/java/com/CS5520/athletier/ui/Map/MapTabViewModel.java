package com.CS5520.athletier.ui.Map;

import android.app.Application;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.AcceptanceStatus;
import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Utilities.DataSnapShotParser;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeKeys;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MapTabViewModel extends AndroidViewModel {
    private Location userLocation;
    private MutableLiveData<List<Challenge>> challenges;
    private DatabaseReference databaseReference;

    public MapTabViewModel(@NonNull Application application) {
        super(application);
        this.challenges = new MutableLiveData<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();

        listenForNewChallenges();
    }

    private void listenForNewChallenges() {
        databaseReference.child(Challenge.challengeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Challenge> newChallenges =
                        DataSnapShotParser.parseToChallengeList(dataSnapshot);
                challenges.setValue(dateAndStatusFilteredChallenges(newChallenges));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<Challenge> dateAndStatusFilteredChallenges(List<Challenge> challenges) {
        List<Challenge> filteredChallenges = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar filterCalendar = Calendar.getInstance();
        filterCalendar.set(year, month, day, 0, 0, 0);

        // The current date (not including time) in milliseconds
        long filterMillis = filterCalendar.getTimeInMillis();

        for (Challenge challenge : challenges) {
            // Only include challenges that are not Complete and have a date that is today or later
            if (!challenge.getAcceptanceStatus().equals(AcceptanceStatus.COMPLETE.name())
                    && challenge.getDate() >= filterMillis) {
                filteredChallenges.add(challenge);
            }
        }
        return filteredChallenges;
    }

    void setUserLocation(Location location) {
        userLocation = location;
    }

    Location getUserLocation() {
        return userLocation;
    }

    LiveData<List<Challenge>> getMapChallenges() {
        return challenges;
    }

    List<Challenge> getChallengesAtLatLng(LatLng latLng) {
        if (challenges.getValue() == null) {
            return null;
        }

        List<Challenge> challengesAtLatLng = new ArrayList<>();
        for (Challenge challenge : challenges.getValue()) {
            if (challenge.getLatitude() == latLng.latitude
                    && challenge.getLongitude() == latLng.longitude) {
                challengesAtLatLng.add(challenge);
            }
        }

        return challengesAtLatLng;
    }
}