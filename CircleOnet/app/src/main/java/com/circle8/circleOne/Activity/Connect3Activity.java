package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.Level7thConnectionModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityConnect3Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.POST2;

public class Connect3Activity extends AppCompatActivity
{
    String profileImg, profileName;
    LoginSession loginSession ;
    ArrayList<Level7thConnectionModel> allTags;
    String backStatus = "None",connectLevel = "",level = "",UserId = "", friendUserID = "",userName1 = "", userName2 = "", userName3 = "", userName4 = "", userName5 = "", userName6 = "", userName7 = "",userPhoto1 = "", userPhoto2 = "", userPhoto3 = "", userPhoto4 = "", userPhoto5 = "", userPhoto6 = "", userPhoto7 = "" ,userProfileId1 = "", userProfileId2 = "", userProfileId3 = "", userProfileId4 = "", userProfileId5 = "",userProfileId6 = "", userProfileId7 = "";
    ActivityConnect3Binding activityConnect3Binding;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityConnect3Binding  = DataBindingUtil.setContentView(this,R.layout.activity_connect3);

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);      // name
        activity = this;
        Intent intent = getIntent();
        profileImg = intent.getStringExtra("profile");
        friendUserID = intent.getStringExtra("friendUserID");
        profileName = intent.getStringExtra("profileName");
        allTags = new ArrayList<>();

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        activityConnect3Binding.imgConnecting.startAnimation(anim);

        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        activityConnect3Binding.imgConnecting1.startAnimation(anim1);

        activityConnect3Binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                backStatus = "Back";
                finish();
            }
        });

        if (backStatus.equalsIgnoreCase("None"))
        {
            Utility.freeMemory();
            new HttpAsyncTask().execute(Utility.BASE_URL+"Connection7Level");
        }

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("userId_dest", friendUserID );
                jsonObject.accumulate("userId_src", UserId );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
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
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load friends..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
