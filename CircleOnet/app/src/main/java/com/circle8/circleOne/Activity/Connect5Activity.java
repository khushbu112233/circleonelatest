package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityConnect5Binding;
import com.squareup.picasso.Picasso;

public class Connect5Activity extends AppCompatActivity implements View.OnClickListener
{

    String level = "0";
    String profile,profileName;
    String connectLevel = "";
    String userName1 = "", userName2 = "", userName3 = "", userName4 = "", userName5 = "", userName6 = "", userName7 = "";
    String userPhoto1 = "", userPhoto2 = "", userPhoto3 = "", userPhoto4 = "", userPhoto5 = "", userPhoto6 = "", userPhoto7 = "" ;
    String userProfileId1 = "", userProfileId2 = "", userProfileId3 = "", userProfileId4 = "", userProfileId5 = "",
            userProfileId6 = "", userProfileId7 = "";
    ActivityConnect5Binding activityConnect5Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityConnect5Binding = DataBindingUtil.setContentView(this,R.layout.activity_connect5);
        Utility.freeMemory();
        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        profile = intent.getStringExtra("profile");
        profileName = intent.getStringExtra("profileName");
        connectLevel = intent.getStringExtra("connectLevel");
        userName1 = intent.getStringExtra("userName1");
        userPhoto1 = intent.getStringExtra("userPhoto1");
        userProfileId1 = intent.getStringExtra("userProfileId1");
        userName2 = intent.getStringExtra("userName2");
        userPhoto2 = intent.getStringExtra("userPhoto2");
        userProfileId2 = intent.getStringExtra("userProfileId2");
        userName3 = intent.getStringExtra("userName3");
        userPhoto3 = intent.getStringExtra("userPhoto3");
        userProfileId3 = intent.getStringExtra("userProfileId3");
        userName4 = intent.getStringExtra("userName4");
        userPhoto4 = intent.getStringExtra("userPhoto4");
        userProfileId4 = intent.getStringExtra("userProfileId4");
        userName5 = intent.getStringExtra("userName5");
        userPhoto5 = intent.getStringExtra("userPhoto5");
        userProfileId5 = intent.getStringExtra("userProfileId5");
        userName6 = intent.getStringExtra("userName6");
        userPhoto6 = intent.getStringExtra("userPhoto6");
        userProfileId6 = intent.getStringExtra("userProfileId6");
        userName7 = intent.getStringExtra("userName7");
        userPhoto7 = intent.getStringExtra("userPhoto7");
        userProfileId7 = intent.getStringExtra("userProfileId7");


        activityConnect5Binding.txtProfileName.setText(profileName);

        try
        {
            Utility.freeMemory();
            if (profile.equalsIgnoreCase("") || profile.equalsIgnoreCase("null"))
            {
                activityConnect5Binding.ivImage1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(profile).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivImage1);
            }
        }
        catch (Exception e) {
            activityConnect5Binding.ivImage1.setImageResource(R.drawable.usr_white);
        }

        activityConnect5Binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();

               /* Intent go = new Intent(getApplicationContext(),Connect4Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                startActivity(go);
                finish();*/
               /* Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 1);
                startActivity(go);*/

                Intent go = new Intent(getApplicationContext(), Connect4Activity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                go.putExtra("profileName",profileName);
                go.putExtra("connectLevel", connectLevel);
                go.putExtra("userName1",userName1);
                go.putExtra("userPhoto1",userPhoto1);
                go.putExtra("userProfileId1",userProfileId1);
                go.putExtra("userName2",userName2);
                go.putExtra("userPhoto2",userPhoto2);
                go.putExtra("userProfileId2",userProfileId2);
                go.putExtra("userName3",userName3);
                go.putExtra("userPhoto3",userPhoto3);
                go.putExtra("userProfileId3",userProfileId3);
                go.putExtra("userName4",userName4);
                go.putExtra("userPhoto4",userPhoto4);
                go.putExtra("userProfileId4",userProfileId4);
                go.putExtra("userName5",userName5);
                go.putExtra("userPhoto5",userPhoto5);
                go.putExtra("userProfileId5",userProfileId5);
                go.putExtra("userName6",userName6);
                go.putExtra("userPhoto6",userPhoto6);
                go.putExtra("userProfileId6",userProfileId6);
                go.putExtra("userName7",userName7);
                go.putExtra("userPhoto7",userPhoto7);
                go.putExtra("userProfileId7",userProfileId7);
                startActivity(go);
                finish();
            }
        });

        if (level.equals("0"))
        {
            Utility.freeMemory();
            //  txtCongratulations.setVisibility(View.GONE);
            activityConnect5Binding.txtLink.setText("You have no established connections");
            /*rltLevel1.setVisibility(View.INVISIBLE);
            rltLevel2.setVisibility(View.INVISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.ivProfile1.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile2.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        if (level.equals("1"))
        {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 1 connection away");
           /* rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.INVISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            if (userPhoto1.equals(""))
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).placeholder(R.drawable.usr2).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            activityConnect5Binding.ivProfile2.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("2"))
        {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 2 connections away");
            /*rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.INVISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            activityConnect5Binding.ivProfile3.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("3"))
        {
            Utility.freeMemory();
            //txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 3 connections away");
           /* rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.INVISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);
            activityConnect5Binding.txtName3.setText(userName3);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            if (userPhoto3.equals("")  )
            {
                activityConnect5Binding.ivProfile3.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto3)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile3);
            }

            activityConnect5Binding.ivProfile4.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("4"))
        {
            // txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 4 connections away");
            /*rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.INVISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);
            activityConnect5Binding.txtName3.setText(userName3);
            activityConnect5Binding.txtName4.setText(userName4);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            if (userPhoto3.equals("")  )
            {
                activityConnect5Binding.ivProfile3.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto3)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile3);
            }

            if (userPhoto4.equals("")  )
            {
                activityConnect5Binding.ivProfile4.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto4)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile4);
            }

            activityConnect5Binding.ivProfile5.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("5"))
        {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 5 connections away");
            /*rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.INVISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);
            activityConnect5Binding.txtName3.setText(userName3);
            activityConnect5Binding.txtName4.setText(userName4);
            activityConnect5Binding.txtName5.setText(userName5);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            if (userPhoto3.equals("")  )
            {
                activityConnect5Binding.ivProfile3.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto3)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile3);
            }

            if (userPhoto4.equals("")  )
            {
                activityConnect5Binding.ivProfile4.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto4)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile4);
            }

            if (userPhoto5.equals("")  )
            {
                activityConnect5Binding.ivProfile5.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto5)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile5);
            }

            activityConnect5Binding.ivProfile6.setVisibility(View.INVISIBLE);
            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("6"))
        {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 6 connections away");
            /*rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.VISIBLE);
            rltLevel7.setVisibility(View.INVISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding. txtName3.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName4.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.INVISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);
            activityConnect5Binding.txtName3.setText(userName3);
            activityConnect5Binding.txtName4.setText(userName4);
            activityConnect5Binding.txtName5.setText(userName5);
            activityConnect5Binding.txtName6.setText(userName6);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            if (userPhoto3.equals("")  )
            {
                activityConnect5Binding.ivProfile3.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto3)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile3);
            }

            if (userPhoto4.equals("")  )
            {
                activityConnect5Binding.ivProfile4.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto4)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile4);
            }

            if (userPhoto5.equals("")  )
            {
                activityConnect5Binding.ivProfile5.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto5)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile5);
            }

            if (userPhoto6.equals("")  )
            {
                activityConnect5Binding.ivProfile6.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto6)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile6);
            }

            activityConnect5Binding.ivProfile7.setVisibility(View.INVISIBLE);
        }
        else if (level.equals("7"))
        {
            //  txtCongratulations.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtLink.setText("you are now 7 connections away");
            /*rltLevel1.setVisibility(View.VISIBLE);
            rltLevel2.setVisibility(View.VISIBLE);
            rltLevel3.setVisibility(View.VISIBLE);
            rltLevel4.setVisibility(View.VISIBLE);
            rltLevel5.setVisibility(View.VISIBLE);
            rltLevel6.setVisibility(View.VISIBLE);
            rltLevel7.setVisibility(View.VISIBLE);*/

            activityConnect5Binding.txtName1.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName2.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName3.setVisibility(View.VISIBLE);
            activityConnect5Binding. txtName4.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName5.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName6.setVisibility(View.VISIBLE);
            activityConnect5Binding.txtName7.setVisibility(View.VISIBLE);

            activityConnect5Binding.imgLevel1.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel2.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel3.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel4.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel5.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel6.setVisibility(View.VISIBLE);
            activityConnect5Binding.imgLevel7.setVisibility(View.VISIBLE);

            activityConnect5Binding.txtName1.setText(userName1);
            activityConnect5Binding.txtName2.setText(userName2);
            activityConnect5Binding.txtName3.setText(userName3);
            activityConnect5Binding.txtName4.setText(userName4);
            activityConnect5Binding.txtName5.setText(userName5);
            activityConnect5Binding.txtName6.setText(userName6);
            activityConnect5Binding.txtName7.setText(userName7);

            if (userPhoto1.equals("")  )
            {
                activityConnect5Binding.ivProfile1.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto1).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect5Binding.ivProfile1);
            }

            if (userPhoto2.equals("")  )
            {
                activityConnect5Binding.ivProfile2.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto2)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile2);
            }

            if (userPhoto3.equals("")  )
            {
                activityConnect5Binding.ivProfile3.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto3)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile3);
            }

            if (userPhoto4.equals("")  )
            {
                activityConnect5Binding.ivProfile4.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto4)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile4);
            }

            if (userPhoto5.equals("")  )
            {
                activityConnect5Binding.ivProfile5.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto5)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile5);
            }

            if (userPhoto6.equals("")  )
            {
                activityConnect5Binding.ivProfile6.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto6)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile6);
            }

            if (userPhoto7.equals("")  )
            {
                activityConnect5Binding.ivProfile7.setImageResource(R.drawable.usr_white);
            }
            else
            {
                Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+userPhoto7)
                        .resize(300,300).onlyScaleDown().skipMemoryCache()
                        .into(activityConnect5Binding.ivProfile7);
            }
        }

        activityConnect5Binding.imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                CardsActivity.mViewPager.setCurrentItem(0);
                ConnectActivity.kill();
                Connect3Activity.kill();
                Connect4Activity.kill();

                finish();
            }
        });

        activityConnect5Binding.imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                CardsActivity.mViewPager.setCurrentItem(1);
                ConnectActivity.kill();
                Connect3Activity.kill();
                Connect4Activity.kill();

                finish();
            }
        });

        activityConnect5Binding.imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                CardsActivity.mViewPager.setCurrentItem(2);
                ConnectActivity.kill();
                Connect3Activity.kill();
                Connect4Activity.kill();

                finish();
            }
        });

        activityConnect5Binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                CardsActivity.mViewPager.setCurrentItem(3);
                ConnectActivity.kill();
                Connect3Activity.kill();
                Connect4Activity.kill();

                finish();
            }
        });

        activityConnect5Binding.ivProfile1.setOnClickListener(this);
        activityConnect5Binding.ivProfile2.setOnClickListener(this);
        activityConnect5Binding. ivProfile3.setOnClickListener(this);
        activityConnect5Binding.ivProfile4.setOnClickListener(this);
        activityConnect5Binding.ivProfile5.setOnClickListener(this);
        activityConnect5Binding.ivProfile6.setOnClickListener(this);
        activityConnect5Binding.ivProfile7.setOnClickListener(this);

        activityConnect5Binding.txtName1.setOnClickListener(this);
        activityConnect5Binding.txtName2.setOnClickListener(this);
        activityConnect5Binding.txtName3.setOnClickListener(this);
        activityConnect5Binding.txtName4.setOnClickListener(this);
        activityConnect5Binding.txtName5.setOnClickListener(this);
        activityConnect5Binding.txtName6.setOnClickListener(this);
        activityConnect5Binding.txtName7.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

    @Override
    public void onBackPressed()
    {
        Intent go = new Intent(getApplicationContext(), Connect4Activity.class);
        go.putExtra("level", level);
        go.putExtra("profile", profile);
        go.putExtra("profileName",profileName);
        go.putExtra("connectLevel", connectLevel);
        go.putExtra("userName1",userName1);
        go.putExtra("userPhoto1",userPhoto1);
        go.putExtra("userProfileId1",userProfileId1);
        go.putExtra("userName2",userName2);
        go.putExtra("userPhoto2",userPhoto2);
        go.putExtra("userProfileId2",userProfileId2);
        go.putExtra("userName3",userName3);
        go.putExtra("userPhoto3",userPhoto3);
        go.putExtra("userProfileId3",userProfileId3);
        go.putExtra("userName4",userName4);
        go.putExtra("userPhoto4",userPhoto4);
        go.putExtra("userProfileId4",userProfileId4);
        go.putExtra("userName5",userName5);
        go.putExtra("userPhoto5",userPhoto5);
        go.putExtra("userProfileId5",userProfileId5);
        go.putExtra("userName6",userName6);
        go.putExtra("userPhoto6",userPhoto6);
        go.putExtra("userProfileId6",userProfileId6);
        go.putExtra("userName7",userName7);
        go.putExtra("userPhoto7",userPhoto7);
        go.putExtra("userProfileId7",userProfileId7);
        startActivity(go);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        if ( v == activityConnect5Binding.ivProfile1)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId1);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile2)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId2);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile3)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId3);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile4)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId4);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile5)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId5);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile6)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId6);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
        if ( v == activityConnect5Binding.ivProfile7)
        {
            Utility.freeMemory();
            Intent in = new Intent(Connect5Activity.this, CardDetail.class);
            in.putExtra("profile_id", userProfileId7);
            in.putExtra("DateInitiated", "");
            in.putExtra("lat", "");
            in.putExtra("long", "");
            startActivity(in);
            finish();
        }
    }

}