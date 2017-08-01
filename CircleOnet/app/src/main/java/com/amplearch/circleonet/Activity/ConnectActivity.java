package com.amplearch.circleonet.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.Utility;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ConnectActivity extends AppCompatActivity
{
    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private ImageView ivConnectImg, ivAddRound, ivConnectRound ;
    private TextView tvAdd, tvConnect, tvConnectLine1, tvConnectLine2 ;
    private RelativeLayout rlAdd, rlConnect ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect2);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
//        imgConnecting = (ImageView) findViewById(R.id.fab);

        ivConnectImg = (ImageView)findViewById(R.id.iv_ConnectImg);
        ivAddRound = (ImageView)findViewById(R.id.ivAddRound);
        ivConnectRound = (ImageView)findViewById(R.id.ivConnectRound);

        tvAdd = (TextView)findViewById(R.id.tvAdd);
        tvConnect = (TextView)findViewById(R.id.tvConnect);
        tvConnectLine1 = (TextView)findViewById(R.id.tvConnectLine1);
        tvConnectLine2 = (TextView)findViewById(R.id.tvConnectLine2);

        rlAdd = (RelativeLayout)findViewById(R.id.rlAdd);
        rlConnect = (RelativeLayout)findViewById(R.id.rlConnect);

        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), "+91 9737032082");

                    if (aBoolean == true) {
                        TranslateAnimation slide1 = new TranslateAnimation(0, -170, 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivConnectRound.setImageResource(R.drawable.round_gray);
                                tvConnect.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine2.setTextColor(getResources().getColor(R.color.unselected));
                            }
                        }, 1100);
                        // Second Things
                        ivAddRound.setImageResource(R.drawable.round_blue);
                        tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else {

                    }
                }
            }
        });
        boolean result = Utility.checkContactPermission(ConnectActivity.this);
        if (result) {
            contactExists(getApplicationContext(), "+91 9737032082");
        }
        rlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), "+91 9737032082");

                    if (aBoolean == true) {
                        TranslateAnimation slide1 = new TranslateAnimation(0, 170, 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                            }
                        }, 1100);
                        // Second Things
                        ivConnectRound.setImageResource(R.drawable.round_blue);
                        tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else {
                        TranslateAnimation slide1 = new TranslateAnimation(0, 170, 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                            }
                        }, 1100);
                        // Second Things
                        ivConnectRound.setImageResource(R.drawable.round_blue);
                        tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
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

//        imgConnecting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
//
//                // you pass the position you want the viewpager to show in the extra,
//                // please don't forget to define and initialize the position variable
//                // properly
//
//                startActivity(go);
//                finish();
//            }
//        });

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

    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                Toast.makeText(getApplicationContext(), "Contact Exists", Toast.LENGTH_LONG).show();
                ivAddRound.setImageResource(R.drawable.round_blue);
                tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        Toast.makeText(getApplicationContext(), "Contact doesn't Exists", Toast.LENGTH_LONG).show();
        ivAddRound.setImageResource(R.drawable.round_gray);
        tvAdd.setTextColor(getResources().getColor(R.color.unselected));
        tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
        return false;
    }
}
