package com.example.aggoetey.myapplication.discover.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by amoryhoste on 02/04/2018.
 */

public class ClickableImageView extends android.support.v7.widget.AppCompatImageView {

    public ClickableImageView(Context context) {
        super(context);
        setOnTouchListener(new OnTouchImageViewListener());
    }

    public ClickableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new OnTouchImageViewListener());
    }

    public ClickableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(new OnTouchImageViewListener());
    }

    class OnTouchImageViewListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getAction();
            ImageView v = (ImageView) view;

            if (action == MotionEvent.ACTION_DOWN) {
                v.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                v.getDrawable().clearColorFilter();
                v.invalidate();
            }

            return false;
        }
    }

}

