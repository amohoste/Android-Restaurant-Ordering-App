package com.example.aggoetey.myapplication.discover.wrappers;

import com.example.aggoetey.myapplication.model.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Container class needed for the clustermanager, contains a restaurant and all neccesary fields
 */
public class RestaurantMapItem implements ClusterItem, Serializable{

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final Restaurant mRestaurant;

    public RestaurantMapItem(Restaurant restaurant) {
        this.mPosition = restaurant.getPosition();
        this.mTitle = restaurant.getTitle();
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
