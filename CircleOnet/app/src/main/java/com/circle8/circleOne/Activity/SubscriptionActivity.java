package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.SubscriptionAdapter;
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

public class SubscriptionActivity extends AppCompatActivity
{
    private ListView listView ;
    private ImageView ivBack ;
    private RelativeLayout rlStripePayment, rlListView ;
//    private TextView tvCancel, tvPay ;

    private ArrayList<SubscriptionModel> subscriptionModelArrayList ;

    private ArrayList<String> packageName = new ArrayList<>();
    private ArrayList<Integer> contact = new ArrayList<>();
    private ArrayList<Integer> group = new ArrayList<>();
    private ArrayList<Integer> connection = new ArrayList<>();
    private ArrayList<Integer> amount = new ArrayList<>();
    private ArrayList<Integer> left_connection = new ArrayList<>();

    private SubscriptionAdapter subscriptionAdapter ;
    TextView cardNumberField, monthField, yearField, cvcField;
    Stripe stripe;
    Card card;
    Token tok;
    String strToken;
    AlertDialog alertDialog;
    ImageView imgBack;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    int width, height;
    WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        ivBack = (ImageView)findViewById(R.id.imgBack);
        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        subscriptionModelArrayList = new ArrayList<>();

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        rlStripePayment = (RelativeLayout)findViewById(R.id.rlStripePayment);
        rlListView = (RelativeLayout)findViewById(R.id.rlListView);

//        tvPay = (TextView)findViewById(R.id.tvPay);
//        tvCancel = (TextView)findViewById(R.id.tvCancel);

        packageName.add("Free Package");
        contact.add(10);
        group.add(2);
        connection.add(2);
        amount.add(0);
        left_connection.add(0);

        packageName.add("Package 1");
        contact.add(300);
        group.add(5);
        connection.add(1);
        amount.add(1);
        left_connection.add(1);

        packageName.add("Package 2");
        contact.add(350);
        group.add(6);
        connection.add(2);
        amount.add(2);
        left_connection.add(0);

        packageName.add("Package 3");
        contact.add(400);
        group.add(10);
        connection.add(8);
        amount.add(3);
        left_connection.add(2);

        packageName.add("Package 4");
        contact.add(450);
        group.add(20);
        connection.add(20);
        amount.add(3);
        left_connection.add(0);

        packageName.add("Package 5");
        contact.add(1000);
        group.add(200);
        connection.add(200);
        amount.add(30);
        left_connection.add(10);

        /*subscriptionAdapter = new SubscriptionAdapter(getApplicationContext(),
                packageName, contact, group, connection, amount, left_connection);
        listView.setAdapter(subscriptionAdapter);
        subscriptionAdapter.notifyDataSetChanged();*/

        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/Subscription/GetPackageList");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

/*
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rlStripePayment.setVisibility(View.GONE);
                rlListView.setAlpha(1);
            }
        });
*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String package_Name = subscriptionModelArrayList.get(position).getPackageName();
                String contacts_limit = subscriptionModelArrayList.get(position).getConnectionLimit();
                String groups_limit = subscriptionModelArrayList.get(position).getGroupLimit();
                String month_connect_limit = subscriptionModelArrayList.get(position).getMonthlyConnectionLimit();
                String left_connection = subscriptionModelArrayList.get(position).getLetf_connection();
                String amount = subscriptionModelArrayList.get(position).getPrice();

                try
                {
                    stripe = new Stripe("pk_live_d0uXEesOC2Qg5919ul4t7Ocl");
                }
                catch (AuthenticationException e)
                {
                    e.printStackTrace();
                }


//                rlStripePayment.setVisibility(View.VISIBLE);
//                rlListView.setAlpha((float) 0.1);

                alertDialog = new AlertDialog.Builder(SubscriptionActivity.this).create();
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.stripe_payment_screen, null);

                TextView tvPackageName = (TextView)dialogView.findViewById(R.id.tvPackageName);
                TextView tvConnect_Group = (TextView)dialogView.findViewById(R.id.tvContact_Group);
                TextView tvConnection = (TextView)dialogView.findViewById(R.id.tvConnection);
                TextView tvAmount = (TextView)dialogView.findViewById(R.id.tvAmount);
                TextView tvLeftConnection = (TextView)dialogView.findViewById(R.id.tvLeftConnection);
                RelativeLayout rlLeftConnection = (RelativeLayout)dialogView.findViewById(R.id.rlLeftConnection);
                ImageView ivVisa = (ImageView)dialogView.findViewById(R.id.ivVisa);
                ImageView ivMasterCard = (ImageView)dialogView.findViewById(R.id.ivMasterCard);
                ImageView ivAmex = (ImageView)dialogView.findViewById(R.id.ivAmex);
                EditText etCardNumber = (EditText)dialogView.findViewById(R.id.etCardNumber);
                EditText etCardHolderName = (EditText)dialogView.findViewById(R.id.etCardHolderName);
                EditText etExMonth = (EditText)dialogView.findViewById(R.id.etExMonth);
                EditText etExYear = (EditText)dialogView.findViewById(R.id.etExYear);
                EditText etSecurityCode = (EditText)dialogView.findViewById(R.id.etSecurityCode);
                EditText etMobileNumber = (EditText)dialogView.findViewById(R.id.etMobileNumber);
                TextView tvPay = (TextView)dialogView.findViewById(R.id.tvPay);
                TextView tvCancel = (TextView)dialogView.findViewById(R.id.tvCancel);

                tvPackageName.setText(package_Name);
                tvConnect_Group.setText(contacts_limit+" contacts, up to "+groups_limit+" circles,");
                tvConnection.setText("up to "+month_connect_limit+" connections per month.");
                tvAmount.setText("S$"+amount);
                /*cardNumberField = (TextView) dialogView.findViewById(R.id.cardNumber);
                monthField = (TextView) dialogView.findViewById(R.id.month);
                yearField = (TextView) dialogView.findViewById(R.id.year);
                cvcField = (TextView) dialogView.findViewById(R.id.cvc);*/

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.setCancelable(false);
//                alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                alertDialog.show();
            }
        });

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

           /* try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String findBy = response.getString("FindBy");
                    String search = response.getString("Search");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

                    connectTags.clear();
                    try
                    {
                        connectListAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if(connect.length() == 0)
                    {
                        tvDataInfo.setVisibility(View.VISIBLE);
                        connectTags.clear();
                        try {connectListAdapter.notifyDataSetChanged();}
                        catch (Exception e) { e.printStackTrace();}
                    }
                    else
                    {
                        tvDataInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= connect.length() ; i++ )
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            ConnectList connectModel = new ConnectList();
                            connectModel.setUserID(iCon.getString("UserID"));
                            connectModel.setFirstname(iCon.getString("FirstName"));
                            connectModel.setLastname(iCon.getString("LastName"));
                            connectModel.setUsername(iCon.getString("UserName"));
                            connectModel.setUserphoto(iCon.getString("UserPhoto"));
                            connectModel.setCard_front(iCon.getString("Card_Front"));
                            connectModel.setCard_back(iCon.getString("Card_Back"));
                            connectModel.setProfile_id(iCon.getString("ProfileId"));
                            connectModel.setPhone(iCon.getString("Phone"));
                            connectModel.setCompanyname(iCon.getString("CompanyName"));
                            connectModel.setDesignation(iCon.getString("Designation"));
                            connectModel.setFacebook(iCon.getString("Facebook"));
                            connectModel.setTwitter(iCon.getString("Twitter"));
                            connectModel.setGoogle(iCon.getString("Google"));
                            connectModel.setLinkedin(iCon.getString("LinkedIn"));
                            connectModel.setWebsite(iCon.getString("Website"));
                            connectTags.add(connectModel);

                            connectListAdapter = new ConnectListAdapter(getContext(),R.layout.grid_list5_layout, connectTags);
                            listView.setAdapter(connectListAdapter);
                            connectListAdapter.notifyDataSetChanged();

//                            GetData(getContext());
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }*/
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
            jsonObject.accumulate("amt", 15 );
            jsonObject.accumulate("currency", "usd" );
            jsonObject.accumulate("source", strToken );
            jsonObject.accumulate("Description", "test" );

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
