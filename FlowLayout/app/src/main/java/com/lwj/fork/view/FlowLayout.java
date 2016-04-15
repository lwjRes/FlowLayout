package com.lwj.fork.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lwj.fork.R;

/**
 * Created by lwj on 16/4/13.
 * Des:
 */
public class FlowLayout extends ViewGroup {


    int mChildHMargin;//  child 之间的水平margin
    int mChildVMargin;  // child 之间的 竖直 margin

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout);

            mChildHMargin = (int) array.getDimension(R.styleable.FlowLayout_horizontalMargin, dip2px(10));
            mChildVMargin = (int) array.getDimension(R.styleable.FlowLayout_verticalMargin, dip2px(5));

            array.recycle();
        } else {
            mChildHMargin = dip2px(10);
            mChildVMargin = dip2px(5);
        }
    }

    /**
     * 测量
     * 以行排列
     * 行与行之间  间距 mChildVMargin
     * 每行的元素之间 间距 mChildHMargin
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取 父View 测量的宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 获取 上下 padding
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        // 获取左右 padding
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        // 内容区域的宽度
        int mContentWidth = width - leftPadding - rightPadding;
        int childCount = getChildCount();
        // 希望高度
        int shouldHeight = 0;
        int maxWidth = 0;
        // 每行的高度  第一行 只需要加child 的高度
        int lineHeight = 0;
        //  每一行的宽度  不可超过 父容器width
        int lineWidth = 0;

        int lineIndex = 0;//行索引

        int childIndexOfLine = 0; // child 在自己行的索引
        for (int i = 0; i < childCount; i++) {
            View chidlView = getChildAt(i);
            //  测量childview
            measureChild(chidlView, widthMeasureSpec, heightMeasureSpec);
//            chidlView.measure(MeasureSpec.makeMeasureSpec(mContentWidth, MeasureSpec.AT_MOST), heightMeasureSpec);
            int childWidth = chidlView.getMeasuredWidth();
            int childHeight = chidlView.getMeasuredHeight();
            MarginLayoutParams marginParmas;
            ViewGroup.LayoutParams params = chidlView.getLayoutParams();
            marginParmas = new MarginLayoutParams(params);
            marginParmas.topMargin = 0;
            marginParmas.bottomMargin = 0;
            marginParmas.leftMargin = 0;
            marginParmas.rightMargin = 0;
            int childShouldWidth;
            if (childIndexOfLine == 0) {  // 行首
                marginParmas.leftMargin = 0;
            } else {
                // 非行首
                marginParmas.leftMargin = mChildHMargin;
            }
            childShouldWidth = marginParmas.leftMargin + childWidth;

            if (lineWidth + childShouldWidth > mContentWidth) {
                //  加入当前view  行宽越界
                // 另起一行
                //  换行前 得到 最大宽度
                maxWidth = Math.max(maxWidth, lineWidth);
                lineIndex++;
                childIndexOfLine = 0;
                lineWidth = 0;
                childShouldWidth = childWidth;
                marginParmas.topMargin = mChildVMargin;
                int childNeedVSpace = marginParmas.topMargin + childHeight;
                shouldHeight = childNeedVSpace + shouldHeight;
            }
            //  计算 行宽
            lineWidth = childShouldWidth + lineWidth;
            maxWidth = Math.max(maxWidth, lineWidth);
            childIndexOfLine++;
            chidlView.setLayoutParams(marginParmas);
        }


        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        // 加上 上下的padding才是真正的高度
        shouldHeight = shouldHeight + topPadding + bottomPadding;
        int shouldWidth = maxWidth + leftPadding + rightPadding;
        setMeasuredDimension((wMode == MeasureSpec.EXACTLY) ? width
                : shouldWidth, (hMode == MeasureSpec.EXACTLY) ? height
                : shouldHeight);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int leftPadding = getPaddingLeft();
        width = width - leftPadding - getPaddingRight();
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();
                MarginLayoutParams marginParmas = (MarginLayoutParams) child.getLayoutParams();
                int topMargin = marginParmas.topMargin;
                int leftMargin = marginParmas.leftMargin;

                if (xpos + childw + leftMargin > width) {
                    xpos = getPaddingLeft();
                    ypos = topMargin + ypos + childh;
                } else {
                    //  计算 开始位置
                    xpos = xpos + leftMargin;
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh);
                xpos = xpos + childw;
            }
        }

    }

    //此方法是将dp值转化为px值，方便适配
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
