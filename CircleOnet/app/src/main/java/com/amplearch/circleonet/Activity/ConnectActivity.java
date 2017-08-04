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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
    CircleImageView ivProfileImage;

    private ImageView ivConnectImg, ivAddRound, ivConnectRound ;
    private TextView tvAdd, tvConnect, tvConnectLine1, tvConnectLine2, txtWeb, txtMail, txtNum, txtMob ;
    private RelativeLayout rlAdd, rlConnect ;
    private String tag_id;
    DatabaseHelper db;
    TextView tvPersonName, tvPersonDesignation, tvCompanyName;
    LinearLayout lnrWeb, lnrmail, lnrnum, lnrmob;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect2);

        db = new DatabaseHelper(getApplicationContext());

        txtWeb = (TextView)findViewById(R.id.txtWeb);
        txtMail = (TextView)findViewById(R.id.txtMail);
        txtNum = (TextView)findViewById(R.id.txtNum);
        txtMob = (TextView)findViewById(R.id.txtMob);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
//        imgConnecting = (ImageView) findViewById(R.id.fab);
        ivProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);
        ivConnectImg = (ImageView)findViewById(R.id.iv_ConnectImg);
        ivAddRound = (ImageView)findViewById(R.id.ivAddRound);
        ivConnectRound = (ImageView)findViewById(R.id.ivConnectRound);

        tvAdd = (TextView)findViewById(R.id.tvAdd);
        tvConnect = (TextView)findViewById(R.id.tvConnect);
        tvConnectLine1 = (TextView)findViewById(R.id.tvConnectLine1);
        tvConnectLine2 = (TextView)findViewById(R.id.tvConnectLine2);


        tvPersonName = (TextView)findViewById(R.id.tvPersonName);
        tvPersonDesignation = (TextView)findViewById(R.id.tvPersonDesignation);
        tvCompanyName = (TextView)findViewById(R.id.tvCompanyName);

        rlAdd = (RelativeLayout)findViewById(R.id.rlAdd);
        rlConnect = (RelativeLayout)findViewById(R.id.rlConnect);


        Intent intent = getIntent();
        tag_id = intent.getStringExtra("tag_id");
        final List<NFCModel> modelList = db.getNFCbyTag(tag_id);
        try {

            if (modelList != null){

                for (NFCModel tag1 : modelList) {
                    // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                    //Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                   // imgCard.setImageResource(tag1.getCard_front());

                    //  Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                    ivProfileImage.setImageResource(tag1.getUser_image());
                    tvPersonName.setText(tag1.getName());
                    tvCompanyName.setText(tag1.getCompany());
                    txtWeb.setText(tag1.getWebsite());
                    txtMail.setText(tag1.getEmail());
                    txtNum.setText(tag1.getPh_no());
                   // txtWork.setText(tag1.getWork_no());
                    txtMob.setText(tag1.getMob_no());
                    tvPersonDesignation.setText(tag1.getDesignation());

                }
            }

        }catch (Exception e){

        }


        ivConnectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), txtMob.getText().toString());

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
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                startActivity(go);
                                finish();
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
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                startActivity(go);
                                finish();
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
