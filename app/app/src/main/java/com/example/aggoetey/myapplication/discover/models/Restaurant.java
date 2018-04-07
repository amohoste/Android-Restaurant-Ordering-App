package com.example.aggoetey.myapplication.discover.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by amoryhoste on 02/04/2018.
 */

public class Restaurant {

    private final LatLng position;
    private final String address;
    private final String name;
    private final String phone;
    private final double rating;
    private final String googlePlaceId;

    public Restaurant(double lat, double lng, String address, String name, String phone, double rating, String googlePlaceId) {
        this.position = new LatLng(lat, lng);
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.googlePlaceId = googlePlaceId;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getRating() {
        return rating;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }
}