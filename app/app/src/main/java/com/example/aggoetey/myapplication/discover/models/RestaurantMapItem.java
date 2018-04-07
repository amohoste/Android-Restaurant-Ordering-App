package com.example.aggoetey.myapplication.discover.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by amoryhoste on 02/04/2018.
 */

public class RestaurantMapItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final Restaurant mRestaurant;

    public RestaurantMapItem(Restaurant restaurant) {
        this.mPosition = restaurant.getPosition();
        this.mTitle = restaurant.getName();
        this.mSnippet = restaurant.getAddress();
        this.mRestaurant = restaurant;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }
}
