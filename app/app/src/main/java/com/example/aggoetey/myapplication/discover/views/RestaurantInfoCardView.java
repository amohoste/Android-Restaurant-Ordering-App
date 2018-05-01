package com.example.aggoetey.myapplication.discover.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.adapters.RestaurantListAdapter;
import com.example.aggoetey.myapplication.discover.helpers.DayConverter;
import com.example.aggoetey.myapplication.discover.helpers.KeyProvider;
import com.example.aggoetey.myapplication.discover.helpers.PlacetypeStringifier;
import com.example.aggoetey.myapplication.model.DataView;
import com.example.aggoetey.myapplication.model.Restaurant;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by sitt on 18/04/18.
 * 
 * A view which contains a card view of a restaurant similar to the cards in restaurant list.
 * This info card will be expanded with google maps routing in the near future.
 * 
 */

public class RestaurantInfoCardView extends LinearLayout implements DataView<Restaurant>{

    // Constants
    private static String API_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

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
    private Context context;

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
        API_KEY = KeyProvider.getPlacesApiKey(context);
        this.context = context;
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
                    Toast.makeText(getContext(), "Navigation clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        HashMap<Integer,HashMap<String,String>> openingHours = restaurant.getOpeningHours();
        if (openingHours != null) {
            Calendar calendar = Calendar.getInstance();
            int day = DayConverter.toGoogleDay(calendar.get(Calendar.DAY_OF_WEEK));
            if (openingHours.get(day) != null) {
                hoursTextView.setText(openingHours.get(day).get("open") + " - " + openingHours.get(day).get("close"));
            } else {
                hoursTextView.setText("Closed");
            }
        } else {
            hoursTextView.setText("Hours not known");
        }

        placetype.setText(PlacetypeStringifier.stringify(restaurant.getType()));

        loadRestaurantPicture(restaurant);
    }

    private void loadRestaurantPicture( Restaurant restaurant) {
        // Transformation to round corners etc.
        MultiTransformation<android.graphics.Bitmap> multi;
        multi = new MultiTransformation<>(
                new FitCenter(),
                new CenterCrop(),
                new RoundedCornersTransformation(7, 0));

        final ProgressBar progressBar = (ProgressBar) this.progressbar;

        // Load restaurant image if existent
        if (restaurant.getPictureReference() != null && restaurant.getPictureReference() != "") {
            Glide.with(context).load(BASE_URL + restaurant.getPictureReference() + "&key=" + API_KEY)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(RequestOptions.bitmapTransform(multi))
                    .into(this.restaurantImageView);
        } else {
            Glide.with(context).clear(this.restaurantImageView);
            this.restaurantImageView.setImageDrawable(null);
            this.progressbar.setVisibility(View.GONE);
            this.restaurantImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.restaurant_placeholder));
        }
    }
}
