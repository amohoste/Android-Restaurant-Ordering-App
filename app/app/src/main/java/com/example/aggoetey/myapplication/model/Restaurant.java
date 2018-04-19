package com.example.aggoetey.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 *
 * Een restaurant model.
 */

//TODO: Make restaurant parcelable to be able to save restaurant in bundles since LatLng is only parcelable

public class Restaurant implements Serializable {

    private String title;
    //TODO: Remove transient once parcelable is achieved
    private transient LatLng position;
    private String address;
    private String phone;
    private double rating;
    private String googlePlaceId;
    private Menu menu;
    private List<Table> tables = new ArrayList<>();

    public Restaurant(String title, Menu menu) {
        this.title = title;
        this.menu = menu;
    }

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


}
