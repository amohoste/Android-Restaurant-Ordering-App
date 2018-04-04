package com.example.aggoetey.myapplication.tab;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.orderdetail.OrderDetailActivity;

import java.util.List;

/**
 * Created by aggoetey on 3/26/18.
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    private OnOrderClickListener mListener;

    public interface OnOrderClickListener {
        void onOrderClick(Tab.Order order);
    }

    public void setOrderClickListener(OnOrderClickListener listener) {
        this.mListener = listener;
    }

    private List<Tab.Order> orders;

    public TabAdapter(List<Tab.Order> orders) {
        this.orders = orders;
    }

    @Override
    public TabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    //Zet de inhoud van een ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Tab.Order order;

        private TextView number;
        private TextView price;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order_detail_fragment, parent, false));
            itemView.setOnClickListener(this);
            number = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }

        public void bind(Tab.Order order) {
            this.order = order;
            number.setText(number.getContext().getString(R.string.tab_order_position, this.order.getOrderNumber()));
            price.setText(String.valueOf(this.order.getPrice()));
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onOrderClick(order);
        }
    }
}
