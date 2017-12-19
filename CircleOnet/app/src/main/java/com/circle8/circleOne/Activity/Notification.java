package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.NotificationAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.NotificationModel;
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

public class Notification extends AppCompatActivity
{
    static ListView listNotification;
    LoginSession loginSession;
    static String UserId = "";
    static ArrayList<NotificationModel> allTags;
    static NotificationAdapter notificationAdapter;
    private static TextView textView;
    ImageView imgLogo;

    public static Context mContext ;

    public static RelativeLayout rlProgressDialog ;
    public static TextView tvProgressing ;
    public static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    static ArrayList<NotificationModel> allTagsList = new ArrayList<>();
    static RelativeLayout rlLoadMore ;
    static int numberCount, listSize;
    public static int pageno = 1 ;
    static String counts = "0" ;

    public static String progressStatus = "FIRST";
    public static String comeFirst = "Yes" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mContext = Notification.this ;
        pageno = 1;

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        ImageView drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setVisibility(View.GONE);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);

        textView.setText("Notifications - 0");

        listNotification = (ListView) findViewById(R.id.listNotification);

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_SHORT).show();

        allTags = new ArrayList<>();

        rlLoadMore = (RelativeLayout)findViewById(R.id.rlLoadMore);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        callFirst();

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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

    private void callFirst()
    {
        new HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
    }

    public static void webCall()
    {
        allTags.clear();
        notificationAdapter.notifyDataSetChanged();
        new HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
    }

    public static String POST(String url)
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
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", pageno );
            jsonObject.accumulate("userid", UserId );

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

        pageno++ ;
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

    private static class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(mContext);
            dialog.setMessage("Getting Notifications...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            if (progressStatus.equalsIgnoreCase("LOAD MORE"))
            {

            }
            else
            {
                String loading = "Notification" ;
                CustomProgressDialog(loading);
            }

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
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    counts = jsonObject.getString("Count");
                    JSONArray jsonArray = jsonObject.getJSONArray("notification");

                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    if (pageno == 2)
                    {
                        textView.setText("Notifications - "+jsonObject.getString("Count"));
                    }

                    if (counts.equals("0") || counts == null)
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(counts);
                    }

                    rlLoadMore.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        NotificationModel nfcModelTag = new NotificationModel();
                        nfcModelTag.setFriendUserID(object.getString("FriendUserID"));
                        nfcModelTag.setFriendProfileID(object.getString("FriendProfileID"));
                        nfcModelTag.setMyUserID(object.getString("MyUserID"));
                        nfcModelTag.setMyProfileID(object.getString("MyProfileID"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setPurpose(object.getString("Purpose"));
                        nfcModelTag.setNotificationID(object.getString("NotificationID"));
                        nfcModelTag.setStatus(object.getString("Status"));
                        nfcModelTag.setShared_UserID(object.getString("Shared_UserID"));
                        nfcModelTag.setShared_ProfileID(object.getString("Shared_ProfileID"));
                        nfcModelTag.setViewed_Flag(object.getString("Viewed_Flag"));
                        nfcModelTag.setListCount(counts);
                        allTags.add(nfcModelTag);
                    }

                    listSize = allTags.size();

                    /*notificationAdapter = new NotificationAdapter(mContext, allTags);
                    listNotification.setAdapter(notificationAdapter);*/

                    GetData(mContext);

                    listNotification.setOnScrollListener(new AbsListView.OnScrollListener()
                    {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            // TODO Auto-generated method stub

                            progressStatus = "LOAD MORE";

                            int threshold = 1;
                            int count = listNotification.getCount();

                            if (scrollState == SCROLL_STATE_IDLE)
                            {
                                if (listSize <= numberCount)
                                {
                                    if (listNotification.getLastVisiblePosition() >= count - threshold)
                                    {
                                        rlLoadMore.setVisibility(View.VISIBLE);
                                        // Execute LoadMoreDataTask AsyncTask
                                        new HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
                                    }
                                }
                                else {  }
                            }
                        }
                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem,
                                             int visibleItemCount, int totalItemCount) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Friends..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
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

    public static void GetData(Context context)
    {
        allTagsList.clear();

        for (NotificationModel reTag : allTags)
        {
            NotificationModel nfcModelTag = new NotificationModel();
            nfcModelTag.setFriendUserID(reTag.getFriendUserID());
            nfcModelTag.setFriendProfileID(reTag.getFriendProfileID());
            nfcModelTag.setMyUserID(reTag.getMyUserID());
            nfcModelTag.setMyProfileID(reTag.getMyProfileID());
            nfcModelTag.setFirstName(reTag.getFirstName());
            nfcModelTag.setLastName(reTag.getLastName());
            nfcModelTag.setUserPhoto(reTag.getUserPhoto());
            nfcModelTag.setPurpose(reTag.getPurpose());
            nfcModelTag.setNotificationID(reTag.getNotificationID());
            nfcModelTag.setStatus(reTag.getStatus());
            nfcModelTag.setShared_UserID(reTag.getShared_UserID());
            nfcModelTag.setShared_ProfileID(reTag.getShared_ProfileID());
            nfcModelTag.setViewed_Flag(reTag.getViewed_Flag());
            nfcModelTag.setListCount(reTag.getListCount());
            allTagsList.add(nfcModelTag);
        }

        notificationAdapter = new NotificationAdapter(mContext, allTagsList);
        listNotification.setAdapter(notificationAdapter);
    }

}
