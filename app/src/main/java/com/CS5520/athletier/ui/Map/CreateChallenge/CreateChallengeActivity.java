package com.CS5520.athletier.ui.Map.CreateChallenge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.CS5520.athletier.R;

public class CreateChallengeActivity extends AppCompatActivity {

    private CreateChallengeFormFragment createChallengeForm;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        setupActionBar();
        setupFragments();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupObservers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_slide, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_slide, R.anim.slide_down);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.create_challenge_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
    }

    private void setupFragments() {
        FragmentManager manager = getSupportFragmentManager();
        createChallengeForm = (CreateChallengeFormFragment)
                manager.findFragmentById(R.id.createChallengeForm);
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setEnabled(false);
    }

    private void setupObservers() {
        LifecycleOwner owner = createChallengeForm.getViewLifecycleOwner();
        createChallengeForm.getHasRequiredFields().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasRequiredFields) {
                System.out.println("Button enabled:" + hasRequiredFields);
                createButton.setEnabled(hasRequiredFields);
            }
        });
    }


}
