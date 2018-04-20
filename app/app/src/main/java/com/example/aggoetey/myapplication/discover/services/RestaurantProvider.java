package com.example.aggoetey.myapplication.discover.services;


import android.support.v4.app.Fragment;

import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.Restaurant;

import java.util.ArrayList;

/**
 * Helper class that provides restaurants, will fetch from database in sprint 2
 */
public class RestaurantProvider extends Fragment {

    private static final RestaurantProvider mInstance = new RestaurantProvider();

    private ArrayList<Restaurant> list = new ArrayList<>();

    public static RestaurantProvider newInstance() {
        return mInstance;
    }

    public RestaurantProvider() {

        // Dummy restaurants, zullen in Sprint 2 dynamisch van de database gehaald worden
        list.add(new Restaurant("Ally's", new Menu(),51.0116228, 3.703219500000001, "Grotesteenweg-Noord 83, 9052 Gent, Belgium", "+32 9 324 44 42", 4.4,"ChIJW-0XrL5zw0cR5qMWyaOQO3c"));
        list.add(new Restaurant("Hong Thong Thai Restaurant", new Menu(), 51.0107888, 3.702853, "Grotesteenweg-Noord 61, 9052 Gent, Belgium", "+32 9 391 95 21", 3.8,"ChIJTQGh-Ltzw0cRchmw4FtjZbI"));
        list.add(new Restaurant("Woody Sandwichbar", new Menu(), 51.0130622, 3.704545099999999, "Bollebergen 15/A, 9052 Gent, Belgium", "+32 9 221 43 93", 3.5,"ChIJB_Gg6b5zw0cRx52kSGVGQT0"));
        list.add(new Restaurant("Frituur 't Lekkerbekje", new Menu(), 51.0105049, 3.702324200000001, "Grotesteenweg-Noord 51, 9052 Gent, Belgium", "+32 9 330 06 41", 4.3,"ChIJUxDA8Ltzw0cRwWbx7AcryD4"));
        list.add(new Restaurant("Gitane", new Menu(), 51.015314, 3.7062634, "Bollebergen 95, 9052 Gent, Belgium", "+32 9 221 78 87", -1,"ChIJI0UedL9zw0cR_5TZvd3pDWs"));
        list.add(new Restaurant("Amadeus Gent 1", new Menu(), 51.057956, 3.722445, "Plotersgracht 8, 9000 Gent, Belgium", "+32 497 43 85 71", 4.1,"ChIJq3hVrzhxw0cRSC3feblv4iY"));
    }

    public ArrayList<Restaurant> getRestaurants() {
        return list;
    }

}
