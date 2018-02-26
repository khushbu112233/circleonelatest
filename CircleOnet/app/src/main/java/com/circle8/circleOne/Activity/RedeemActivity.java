package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.RedeemActivityLayoutBinding;

/**
 * Created by ample-arch on 2/26/2018.
 */

public class RedeemActivity extends AppCompatActivity {
    RedeemActivityLayoutBinding redeemActivityLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redeemActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.redeem_activity_layout);
    }
}
