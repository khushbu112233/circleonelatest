package com.amplearch.circleonet.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class ConnectFragment extends Fragment
{
    public static CustomViewPager mViewPager;
    TabLayout tabLayout;

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

     //   SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        // Set up the ViewPager with the sections adapter.

       // mViewPager.setAdapter(mSectionsPagerAdapter);

        SectionsPagerAdapter mSectionsPagerAdapter = new ConnectFragment.SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (CustomViewPager) view.findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);

        setupViewPager(mViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
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
