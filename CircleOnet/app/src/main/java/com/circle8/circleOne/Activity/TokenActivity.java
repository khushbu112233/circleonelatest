package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.TokenActivityLayoutBinding;

/**
 * Created by ample-arch on 2/23/2018.
 */

public class TokenActivity extends AppCompatActivity implements View.OnClickListener {
    TokenActivityLayoutBinding tokenActivityLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.token_activity_layout);

        tokenActivityLayoutBinding.imageLeft.setOnClickListener(this);
        tokenActivityLayoutBinding.imageRight.setOnClickListener(this);
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
}
