package com.example.aggoetey.myapplication.discover.services;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;

public class CurrentLocationProvider extends Fragment implements LocationSource, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private final int LOCATION_PERMISSION = 2748;

    // Listeners
    private LocationListener locationListener;
    private OnLocationChangedListener listener; // Listener for google maps

    public interface LocationListener {
        void onLocationUpdate(Location location);
    }

    public void addLocationListener(LocationListener restaurantListener) {
        this.locationListener = restaurantListener;
    }

    public void removeLocationListener(LocationListener restaurantListener) {
        this.locationListener = null;
    }

    // Used to track location
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    // Last locaiton
    private Location lastLocation;

    public Location getLastLocation() {
        return lastLocation;
    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            lastLocation = locationResult.getLastLocation();

            if(listener != null)  {
                listener.onLocationChanged(lastLocation);
            }

            if (locationListener != null) {
                locationListener.onLocationUpdate(lastLocation);
            }
        }
    };

    public static CurrentLocationProvider newInstance() {
        CurrentLocationProvider provider = new CurrentLocationProvider();
        return provider;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            startLocationServices();
        } else {
            buildGoogleApiClient();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void startLocationServices() {
        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mFusedLocationClient.requestLocationUpdates(req, mLocationCallback, Looper.myLooper());
        } catch (SecurityException exception) {
            showAlertDialog();
        }
    }

    public void showAlertDialog() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please give location permissions");
        builder.setMessage("Without location permissions we can't give an interactive map view of your location.");

        // add the buttons
        builder.setPositiveButton("Give permissions", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestLocationPermissions();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (getContext() != null && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        } else {
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.listener = onLocationChangedListener;
        if (lastLocation != null) {
            listener.onLocationChanged(lastLocation);
        }
    }

    @Override
    public void deactivate() {
        listener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());

        }
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
