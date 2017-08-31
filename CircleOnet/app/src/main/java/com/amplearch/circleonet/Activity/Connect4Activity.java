package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.R;
import com.squareup.picasso.Picasso;

public class Connect4Activity extends AppCompatActivity {

    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting;
    TextView txtAsk, txtLink;
    String level = "0";
    ImageView level1, level2, level3, level4, level5, level6, ivImage1;
    String profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect4);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtLink = (TextView) findViewById(R.id.txtLink);
        txtAsk = (TextView) findViewById(R.id.txtAsk);
        level1 = (ImageView) findViewById(R.id.imgLevel1);
        level2 = (ImageView) findViewById(R.id.imgLevel2);
        level3 = (ImageView) findViewById(R.id.imgLevel3);
        level4 = (ImageView) findViewById(R.id.imgLevel4);
        level5 = (ImageView) findViewById(R.id.imgLevel5);
        level6 = (ImageView) findViewById(R.id.imgLevel6);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);

        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getStringExtra("profile");
        Picasso.with(getApplicationContext()).load(profile).placeholder(R.drawable.usr).into(ivImage1);
        if (level.equals("0")){
            txtLink.setText("You are not having any Connection.");
            level1.setVisibility(View.GONE);
            level2.setVisibility(View.GONE);
            level3.setVisibility(View.GONE);
            level4.setVisibility(View.GONE);
            level5.setVisibility(View.GONE);
            level6.setVisibility(View.GONE);
        }
        if (level.equals("1")){
            txtLink.setText("You have a 1st level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.GONE);
            level3.setVisibility(View.GONE);
            level4.setVisibility(View.GONE);
            level5.setVisibility(View.GONE);
            level6.setVisibility(View.GONE);
        }
        else if (level.equals("2")){
            txtLink.setText("You have a 2nd level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.GONE);
            level4.setVisibility(View.GONE);
            level5.setVisibility(View.GONE);
            level6.setVisibility(View.GONE);
        }
        else if (level.equals("3")){
            txtLink.setText("You have a 3rd level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.VISIBLE);
            level4.setVisibility(View.GONE);
            level5.setVisibility(View.GONE);
            level6.setVisibility(View.GONE);
        }
        else if (level.equals("4")){
            txtLink.setText("You have a 4th level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.VISIBLE);
            level4.setVisibility(View.VISIBLE);
            level5.setVisibility(View.GONE);
            level6.setVisibility(View.GONE);
        }
        else if (level.equals("5")){
            txtLink.setText("You have a 5th level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.VISIBLE);
            level4.setVisibility(View.VISIBLE);
            level5.setVisibility(View.VISIBLE);
            level6.setVisibility(View.GONE);
        }
        else if (level.equals("6")){
            txtLink.setText("You have a 6th level connection.");
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.VISIBLE);
            level4.setVisibility(View.VISIBLE);
            level5.setVisibility(View.VISIBLE);
            level6.setVisibility(View.VISIBLE);
        }
        txtAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),Connect5Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
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
