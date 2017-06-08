package com.amplearch.circleonet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class OneFragment extends Fragment{

    private ArrayList<Integer> image = new ArrayList<>();
    private ViewPager viewPager;
    private MyPager myPager ;

    public OneFragment() {
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

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer

        image.add(R.drawable.e_bike1);


        image.add(R.drawable.e_bike2);


        image.add(R.drawable.e_bike3);


        myPager = new MyPager(getContext(), image, image);
        viewPager.setAdapter(myPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private int index = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }



}
