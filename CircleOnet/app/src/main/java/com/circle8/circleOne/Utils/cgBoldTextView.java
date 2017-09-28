package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/28/2017.
 */

public class cgBoldTextView extends AppCompatTextView
{
    public cgBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public cgBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public cgBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "cgbold.ttf");
        setTypeface(tf);
    }

}

