package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCardRequestActivity1 extends AppCompatActivity
{
    private CircleImageView imgProfile  ;
    private ImageView ivSubmit ;
    private TextView tvPerson, tvDesignation, tvCompany, tvProfile ;
    private EditText etPerson, etCompany, etPhone, etAddress1, etAddress2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        ivSubmit = (ImageView)findViewById(R.id.ivSubmit);

        tvPerson = (TextView)findViewById(R.id.tvPersonName);
        tvDesignation = (TextView)findViewById(R.id.tvDesignation);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvProfile = (TextView)findViewById(R.id.tvProfile);

        etPerson = (EditText)findViewById(R.id.etPerson);
        etCompany = (EditText)findViewById(R.id.etCompany);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);

        Intent i = getIntent();

        imgProfile.setImageResource(Integer.parseInt(i.getStringExtra("image")));

        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));

        etPerson.setText(i.getStringExtra("person"));
        etCompany.setText(i.getStringExtra("company"));
        etPhone.setText(i.getStringExtra("profile"));
        etAddress1.setText("Address");
        etAddress2.setText("");

    }
}
