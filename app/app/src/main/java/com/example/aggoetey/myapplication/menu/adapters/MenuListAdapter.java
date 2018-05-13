package com.example.aggoetey.myapplication.menu.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.MenuItem;
import com.example.aggoetey.myapplication.model.Tab;

import java.io.Serializable;

/**
 * Created by Dries on 26/03/2018.
 *
 * Adapter voor de recyclerview voor menuitems.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuItemHolder> implements Serializable, Listener {

    public interface MenuListClickListener {
        void onMenuListLongClick(MenuItem menuItem, MenuInfo menuInfo, int position);
    }


    private MenuInfo menuInfo;
    private String category;
    private MenuListClickListener listClickListener;

    public MenuListAdapter(MenuInfo menuInfo, String category, MenuListClickListener listClickListener) {
        this.menuInfo = menuInfo;
        this.category = category;
        this.listClickListener = listClickListener;

    }

    @Override
    public void invalidated() {
        notifyDataSetChanged();
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_list_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        holder.bind(Tab.getInstance().getRestaurant().getMenu().getMenuItemList(category).get(position), position);
    }


    @Override
    public int getItemCount() {
        return Tab.getInstance().getRestaurant().getMenu().getMenuItemList(category).size();
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

        public void bind(final MenuItem menuItem, final int position) {
            mTitleTextView.setText(menuItem.title + " (€" + Double.toString(menuItem.price) +")");
            setNewOrderCount(menuItem.id);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listClickListener.onMenuListLongClick(menuItem, menuInfo, position);
                    return true;
                }
            });

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
