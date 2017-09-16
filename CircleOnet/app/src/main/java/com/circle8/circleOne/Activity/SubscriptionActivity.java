package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.EditGroupAdapter;
import com.circle8.circleOne.Adapter.GroupsItemsAdapter1;
import com.circle8.circleOne.Adapter.SubscriptionAdapter;
import com.circle8.circleOne.Model.GroupModel;
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
    AlertDialog alertDialog;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        ivBack = (ImageView)findViewById(R.id.imgBack);
        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        subscriptionModelArrayList = new ArrayList<>();

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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/Subscription/GetPackageList");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    stripe = new Stripe("pk_test_6fZCC6Gu2kwYLUQxJhGte65l");
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
                alertDialog = new AlertDialog.Builder(SubscriptionActivity.this).create();
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.activity_stripe_1, null);

                cardNumberField = (TextView) dialogView.findViewById(R.id.cardNumber);
                monthField = (TextView) dialogView.findViewById(R.id.month);
                yearField = (TextView) dialogView.findViewById(R.id.year);
                cvcField = (TextView) dialogView.findViewById(R.id.cvc);

                alertDialog.setView(dialogView);

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


        stripe.createToken(card, "pk_test_6fZCC6Gu2kwYLUQxJhGte65l", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                //  new StripeCharge(token.getId()).execute();
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
            dialog = new ProgressDialog(SubscriptionActivity.this);
            dialog.setMessage("Fetching Groups...");
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
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
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

}
