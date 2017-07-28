package com.amplearch.circleonet.Walkthrough;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class HelpActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_help);

        addSlide(AppIntroFragment.newInstance("Welcome!", "This is a demo of the AppIntro library.", R.drawable.ic_slide1, Color.BLUE));

        addSlide(AppIntroFragment.newInstance("Welcome!", "This is a demo of the AppIntro library.", R.drawable.ic_slide2, Color.CYAN));

        addSlide(AppIntroFragment.newInstance("Welcome!", "This is a demo of the AppIntro library.", R.drawable.ic_slide3, Color.GRAY));

        addSlide(AppIntroFragment.newInstance("Welcome!", "This is a demo of the AppIntro library.", R.drawable.ic_slide4, Color.GREEN));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), CardsActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), CardsActivity.class));
        finish();
    }
}
