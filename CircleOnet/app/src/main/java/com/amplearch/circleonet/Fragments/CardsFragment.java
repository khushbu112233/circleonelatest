package com.amplearch.circleonet.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CustomViewPager;

public class CardsFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) view.findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        setupTabIcons();
        mViewPager.setCurrentItem(CardsActivity.nested_position);
        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new List1Fragment();
            } else if (position == 1) {
                return new List2Fragment();
            }
            else if (position == 2) {
                return new List3Fragment();
            }
            else if (position == 3) {
                return new List4Fragment();
            }
            else {
                return new List1Fragment();
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

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.icon3,
                R.drawable.icon1,
                R.drawable.icon2,
                R.drawable.icon4
                // R.drawable.ic_tab_contacts
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }


}
