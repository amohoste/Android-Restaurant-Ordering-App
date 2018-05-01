package com.example.aggoetey.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een restaurant model.
 */

//TODO: Make restaurant parcelable to be able to save restaurant in bundles without transient
//TODO: since LatLng is only parcelable not serializable.

public class Restaurant implements Serializable {

    //TODO: Remove transient once parcelable is achieved

    // Menu
    private Menu menu;
    private List<Table> tables = new ArrayList<>();

    // Info
    private String title;
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

    public Restaurant(String title, String phone, String googlePlaceId) {
        this.title = title;
        this.phone = phone;
        this.googlePlaceId = googlePlaceId;
    }

    // TODO: remove this
    public Restaurant(String title, Menu menu, double lat, double lng, String address, String phone, double rating, String googlePlaceId) {
        this.title = title;
        this.menu = menu;
        this.position = new LatLng(lat, lng);
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.googlePlaceId = googlePlaceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public void addTable(Table table){
        this.tables.add(table);
    }

    public LatLng getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
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

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public HashMap<Integer, HashMap<String, String>> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(HashMap<Integer, HashMap<String, String>> openingHours) {
        this.openingHours = openingHours;
    }

    public String getPictureReference() {
        return pictureReference;
    }

    public void setPictureReference(String pictureReference) {
        this.pictureReference = pictureReference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
