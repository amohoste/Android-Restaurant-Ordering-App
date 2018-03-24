package com.example.aggoetey.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        TabListFragment tabFragment = new TabListFragment();
                        switch (item.getItemId()){
                            case R.id.action_discover:
                                break;
                            case R.id.action_menu:
                                manager.beginTransaction().remove(manager.findFragmentById(R.id.fragment_place)).commit();
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
}
