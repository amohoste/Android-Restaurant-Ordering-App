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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.helpers.SearchRestaurantHelper;
import com.example.aggoetey.myapplication.discover.views.ClickableImageView;
import com.example.aggoetey.myapplication.discover.views.MapIconsRenderer;
import com.example.aggoetey.myapplication.discover.views.RestaurantInfoCardView;
import com.example.aggoetey.myapplication.discover.wrappers.RestaurantMapItem;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
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


/**
 * Fragment that displays a map of restaurants
 */
public class MapsFragment extends DiscoverFragment implements OnMapReadyCallback, View.OnClickListener, RestaurantInfoCardView.OnCardClickListener {

    // Constants
    private static final String CAMERA_STATE_KEY = "MAPS-CAMERA-POSITION";
    private static final String CHOSEN_RESTAURANT = "CHOSEN_RESTAURANT";
    private static final String CHOSEN_RESTAURANT_POSITION = "CHOSEN_RESTAURANT_POSITION";
    private static final int MY_LOCATION_ZOOM = 17;
    private static final int GENERAL_ZOOM = 11;

    // Maps
    private GoogleMap mMap;
    private ClusterManager<RestaurantMapItem> mClusterManager;
    MarkerManager.Collection collection;
    private static boolean init = false;

    private ClickableImageView locationButton;
    private CameraPosition lastpos;
    private LinearLayout restaurantCardLayout;
    private static boolean mapReady = false;

    /**
     * Position of restaurant is stored separately as parcelable. This is because making
     * Restaurant parcelable means having to replace almost all models in a direct/indirect
     * relationship with restaurant to implement Parcelable. (Difficult to achieve with the time constraint)
     */
    private LatLng restaurantPosition;
    private Restaurant chosenRestaurant;


    public MapsFragment() {

    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
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

        if (savedInstanceState != null) {
            if (mMap != null) {
                lastpos = savedInstanceState.getParcelable(CAMERA_STATE_KEY);
            }
            loadRestaurant(savedInstanceState);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            lastpos = (CameraPosition) savedInstanceState.getParcelable(CAMERA_STATE_KEY);
            loadRestaurant(savedInstanceState);
        }
        View v = inflater.inflate(R.layout.discover_maps_fragment, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationButton = (ClickableImageView) v.findViewById(R.id.imgMyLocation);
        locationButton.setOnClickListener(this);
        locationButton.setVisibility(View.INVISIBLE);


        ClickableImageView zoominButton = (ClickableImageView) v.findViewById(R.id.imgZoomIn);
        zoominButton.setOnClickListener(this);

        ClickableImageView zoomOutButton = (ClickableImageView) v.findViewById(R.id.imgZoomOut);
        zoomOutButton.setOnClickListener(this);

        ClickableImageView listButton = (ClickableImageView) v.findViewById(R.id.listButton);
        listButton.setOnClickListener(this);

        restaurantCardLayout = v.findViewById(R.id.restaurant_info_card_parent);
        selectAndStartRestCardView(chosenRestaurant);

        restaurantProvider = mCallbacks.getRestaurantProvider();
        restaurantProvider.addRestaurantListener(this);
        locationProvider = mCallbacks.getLocationProvider();

        return v;
    }


    /**
     * Initializes map when map is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle_day);
        mMap.setMapStyle(style);

        if (locationProvider != null) {
            enableLocationBullet();
            if (lastpos != null) {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(lastpos));
            }
        }
        // Start clustermanager and add markers
        setUpClusterManager();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapReady = true;
                if (searchedRestaurants != null) {
                    addRestaurants(searchedRestaurants, false);
                }

                Location loc = locationProvider.getLastLocation();
                if (!init) {
                    zoomMapToPosition(loc, MY_LOCATION_ZOOM);
                    init = true;
                }

            }
        });

    }


    /**
     * Sets up clustermanager (used to display restaurants on map)
     */
    private void setUpClusterManager() {
        if (mClusterManager == null) {
            MarkerManager manager = new MarkerManager(mMap);
            collection = manager.newCollection();
            collection.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Restaurant res = (Restaurant) marker.getTag();
                    resetChosenRestaurant();
                    selectAndStartRestCardView(res);
                    return true;
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
                            resetChosenRestaurant();
                            return true;
                        }
                    });


            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<RestaurantMapItem>() {
                @Override
                public boolean onClusterItemClick(RestaurantMapItem restaurantMapItem) {
                    resetChosenRestaurant();
                    selectAndStartRestCardView(restaurantMapItem.getRestaurant());
                    return true;
                }
            });


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    resetChosenRestaurant();
                }
            });

            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(manager);
        }

        // Add cluster items (markers) to the cluster manager.
        if (restaurantProvider != null && restaurantProvider.getRestaurants() != null) {
            addItemsToClusterManager();
        }
    }

    private void selectAndStartRestCardView(Restaurant restaurant) {
        if (restaurant != null) {
            chosenRestaurant = restaurant;
            RestaurantInfoCardView restaurantInfoCardView = new RestaurantInfoCardView(getContext());
            restaurantInfoCardView.bind(restaurant);
            restaurantInfoCardView.setCardClickListener(this);
            restaurantCardLayout.addView(restaurantInfoCardView);
        }
    }

    private void resetChosenRestaurant() {
        restaurantCardLayout.removeAllViews();
        chosenRestaurant = null;
    }

    /**
     * Helper method to add all restaurants from the restaurantprovider to the clustermanager
     */
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
        mCallbacks.setFragment(DiscoverContainerFragment.LIST_FRAGMENT_ID);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgMyLocation:
                Location loc = locationProvider.getLastLocation();
                if (loc != null) {
                    zoomMapToPosition(loc, MY_LOCATION_ZOOM);
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

    private void saveRestaurant(Bundle bundle) {
        if (bundle != null && chosenRestaurant != null) {

            bundle.putSerializable(CHOSEN_RESTAURANT, chosenRestaurant);
            bundle.putParcelable(CHOSEN_RESTAURANT_POSITION, chosenRestaurant.getPosition());
        }

    }

    private void loadRestaurant(Bundle bundle) {
        if (bundle != null) {
            restaurantPosition = bundle.getParcelable(CHOSEN_RESTAURANT_POSITION);
            chosenRestaurant = (Restaurant) bundle.getSerializable(CHOSEN_RESTAURANT);
        }

    }

    @Override
    public void onRestaurantUpdate(ArrayList<Restaurant> restaurants) {
        addItemsToClusterManager();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(CAMERA_STATE_KEY, mMap.getCameraPosition());
        }

        saveRestaurant(outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCardClick(Restaurant restaurant) {
        DiscoverContainerFragment parent = (DiscoverContainerFragment) getParentFragment();
        DiscoverContainerFragment.RestaurantSelectListener mListener = parent.getSelectListener();
        if (mListener != null && Tab.getInstance().canLogout()) {
            parent.getSelectListener().onRestaurantSelect(MenuInfo.getInstance().reset());
            Tab.getInstance().setRestaurant(restaurant);
        }
        //Toast.makeText(getContext(), "Menu", Toast.LENGTH_SHORT).show();
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
        this.searchedRestaurants = result;
        if (mapReady) {
            addRestaurants(result, clear);
        }
    }

    private void addRestaurants(ArrayList<Restaurant> result, boolean clear) {
        init = true;
        if (clear) {
            removeMarkers();
        } else if (collection != null && mapReady) {
            removeMarkers();

            if (result.size() > 1) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (Restaurant restaurant : result) {
                    builder.include(restaurant.getPosition());
                    Marker myMarker = collection.addMarker(new MarkerOptions().position(restaurant.getPosition()).title(restaurant.getTitle()).zIndex(2));
                    myMarker.setTag(restaurant);
                    dropPinEffect(myMarker);
                }
                final LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

            } else if (result.size() == 1) {
                Restaurant restaurant = result.get(0);
                Marker myMarker = collection.addMarker(new MarkerOptions().position(restaurant.getPosition()).title(restaurant.getTitle()).zIndex(2));
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
        if (collection != null && searchedRestaurants != null) {
            ArrayList<Restaurant> result = new ArrayList<>();

            for (Restaurant restaurant : searchedRestaurants) {
                if (SearchRestaurantHelper.satisfiesFilter(restaurant)) {
                    result.add(restaurant);
                }
            }
            if (mapReady) {
                addRestaurants(result, false);
            }
        }
    }
}
