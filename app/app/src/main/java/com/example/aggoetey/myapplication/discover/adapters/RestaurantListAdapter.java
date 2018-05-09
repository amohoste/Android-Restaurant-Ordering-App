package com.example.aggoetey.myapplication.discover.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.helpers.DayConverter;
import com.example.aggoetey.myapplication.discover.helpers.KeyProvider;
import com.example.aggoetey.myapplication.discover.helpers.PlacetypeStringifier;
import com.example.aggoetey.myapplication.model.Restaurant;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by amoryhoste on 03/04/2018.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    // Constants
    private static String API_KEY;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    // Listener
    private OnRestaurantClickListener mListener;

    public interface OnRestaurantClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    public void setRestaurantClickListener(OnRestaurantClickListener listener) {
        this.mListener = listener;
    }

    // Private variables
    private List<Restaurant> mRestaurants;
    private Context context;

    public RestaurantListAdapter(Context context) {
        this.context = context;
        API_KEY = KeyProvider.getPlacesApiKey(context);
    }

    public void setRestaurants(List<Restaurant> mRestaurants) {
        this.mRestaurants = mRestaurants;
        this.notifyDataSetChanged();
    }

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        loadRestaurantPicture(holder, restaurant);
        holder.bind(restaurant);
    }

    private void loadRestaurantPicture(ViewHolder holder, Restaurant restaurant) {
        // Transformation to round corners etc.
        MultiTransformation<android.graphics.Bitmap> multi;
        multi = new MultiTransformation<>(
                new FitCenter(),
                new CenterCrop(),
                new RoundedCornersTransformation(7, 0));

        final ProgressBar progressBar = (ProgressBar) holder.progressbar;

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
                    .into(holder.restaurantImageView);
        } else {
            Glide.with(context).clear(holder.restaurantImageView);
            holder.restaurantImageView.setImageDrawable(null);
            holder.progressbar.setVisibility(View.GONE);
            holder.restaurantImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.restaurant_placeholder));
        }
    }

    @Override
    public int getItemCount() {
        return mRestaurants == null ? 0 : mRestaurants.size();
    }

    /**
     * Viewholder for the restaurant ListView
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Corresponding restaurant for viewholder object
        private Restaurant mRestaurant;

        // View fields
        private TextView nameTextView;
        private TextView locationTextView;
        private TextView ratingTextView;
        private TextView hoursTextView;
        private ImageView starImageView;
        private ImageView restaurantImageView;
        private ProgressBar progressbar;
        private TextView placetype;

        ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.discover_restaurantlist_item, parent, false));

            // Listen to clicks
            itemView.setOnClickListener(this);

            // Initialize views
            nameTextView = (TextView) itemView.findViewById(R.id.card_name);
            locationTextView = (TextView) itemView.findViewById(R.id.card_location);
            ratingTextView = (TextView) itemView.findViewById(R.id.card_stars);
            hoursTextView = (TextView) itemView.findViewById(R.id.card_hours);
            starImageView = (ImageView) itemView.findViewById(R.id.star_image);
            restaurantImageView = (ImageView) itemView.findViewById(R.id.card_img);
            progressbar = (ProgressBar) itemView.findViewById(R.id.progress);
            placetype = (TextView) itemView.findViewById(R.id.placetype);
        }

        /**
         * Updates viewholder when new restaurant is bound
         */
        void bind(final Restaurant restaurant) {
            this.mRestaurant = restaurant;
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
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onRestaurantClick(mRestaurant);
            }
        }
    }
}
