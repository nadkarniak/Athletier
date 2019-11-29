package com.CS5520.athletier.ui.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.Models.Challenge;
import com.CS5520.athletier.Models.ChallengeStatus;
import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.RequestCodes;
import com.CS5520.athletier.ui.Map.CreateChallenge.CreateChallengeActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;;
import java.util.List;

public class MapTabFragment extends Fragment implements OnMapReadyCallback, LocationUpdateListener {

    //region - View Model

    private MapTabViewModel viewModel;

    //endregion

    //region - Location Requesting

    private LocationRequester locationRequester;
    private LocationRequest locationRequest;
    private boolean hasPermissions;
    private boolean didPreviouslyZoom = false;

    //endregion

    //region - Views/UI

    private GoogleMap mapView;
    private SelectedChallengeFragment selectedChallengeFragment;
    private List<Marker> challengeMarkers = new ArrayList<>();

    //endregion

    //region - Fragment Lifecycle

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setupLocationRequest();
        checkLocationSettings(getActivity());
        hasPermissions =
                ContextCompat.checkSelfPermission(context, LocationRequester.requiredPermission)
                        == PackageManager.PERMISSION_GRANTED;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_tab, container, false);
        setupFragments();
        setupCreateChallengeButton(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MapTabViewModel.class);
        listenForChallenges();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationSettings(getActivity());
    }

    //endregion

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMyLocationEnabled(hasPermissions);
        if (hasPermissions) {
            Location userLocation = viewModel.getUserLocation();
            if (userLocation != null) {
                LatLng position = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
            }

            mapView.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Get all challenges at location of marker and pass to selectedChallengeFragment
                    List<Challenge> challenges = viewModel.getChallengesAtLatLng(marker.getPosition());
                    if (challenges != null) {
                        selectedChallengeFragment.setChallengesAtSelectedLocation(challenges);
                        if (selectedChallengeFragment.isHidden()) {
                            toggleSelectedChallengeFragmentVisibility(true, true);
                        }
                    }
                    return false;
                }
            });

            mapView.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (!selectedChallengeFragment.isHidden()) {
                        toggleSelectedChallengeFragmentVisibility(false,
                                true);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == RequestCodes.LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermissions = true;
                locationRequester.startUpdatingLocation(locationRequest);
            } else {
                hasPermissions = false;
            }
        }
    }


    private void setupFragments() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        selectedChallengeFragment = (SelectedChallengeFragment)
                getChildFragmentManager().findFragmentById(R.id.selectedChallengeFragment);

        // Initially hide selected challenge fragment since no challenges are selected
        toggleSelectedChallengeFragmentVisibility(false, false);
    }

    private void toggleSelectedChallengeFragmentVisibility(boolean shouldShow, boolean withAnimation) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (shouldShow) {
            transaction = transaction.show(selectedChallengeFragment);
        } else {
            transaction = transaction.hide(selectedChallengeFragment);
        }

        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        }

        transaction.commit();
    }


    private void setupCreateChallengeButton(View view) {
        Button createChallengeButton = view.findViewById(R.id.openChallengeButton);
        createChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewOpenChallenge();
            }
        });
    }

    private void createNewOpenChallenge(){
        Activity currentActivity = getActivity();
        if (currentActivity != null) {
            stopLocationUpdates();
            Intent intent = new Intent(currentActivity, CreateChallengeActivity.class);
            startActivityForResult(intent, RequestCodes.CREATE_MAP_CHALLENGE);
            currentActivity.overridePendingTransition(R.anim.slide_up, R.anim.no_slide);
        }
    }

    private void setupLocationRequester(Context context) {
        locationRequester = new LocationRequester(context, this);
    }

    private void setupLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void listenForChallenges() {
        viewModel.getMapChallenges().observe(getViewLifecycleOwner(), new Observer<List<Challenge>>() {
            @Override
            public void onChanged(List<Challenge> challenges) {
                if (mapView == null) {
                    return;
                }

                // Clear existing markers
                clearMarkers();

                // Create new markers
                for (Challenge challenge : challenges) {
                    LatLng latLng = new LatLng(challenge.getLatitude(), challenge.getLongitude());
                    float color = getChallengeMarkerColor(
                                ChallengeStatus.valueOf(challenge.getChallengeStatus())
                    );
                    challengeMarkers.add(
                            mapView.addMarker(
                                    new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                            )
                    );
                }
            }
        });
    }

    private float getChallengeMarkerColor(ChallengeStatus status) {
        switch (status) {
            case IN_PROGRESS:
                return BitmapDescriptorFactory.HUE_RED;
            case AWAITING_PLAYERS:
                return BitmapDescriptorFactory.HUE_GREEN;
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    private void clearMarkers() {
        if (challengeMarkers == null || challengeMarkers.size() == 0) {
            return;
        }
        for (Marker marker : challengeMarkers) {
            marker.remove();
        }
    }

    @Override
    public void updateWithLocation(Location location) {
        viewModel.setUserLocation(location);
        if (mapView != null && !didPreviouslyZoom) {
            mapView.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()),
                            15
                    )
            );
            didPreviouslyZoom = true;
        }
    }

    private void stopLocationUpdates() {
        if (locationRequester != null) {
            locationRequester.stopUpdatingLocation();
        }
    }


    private void checkLocationSettings(final Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                setupLocationRequester(activity);
                if (hasPermissions) {
                    if (mapView != null) { mapView.setMyLocationEnabled(true); }
                    locationRequester.startUpdatingLocation(locationRequest);
                } else {
                    requestPermissions(new String[] { LocationRequester.requiredPermission },
                            RequestCodes.LOCATION_PERMISSIONS);
                }
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Show dialogue prompting user to enable appropriate location settings.
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity, RequestCodes.LOCATION_SETTINGS);
                    } catch (IntentSender.SendIntentException exc) {

                    }
                }
            }
        });
    }

}