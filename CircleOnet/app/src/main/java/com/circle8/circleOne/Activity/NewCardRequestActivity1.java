package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CardSwipeBitmap;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.StickyScrollView;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.bitmap;
import static com.circle8.circleOne.Activity.EditProfileActivity.BitMapToString;

public class NewCardRequestActivity1 extends AppCompatActivity
{
    private CircleImageView imgProfile;
    private ImageView ivSubmit;
    private TextView tvPerson, tvDesignation, tvCompany, tvProfile;
    private EditText etPerson, etCompany, etPhone, etAddress1, etAddress2;

    private CardSwipe myPager;
    CardSwipeBitmap swipeBitmap;
    private ArrayList<String> swipe_image = new ArrayList<>();
    private ArrayList<Bitmap> swipe_imageBmp = new ArrayList<>();
    String recycle_image1, recycle_image2;
    ViewPager mViewPager1, mViewPager2;
    private String image;
    private ImageView ivLaserCard, ivNormalCard ;
    TextView cardNumberField, monthField, yearField, cvcField;
    Stripe stripe;
    Card card;
    Token tok;
    AlertDialog alertDialog;
    ImageView imgBack;
    public static String card_front = "", card_back = "", Type, Description, Cost, PhysicalCardLaserId, PhysicalCardNormalId, type;
    TextView txtLaserCost, txtLaserDesc, txtNormalCost, txtNormalDesc;
    String PhysicalCardTypeID = "";
    LoginSession session;
    private String profileId, userID;
    public static Bitmap cardFrontBmp, cardBackBmp;
    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    private String final_ImgBase64Front, final_ImgBase64Back;

    private String laserPrintCost, normalPrintCost ;

    String numberOnCard, nameOnCard, exYearOnCard, exMonthOnCard, cvvOnCard, mobileNoOnCard, strToken ;
    int amount;
    private String email;
    String Price;

    /*for stripe payment*/
    CoordinatorLayout main_contains;
    ImageView ivTransImg ;
    RelativeLayout rlStripeScreen ;
    EditText etCardNumber, etCardHolderName, etExMonth, etExYear, etSecurityCode, etMobileNumber ;
    TextView tvPay, tvCancel, tvAmount ;
    StickyScrollView scrollView ;
    boolean lay_Enable = false ;
    boolean open_Stripe = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        userID = user.get(LoginSession.KEY_USERID);
        email = user.get(LoginSession.KEY_EMAIL);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        txtLaserCost = (TextView) findViewById(R.id.txtLaserCost);
        txtLaserDesc = (TextView) findViewById(R.id.txtLaserDesc);
        txtNormalCost = (TextView) findViewById(R.id.txtNormalCost);
        txtNormalDesc = (TextView) findViewById(R.id.txtNormalDesc);
        ivSubmit = (ImageView) findViewById(R.id.ivSubmit);
        tvPerson = (TextView) findViewById(R.id.tvPersonName);
        tvDesignation = (TextView) findViewById(R.id.tvDesignation);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        etPerson = (EditText) findViewById(R.id.etPerson);
        etCompany = (EditText) findViewById(R.id.etCompany);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etAddress1 = (EditText) findViewById(R.id.etAddress1);
        etAddress2 = (EditText) findViewById(R.id.etAddress2);

        ivLaserCard = (ImageView)findViewById(R.id.ivLaserCard);
        ivNormalCard = (ImageView)findViewById(R.id.ivNormalCard);

        mViewPager1 = (ViewPager) findViewById(R.id.viewPager);
        mViewPager2 = (ViewPager) findViewById(R.id.viewPager1);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        /*for stripe payment screen*/
        main_contains = (CoordinatorLayout)findViewById(R.id.main_content);
        ivTransImg = (ImageView)findViewById(R.id.ivTransImg);
        rlStripeScreen = (RelativeLayout)findViewById(R.id.rlStripeScreen);
        etCardNumber = (EditText)findViewById(R.id.etCardNumber);
        etCardHolderName = (EditText)findViewById(R.id.etCardHolderName);
        etExMonth = (EditText)findViewById(R.id.etExMonth);
        etExYear = (EditText)findViewById(R.id.etExYear);
        etSecurityCode = (EditText)findViewById(R.id.etSecurityCode);
        etMobileNumber = (EditText)findViewById(R.id.etMobileNumber);
        tvPay = (TextView)findViewById(R.id.tvPay);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        tvAmount = (TextView)findViewById(R.id.tvAmount);
        scrollView = (StickyScrollView)findViewById(R.id.scroll);

        Intent i = getIntent();
        image = i.getStringExtra("image");
        type = i.getStringExtra("type");
        profileId = i.getStringExtra("profileID");
        if (type.equals("string")) {
            card_back = i.getStringExtra("card_back");
            card_front = i.getStringExtra("card_front");
            recycle_image1 = Utility.BASE_IMAGE_URL+"Cards/"+card_front;
            recycle_image2 = Utility.BASE_IMAGE_URL+"Cards/"+card_back;
            swipe_image.add(recycle_image1);
            swipe_image.add(recycle_image2);
            myPager = new CardSwipe(getApplicationContext(), swipe_image);

            mViewPager1.setClipChildren(false);
            mViewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
            mViewPager1.setOffscreenPageLimit(1);
            mViewPager1.setAdapter(myPager);

            mViewPager2.setClipChildren(false);
            mViewPager2.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
            mViewPager2.setOffscreenPageLimit(1);
            mViewPager2.setAdapter(myPager);
        }
        else {
          //  cardBackBmp = (Bitmap) i.getParcelableExtra("card_back");
           // cardFrontBmp = (Bitmap) i.getParcelableExtra("card_front");

            swipe_imageBmp.add(cardFrontBmp);
            swipe_imageBmp.add(cardBackBmp);
            swipeBitmap = new CardSwipeBitmap(getApplicationContext(), swipe_imageBmp);

            mViewPager1.setClipChildren(false);
            mViewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
            mViewPager1.setOffscreenPageLimit(1);
            mViewPager1.setAdapter(swipeBitmap);

            mViewPager2.setClipChildren(false);
            mViewPager2.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
            mViewPager2.setOffscreenPageLimit(1);
            mViewPager2.setAdapter(swipeBitmap);
        }

        new HttpAsyncTask().execute(Utility.BASE_URL+"Physical_Card/Get_Type");


        if (image.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_white1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/" + image).resize(300,300).onlyScaleDown().skipMemoryCache().into(imgProfile);
        }
        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));

        etPerson.setText(i.getStringExtra("person"));
        etCompany.setText(i.getStringExtra("company"));
        etPhone.setText(i.getStringExtra("phone"));
        etAddress1.setText("");
        etAddress2.setText("");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (PhysicalCardTypeID.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please select card type", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (card_back.equals("") && card_front.equals(""))
                    {
                        try
                        {
                            final_ImgBase64Back = BitMapToString(cardBackBmp);
                            new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL + "ImgUpload");
                        } catch (Exception e) {
                            final_ImgBase64Back = "";
                            card_back = "";
                        }

                        try
                        {
                            final_ImgBase64Front = BitMapToString(cardFrontBmp);
                            new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL + "ImgUpload");
                        } catch (Exception e) {
                            final_ImgBase64Front = "";
                            card_back = "";
                        }
                    }

                    if (amount == 0)
                    {
                        new HttpAsyncRequestTask().execute(Utility.BASE_URL + "Physical_Card/Order");
                    }
                    else
                    {
                        try
                        {
                            stripe = new Stripe("pk_test_6fZCC6Gu2kwYLUQxJhGte65l");
                        }
                        catch (AuthenticationException e) {
                            e.printStackTrace();
                        }

                        for ( int i = 0; i < main_contains.getChildCount();  i++ )
                        {
                            View view = main_contains.getChildAt(i);
                            view.setFilterTouchesWhenObscured(false);
                            view.setClickable(false);
                            view.setEnabled(false); // Or whatever you want to do with the view.
                        }

                        ivTransImg.setVisibility(View.VISIBLE);
                        rlStripeScreen.setVisibility(View.VISIBLE);

                        etPerson.setEnabled(false);
                        etCompany.setEnabled(false);
                        etPhone.setEnabled(false);
                        etAddress1.setEnabled(false);
                        etAddress2.setEnabled(false);
                        ivLaserCard.setEnabled(false);
                        ivNormalCard.setEnabled(false);
                        ivSubmit.setEnabled(false);
/*
                        if (lay_Enable)
                        {
                            ivTransImg.setVisibility(View.VISIBLE);
                            rlStripeScreen.setVisibility(View.VISIBLE);
                            return ;
                        }
*/

                           /* alertDialog = new AlertDialog.Builder(NewCardRequestActivity1.this).create();
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.activity_stripe_1, null);
                            cardNumberField = (TextView) dialogView.findViewById(R.id.cardNumber);
                            monthField = (TextView) dialogView.findViewById(R.id.month);
                            yearField = (TextView) dialogView.findViewById(R.id.year);
                            cvcField = (TextView) dialogView.findViewById(R.id.cvc);
                            alertDialog.setView(dialogView);
                            alertDialog.show();*/

                        /*new payment mode alert-dialog*/
                        /*alertDialog = new AlertDialog.Builder(NewCardRequestActivity1.this).create();
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.stripe_payment_screen1, null);

                        LinearLayout llCardValues = (LinearLayout) dialogView.findViewById(R.id.llCardValues);
                        LinearLayout llPackageDetails = (LinearLayout) dialogView.findViewById(R.id.llPackageDetails);

                        final EditText etCardNumber = (EditText) dialogView.findViewById(R.id.etCardNumber);
                        final EditText etCardHolderName = (EditText) dialogView.findViewById(R.id.etCardHolderName);
                        final EditText etExMonth = (EditText) dialogView.findViewById(R.id.etExMonth);
                        final EditText etExYear = (EditText) dialogView.findViewById(R.id.etExYear);
                        final EditText etSecurityCode = (EditText) dialogView.findViewById(R.id.etSecurityCode);
                        final EditText etMobileNumber = (EditText) dialogView.findViewById(R.id.etMobileNumber);
                        TextView tvPay = (TextView) dialogView.findViewById(R.id.tvPay);
                        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);

                        etExMonth.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                int month = 0;
                                if (s.toString().equals("")){

                                }else {
                                    try {
                                        month = Integer.parseInt(s.toString());
                                    }catch (Exception e){
                                        etExMonth.setText("");
                                    }
                                    if (month > 12){
                                        Toast.makeText(getApplicationContext(), "selected month is not proper", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        tvPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                numberOnCard = etCardNumber.getText().toString();
                                nameOnCard = etCardHolderName.getText().toString();
                                exYearOnCard = etExYear.getText().toString();
                                exMonthOnCard = etExMonth.getText().toString();
                                cvvOnCard = etSecurityCode.getText().toString();
                                mobileNoOnCard = etMobileNumber.getText().toString();

                                if (numberOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter Card No.", Toast.LENGTH_SHORT).show();
                                } else if (nameOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter Holder Name", Toast.LENGTH_SHORT).show();
                                } else if (exMonthOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter Expiry Month", Toast.LENGTH_SHORT).show();
                                } else if (exYearOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter Expiry Year", Toast.LENGTH_SHORT).show();
                                } else if (cvvOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter CVV No.", Toast.LENGTH_SHORT).show();
                                } else if (mobileNoOnCard.isEmpty()) {
                                    Toast.makeText(NewCardRequestActivity1.this, "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                                } else {
                                    cardPayment();
                                    alertDialog.dismiss();
                                }
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.setView(dialogView);
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
                        alertDialog.show();*/
                    }
                }
            }
        });

        etExMonth.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {     }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int month = 0;
                if (s.toString().equals("")) {  }
                else
                {
                    try { month = Integer.parseInt(s.toString()); }
                    catch (Exception e){ etExMonth.setText(""); }

                    if (month > 12)
                    {
                        Toast.makeText(getApplicationContext(), "selected month is not proper", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {     }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                numberOnCard = etCardNumber.getText().toString();
                nameOnCard = etCardHolderName.getText().toString();
                exYearOnCard = etExYear.getText().toString();
                exMonthOnCard = etExMonth.getText().toString();
                cvvOnCard = etSecurityCode.getText().toString();
                mobileNoOnCard = etMobileNumber.getText().toString();

                if (numberOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter Card No.", Toast.LENGTH_SHORT).show();
                } else if (nameOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter Holder Name", Toast.LENGTH_SHORT).show();
                } else if (exMonthOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter Expiry Month", Toast.LENGTH_SHORT).show();
                } else if (exYearOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter Expiry Year", Toast.LENGTH_SHORT).show();
                } else if (cvvOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter CVV No.", Toast.LENGTH_SHORT).show();
                } else if (mobileNoOnCard.isEmpty()) {
                    Toast.makeText(NewCardRequestActivity1.this, "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    cardPayment();
                    ivTransImg.setVisibility(View.GONE);
                    rlStripeScreen.setVisibility(View.GONE);

                    lay_Enable = true ;

                    etPerson.setEnabled(true);
                    etCompany.setEnabled(true);
                    etPhone.setEnabled(true);
                    etAddress1.setEnabled(true);
                    etAddress2.setEnabled(true);
                    ivLaserCard.setEnabled(true);
                    ivNormalCard.setEnabled(true);
                    ivSubmit.setEnabled(true);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ivTransImg.setVisibility(View.GONE);
                rlStripeScreen.setVisibility(View.GONE);

                lay_Enable = true ;

                etPerson.setEnabled(true);
                etCompany.setEnabled(true);
                etPhone.setEnabled(true);
                etAddress1.setEnabled(true);
                etAddress2.setEnabled(true);
                ivLaserCard.setEnabled(true);
                ivNormalCard.setEnabled(true);
                ivSubmit.setEnabled(true);
            }
        });

        ivLaserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ivLaserCard.setAlpha(1.0f);
                txtLaserCost.setAlpha(1.0f);
                txtLaserDesc.setAlpha(1.0f);

                ivNormalCard.setAlpha(0.4f);
                txtNormalCost.setAlpha(0.4f);
                txtNormalDesc.setAlpha(0.4f);

                PhysicalCardTypeID = "1";
                Price = laserPrintCost;
                int amt = Integer.parseInt(Price);
                amount = amt * 100;
            }
        });

        ivNormalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ivLaserCard.setAlpha(0.4f);
                txtLaserCost.setAlpha(0.4f);
                txtLaserDesc.setAlpha(0.4f);

                ivNormalCard.setAlpha(1.0f);
                txtNormalCost.setAlpha(1.0f);
                txtNormalDesc.setAlpha(1.0f);

                PhysicalCardTypeID = "2";
                Price = normalPrintCost;
                int amt = Integer.parseInt(Price);
                amount = amt * 100;
            }
        });

        mViewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager1.scrollTo(mViewPager2.getScrollX(), mViewPager2.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager1.setCurrentItem(mViewPager2.getCurrentItem(), false);
                }
            }
        });

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


    private class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(activity);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
          //  CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST8(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
          //  rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null)
                    {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                     //   Toast.makeText(getApplicationContext(), "Front Card Uploaded Successfully. Add Back Card..", Toast.LENGTH_LONG).show();
                        card_front = ImgName;


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    public String POST7(String url)
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64Back );
            jsonObject.accumulate("classification", "card" );

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

    public String POST8(String url)
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64Front );
            jsonObject.accumulate("classification", "card" );

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

    private class HttpAsyncTaskBackUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
            //CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST7(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            //rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "Back Card Uploaded Successfully.", Toast.LENGTH_LONG).show();
                         card_back = ImgName;
                    } else {
                        Toast.makeText(getApplicationContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public String POST1(String url)
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
            jsonObject.accumulate("Delivery_Addr", etAddress1.getText().toString() + " " + etAddress2.getText().toString() );
            jsonObject.accumulate("Name", etPerson.getText().toString() );
            jsonObject.accumulate("NumOfCards", "1" );
            jsonObject.accumulate("Phone", etPhone.getText().toString() );
            jsonObject.accumulate("PhysicalCard_Type_Id", PhysicalCardTypeID );
            jsonObject.accumulate("PhysicalCard_back_image", card_back );
            jsonObject.accumulate("PhysicalCard_front_image", card_front );
            jsonObject.accumulate("ProfileId", profileId );
            jsonObject.accumulate("UserId", userID );

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(NewCardRequestActivity1.this);
            dialog.setMessage("Loading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("PhysicalCard_Types");
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    JSONObject object = jsonArray.getJSONObject(0);
                    //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                   // Type = object.getString("Type");
//                    txtLaserDesc.setText(object.getString("Description"));

                    laserPrintCost = object.getString("Cost");
                    if (laserPrintCost.equalsIgnoreCase("0"))
                    {
                        txtLaserCost.setText("Free for a Limited Period");
                        tvAmount.setText("S$"+laserPrintCost);
                    }
                    else
                    {
                        txtLaserCost.setText("S$"+laserPrintCost+"/pc");
                        tvAmount.setText("S$"+laserPrintCost);
                    }

                    PhysicalCardLaserId = "1";

                    JSONObject object1 = jsonArray.getJSONObject(1);
                    //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                  //  Type = object.getString("Type");
//                    txtNormalDesc.setText(object1.getString("Description"));
                    normalPrintCost = object1.getString("Cost") ;

                    if (normalPrintCost.equalsIgnoreCase("0"))
                    {
                        txtNormalCost.setText("Free for a Limited Period");
                        tvAmount.setText("S$"+normalPrintCost);
                    }
                    else
                    {
                        txtNormalCost.setText("S$"+normalPrintCost+"/pc");
                        tvAmount.setText("S$"+normalPrintCost);
                    }

                   // txtNormalCost.setText("SGD $"+normalPrintCost+"/Pc");
//                    txtNormalCost.setText(object1.getString("Cost"));
                    PhysicalCardNormalId = "2";

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncRequestTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(NewCardRequestActivity1.this);
            dialog.setMessage("Requesting...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Requesting" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null) {
                    JSONObject object = new JSONObject(result);

                    // Type = object.getString("Type");
                    String success = object.getString("success");
                    String message = object.getString("message");

                    if (success.equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), "Request for NFC card is successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Send New Card Request..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void cardPayment()
    {
        card = new Card
                (
                        numberOnCard,
                        Integer.valueOf(exMonthOnCard),
                        Integer.valueOf(exYearOnCard),
                        cvvOnCard
                );

        card.setCurrency("sgd");
        card.setName(nameOnCard);

        stripe.createToken(card, "pk_live_d0uXEesOC2Qg5919ul4t7Ocl", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                strToken = token.getId();
                //  new StripeCharge(token.getId()).execute();
                new HttpAsyncTokenTask().execute("http://circle8.asia/Checkout/Pay");
                alertDialog.cancel();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public  String POST2(String url)
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
            jsonObject.accumulate("amt", amount );
            jsonObject.accumulate("currency", "sgd" );
            jsonObject.accumulate("source", strToken );
            jsonObject.accumulate("Email", email );
            jsonObject.accumulate("Description", "New card Request Payment" );

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

    private class HttpAsyncTokenTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*dialog = new ProgressDialog(SubscriptionActivity.this);
            dialog.setMessage("Loading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST2(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("Status");
                    if (success.equals("success")){
                        Toast.makeText(getApplicationContext(), "Paied..", Toast.LENGTH_LONG).show();
                        new HttpAsyncRequestTask().execute(Utility.BASE_URL+"Physical_Card/Order");
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void submitCard(View view)
    {

        // TODO: replace with your own test key

        card = new Card(
                cardNumberField.getText().toString(),
                Integer.valueOf(monthField.getText().toString()),
                Integer.valueOf(yearField.getText().toString()),
                cvcField.getText().toString()
        );

        card.setCurrency("usd");
        card.setName("Theodhor Pandeli");
        card.setAddressZip("1000");
        /*
        card.setNumber(4242424242424242);
        card.setExpMonth(12);
        card.setExpYear(19);
        card.setCVC("123");
        */

        stripe.createToken(card, "pk_test_6fZCC6Gu2kwYLUQxJhGte65l", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                //  new StripeCharge(token.getId()).execute();
                alertDialog.cancel();
                new HttpAsyncRequestTask().execute(Utility.BASE_URL+"Physical_Card/Order");

             /*   Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                startActivity(intent);
                finish();*/
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                new HttpAsyncRequestTask().execute(Utility.BASE_URL+"Physical_Card/Order");
            }
        });
    }

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);

        int SPLASHTIME = 1000*60 ;  //since 1000=1sec so 1000*60 = 60000 or 60sec or 1 min.
        for (int i = 350; i <= SPLASHTIME; i = i + 350)
        {
            final int j = i;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run()
                {
                    if (j / 350 == 1 || j / 350 == 4 || j / 350 == 7 || j / 350 == 10)
                    {
                        tvProgressing.setText(loading+".");
                    }
                    else if (j / 350 == 2 || j / 350 == 5 || j / 350 == 8)
                    {
                        tvProgressing.setText(loading+"..");
                    }
                    else if (j / 350 == 3 || j / 350 == 6 || j / 350 == 9)
                    {
                        tvProgressing.setText(loading+"...");
                    }

                }
            }, i);
        }
    }


}
