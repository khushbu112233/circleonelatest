package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/21/2017.
 */

public class latoTextView extends AppCompatTextView
{
    public latoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public latoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public latoTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "lato_black.ttf");
        setTypeface(tf);
    }

}

