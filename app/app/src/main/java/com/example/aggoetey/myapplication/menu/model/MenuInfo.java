package com.example.aggoetey.myapplication.menu.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.menu.adapters.MenuListAdapter;
import com.example.aggoetey.myapplication.menu.services.RestaurantMenuLoader;
import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Dries on 6/04/2018.
 *
 * Klasse dat alle nodige informatie bijhoudt om de huidige bestelling
 * te kunnen tonen op de menuview van een restaurant.
 */

public class MenuInfo implements Serializable {

    private Restaurant restaurant;
    private HashMap<String, Integer> orderCountMap;
    private HashSet<RecyclerView.Adapter> mAdapters;
    private Tab.Order currentOrder;


    public MenuInfo(Restaurant restaurant) {
        this.restaurant = restaurant;

        // Load the restaurant's menu from the FireStore backend
        if (restaurant.getMenu() == null) {
            new RestaurantMenuLoader(restaurant);
        }

        orderCountMap = new HashMap<>();
        mAdapters = new HashSet<>();
        currentOrder = Tab.getInstance().newOrder();
    }

    public HashMap<String, Integer> getOrderCountMap() {
        return orderCountMap;
    }

    public HashSet<RecyclerView.Adapter> getmAdapters() {
        return mAdapters;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Tab.Order getCurrentOrder() {
        return currentOrder;
    }

    public void commitOrder() {
        if (currentOrder.getOrderItems().size() > 0) {
            List<Listener> listenerList = currentOrder.getListeners();
            Tab.getInstance().commitOrder(currentOrder);
            currentOrder = Tab.getInstance().newOrder();
            currentOrder.setListeners(listenerList);
            orderCountMap.clear();
            notifyAllAdapters();
        }
    }

    public void notifyAllAdapters() {
        for (RecyclerView.Adapter adapter : mAdapters) {
            Log.i("MenuInfo", mAdapters.size() + "");
            adapter.notifyDataSetChanged();
        }
    }
    public void clearAdapters(){

        Log.e("Menuinfo", "Cleared Adapters!");
        this.mAdapters.clear();
    }

    public void addAdapter(RecyclerView.Adapter adapter){

        mAdapters.add(adapter);
    }

    public void removeAdapter(RecyclerView.Adapter adapter) {
        mAdapters.remove(adapter);
    }


    public int changeOrderCount(int i, String itemID) {
        int c = i + (orderCountMap.containsKey(itemID) ? orderCountMap.get(itemID) : 0);
        if (c >= 0) {
            orderCountMap.put(itemID, c);
        }
        return c;
    }

    public void addOrderItem(MenuItem menuItem) {
        currentOrder.addOrderItem("", menuItem);
        changeOrderCount(1, menuItem.id);
    }

    public boolean removeOrderItem(MenuItem menuItem) {
        if (changeOrderCount(-1, menuItem.id) >= 0) {
            currentOrder.removeOrderItem(menuItem);
            return true;
        }
        return false;
    }

    public String getOrderCount(String itemID) {
        if (orderCountMap.containsKey(itemID)) {
            return Integer.toString(orderCountMap.get(itemID));
        } else {
            return "0";
        }
    }
}