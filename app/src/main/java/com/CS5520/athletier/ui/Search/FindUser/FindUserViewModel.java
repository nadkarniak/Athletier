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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindUserViewModel extends AndroidViewModel {

    private MutableLiveData<String> id;
    private MutableLiveData<String> expPts;
    private FirebaseAuth mAuth;
    private FirebaseUser searchedUser;
    private DatabaseReference mRef;

    public FindUserViewModel(@NonNull Application application) {
        super(application);
        this.id = new MutableLiveData<>();
        this.expPts = new MutableLiveData<>();
        this.mRef = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
    }

    void findUserWithEmail(final String email) {


        mRef.child("users").orderByChild("emailAddress").equalTo(email).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.child("id").getValue().toString();
                    id.setValue(uid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void queryExp(String sport, String uid) {
        System.out.println(uid);
        mRef.child(SportsAchievementSummary.sportsAchievementKey)
                .child(sport).orderByChild(SportsAchievementSummary.ownerIdKey)
                .equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String exp = data.child("exp").getValue().toString();
                    expPts.setValue(exp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    LiveData<String> getOtherUid() {
        return id;
    }

    LiveData<String> getExp(Sport sport, String UID) {
        queryExp(sport.name(), UID);
        return expPts;
    }

}
