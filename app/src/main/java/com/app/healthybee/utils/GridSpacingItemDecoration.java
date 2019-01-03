package com.app.healthybee.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/*
 * Created by Amod on 15/9/18.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    private final static int PADDING_IN_BOTTOM= 60;

    private final int mPaddingBottom;

    public GridSpacingItemDecoration(Context context, int spanCount, int spacing, boolean includeEdge) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mPaddingBottom= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_IN_BOTTOM, metrics);
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing+2;
            }
            outRect.bottom = spacing+10; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
        final RecyclerView.Adapter adapter = parent.getAdapter();
        if ((adapter != null) && (position == adapter.getItemCount() - 1)) {
            outRect.bottom = mPaddingBottom;
        }
    }
}
