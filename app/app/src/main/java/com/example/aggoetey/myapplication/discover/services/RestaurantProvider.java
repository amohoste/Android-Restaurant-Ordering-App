package com.example.aggoetey.myapplication.discover.services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aggoetey.myapplication.discover.helpers.KeyProvider;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantProvider extends Fragment {

    // Listner for results / updates
    private AsyncListener listener;

    public interface AsyncListener {
        void onPreExecute();
        void onProgressUpdate(Integer... progress);
        void onPostExecute(ArrayList<Restaurant> result);
        void onCancelled(ArrayList<Restaurant> result);
    }

    // Listeners
    private static ArrayList<RestaurantListener> restaurantListeners;

    public interface RestaurantListener {
        void onRestaurantUpdate(ArrayList<Restaurant> restaurants);
    }

    public void addRestaurantListener(RestaurantListener restaurantListener) {
        restaurantListeners.add(restaurantListener);
    }

    public void removeRestaurantListener(RestaurantListener restaurantListener) {
        restaurantListeners.remove(restaurantListener);
    }

    private void notifyRestaurantListeners() {
        if (restaurantListeners.size() != 0) {
            for (RestaurantListener listener : restaurantListeners) {
                listener.onRestaurantUpdate(restaurantList);
            }
        }
    }

    // Constants
    private static String API_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";

    // Asynctask
    private RequestQueue queue;
    private GetRestaurantsTask restaurantTask;

    ArrayList<Restaurant> restaurantList;

    public ArrayList<Restaurant> getRestaurants() {
        return restaurantList;
    }


    public static RestaurantProvider newInstance() {
        RestaurantProvider myFragment = new RestaurantProvider();
        restaurantListeners = new ArrayList<>();
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        API_KEY = KeyProvider.getPlacesApiKey(getContext());
        restaurantList = new ArrayList<>();
        queue = Volley.newRequestQueue(getContext());
        restaurantTask = new GetRestaurantsTask();

        // Get restaurant ids from FireBase and start get restaurants task
        CollectionReference mColRef = FirebaseFirestore.getInstance().collection("places");
        mColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    ArrayList<String> ids = new ArrayList<>();

                    for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                        ids.add(document.getId());
                    }

                    restaurantTask.execute(ids);
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AsyncListener) getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private class GetRestaurantsTask extends AsyncTask<ArrayList<String>, Integer, ArrayList<Restaurant>> {

        private int amount = 0;

        @Override
        protected void onPreExecute() {
            if (listener != null) {
                listener.onPreExecute();
            }
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(ArrayList<String>... ids) {

            amount = ids[0].size();
            for (int i = 0; i < ids[0].size(); i++) {
                final String id = ids[0].get(i);
                final String url = BASE_URL + id + "&key=" + API_KEY;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Restaurant res = parseRestaurant(response);
                                if (res != null && res.getTitle() != null) {
                                    res.setGooglePlaceId(id);
                                    addRestaurant(res);
                                } else {
                                    addRestaurant(null);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("Menu_App", error.toString());
                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }
            return null;
        }

        private void addRestaurant(Restaurant restaurant) {
            if (restaurant != null) {
                restaurantList.add(restaurant);
            } else {
                amount -=1;
            }

            boolean last = (amount == restaurantList.size());
            if (last) {
                if (listener != null) {
                    listener.onPostExecute(restaurantList);
                }
                notifyRestaurantListeners();
            }
        }

        private Restaurant parseRestaurant(JSONObject response) {
            Restaurant restaurant = new Restaurant();

            try {
                JSONObject result = response.getJSONObject("result");

                if (result.has("name")) {
                    restaurant.setTitle(result.getString("name"));
                }

                if (result.has("name")) {
                    restaurant.setAddress(result.getString("formatted_address"));
                }

                if (result.has("international_phone_number")) {
                    restaurant.setPhone(result.getString("international_phone_number"));
                }

                if (result.has("rating")) {
                    restaurant.setRating(result.getDouble("rating"));
                } else {
                    restaurant.setRating(-1);
                }

                if (result.has("geometry")) {
                    JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                    restaurant.setPosition(new LatLng(location.getDouble("lat"), location.getDouble("lng")));
                }

                if (result.has("photos")) {
                    JSONObject photo = result.getJSONArray("photos").getJSONObject(0);
                    restaurant.setPictureReference(photo.getString("photo_reference"));
                }

                if (result.has("opening_hours")) {
                    // Parse opening hours into hashmap
                    JSONArray openinghours = result.getJSONObject("opening_hours").getJSONArray("periods");
                    HashMap<Integer,HashMap<String,String>> hours = new HashMap<>();
                    for (int i = 0; i < openinghours.length(); i++) {
                        HashMap<String,String> tmp = new HashMap<>();
                        JSONObject curday = openinghours.getJSONObject(i);

                        // Parse opening hours
                        JSONObject open = curday.getJSONObject("open");
                        String openhours = open.getString("time");
                        tmp.put("open", openhours.substring(0, 2) + ":" + openhours.substring(2, 4));

                        // Parse closing hours
                        JSONObject close = curday.getJSONObject("close");
                        String closehours = close.getString("time");
                        tmp.put("close", closehours.substring(0, 2) + ":" + closehours.substring(2, 4));

                        // Put in hashmap
                        int day = open.getInt("day");
                        hours.put(day, tmp);
                    }
                    restaurant.setOpeningHours(hours);
                }

                if(result.has("types")) {
                    restaurant.setType(result.getJSONArray("types").getString(0));
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return restaurant;
        }

    }
}
