package com.circle8.circleOne.Fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentCardsBinding;

import static com.circle8.circleOne.Activity.CardsActivity.Connection_Limit;
import static com.circle8.circleOne.Activity.DashboardActivity.setActionBarTitle;

public class CardsFragment extends Fragment
{
    public static TabLayout tabLayout;
    public static FragmentCardsBinding fragmentCardsBinding;
    View view;
    SectionsPagerAdapter mSectionsPagerAdapter;
    Context context;
    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        fragmentCardsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false);
        view = fragmentCardsBinding.getRoot();
        context=getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(Pref.getValue(getActivity(), "AddQr", "").equalsIgnoreCase("1")) {
            Pref.setValue(getActivity(), "AddQr", "0");
        }else
        {
            ((AppCompatActivity) context).getSupportActionBar().show();
            ((AppCompatActivity) context).getSupportActionBar().setShowHideAnimationEnabled(false);

        }

        if (Pref.getValue(getContext(), "current_frag", "").equalsIgnoreCase("1")) {
            try {
                if (Connection_Limit.equalsIgnoreCase("100000")) {
                    setActionBarTitle("Cards - " + List1Fragment.count + "/", true);
                } else {
                    setActionBarTitle("Cards - " + List1Fragment.count + "/" + Connection_Limit, false);

                }
            } catch (Exception e) {

            }
        }
        final AlertDialog ShadowAlertDialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.card_dialog_shadow_layout, null);
        LinearLayout llDialog = (LinearLayout) dialogView.findViewById(R.id.llDialog);

        ColorDrawable dialogColor = new ColorDrawable(getResources().getColor(R.color.black));
        dialogColor.setAlpha(200);
        ShadowAlertDialog.getWindow().setBackgroundDrawable(dialogColor);
        ShadowAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        // tvBarName.setText(User_name);
//                    bitmap = TextToImageEncode(barName);
        //  ivBarImage.setImageBitmap(mergeBitmaps(overlay, bitmap));
//
        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShadowAlertDialog.dismiss();
            }
        });

        ShadowAlertDialog.setView(dialogView);
        ShadowAlertDialog.show();
        // Set up the ViewPager with the sections adapter.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
                fragmentCardsBinding.container1.setOffscreenPageLimit(3);
                fragmentCardsBinding.container1.setAdapter(mSectionsPagerAdapter);
                fragmentCardsBinding.container1.setPagingEnabled(false);
                fragmentCardsBinding.tabs1.setupWithViewPager(fragmentCardsBinding.container1);
                fragmentCardsBinding.tabs1.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
                setupTabIcons();

            }
        }, 3);


        fragmentCardsBinding.tabs1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    try {
                        if (Connection_Limit.equalsIgnoreCase("100000")) {
                            setActionBarTitle("Cards - " + List1Fragment.count + "/" , true);
                        }
                        else {
                            setActionBarTitle("Cards - " + List1Fragment.count + "/" + Connection_Limit, false);
                        }
                    }catch (Exception e){
                    }
                }
                else if (tab.getPosition() == 1) {
                    try {
                        if (Connection_Limit.equalsIgnoreCase("100000")) {
                            setActionBarTitle("Cards - " + List2Fragment.counts + "/" , true);
                        }
                        else {
                            setActionBarTitle("Cards - " + List2Fragment.counts + "/" + Connection_Limit, false);
                        }
                    }catch (Exception e){
                    }
                }
                else if (tab.getPosition() == 2) {

                    try {
                        if (Connection_Limit.equalsIgnoreCase("100000")) {
                            setActionBarTitle("Cards - " + List4Fragment.counts + "/" , true);
                        }
                        else {
                            setActionBarTitle("Cards - " + List4Fragment.counts + "/" + Connection_Limit, false);
                        }
                    }catch (Exception e){

                    }
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fragmentCardsBinding.tabs1.getApplicationWindowToken(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        DashboardActivity.setDrawerVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.callMainPage("Card");

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            //do nothing here! no call to super.restoreState(arg0, arg1);
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position == 0)
            {

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                return List1Fragment.newInstance();
            }
            else if (position == 1)
            {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                return List2Fragment.newInstance();
                // return new List2Fragment();
            }
            else if (position == 2)
            {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

                return List4Fragment.newInstance();
                //return new List3Fragment();
            }
            else
            {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                return List1Fragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.icon3,
                R.drawable.icon1,
                R.drawable.icon2
                // R.drawable.ic_tab_contacts
        };

        fragmentCardsBinding.tabs1.getTabAt(0).setIcon(tabIcons[0]);
        fragmentCardsBinding.tabs1.getTabAt(1).setIcon(tabIcons[1]);
        fragmentCardsBinding.tabs1.getTabAt(2).setIcon(tabIcons[2]);
    }
}
