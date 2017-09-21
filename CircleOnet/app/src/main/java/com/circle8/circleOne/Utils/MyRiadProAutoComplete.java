package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/21/2017.
 */

public class MyRiadProAutoComplete extends AppCompatAutoCompleteTextView {

    public MyRiadProAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyRiadProAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRiadProAutoComplete(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "MyriadPro-Regular.otf");
        setTypeface(tf);
    }
}
