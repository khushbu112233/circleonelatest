package com.circle8.circleOne.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.databinding.FragmentDashboardLayoutBinding;

/**
 * Created by Ample-Arch on 06-01-2018.
 */

public class DashboardFragment extends Fragment {
    FragmentDashboardLayoutBinding fragmentDashboardLayoutBinding;
    View view;
    Context context;
    int count=0;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        fragmentDashboardLayoutBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dashboard_layout, container, false);
        view = fragmentDashboardLayoutBinding.getRoot();
        context =getActivity();
        initClick();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        count++;
        if(Pref.getValue(context,"appopen","").equalsIgnoreCase("1"))
        {
            fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            Pref.setValue(context,"appopen","");
        }else
        {
            if(count==1) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            }else if(count==2) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard2);
            }else if(count==3) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard3);
            }else if(count==4) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard4);
                count=0;
            }
        }
    }

    public void initClick() {

        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlTapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
