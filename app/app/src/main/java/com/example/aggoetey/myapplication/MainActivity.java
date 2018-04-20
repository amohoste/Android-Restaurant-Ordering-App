package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.aggoetey.myapplication.discover.fragments.DiscoverContainerFragment;
import com.example.aggoetey.myapplication.discover.fragments.DiscoverFragment;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;

import com.example.aggoetey.myapplication.menu.fragments.MenuFragment;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Menu;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.tab.TabFragment;


public class MainActivity extends AppCompatActivity implements TabFragment.Callbacks, DiscoverContainerFragment.RestaurantSelectListener {

    private boolean first = false;
    private static final String FIRST_KEY = "VISIBLE_FRAGMEN";

    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            first = savedInstanceState.getBoolean(FIRST_KEY);
            menuFragment = (MenuFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MenuFragment");
        }

        // Todo Little hacky, do better in second sprint
        Fragment cur = getSupportFragmentManager().findFragmentById(R.id.fragment_place);
        if (cur instanceof DiscoverContainerFragment) {
            ((DiscoverContainerFragment) cur).setRestaurantSelectListener(this);
        }

        setContentView(R.layout.activity_main);

        enableBottomNavigation();

        createTestRestaurant();
    }

    /**
     * Listeners enablen van de bottomnavigationview
     */
    private void enableBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set discovery as visible fragment when app starts
        if (!first) {
            FragmentManager manager = getSupportFragmentManager();
            DiscoverContainerFragment discoverContainerFragment = new DiscoverContainerFragment();
            discoverContainerFragment.setRestaurantSelectListener(this);
            manager.beginTransaction().add(R.id.fragment_place, discoverContainerFragment).commit();
            first = true;
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager manager = getSupportFragmentManager();

                        switch (item.getItemId()) {
                            case R.id.action_discover:
                                DiscoverContainerFragment discoverContainerFragment = new DiscoverContainerFragment();
                                discoverContainerFragment.setRestaurantSelectListener(MainActivity.this);
                                manager.beginTransaction().replace(R.id.fragment_place, discoverContainerFragment).commit();
                                break;
                            case R.id.action_menu:
                                switchToMenu(createTestRestaurant());
                                break;
                            case R.id.action_pay:
                                manager.beginTransaction().replace(R.id.fragment_place, PayFragment.newInstance()).commit();
                                break;
                        }


                        return true;
                    }
                }
        );
    }


    // TODO: AMORY REMOVE THIS WHEN LOADING RESTAURANTS FROM MAPS IS POSSIBLE
    private MenuInfo createTestRestaurant() {
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

        return new MenuInfo(new Restaurant("Chez le Bernadin", current_menu));
    }

    /**
     * Als er geen menu is dat ingeladen moet worden kan je hier null aan meegeven
     * Dan zal het menuFragment zelf kijken of hij al weet welk menu er is en anders een ander menuutje inladen
     */
    private void switchToMenu(MenuInfo menuInfo) {
        FragmentManager manager = getSupportFragmentManager();

        /*
          Placeholder for demo
          ------
          TODO: Remove this once default behaviour without selecting restaurant has been decided.
         */
        if(menuInfo == null){
            Restaurant restaurant = RestaurantProvider.newInstance().getRestaurants().get(0);
            menuInfo = new MenuInfo(restaurant);
        }
        /*
          ----------
         */

        if (menuFragment == null) {
            menuFragment = MenuFragment.newInstance(menuInfo);
        }
        manager.beginTransaction().replace(R.id.fragment_place, menuFragment).commit();

        // Selects the correct item in the view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_menu).setChecked(true);
    }

    /**
     * Bij het selecteren van een order de juiste actie doen
     *
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save MenuFragment instance
        super.onSaveInstanceState(outState);
        outState.putBoolean(FIRST_KEY, first);

        // Save MenuFragment instance
        if(menuFragment != null && menuFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "MenuFragment", menuFragment);
        }
    }


    @Override
    public void onRestaurantSelect(MenuInfo menuInfo) {
        switchToMenu(menuInfo);
    }
}
