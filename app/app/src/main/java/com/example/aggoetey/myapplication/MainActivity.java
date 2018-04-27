package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.aggoetey.myapplication.discover.fragments.DiscoverContainerFragment;

import com.example.aggoetey.myapplication.menu.fragments.MenuFragmentContainer;
import com.example.aggoetey.myapplication.menu.fragments.NoMenuSelectedFragment;
import com.example.aggoetey.myapplication.menu.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.pay.orderdetail.OrderDetailActivity;
import com.example.aggoetey.myapplication.pay.orderdetail.OrderDetailFragment;
import com.example.aggoetey.myapplication.pay.PayFragment;
import com.example.aggoetey.myapplication.pay.TabFragment;


public class MainActivity extends AppCompatActivity implements TabFragment.Callbacks, DiscoverContainerFragment.RestaurantSelectListener {

    private static final String MENU_OBJECT_KEY = "MENU_OBJECT_KEY";
    private MenuInfo menuInfo;

    private static final String DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG";
    private static final String MENU_FRAGMENT_CONTAINER_TAG = "MENU_FRAGMENT_CONTAINER_TAG";
    private static final String PAY_FRAGMENT_TAG = "PAY_FRAGMENT_TAG";

    private static final String DEBUG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            menuInfo = (MenuInfo) savedInstanceState.getSerializable(MENU_OBJECT_KEY);
        }

        // Todo Little hacky, do better in second sprint
        Fragment cur = getSupportFragmentManager().findFragmentById(R.id.fragment_place);
        if (cur instanceof DiscoverContainerFragment) {
            ((DiscoverContainerFragment) cur).setRestaurantSelectListener(this);
        }

        setContentView(R.layout.activity_main);

        enableBottomNavigation();
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
                                DiscoverContainerFragment discoverContainerFragment = (DiscoverContainerFragment) manager.findFragmentByTag(DISCOVER_FRAGMENT_TAG);
                                if(discoverContainerFragment == null) {
                                    discoverContainerFragment = DiscoverContainerFragment.newInstance();
                                    discoverContainerFragment.setRestaurantSelectListener(MainActivity.this);
                                }
                                manager.beginTransaction().replace(R.id.fragment_place, discoverContainerFragment, DISCOVER_FRAGMENT_TAG)
                                        .addToBackStack(DISCOVER_FRAGMENT_TAG).commit();
                                break;
                            case R.id.action_menu:
                                MenuFragmentContainer menuFragmentContainer = (MenuFragmentContainer) manager.findFragmentByTag(MENU_FRAGMENT_CONTAINER_TAG);
                                if(menuFragmentContainer == null){
                                    menuFragmentContainer = MenuFragmentContainer.newInstance(null);
                                }
                                manager.beginTransaction().replace(R.id.fragment_place, menuFragmentContainer, MENU_FRAGMENT_CONTAINER_TAG)
                                        .addToBackStack(MENU_FRAGMENT_CONTAINER_TAG).commit();
                                break;
                            case R.id.action_pay:
                                PayFragment payFragment = (PayFragment) manager.findFragmentByTag(PAY_FRAGMENT_TAG);
                                if(payFragment == null){
                                    payFragment = PayFragment.newInstance();
                                }
                                manager.beginTransaction().replace(R.id.fragment_place, payFragment)
                                        .addToBackStack(PAY_FRAGMENT_TAG).commit();
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
        this.menuInfo = menuInfo;

        if (menuInfo == null) {
            manager.beginTransaction().replace(R.id.fragment_place, NoMenuSelectedFragment.newInstance()).commit();
        } else {


            // Selects the correct item in the view
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.getMenu().findItem(R.id.action_menu).setChecked(true);
        }
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
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onRestaurantSelect(MenuInfo menuInfo) {
        switchToMenu(menuInfo);
    }
}
