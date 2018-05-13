package com.example.aggoetey.myapplication.pay.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.List;

/**
 * Created by aggoetey on 3/29/18.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{

    private List<List<Tab.Order.OrderItem>> orderItemList;

    public OrderItemAdapter(Tab.Order order) {
        this.orderItemList = Tab.Order.groupOrders(order);
    }

    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    //Zet de inhoud van een ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(orderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private List<Tab.Order.OrderItem> orderItems;

        private TextView name;
        private TextView price;
        private TextView amount;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order_item_item, parent, false));
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            amount = (TextView) itemView.findViewById(R.id.amount);
        }

        public void bind(List<Tab.Order.OrderItem> orderItems) {
            this.orderItems = orderItems;
            name.setText(orderItems.get(0).getMenuItem().title);
            price.setText(String.valueOf(this.orderItems.get(0).getMenuItem().price
                    * this.orderItems.size()));
            amount.setText(String.valueOf(this.orderItems.size()) + "x");
        }

    }
}
