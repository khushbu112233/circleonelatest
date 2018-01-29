package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.HistoryAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.HistoryModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class HistoryActivity extends AppCompatActivity
{
    LoginSession session;
    String user_id ;
    ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    HistoryAdapter historyAdapter ;
    ActivityHistoryBinding activityHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_history);
        Utility.freeMemory();
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        new HttpAsyncTaskHistoryList().execute(Utility.BASE_URL+"History");

        activityHistoryBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
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
            String loading = "History" ;
            CustomProgressDialog(loading, HistoryActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("UserId",user_id);
                jsonObject.accumulate("numofrecords", "100");
                jsonObject.accumulate("pageno", "1" );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                        activityHistoryBinding.tvHistoryInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Utility.freeMemory();
                        activityHistoryBinding.tvHistoryInfo.setVisibility(View.GONE);

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
                            activityHistoryBinding.listView.setAdapter(historyAdapter);
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
}
