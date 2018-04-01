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

    private List<MenuItem> items;
    private HashMap<String, Integer> orderCountMap;
    private Tab.Order currentOrder;
    private Button orderButton;

    public MenuListAdapter(Tab.Order currentOrder, List<MenuItem> items, Button orderButton) {
        this.currentOrder = currentOrder;
        this.items = items;
        orderCountMap = new HashMap<>();
        this.orderButton = orderButton;
    }

    public void setCurrentOrder(Tab.Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void resetOrderCountMap() {
        orderCountMap = new HashMap<>();
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_menu_list_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private Button mOrderIncrementButton;
        private Button mOrderDecrementButton;
        private TextView mOrderCountTextView;

        private String itemTitle;


        public MenuItemHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.menu_recycler_title_view);
            mOrderIncrementButton = (Button) itemView.findViewById(R.id.menu_recycler_increment_ordercount_button);
            mOrderDecrementButton = (Button) itemView.findViewById(R.id.menu_recycler_decrement_ordercount_button);
            mOrderCountTextView = (TextView) itemView.findViewById(R.id.menu_recycler_item_count_view);
        }

        public void bind(final MenuItem menuItem) {
            itemTitle = menuItem.title;
            mTitleTextView.setText(menuItem.title + " (€" + Integer.toString(menuItem.price) +")");
            if (orderCountMap.containsKey(menuItem.title)) {
               setNewOrderCount();
            }

            mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeOrderCount(1);
                    currentOrder.addOrderItem("", menuItem);
                    orderButton.setText("ORDER (€" + currentOrder.getPrice() + ")");
                    orderButton.setVisibility(View.VISIBLE);
                    orderButton.setAlpha(1.0f);
            }
            });

            mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (changeOrderCount(-1) >= 0) {
                        currentOrder.removeOrderItem(menuItem);
                        orderButton.setText("ORDER (€" + currentOrder.getPrice() + ")");
                        if (currentOrder.getOrderItems().size() == 0) {
                            orderButton.setAlpha(0.2f);
                        }
                    }
                }
            });
        }

        public int changeOrderCount(int i) {
            int c = i + (orderCountMap.containsKey(itemTitle) ? orderCountMap.get(itemTitle) : 0);
            if (c >= 0) {
                orderCountMap.put(itemTitle, c);
                setNewOrderCount();
            }
            return c;
        }

        public void setNewOrderCount() {
            mOrderCountTextView.setText(Integer.toString(orderCountMap.get(itemTitle)));
        }

        public void setNewOrderCount(int i) {
            mOrderCountTextView.setText(Integer.toString(i));
        }
    }
}
