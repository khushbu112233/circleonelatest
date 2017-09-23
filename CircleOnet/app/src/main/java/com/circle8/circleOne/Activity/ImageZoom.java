package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.TouchImageView;
import com.squareup.picasso.Picasso;

public class ImageZoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        Intent intent = getIntent();
        String displayProfile = intent.getStringExtra("displayProfile");
        TouchImageView img = (TouchImageView) findViewById(R.id.imgTouch);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
      //  img.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (displayProfile.equals(""))
        {
            img.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+displayProfile).placeholder(R.drawable.usr_1).into(img);
        }


       // img.setImageResource(R.drawable.walkthrough_1);
        img.setMaxZoom(4f);
       // setContentView(img);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
