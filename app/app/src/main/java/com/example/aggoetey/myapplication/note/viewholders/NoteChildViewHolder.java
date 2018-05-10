package com.example.aggoetey.myapplication.note.viewholders;

import android.view.View;
import android.widget.TextView;

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

    public NoteChildViewHolder(View itemView) {
        super(itemView);

        number =  itemView.findViewById(R.id.notes_number);
        noteDescription =  itemView.findViewById(R.id.notes_detail);
        editBtn =  itemView.findViewById(R.id.notes_child_add_note_btn);
    }

    public void bind (Tab.Order.OrderItem orderItem, int number, boolean isPending){
        this.number.setText(String.format("%d",number));
        this.noteDescription.setText(orderItem.getNote());
        if(!isPending) {
            this.editBtn.setVisibility(View.GONE);
        }
    }
}
