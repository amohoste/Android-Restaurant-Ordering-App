package com.example.aggoetey.myapplication.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.note.adapters.NotesFragmentPagerAdapter;

public class NotesActivity extends AppCompatActivity {

    public static  final String ARG_MENU_INFO = "ARG_MENU_INFO";
    public static final int RESULT_MENU_INFO_SET = 200;
    private MenuInfo menuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuInfo =  MenuInfo.getInstance();
        setContentView(R.layout.activity_notes);
        setupViewPager(menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveMenuInfo();
        super.onBackPressed();
    }

    private void saveMenuInfo() {
        Intent mIntent = new Intent();
        mIntent.putExtra(ARG_MENU_INFO, menuInfo);
        setResult(RESULT_OK, mIntent);
    }

    private void setupViewPager(MenuInfo menuInfo) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.notes_view_pager);
        NotesFragmentPagerAdapter pagerAdapter = new NotesFragmentPagerAdapter(getSupportFragmentManager(), menuInfo);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.notes_sliding_tab);
        tabLayout.setupWithViewPager(viewPager,true);

        Log.e("notes", menuInfo.getCurrentOrder().getOrderItems().size() +"");
        pagerAdapter.notifyDataSetChanged();
    }
}
