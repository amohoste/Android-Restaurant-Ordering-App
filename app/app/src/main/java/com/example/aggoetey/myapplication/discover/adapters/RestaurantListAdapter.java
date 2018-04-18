package com.example.aggoetey.myapplication.discover.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import java.util.List;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.views.RestaurantCardViewInitializer;
import com.example.aggoetey.myapplication.model.Restaurant;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by amoryhoste on 03/04/2018.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

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
        holder.bind(restaurant);
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
        private RestaurantCardViewInitializer initializer;

        ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.discover_restaurantlist_item, parent, false));

            // Listen to clicks
            itemView.setOnClickListener(this);
            initializer = new RestaurantCardViewInitializer(itemView,false);
        }

        /**
         * Updates viewholder when new restaurant is bound
         */
        void bind(final Restaurant restaurant) {
            this.mRestaurant = restaurant;
            initializer.bind(restaurant);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onRestaurantClick(mRestaurant);
            }
        }
    }
}
