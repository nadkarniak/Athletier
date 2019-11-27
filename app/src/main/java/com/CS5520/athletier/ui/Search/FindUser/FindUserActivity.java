package com.CS5520.athletier.ui.Search.FindUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.CS5520.athletier.R;

public class FindUserActivity extends FragmentActivity {

    private FindUserFragment findUserFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        findUserFragment = new FindUserFragment();
        setupFragment();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        findUserFragment.setArguments(bundle);
    }

    private void setupFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, findUserFragment).commit();

    }






}
