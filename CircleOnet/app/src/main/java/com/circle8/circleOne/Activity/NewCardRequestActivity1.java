package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
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

public class NewCardRequestActivity1 extends AppCompatActivity {
    private CircleImageView imgProfile;
    private ImageView ivSubmit;
    private TextView tvPerson, tvDesignation, tvCompany, tvProfile;
    private EditText etPerson, etCompany, etPhone, etAddress1, etAddress2;

    private CardSwipe myPager;
    private ArrayList<String> swipe_image = new ArrayList<>();
    String recycle_image1, recycle_image2;
    ViewPager mViewPager1, mViewPager2;
    private String image;
    LinearLayout llBlueCardSample, llGoldCardSample;
    TextView cardNumberField, monthField, yearField, cvcField;
    Stripe stripe;
    Card card;
    Token tok;
    AlertDialog alertDialog;
    ImageView imgBack;
    String card_front = "", card_back = "", Type, Description, Cost, PhysicalCardLaserId, PhysicalCardNormalId;
    TextView txtLaserCost, txtLaserDesc, txtNormalCost, txtNormalDesc;
    String PhysicalCardTypeID;
    LoginSession session;
    private String profileId, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profileId = user.get(LoginSession.KEY_PROFILEID);
        userID = user.get(LoginSession.KEY_USERID);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        txtLaserCost = (TextView) findViewById(R.id.txtLaserCost);
        txtLaserDesc = (TextView) findViewById(R.id.txtLaserDesc);
        txtNormalCost = (TextView) findViewById(R.id.txtNormalCost);
        txtNormalDesc = (TextView) findViewById(R.id.txtNormalDesc);
        ivSubmit = (ImageView) findViewById(R.id.ivSubmit);
        llGoldCardSample = (LinearLayout) findViewById(R.id.llGoldCardSample);
        llBlueCardSample = (LinearLayout) findViewById(R.id.llBlueCardSample);
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

        mViewPager1 = (ViewPager) findViewById(R.id.viewPager);
        mViewPager2 = (ViewPager) findViewById(R.id.viewPager1);

        recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
        recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
        swipe_image.add(recycle_image1);
        swipe_image.add(recycle_image2);
        myPager = new CardSwipe(getApplicationContext(), swipe_image);
        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/Physical_Card/GetType");

        mViewPager1.setClipChildren(false);
        mViewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager1.setOffscreenPageLimit(1);
        mViewPager1.setAdapter(myPager);

        mViewPager2.setClipChildren(false);
        mViewPager2.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.setAdapter(myPager);

        Intent i = getIntent();
        image = i.getStringExtra("image");
        card_front = i.getStringExtra("card_front");
        card_back = i.getStringExtra("card_back");

        if (image.equals("")) {
            imgProfile.setImageResource(R.drawable.usr_1);
        } else {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/" + image).into(imgProfile);
        }
        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));

        etPerson.setText(i.getStringExtra("person"));
        etCompany.setText(i.getStringExtra("company"));
        etPhone.setText(i.getStringExtra("profile"));
        etAddress1.setText("Address");
        etAddress2.setText("");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stripe = new Stripe("pk_test_6fZCC6Gu2kwYLUQxJhGte65l");
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
                alertDialog = new AlertDialog.Builder(NewCardRequestActivity1.this).create();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.activity_stripe_1, null);

                cardNumberField = (TextView) dialogView.findViewById(R.id.cardNumber);
                monthField = (TextView) dialogView.findViewById(R.id.month);
                yearField = (TextView) dialogView.findViewById(R.id.year);
                cvcField = (TextView) dialogView.findViewById(R.id.cvc);


                alertDialog.setView(dialogView);

                alertDialog.show();
            }
        });

        llGoldCardSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBlueCardSample.setAlpha(0.4f);
                llGoldCardSample.setAlpha(1.0f);
                PhysicalCardTypeID = "1";
                //llBlueCardSample.setEnabled(false);
            }
        });


        llBlueCardSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llGoldCardSample.setAlpha(0.4f);
                llBlueCardSample.setAlpha(1.0f);
                PhysicalCardTypeID = "2";
                //llGoldCardSample.setEnabled(false);
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
            jsonObject.accumulate("City", "" );
            jsonObject.accumulate("Country", "" );
            jsonObject.accumulate("Delivery_Addr1", etAddress1.getText().toString() );
            jsonObject.accumulate("Delivery_Addr2", etAddress2.getText().toString() );
            jsonObject.accumulate("Delivery_Addr3", "" );
            jsonObject.accumulate("Delivery_Addr4", "" );
            jsonObject.accumulate("Name", etPerson.getText().toString() );
            jsonObject.accumulate("NumOfCards", "1" );
            jsonObject.accumulate("Phone", etPhone.getText().toString() );
            jsonObject.accumulate("PhysicalCard_Type_Id", PhysicalCardTypeID );
            jsonObject.accumulate("PhysicalCard_back_image", card_back );
            jsonObject.accumulate("PhysicalCard_front_image", card_front );
            jsonObject.accumulate("PostalCode", "1" );
            jsonObject.accumulate("ProfileId", profileId );
            jsonObject.accumulate("State", "" );
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
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewCardRequestActivity1.this);
            dialog.setMessage("Loading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("PhysicalCard_Types");
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    JSONObject object = jsonArray.getJSONObject(0);
                    //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                   // Type = object.getString("Type");
                    txtLaserDesc.setText(object.getString("Description"));
                    txtLaserCost.setText(object.getString("Cost"));
                    PhysicalCardLaserId = "1";


                    JSONObject object1 = jsonArray.getJSONObject(1);
                    //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                  //  Type = object.getString("Type");
                    txtNormalDesc.setText(object1.getString("Description"));
                    txtNormalCost.setText(object1.getString("Cost"));
                    PhysicalCardNormalId = "2";

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncRequestTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewCardRequestActivity1.this);
            dialog.setMessage("Requesting...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject object = new JSONObject(result);

                    // Type = object.getString("Type");
                    String success = object.getString("success");
                    String message = object.getString("message");

                    if (success.equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), "Request sent Successfully.", Toast.LENGTH_LONG).show();
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


    public void submitCard(View view) {
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
                new HttpAsyncRequestTask().execute("http://circle8.asia:8999/Onet.svc/Physical_Card/Order");

             /*   Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                startActivity(intent);
                finish();*/
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

}
