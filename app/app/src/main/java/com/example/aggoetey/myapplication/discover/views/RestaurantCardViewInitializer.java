package com.example.aggoetey.myapplication.discover.views;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
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

public class RestaurantCardViewInitializer implements Serializable {
    // View fields
    private TextView nameTextView;
    private TextView locationTextView;
    private TextView ratingTextView;
    private TextView hoursTextView;
    private ImageView starImageView;
    private ImageView restaurantImageView;
    private ProgressBar progressbar;
    private View itemView;
    private TextView placetype;
    private ClickableImageView navigation;

    public RestaurantCardViewInitializer(View view, boolean enableNavigation) {

        itemView = view;
        // Initialize views
        nameTextView = (TextView) itemView.findViewById(R.id.card_name);
        locationTextView = (TextView) itemView.findViewById(R.id.card_location);
        ratingTextView = (TextView) itemView.findViewById(R.id.card_stars);
        hoursTextView = (TextView) itemView.findViewById(R.id.card_hours);
        starImageView = (ImageView) itemView.findViewById(R.id.star_image);
        restaurantImageView = (ImageView) itemView.findViewById(R.id.card_img);
        progressbar = (ProgressBar) itemView.findViewById(R.id.progress);
        placetype = (TextView) itemView.findViewById(R.id.placetype);

        if(enableNavigation) {
            navigation = (ClickableImageView) itemView.findViewById(R.id.card_navigationButton);
            navigation.setVisibility(View.VISIBLE);
        }
    }
    

    public void bind (final Restaurant restaurant){
        this.nameTextView.setText(restaurant.getTitle());
        this.locationTextView.setText(restaurant.getAddress());

        Double rating = restaurant.getRating();

        // Check if restaurant has rating
        if (rating >= 0 && rating <= 5) {
            this.ratingTextView.setText(Double.toString(rating));
            this.starImageView.setVisibility(View.VISIBLE);
        } else {
            this.starImageView.setVisibility(View.INVISIBLE);
            this.ratingTextView.setText("");
        }

        if(navigation != null){
            navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Navigation clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        hoursTextView.setText("Hours not known");
        placetype.setText("8:00 - 16:00");
        
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

        Glide.with(itemView.getContext()).clear(restaurantImageView);
        restaurantImageView.setImageDrawable(null);
        progressbar.setVisibility(View.GONE);
        restaurantImageView.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.restaurant_placeholder));
    }
    
    
}
