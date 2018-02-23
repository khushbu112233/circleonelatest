package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.TokenActivityLayoutBinding;

/**
 * Created by ample-arch on 2/23/2018.
 */

public class TokenActivity extends AppCompatActivity {
    TokenActivityLayoutBinding tokenActivityLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.token_activity_layout);
    }
}
