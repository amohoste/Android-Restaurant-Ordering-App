package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.aggoetey.myapplication.menu.fragments.MenuFragment;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.tab.TabFragment;

import com.example.aggoetey.myapplication.model.Menu;

public class MainActivity extends AppCompatActivity implements TabFragment.Callbacks {


    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            menuFragment = (MenuFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MenuFragment");
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

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager manager = getSupportFragmentManager();

                        switch (item.getItemId()) {
                            case R.id.action_discover:
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


    /**
     * Als er geen menu is dat ingeladen moet worden kan je hier null aan meegeven
     * Dan zal het menuFragment zelf kijken of hij al weet welk menu er is en anders een ander menuutje inladen
     */
    private void switchToMenu(MenuInfo menuInfo) {
        FragmentManager manager = getSupportFragmentManager();
        if (menuFragment == null) {
            menuFragment = MenuFragment.newInstance(menuInfo);
        }
        manager.beginTransaction().replace(R.id.fragment_place, menuFragment).commit();

        // Selects the correct item in the view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.action_menu).setChecked(true);
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

        return new MenuInfo(new Restaurant("Chez Cyka Blyat", current_menu));
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

    /**
     * Bij het selecteren van een Restaurant
     */
    public void onRestaurantSelect(MenuInfo menuInfo) {
        switchToMenu(menuInfo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save MenuFragment instance
        if(menuFragment != null) {
            getSupportFragmentManager().putFragment(outState, "MenuFragment", menuFragment);
        }
    }
}
