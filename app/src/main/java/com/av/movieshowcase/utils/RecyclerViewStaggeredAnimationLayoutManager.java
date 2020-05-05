package com.av.movieshowcase.utils;


import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class RecyclerViewStaggeredAnimationLayoutManager  extends StaggeredGridLayoutManager {


    public RecyclerViewStaggeredAnimationLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG", "meet a IOOBE in RecyclerView");
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}