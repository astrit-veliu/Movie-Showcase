package com.av.movieshowcase.utils;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by aveliu2 on 03,April,2020
 */
public class ViewUtils {

    public static Boolean isViewVisibleOnScreen(Rect scrollBounds, View view){
        boolean isVisible = true;
        if (view != null) {
            if (view.getLocalVisibleRect(scrollBounds)) {
                if (!view.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < view.getHeight()) {
                    //Log.i(TAG, "BTN APPEAR PARCIALY");
                    isVisible=true;
                } else {//Log.i(TAG, "BTN APPEAR FULLY!!!");
                    isVisible=true;  }
            } else { // Log.i(TAG, "No");
                isVisible=false; }
        }
        return isVisible;
    }

}
