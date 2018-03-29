package com.example.aggoetey.myapplication.orderdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

/**
 * Created by aggoetey on 3/29/18.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{

    private Tab.Order order;

    public OrderItemAdapter(Tab.Order order) {
        this.order = order;
    }

    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    //Zet de inhoud van een ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(order.getOrderItems().get(position));
    }

    @Override
    public int getItemCount() {
        return order.getOrderItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Tab.Order.OrderItem orderItem;

        private TextView name;
        private TextView price;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order_item, parent, false));
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
        }

        public void bind(Tab.Order.OrderItem orderItem) {
            this.orderItem = orderItem;
            name.setText(orderItem.getMenuItem().title);
            price.setText(String.valueOf(this.orderItem.getMenuItem().price));
        }

    }
}
