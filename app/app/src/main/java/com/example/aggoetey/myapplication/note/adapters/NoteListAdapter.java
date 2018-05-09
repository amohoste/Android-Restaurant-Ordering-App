package com.example.aggoetey.myapplication.note.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;

/**
 * Created by sitt on 09/05/18.
 */

public class NoteListAdapter extends RecyclerView.Adapter {

    private MenuInfo menuInfo;

    public NoteListAdapter(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()),  parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final LayoutInflater inflater,  final ViewGroup viewGroup) {
                super(inflater.inflate(R.layout.notes_list_card, viewGroup));
        }
    }
}
