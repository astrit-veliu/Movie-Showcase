package com.av.movieshowcase.ui.base.custom.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RecyclerViewLinearManagerAnimation extends LinearLayoutManager {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public RecyclerViewLinearManagerAnimation(Context context, int defStyleAttr, boolean reverse) {
        super(context, defStyleAttr, reverse);
    }

}