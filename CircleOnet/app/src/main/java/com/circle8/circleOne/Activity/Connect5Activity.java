package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Connect5Activity extends AppCompatActivity
{

    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting;

    String level = "0";
    String profile;
    TextView txtCongratulations, txtLink;
    CircleImageView ivImage1;
    CircleImageView level1, level2, level3, level4, level5, level6, level7;
    CircleImageView ivProfile1, ivProfile2, ivProfile3, ivProfile4, ivProfile5, ivProfile6, ivProfile7;
    TextView txtName1, txtName2, txtName3, txtName4, txtName5, txtName6, txtName7;
    RelativeLayout rltLevel1, rltLevel2, rltLevel3, rltLevel4, rltLevel5, rltLevel6, rltLevel7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect5);
        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getStringExtra("profile");
        ivImage1 = (CircleImageView) findViewById(R.id.ivImage1);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        level1 = (CircleImageView) findViewById(R.id.imgLevel1);
        level2 = (CircleImageView) findViewById(R.id.imgLevel2);
        level3 = (CircleImageView) findViewById(R.id.imgLevel3);
        level4 = (CircleImageView) findViewById(R.id.imgLevel4);
        level5 = (CircleImageView) findViewById(R.id.imgLevel5);
        level6 = (CircleImageView) findViewById(R.id.imgLevel6);
        level7 = (CircleImageView) findViewById(R.id.imgLevel7);

        ivProfile1 = (CircleImageView) findViewById(R.id.ivProfile1);
        ivProfile2 = (CircleImageView) findViewById(R.id.ivProfile2);
        ivProfile3 = (CircleImageView) findViewById(R.id.ivProfile3);
        ivProfile4 = (CircleImageView) findViewById(R.id.ivProfile4);
        ivProfile5 = (CircleImageView) findViewById(R.id.ivProfile5);
        ivProfile6 = (CircleImageView) findViewById(R.id.ivProfile6);
        ivProfile7 = (CircleImageView) findViewById(R.id.ivProfile7);

        txtName1 = (TextView) findViewById(R.id.txtName1);
        txtName2 = (TextView) findViewById(R.id.txtName2);
        txtName3 = (TextView) findViewById(R.id.txtName3);
        txtName4 = (TextView) findViewById(R.id.txtName4);
        txtName5 = (TextView) findViewById(R.id.txtName5);
        txtName6 = (TextView) findViewById(R.id.txtName6);
        txtName7 = (TextView) findViewById(R.id.txtName7);

        rltLevel1 = (RelativeLayout) findViewById(R.id.rltLevel1);
        rltLevel2 = (RelativeLayout) findViewById(R.id.rltLevel2);
        rltLevel3 = (RelativeLayout) findViewById(R.id.rltLevel3);
        rltLevel4 = (RelativeLayout) findViewById(R.id.rltLevel4);
        rltLevel5 = (RelativeLayout) findViewById(R.id.rltLevel5);
        rltLevel6 = (RelativeLayout) findViewById(R.id.rltLevel6);
        rltLevel7 = (RelativeLayout) findViewById(R.id.rltLevel7);

        txtLink = (TextView) findViewById(R.id.txtLink);
        txtCongratulations = (TextView) findViewById(R.id.txtCongratulations);

        try
        {
            if (profile.equalsIgnoreCase("") || profile.equalsIgnoreCase("null"))
            {
                ivImage1.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(profile).into(ivImage1);
            }
        }
        catch (Exception e) {
            ivImage1.setImageResource(R.drawable.usr);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent go = new Intent(getApplicationContext(),Connect4Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                startActivity(go);
                finish();*/
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 1);
                startActivity(go);
                finish();
            }
        });

        if (level.equals("0"))
        {
            txtCongratulations.setVisibility(View.GONE);
            txtLink.setText("You are not having any Connection.");
            rltLevel1.setVisibility(View.INVISIBLE);
            rltLevel2.setVisibility(View.INVISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.INVISIBLE);
            txtName2.setVisibility(View.INVISIBLE);
            txtName3.setVisibility(View.INVISIBLE);
            txtName4.setVisibility(View.INVISIBLE);
            txtName5.setVisibility(View.INVISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        if (level.equals("1"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 1st level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.INVISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.INVISIBLE);
            txtName3.setVisibility(View.INVISIBLE);
            txtName4.setVisibility(View.INVISIBLE);
            txtName5.setVisibility(View.INVISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("2"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 2nd level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.INVISIBLE);
            txtName4.setVisibility(View.INVISIBLE);
            txtName5.setVisibility(View.INVISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("3"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 3rd level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.VISIBLE);
            txtName4.setVisibility(View.INVISIBLE);
            txtName5.setVisibility(View.INVISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("4"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 4th level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.VISIBLE);
            txtName4.setVisibility(View.VISIBLE);
            txtName5.setVisibility(View.INVISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("5"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 5th level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.VISIBLE);
            txtName4.setVisibility(View.VISIBLE);
            txtName5.setVisibility(View.VISIBLE);
            txtName6.setVisibility(View.INVISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("6"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 6th level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.VISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.VISIBLE);
            txtName4.setVisibility(View.VISIBLE);
            txtName5.setVisibility(View.VISIBLE);
            txtName6.setVisibility(View.VISIBLE);
            txtName7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("7"))
        {
            txtCongratulations.setVisibility(View.VISIBLE);
            txtLink.setText("You have a 7th level connection.");
            rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.VISIBLE);
            rltLevel7.setVisibility(View.VISIBLE);

            txtName1.setVisibility(View.VISIBLE);
            txtName2.setVisibility(View.VISIBLE);
            txtName3.setVisibility(View.VISIBLE);
            txtName4.setVisibility(View.VISIBLE);
            txtName5.setVisibility(View.VISIBLE);
            txtName6.setVisibility(View.VISIBLE);
            txtName7.setVisibility(View.VISIBLE);
        }


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