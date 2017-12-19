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
import com.circle8.circleOne.Utils.Utility;

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

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class Connect3Activity extends AppCompatActivity
{
    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting, imgConnecting1;
    TextView txtConnecting;
    int x = 0;
    String profileImg, profileName;
    LoginSession loginSession ;
    String UserId = "", friendUserID = "";
    ArrayList<Level7thConnectionModel> allTags;

    String connectLevel = "";
    String userName1 = "", userName2 = "", userName3 = "", userName4 = "", userName5 = "", userName6 = "", userName7 = "";
    String userPhoto1 = "", userPhoto2 = "", userPhoto3 = "", userPhoto4 = "", userPhoto5 = "", userPhoto6 = "", userPhoto7 = "" ;
    String userProfileId1 = "", userProfileId2 = "", userProfileId3 = "", userProfileId4 = "", userProfileId5 = "",
            userProfileId6 = "", userProfileId7 = "";

    String backStatus = "None";
    String level = "";

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
        Utility.freeMemory();
        HashMap<String, String> user = loginSession.getUserDetails();

        UserId = user.get(LoginSession.KEY_USERID);      // name

        Intent intent = getIntent();
        profileImg = intent.getStringExtra("profile");
        friendUserID = intent.getStringExtra("friendUserID");
        profileName = intent.getStringExtra("profileName");

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

//        new HttpAsyncTask().execute(Utility.BASE_URL+"Connection7Level");

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
                Utility.freeMemory();
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

                backStatus = "Back";

//                new HttpAsyncTask().cancel(true);
               /* Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                go.putExtra("viewpager_position", 1);
                startActivity(go);*/
                finish();
            }
        });

        if (backStatus.equalsIgnoreCase("None"))
        {
            Utility.freeMemory();
            new HttpAsyncTask().execute(Utility.BASE_URL+"Connection7Level");
        }

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                finish();
                Utility.freeMemory();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 1);
                startActivity(go);
                finish();
                Utility.freeMemory();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 2);
                startActivity(go);
                finish();
                Utility.freeMemory();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                go.putExtra("viewpager_position", 3);
                startActivity(go);
                finish();
                Utility.freeMemory();
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

    private void startAction()
    {
        Utility.freeMemory();
        if (backStatus.equalsIgnoreCase("Back"))
        {

        }
        else
        {
            Intent go = new Intent(getApplicationContext(), Connect4Activity.class);
            go.putExtra("level", level);
            go.putExtra("profile", profileImg);
            go.putExtra("profileName", profileName);
            go.putExtra("connectLevel", connectLevel);
            go.putExtra("userName1", userName1);
            go.putExtra("userPhoto1", userPhoto1);
            go.putExtra("userProfileId1", userProfileId1);
            go.putExtra("userName2", userName2);
            go.putExtra("userPhoto2", userPhoto2);
            go.putExtra("userProfileId2", userProfileId2);
            go.putExtra("userName3", userName3);
            go.putExtra("userPhoto3", userPhoto3);
            go.putExtra("userProfileId3", userProfileId3);
            go.putExtra("userName4", userName4);
            go.putExtra("userPhoto4", userPhoto4);
            go.putExtra("userProfileId4", userProfileId4);
            go.putExtra("userName5", userName5);
            go.putExtra("userPhoto5", userPhoto5);
            go.putExtra("userProfileId5", userProfileId5);
            go.putExtra("userName6", userName6);
            go.putExtra("userPhoto6", userPhoto6);
            go.putExtra("userProfileId6", userProfileId6);
            go.putExtra("userName7", userName7);
            go.putExtra("userPhoto7", userPhoto7);
            go.putExtra("userProfileId7", userProfileId7);
            startActivity(go);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Utility.freeMemory();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if (backStatus.equalsIgnoreCase("Back"))
        {
            Utility.freeMemory();
            finish();
        }
    }



    public  String POST(String url)
    {
        Utility.freeMemory();
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
            try
            {
                Utility.freeMemory();
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
//                    Toast.makeText(getApplicationContext(), jsonArray.length()+"", Toast.LENGTH_LONG).show();

                    level = jsonArray.length()-1+"";

                    if(jsonArray.length() == 8)
                    {
                        connectLevel = "7" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(2);
                        userName2 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto2 = object2.getString("UserPhoto");
                        userProfileId2 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(3);
                        userName3 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto3 = object3.getString("UserPhoto");
                        userProfileId3 = object3.getString("ProfileId") ;
                        //for 4th user
                        JSONObject object4 = jsonArray.getJSONObject(4);
                        userName4 = object4.getString("FirstName")+" "+object4.getString("LastName");
                        userPhoto4 = object4.getString("UserPhoto");
                        userProfileId4 = object4.getString("ProfileId") ;
                        //for 5th user
                        JSONObject object5 = jsonArray.getJSONObject(5);
                        userName5 = object5.getString("FirstName")+" "+object5.getString("LastName");
                        userPhoto5 = object5.getString("UserPhoto");
                        userProfileId5 = object5.getString("ProfileId") ;
                        //for 6th user
                        JSONObject object6 = jsonArray.getJSONObject(6);
                        userName6 = object6.getString("FirstName")+" "+object6.getString("LastName");
                        userPhoto6 = object6.getString("UserPhoto");
                        userProfileId6 = object6.getString("ProfileId") ;
                        //for 7th user
                        JSONObject object7 = jsonArray.getJSONObject(7);
                        userName7 = object7.getString("FirstName")+" "+object7.getString("LastName");
                        userPhoto7 = object7.getString("UserPhoto");
                        userProfileId7 = object7.getString("ProfileId") ;
                    }
                    else if(jsonArray.length() == 7)
                    {
                        connectLevel = "6" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(2);
                        userName2 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto2 = object2.getString("UserPhoto");
                        userProfileId2 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(3);
                        userName3 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto3 = object3.getString("UserPhoto");
                        userProfileId3 = object3.getString("ProfileId") ;
                        //for 4th user
                        JSONObject object4 = jsonArray.getJSONObject(4);
                        userName4 = object4.getString("FirstName")+" "+object4.getString("LastName");
                        userPhoto4 = object4.getString("UserPhoto");
                        userProfileId4 = object4.getString("ProfileId") ;
                        //for 5th user
                        JSONObject object5 = jsonArray.getJSONObject(5);
                        userName5 = object5.getString("FirstName")+" "+object5.getString("LastName");
                        userPhoto5 = object5.getString("UserPhoto");
                        userProfileId5 = object5.getString("ProfileId") ;
                        //for 6th user
                        JSONObject object6 = jsonArray.getJSONObject(6);
                        userName6 = object6.getString("FirstName")+" "+object6.getString("LastName");
                        userPhoto6 = object6.getString("UserPhoto");
                        userProfileId6 = object6.getString("ProfileId") ;
                    }
                    else if (jsonArray.length() == 6)
                    {
                        connectLevel = "5" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(2);
                        userName2 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto2 = object2.getString("UserPhoto");
                        userProfileId2 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(3);
                        userName3 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto3 = object3.getString("UserPhoto");
                        userProfileId3 = object3.getString("ProfileId") ;
                        //for 4th user
                        JSONObject object4 = jsonArray.getJSONObject(4);
                        userName4 = object4.getString("FirstName")+" "+object4.getString("LastName");
                        userPhoto4 = object4.getString("UserPhoto");
                        userProfileId4 = object4.getString("ProfileId") ;
                        //for 5th user
                        JSONObject object5 = jsonArray.getJSONObject(5);
                        userName5 = object5.getString("FirstName")+" "+object5.getString("LastName");
                        userPhoto5 = object5.getString("UserPhoto");
                        userProfileId5 = object5.getString("ProfileId") ;
                    }
                    else if (jsonArray.length() == 5)
                    {
                        connectLevel = "4" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(2);
                        userName2 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto2 = object2.getString("UserPhoto");
                        userProfileId2 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(3);
                        userName3 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto3 = object3.getString("UserPhoto");
                        userProfileId3 = object3.getString("ProfileId") ;
                        //for 4th user
                        JSONObject object4 = jsonArray.getJSONObject(4);
                        userName4 = object4.getString("FirstName")+" "+object4.getString("LastName");
                        userPhoto4 = object4.getString("UserPhoto");
                        userProfileId4 = object4.getString("ProfileId") ;
                    }
                    else if (jsonArray.length() == 4)
                    {
                        connectLevel = "3" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(2);
                        userName2 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto2 = object2.getString("UserPhoto");
                        userProfileId2 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(3);
                        userName3 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto3 = object3.getString("UserPhoto");
                        userProfileId3 = object3.getString("ProfileId") ;

                    }
                    else if (jsonArray.length() == 3)
                    {
                        connectLevel = "2" ;
                        //for 1st user
                        //for 2nd user
                        JSONObject object2 = jsonArray.getJSONObject(1);
                        userName1 = object2.getString("FirstName")+" "+object2.getString("LastName");
                        userPhoto1 = object2.getString("UserPhoto");
                        userProfileId1 = object2.getString("ProfileId") ;
                        //for 3rd user
                        JSONObject object3 = jsonArray.getJSONObject(2);
                        userName2 = object3.getString("FirstName")+" "+object3.getString("LastName");
                        userPhoto2 = object3.getString("UserPhoto");
                        userProfileId2 = object3.getString("ProfileId") ;
                    }
                    else if (jsonArray.length() == 2)
                    {
                        connectLevel = "1" ;
                        //for 1st user
                        JSONObject object1 = jsonArray.getJSONObject(1);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;
                        //for 2nd user
                    }
                    else if (jsonArray.length() == 1)
                    {
                        connectLevel = "0" ;
                        //for 1st user
                       /* JSONObject object1 = jsonArray.getJSONObject(0);
                        userName1 = object1.getString("FirstName")+" "+object1.getString("LastName");
                        userPhoto1 = object1.getString("UserPhoto");
                        userProfileId1 = object1.getString("ProfileId") ;*/
                    }
                    else if (jsonArray.length() == 0)
                    {
                        connectLevel = "0" ;
                    }
                    else
                    {
                        connectLevel = "0" ;
                    }

                    startAction();

                   /* for (int i = 0; i < jsonArray.length(); i++)
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
                    }*/

//                    notificationAdapter = new NotificationAdapter(Notification.this, allTags);
//                    listNotification.setAdapter(notificationAdapter);

                   /* if (new HttpAsyncTask().isCancelled())
                    {
                        return;
                    }
                    else {

                        Intent go = new Intent(getApplicationContext(), Connect4Activity.class);
                        go.putExtra("level", level);
                        go.putExtra("profile", profile);
                        go.putExtra("connectLevel", connectLevel);
                        go.putExtra("userName1", userName1);
                        go.putExtra("userPhoto1", userPhoto1);
                        go.putExtra("userProfileId1", userProfileId1);
                        go.putExtra("userName2", userName2);
                        go.putExtra("userPhoto2", userPhoto2);
                        go.putExtra("userProfileId2", userProfileId2);
                        go.putExtra("userName3", userName3);
                        go.putExtra("userPhoto3", userPhoto3);
                        go.putExtra("userProfileId3", userProfileId3);
                        go.putExtra("userName4", userName4);
                        go.putExtra("userPhoto4", userPhoto4);
                        go.putExtra("userProfileId4", userProfileId4);
                        go.putExtra("userName5", userName5);
                        go.putExtra("userPhoto5", userPhoto5);
                        go.putExtra("userProfileId5", userProfileId5);
                        go.putExtra("userName6", userName6);
                        go.putExtra("userPhoto6", userPhoto6);
                        go.putExtra("userProfileId6", userProfileId6);
                        go.putExtra("userName7", userName7);
                        go.putExtra("userPhoto7", userPhoto7);
                        go.putExtra("userProfileId7", userProfileId7);
                        startActivity(go);
                        finish();
                    }*/
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
