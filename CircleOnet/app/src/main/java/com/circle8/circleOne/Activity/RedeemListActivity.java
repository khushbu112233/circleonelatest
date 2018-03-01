package com.circle8.circleOne.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.circle8.circleOne.Adapter.RedeemListAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.RedeemListLayoutBinding;

/**
 * Created by ample-arch on 2/27/2018.
 */

public class RedeemListActivity extends AppCompatActivity {
    RedeemListLayoutBinding redeemListLayoutBinding;
    RedeemListAdapter redeemListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redeemListLayoutBinding = DataBindingUtil.setContentView(this,R.layout.redeem_list_layout);
        redeemListAdapter  = new RedeemListAdapter(RedeemListActivity.this);
        redeemListLayoutBinding.lstRedeem.setAdapter(redeemListAdapter);
    }
}
