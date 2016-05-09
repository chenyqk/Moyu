package com.mstc.moyu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2016/5/8.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private HorizontalScrollViewListener horizontalScrollViewListener = null;

    public MyHorizonalScrollView(Context context) {
        super(context);
    }

    public MyHorizonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizonalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnHorizonalScrollViewListener(HorizontalScrollViewListener horizontalScrollViewListener){
        this.horizonalScrollViewListener = horizonalScrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l,int t,int oldl,int oldt){
        super.onScrollChanged(l, t, oldl, oldt);
        if(horizonalScrollViewListener != null){
            horizonalScrollViewListener.onScrollChanged(this,l,t,oldl,oldt);
        }
    }
}
