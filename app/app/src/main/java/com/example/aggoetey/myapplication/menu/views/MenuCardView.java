package com.example.aggoetey.myapplication.menu.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.model.MenuInfo;
import com.example.aggoetey.myapplication.model.DataView;
import com.example.aggoetey.myapplication.model.MenuItem;

/**
 * Created by sitt on 09/04/18.
 *
 * View initializer for menu cards in grid view.
 *
 * //TODO: Make this class extend a view group instead of working with view initializer
 */

public class MenuCardView extends CardView implements DataView<MenuItem>{


    public interface  OnAddNoteButtonClickListener {
        void onAddNoteButtonClick (MenuItem data);
    }

    private TextView mDescriptionTextView;
    private TextView mPriceTextView;
    private TextView mNameTextView;
    private ImageView mDishImageView;
    private ImageView mDivider;
    private Button mOrderIncrementButton;
    private Button mOrderDecrementButton;
    private TextView mOrderCountTextView;
    private Button mAddNoteButton;
    private OnAddNoteButtonClickListener mListener;


    private MenuInfo menuInfo;

    public MenuCardView(Context context) {
        super(context);
        init();
    }

    public MenuCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public MenuCardView(Context context, MenuInfo menuInfo, OnAddNoteButtonClickListener listener) {
        super(context);
        this.menuInfo = menuInfo;
        this.mListener = listener;
        init();
    }


    @Override
    public void init() {
        View view = inflate(getContext(), R.layout.fragment_menu_card_item, this );
        infoViewInit(view);
        buttonInit(view);
    }

    @Override
    public void bind(final MenuItem data) {
        setNewOrderCount(data.id);
        updateAddNoteButton(data);
        updateTextView(data);

        mOrderIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuInfo.addOrderItem(data);
                setNewOrderCount(data.id);
                updateAddNoteButton(data);

            }
        });

        mOrderDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuInfo.removeOrderItem(data)) {
                    setNewOrderCount(data.id);
                    updateAddNoteButton(data);
                }
            }
        });


        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddNoteButtonClick(data);
            }
        });
    }

    private void updateTextView (final MenuItem item){
        mDescriptionTextView.setText(item.description);
        mNameTextView.setText(item.title);
        mPriceTextView.setText(String.format("€ %d", item.price));

        // TEMPORARY IMAGE
        mDishImageView.setImageResource(R.drawable.kimchi1);

    }


    private void updateAddNoteButton (final MenuItem data){
        mAddNoteButton.setEnabled(Integer.valueOf(menuInfo.getOrderCount(data.id)) > 0);
    }

    private void infoViewInit(View view) {
        mNameTextView = view.findViewById(R.id.menu_card_item_name);
        mPriceTextView = view.findViewById(R.id.menu_card_item_price);
        mDivider = view.findViewById(R.id.menu_card_item_desc_divider);
        mDescriptionTextView = view.findViewById(R.id.menu_card_item_description);
        mOrderCountTextView = view.findViewById(R.id.menu_cards_item_count_view);
        mDishImageView = view.findViewById(R.id.menu_card_item_image);
    }


    private void buttonInit(View view) {
        mOrderIncrementButton = view.findViewById(R.id.menu_cards_increment_ordercount_button);
        mOrderDecrementButton = view.findViewById(R.id.menu_cards_decrement_ordercount_button);
        mAddNoteButton = view.findViewById(R.id.add_note_button);

    }

    private void setNewOrderCount(String itemID) {
        mOrderCountTextView.setText(menuInfo.getOrderCount(itemID));
    }
}


