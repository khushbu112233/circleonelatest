package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/21/2017.
 */

public class HelveticaLightTextView extends AppCompatTextView
{
    public HelveticaLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HelveticaLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaLightTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "HelveticaNeue.dfont");
        setTypeface(tf);
    }

}
