package com.circle8.circleOne.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.circle8.circleOne.R;
import com.circle8.circleOne.TabPagerAdapter;
import com.circle8.circleOne.Utils.Utility;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabPagerAdapter tabPagerAdapter;
	private ActionBar actionBar;
	private String[] tabNames = { "First", "Second", "Third" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabPagerAdapter);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 0; i < 3; i++) {
			actionBar.addTab(actionBar.newTab().setText(tabNames[i])
					.setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				actionBar.setSelectedNavigationItem(postion);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}



	@Override
	public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0)
            getSupportActionBar().show();
        else if (tab.getPosition() == 1)
            getSupportActionBar().show();
        else if (tab.getPosition() == 2)
            getSupportActionBar().hide();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

	}
}
