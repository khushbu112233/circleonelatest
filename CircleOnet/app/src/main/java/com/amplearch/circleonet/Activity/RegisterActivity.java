package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amplearch.circleonet.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etUserName, etFirstName, etLastName, etPassword, etConfirmPass, etPhone, etEmail, etDOB, etAddress;
    LinearLayout lnrRegister;

    String UrlRegister = "http://circle8.asia:8081/Onet.svc/Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lnrRegister = (LinearLayout) findViewById(R.id.lnrBottomReg);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etAddress = (EditText) findViewById(R.id.etAddress);

        lnrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
