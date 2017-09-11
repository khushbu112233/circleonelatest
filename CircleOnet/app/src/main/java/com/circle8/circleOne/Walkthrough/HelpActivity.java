package com.circle8.circleOne.Walkthrough;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class HelpActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_help);

        addSlide(AppIntroFragment.newInstance("", "", R.drawable.walkthrough_1, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("", "", R.drawable.walkthrough_2, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("", "", R.drawable.walkthrough_3, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("", "", R.drawable.walkthrough_4, Color.TRANSPARENT));

        addSlide(AppIntroFragment.newInstance("", "", R.drawable.walkthrough_5, Color.TRANSPARENT));

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
