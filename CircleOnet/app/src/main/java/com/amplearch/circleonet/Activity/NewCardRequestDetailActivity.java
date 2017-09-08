package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.amplearch.circleonet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCardRequestDetailActivity extends AppCompatActivity
{

    private CircleImageView imgProfile ;
    private TextView tvPerson, tvCompany, tvDesignation, tvProfile ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request_detail);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        tvPerson = (TextView)findViewById(R.id.tvPersonName);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvDesignation = (TextView)findViewById(R.id.tvDesignation);
        tvProfile = (TextView)findViewById(R.id.tvProfile);


        Intent i = getIntent();

        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));
        imgProfile.setImageResource(Integer.parseInt(i.getStringExtra("image")));

    }
}
