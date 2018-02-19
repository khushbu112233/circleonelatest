package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Walkthrough.HelpActivity;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageView1, imageView2;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("com.circle8.circleOne", MODE_PRIVATE);
        imageView1 = (ImageView) findViewById(R.id.imgLogin);
        imageView2 = (ImageView) findViewById(R.id.imgLogin1);

        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (prefs.getBoolean("firstrun", true)) {
                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (MyApplication.notiStatus.equals("")) {
                        if (Pref.getValue(SplashActivity.this, "login_value", "").equalsIgnoreCase("1")) {
                            Intent userIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                            //userIntent.putExtra("viewpager_position", 0);
                            startActivity(userIntent);
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    } else {
                        if (MyApplication.notiStatus.equalsIgnoreCase("dashboard")) {
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("event")) {
                            Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("rewards")) {
                            Intent intent = new Intent(getApplicationContext(), RewardsPointsActivity1.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("notification")) {
                            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("connect")) {
                            Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("newcard")) {
                            Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("circle")) {
                            Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("updateapp")) {
                            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://circle8.asia/mobileapp.html"));
                            viewIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(viewIntent);

                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("card")) {
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (MyApplication.notiStatus.equalsIgnoreCase("subscription")) {
                            Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        }, 1000);
    }
}
