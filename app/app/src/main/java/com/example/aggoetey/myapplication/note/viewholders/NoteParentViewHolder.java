package com.example.aggoetey.myapplication.note.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.note.models.NoteItemParent;

/**
 * Created by sitt on 10/05/18.
 */

public class NoteParentViewHolder extends ParentViewHolder {
    private TextView itemName;
    private TextView itemAmount;
    private TextView itemGroup;
    private ImageButton collapseBtn;
    public NoteParentViewHolder(View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.note_item_name);
        itemAmount = itemView.findViewById(R.id.note_item_amount);
        itemGroup = itemView.findViewById(R.id.note_item_order_group);
        collapseBtn = itemView.findViewById(R.id.note_expand_btn);
    }

    public void bind (NoteItemParent noteItemParent, int groupNo, boolean isPending){
        itemAmount.setText(String.format("%d", noteItemParent.getChildObjectList().size())  );
        itemName.setText(noteItemParent.getName());
        String text = itemView.getResources().getString(R.string.note_group_text);
        if(!isPending){
            itemGroup.setText( text + " " + groupNo);
        }
    }


}
