package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aleksandra on 21/07/16.
 */
public class MyCustomLayout extends ViewGroup {

    public MyCustomLayout(Context context) {
        super(context);
    }

    public MyCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int maxHeight = 0;
        int sumWidth = 0;
        int numberOfMatchParentChild = -1;

        for (int i = 0; i < childCount; ++i) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = child.getLayoutParams();
            if (lp.width == LayoutParams.MATCH_PARENT) {
                numberOfMatchParentChild = i;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                sumWidth += child.getMeasuredWidth();
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }

        final View childMatchParent = getChildAt(numberOfMatchParentChild);
        final LayoutParams lp = childMatchParent.getLayoutParams();

        measureChild(childMatchParent,
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - sumWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY)
        );
        sumWidth += childMatchParent.getMeasuredWidth();

        setMeasuredDimension(resolveSize(sumWidth, widthMeasureSpec),
                resolveSize(maxHeight, heightMeasureSpec));

        Log.v(MyCustomLayout.class.getName(), "OnMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        int curLeft = parentLeft;

        for (int i = 0; i < childCount; ++i) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final int curWidth = child.getMeasuredWidth();
            final int curHeight = child.getMeasuredHeight();

            final int childLeft = curLeft;
            final int childRight = curLeft + curWidth;

            child.layout(childLeft, parentTop, childRight, parentTop + curHeight);
            curLeft = childRight;
        }

        Log.v(MyCustomLayout.class.getName(), "OnLayout");
    }
}
