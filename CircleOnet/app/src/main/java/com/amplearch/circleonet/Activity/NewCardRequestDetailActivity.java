package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplearch.circleonet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCardRequestDetailActivity extends AppCompatActivity
{
    private CircleImageView imgProfile ;
    private TextView tvPerson, tvCompany, tvDesignation, tvProfile ;
    private LinearLayout llDefaultCard, llNewCard ;

    private String name, image, company, designation, phone, profile;

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

        llDefaultCard = (LinearLayout)findViewById(R.id.llDefaultCard);
        llNewCard = (LinearLayout)findViewById(R.id.llNewCard);


        Intent i = getIntent();
        name = i.getStringExtra("person") ;
        designation = i.getStringExtra("designation") ;
        company = i.getStringExtra("company") ;
        phone = i.getStringExtra("phone") ;
        profile = i.getStringExtra("profile");
        image = i.getStringExtra("image") ;

        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));
        imgProfile.setImageResource(Integer.parseInt(i.getStringExtra("image")));

        llDefaultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", name);
                i.putExtra("designation", designation);
                i.putExtra("company", company);
                i.putExtra("profile", profile);
                i.putExtra("image", image);
                i.putExtra("phone",profile);
                startActivity(i);
            }
        });

        llNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", i.getStringExtra("person"));
                i.putExtra("designation", i.getStringExtra("designation"));
                i.putExtra("company", i.getStringExtra("company"));
                i.putExtra("profile", i.getStringExtra("profile"));
                i.putExtra("image", i.getStringExtra("image"));
                i.putExtra("phone", i.getStringExtra("phone"));
                startActivity(i);
            }
        });

    }
}
