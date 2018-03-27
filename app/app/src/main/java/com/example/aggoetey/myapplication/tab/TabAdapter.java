package com.example.aggoetey.myapplication.tab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;

import java.util.List;

/**
 * Created by aggoetey on 3/26/18.
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    private List<Tab.Order> orders;

    public TabAdapter(List<Tab.Order> orders){
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Tab.Order order;

        private TextView number;
        private TextView price;

        public ViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.order, parent, false));
//            itemView.setOnClickListener(this);
            number = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }

        public void bind(Tab.Order order){
            this.order = order;
            number.setText(number.getContext().getString(R.string.tab_order_position, getAdapterPosition() + 1));
            price.setText(String.valueOf(this.order.getPrice()));
        }
    }
}
