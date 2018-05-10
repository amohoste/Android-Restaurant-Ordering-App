package com.example.aggoetey.myapplication.note.viewholders;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.discover.views.ClickableImageView;
import com.example.aggoetey.myapplication.model.Tab;


/**
 * Created by sitt on 10/05/18.
 */

public class NoteChildViewHolder extends ChildViewHolder {
    private TextView number;
    private TextView noteDescription;
    private ClickableImageView editBtn;
    private OnNoteItemEditBtnClickListener listener;

    public NoteChildViewHolder(View itemView, OnNoteItemEditBtnClickListener listener) {
        super(itemView);
        this.listener = listener;
        number = itemView.findViewById(R.id.notes_number);
        noteDescription = itemView.findViewById(R.id.notes_detail);
        editBtn = itemView.findViewById(R.id.notes_child_add_note_btn);
    }


    public void bind(Tab.Order.OrderItem orderItem, int number, boolean isPending) {
        this.number.setText(String.format("%d", number));
        this.noteDescription.setText(orderItem.getNote());
        editBtn.setOnClickListener(v -> listener.onEditBtnClick(orderItem));

        if (!isPending) {
            this.editBtn.setVisibility(View.GONE);
        }
    }

    public interface OnNoteItemEditBtnClickListener {
        void onEditBtnClick(Tab.Order.OrderItem orderItem);
    }
}
