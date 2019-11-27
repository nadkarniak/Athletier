package com.CS5520.athletier.ui.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileTabViewModel extends ViewModel {

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    void findUserWithId(String userId) {
        // TODO: Get the user from the Firebase database...

        // TODO: Pass the User to the LiveData stream

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user.setValue(currentUser);

    }

    LiveData<FirebaseUser> getCurrentUser() {
        return user;
    }


}