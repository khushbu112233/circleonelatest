package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.circle8.circleOne.R;

public class CardVerificationActivity extends AppCompatActivity implements View.OnClickListener
{
    ImageView ivBack, ivAddCard ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_verification);

        ivBack = (ImageView)findViewById(R.id.imgBack);
        ivAddCard = (ImageView)findViewById(R.id.ivAddCard);

    }

    @Override
    public void onClick(View v)
    {
        if ( v == ivBack)
        {
            finish();
        }
        if ( v == ivAddCard)
        {

        }
    }
}
