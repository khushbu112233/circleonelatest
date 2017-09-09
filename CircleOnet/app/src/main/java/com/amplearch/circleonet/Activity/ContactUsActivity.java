package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.R;

public class ContactUsActivity extends AppCompatActivity
{
    private ImageView ivMessage, ivPhone ;
    private TextView tvAddress1, tvAddress2, tvWebsite, tvEmail, tvPhone, tvFax ;
    private TextView tvCompany, tvPartner ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        tvCompany = (TextView)findViewById(R.id.tvCompanyName);
        tvPartner = (TextView)findViewById(R.id.tvPartner);
        tvAddress1 = (TextView)findViewById(R.id.tvAddress1);
        tvAddress2 = (TextView)findViewById(R.id.tvAddress2);
        tvWebsite = (TextView)findViewById(R.id.tvWebsite);
        tvEmail = (TextView)findViewById(R.id.tvMail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvFax = (TextView)findViewById(R.id.tvWork);
        
    }
}
