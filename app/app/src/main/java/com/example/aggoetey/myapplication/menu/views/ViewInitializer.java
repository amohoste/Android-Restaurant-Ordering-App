package com.example.aggoetey.myapplication.menu.views;

import android.view.View;

/**
 * Created by sitt on 09/04/18.
 * TODO: Remove this ugly disgusting class (I didn't know what I was thinking)
 */

public interface ViewInitializer<T, R extends ViewInitializer>  {

     R intializeView (View view);

     R updateView (T data);
}
