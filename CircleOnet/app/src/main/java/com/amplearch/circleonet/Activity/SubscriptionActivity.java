package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.amplearch.circleonet.Adapter.SubscriptionAdapter;
import com.amplearch.circleonet.Model.SubscriptionModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

public class SubscriptionActivity extends AppCompatActivity
{
    private ListView listView ;
    private ImageView ivBack ;

    private ArrayList<SubscriptionModel> subscriptionModelArrayList ;

    private ArrayList<String> packageName = new ArrayList<>();
    private ArrayList<Integer> contact = new ArrayList<>();
    private ArrayList<Integer> group = new ArrayList<>();
    private ArrayList<Integer> connection = new ArrayList<>();
    private ArrayList<Integer> amount = new ArrayList<>();
    private ArrayList<Integer> left_connection = new ArrayList<>();

    private SubscriptionAdapter subscriptionAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        ivBack = (ImageView)findViewById(R.id.imgBack);
        listView = (ListView)findViewById(R.id.listView);

        subscriptionModelArrayList = new ArrayList<>();

        packageName.add("Free Package");
        contact.add(10);
        group.add(2);
        connection.add(2);
        amount.add(0);
        left_connection.add(0);

        packageName.add("Package 1");
        contact.add(300);
        group.add(5);
        connection.add(1);
        amount.add(1);
        left_connection.add(1);

        packageName.add("Package 2");
        contact.add(350);
        group.add(6);
        connection.add(2);
        amount.add(2);
        left_connection.add(0);

        packageName.add("Package 3");
        contact.add(400);
        group.add(10);
        connection.add(8);
        amount.add(3);
        left_connection.add(2);

        packageName.add("Package 4");
        contact.add(450);
        group.add(20);
        connection.add(20);
        amount.add(3);
        left_connection.add(0);

        packageName.add("Package 5");
        contact.add(1000);
        group.add(200);
        connection.add(200);
        amount.add(30);
        left_connection.add(10);

        subscriptionAdapter = new SubscriptionAdapter(getApplicationContext(),
                packageName, contact, group, connection, amount, left_connection);
        listView.setAdapter(subscriptionAdapter);
        subscriptionAdapter.notifyDataSetChanged();

    }
}
