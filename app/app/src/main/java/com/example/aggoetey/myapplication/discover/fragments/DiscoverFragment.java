package com.example.aggoetey.myapplication.discover.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;

/**
 * Created by amoryhoste on 03/04/2018.
 */
public class DiscoverFragment extends Fragment implements MapsFragment.Callbacks, CurrentLocationProvider.LocationListener {

    // Tags
    private static final String TAG_LOCATION_PROVIDER = "LocationProvider";
    private static final String RESTAURANT_FRAG = "GetRestaurantsFrag";

    // Providers
    private CurrentLocationProvider mLocationProvider;
    private RestaurantProvider mRestaurantProvider;

    private FragmentManager fm;
    private LocationFragment currentfragment;
    private FloatingSearchView mSearchView;

    public DiscoverFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discover_fragment, container, false);

        fm = getChildFragmentManager();

        mSearchView = (FloatingSearchView) v.findViewById(R.id.floating_search_view);

        // Setup location provider
        mLocationProvider = (CurrentLocationProvider) fm.findFragmentByTag(TAG_LOCATION_PROVIDER);

        if (mLocationProvider == null) {
            mLocationProvider = CurrentLocationProvider.newInstance();
            fm.beginTransaction().add(mLocationProvider, TAG_LOCATION_PROVIDER).commit();
        }
        mLocationProvider.addLocationListener(this);

        // Setup restaurant provider
        mRestaurantProvider = (RestaurantProvider) fm.findFragmentByTag(RESTAURANT_FRAG);
        if (mRestaurantProvider == null) {
            mRestaurantProvider = RestaurantProvider.newInstance();
            fm.beginTransaction().add(mRestaurantProvider, RESTAURANT_FRAG).commit();
        }

        // Setup current fragment
        currentfragment = (LocationFragment) fm.findFragmentById(R.id.discover_fragment_container);

        if (currentfragment == null) {
            currentfragment = MapsFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, currentfragment)
                    .commit();
        }

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_filter) {
                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), "Open filter",
                            Toast.LENGTH_SHORT).show();
                } else if(item.getItemId() == R.id.action_qr){
                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), "Open qr scanner",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void setFragment(int fragment) {
        if (fragment == 0) {
            currentfragment = MapsFragment.newInstance();

        } else {
            currentfragment = RestaurantListFragment.newInstance();
        }
        getChildFragmentManager().beginTransaction()
                .replace(R.id.discover_fragment_container, currentfragment)
                .commit();
    }


    @Override
    public CurrentLocationProvider getLocationProvider() {
        return mLocationProvider;
    }

    @Override
    public RestaurantProvider getRestaurantProvider() {
        return mRestaurantProvider;
    }


    @Override
    public void onStop() {
        if (mLocationProvider != null) {
            mLocationProvider.removeLocationListener(this);
        }
        super.onStop();
    }

    @Override
    public void onLocationUpdate(Location location) {

    }
}
