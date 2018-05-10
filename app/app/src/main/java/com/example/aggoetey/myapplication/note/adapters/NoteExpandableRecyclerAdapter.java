package com.example.aggoetey.myapplication.note.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.Tab;
import com.example.aggoetey.myapplication.note.models.NoteItemParent;
import com.example.aggoetey.myapplication.note.viewholders.NoteChildViewHolder;
import com.example.aggoetey.myapplication.note.viewholders.NoteParentViewHolder;

import java.util.List;


/**
 * Created by sitt on 09/05/18.
 */

public class NoteExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<NoteParentViewHolder, NoteChildViewHolder> {

    private LayoutInflater mLayoutInflater;
    private boolean isEditable = true;
    private NoteChildViewHolder.OnNoteItemEditBtnClickListener mListener;

    public NoteExpandableRecyclerAdapter(Context context, List<ParentObject> parentItemList, boolean isEditable) {
        super(context, parentItemList);
        this.isEditable = isEditable;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setmListener (NoteChildViewHolder.OnNoteItemEditBtnClickListener listener){
        this.mListener = listener;
    }

    @Override
    public NoteParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        return new NoteParentViewHolder(mLayoutInflater.inflate(R.layout.notes_list_card, viewGroup, false));
    }

    @Override
    public NoteChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        if(mListener== null){

            throw new  IllegalStateException("Listener is null: You need to call setmListener first");
        }

        return new NoteChildViewHolder(mLayoutInflater.inflate(R.layout.notes_list_item, viewGroup, false), mListener);
    }

    @Override
    public void onBindParentViewHolder(NoteParentViewHolder noteParentViewHolder, int i, Object o) {
        NoteItemParent parent = (NoteItemParent) o;
        noteParentViewHolder.bind(parent, parent.getGroupNo(), isEditable);
    }

    @Override
    public void onBindChildViewHolder(NoteChildViewHolder noteChildViewHolder, int i, Object o) {
        noteChildViewHolder.bind((Tab.Order.OrderItem) o, i, isEditable);

    }
}
