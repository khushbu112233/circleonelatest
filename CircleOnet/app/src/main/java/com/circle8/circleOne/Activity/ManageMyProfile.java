package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.NewCardRequestAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Interface.ItemClickProfile;
import com.circle8.circleOne.Interface.ItemLongClickProfile;
import com.circle8.circleOne.Model.NewCardModel;
import com.circle8.circleOne.Model.ProfileModel;
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

public class ManageMyProfile extends AppCompatActivity
{
    private RecyclerView listView ;
    private ArrayList<NewCardModel> newCardModelArrayList ;
    private NewCardRequestAdapter newCardRequestAdapter ;
    public static ArrayList<ProfileModel> allTags ;
    private String UserID = "";
    private LoginSession session;
    ImageView imgBack;
    String profile_id;
    RelativeLayout llBottomAdd;
    String ProfileID = "";

    ItemClickProfile itemClickProfile;
    ItemLongClickProfile itemLongClickProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_profile);

        session = new LoginSession(getApplicationContext());
        listView = (RecyclerView) findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        llBottomAdd = (RelativeLayout) findViewById(R.id.llBottomAdd);
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        allTags = new ArrayList<>();
        newCardModelArrayList = new ArrayList<>();


        itemClickProfile = new ItemClickProfile() {
            @Override
            public void OnItemClickProfile(int position) {


                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("profile_id", allTags.get(position).getProfileID());
                intent.putExtra("activity", "manage");
                startActivity(intent);
            }
        };
        itemLongClickProfile =  new ItemLongClickProfile() {
            @Override
            public void OnItemLongClickProfile(final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        ManageMyProfile.this, R.style.Blue_AlertDialog);
                alert.setMessage("Do you want to delete this profile?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        ProfileID = allTags.get(position).getProfileID();
                        dialog.dismiss();
                        new HttpAsyncTaskProfileDelete().execute(Utility.BASE_URL+"DeleteProfile");

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();


            }
        };


        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
       // new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llBottomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("profile_id", profile_id);
                intent.putExtra("type", "add");
                intent.putExtra("activity", "manage");
                startActivity(intent);
            }
        });

  /*      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("profile_id", allTags.get(position).getProfileID());
                intent.putExtra("activity", "manage");
                startActivity(intent);
            }
        });
*/
       /* listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        ManageMyProfile.this, R.style.Blue_AlertDialog);
                alert.setMessage("Do you want to delete this profile?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        ProfileID = allTags.get(position).getProfileID();
                        dialog.dismiss();
                        new HttpAsyncTaskProfileDelete().execute(Utility.BASE_URL+"DeleteProfile");

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
*/
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


    public  String POST5(String url)
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
            jsonObject.accumulate("numofrecords", "100" );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("userid", UserID);

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

    public  String POST1(String url)
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
            jsonObject.accumulate("ProfileID", ProfileID );
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


    private class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*dialog = new ProgressDialog(ManageMyProfile.this);
            dialog.setMessage("Fetching Profiles...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Fetching profiles" ;
            CustomProgressDialog(loading,ManageMyProfile.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST5(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           dismissProgress();

            try
            {

                if (result != null)
                {
                    allTags.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Profiles");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        ProfileModel nfcModelTag = new ProfileModel();
                        nfcModelTag.setUserID(object.getString("UserID"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setUserName(object.getString("UserName"));
                        nfcModelTag.setProfileID(object.getString("ProfileID"));
                        nfcModelTag.setCard_Front(object.getString("Card_Front"));
                        nfcModelTag.setCard_Back(object.getString("Card_Back"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCompanyName(object.getString("CompanyName"));
                        nfcModelTag.setCompany_Profile(object.getString("Company_Profile"));
                        nfcModelTag.setPhone1(object.getString("Phone1"));
                        nfcModelTag.setPhone2(object.getString("Phone2"));
                        nfcModelTag.setMobile1(object.getString("Mobile1"));
                        nfcModelTag.setMobile2(object.getString("Mobile2"));
                        nfcModelTag.setFax1(object.getString("Fax1"));
                        nfcModelTag.setFax2(object.getString("Fax2"));
                        nfcModelTag.setEmail1(object.getString("Email1"));
                        nfcModelTag.setEmail2(object.getString("Email2"));
                        nfcModelTag.setAddress1(object.getString("Address1"));
                        nfcModelTag.setAddress2(object.getString("Address2"));
                        nfcModelTag.setAddress3(object.getString("Address3"));
                        nfcModelTag.setAddress4(object.getString("Address4"));
                        nfcModelTag.setCity(object.getString("City"));
                        nfcModelTag.setState(object.getString("State"));
                        nfcModelTag.setCountry(object.getString("Country"));
                        nfcModelTag.setPostalcode(object.getString("Postalcode"));
                        nfcModelTag.setWebsite(object.getString("Website"));
                        nfcModelTag.setFacebook(object.getString("Facebook"));
                        nfcModelTag.setTwitter(object.getString("Twitter"));
                        nfcModelTag.setGoogle(object.getString("Google"));
                        nfcModelTag.setLinkedin(object.getString("Linkedin"));
                        nfcModelTag.setYoutube(object.getString("Youtube"));
                        nfcModelTag.setAttachment_FileName(object.getString("Attachment_FileName"));
                        nfcModelTag.setProfile(object.getString("ProfileName"));
                        allTags.add(nfcModelTag);
                        //  GetData(getContext());
                    }

                    profile_id = allTags.get(0).getProfileID();

//                    newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(), allTags);
                    newCardRequestAdapter = new NewCardRequestAdapter(ManageMyProfile.this, R.layout.new_card_request_parameter, allTags);
                    newCardRequestAdapter.onItemclick(itemClickProfile);
                    newCardRequestAdapter.onItemLongClick(itemLongClickProfile);
                    listView.setAdapter(newCardRequestAdapter);
                    newCardRequestAdapter.notifyDataSetChanged();

                   /* ManageMyProfileAdapter manageMyProfileAdapter = new ManageMyProfileAdapter(ManageMyProfile.this, R.layout.new_card_request_parameter, allTags);
                    listView.setAdapter(manageMyProfileAdapter);
                    manageMyProfileAdapter.notifyDataSetChanged();*/
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load profiles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");
    }

    private class HttpAsyncTaskProfileDelete extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(ManageMyProfile.this);
            dialog.setMessage("Deleting Profile...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Deleting profile" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST1(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           dismissProgress();

            try
            {

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equalsIgnoreCase("1")){
                        new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");
                        Toast.makeText(getApplicationContext(), "Profile deleted successfully..", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to delete profile..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
