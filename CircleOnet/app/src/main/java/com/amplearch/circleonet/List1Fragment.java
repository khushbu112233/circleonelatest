package com.amplearch.circleonet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class List1Fragment extends Fragment{

    private ArrayList<Integer> imageFront = new ArrayList<>();
    private ArrayList<Integer> imageBack = new ArrayList<>();
    private ViewPager viewPager;
    private MyPager myPager ;

    public List1Fragment() {
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

        View view = inflater.inflate(R.layout.fragment_list1, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer

        imageFront.add(R.drawable.card1_front);


        imageFront.add(R.drawable.card2_front);


        imageFront.add(R.drawable.card3_front);

        imageFront.add(R.drawable.card4_front);
        imageFront.add(R.drawable.card5_front);


        imageBack.add(R.drawable.card1_back);


        imageBack.add(R.drawable.card2_back);


        imageBack.add(R.drawable.card3_back);

        imageBack.add(R.drawable.card4_back);
        imageBack.add(R.drawable.card5_back);


        myPager = new MyPager(getContext(), imageFront, imageBack);
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
