package com.example.aggoetey.myapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by sitt on 05/04/18.
 */

public class UIUtility {
    public static int calculateNoOfColumns(Context context, float itemDpWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float itemWidth = itemDpWidth / displayMetrics.density;
        Log.i("display: ", "" + dpWidth);

        Log.i("width: " , "" + itemWidth);
        return Math.round(dpWidth / itemWidth);
    }
}
