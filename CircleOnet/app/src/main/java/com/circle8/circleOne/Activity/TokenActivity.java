package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.TokenActivityLayoutBinding;

/**
 * Created by ample-arch on 2/23/2018.
 */

public class TokenActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    TokenActivityLayoutBinding tokenActivityLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.token_activity_layout);

        tokenActivityLayoutBinding.imageLeft.setOnClickListener(this);
        tokenActivityLayoutBinding.imageRight.setOnClickListener(this);
        tokenActivityLayoutBinding.sliderZoom.setOnSeekBarChangeListener(this);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL, tokenActivityLayoutBinding.sliderZoom.getId());
        Rect thumbRect = tokenActivityLayoutBinding.sliderZoom.getSeekBarThumb().getBounds();
        p.setMargins(
                thumbRect.centerX(),0, 0, 0);
        tokenActivityLayoutBinding.txtSeekValue.setLayoutParams(p);
        tokenActivityLayoutBinding.txtSeekValue.setText(String.valueOf(tokenActivityLayoutBinding.sliderZoom.getProgress()) + "");

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageLeft:
                // Do something
                tokenActivityLayoutBinding.sliderZoom.setProgress(tokenActivityLayoutBinding.sliderZoom.getProgress()-5);
                break;

            case R.id.imageRight:
                // Do something
                tokenActivityLayoutBinding.sliderZoom.setProgress(tokenActivityLayoutBinding.sliderZoom.getProgress()+5);
                break;
        }
    }

    /*public Drawable getThumb(int progress) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }*/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       /* int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
        _testText.setText("" + progress);
        _testText.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
*/
       // tokenActivityLayoutBinding.sliderZoom.setThumb(getThumb(progress));

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_VERTICAL, seekBar.getId());
        Rect thumbRect = tokenActivityLayoutBinding.sliderZoom.getSeekBarThumb().getBounds();
        p.setMargins(
                thumbRect.centerX(),0, 0, 0);
        tokenActivityLayoutBinding.txtSeekValue.setLayoutParams(p);
        tokenActivityLayoutBinding.txtSeekValue.setText(String.valueOf(progress) + "");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}