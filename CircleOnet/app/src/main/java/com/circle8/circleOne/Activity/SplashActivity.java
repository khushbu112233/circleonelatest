package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.Walkthrough.HelpActivity;

public class SplashActivity extends AppCompatActivity
{
    private ImageView imageView1, imageView2;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("com.circle8.circleOne", MODE_PRIVATE);
        imageView1 = (ImageView)findViewById(R.id.imgLogin);
        imageView2 = (ImageView)findViewById(R.id.imgLogin1);

        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run()
            {

                if (prefs.getBoolean("firstrun", true)) {
                    // Do first run stuff here then set 'firstrun' as false
                    // using the following line to edit/commit prefs
                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);
                    //  prefs.edit().putBoolean("firstrun", false).commit();
                    finish();
                } else {
                    if( Pref.getValue(SplashActivity.this,"login_value","").equalsIgnoreCase("1"))
                    {
                        Utility.freeMemory();
                        Pref.setValue(SplashActivity.this,"appopen","1");
                        Intent userIntent = new Intent(SplashActivity.this, CardsActivity.class);
                        userIntent.putExtra("viewpager_position", 0);
                        startActivity(userIntent);
                        finish();

                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }

                }


            }
        },1000);
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }
}
