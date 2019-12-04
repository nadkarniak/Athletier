package com.CS5520.athletier.ui.Leaderboards;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.CS5520.athletier.Models.SportsAchievementSummary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaderboardsTabViewModel extends AndroidViewModel {

    private MutableLiveData<SportsAchievementSummary> achievements;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;


    public LeaderboardsTabViewModel(@NonNull Application application) {
        super(application);
        this.achievements = new MutableLiveData<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();


    }



}
