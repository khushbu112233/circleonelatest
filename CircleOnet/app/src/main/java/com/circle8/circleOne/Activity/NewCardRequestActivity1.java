package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCardRequestActivity1 extends AppCompatActivity
{
    private CircleImageView imgProfile  ;
    private ImageView ivSubmit ;
    private TextView tvPerson, tvDesignation, tvCompany, tvProfile ;
    private EditText etPerson, etCompany, etPhone, etAddress1, etAddress2 ;

    private CardSwipe myPager ;
    private ArrayList<String> swipe_image = new ArrayList<>();
    String recycle_image1, recycle_image2 ;
    ViewPager mViewPager1, mViewPager2;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        ivSubmit = (ImageView)findViewById(R.id.ivSubmit);

        tvPerson = (TextView)findViewById(R.id.tvPersonName);
        tvDesignation = (TextView)findViewById(R.id.tvDesignation);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvProfile = (TextView)findViewById(R.id.tvProfile);

        etPerson = (EditText)findViewById(R.id.etPerson);
        etCompany = (EditText)findViewById(R.id.etCompany);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);

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
        image = i.getStringExtra("image");
        if (image.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+image).into(imgProfile);
        }
        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));

        etPerson.setText(i.getStringExtra("person"));
        etCompany.setText(i.getStringExtra("company"));
        etPhone.setText(i.getStringExtra("profile"));
        etAddress1.setText("Address");
        etAddress2.setText("");

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
