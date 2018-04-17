package com.example.aggoetey.myapplication.discover.fragments;

import android.support.v4.app.Fragment;


import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;

/**
 * Abstract class for a fragment that has a location and restaurantprovider (list and map)
 */
public abstract class DiscoverFragment extends Fragment {

    protected Callbacks mCallbacks;
    protected CurrentLocationProvider locationProvider;
    protected RestaurantProvider restaurantProvider;

    public interface Callbacks {
        void setFragment(int fragment);
        RestaurantProvider getRestaurantProvider();
        CurrentLocationProvider getLocationProvider();
    }

    public DiscoverFragment() {

    }
}
