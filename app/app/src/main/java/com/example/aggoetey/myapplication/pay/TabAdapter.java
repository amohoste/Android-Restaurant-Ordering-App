package com.example.aggoetey.myapplication.pay;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

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

        private TextView time;
        private TextView price;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order_item, parent, false));
            itemView.setOnClickListener(this);
            time = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }

        public void bind(Tab.Order order) {
            this.order = order;
            time.setText(DateFormat.format("hh:mm", order.getTime()));
            price.setText(price.getContext().getString(R.string.price_order_short, this.order.getPrice()));
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onOrderClick(order);
        }
    }
}
