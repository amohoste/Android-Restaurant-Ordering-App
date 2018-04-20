package com.example.aggoetey.myapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by sitt on 05/04/18.
 * 
 * Utility class which can be expanded upon to calculate UI bounds/layouts. 
 */

public class UIUtility {
    public static int calculateNoOfColumns(Context context, float itemDpWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float itemWidth = itemDpWidth / displayMetrics.density;

        Log.i("UIUtility" , "dpWidth " + dpWidth);
        Log.i("UIUtility" , "itemWidth "  + itemWidth);
        return Math.round(dpWidth / itemWidth);
    }
}
