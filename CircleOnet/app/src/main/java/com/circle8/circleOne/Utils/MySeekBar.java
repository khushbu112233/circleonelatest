package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Ample-Arch on 27-02-2018.
 */

public class MySeekBar extends AppCompatSeekBar {

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
    }

    Drawable mThumb;

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }
}

