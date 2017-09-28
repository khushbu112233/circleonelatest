package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by admin on 09/28/2017.
 */

public class orkebyEditView extends AppCompatEditText
{

    public orkebyEditView(Context context) {
        super(context);
        init();
    }

    public orkebyEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public orkebyEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Orkney-Light.otf");
        setTypeface(tf);
    }
}
