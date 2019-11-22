package com.CS5520.athletier.ui.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.CS5520.athletier.Models.User;

public class ProfileTabViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();

    void findUserWithId(String userId) {
        // TODO: Get the user from the Firebase database...

        // TODO: Pass the User to the LiveData stream
        User dummyUser = new User();
        user.setValue(dummyUser);
    }

    LiveData<User> getCurrentUser() {
        return user;
    }


}