package com.example.aggoetey.myapplication.discover.helpers;

import java.util.HashMap;

import com.example.aggoetey.myapplication.R;

public class PlacetypeStringifier {

    private static final PlacetypeStringifier ourInstance = new PlacetypeStringifier();

    public static PlacetypeStringifier getInstance() {
        return ourInstance;
    }

    private static HashMap<String, String> placeTypesMap;
    private static HashMap<String, Integer> placeIconMap;

    private PlacetypeStringifier() {
        placeTypesMap = new HashMap<>();
        placeTypesMap.put("restaurant", "Restaurant");
        placeTypesMap.put("point_of_interest", "Point of interest");
        placeTypesMap.put("furniture_store", "Furniture store");
        placeTypesMap.put("bar", "Bar");
        placeTypesMap.put("amusement_park", "Amusement Park");
        placeTypesMap.put("bakery", "Bakery");
        placeTypesMap.put("cafe", "Cafe");
        placeTypesMap.put("casino", "Casino");
        placeTypesMap.put("city_hall", "City Hall");
        placeTypesMap.put("supermarket", "Supermarket");
        placeTypesMap.put("subway_station", "Subway Station");
        placeTypesMap.put("stadium", "Stadium");
        placeTypesMap.put("shopping_mall", "Shopping mall");
        placeTypesMap.put("school", "School");
        placeTypesMap.put("night_club", "Night club");
        placeTypesMap.put("movie_theater", "Movie Theater");
        placeTypesMap.put("establishment", "Establishment mall");
        placeTypesMap.put("food", "Food");
        placeTypesMap.put("locality", "Locality");

        placeIconMap = new HashMap<>();
        placeIconMap.put("restaurant", R.drawable.restaurant_icon);
        placeIconMap.put("bar", R.drawable.bar_icon);
        placeIconMap.put("cafe", R.drawable.cafe_icon);
        placeIconMap.put("place", R.drawable.place_icon);

    }

    public static String stringify(String orig) {
        String result = ourInstance.placeTypesMap.get(orig);
        if (result != null) {
            return result;
        } else {
            return orig;
        }
    }

    public static int getIcon(String type) {
        if (ourInstance.placeIconMap.containsKey(type)) {
            return ourInstance.placeIconMap.get(type);
        } else {
            return ourInstance.placeIconMap.get("place");
        }
    }
}
