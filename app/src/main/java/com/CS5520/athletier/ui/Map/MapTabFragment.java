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
import androidx.lifecycle.ViewModelProviders;

import com.CS5520.athletier.R;
import com.CS5520.athletier.Utilities.RequestCodes;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapTabFragment extends Fragment implements OnMapReadyCallback, LocationUpdateListener {

    //region - View Model

    private MapTabViewModel mapTabViewModel;

    //endregion

    //region - Location Requesting

    private LocationRequester locationRequester;
    private LocationRequest locationRequest;
    private boolean hasPermissions;
    private boolean didPreviouslyZoom = false;

    //endregion

    //region - Views/UI

    private GoogleMap mapView;

    //endregion

    //region - Fragment Lifecycle

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_tab, container, false);
        setupMapFragment();
        setupCreateChallengeButton(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setupLocationRequest();
        checkLocationSettings(getActivity());
        hasPermissions =
                ContextCompat.checkSelfPermission(context, LocationRequester.requiredPermission)
                        == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapTabViewModel = ViewModelProviders.of(this).get(MapTabViewModel.class);
    }

    @Override
    public void onDestroy() {
        if (locationRequester != null) {
            locationRequester.stopUpdatingLocation();
        }
        super.onDestroy();
    }
    //endregion

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setMyLocationEnabled(true);

        Location userLocation = mapTabViewModel.getUserLocation();
        if (userLocation != null) {
            LatLng position = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
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

    private void setupMapFragment() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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
        // TODO: Create new activity for creating a new challenge
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

    @Override
    public void updateWithLocation(Location location) {
        mapTabViewModel.setUserLocation(location);
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