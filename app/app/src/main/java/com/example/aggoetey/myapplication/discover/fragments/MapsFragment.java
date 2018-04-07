package com.example.aggoetey.myapplication.discover.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.helpers.SearchRestaurantHelper;
import com.example.aggoetey.myapplication.discover.models.Restaurant;
import com.example.aggoetey.myapplication.discover.models.RestaurantMapItem;
import com.example.aggoetey.myapplication.discover.views.ClickableImageView;
import com.example.aggoetey.myapplication.discover.views.MapIconsRenderer;


/**
 * Created by amoryhoste on 03/04/2018.
 */
public class MapsFragment extends LocationFragment implements OnMapReadyCallback, View.OnClickListener {

    // Constants
    private static final String CAMERA_STATE_KEY = "MAPS-CAMERA-POSITION";

    // Maps
    private GoogleMap mMap;
    private ClusterManager<RestaurantMapItem> mClusterManager;
    MarkerManager.Collection collection;

    private ClickableImageView locationButton;
    private CameraPosition lastpos;

    public MapsFragment() {

    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mCallbacks = (Callbacks) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && mMap != null) {
            lastpos = savedInstanceState.getParcelable(CAMERA_STATE_KEY);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.discover_maps_fragment, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationButton = (ClickableImageView) v.findViewById(R.id.imgMyLocation);
        locationButton.setOnClickListener(this);
        locationButton.setVisibility(View.INVISIBLE);

        ClickableImageView  zoominButton = (ClickableImageView) v.findViewById(R.id.imgZoomIn);
        zoominButton.setOnClickListener(this);

        ClickableImageView  zoomOutButton = (ClickableImageView) v.findViewById(R.id.imgZoomOut);
        zoomOutButton.setOnClickListener(this);

        ClickableImageView listButton = (ClickableImageView) v.findViewById(R.id.listButton);
        listButton.setOnClickListener(this);

        restaurantProvider = mCallbacks.getRestaurantProvider();
        restaurantProvider.addRestaurantListener(this);

        locationProvider = mCallbacks.getLocationProvider();

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle_day);
        mMap.setMapStyle(style);

        if (locationProvider != null) {
            enableLocationBullet();
            if (lastpos != null) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lastpos));
            } else {
                Location loc = locationProvider.getLastLocation();
                if (loc != null) {
                    zoomMapToPosition(locationProvider.getLastLocation(), 11);
                }
            }
        }

        // Start clustermanager and add markers
        setUpClusterManager();
    }

    private void setUpClusterManager() {
        MarkerManager manager = new MarkerManager(mMap);
        collection = manager.newCollection();
        collection.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Restaurant res = (Restaurant) marker.getTag();
                if (res != null) {
                    Log.v("Menu", res.getName());
                }
                return false;
            }
        });

        mClusterManager = new ClusterManager<RestaurantMapItem>(getContext(), mMap, manager);
        mClusterManager.setRenderer(new MapIconsRenderer(getContext(), mMap, mClusterManager));

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<RestaurantMapItem>() {
                    @Override
                    public boolean onClusterClick(Cluster<RestaurantMapItem> cluster) {
                        LatLngBounds.Builder builder = LatLngBounds.builder();
                        for (ClusterItem item : cluster.getItems()) {
                            builder.include(item.getPosition());
                        }
                        final LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
                        return true;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<RestaurantMapItem>() {
            @Override
            public boolean onClusterItemClick(RestaurantMapItem restaurantMapItem) {
                Log.v("menü", "Open sitt zijn ding adhv restaurantmapitem");
                return false;
            }
        });

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(manager);

        // Add cluster items (markers) to the cluster manager.
        if (restaurantProvider != null && restaurantProvider.getRestaurants() != null) {
            addItemsToClusterManager();
        }
    }

    private void addItemsToClusterManager() {
        if (mClusterManager != null) {
            mClusterManager.clearItems();
            for (Restaurant restaurant : restaurantProvider.getRestaurants()) {
                mClusterManager.addItem(new RestaurantMapItem(restaurant));
            }
            mClusterManager.cluster();
        }
    }

    private void zoomMapToPosition(Location loc, int zoom) {
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }

    private void zoomInMap() {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    private void zoomOutMap() {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    private void onClickListButton() {
        mCallbacks.setFragment(1);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgMyLocation:
                Location loc = locationProvider.getLastLocation();
                if (loc != null) {
                    zoomMapToPosition(loc, 17);
                }

                break;
            case R.id.imgZoomIn:
                zoomInMap();
                break;
            case R.id.imgZoomOut:
                zoomOutMap();
                break;
            case R.id.listButton:
                onClickListButton();
                break;
            default:
                break;
        }
    }

    private void enableLocationBullet() {
        // Enable location bullet
        if (locationProvider != null && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setLocationSource(locationProvider);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            locationButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mMap != null) {
            outState.putParcelable(CAMERA_STATE_KEY, mMap.getCameraPosition());
        }
    }

    @Override
    public void onRestaurantUpdate(ArrayList<Restaurant> restaurants) {
        addItemsToClusterManager();
    }

    @Override
    public void onStop() {
        if (restaurantProvider != null) {
            restaurantProvider.removeRestaurantListener(this);
        }
        super.onStop();
    }

    @Override
    void onSearchResult(ArrayList<Restaurant> result, boolean clear) {
        if (clear) {
            removeMarkers();
        } else if (collection != null) {
            removeMarkers();

            if (result.size() > 1) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (Restaurant restaurant : result) {
                    builder.include(restaurant.getPosition());
                    Marker myMarker = collection.addMarker(new MarkerOptions().position(restaurant.getPosition()).title(restaurant.getName()).zIndex(2));
                    myMarker.setTag(restaurant);
                    dropPinEffect(myMarker);
                }
                final LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

            } else if (result.size() == 1) {
                Restaurant restaurant = result.get(0);
                Marker myMarker = collection.addMarker(new MarkerOptions().position(restaurant.getPosition()).title(restaurant.getName()).zIndex(2));
                myMarker.setTag(restaurant);
                dropPinEffect(myMarker);
                Location loc = new Location("");
                loc.setLatitude(restaurant.getPosition().latitude);
                loc.setLongitude(restaurant.getPosition().longitude);
                zoomMapToPosition(loc, 16);
            }
        }
    }

    // Source: https://stackoverflow.com/questions/13189054/implement-falling-pin-animation-on-google-maps-android/20241972#20241972
    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1000;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 4 * t);

                if (t > 0.0) {
                    handler.postDelayed(this, 15);
                }
            }
        });
    }

    private void removeMarkers() {
        if (collection != null) {
            collection.clear();
        }
    }

    @Override
    void filterResults() {

        if (collection != null && collection.getMarkers() != null) {
            for (Marker marker : collection.getMarkers()) {
                Restaurant res = (Restaurant) marker.getTag();
                if (! SearchRestaurantHelper.satisfiesFilter(res)) {
                    collection.remove(marker);
                }
            }
        }
    }
}
