package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.HistoryAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.HistoryModel;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class HistoryActivity extends AppCompatActivity
{
    private ListView listView;
    private TextView tvHistoryInfo ;
    private ImageView imgBack ;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    LoginSession session;
    String user_id ;

    ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    HistoryAdapter historyAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Utility.freeMemory();
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

        listView = (ListView)findViewById(R.id.listView);
        tvHistoryInfo = (TextView)findViewById(R.id.tvHistoryInfo);
        imgBack = (ImageView)findViewById(R.id.imgBack);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        new HttpAsyncTaskHistoryList().execute(Utility.BASE_URL+"History");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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


    @Override
    public void onBackPressed() {
        finish();
    }

    private class HttpAsyncTaskHistoryList extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Finding Events...");
            dialog.show();*/

            String loading = "History" ;
            CustomProgressDialog(loading, HistoryActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return HistoryPost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check data connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String count = response.getString("Total_Count");
                    String pageno = response.getString("pageno");
                    String numofrecords = response.getString("numofrecords");

                    JSONArray historyList = response.getJSONArray("History_List");

                    if(historyList.length() == 0)
                    {
                        tvHistoryInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Utility.freeMemory();
                        tvHistoryInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= historyList.length() ; i++ )
                        {
                            JSONObject hList = historyList.getJSONObject(i);

                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setMyUserID(hList.getString("MyUserID"));
                            historyModel.setMyProfileID(hList.getString("MyProfileID"));
                            historyModel.setFriendUserID(hList.getString("FriendUserID"));
                            historyModel.setFriendProfileID(hList.getString("FriendProfileID"));
                            historyModel.setHistoryID(hList.getString("HistoryID"));
                            historyModel.setHistory_Type(hList.getString("History_Type"));
                            historyModel.setHistory_Status(hList.getString("History_Status"));
                            historyModel.setHistory_Date(hList.getString("History_Date"));
                            historyModel.setFirstName(hList.getString("FirstName"));
                            historyModel.setLastName(hList.getString("LastName"));
                            historyModel.setUserPhoto(hList.getString("UserPhoto"));
                            historyModelArrayList.add(historyModel);

                            historyAdapter = new HistoryAdapter(HistoryActivity.this, historyModelArrayList);
                            listView.setAdapter(historyAdapter);
                            historyAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public  String HistoryPost(String url)
    {
        Utility.freeMemory();
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
            jsonObject.accumulate("UserId",user_id);
            jsonObject.accumulate("numofrecords", "100");
            jsonObject.accumulate("pageno", "1" );

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
}
