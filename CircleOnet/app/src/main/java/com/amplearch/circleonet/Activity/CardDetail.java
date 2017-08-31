package com.amplearch.circleonet.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.StickyScrollView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.async.WriteCallbackNfcAsync;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardDetail extends NfcActivity
{

    ViewPager mViewPager, viewPager1;
    private ArrayList<String> image = new ArrayList<>();;
    private CardSwipe myPager ;
    private ImageView imgCards, imgConnect, imgEvents, imgProfile, imgBack, imgCard, imgMap;
    private static final String TAG = NFCDemo.class.getName();
    private LinearLayout llWebsiteBox, llEmailBox, llMobileBox, llTeleBox, llFaxBox ;

    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    ProgressDialog mProgressDialog;
    DatabaseHelper db ;
    TextView txtName, txtCompany, txtWebsite, txtEmail, txtPH, txtWork, txtMob, txtAddress, txtRemark, txtDesi;
    CircleImageView imgProfileCard;
    String user_id, profile_id;
    StickyScrollView scroll;
    ImageView imgCall, imgSMS, imgMail;
    String recycle_image1, recycle_image2 ;

    String userImg , frontCardImg, backCardImg ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) findViewById(R.id.viewPager1);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCard = (ImageView) findViewById(R.id.imgCard);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgSMS = (ImageView) findViewById(R.id.imgSMS);
        imgMail = (ImageView) findViewById(R.id.imgMail);
        imgMap = (ImageView)findViewById(R.id.ivMap);
        imgProfileCard = (CircleImageView) findViewById(R.id.imgProfileCard);
        db = new DatabaseHelper(getApplicationContext());
        txtName = (TextView) findViewById(R.id.txtName);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPH = (TextView) findViewById(R.id.txtPH);
        txtWork = (TextView) findViewById(R.id.txtWork);
        txtMob = (TextView) findViewById(R.id.txtMob);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtRemark = (TextView) findViewById(R.id.txtRemark);
        txtDesi = (TextView) findViewById(R.id.txtDesi);
        scroll = (StickyScrollView) findViewById(R.id.scroll);

        llWebsiteBox = (LinearLayout)findViewById(R.id.llWebsiteBox);
        llEmailBox = (LinearLayout)findViewById(R.id.llEmailBox);
        llMobileBox = (LinearLayout)findViewById(R.id.llMobileBox);
        llTeleBox = (LinearLayout)findViewById(R.id.llTeleBox);
        llFaxBox = (LinearLayout)findViewById(R.id.llFaxBox);

        Intent intent = getIntent();
        profile_id = intent.getStringExtra("profile_id");
        new CardDetail.HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetUserProfile");

        llWebsiteBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Redirect to Web Browser")
                        .setMessage("Are you sure you want to redirect to Web Browser ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String url = txtWebsite.getText().toString();
                                if (url!=null) {
                                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                                        url = "http://" + url;
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_set_as)
                        .show();
            }
        });

        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().equals(""))
                {

                }
                else
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);
                    builder.setTitle("Mail to "+ txtName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try
                                    {
                                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + txtEmail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }
        });

        imgSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMob.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "You are not having contact to SMS..", Toast.LENGTH_LONG).show();
                }else {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", txtMob.getText().toString());
                    smsIntent.putExtra("sms_body", "");
                    startActivity(smsIntent);
                }
            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtMob.getText().toString().equals("")){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtMob.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                }
                else if (!txtWork.getText().toString().equals("")){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtWork.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                }
                else if (!txtPH.getText().toString().equals("")){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtPH.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                }
            }
        });

        llEmailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (txtEmail.getText().toString().equals(""))
                {

                }
                else
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);
                    builder.setTitle("Mail to "+ txtName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try
                                    {
                                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + txtEmail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }
        });

        llMobileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Call to "+ txtName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+txtMob.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });

        llTeleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Call to "+ txtName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+txtPH.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });

        llFaxBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });


//        Toast.makeText(getApplicationContext(),"Profile_id: "+profile_id,Toast.LENGTH_SHORT).show();
//        final List<NFCModel> modelList = db.getNFCbyTag(tag_id);

       /* try
        {
            if (modelList != null)
            {
                for (NFCModel tag1 : modelList)
                {
                    // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                    //Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                    imgCard.setImageResource(tag1.getCard_front());

                  //  Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                    imgProfileCard.setImageResource(tag1.getUser_image());
                    txtName.setText(tag1.getName());
                    txtCompany.setText(tag1.getCompany());
                    txtWebsite.setText(tag1.getWebsite());
                    txtEmail.setText(tag1.getEmail());
                    txtPH.setText(tag1.getPh_no());
                    txtWork.setText(tag1.getWork_no());
                    txtMob.setText(tag1.getMob_no());
                    txtAddress.setText(tag1.getAddress());
                    txtRemark.setText(tag1.getAddress());
                    txtDesi.setText(tag1.getDesignation());
                    image.add(tag1.getCard_front());
                    image.add(tag1.getCard_back());

                    myPager = new CardSwipe(getApplicationContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(3);
                    //mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(3);
                   // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                    viewPager1.setAdapter(myPager);

                }
            }

        }catch (Exception e){

        }*/


      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                scroll.scrollTo(0, mViewPager.getBottom());
            }
        });*/

        imgBack.setOnClickListener(new View.OnClickListener() {
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

        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager.scrollTo(viewPager1.getScrollX(), viewPager1.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
               // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(viewPager1.getCurrentItem(), false);
                }
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

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Google Map")
                        .setMessage("Are you sure you want to redirect to Google Map ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String map = "http://maps.google.co.in/maps?q=" + txtAddress.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        });
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                        jsonObject.getString("FirstName");
                        jsonObject.getString("LastName");
                        jsonObject.getString("OfficePhone");
                        jsonObject.getString("PrimaryPhone");
                        jsonObject.getString("Emailid");
                        jsonObject.getString("IndustryName");
                        jsonObject.getString("CompanyName");
                        jsonObject.getString("CompanyProfile");
                        jsonObject.getString("Designation");
                        jsonObject.getString("Facebook");
                        jsonObject.getString("Twitter");
                        jsonObject.getString("Google");
         frontCardImg = jsonObject.getString("Card_Front");
          backCardImg = jsonObject.getString("Card_Back");
              userImg = jsonObject.getString("UserPhoto");

                        txtName.setText(jsonObject.getString("FirstName")+" "+jsonObject.getString("LastName"));
                        txtDesi.setText(jsonObject.getString("Designation"));
                        txtCompany.setText(jsonObject.getString("CompanyName"));
                        txtEmail.setText(jsonObject.getString("Emailid"));
                        txtMob.setText(jsonObject.getString("PrimaryPhone"));
                        txtPH.setText(jsonObject.getString("OfficePhone"));
                        txtAddress.setText(jsonObject.getString("Address1") + " " + jsonObject.getString("Address2")
                            + " " + jsonObject.getString("Address3") + " " + jsonObject.getString("Address4")
                            + " " + jsonObject.getString("City")  + " " + jsonObject.getString("State")
                            + " " + jsonObject.getString("Country") + " " + jsonObject.getString("Postalcode"));
                        txtWebsite.setText(jsonObject.getString("Website"));


                        if(userImg.equalsIgnoreCase(""))
                        {
                            imgProfileCard.setImageResource(R.drawable.usr) ;
                        }
                        else
                        {
                            Picasso.with(CardDetail.this).load("http://circle8.asia/App_ImgLib/UserProfile/"+userImg).into(imgProfileCard) ;
                        }

                        if(frontCardImg.equalsIgnoreCase(""))
                        {
                           recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg" ;
                        }
                        else
                        {
                            recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/"+frontCardImg ;
                        }

                        if(backCardImg.equalsIgnoreCase(""))
                        {
                            recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg" ;
                        }
                        else
                        {
                            recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/"+backCardImg ;
                        }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getApplicationContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(3);
                  //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(3);
                 //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    viewPager1.setAdapter(myPager);


                        /*FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));

                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);*/
//                        GetData(getContext());

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("profileid", profile_id );

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent
     *         containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent)
    {
        super.onNewIntent(paramIntent);


            Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
                Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
                //textViewInfo.setText("tag == null");
            }else {
                String tagInfo = tag.toString() + "\n";
                String id = "";
                tagInfo += "\nTag Id: \n";
                byte[] tagId = tag.getId();
                tagInfo += "length = " + tagId.length + "\n";
                for (int i = 0; i < tagId.length; i++) {
                    tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                    // id += Integer.toHexString(tagId[i] & 0xFF) + " ";
                }
                id = bytesToHex(tagId);
               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


              //  Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
               // callData(id);
                for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values())
                {
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                    List<NFCModel> modelList = db.getNFCbyTag(data);

                    try {

                        if (modelList != null){

                            for (NFCModel tag1 : modelList)
                            {
                                // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                               // Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                                imgCard.setImageResource(tag1.getCard_front());

                               // Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                                imgProfileCard.setImageResource(tag1.getUser_image());
                                txtName.setText(tag1.getName());
                                txtCompany.setText(tag1.getCompany());
                                txtWebsite.setText(tag1.getWebsite());
                                txtEmail.setText(tag1.getEmail());
                                txtPH.setText(tag1.getPh_no());
                                txtWork.setText(tag1.getWork_no());
                                txtMob.setText(tag1.getMob_no());
                                txtAddress.setText(tag1.getAddress());
                                txtRemark.setText(tag1.getRemark());
                                txtDesi.setText(tag1.getDesignation());
                                image.add(String.valueOf(tag1.getCard_front()));   // its change from integer to string
                                image.add(String.valueOf(tag1.getCard_back()));    // its change from integer to string
                                myPager = new CardSwipe(getApplicationContext(), image);

                                mViewPager.setClipChildren(false);
                                mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                                mViewPager.setOffscreenPageLimit(3);
                             //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

                                viewPager1.setAdapter(myPager);

                                viewPager1.setClipChildren(false);
                                viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                                viewPager1.setOffscreenPageLimit(3);
                            //    viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                                viewPager1.setAdapter(myPager);

                            }
                        }

                    }catch (Exception e){

                    }
                }
        }
    }

    public void callData(String id){

    }

}
