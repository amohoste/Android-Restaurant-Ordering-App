package com.example.aggoetey.myapplication.discover.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;

/**
 * Fragment which includes a searchbar and can hold a map / listview with restaurants
 */
public class DiscoverContainerFragment extends Fragment implements MapsFragment.Callbacks {

    // Constants
    public static final int MAPS_FRAGMENT_ID = 0;
    public static final int LIST_FRAGMENT_ID = 1;

    // Tags
    private static final String TAG_LOCATION_PROVIDER = "LocationProvider";
    private static final String RESTAURANT_FRAG = "GetRestaurantsFrag";

    // Providers
    private CurrentLocationProvider mLocationProvider;
    private RestaurantProvider mRestaurantProvider;

    private FragmentManager fm;
    private DiscoverFragment currentfragment;
    private FloatingSearchView mSearchView;

    // Interface to open menu
    private RestaurantSelectListener mListener;
    public interface RestaurantSelectListener {
        void onRestaurantSelect(MenuInfo menuInfo);
    }

    public DiscoverContainerFragment() {

    }

    public static DiscoverContainerFragment newInstance() {
        DiscoverContainerFragment fragment = new DiscoverContainerFragment();
        return fragment;
    }

    public void setRestaurantSelectListener(RestaurantSelectListener listener) {
        this.mListener = listener;
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

        // Setup current fragment
        currentfragment = (DiscoverFragment) fm.findFragmentById(R.id.discover_fragment_container);

        if (currentfragment == null) {
            currentfragment = MapsFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, currentfragment)
                    .commit();
        }

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
     * Change discover view fragment (map / list)
     */
    @Override
    public void setFragment(int fragment) {
        if (fragment == MAPS_FRAGMENT_ID) {
            currentfragment = MapsFragment.newInstance();

        } else {
            currentfragment = RestaurantListFragment.newInstance();
        }
        getChildFragmentManager().beginTransaction()
                .replace(R.id.discover_fragment_container, currentfragment)
                .commit();
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
        super.onStop();
    }

}
