package com.example.aggoetey.myapplication.discover.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.activities.FilterActivity;
import com.example.aggoetey.myapplication.discover.helpers.PlacetypeStringifier;
import com.example.aggoetey.myapplication.discover.helpers.SearchRestaurantHelper;
import com.example.aggoetey.myapplication.discover.models.Filter;
import com.example.aggoetey.myapplication.discover.models.Restaurant;
import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;

/**
 * Created by amoryhoste on 03/04/2018.
 */
public class DiscoverFragment extends Fragment implements MapsFragment.Callbacks, RestaurantProvider.AsyncListener, CurrentLocationProvider.LocationListener {

    // Tags
    private static final String TAG_LOCATION_PROVIDER = "LocationProvider";
    private static final String RESTAURANT_FRAG = "GetRestaurantsFrag";
    private static final int REQUEST_CODE_FILTER = 0;
    public static final String EXTRA_DISCOVERFILTER = "com.menu.discover.extra_filter";
    private static final String SAVE_FILTER = "com.menu.discover.save_filter";

    // Providers
    private CurrentLocationProvider mLocationProvider;
    private RestaurantProvider mRestaurantProvider;

    private FragmentManager fm;
    private LocationFragment currentfragment;
    private FloatingSearchView mSearchView;

    private final SearchRestaurantHelper helper = SearchRestaurantHelper.getInstance();
    private Filter filter;

    public DiscoverFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discover_fragment, container, false);

        if (savedInstanceState != null) {
            filter = savedInstanceState.getParcelable(SAVE_FILTER);
            helper.setFilter(filter);
        } else {
            filter = new Filter(Filter.SortMethod.DISTANCE, 0, false, 0);
        }

        fm = getChildFragmentManager();

        mSearchView = (FloatingSearchView) v.findViewById(R.id.floating_search_view);

        // Setup location provider
        mLocationProvider = (CurrentLocationProvider) fm.findFragmentByTag(TAG_LOCATION_PROVIDER);

        if (mLocationProvider == null) {
            mLocationProvider = CurrentLocationProvider.newInstance();
            fm.beginTransaction().add(mLocationProvider, TAG_LOCATION_PROVIDER).commit();
        }
        mLocationProvider.addLocationListener(this);

        // Setup restaurant provider
        mRestaurantProvider = (RestaurantProvider) fm.findFragmentByTag(RESTAURANT_FRAG);
        if (mRestaurantProvider == null) {
            mRestaurantProvider = RestaurantProvider.newInstance();
            fm.beginTransaction().add(mRestaurantProvider, RESTAURANT_FRAG).commit();
        } else if (mRestaurantProvider.getRestaurants() != null) {
            setupSearchbar();
        }

        // Setup current fragment
        currentfragment = (LocationFragment) fm.findFragmentById(R.id.discover_fragment_container);

        if (currentfragment == null) {
            currentfragment = MapsFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.discover_fragment_container, currentfragment)
                    .commit();
        }

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_filter) {
                    Intent in = new Intent(getContext(), FilterActivity.class);
                    in.putExtra(EXTRA_DISCOVERFILTER, filter);
                    startActivityForResult(in, REQUEST_CODE_FILTER);
                } else if(item.getItemId() == R.id.action_qr){
                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), "Open qr scanner",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void setFragment(int fragment) {
        if (fragment == 0) {
            currentfragment = MapsFragment.newInstance();

        } else {
            currentfragment = RestaurantListFragment.newInstance();
        }
        getChildFragmentManager().beginTransaction()
                .replace(R.id.discover_fragment_container, currentfragment)
                .commit();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onPostExecute(ArrayList<Restaurant> result) {
        if (result != null) {
            setupSearchbar();
        }
    }

    public void setupSearchbar() {

        helper.setRestaurants(mRestaurantProvider.getRestaurants());

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    helper.findSuggestions(getActivity(), newQuery, 5,
                            new SearchRestaurantHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<Restaurant> results) {
                                    mSearchView.swapSuggestions(results);
                                }
                            });
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                ArrayList<Restaurant> result = new ArrayList<>();
                result.add((Restaurant) searchSuggestion);
                if (currentfragment != null) {
                    currentfragment.onSearchResult(result, false);
                }
            }

            @Override
            public void onSearchAction(String query) {
                helper.findRestaurants(getActivity(), query,
                        new SearchRestaurantHelper.onFindRestaurantsListener() {

                            @Override
                            public void onResults(List<Restaurant> results) {
                                if (currentfragment != null) {
                                    currentfragment.onSearchResult(new ArrayList<Restaurant>(results), false);
                                }
                            }

                        });
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                Restaurant restaurantSuggestion = (Restaurant) item;

                leftIcon.setAlpha(0.54f);
                leftIcon.setImageDrawable(getResources().getDrawable(PlacetypeStringifier.getIcon(restaurantSuggestion.getType())));

                textView.setTextColor(Color.parseColor("#95000000"));
                textView.setText(restaurantSuggestion.getBody());
            }

        });

        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                if (currentfragment != null && mRestaurantProvider != null) {
                    currentfragment.onSearchResult(mRestaurantProvider.getRestaurants(), true);
                }
            }
        });
    }

    @Override
    public void onCancelled(ArrayList<Restaurant> result) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_FILTER) {
            if (data == null) {
                return;
            }
            Filter filter = data.getParcelableExtra(FilterActivity.EXTRA_FILTER);
            if (filter != null) {
                this.filter = filter;
                helper.setFilter(filter);
                if (currentfragment != null) {
                    currentfragment.filterResults();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVE_FILTER, filter);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if (mLocationProvider != null) {
            mLocationProvider.removeLocationListener(this);
        }
        super.onStop();
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (helper != null) {
            helper.setLastLocation(location);
        }
    }
}
