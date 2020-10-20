package com.mapxus.mapxusmapandroiddemo.customizeview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CircularPagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private int colorActive = 0xFF49B1D3;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private final int mIndicatorHeight = (int) (DP * 25);

    /**
     * Indicator stroke width.
     */
    private final float mIndicatorStrokeWidth = DP * 1f;

    /**
     * Indicator width.
     */
    private final float mIndicatorItemRadius = DP * 3;
    /**
     * Padding between indicators.
     */
    private final float mIndicatorItemPadding = DP * 6;

    /**
     * Some more natural animation interpolation
     */
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    public CircularPagerIndicatorDecoration() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);

        mPaint.setAntiAlias(true);
        mPaint.setColor(colorActive);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        // center horizontally, calculate width and subtract half from center
        float totalLength = mIndicatorItemRadius * 2 * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - (parent.getHeight() / 5F);

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY + mIndicatorHeight, itemCount);


        // find active page (which should be highlighted)
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY + mIndicatorHeight, activePosition, progress, itemCount);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setStyle(Paint.Style.STROKE);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemRadius * 2 + mIndicatorItemPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            // draw the circle for every item
            c.drawCircle(start, indicatorPosY, mIndicatorItemRadius, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int itemCount) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemRadius * 2 + mIndicatorItemPadding;

        if (progress == 0F) {
            // no swipe, draw a normal indicator
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemRadius, mPaint);
        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            // calculate partial highlight
            float partialLength = (mIndicatorItemRadius * 2 + mIndicatorItemPadding) * progress;

            // draw the cut off highlight

            c.drawCircle(highlightStart + partialLength, indicatorPosY, mIndicatorItemRadius, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }
}