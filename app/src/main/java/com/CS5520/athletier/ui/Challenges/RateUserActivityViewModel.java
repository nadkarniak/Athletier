package com.CS5520.athletier.ui.Challenges;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateUserActivityViewModel extends AndroidViewModel {
    private Challenge selectedChallenge;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private MutableLiveData<User> userToRate;

    public RateUserActivityViewModel(@NonNull Application application) {
        super(application);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.userToRate = new MutableLiveData<>();
    }

    void setChallenge(Challenge challenge) {
        this.selectedChallenge = challenge;

        String userToRateId = challenge.getHostId().equals(currentUser.getUid()) ?
                challenge.getHostId() : challenge.getOpponentId();
        queryUser(userToRateId);
    }

    LiveData<User> getUserToRate() {
        return userToRate;
    }

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


}
