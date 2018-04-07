package com.example.aggoetey.myapplication.discover.fragments;

import android.support.v4.app.Fragment;

import com.example.aggoetey.myapplication.discover.models.Restaurant;

import java.util.ArrayList;


import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;

/**
 * Created by amoryhoste on 03/04/2018.
 */

public abstract class LocationFragment extends Fragment implements RestaurantProvider.RestaurantListener {

    protected Callbacks mCallbacks;
    protected CurrentLocationProvider locationProvider;
    protected RestaurantProvider restaurantProvider;

    public interface Callbacks {
        void setFragment(int fragment);
        RestaurantProvider getRestaurantProvider();
        CurrentLocationProvider getLocationProvider();
    }

    public LocationFragment() {

    }

    abstract void onSearchResult(ArrayList<Restaurant> result, boolean clear);
    abstract void filterResults();
}
