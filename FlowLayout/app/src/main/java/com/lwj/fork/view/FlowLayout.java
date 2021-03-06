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

    public Context mContext;
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,context);
    }

    private void init(AttributeSet attrs,Context context) {
        this.mContext = context;
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
        //  每一行的宽度  不可超过 父容器width
        int lineWidth = 0;


        int childIndexOfLine = 0; // child 在自己行的索引

        //  child 水平上需要占据的空间
        int childNeedHSpace;
        //  child 竖直上需要占据的空间
        int childNeedVSpace;
        for (int i = 0; i < childCount; i++) {
            View chidlView = getChildAt(i);
            //  测量childview
            measureChild(chidlView, widthMeasureSpec, heightMeasureSpec);
//            chidlView.measure(MeasureSpec.makeMeasureSpec(mContentWidth, MeasureSpec.AT_MOST), heightMeasureSpec);
            int childWidth = chidlView.getMeasuredWidth();
            int childHeight = chidlView.getMeasuredHeight();

            childNeedHSpace=childWidth;
            childNeedVSpace = childHeight;

           // bug－－－ 首次测量 第一个view 需要设置初始高度
            if (i == 0) {
                shouldHeight = childNeedVSpace;
            }
            MarginLayoutParams marginParmas;
            ViewGroup.LayoutParams params = chidlView.getLayoutParams();
            marginParmas = new MarginLayoutParams(params);
            marginParmas.topMargin = 0;
            marginParmas.bottomMargin = 0;
            marginParmas.leftMargin = 0;
            marginParmas.rightMargin = 0;

            if (childIndexOfLine == 0) {  // 行首
                marginParmas.leftMargin = 0;
            } else {
                // 非行首
                marginParmas.leftMargin = mChildHMargin;
            }
            childNeedHSpace = marginParmas.leftMargin + childWidth;

            if (lineWidth + childNeedHSpace > mContentWidth) {
                //  加入当前view  行宽越界
                // 另起一行
                //  换行前 得到 最大宽度
                maxWidth = Math.max(maxWidth, lineWidth);
                childIndexOfLine = 0;
                lineWidth = 0;
                childNeedHSpace = childWidth;
                marginParmas.topMargin = mChildVMargin;
                childNeedVSpace = marginParmas.topMargin + childHeight;
                shouldHeight = childNeedVSpace + shouldHeight;
            }
            //  计算 行宽
            lineWidth = childNeedHSpace + lineWidth;
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
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childRight = 0;
        int childBottom = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();
                MarginLayoutParams marginParmas = (MarginLayoutParams) child.getLayoutParams();
                int topMargin = marginParmas.topMargin;
                int leftMargin = marginParmas.leftMargin;
                if (childLeft + childw + leftMargin > width) {
                    childLeft = getPaddingLeft();

                    if(topMargin == 0){
                        marginParmas.topMargin = mChildVMargin;
                    }
                    topMargin = marginParmas.topMargin;
                    childTop = topMargin + childBottom;
                } else {
                    //  计算 开始位置
                    childLeft = childLeft + leftMargin;
                }
                childRight = childLeft + childw;
                childBottom = childTop + childh;

                child.layout(childLeft, childTop, childRight, childBottom);
                childLeft = childLeft + childw;
            }
        }

    }
    //此方法是将dp值转化为px值，方便适配
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    BaseAdapter baseAdapter;

    public void setAdapter(BaseAdapter baseAdapter){
        this.baseAdapter = baseAdapter;
        addTexts();
    }
    public void addTexts(){

        int childCount  = baseAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            final View itemView = baseAdapter.getItemView(i,this);
            itemView.setTag(i);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) itemView.getTag();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v,FlowLayout.this,index);
                    }
                }
            });
            addView(itemView);
        }
      requestLayout();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener{
        void onItemClick(View itemView,FlowLayout flowLayout,int position);
    }


    public static abstract  class BaseAdapter{

        public BaseAdapter() {
        }

        public abstract  int getCount();
        public abstract  View getItemView(int index,FlowLayout parent);
    }


}
