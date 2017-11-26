package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.TestimonialRequestAdapter;
import com.circle8.circleOne.Fragments.ByAssociationFragment;
import com.circle8.circleOne.Fragments.ByAssociationGroupFragment;
import com.circle8.circleOne.Fragments.ByCompanyFragment;
import com.circle8.circleOne.Fragments.ByCompanyGroupFragment;
import com.circle8.circleOne.Fragments.ByIndustryFragment;
import com.circle8.circleOne.Fragments.ByIndustryGroupFragment;
import com.circle8.circleOne.Fragments.ByNameGroupFragment;
import com.circle8.circleOne.Fragments.ByTitleFragment;
import com.circle8.circleOne.Fragments.ByTitleGroupFragment;
import com.circle8.circleOne.Fragments.ConnectFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.CustomViewPager;
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
import java.util.List;

public class SearchGroupMembers extends AppCompatActivity
{

    public static CustomViewPager mViewPager;
    private ImageView imgBack ;
    private TabLayout tabLayout;
    private TextView txtAdd, mytext;
    public static JSONArray selectedStrings = new JSONArray();
    private String user_id = "";
    private LoginSession loginSession;
    private String GroupId = "", from, ProfileId;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    private String profileId = "";
    private String currentProfile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group_members);

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_group_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        txtAdd = (TextView) findViewById(R.id.mytext1);
        mytext = (TextView) findViewById(R.id.mytext);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        Intent intent = getIntent();
        from = intent.getStringExtra("from");

        if (from.equalsIgnoreCase("group")){
            GroupId = intent.getStringExtra("GroupId");
            mytext.setText("Select user to add in circle");
            txtAdd.setText("Add");
        }
        else if (from.equalsIgnoreCase("profile")) {
            ProfileId = intent.getStringExtra("ProfileId");
            mytext.setText("Select User to send Testimonial Request");
            txtAdd.setText("Send");
        }
        else if (from.equalsIgnoreCase("cardDetail")) {
            ProfileId = intent.getStringExtra("ProfileId");
            mytext.setText("Select User to send Profile Request to share");
            txtAdd.setText("Send");
        }


        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        currentProfile = user.get(LoginSession.KEY_PROFILEID);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);

        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               // Toast.makeText(getApplicationContext(), selectedStrings.toString(), Toast.LENGTH_LONG).show();
                if (from.equalsIgnoreCase("group")){
                    if (selectedStrings == null || selectedStrings.length() <= 0)
                    {
                        Toast.makeText(getApplicationContext(), "You haven’t selected member", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(), selectedStrings.toString(), Toast.LENGTH_LONG).show();
                        new HttpAsyncTaskGroupAddFriend().execute(Utility.BASE_URL+"Group/AddFriend");
                    }
                }
                else if (from.equalsIgnoreCase("profile")) {
                    if (selectedStrings == null || selectedStrings.length() <= 0)
                    {
                        Toast.makeText(getApplicationContext(), "You haven’t selected member", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        new HttpAsyncTaskTestimonialRequest().execute(Utility.BASE_URL+"Testimonial/Request");
                    }
                }
                else if (from.equalsIgnoreCase("cardDetail")) {
                    if (selectedStrings == null || selectedStrings.length() <= 0)
                    {
                        Toast.makeText(getApplicationContext(), "You haven’t selected member", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        new HttpAsyncTaskShareRequest().execute(Utility.BASE_URL+"ShareProfile/Request");
                    }
                }

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
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

    public String POST2(String url)
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
            jsonObject.accumulate("friendprofileID",  selectedStrings);
            jsonObject.accumulate("myprofileID", ProfileId );

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

    public String POST3(String url)
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
            jsonObject.accumulate("MyFriendProfileIDs",  selectedStrings);
            jsonObject.accumulate("MyProfileID", currentProfile );
            jsonObject.accumulate("ShareProfileID", ProfileId );

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

    private class HttpAsyncTaskTestimonialRequest extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;
        private TestimonialRequestAdapter adapter;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(context);
            dialog.setMessage("Requesting...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Requesting";
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
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("Success");
                    String message = jsonObject.getString("Message");

                    if (success.equals("1"))
                    {
                        Toast.makeText(SearchGroupMembers.this, "Request sent..", Toast.LENGTH_LONG).show();
                      //  txtRequest.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Already Requested for Testimonial..", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Request has not been sent..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskShareRequest extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;
        private TestimonialRequestAdapter adapter;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(context);
            dialog.setMessage("Requesting...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Requesting";
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST3(urls[0]);
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
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1"))
                    {
                        Toast.makeText(SearchGroupMembers.this, "Profile Share Request sent..", Toast.LENGTH_LONG).show();
                        //  txtRequest.setVisibility(View.GONE);
                    }
                    else if (success.equals("-1"))
                    {
                        Toast.makeText(SearchGroupMembers.this, "Profile share request already sent..", Toast.LENGTH_LONG).show();
                        //  txtRequest.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Profile share request already sent..", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Request has not been sent..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
      /*  adapter.addFrag(new ConnectListFragment(), "by Name");
        adapter.addFrag(new ConnectListFragment(), "by Company Name");
        adapter.addFrag(new ConnectListFragment(), "by Title");
        adapter.addFrag(new ConnectListFragment(), "by Industry");
        adapter.addFrag(new ConnectListFragment(), "by Association");*/
        adapter.addFrag(new ByNameGroupFragment(), "by Name");
        adapter.addFrag(new ByCompanyGroupFragment(), "by Company Name");
        adapter.addFrag(new ByTitleGroupFragment(), "by Title");
        adapter.addFrag(new ByIndustryGroupFragment(), "by Industry");
        adapter.addFrag(new ByAssociationGroupFragment(), "by Association");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                return new ByNameGroupFragment();
            }
            else if (position == 1) {
                return new ByCompanyGroupFragment();
            }
            else if (position == 2) {
                return new ByTitleGroupFragment();
            }
            else if (position == 3) {
                return new ByIndustryGroupFragment();
            }
            else if (position == 4) {
                return new ByAssociationGroupFragment();
            }
            else
            {
                return new ByNameGroupFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return getString(R.string.app_name);
                case 1:
                    return getString(R.string.hello_blank_fragment);
            }*/
            return null;
        }
    }

    public String POST5(String url) {
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
            jsonObject.accumulate("GroupID", GroupId);
            jsonObject.accumulate("UserID", user_id);
            jsonObject.accumulate("myFriendProfileIds", selectedStrings);

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

    private class HttpAsyncTaskGroupAddFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(SearchGroupMembers.this);
            dialog.setMessage("Adding Friend...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Adding Friend" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST5(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
          //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success");
                    String Message = jsonObject.getString("Message");
                    if (Success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Member added in Circle", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Add Friend in circle", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
