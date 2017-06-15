package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amplearch.circleonet.Adapter.EventsAdapter;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    private ListView listView;
    private EventsAdapter gridAdapter;
    private TextView actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Events");
        ArrayList<Integer> image = new ArrayList<Integer>();
        image.add(R.drawable.events1);
        image.add(R.drawable.events2);
        image.add(R.drawable.events3);
        image.add(R.drawable.events4);
        image.add(R.drawable.events5);

        ArrayList<String> title = new ArrayList<>();
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");

        ArrayList<String> desc = new ArrayList<>();
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");

        listView = (ListView) findViewById(R.id.listEvents);
        gridAdapter = new EventsAdapter(EventsActivity.this, R.layout.row_events, image, title, desc);
        listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                startActivity(intent);
            }
        });

    }
}
