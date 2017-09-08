package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amplearch.circleonet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class WriteTestimonialActivity extends AppCompatActivity implements View.OnClickListener
{
    private CircleImageView imgProfile ;
    private TextView tvUserName, tvSend, tvCancel ;
    private EditText etTestimonial1, etTestimonial2, etTestimonial3,
            etTestimonial4, etTestimonial5, etTestimonial6 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_testimonial);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);

        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvSend = (TextView)findViewById(R.id.tvSend);
        tvCancel = (TextView)findViewById(R.id.tvCancel);

        etTestimonial1 = (EditText)findViewById(R.id.etTestimonial1);
        etTestimonial2 = (EditText)findViewById(R.id.etTestimonial2);
        etTestimonial3 = (EditText)findViewById(R.id.etTestimonial3);
        etTestimonial4 = (EditText)findViewById(R.id.etTestimonial4);
        etTestimonial5 = (EditText)findViewById(R.id.etTestimonial5);
        etTestimonial6 = (EditText)findViewById(R.id.etTestimonial6);

        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if( v == tvSend)
        {

        }
        if( v == tvCancel)
        {

        }
    }
}
