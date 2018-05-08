package com.example.aggoetey.myapplication.discover.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.aggoetey.myapplication.model.DataView;
import com.example.aggoetey.myapplication.model.Restaurant;

import java.io.Serializable;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by sitt on 18/04/18.
 * 
 * A view which contains a card view of a restaurant similar to the cards in restaurant list.
 * This info card will be expanded with google maps routing in the near future.
 * 
 */

public class RestaurantInfoCardView extends LinearLayout implements DataView<Restaurant>{
    // View fields
    private TextView nameTextView;
    private TextView ratingTextView;
    private TextView hoursTextView;
    private ImageView starImageView;
    private ImageView restaurantImageView;
    private ProgressBar progressbar;
    private TextView placetype;
    private ClickableImageView navigation;
    private TextView phone;
    private CardView cardView;

    private Restaurant mRestaurant;

    // Listener
    private OnCardClickListener mListener;

    public interface OnCardClickListener {
        void onCardClick(Restaurant restaurant);
    }

    public void setCardClickListener(OnCardClickListener listener) {
        this.mListener = listener;
    }

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

    @Override
    public void init() {
        inflate(getContext(), R.layout.discover_restaurant_info_card, this);
        // Initialize views
        cardView = (CardView) findViewById(R.id.restaurant_infocard_cardview);
        nameTextView = (TextView) findViewById(R.id.card_name);
        ratingTextView = (TextView) findViewById(R.id.card_stars);
        hoursTextView = (TextView) findViewById(R.id.card_hours);
        starImageView = (ImageView) findViewById(R.id.star_image);
        restaurantImageView = (ImageView) findViewById(R.id.card_img);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        placetype = (TextView) findViewById(R.id.placetype);
        phone = (TextView) findViewById(R.id.phone_hours);

        navigation = (ClickableImageView) findViewById(R.id.card_navigationButton);
        navigation.setVisibility(View.VISIBLE);

        cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null && mRestaurant != null) {
                    mListener.onCardClick(mRestaurant);
                }
            }
        });
    }

    @Override
    public void bind(final Restaurant restaurant) {
        mRestaurant = restaurant;
        this.nameTextView.setText(restaurant.getTitle());
        Double rating = restaurant.getRating();
        phone.setText(restaurant.getPhone());

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
                    Uri navigationURI = Uri.parse("google.navigation:q=" + restaurant.getPosition().latitude + "," + restaurant.getPosition().longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationURI);
                    mapIntent.setPackage(getResources().getString(R.string.google_map_app));
                    getContext().startActivity(mapIntent);
                }
            });
        }

        //TODO: get data from backend
        hoursTextView.setText("08:00 - 16:00");
        placetype.setText("Restaurant");

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
