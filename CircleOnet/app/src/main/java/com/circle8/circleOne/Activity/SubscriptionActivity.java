package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.SubscriptionAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.SubscriptionModel;
import com.circle8.circleOne.R;
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

public class SubscriptionActivity extends AppCompatActivity
{
    private ListView listView ;
    private RelativeLayout  rlListView ;

    private ArrayList<SubscriptionModel> subscriptionModelArrayList ;

    private SubscriptionAdapter subscriptionAdapter ;
    TextView cardNumberField, monthField, yearField, cvcField, cardHolderName;
    Stripe stripe;
    Card card;
    Token tok;
    String strToken;
    AlertDialog alertDialog;
    ImageView imgBack, ivAlphaImg ;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    int amount;
    private String PackageName, SubscriptionID;
    String UserId = "";
    private LoginSession session;

    int width, height;
    WindowManager.LayoutParams params;

    String numberOnCard, nameOnCard, exYearOnCard, exMonthOnCard, cvvOnCard, mobileNoOnCard ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        ivAlphaImg = (ImageView) findViewById(R.id.ivAlphaImg);
        subscriptionModelArrayList = new ArrayList<>();
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // name
        UserId = user.get(LoginSession.KEY_USERID);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        rlListView = (RelativeLayout)findViewById(R.id.rlListView);

//        tvPay = (TextView)findViewById(R.id.tvPay);
//        tvCancel = (TextView)findViewById(R.id.tvCancel);

        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/Subscription/GetPackageList");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ivAlphaImg.setVisibility(View.VISIBLE);

                String package_Name = subscriptionModelArrayList.get(position).getPackageName();
                String contacts_limit = subscriptionModelArrayList.get(position).getConnectionLimit();
                String groups_limit = subscriptionModelArrayList.get(position).getGroupLimit();
                String month_connect_limit = subscriptionModelArrayList.get(position).getMonthlyConnectionLimit();
                String left_connection = subscriptionModelArrayList.get(position).getLetf_connection();
                String amountt = subscriptionModelArrayList.get(position).getPrice();

                int price = Integer.parseInt(subscriptionModelArrayList.get(position).getPrice());
                PackageName = subscriptionModelArrayList.get(position).getPackageName();
                SubscriptionID = subscriptionModelArrayList.get(position).getPackageID();
                amount = price * 100;

                try
                {
                    stripe = new Stripe("pk_live_d0uXEesOC2Qg5919ul4t7Ocl");
                }
                catch (AuthenticationException e)
                {
                    e.printStackTrace();
                }

                alertDialog = new AlertDialog.Builder(SubscriptionActivity.this).create();
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.stripe_payment_screen, null);

                LinearLayout llCardValues = (LinearLayout)dialogView.findViewById(R.id.llCardValues);
                LinearLayout llPackageDetails = (LinearLayout)dialogView.findViewById(R.id.llPackageDetails);

                TextView tvPackageName = (TextView)dialogView.findViewById(R.id.tvPackageName);
                TextView tvConnect_Group = (TextView)dialogView.findViewById(R.id.tvContact_Group);
                TextView tvConnection = (TextView)dialogView.findViewById(R.id.tvConnection);
                TextView tvAmount = (TextView)dialogView.findViewById(R.id.tvAmount);
                TextView tvLeftConnection = (TextView)dialogView.findViewById(R.id.tvLeftConnection);
                RelativeLayout rlLeftConnection = (RelativeLayout)dialogView.findViewById(R.id.rlLeftConnection);
                ImageView ivVisa = (ImageView)dialogView.findViewById(R.id.ivVisa);
                ImageView ivMasterCard = (ImageView)dialogView.findViewById(R.id.ivMasterCard);
                ImageView ivAmex = (ImageView)dialogView.findViewById(R.id.ivAmex);
                final EditText etCardNumber = (EditText)dialogView.findViewById(R.id.etCardNumber);
                final EditText etCardHolderName = (EditText)dialogView.findViewById(R.id.etCardHolderName);
                final EditText etExMonth = (EditText)dialogView.findViewById(R.id.etExMonth);
                final EditText etExYear = (EditText)dialogView.findViewById(R.id.etExYear);
                final EditText etSecurityCode = (EditText)dialogView.findViewById(R.id.etSecurityCode);
                final EditText etMobileNumber = (EditText)dialogView.findViewById(R.id.etMobileNumber);
                TextView tvPay = (TextView)dialogView.findViewById(R.id.tvPay);
                TextView tvCancel = (TextView)dialogView.findViewById(R.id.tvCancel);

                tvPackageName.setText(package_Name);
                tvConnect_Group.setText(contacts_limit+" contacts, up to "+groups_limit+" circles,");
                tvConnection.setText("up to "+month_connect_limit+" connections per month.");
                tvAmount.setText("SGD $"+price);

                /*cardNumberField = (TextView) dialogView.findViewById(R.id.cardNumber);
                monthField = (TextView) dialogView.findViewById(R.id.month);
                yearField = (TextView) dialogView.findViewById(R.id.year);
                cvcField = (TextView) dialogView.findViewById(R.id.cvc);*/
//                cvcField = (TextView) dialogView.findViewById(R.id.cvc);
//                cardHolderName = (TextView) dialogView.findViewById(R.id.cardHolderName);

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

                        if (numberOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter Card No.",Toast.LENGTH_SHORT).show();
                        }
                        else if (nameOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter Holder Name",Toast.LENGTH_SHORT).show();
                        }
                        else if (exMonthOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter Expiry Month",Toast.LENGTH_SHORT).show();
                        }
                        else if (exYearOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter Expiry Month",Toast.LENGTH_SHORT).show();
                        }
                        else if (cvvOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter CVV No.",Toast.LENGTH_SHORT).show();
                        }
                        else if (mobileNoOnCard.isEmpty())
                        {
                            Toast.makeText(SubscriptionActivity.this,"Enter Mobile No.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            cardPayment();
                            alertDialog.dismiss();
                            ivAlphaImg.setVisibility(View.GONE);
                        }
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                        ivAlphaImg.setVisibility(View.GONE);
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
//                alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        });

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
                new HttpAsyncTokenTask().execute("https://circle8.asia/Checkout/pay");
                alertDialog.cancel();
                ivAlphaImg.setVisibility(View.GONE);
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }


    public void submitCard(View view)
    {
        // TODO: replace with your own test key

       /* card = new Card
        (
            cardNumberField.getText().toString(),
            Integer.valueOf(monthField.getText().toString()),
            Integer.valueOf(yearField.getText().toString()),
            cvcField.getText().toString()
        );*/
        card = new Card
                (
                        numberOnCard,
                        Integer.valueOf(exMonthOnCard),
                        Integer.valueOf(exYearOnCard),
                        cvvOnCard
                );

        card.setCurrency("sgd");
//        card.setName(cardHolderName.getText().toString());
        card.setName(nameOnCard);
       // card.setAddressZip("1000");
        /*
        card.setNumber(4242424242424242);
        card.setExpMonth(12);
        card.setExpYear(19);
        card.setCVC("123");
        */

        stripe.createToken(card, "pk_live_d0uXEesOC2Qg5919ul4t7Ocl", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                strToken = token.getId();
                //  new StripeCharge(token.getId()).execute();
                new HttpAsyncTokenTask().execute("https://circle8.asia/Checkout/pay");
                alertDialog.cancel();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(SubscriptionActivity.this);
            dialog.setMessage("Fetching Groups...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Subscriptions" ;
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
                    JSONArray jsonArray = jsonObject.getJSONArray("SubscriptionPackage_List");
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        SubscriptionModel subscriptionModel = new SubscriptionModel();
                        subscriptionModel.setPackageID(object.getString("PackageID"));
                        subscriptionModel.setPackageName(object.getString("Package_Name"));
                        subscriptionModel.setPackageDesc(object.getString("Package_Desc"));
                        subscriptionModel.setPrice(object.getString("Price"));
                        subscriptionModel.setPackageType(object.getString("Package_Type"));
                        subscriptionModel.setConnectionLimit(object.getString("Connection_Limit"));
                        subscriptionModel.setGroupLimit(object.getString("Group_Limit"));
                        subscriptionModel.setMonthlyConnectionLimit(object.getString("Monthy_Connection_Limit"));
                        subscriptionModel.setLetf_connection("");
                        subscriptionModelArrayList.add(subscriptionModel);

                        String PackageID = object.getString("PackageID");
                        String Package_Name = object.getString("Package_Name");
                        String Package_Desc = object.getString("Package_Desc");
                        String Price = object.getString("Price");
                        String Package_Type = object.getString("Package_Type");
                        String Connection_Limit = object.getString("Connection_Limit");
                        String Group_Limit = object.getString("Group_Limit");
                        String Monthy_Connection_Limit = object.getString("Monthy_Connection_Limit");
                    }

                    subscriptionAdapter = new SubscriptionAdapter(SubscriptionActivity.this, subscriptionModelArrayList);
                    listView.setAdapter(subscriptionAdapter);
                    subscriptionAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
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

    private class HttpAsyncSubscriptTask extends AsyncTask<String, Void, String>
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
                    String message = response.getString("Message");
                    String success = response.getString("Success");
                    if (success.equals("1")){
                        Toast.makeText(getApplicationContext(), "Subscribed..", Toast.LENGTH_LONG).show();
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
            return POST1(urls[0]);
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
                        new HttpAsyncSubscriptTask().execute("http://circle8.asia:8999/Onet.svc/Subscription/AddUser");
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

    public  String POST1(String url)
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
            jsonObject.accumulate("Description", PackageName );

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
            jsonObject.accumulate("my_userid", UserId );
            jsonObject.accumulate("subscription_id", SubscriptionID );

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
