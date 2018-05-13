package com.example.aggoetey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aggoetey.myapplication.discover.fragments.DiscoverContainerFragment;
import com.example.aggoetey.myapplication.discover.services.RestaurantProvider;
import com.example.aggoetey.myapplication.menu.fragments.MenuFragmentContainer;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.Restaurant;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.model.Table;
import com.example.aggoetey.myapplication.pay.OrderDetailActivity;
import com.example.aggoetey.myapplication.pay.fragments.OrderDetailFragment;
import com.example.aggoetey.myapplication.pay.fragments.PayContainerFragment;
import com.example.aggoetey.myapplication.pay.fragments.TabFragment;
import com.example.aggoetey.myapplication.pay.fragments.tabfragmentpage.TabPageFragment;
import com.example.aggoetey.myapplication.qrscanner.activity.QRScannerActivity;


public class MainActivity extends AppCompatActivity implements TabPageFragment.OrderSelectedListener, DiscoverContainerFragment.RestaurantSelectListener, TabFragment.LogoutListener {


    private static final String DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG";
    private static final String MENU_FRAGMENT_CONTAINER_TAG = "MENU_FRAGMENT_CONTAINER_TAG";
    private static final String PAY_FRAGMENT_TAG = "PAY_FRAGMENT_TAG";

    private static final String DEBUG = "DEBUG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        enableBottomNavigation();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportFragmentManager().getFragments().size() == 0) {
            // als er nog geen fragments zijn dan gaan we naar discover
            // dit is het geval als we de app voor de eerste keer opstarten
            switchToDiscover();
        }
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
                        switch (item.getItemId()) {
                            case R.id.action_discover:
                                switchToDiscover();
                                break;
                            case R.id.action_menu:
                                switchToMenu();
                                break;
                            case R.id.action_pay:
                                switchToPay();
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
    private void switchToMenu() {
        FragmentManager manager = getSupportFragmentManager();
        MenuFragmentContainer menuFragmentContainer = (MenuFragmentContainer) manager.findFragmentByTag(MENU_FRAGMENT_CONTAINER_TAG);

        if (menuFragmentContainer == null) {
            menuFragmentContainer = MenuFragmentContainer.newInstance();
        }

        manager.beginTransaction().replace(R.id.fragment_place, menuFragmentContainer, MENU_FRAGMENT_CONTAINER_TAG)
                .addToBackStack(MENU_FRAGMENT_CONTAINER_TAG).commit();
    }

    private void switchToDiscover() {
        FragmentManager manager = getSupportFragmentManager();
        DiscoverContainerFragment discoverContainerFragment = (DiscoverContainerFragment) manager.findFragmentByTag(DISCOVER_FRAGMENT_TAG);
        if (discoverContainerFragment == null) {
            discoverContainerFragment = DiscoverContainerFragment.newInstance();
        }
        manager.beginTransaction().replace(R.id.fragment_place, discoverContainerFragment, DISCOVER_FRAGMENT_TAG)
                .addToBackStack(DISCOVER_FRAGMENT_TAG).commit();
    }

    private void switchToPay() {
        FragmentManager manager = getSupportFragmentManager();
        PayContainerFragment payContainerFragment = (PayContainerFragment) manager.findFragmentByTag(PAY_FRAGMENT_TAG);
        if (payContainerFragment == null) {
            payContainerFragment = PayContainerFragment.newInstance();
        }
        manager.beginTransaction().replace(R.id.fragment_place, payContainerFragment)
                .addToBackStack(PAY_FRAGMENT_TAG).commit();
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
        if (Tab.getInstance().canLogout()) {
            Tab.getInstance().logout();
        } else {
            Toast.makeText(getApplicationContext(), R.string.open_bill, Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.action_menu).performClick();
    }

    public void startQRScannerActivity() {
        Intent qrIntent = new Intent(this, QRScannerActivity.class);
        startActivityForResult(qrIntent, QRScannerActivity.QR_CODE_REQUEST);
        Toast.makeText(getApplicationContext(), "Open QR-scanner",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            {
                return;
            }
        }

        if (requestCode == QRScannerActivity.QR_CODE_REQUEST) {
            if (data == null) {
                return;
            }
            String code = data.getStringExtra(QRScannerActivity.EXTRA_ANSWER_SHOWN);

            // Split scanned code into restaurant_id and table_id
            Log.d("QR RESULTS", code);
            String[] ids = code.split(":");
            String restaurant_id = ids[0];
            String table_id = ids.length == 2 ? ids[1] : null;


            // TODO SITT: move this validation check to QRActivity
            // TODO: add table validation check (+ make it possible to only scan table once restaurant is filled)
            Restaurant restaurant = RestaurantProvider.getInstance().getRestaurant(restaurant_id);

            if (restaurant == null) {
                Toast.makeText(this, R.string.qr_code_not_recognized, Toast.LENGTH_SHORT)
                        .show();
            } else if (Tab.getInstance().canLogout()) {
                // Load restaurant into MenuInfo
                Tab.getInstance().setRestaurant(restaurant);
                Tab.getInstance().setTable(new Table(null, table_id));
                findViewById(R.id.action_menu).performClick();
            } else {
                Toast.makeText(getApplicationContext(), R.string.open_bill, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void loggedOut() {
        findViewById(R.id.action_discover).performClick();
    }
}
