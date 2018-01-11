package com.circle8.circleOne.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.CustomViewPager;
import com.circle8.circleOne.Utils.Utility;

import java.text.DecimalFormatSymbols;

import static com.circle8.circleOne.Activity.CardsActivity.Connection_Limit;
import static com.circle8.circleOne.Activity.DashboardActivity.setActionBarTitle;

public class CardsFragment extends Fragment
{

    public static CustomViewPager mViewPager;
    public static TabLayout tabLayout;
    public CardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        FragmentManager fragmentManager=((FragmentActivity)getContext()).getSupportFragmentManager();

                        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++){
                            fragmentManager.popBackStack();
                        }

                        DashboardFragment fragment = new DashboardFragment();
                        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().add(R.id.main_container_wrapper, fragment).commit();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        try {
            if (Connection_Limit.equalsIgnoreCase("100000")) {
                setActionBarTitle("Cards - " + List1Fragment.count + "/", true);
            }
            else {
                setActionBarTitle("Cards - " + List1Fragment.count + "/" + Connection_Limit, false);

            }
        }catch (Exception e){

        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) view.findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
      //  mViewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        setupTabIcons();
        //mViewPager.setCurrentItem(CardsActivity.nested_position);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                //    setActionBarTitle("Cards - " + List1Fragment.count + "/" + Connection_Limit);
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
                imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DashboardActivity.setDrawerVisibility(true);

        return view;
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
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                List1Fragment tab1 = new List1Fragment();
                return tab1;
               // return new List1Fragment();
            }
            else if (position == 1)
            {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                List2Fragment tab2 = new List2Fragment();
                return tab2;
               // return new List2Fragment();
            }
            else if (position == 2)
            {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                List4Fragment tab1 = new List4Fragment();
                return tab1;
                //return new List3Fragment();
            }
            else
            {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                List1Fragment tab1 = new List1Fragment();
                return tab1;
               // return new List1Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return getString(R.string.app_name);
                case 1:
                    return getString(R.string.hello_blank_fragment);
            }*/
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

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


}
