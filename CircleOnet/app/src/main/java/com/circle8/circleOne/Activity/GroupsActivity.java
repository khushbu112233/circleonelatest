package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.circle8.circleOne.Adapter.GroupsItemsAdapter;
import com.circle8.circleOne.R;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity
{
    ArrayList<String> groupName = new ArrayList<>();
    GroupsItemsAdapter groupsItemsAdapter ;

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        listView = (ListView)findViewById(R.id.listView);

        groupName.add("Group 1");
        groupName.add("Group 2");
        groupName.add("Group 3");

        groupsItemsAdapter = new GroupsItemsAdapter(getApplicationContext(), groupName);
        listView.setAdapter(groupsItemsAdapter);
        groupsItemsAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
