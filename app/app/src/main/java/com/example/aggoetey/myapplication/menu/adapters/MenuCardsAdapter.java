package com.example.aggoetey.myapplication.menu.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

/**
 * Created by sitt on 05/04/18.
 * 
 * Adapter to show menu in cards form.
 */

public class MenuCardsAdapter extends RecyclerView.Adapter<MenuCardsAdapter.MenuCardHolder> implements Listener {

    public interface OnAddNoteButtonClickListener {
         void onAddNoteButtonClick(MenuItem menuItem);
    }
    private MenuInfo menuInfo;
    private String category;
    private OnAddNoteButtonClickListener listener;
    private  int mExpandedPosition = -1;
    private  int mPreviousPosition = -1;

    public MenuCardsAdapter(MenuInfo menuInfo, String category, OnAddNoteButtonClickListener listener){
        this.menuInfo = menuInfo;
        this.category = category;
        this.listener = listener;
    }

    @Override
    public void invalidated() {
        notifyDataSetChanged();
    }

    @Override
    public MenuCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_card_item,parent,false);

        return new MenuCardHolder(view, this.listener);
    }


    @Override
    public void onBindViewHolder(final MenuCardHolder holder, final int position) {
        final boolean isExpanded = position == mExpandedPosition;
        MenuItem menuItem = Tab.getInstance().getRestaurant().getMenu().getMenuItemList(category).get(position);


        if(isExpanded) {
            mPreviousPosition = mExpandedPosition;
            holder.mDescriptionTextView.setVisibility(View.VISIBLE);
            holder.mDivider.setVisibility(View.VISIBLE);
        }else{
            holder.mDescriptionTextView.setVisibility(View.GONE);
            holder.mDivider.setVisibility(View.GONE);
        }


        holder.mDishImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Card clicked position: ", String.valueOf(position));
                mExpandedPosition = isExpanded? -1 : position;
                notifyItemChanged(mPreviousPosition);
                notifyItemChanged(position);
            }
        });



        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return Tab.getInstance().getRestaurant().getMenu().getMenuItemList(category).size();
    }



    /**
     * View holder for individual cards in the recycler view. 
     */
    public class  MenuCardHolder extends RecyclerView.ViewHolder{

        private TextView mDescriptionTextView;
        private TextView mPriceTextView;
        private TextView mNameTextView;
        private ImageView mDishImageView;
        private ImageView mDivider;
        private Button mOrderIncrementButton;
        private Button mOrderDecrementButton;
        private TextView mOrderCountTextView;
        private Button mAddNoteButton;
        private OnAddNoteButtonClickListener mListener;

        public MenuCardHolder(View itemView, OnAddNoteButtonClickListener listener) {
            super(itemView);
            mListener = listener;
            mAddNoteButton = itemView.findViewById(R.id.add_note_button);
            mDescriptionTextView = itemView.findViewById(R.id.menu_card_item_description);
            mNameTextView = itemView.findViewById(R.id.menu_card_item_name);
            mDishImageView = itemView.findViewById(R.id.menu_card_item_image);
            mDivider = itemView.findViewById(R.id.menu_card_item_desc_divider);
            mPriceTextView = itemView.findViewById(R.id.menu_card_item_price);
            mOrderIncrementButton = itemView.findViewById(R.id.menu_cards_increment_ordercount_button);
            mOrderDecrementButton = itemView.findViewById(R.id.menu_cards_decrement_ordercount_button);
            mOrderCountTextView = itemView.findViewById(R.id.menu_cards_item_count_view);

        }

        public void bind(final MenuItem item){
            setNewOrderCount(item.id);
            setTextViews(item);
            updateAddNoteButton(item);
            mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (menuInfo.addOrderItem(item)) {
                        setNewOrderCount(item.id);
                        updateAddNoteButton(item);
                    }
                }
            });

            mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (menuInfo.removeOrderItem(item)) {
                        setNewOrderCount(item.id);
                        updateAddNoteButton(item);
                    }
                }
            });


            mAddNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddNoteButtonClick(item);
                }
            });
        }



        public void updateAddNoteButton (final MenuItem item){
            mAddNoteButton.setEnabled(Integer.valueOf(menuInfo.getOrderCount(item.id)) > 0);
        }


        private void setTextViews(final MenuItem item){


            mDescriptionTextView.setText(item.description);
            mNameTextView.setText(item.title);
            mPriceTextView.setText(String.format("€ %.2f", item.price));

            // TEMPORARY IMAGE
            mDishImageView.setImageResource(R.drawable.kimchi1);

        }

        private void setNewOrderCount(String itemID) {
            mOrderCountTextView.setText(menuInfo.getOrderCount(itemID));
        }
    }
}

