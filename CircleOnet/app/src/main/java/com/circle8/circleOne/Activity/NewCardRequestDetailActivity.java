package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCardRequestDetailActivity extends AppCompatActivity
{
    private CircleImageView imgProfile ;
    private TextView tvPerson, tvCompany, tvDesignation, tvProfile ;
    private LinearLayout llDefaultCard, llNewCard ;

    private String name, image, company, designation, phone, profile;

    private CardSwipe myPager ;
    private ArrayList<String> swipe_image = new ArrayList<>();
    String recycle_image1, recycle_image2 ;
    ViewPager mViewPager1, mViewPager2;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request_detail);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        tvPerson = (TextView)findViewById(R.id.tvPersonName);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvDesignation = (TextView)findViewById(R.id.tvDesignation);
        tvProfile = (TextView)findViewById(R.id.tvProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        llDefaultCard = (LinearLayout)findViewById(R.id.llDefaultCard);
        llNewCard = (LinearLayout)findViewById(R.id.llNewCard);

        mViewPager1 = (ViewPager)findViewById(R.id.viewPager);
        mViewPager2 = (ViewPager)findViewById(R.id.viewPager1);

        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
        swipe_image.add(recycle_image1);
        swipe_image.add(recycle_image2);
        myPager = new CardSwipe(getApplicationContext(), swipe_image);

        mViewPager1.setClipChildren(false);
        mViewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager1.setOffscreenPageLimit(1);
        mViewPager1.setAdapter(myPager);

        mViewPager2.setClipChildren(false);
        mViewPager2.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.setAdapter(myPager);

        Intent i = getIntent();
        name = i.getStringExtra("person") ;
        designation = i.getStringExtra("designation") ;
        company = i.getStringExtra("company") ;
        phone = i.getStringExtra("phone") ;
        profile = i.getStringExtra("profile");
        image = i.getStringExtra("image") ;

        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));
      //  imgProfile.setImageResource(Integer.parseInt(i.getStringExtra("image")));
        if (image.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+image).into(imgProfile);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llDefaultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", name);
                i.putExtra("designation", designation);
                i.putExtra("company", company);
                i.putExtra("profile", profile);
                i.putExtra("image", image);
                i.putExtra("phone",profile);
                startActivity(i);
            }
        });

        llNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", name);
                i.putExtra("designation", designation);
                i.putExtra("company", company);
                i.putExtra("profile", profile);
                i.putExtra("image", image);
                i.putExtra("phone",profile);
                startActivity(i);
            }
        });

        mViewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager1.scrollTo(mViewPager2.getScrollX(), mViewPager2.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager1.setCurrentItem(mViewPager2.getCurrentItem(), false);
                }
            }
        });

    }
}
