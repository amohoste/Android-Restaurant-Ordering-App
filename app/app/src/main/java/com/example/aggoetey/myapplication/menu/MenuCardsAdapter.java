package com.example.aggoetey.myapplication.menu;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;

/**
 * Created by sitt on 05/04/18.
 */

public class MenuCardsAdapter extends RecyclerView.Adapter<MenuCardsAdapter.MenuCardHolder> {
    private MenuInfo menuInfo;
    private String category;
    private  int mExpandedPosition = -1;
    private  int mPreviousPosition = -1;

    public MenuCardsAdapter(MenuInfo menuInfo, String category){
        this.menuInfo = menuInfo;
        this.category = category;

    }

    @Override
    public MenuCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_card_item,parent,false);
        return new MenuCardHolder(view);
    }


    @Override
    public void onBindViewHolder(final MenuCardHolder holder, final int position) {
        final boolean isExpanded = position == mExpandedPosition;
        if(isExpanded) {
            mPreviousPosition = mExpandedPosition;
            holder.mDescriptionTextView.setVisibility(View.VISIBLE);
            holder.mDivider.setVisibility(View.VISIBLE);
        }else{
            holder.mDescriptionTextView.setVisibility(View.GONE);
            holder.mDivider.setVisibility(View.GONE);
        }



        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Card clicked position: ", String.valueOf(position));
                mExpandedPosition = isExpanded? -1 : position;
                notifyItemChanged(mPreviousPosition);
                notifyItemChanged(position);
            }
        });


        holder.bind(menuInfo.getRestaurant().getMenu().getMenuItemList(category).get(position));
    }

    @Override
    public int getItemCount() {
        return menuInfo.getRestaurant().getMenu().getMenuItemList(category).size();
    }




    public class  MenuCardHolder extends RecyclerView.ViewHolder{

        private TextView mDescriptionTextView;
        private TextView mPriceTextView;
        private TextView mNameTextView;
        private ImageView mDishImageView;
        private ImageView mDivider;
        private Button mOrderIncrementButton;
        private Button mOrderDecrementButton;
        private TextView mOrderCountTextView;


        public MenuCardHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = itemView.findViewById(R.id.menu_item_description);
            mNameTextView = itemView.findViewById(R.id.menu_item_name);
            mDishImageView = itemView.findViewById(R.id.menu_item_image);
            mDivider = itemView.findViewById(R.id.menu_item_desc_divider);
            mPriceTextView = itemView.findViewById(R.id.menu_item_price);
            mOrderIncrementButton = itemView.findViewById(R.id.menu_cards_increment_ordercount_button);
            mOrderDecrementButton = itemView.findViewById(R.id.menu_cards_decrement_ordercount_button);
            mOrderCountTextView = itemView.findViewById(R.id.menu_cards_item_count_view);

        }

        public void bind(final MenuItem item){

            mDescriptionTextView.setText(item.description);
            mNameTextView.setText(item.title);
            mPriceTextView.setText(String.format("€ %d", item.price));

            mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuInfo.addOrderItem(item);
                    setNewOrderCount(item.id);
                }
            });

            mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (menuInfo.removeOrderItem(item)) {
                        setNewOrderCount(item.id);
                    }
                }
            });

            // TEMPORARY IMAGE
            mDishImageView.setImageResource(R.drawable.kimchi1);

        }

        private void setNewOrderCount(String itemID) {
            mOrderCountTextView.setText(menuInfo.getOrderCount(itemID));
        }
    }
}

