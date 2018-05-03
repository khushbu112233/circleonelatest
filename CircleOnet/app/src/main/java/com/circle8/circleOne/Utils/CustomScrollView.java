package com.circle8.circleOne.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Ample-Arch on 13-03-2018.
 */

public class CustomScrollView extends ScrollView {

    private float mTouchPosition;
    private float mReleasePosition;

    public interface OnScrollViewListener {
        void onScrollChanged(CustomScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
        void scrollUp();
        void scrollDown();
    }

    private OnScrollViewListener mOnScrollViewListener;

    public CustomScrollView(Context context) {
        this(context, null, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchPosition = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mReleasePosition = event.getY();

            if (mTouchPosition - mReleasePosition > 0) {
                mOnScrollViewListener.scrollDown();
            } else {
                mOnScrollViewListener.scrollUp();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

}