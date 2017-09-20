package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.circle8.circleOne.R;

public class SplashActivity extends AppCompatActivity
{
    private ImageView imageView1, imageView2;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView1 = (ImageView)findViewById(R.id.imgLogin);
        imageView2 = (ImageView)findViewById(R.id.imgLogin1);

        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);

                TranslateAnimation slide = new TranslateAnimation(0, 0, 250,0 );
                slide.setDuration(1000);
                imageView1.startAnimation(slide);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                },1500);

//                Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.img_anim);
//                imageView1.startAnimation(anim);
            }
        }, SPLASH_TIME_OUT);


    }
}
