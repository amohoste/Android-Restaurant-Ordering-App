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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Dries on 26/03/2018.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuItemHolder>{

    private MenuFragment menuFragment;

    public MenuListAdapter(MenuFragment menuFragment) {
        this.menuFragment = menuFragment;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_list_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        holder.bind(menuFragment.getRestaurant().getMenu().getMenuItemList().get(position));
    }

    @Override
    public int getItemCount() {
        return menuFragment.getRestaurant().getMenu().getMenuItemList().size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private Button mOrderIncrementButton;
        private Button mOrderDecrementButton;
        private TextView mOrderCountTextView;

        private String itemID;


        public MenuItemHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.menu_recycler_title_view);
            mOrderIncrementButton = (Button) itemView.findViewById(R.id.menu_recycler_increment_ordercount_button);
            mOrderDecrementButton = (Button) itemView.findViewById(R.id.menu_recycler_decrement_ordercount_button);
            mOrderCountTextView = (TextView) itemView.findViewById(R.id.menu_recycler_item_count_view);
        }

        public void bind(final MenuItem menuItem) {
            itemID = menuItem.id;
            mTitleTextView.setText(menuItem.title + " (€" + Integer.toString(menuItem.price) +")");
            if (menuFragment.getOrderCountMap().containsKey(menuItem.id)) {
               setNewOrderCount();
            }

            mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeOrderCount(1);
                    menuFragment.getCurrentOrder().addOrderItem("", menuItem);
                    menuFragment.setOrderButtonText();
                    menuFragment.enableOrderButton();
            }
            });

            mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (changeOrderCount(-1) >= 0) {
                        menuFragment.getCurrentOrder().removeOrderItem(menuItem);
                        menuFragment.setOrderButtonText();
                        if (menuFragment.getCurrentOrder().getOrderItems().size() == 0) {
                            menuFragment.disableOrderButton();
                        }
                    }
                }
            });
        }


        private int changeOrderCount(int i) {
            int c = i + (menuFragment.getOrderCountMap().containsKey(itemID) ? menuFragment.getOrderCountMap().get(itemID) : 0);
            if (c >= 0) {
                menuFragment.getOrderCountMap().put(itemID, c);
                setNewOrderCount();
            }
            return c;
        }

        private void setNewOrderCount() {
            mOrderCountTextView.setText(Integer.toString(menuFragment.getOrderCountMap().get(itemID)));
        }
    }
}
