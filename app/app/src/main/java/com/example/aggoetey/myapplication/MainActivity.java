package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.aggoetey.myapplication.discover.fragments.DiscoverContainerFragment;
import com.example.aggoetey.myapplication.menu.MenuFragment;
import com.example.aggoetey.myapplication.menu.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.tab.TabFragment;

import com.example.aggoetey.myapplication.model.Menu;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener, TabFragment.Callbacks {

    private int visibleFragment = -1;
    private static final String VISIBLE_FRAGMENT_KEY = "VISIBLE_FRAGMEN";

    private Restaurant restaurant;
    private MenuInfo menuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            visibleFragment = savedInstanceState.getInt(VISIBLE_FRAGMENT_KEY);
        }

        enableBottomNavigation();

        createTestRestaurant();
        menuInfo = new MenuInfo(restaurant);
    }

    /**
     * Listeners enablen van de bottomnavigationview
     */
    private void enableBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set discovery as visible fragment when app starts
        if (visibleFragment == -1) {
            FragmentManager manager = getSupportFragmentManager();
            DiscoverContainerFragment discoverContainerFragment = new DiscoverContainerFragment();
            manager.beginTransaction().add(R.id.fragment_place, discoverContainerFragment).commit();
            setVisibleFragment(R.id.action_discover);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager manager = getSupportFragmentManager();

                        switch (item.getItemId()) {
                            case R.id.action_discover:
                                if (!isFragmentVisible(R.id.action_discover)) {
                                    DiscoverContainerFragment discoverContainerFragment = new DiscoverContainerFragment();
                                    manager.beginTransaction().replace(R.id.fragment_place, discoverContainerFragment).commit();
                                    setVisibleFragment(R.id.action_discover);
                                }
                                break;
                            case R.id.action_menu:
                                if (!isFragmentVisible(R.id.fragment_place)) {
                                    manager.beginTransaction().replace(R.id.fragment_place, MenuFragment.newInstance(menuInfo)).commit();
                                    setVisibleFragment(R.id.fragment_place);
                                }
                                manager.beginTransaction().replace(R.id.fragment_place, MenuFragment.newInstance(menuInfo)).commit();
                                break;
                            case R.id.action_pay:
                                if (!isFragmentVisible(R.id.action_pay)) {
                                    TabFragment tabFragment = new TabFragment();
                                    manager.beginTransaction().replace(R.id.fragment_place, tabFragment).commit();
                                    setVisibleFragment(R.id.action_pay);
                                }
                                break;
                        }


                        return true;
                    }
                }
        );
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void createTestRestaurant() {
        Menu current_menu = new Menu();
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Coca-cola", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Fanta", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Water", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Jupiler", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Stella", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Maes", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Kobe Beef", 26, "blabla", "food"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Coca-cola", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Fanta", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Water", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Jupiler", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Stella", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Maes", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Kobe Beef", 26, "blabla", "food"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Coca-cola", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Fanta", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Water", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Jupiler", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Stella", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Maes", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Kobe Beef", 26, "blabla", "food"));

        restaurant = new Restaurant("Chez Cyka Blyat", current_menu);
    }


    /**
     * Bij het selecteren van een order de juiste actie doen
     * @param order
     */
    @Override
    public void onOrderSelected(Tab.Order order) {
        if (findViewById(R.id.order_detail_fragment_container) == null) {
            // portrait
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(OrderDetailFragment.ORDER_KEY, order);
            startActivity(intent);
        } else {
            // landscape
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_fragment_container, OrderDetailFragment.newInstance(order))
                    .commit();
        }
    }

    public boolean isFragmentVisible(int id) {
        return visibleFragment == id;
    }

    public void setVisibleFragment(int id) {
        visibleFragment = id;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(VISIBLE_FRAGMENT_KEY, visibleFragment);
        super.onSaveInstanceState(outState);
    }
}
