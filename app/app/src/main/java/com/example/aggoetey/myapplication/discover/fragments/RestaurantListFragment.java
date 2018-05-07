package com.example.aggoetey.myapplication.discover.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.adapters.RestaurantListAdapter;
import com.example.aggoetey.myapplication.discover.helpers.SearchRestaurantHelper;
import com.example.aggoetey.myapplication.discover.views.ClickableImageView;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;

/**
 * Fragment that displays a list of restaurants
 */
public class RestaurantListFragment extends DiscoverFragment implements View.OnClickListener, RestaurantListAdapter.OnRestaurantClickListener {

    private RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    public static RestaurantListFragment newInstance() {
        RestaurantListFragment fragment = new RestaurantListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mCallbacks = (Callbacks) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discover_list_fragment, container, false);

        ClickableImageView mapButton = (ClickableImageView) v.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.restaurants_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RestaurantListAdapter(getContext());
        mAdapter.setRestaurantClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        restaurantProvider = mCallbacks.getRestaurantProvider();
        restaurantProvider.addRestaurantListener(this);
        ArrayList<Restaurant> restaurants = restaurantProvider.getRestaurants();
        if (restaurants != null) {
            restaurants = SearchRestaurantHelper.sortResults(restaurants);
            mAdapter.setRestaurants(restaurants);
        }

        locationProvider = mCallbacks.getLocationProvider();

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mapButton:
                onClickMapButton();
                break;
            default:
                break;
        }
    }

    public void onClickMapButton() {
        mCallbacks.setFragment(DiscoverContainerFragment.MAPS_FRAGMENT_ID);
    }


    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        // Todo open restaurant
        DiscoverContainerFragment parent = (DiscoverContainerFragment) getParentFragment();
        DiscoverContainerFragment.RestaurantSelectListener mListener =  parent.getSelectListener();
        if (mListener != null) {
            parent.getSelectListener().onRestaurantSelect(new MenuInfo(restaurant));
        }
        // Toast.makeText(getContext(), restaurant.getGooglePlaceId() + " clicked!" + " open menu...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRestaurantUpdate(ArrayList<Restaurant> restaurants) {
        if (mAdapter != null && restaurants != null) {
            mAdapter.setRestaurants(restaurants);
        }
    }

    @Override
    public void onStop() {
        if (restaurantProvider != null) {
            restaurantProvider.removeRestaurantListener(this);
        }
        super.onStop();
    }

    @Override
    void onSearchResult(ArrayList<Restaurant> result, boolean clear) {
        this.searchedRestaurants = result;
        filterResults();
    }

    @Override
    void filterResults() {
        if (mAdapter != null && searchedRestaurants != null) {
            ArrayList<Restaurant> result = new ArrayList<>();

            for (Restaurant restaurant : searchedRestaurants) {
                if (SearchRestaurantHelper.satisfiesFilter(restaurant)) {
                    result.add(restaurant);
                }
            }
            result = SearchRestaurantHelper.sortResults(result);
            mAdapter.setRestaurants(result);
        }
    }

}
