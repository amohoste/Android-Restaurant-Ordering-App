package com.example.aggoetey.myapplication.discover.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.fragments.DiscoverFragment;
import com.example.aggoetey.myapplication.discover.models.Filter;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FilterActivity extends AppCompatActivity {

    public static final String EXTRA_FILTER = "com.menu.fiter.extra_filter";

    private RadioGroup sortGroup;
    private MaterialRatingBar ratingBar;
    private CheckBox openCheckbox;
    private BubbleSeekBar distanceBar;
    private TextView confirm;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // Enables back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(DiscoverFragment.EXTRA_DISCOVERFILTER);

        sortGroup = findViewById(R.id.filter_sortgroup);
        ratingBar = findViewById(R.id.filter_materialRatingBar);
        openCheckbox = findViewById(R.id.filter_checkbox);
        distanceBar = findViewById(R.id.filter_distance_bar);
        confirm = findViewById(R.id.filter_confirmButton);

        sortGroup.check(filter.getSortMethod() == Filter.SortMethod.ALPHABET ? R.id.radioButton_alphabet : R.id.radioButton_distance);
        ratingBar.setRating((float) filter.getMinRating());
        openCheckbox.setChecked(filter.isOpen());
        distanceBar.setProgress((float) filter.getDistance());
    }


    // Enables back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickConfirm(View v) {
        Filter.SortMethod sortmethod = sortGroup.getCheckedRadioButtonId() == R.id.radioButton_alphabet ? Filter.SortMethod.ALPHABET : Filter.SortMethod.DISTANCE;

        Filter filter = new Filter(sortmethod, ratingBar.getRating(), openCheckbox.isChecked(), distanceBar.getProgress());
        Intent data = new Intent();
        data.putExtra(EXTRA_FILTER, filter);
        setResult(RESULT_OK, data);
        finish();
    }
}
