package com.example.aggoetey.myapplication.discover.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
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
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.activities.FilterActivity;
import com.example.aggoetey.myapplication.discover.helpers.PlacetypeStringifier;
import com.example.aggoetey.myapplication.discover.helpers.SearchRestaurantHelper;
import com.example.aggoetey.myapplication.discover.services.CurrentLocationProvider;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.discover.models.Filter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment which includes a searchbar and can hold a map / listview with restaurants
 */
public class DiscoverContainerFragment extends Fragment implements DiscoverFragment.Callbacks, RestaurantProvider.AsyncListener, CurrentLocationProvider.LocationListener {

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

    private static final int REQUEST_CODE_FILTER = 0;
    public static final String EXTRA_DISCOVERFILTER = "com.menu.discover.extra_filter";
    private static final String SAVE_FILTER = "com.menu.discover.save_filter";

    // Providers
    private CurrentLocationProvider mLocationProvider;
    private RestaurantProvider mRestaurantProvider;

    private FragmentManager fm;
    private FloatingSearchView mSearchView;
    private final SearchRestaurantHelper helper = SearchRestaurantHelper.getInstance();
    private Filter filter;

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
    public void onResume() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        super.onResume();
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
        mLocationProvider.addLocationListener(this);

        // Setup restaurant provider
        mRestaurantProvider = (RestaurantProvider) fm.findFragmentByTag(RESTAURANT_FRAG);
        if (mRestaurantProvider == null) {
            mRestaurantProvider = RestaurantProvider.getInstance();
            fm.beginTransaction().add(mRestaurantProvider, RESTAURANT_FRAG).commit();
        } else if (mRestaurantProvider.getRestaurants() != null) {
            setupSearchbar();
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
            filter = savedInstanceState.getParcelable(SAVE_FILTER);
            helper.setFilter(filter);
        }  else {
            if (getActivity() != null && getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE) != null) {
                SharedPreferences prefs =  getActivity().getSharedPreferences(DISCOVER_VISIBLE_FRAGMENT, MODE_PRIVATE);
                currentFragmentId = prefs.getInt(CURRENT_FRAGMENT_KEY, 0);
            } else {
                currentFragmentId = 0;
            }
            if (filter == null) {
                filter = new Filter(Filter.SortMethod.DISTANCE, 0, false, 0);
            }
            helper.setFilter(filter);
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

        if (mLocationProvider != null) {
            mLocationProvider.removeLocationListener(this);
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_FILTER, filter);
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
        if (result != null) {
            setupSearchbar();
        }
        setQrButtonEnabled(true);
    }

    @Override
    public void onCancelled(ArrayList<Restaurant> result) {

    }

    public void setupSearchbar() {

        helper.setRestaurants(mRestaurantProvider.getRestaurants());

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                String query = helper.getLastQuery();
                if (!query.equals("")) {
                    mSearchView.setSearchText(query);
                    helper.findSuggestions(getActivity(), query, 5,
                            new SearchRestaurantHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<Restaurant> results) {
                                    mSearchView.swapSuggestions(results);
                                }
                            });
                }
            }

            @Override
            public void onFocusCleared() {
                mSearchView.setSearchBarTitle(mSearchView.getQuery());
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                helper.setLastQuery(newQuery);
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
                if (currentFragmentId != -1) {
                    if (mapsFragment != null) {
                        mapsFragment.onSearchResult(result, false);
                    }
                    if (listFragment != null) {
                        listFragment.onSearchResult(result, false);
                    }
                }
            }

            @Override
            public void onSearchAction(String query) {
                helper.findRestaurants(getActivity(), query,
                        new SearchRestaurantHelper.onFindRestaurantsListener() {

                            @Override
                            public void onResults(List<Restaurant> results) {
                                if (currentFragmentId != -1) {
                                    if (mapsFragment != null) {
                                        mapsFragment.onSearchResult(new ArrayList<Restaurant>(results), false);
                                    }
                                    if (listFragment != null) {
                                        if (results == null || results.size() == 0) {
                                            listFragment.onSearchResult(mRestaurantProvider.getRestaurants(), true);
                                        } else {
                                            listFragment.onSearchResult(new ArrayList<Restaurant>(results), false);
                                        }
                                    }
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
                if (currentFragmentId != -1 && mRestaurantProvider != null) {
                    if (currentFragmentId == MAPS_FRAGMENT_ID) {
                        mapsFragment.onSearchResult(mRestaurantProvider.getRestaurants(), true);
                    } else {
                        listFragment.onSearchResult(mRestaurantProvider.getRestaurants(), true);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

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
                if (mapsFragment != null) {
                    mapsFragment.filterResults();
                }

                if (listFragment != null) {
                    listFragment.filterResults();
                }

            }
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (helper != null) {
            helper.setLastLocation(location);
        }
    }

    private void setQrButtonEnabled(boolean enabled) {
        // Enable qr scanner
         List<MenuItemImpl> list = mSearchView.getCurrentMenuItems();

        if (list != null) {
            for (MenuItem item : list) {
                if (item.getItemId() == R.id.action_qr) {
                    item.setEnabled(enabled);
                }
            }
        }
    }

}
