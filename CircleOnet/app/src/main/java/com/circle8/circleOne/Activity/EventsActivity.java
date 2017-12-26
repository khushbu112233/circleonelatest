package com.circle8.circleOne.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Adapter.EventsAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

public class EventsActivity extends AppCompatActivity
{
    private ListView listView;
    private EventsAdapter gridAdapter;
    private TextView actionText;
    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1;
    private static ImageView ivConnecting2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Events");


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
