package com.example.aggoetey.myapplication.tab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Order;
import com.example.aggoetey.myapplication.model.OrderItem;

import java.util.List;

/**
 * Created by aggoetey on 3/26/18.
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    private List<OrderItem> orderItems;

    public TabAdapter(List<OrderItem> orderItems){
        this.orderItems = orderItems;
    }

    @Override
    public TabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    //Zet de inhoud van een ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private OrderItem orderItem;

        private TextView title;
        private TextView price;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order_item, parent, false));
//            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }

        public void bind(OrderItem orderItem){
            this.orderItem = orderItem;
            title.setText(orderItem.getMenuItem().title);
            price.setText(orderItem.getMenuItem().price);
        }
    }
}
