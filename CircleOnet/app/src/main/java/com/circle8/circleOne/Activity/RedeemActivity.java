package com.circle8.circleOne.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.circle8.circleOne.Adapter.RedeemListAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.RedeemActivityLayoutBinding;

/**
 * Created by ample-arch on 2/26/2018.
 */

public class RedeemActivity extends AppCompatActivity {
    RedeemActivityLayoutBinding redeemActivityLayoutBinding;
    RedeemListAdapter redeemListAdapter;
    private PopupWindow popupWindow;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redeemActivityLayoutBinding = DataBindingUtil.setContentView(this,R.layout.redeem_activity_layout);

        redeemListAdapter  = new RedeemListAdapter(RedeemActivity.this);
        redeemActivityLayoutBinding.lstRedeem.setAdapter(redeemListAdapter);
        final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
     //   redeemActivityLayoutBinding.llActivity.startAnimation(slide_up);


        redeemActivityLayoutBinding.imgrightDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = popupWindowsort();
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);

            }
        });
    }
    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(RedeemActivity.this);

        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // setContentView(inflator.inflate(layoutResID, null));

        View view1 = inflator.inflate(R.layout.layout_lucky_menu, null);
        popupWindow.setContentView(view1);
        popupWindow.setOutsideTouchable(true);

        RelativeLayout rltHistory = view1.findViewById(R.id.rltHistory);
        RelativeLayout rltContactUs = view1.findViewById(R.id.rltContactUs);
        RelativeLayout rltHelp = view1.findViewById(R.id.rltHelp);

        ImageView imgDismiss = view1.findViewById(R.id.imgMenu);

        // set our adapter and pass our pop up window contents

        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });


        // set on item selected

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content

        return popupWindow;
    }

}
