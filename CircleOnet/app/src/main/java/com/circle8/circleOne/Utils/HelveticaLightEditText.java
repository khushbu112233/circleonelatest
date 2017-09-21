package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by admin on 09/21/2017.
 */

public class HelveticaLightEditText extends AppCompatEditText
{

    public HelveticaLightEditText(Context context) {
        super(context);
        init();
    }

    public HelveticaLightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaLightEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "HelveticaNeue.dfont");
        setTypeface(tf);
    }
}
