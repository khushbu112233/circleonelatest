package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/28/2017.
 */

public class GillSans extends AppCompatTextView
{
    public GillSans(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GillSans(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GillSans(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "gillsans.ttf");
        setTypeface(tf);
    }

}


