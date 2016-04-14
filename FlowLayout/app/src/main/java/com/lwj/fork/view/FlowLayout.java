package com.lwj.fork.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lwj on 16/4/13.
 * Des:
 */
public class FlowLayout extends ViewGroup {

    int mWidth;// 宽
    int mHeight; // 高
    int mLeftPadding;// 左侧padd
    int mRightPadding;//
    int mTopPadding;
    int mBottomPadding;
    int childHorizontalMargin = dip2px(3);  // 子view 间的水平margin
    int childVerticalMargin = dip2px(4);
    int childMargin = dip2px(4);
    int childCount;
    int contentWidth = 0;   // 内容区域 宽
    int contentHeight = 0; // 内容区域 高


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得父容器为它设置大小
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mLeftPadding = getPaddingLeft();
        mRightPadding = getPaddingRight();
        mTopPadding = getPaddingTop();
        mBottomPadding = getPaddingBottom();
        childCount = getChildCount();
        contentWidth = mWidth - mLeftPadding - mRightPadding;

        measureChilds(widthMeasureSpec, heightMeasureSpec);
        mHeight = contentHeight + mTopPadding + mBottomPadding;
        setMeasuredDimension(mWidth, mHeight);
        Log.e("contentWidth", contentWidth + "");
        Log.e("contentHeight", contentHeight + "");
    }

    /**
     * 测量 子view
     * <p/>
     * 每行的行头 不需要加  childHorizontalMargin
     * <p/>
     * 第一行   不需要加 childVerticalMargin
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureChilds(int widthMeasureSpec, int heightMeasureSpec) {

        int childIndexInLine = 0;
        int lineIndex = 0; // 行索引
        int x = mLeftPadding;
        int childHeightMeasureSpec;

        int childWidthMeasureSec = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.AT_MOST);
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(childWidthMeasureSec, childHeightMeasureSpec);
                // child 宽度
                int childWidth = child.getMeasuredWidth();
                // child 高度
                int childHeight = child.getMeasuredHeight();


                if(lineIndex==0 &&childIndexInLine == 0){ // 第一行的第一个
                    contentHeight = childHeight + contentHeight;
                    x = childWidth + x;

                }else if(lineIndex==0 &&childIndexInLine > 0){ // 第一行的其他
                    //  越界  另起一行
                    if (x + childWidth + childHorizontalMargin > contentWidth) {
                        lineIndex++;
                        x = mLeftPadding;
                        childIndexInLine = 0;
                    }
                }else if(lineIndex>0 && childIndexInLine == 0){  // 不是第一行的第一个

                }
                // 每行的第一个child
                if (childIndexInLine == 0) {
                    {
                        if (lineIndex == 0) {
                            // 第一行   只需加 child 的高度
                            contentHeight = childHeight + contentHeight;
                        } else {
                            // 其余行  加 child 的高度和 childVerticalMargin
                            contentHeight = childHeight + contentHeight + childVerticalMargin;
                        }
                        x = childWidth + x;
                    }
                } else {
                    //  越界  另起一行
                    if (x + childWidth + childHorizontalMargin > contentWidth) {
                        lineIndex++;
                        x = mLeftPadding;
                        childIndexInLine = 0;
                    }
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childIndexInLine = 0;
        int lineIndex = 0; // 行索引

        int childLeft = mLeftPadding;
        int childTop = mTopPadding;

        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                // 每行的第一个child
                if (childIndexInLine == 0) {
                    {
                        if (lineIndex == 0) {
                            // 啥都不做
                            childLeft = mLeftPadding;
                            childTop = mTopPadding;
                        } else {
                            // 其余行  加 child 的高度和 childVerticalMargin
                            height = childHeight + height + childVerticalMargin;
                            childLeft = mLeftPadding;
                            childTop = mTopPadding + height;
                        }
                    }
                } else {
                    //  越界  另起一行
                    if (childLeft + childWidth + childHorizontalMargin > contentWidth) {
                        lineIndex++;
                        childLeft = mLeftPadding;
                        childIndexInLine = 0;
                    } else {
                        childLeft = childLeft + childHorizontalMargin;
                        childIndexInLine++;
                    }
                }
                int childRight = childLeft + childWidth;
                int childBottom = childTop + childHeight;
                child.layout(childLeft, childTop, childRight, childBottom);
            }
        }
    }

    //此方法是将dp值转化为px值，方便适配
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public int getChildHorizontalMargin() {
        return childHorizontalMargin;
    }

    public int getChildVerticalMargin() {
        return childVerticalMargin;
    }

    public int getChildMargin() {
        return childMargin;
    }
}
