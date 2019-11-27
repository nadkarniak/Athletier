package com.CS5520.athletier.ui.Search.FindUser;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindUserViewModel extends ViewModel {

    private MutableLiveData<String> id = new MutableLiveData<>();
    private FirebaseAuth mAuth;
    private FirebaseUser searchedUser;
    private DatabaseReference mRef;

    void findUserWithEmail(final String email) {
        Log.d("email", email);


        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();


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

    LiveData<String> getOtherUid() {
        return id;
    }

}
