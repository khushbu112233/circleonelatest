package com.amplearch.circleonet.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

public class CardDetail extends AppCompatActivity {

    ViewPager mViewPager;
    private ArrayList<Integer> image = new ArrayList<>();
    private CardSwipe myPager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mViewPager.setClipChildren(false);
        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

        image.add(R.drawable.card1_front);
        image.add(R.drawable.card1_back);

        myPager = new CardSwipe(getApplicationContext(), image);
        mViewPager.setAdapter(myPager);

    }
}
