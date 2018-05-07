package com.example.aggoetey.myapplication.discover.helpers;

import android.content.Context;
import android.location.Location;
import android.widget.Filter;

import com.example.aggoetey.myapplication.model.Restaurant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SearchRestaurantHelper {

    private static SearchRestaurantHelper ourInstance;
    private static com.example.aggoetey.myapplication.discover.models.Filter filter;
    private static ArrayList<Restaurant> restaurants;
    private static Location lastLocation;

    public static SearchRestaurantHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new SearchRestaurantHelper();
        }
        return ourInstance;
    }

    private SearchRestaurantHelper() {
        this.filter = new com.example.aggoetey.myapplication.discover.models.Filter(com.example.aggoetey.myapplication.discover.models.Filter.SortMethod.DISTANCE, 0, false, 0);
    }

    public void setFilter(com.example.aggoetey.myapplication.discover.models.Filter filter) {
        this.filter = filter;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setLastLocation(Location loc) {
        this.lastLocation = loc;
    }

    public interface onFindRestaurantsListener {
        void onResults(List<Restaurant> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<Restaurant> results);
    }

    public void findSuggestions(Context context, String query, final int limit,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<Restaurant> suggestions = new ArrayList<>();
                if (constraint != null) {

                    for (Restaurant suggestion : restaurants) {
                        if ((constraint.length() == 0 || suggestion.getBody().toUpperCase().startsWith(constraint.toString().toUpperCase())) && satisfiesFilter(suggestion)) {

                            suggestions.add(suggestion);
                            if (limit != -1 && suggestions.size() == limit) {
                                break;
                            }
                        }
                    }
                }
                suggestions = sortResults(suggestions);
                FilterResults results = new FilterResults();
                results.values = suggestions;
                results.count = suggestions.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<Restaurant>) results.values);
                }
            }
        }.filter(query);

    }


    public void findRestaurants(Context context, String query, final onFindRestaurantsListener listener) {

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                ArrayList<Restaurant> suggestionList = new ArrayList<>();

                if (constraint != null) {

                    for (Restaurant restaurant : restaurants) {
                        if ((constraint.length() == 0 || restaurant.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase())) && satisfiesFilter(restaurant)) {
                            suggestionList.add(restaurant);
                        }
                    }

                }
                suggestionList = sortResults(suggestionList);
                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<Restaurant>) results.values);
                }
            }
        }.filter(query);

    }

    public static boolean satisfiesFilter(Restaurant restaurant) {
        return satisfiesRating(restaurant) && satisfiesOpen(restaurant) && satisfiesLocation(restaurant);
    }

    private static boolean satisfiesRating(Restaurant restaurant) {
        if (restaurant.getRating() == -1) {
            return 0 >= filter.getMinRating();
        } else {
            return restaurant.getRating() >= filter.getMinRating();
        }

    }

    private static boolean satisfiesOpen(Restaurant restaurant) {
        boolean result;
        HashMap<Integer,HashMap<String,String>> openingHours = restaurant.getOpeningHours();
        if (openingHours != null) {
            Calendar calendar = Calendar.getInstance();
            int day = DayConverter.toGoogleDay(calendar.get(Calendar.DAY_OF_WEEK));
            if (openingHours.get(day) != null) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = true;
        }

        return (! filter.isOpen()) || result;
    }

    private static boolean satisfiesLocation(Restaurant restaurant) {
        return lastLocation == null || filter.getDistance() == 0 || (1000 * Haversine.haversine(lastLocation.getLatitude(), lastLocation.getLongitude(), restaurant.getPosition().latitude, restaurant.getPosition().longitude)) <= filter.getDistance();
    }

    public static ArrayList<Restaurant> sortResults(ArrayList<Restaurant> restaurants) {

        Comparator<Restaurant> cmp = (filter.getSortMethod() == com.example.aggoetey.myapplication.discover.models.Filter.SortMethod.ALPHABET) ? new AlphabetComparator() : new DistanceComparator();
        Collections.sort(restaurants, cmp);
        return restaurants;
    }

    private static class DistanceComparator implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant restaurant, Restaurant t1) {
            if (lastLocation != null) {
                Double dis1 = Haversine.haversine(lastLocation.getLatitude(), lastLocation.getLongitude(), restaurant.getPosition().latitude, restaurant.getPosition().longitude);
                Double dis2 = Haversine.haversine(lastLocation.getLatitude(), lastLocation.getLongitude(), t1.getPosition().latitude, t1.getPosition().longitude);
                return dis1.compareTo(dis2);
            } else {
                return 0;
            }

        }
    }

    private static class AlphabetComparator implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant restaurant, Restaurant t1) {
            return restaurant.getTitle().compareTo(t1.getTitle());
        }
    }

    private static String lastQuery = "";

    public void setLastQuery(String query) {
        lastQuery = query;
    }

    public String getLastQuery() {
        return lastQuery;
    }

}