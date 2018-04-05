package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.aggoetey.myapplication.menu.MenuFragment;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.tab.TabFragment;

import com.example.aggoetey.myapplication.model.Menu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener, TabFragment.Callbacks {

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                manager.beginTransaction().replace(R.id.fragment_place, MenuFragment.newInstance(restaurant)).commit();
                                break;
                            case R.id.action_pay:
                                manager.beginTransaction().replace(R.id.fragment_place, new PayFragment()).commit();
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

    @Override
    public void onOrderSelected(Tab.Order order) {
        if (findViewById(R.id.order_detail_fragment) == null) {
            // portrait
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(OrderDetailFragment.ORDER_KEY, order);
            startActivity(intent);
        } else {
            // landscape
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_fragment, OrderDetailFragment.newInstance(order))
                    .commit();
        }
    }
}
