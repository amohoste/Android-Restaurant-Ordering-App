package com.example.aggoetey.myapplication.discover.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.aggoetey.myapplication.discover.views.RestaurantInfoCardView;
import com.example.aggoetey.myapplication.model.Restaurant;
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
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;


import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.wrappers.RestaurantMapItem;
import com.example.aggoetey.myapplication.discover.views.ClickableImageView;
import com.example.aggoetey.myapplication.discover.views.MapIconsRenderer;


/**
 * Fragment that displays a map of restaurants
 */
public class MapsFragment extends DiscoverFragment implements OnMapReadyCallback, View.OnClickListener {

    // Constants
    private static final String CAMERA_STATE_KEY = "MAPS-CAMERA-POSITION";
    private static final int MY_LOCATION_ZOOM = 17;
    private static final int GENERAL_ZOOM = 11;

    // Maps
    private GoogleMap mMap;
    private ClusterManager<RestaurantMapItem> mClusterManager;
    MarkerManager.Collection collection;



    private ClickableImageView locationButton;
    private CameraPosition lastpos;
    private LinearLayout restaurantCardLayout;


    //TODO: make RestaurantMapItem parcelable 
    private RestaurantMapItem chosenRestaurant;


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

        if (savedInstanceState != null) {
            if(mMap != null) {
                lastpos = savedInstanceState.getParcelable(CAMERA_STATE_KEY);
            }
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

        restaurantCardLayout  =  v.findViewById(R.id.restaurant_info_card_parent);
        selectAndStartRestCardView(this.chosenRestaurant);
        restaurantProvider = mCallbacks.getRestaurantProvider();
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
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lastpos));
            } else {
                Location loc = locationProvider.getLastLocation();
                if (loc != null) {
                    zoomMapToPosition(locationProvider.getLastLocation(), GENERAL_ZOOM);
                }
            }
        }

        // Start clustermanager and add markers
        setUpClusterManager();
    }


    /**
     * Sets up clustermanager (used to display restaurants on map)
     */
        private void setUpClusterManager() {
            MarkerManager manager = new MarkerManager(mMap);
            collection = manager.newCollection();
            collection.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Restaurant res = (Restaurant) marker.getTag();
                    if (res != null) {
                        Log.v("Menu", res.getTitle());
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
                            resetChosenRestaurant();
                            return true;
                        }
                    });



            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<RestaurantMapItem>() {
                @Override
                public boolean onClusterItemClick(RestaurantMapItem restaurantMapItem) {
                    Log.v("menü", "Open sitt zijn ding adhv restaurantmapitem");
                    resetChosenRestaurant();
                    selectAndStartRestCardView(restaurantMapItem);
                    return false;
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

            // Add cluster items (markers) to the cluster manager.
            if (restaurantProvider != null && restaurantProvider.getRestaurants() != null) {
                addItemsToClusterManager();
            }
        }

    private void selectAndStartRestCardView(RestaurantMapItem restaurantMapItem){
        if(restaurantMapItem != null) {
            chosenRestaurant = restaurantMapItem;
            RestaurantInfoCardView restaurantInfoCardView = new RestaurantInfoCardView(getContext());
            restaurantInfoCardView.bind(restaurantMapItem.getRestaurant());
            restaurantCardLayout.addView(restaurantInfoCardView);
        }
    }

    private void resetChosenRestaurant(){
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mMap != null) {
            outState.putParcelable(CAMERA_STATE_KEY, mMap.getCameraPosition());
        }
    }

}
