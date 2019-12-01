package com.CS5520.athletier.ui.Map.CreateChallenge;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.State;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.GeocoderInput;
import com.CS5520.athletier.Utilities.GeocoderResult;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CreateChallengeActivity extends AppCompatActivity {

    // ViewModel
    private CreateChallengeActivityViewModel viewModel;

    // Fragments/Views
    private CreateChallengeFormFragment createChallengeForm;
    private Button createButton;

    // AsyncTasks
    private AsyncTask geocodeAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        viewModel = ViewModelProviders.of(this).get(CreateChallengeActivityViewModel.class);
        setupActionBar();
        setupFragments();
        setupCreateButtonListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupObservers();
    }

    @Override
    protected void onStop() {
        endGeocodeAsyncTask();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_slide, R.anim.slide_down);
        endGeocodeAsyncTask();
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

    public void handleGeocoderResult(GeocoderResult result) {
        if (result.getGeocodingSucceeded()) {
            geocodeAsyncTask.cancel(true);
            viewModel.makeChallenge(
                    createChallengeForm.getSport(),
                    createChallengeForm.getDate(),
                    createChallengeForm.getSelectedTime(),
                    result.getInputStreet(),
                    result.getInputCity(),
                    result.getInputState(),
                    result.getInputZipCode(),
                    result.getLatLng()
            );
        } else {
            Toast.makeText(this, R.string.invalid_address_msg, Toast.LENGTH_LONG).show();
        }
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
        createButton = findViewById(R.id.createButton);
        createButton.setEnabled(false);
        createButton.setFocusable(false);
    }

    private void setupObservers() {
        LifecycleOwner owner = createChallengeForm.getViewLifecycleOwner();
        createChallengeForm.getHasRequiredFields().observe(owner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasRequiredFields) {
                createButton.setEnabled(hasRequiredFields);
                createButton.getBackground().setAlpha(createButton.isEnabled() ? 255 : 128);
            }
        });

        viewModel.getChallengeCreationSucceeded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean creationSucceeded) {
                showChallengeCreationToast(creationSucceeded);
                if (creationSucceeded) {
                    finish();
                }
            }
        });
    }

    private void showChallengeCreationToast(boolean creationSucceeded) {
        String msg = creationSucceeded ? "Challenge created!" : "Challenge creation failed...Try again";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupCreateButtonListener() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Attempt to get latitude and longitude of input address
                attemptToGeocodeAddress();
            }
        });
    }

    private void attemptToGeocodeAddress() {
        String streetAddress = createChallengeForm.getCurrentStreetAddress();
        String city = createChallengeForm.getCurrentCity();
        State state = createChallengeForm.getState();
        String zipCode = createChallengeForm.getZipCode();

        if (streetAddress != null && city != null && zipCode != null) {
             endGeocodeAsyncTask();
             GeocoderInput input = new GeocoderInput(streetAddress, city, state, zipCode);
             geocodeAsyncTask = new GeocodeAsyncTask(this).execute(input);
        }
    }

    private void endGeocodeAsyncTask() {
        if (geocodeAsyncTask != null) {
            geocodeAsyncTask.cancel(true);
        }
    }

    private static class GeocodeAsyncTask extends AsyncTask<GeocoderInput, GeocoderResult, Void> {
        private static final String EXCEPTION_TAG = "GEOCODE_ASYNC_ERROR";
        WeakReference<CreateChallengeActivity> activityWeakReference;

        GeocodeAsyncTask(CreateChallengeActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(GeocoderInput... geocoderInputs) {
            Context context = activityWeakReference.get();
            if (context == null) {
                return null;
            }

            Geocoder geocoder = new Geocoder(context);
            GeocoderInput input = geocoderInputs[0];

            try {
                List<Address> addresses = geocoder.getFromLocationName(
                        input.getLocationName(),
                        5
                );
                if (addresses != null && addresses.size() > 0) {
                    Address result = addresses.get(0);
                    LatLng latLng = new LatLng(result.getLatitude(), result.getLongitude());
                    publishProgress(new GeocoderResult(input, latLng, true));
                } else {
                    publishProgress(new GeocoderResult(input, null, false));
                }
            } catch (IOException exception) {
                String message = exception.getLocalizedMessage();
                if (message != null) {
                    Log.i(EXCEPTION_TAG, message);
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(GeocoderResult... results) {
            CreateChallengeActivity activity = activityWeakReference.get();
            GeocoderResult result = results[0];
            if (activity != null) {
                activity.handleGeocoderResult(result);
            }
        }
    }


}
