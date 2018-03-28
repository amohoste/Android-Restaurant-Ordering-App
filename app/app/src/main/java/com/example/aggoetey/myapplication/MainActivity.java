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
import com.example.aggoetey.myapplication.tab.TabFragment;

import com.example.aggoetey.myapplication.model.Menu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener {


    private Menu current_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableBottomNavigation();

        current_menu = new Menu();
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Coca-cola", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Fanta", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Water", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Jupiler", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Stella", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Maes", 2, "blabla", "drinks"));
        current_menu.addMenuItem(new com.example.aggoetey.myapplication.model.MenuItem("Kobe Beef", 26, "blabla", "food"));
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
                        TabFragment tabFragment = new TabFragment();
                        switch (item.getItemId()) {
                            case R.id.action_discover:
                                break;
                            case R.id.action_menu:
                                manager.beginTransaction().replace(R.id.fragment_place, MenuFragment.newInstance(current_menu)).commit();
                                break;
                            case R.id.action_pay:
                                manager.beginTransaction().replace(R.id.fragment_place, tabFragment).commit();
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
}
