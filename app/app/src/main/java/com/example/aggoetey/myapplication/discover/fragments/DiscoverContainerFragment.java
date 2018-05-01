package com.example.aggoetey.myapplication.discover.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment which includes a searchbar and can hold a map / listview with restaurants
 */
public class DiscoverContainerFragment extends Fragment implements DiscoverFragment.Callbacks, RestaurantProvider.AsyncListener {

    // Constants
    public static final int MAPS_FRAGMENT_ID = 0;
    public static final int LIST_FRAGMENT_ID = 1;

    private static final String CURRENT_FRAGMENT_KEY = "CURRENT-FRAGMENT-KEY";
    private static final String DISCOVER_VISIBLE_FRAGMENT = "DISCOVER_VISIBLE_FRAGMENT";
    private int currentFragmentId = -1;

    // Tags
    private static final String TAG_LOCATION_PROVIDER = "LocationProvider";
    private static final String RESTAURANT_FRAG = "GetRestaurantsFrag";
    private static final String TAG_MAPS = "MapsFragment";
    private static final String TAG_LIST = "ListFragment";

    // Providers
    private CurrentLocationProvider mLocationProvider;
    private RestaurantProvider mRestaurantProvider;

    private FragmentManager fm;
    private FloatingSearchView mSearchView;

    // Interface to open menu
    private RestaurantSelectListener mListener;



    public interface RestaurantSelectListener {
        void onRestaurantSelect(MenuInfo menuInfo);
    }
    private MapsFragment mapsFragment;
    private RestaurantListFragment listFragment;

    public DiscoverContainerFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (RestaurantSelectListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static DiscoverContainerFragment newInstance() {
        DiscoverContainerFragment fragment = new DiscoverContainerFragment();
        return fragment;
    }

    public RestaurantSelectListener getSelectListener() {
        return mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        View v = inflater.inflate(R.layout.discover_fragment, container, false);

        fm = getChildFragmentManager();

        mSearchView = (FloatingSearchView) v.findViewById(R.id.floating_search_view);

        // Setup location provider
        mLocationProvider = (CurrentLocationProvider) fm.findFragmentByTag(TAG_LOCATION_PROVIDER);

        if (mLocationProvider == null) {
            mLocationProvider = CurrentLocationProvider.newInstance();
            fm.beginTransaction().add(mLocationProvider, TAG_LOCATION_PROVIDER).commit();
        }

        // Setup restaurant provider
        mRestaurantProvider = (RestaurantProvider) fm.findFragmentByTag(RESTAURANT_FRAG);
        if (mRestaurantProvider == null) {
            mRestaurantProvider = RestaurantProvider.newInstance();
            fm.beginTransaction().add(mRestaurantProvider, RESTAURANT_FRAG).commit();
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
        } else if (getActivity() != null && getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE) != null) {
            SharedPreferences prefs =  getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE);
            currentFragmentId = prefs.getInt(CURRENT_FRAGMENT_KEY, 0);
        } else {
            currentFragmentId = 0;
        }

        // Set up fragments
        mapsFragment = (MapsFragment) fm.findFragmentByTag(TAG_MAPS) ;
        if (mapsFragment == null) {
            mapsFragment = MapsFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, mapsFragment, TAG_MAPS)
                    .commit();
        }

        listFragment = (RestaurantListFragment) fm.findFragmentByTag(TAG_LIST);
        if (listFragment == null) {
            listFragment = RestaurantListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, listFragment, TAG_LIST)
                    .commit();
        }

        showCurrentFragment();

        // Listen to search view clicks
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_filter) {
                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), "Open filter",
                            Toast.LENGTH_SHORT).show();
                } else if(item.getItemId() == R.id.action_qr){
                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), "Open qr scanner",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    /**
     * Shows Current Fragment
     */
    private void showCurrentFragment() {
        if (currentFragmentId == MAPS_FRAGMENT_ID) {
            getChildFragmentManager().beginTransaction().show(mapsFragment).commit();
            getChildFragmentManager().beginTransaction().hide(listFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().hide(mapsFragment).commit();
            getChildFragmentManager().beginTransaction().show(listFragment).commit();
        }
    }

    /**
     * Change discover view fragment (map / list)
     */
    @Override
    public void setFragment(int fragment) {
        if (fragment == MAPS_FRAGMENT_ID) {
            currentFragmentId = MAPS_FRAGMENT_ID;
            getChildFragmentManager().beginTransaction().hide(listFragment).commit();
            getChildFragmentManager().beginTransaction().show(mapsFragment).commit();

        } else {
            currentFragmentId = LIST_FRAGMENT_ID;
            getChildFragmentManager().beginTransaction().hide(mapsFragment).commit();
            getChildFragmentManager().beginTransaction().show(listFragment).commit();
        }

    }

    @Override
    public CurrentLocationProvider getLocationProvider() {
        return mLocationProvider;
    }

    @Override
    public RestaurantProvider getRestaurantProvider() {
        return mRestaurantProvider;
    }


    @Override
    public void onStop() {
        if (getActivity() != null && getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE) != null) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE).edit();
            editor.putInt(CURRENT_FRAGMENT_KEY, currentFragmentId);
            editor.apply();
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentFragmentId != -1) {
            outState.putInt(CURRENT_FRAGMENT_KEY, currentFragmentId);
        }

    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onPostExecute(ArrayList<Restaurant> result) {

    }

    @Override
    public void onCancelled(ArrayList<Restaurant> result) {

    }

}
