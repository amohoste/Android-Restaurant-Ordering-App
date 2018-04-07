package com.example.aggoetey.myapplication.discover.models;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by amoryhoste on 02/04/2018.
 */

@SuppressLint("ParcelCreator")
public class Restaurant implements SearchSuggestion {

    private String name;
    private String address;
    private String phone;
    private double rating;
    private LatLng position;
    private String googlePlaceId;
    private HashMap<Integer,HashMap<String,String>> openingHours;
    private String pictureReference;
    private String type;

    public Restaurant() {

    }

    public Restaurant(LatLng position, String address, String name, String phone, double rating, String googlePlaceId, HashMap<Integer, HashMap<String, String>> openingHours, String pictureReference, String type) {
        this.position = position;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.googlePlaceId = googlePlaceId;
        this.openingHours = openingHours;
        this.pictureReference = pictureReference;
        this.type = type;
    }

    // Getters
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

    public HashMap<Integer, HashMap<String, String>> getOpeningHours() {
        return openingHours;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setPosition(double lat, double lng) {
        this.position = new LatLng(lat, lng);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOpeningHours(HashMap<Integer, HashMap<String, String>> openingHours) {
        this.openingHours = openingHours;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getPictureReference() {
        return pictureReference;
    }

    public void setPictureReference(String pictureReference) {
        this.pictureReference = pictureReference;
    }

    public void setType(String type) {
        this.type = type;
    }


    // Search suggestions
    @Override
    public String getBody() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(0);
    }
}
