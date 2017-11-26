package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

public class CreateGroupActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }
}
