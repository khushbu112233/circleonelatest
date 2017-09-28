package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/28/2017.
 */

public class orkebyBoldTextView extends AppCompatTextView
{
    public orkebyBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public orkebyBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public orkebyBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Orkney-Bold.otf");
        setTypeface(tf);
    }

}

