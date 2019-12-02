package com.CS5520.athletier.ui.Teams;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamsTabViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    FirebaseDatabase database;
    DatabaseReference reff;
    void findUserWithId(String userId) {
        // TODO: Get the user from the Firebase database...
        database = FirebaseDatabase.getInstance();
        reff = database.getReference("User");
        reff.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                String user = dataSnapshot.getValue(String.class);

                                                //then passing user the LiveData stream
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                           }
                                   });

                // TODO: Pass the User to the LiveData stream
             //   User dummyUser = new User();
       // user.setValue(dummyUser);
    }

    LiveData<User> getCurrentUser() {
        return user;
    }
}
