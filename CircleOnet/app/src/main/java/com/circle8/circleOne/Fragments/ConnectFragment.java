package com.circle8.circleOne.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.circle8.circleOne.Utils.Utility.callSubPAge;

public class ConnectFragment extends AppCompatActivity
{
    public static CustomViewPager mViewPager;
    TabLayout tabLayout;
    private TextView actionText;
    ImageView imgDrawer, imgLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_connect);
        SectionsPagerAdapter mSectionsPagerAdapter = new ConnectFragment.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(true);

        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_2);

        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Connect");
        imgDrawer = findViewById(R.id.drawer);
        imgLogo = findViewById(R.id.imgLogo);

        imgDrawer.setVisibility(View.GONE);
        imgLogo.setVisibility(View.VISIBLE);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        callSubPAge("Connect","LeftMenu");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
      /*  adapter.addFrag(new ConnectListFragment(), "by Name");
        adapter.addFrag(new ConnectListFragment(), "by Company Name");
        adapter.addFrag(new ConnectListFragment(), "by Title");
        adapter.addFrag(new ConnectListFragment(), "by Industry");
        adapter.addFrag(new ConnectListFragment(), "by Association");*/
        adapter.addFrag(new ByNameFragment(), "by Name");
        adapter.addFrag(new ByCompanyFragment(), "by Company Name");
        adapter.addFrag(new ByTitleFragment(), "by Title");
        adapter.addFrag(new ByIndustryFragment(), "by Industry");
        adapter.addFrag(new ByAssociationFragment(), "by Association");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                return new ByNameFragment();
            }
            else if (position == 1) {
                return new ByCompanyFragment();
            }
            else if (position == 2) {
                return new ByTitleFragment();
            }
            else if (position == 3) {
                return new ByIndustryFragment();
            }
            else if (position == 4) {
                return new ByAssociationFragment();
            }
            else
            {
                return new ByNameFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
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

}
