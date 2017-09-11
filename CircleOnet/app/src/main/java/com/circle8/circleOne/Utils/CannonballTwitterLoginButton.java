package com.circle8.circleOne.Utils;

import android.content.Context;
import android.util.AttributeSet;

import com.circle8.circleOne.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by admin on 08/05/2017.
 */

public class CannonballTwitterLoginButton extends TwitterLoginButton {
    public CannonballTwitterLoginButton(Context context) {
        super(context);
        init();
    }

    public CannonballTwitterLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CannonballTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()){
            return;
        }
       /* setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable
                .icon_twitter), null, null, null);*/
        setBackgroundResource(R.drawable.icon_twitter);
        setTextSize(20);
        setHeight(28);
        setWidth(28);
        setPadding(30, 0, 30, 0);
        setTextColor(getResources().getColor(R.color.tw__blue_default));
    }
}
