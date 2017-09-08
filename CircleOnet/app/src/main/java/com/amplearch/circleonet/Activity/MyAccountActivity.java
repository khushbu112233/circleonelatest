package com.amplearch.circleonet.Activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplearch.circleonet.R;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText etUserName, etFirstName, etLastName, etPassword,
            etPasswordAgain, etEmail, etDOB, etAddress1 , etAddress2 ;
    private TextView tvSave, tvCancel ;
    private ImageView ivFemaleround, ivFemaleImg, iv_ConnectImg,
            ivMaleRound, ivMaleImg ;
    private RelativeLayout rlMale, rlFemale ;
    private View line_view1, line_view2 ;

    private String gender ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        etUserName = (EditText)findViewById(R.id.etUserName);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etPasswordAgain = (EditText)findViewById(R.id.etPasswordAgain);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etDOB = (EditText)findViewById(R.id.etDob);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);

        tvSave = (TextView)findViewById(R.id.tvSave);
        tvCancel = (TextView)findViewById(R.id.tvCancel);

        rlMale = (RelativeLayout)findViewById(R.id.ivMale);
        rlFemale = (RelativeLayout)findViewById(R.id.ivFemale);

        line_view1 = (View)findViewById(R.id.vwDrag1);
        line_view2 = (View)findViewById(R.id.vwDrag2);

        ivFemaleround = (ImageView)findViewById(R.id.ivFemaleround);
        ivFemaleImg = (ImageView)findViewById(R.id.ivFemaleImg);
        iv_ConnectImg = (ImageView)findViewById(R.id.iv_ConnectImg);
        ivMaleRound = (ImageView)findViewById(R.id.ivMaleRound);
        ivMaleImg = (ImageView)findViewById(R.id.ivMaleImg);

        tvSave.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if( v == tvSave)
        {

        }
        if( v == tvCancel)
        {

        }
        if( v == rlMale)
        {
            TranslateAnimation slide1 = new TranslateAnimation(0, -190, 0, 0);
            slide1.setDuration(1000);
            iv_ConnectImg.startAnimation(slide1);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    line_view2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                    ivFemaleround.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            line_view1.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivMaleImg.setImageResource(R.drawable.ic_male);
            ivMaleRound.setImageResource(R.drawable.round_blue);
            gender = "M";
//            txtGender.setText("Gender: Male");
        }
        if( v == rlFemale)
        {
            TranslateAnimation slide = new TranslateAnimation(0, 190, 0, 0);
            slide.setDuration(1000);
            iv_ConnectImg.startAnimation(slide);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    line_view1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                    ivMaleRound.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            line_view2.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivFemaleImg.setImageResource(R.drawable.ic_female);
            ivFemaleround.setImageResource(R.drawable.round_blue);
            gender = "F";
//            txtGender.setText("Gender: Female");
        }

    }
}
