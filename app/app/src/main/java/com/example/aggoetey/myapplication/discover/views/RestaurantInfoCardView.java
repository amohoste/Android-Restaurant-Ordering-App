package com.example.aggoetey.myapplication.discover.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.adapters.RestaurantListAdapter;
import com.example.aggoetey.myapplication.model.Restaurant;

import java.io.Serializable;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by sitt on 18/04/18.
 */

public class RestaurantInfoCardView extends LinearLayout {
    // View fields
    private TextView nameTextView;
    private TextView ratingTextView;
    private TextView hoursTextView;
    private ImageView starImageView;
    private ImageView restaurantImageView;
    private ProgressBar progressbar;
    private TextView placetype;
    private ClickableImageView navigation;

    public RestaurantInfoCardView(Context context) {
        super(context);
        init();
    }

    public RestaurantInfoCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RestaurantInfoCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.discover_restaurant_info_card, this);
        // Initialize views
        nameTextView = (TextView) findViewById(R.id.card_name);
        ratingTextView = (TextView) findViewById(R.id.card_stars);
        hoursTextView = (TextView) findViewById(R.id.card_hours);
        starImageView = (ImageView) findViewById(R.id.star_image);
        restaurantImageView = (ImageView) findViewById(R.id.card_img);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        placetype = (TextView) findViewById(R.id.placetype);

        navigation = (ClickableImageView) findViewById(R.id.card_navigationButton);
        navigation.setVisibility(View.VISIBLE);
        
    }


    public void bind(final Restaurant restaurant) {
        this.nameTextView.setText(restaurant.getTitle());
        Double rating = restaurant.getRating();

        // Check if restaurant has rating
        if (rating >= 0 && rating <= 5) {
            this.ratingTextView.setText(Double.toString(rating));
            this.starImageView.setVisibility(View.VISIBLE);
        } else {
            this.starImageView.setVisibility(View.INVISIBLE);
            this.ratingTextView.setText("");
        }

        if (navigation != null) {
            navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Navigation clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //TODO: get data from backend
        hoursTextView.setText("Hours not known");
        placetype.setText("Bar -placeholder-");

        loadRestaurantPicture(restaurant);
    }

    private void loadRestaurantPicture(Restaurant restaurant) {
        // Transformation to round corners etc.
        MultiTransformation<Bitmap> multi;
        multi = new MultiTransformation<>(
                new FitCenter(),
                new CenterCrop(),
                new RoundedCornersTransformation(7, 0));

        final ProgressBar progressBar = (ProgressBar) progressbar;

        Glide.with(getContext()).clear(restaurantImageView);
        restaurantImageView.setImageDrawable(null);
        progressbar.setVisibility(View.GONE);
        restaurantImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.restaurant_placeholder));
    }


}
