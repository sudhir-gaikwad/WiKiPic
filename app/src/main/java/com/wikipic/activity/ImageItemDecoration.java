package com.wikipic.activity;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ImageItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public ImageItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}
