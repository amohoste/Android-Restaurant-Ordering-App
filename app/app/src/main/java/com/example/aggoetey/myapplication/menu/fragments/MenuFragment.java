package com.example.aggoetey.myapplication.menu.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.menu.adapters.MenuFragmentPagerAdapter;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.ViewType;

/**
 * Created by Dries on 26/03/2018.
 * Fragment waarin een menu wordt getoond.
 */
public class MenuFragment extends Fragment implements Listener {
    private static final String ARG_MENUINFO = "menuinfo";
    private MenuInfo menuInfo;

    private ViewPager viewPager;
    private MenuFragmentPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private TextView mMenuRestaurantNameView;
    private Menu optionsMenu;
    private Button mMenuOrderButton;


    private static ViewType viewType = ViewType.LIST_VIEW;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance(MenuInfo menuInfo) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENUINFO, menuInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            menuInfo = (MenuInfo) savedInstanceState.getSerializable(ARG_MENUINFO);
            menuInfo.getCurrentOrder().addListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_MENUINFO, menuInfo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            menuInfo = (MenuInfo) getArguments().getSerializable(ARG_MENUINFO);
            menuInfo.getCurrentOrder().addListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("MenuFragment", "Fragment created");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        //Toolbar initialization
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.menu_toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        mMenuRestaurantNameView = (TextView) v.findViewById(R.id.menu_restaurant_name_view);
        mMenuRestaurantNameView.setText(menuInfo.getRestaurant().getTitle());

        mMenuOrderButton = (Button) v.findViewById(R.id.menu_view_order_button);

        setOrderButtonProperties();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        pagerAdapter = new MenuFragmentPagerAdapter(getChildFragmentManager(), menuInfo, viewType);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        mMenuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuInfo.commitOrder();
                Log.e("MenuFragment", "Adapter size " + menuInfo.getmAdapters().size());
                setOrderButtonProperties();
            }
        });

        return v;
    }


    public void setOrderButtonProperties() {
        if (menuInfo.getCurrentOrder().getOrderItems().size() > 0) {
            mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button) + " (€" + menuInfo.getCurrentOrder().getPrice() + ")");
            mMenuOrderButton.setEnabled(true);
        } else {
            mMenuOrderButton.setText(getResources().getString(R.string.menu_view_order_button));
            mMenuOrderButton.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        toggleViewTypeMenu(this.optionsMenu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        menuInfo.clearAdapters();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_change, menu);
        this.optionsMenu = menu;
        toggleViewTypeMenu(menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.optionsMenu = menu;
        toggleViewTypeMenu(menu);
    }

    private void toggleViewTypeMenu(Menu menu) {
        if (menu != null) {
            int grid = R.id.to_grid_view;
            int list = R.id.to_list_view;
            menu.findItem(grid).setVisible(viewType == ViewType.LIST_VIEW);
            menu.findItem(list).setVisible(viewType == ViewType.GRID_VIEW);
        } else {
            Log.w("MenuFragment ", "MENU NOT FOUND");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("MenuFragment ", item.getItemId() + "");

        switch (item.getItemId()) {
            case R.id.to_grid_view:
                if (viewType == ViewType.LIST_VIEW) {
                    viewType = ViewType.GRID_VIEW;
                    menuInfo.clearAdapters();
                    pagerAdapter.updateViewType(viewType);
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);
                }
                return true;
            case R.id.to_list_view:
                if (viewType == ViewType.GRID_VIEW) {
                    viewType = ViewType.LIST_VIEW;
                    menuInfo.clearAdapters();
                    pagerAdapter.updateViewType(viewType);
                    pagerAdapter.notifyDataSetChanged();
                    toggleViewTypeMenu(this.optionsMenu);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pagerAdapter = null;
        menuInfo.getCurrentOrder().removeListener(this);
    }

    @Override
    public void invalidated() {
        setOrderButtonProperties();
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }
}
