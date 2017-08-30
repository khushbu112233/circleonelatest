package com.amplearch.circleonet.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.NotificationAdapter;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.NotificationModel;
import com.amplearch.circleonet.R;

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

public class Notification extends AppCompatActivity
{
    ListView listNotification;
    LoginSession loginSession;
    String UserId = "";
    ArrayList<NotificationModel> allTags;
    NotificationAdapter notificationAdapter;
    private TextView textView;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        textView.setText("Notifications - 0");
        ImageView drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setVisibility(View.GONE);
       // imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);

        listNotification = (ListView) findViewById(R.id.listNotification);
        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        allTags = new ArrayList<>();
        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/Notification");

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
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
            jsonObject.accumulate("userid", UserId );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("numofrecords", "30" );

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Notification.this);
            dialog.setMessage("Getting Notifications...");
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
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("notification");
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    textView.setText("Notifications - "+jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        NotificationModel nfcModelTag = new NotificationModel();
                        nfcModelTag.setFriendUserID(object.getString("FriendUserID"));
                        nfcModelTag.setFriendProfileID(object.getString("FriendProfileID"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setPurpose(object.getString("Purpose"));
                        allTags.add(nfcModelTag);
                    }

                    notificationAdapter = new NotificationAdapter(Notification.this, allTags);
                    listNotification.setAdapter(notificationAdapter);
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
