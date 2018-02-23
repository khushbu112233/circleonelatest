package com.circle8.circleOne.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.circle8.circleOne.R;

/**
 * Created by Ample-Arch on 21-02-2018.
 */

public class GradientTextView extends AppCompatTextView
{
    public GradientTextView( Context context )
    {
        super( context, null, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void onLayout( boolean changed,
                             int left, int top, int right, int bottom )
    {
        super.onLayout( changed, left, top, right, bottom );
        if(changed)
        {
            getPaint().setShader( new LinearGradient(
                    0, 0, 0, getHeight(),
                    getResources().getColor(R.color.gradient_timer_start), getResources().getColor(R.color.gradient_timer_end),
                    Shader.TileMode.CLAMP ) );
        }
    }
}
