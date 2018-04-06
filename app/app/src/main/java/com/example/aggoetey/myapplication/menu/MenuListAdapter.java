package com.example.aggoetey.myapplication.menu;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dries on 26/03/2018.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuItemHolder> implements Serializable {

    private MenuInfo menuInfo;
    private String category;

    public MenuListAdapter(MenuInfo menuInfo, String category) {
        this.menuInfo = menuInfo;
        menuInfo.addAdapter(this);
        this.category = category;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_list_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        holder.bind(menuInfo.getRestaurant().getMenu().getMenuItemList(category).get(position));
    }

    @Override
    public int getItemCount() {
        return menuInfo.getRestaurant().getMenu().getMenuItemList(category).size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private Button mOrderIncrementButton;
        private Button mOrderDecrementButton;
        private TextView mOrderCountTextView;

        public MenuItemHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.menu_recycler_title_view);
            mOrderIncrementButton = (Button) itemView.findViewById(R.id.menu_recycler_increment_ordercount_button);
            mOrderDecrementButton = (Button) itemView.findViewById(R.id.menu_recycler_decrement_ordercount_button);
            mOrderCountTextView = (TextView) itemView.findViewById(R.id.menu_recycler_item_count_view);
        }

        public void bind(final MenuItem menuItem) {
            mTitleTextView.setText(menuItem.title + " (€" + Integer.toString(menuItem.price) +")");

            setNewOrderCount(menuItem.id);

            mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuInfo.addOrderItem(menuItem);
                    setNewOrderCount(menuItem.id);
            }
            });

            mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (menuInfo.removeOrderItem(menuItem)) {
                        setNewOrderCount(menuItem.id);
                    }
                }
            });
        }

        private void setNewOrderCount(String itemID) {
            mOrderCountTextView.setText(menuInfo.getOrderCount(itemID));
        }
    }
}
