package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.Level7thConnectionModel;
import com.circle8.circleOne.R;

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

public class Connect3Activity extends AppCompatActivity
{
    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting, imgConnecting1;
    TextView txtConnecting;
    int x = 0;
    String profile;
    LoginSession loginSession ;
    String UserId = "", friendUserID = "";
    ArrayList<Level7thConnectionModel> allTags;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect3);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgConnecting = (ImageView) findViewById(R.id.imgConnecting);
        imgConnecting1 = (ImageView) findViewById(R.id.imgConnecting1);
        txtConnecting = (TextView) findViewById(R.id.txtConnecting);
        loginSession = new LoginSession(getApplicationContext());

        HashMap<String, String> user = loginSession.getUserDetails();

        UserId = user.get(LoginSession.KEY_USERID);      // name

        Intent intent = getIntent();
        profile = intent.getStringExtra("profile");
        friendUserID = intent.getStringExtra("friendUserID");
        allTags = new ArrayList<>();
       // Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();
       /* Handler handler = new Handler();

        for (int i = 100; i <= 60000; i=i+100) {
            final int finalI = i;
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if(finalI %500 == 0){
                        txtConnecting.setText("Connecting.");
                    }else if(finalI %400 == 0){
                        txtConnecting.setText("Connecting..");
                    }else if(finalI %300 == 0){
                        txtConnecting.setText("Connecting...");
                    }
                }
            }, i);
        }*/

        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/Connection7Level");

        /*Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
//change your text here
                if (x == 0) {
                    txtConnecting.setText("Connecting.");
                    x += 1;
                } else if (x == 1){
                    txtConnecting.setText("Connecting..");
                    x += 1;
                }
            else if (x == 2) {
                    txtConnecting.setText("Connecting...");
                    x = 0;
                }
            }
        }, 100);*/

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        imgConnecting.startAnimation(anim);

        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        imgConnecting1.startAnimation(anim1);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               /* Intent go = new Intent(getApplicationContext(),ConnectActivity.class);
                startActivity(go);
                finish();*/
              /*  Intent go = new Intent(getApplicationContext(),ConnectActivity.class);
                go.putExtra("level", level);
                go.putExtra("profile", profile);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);
                finish();*/
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

    }

    @Override
    public void onBackPressed() {

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

    public  String POST(String url)
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
            jsonObject.accumulate("userId_dest", friendUserID );
            jsonObject.accumulate("userId_src", UserId );

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

        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
      //  ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(Connect3Activity.this);
            dialog.setMessage("Getting Notifications...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
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
          //  dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
//                    Toast.makeText(getApplicationContext(), jsonArray.length()+"", Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        Level7thConnectionModel nfcModelTag = new Level7thConnectionModel();
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setProfileId(object.getString("ProfileId"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setConnection_Status(object.getString("Connection_Status"));
                        allTags.add(nfcModelTag);
                    }

//                    notificationAdapter = new NotificationAdapter(Notification.this, allTags);
//                    listNotification.setAdapter(notificationAdapter);

                    Intent go = new Intent(getApplicationContext(),Connect4Activity.class);
                    // you pass the position you want the viewpager to show in the extra,
                    // please don't forget to define and initialize the position variable
                    // properly
                    go.putExtra("level", jsonArray.length()+"");
                    go.putExtra("profile", profile);
                    startActivity(go);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Friends..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
