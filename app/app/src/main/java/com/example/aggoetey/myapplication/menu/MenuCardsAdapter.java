package com.example.aggoetey.myapplication.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sitt on 05/04/18.
 */

public class MenuCardsAdapter extends RecyclerView.Adapter<MenuCardsAdapter.MenuCardHolder> {


    @Override
    public MenuCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MenuCardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class  MenuCardHolder extends RecyclerView.ViewHolder{

        public MenuCardHolder(View itemView) {
            super(itemView);
        }

        public void bind(View itemView){

        }
    }
}

