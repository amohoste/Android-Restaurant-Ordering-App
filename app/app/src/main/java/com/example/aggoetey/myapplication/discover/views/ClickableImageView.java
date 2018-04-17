package com.example.aggoetey.myapplication.discover.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * ImageView that is clickable, with touch effect
 */
public class ClickableImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int TOUCH_COLOR = 0x77000000;

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
                v.getDrawable().setColorFilter(TOUCH_COLOR, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                v.getDrawable().clearColorFilter();
                v.invalidate();
            }

            return false;
        }
    }

}

