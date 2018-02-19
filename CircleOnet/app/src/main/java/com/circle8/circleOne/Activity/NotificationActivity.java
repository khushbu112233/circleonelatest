package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.NotificationAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.NotificationModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityNotificationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.POST2;

public class NotificationActivity extends AppCompatActivity
{
    static ListView listNotification;
    LoginSession loginSession;
    static String UserId = "";
    static ArrayList<NotificationModel> allTags = new ArrayList<>();
    static NotificationAdapter notificationAdapter;
    ImageView imgLogo;
    public static Context mContext ;
    static ArrayList<NotificationModel> allTagsList = new ArrayList<>();
    static int numberCount, listSize;
    public static int pageno = 1 ;
    static String counts = "0" ;
    public static String progressStatus = "FIRST";
    public static String comeFirst = "Yes" ;
    public static ActivityNotificationBinding activityNotificationBinding;
    View view;
    static TextView textView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        view = activityNotificationBinding.getRoot();

        mContext = NotificationActivity.this ;
        pageno = 1;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setActionBarTitle("Notifications - 0", false);
       // DashboardActivity.setDrawerVisibility(false);

        listNotification = (ListView) view.findViewById(R.id.listNotification);

        loginSession = new LoginSession(mContext);
        HashMap<String, String> user = loginSession.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);

        callFirst();
    }
    public static void setActionBarTitle(String title, Boolean infinity) {
        textView.setText(title);

        if (infinity == true){
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_infinity_sv, 0);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }
    }


    private void callFirst()
    {
        new HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
    }

    public static void webCall()
    {
        pageno = 1;
        allTags.clear();
        allTagsList.clear();
        listNotification.setStackFromBottom(false);
        new HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", pageno );
                jsonObject.accumulate("userid", UserId );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);
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
                        setActionBarTitle("Notifications - "+jsonObject.getString("Count"), false);
                    }

                    if (counts.equals("0") || counts == null)
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(counts);
                    }

                    activityNotificationBinding.rlLoadMore.setVisibility(View.GONE);

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

                            if (listSize > 10)
                            {
                                listNotification.setStackFromBottom(true);
                            }

                            int threshold = 1;
                            int count = listNotification.getCount();

                            if (scrollState == SCROLL_STATE_IDLE)
                            {
                                if (listSize <= numberCount)
                                {
                                    if (listNotification.getLastVisiblePosition() >= count - threshold)
                                    {
                                        // rlLoadMore.setVisibility(View.VISIBLE);
                                        // Execute LoadMoreDataTask AsyncTask
                                        new NotificationActivity.HttpAsyncTask().execute(Utility.BASE_URL+"Notification");
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
                    Toast.makeText(mContext, "Not able to load notifications..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void CustomProgressDialog(final String loading)
    {
        activityNotificationBinding.rlProgressDialog.setVisibility(View.VISIBLE);
        activityNotificationBinding.txtProgressing.setText(loading+"...");

        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        activityNotificationBinding.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        activityNotificationBinding.imgConnecting2.startAnimation(anim1);

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
        notificationAdapter.notifyDataSetChanged();
    }

}
