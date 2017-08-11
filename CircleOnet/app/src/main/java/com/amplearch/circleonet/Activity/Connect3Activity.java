package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.R;

public class Connect3Activity extends AppCompatActivity {

    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting, imgConnecting1;
    TextView txtConnecting;
    String level =  "0";
    int x = 0;
    int profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect3);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgConnecting = (ImageView) findViewById(R.id.imgConnecting);
        imgConnecting1 = (ImageView) findViewById(R.id.imgConnecting1);
        txtConnecting = (TextView) findViewById(R.id.txtConnecting);

        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getIntExtra("profile", 0);
       // Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();
       /* Handler handler = new Handler();

        for (int i = 100; i <= 60000; i=i+100) {
            final int finalI = i;
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if(finalI %500 == 0){
                        txtConnecting.setText("Connecting.");
                    }else if(finalI %400 == 0){
                        txtConnecting.setText("Connecting..");
                    }else if(finalI %300 == 0){
                        txtConnecting.setText("Connecting...");
                    }
                }
            }, i);
        }*/


        Handler h=new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
//change your text here
                if (x == 0) {
                    txtConnecting.setText("Connecting.");
                    x += 1;
                } else if (x == 1){
                    txtConnecting.setText("Connecting..");
                    x += 1;
                }
            else if (x == 2) {
                    txtConnecting.setText("Connecting...");
                    x = 0;
                }
            }
        }, 100);

        imgConnecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go = new Intent(getApplicationContext(),Connect4Activity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                startActivity(go);
                finish();
            }
        });
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        imgConnecting.startAnimation(anim);

        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        imgConnecting1.startAnimation(anim1);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),ConnectActivity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);
                finish();
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);

                startActivity(go);
                finish();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);

                startActivity(go);
                finish();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });

    }
}
