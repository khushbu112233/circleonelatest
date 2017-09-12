package com.circle8.circleOne.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.circle8.circleOne.Fragments.TutorialScreenFragment;
import com.circle8.circleOne.R;

/**
 * Created by admin on 09/12/2017.
 */

public class TutorialPagerAdapter extends FragmentStatePagerAdapter {
    private static final int COUNT = 4;

    // Images resources
    private static final int[] IMAGE_RES_IDS = {
            R.drawable.walkthrough_1, R.drawable.walkthrough_2, R.drawable.walkthrough_3, R.drawable.walkthrough_4, R.drawable.walkthrough_5 };

    // Text resources
    private static final int[] TITLES_RES_IDS = {
            R.string.app_name, R.string.app_name, R.string.app_name, R.string.app_name, R.string.app_name };

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TutorialScreenFragment.newInstance(IMAGE_RES_IDS[position], TITLES_RES_IDS[position]);
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}