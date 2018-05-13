package com.example.aggoetey.myapplication.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.Model;

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

public class MenuInfo extends Model implements Serializable {
    private HashMap<String, Integer> orderCountMap;
    // Adapters can be transient since trying to invalidate old adapters after being serialized doesn't make
    // sense and adapters here are listeners after all.
    private transient HashSet<RecyclerView.Adapter> mAdapters;
    private Tab.Order currentOrder;
    private boolean orderSendInProgress = false;


    private static final MenuInfo instance = new MenuInfo();

    private MenuInfo (){
        orderCountMap = new HashMap<>();
        mAdapters = new HashSet<>();
        currentOrder = Tab.getInstance().newOrder();
    }
    public static MenuInfo getInstance() {
        return instance;
    }
    
    public MenuInfo reset() {
        orderCountMap = new HashMap<>();
        mAdapters = new HashSet<>();
        currentOrder = Tab.getInstance().newOrder();
        return instance;
    }


    public HashMap<String, Integer> getOrderCountMap() {
        return orderCountMap;
    }

    public HashSet<RecyclerView.Adapter> getmAdapters() {
        return mAdapters;
    }

    public Tab.Order getCurrentOrder() {
        return currentOrder;
    }

    public void orderCommitted() {
        List<Listener> listenerList = currentOrder.getListeners();
        currentOrder = Tab.getInstance().newOrder();
        currentOrder.setListeners(listenerList);
        currentOrder.fireInvalidationEvent();
        orderCountMap.clear();
        notifyAllAdapters();
    }

    public void notifyAllAdapters() {
        for (RecyclerView.Adapter adapter : mAdapters) {
            Log.i("MenuInfo", mAdapters.size() + "");
            adapter.notifyDataSetChanged();
        }
    }
    public void clearAdapters(){
        if(this.mAdapters != null) {
            this.mAdapters.clear();
        }
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

    public boolean addOrderItem(MenuItem menuItem) {
        if (!orderSendInProgress) {
            currentOrder.addOrderItem("", menuItem);
            changeOrderCount(1, menuItem.id);
            return true;
        }
        return false;
    }

    public boolean removeOrderItem(MenuItem menuItem) {
        if (!orderSendInProgress && changeOrderCount(-1, menuItem.id) >= 0) {
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

    public void setCurrentOrder(Tab.Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public void fireInvalidationEvent() {
        super.fireInvalidationEvent();
    }

    public boolean isOrderSendInProgress() {
        return orderSendInProgress;
    }

    public void setOrderSendInProgress(boolean orderSendInProgress) {
        this.orderSendInProgress = orderSendInProgress;
    }
}
