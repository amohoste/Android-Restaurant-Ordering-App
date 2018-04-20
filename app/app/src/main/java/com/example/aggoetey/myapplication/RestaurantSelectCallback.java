package com.example.aggoetey.myapplication;

import com.example.aggoetey.myapplication.menu.model.MenuInfo;

/**
 * Amory deze interface moet je in je fragment zetten. je noemt deze dan gewoon Callbacks (zoals in TabFragment)
 */
public interface RestaurantSelectCallback {
    void onRestaurantSelect(MenuInfo menuInfo);
}