package com.circle8.circleOne.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

public class Connect extends AppCompatActivity {

    private TextView actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Connect");
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

}
