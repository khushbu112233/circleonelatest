package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.NewCardRequestAdapter1;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.NewCardModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class NewCardRequestActivity extends AppCompatActivity
{
    private ListView listView ;
    private ArrayList<NewCardModel> newCardModelArrayList ;
    private NewCardRequestAdapter1 newCardRequestAdapter ;
    public static ArrayList<ProfileModel> allTags ;
    private String UserID = "";
    private LoginSession session;
    ImageView imgBack;

    public static RelativeLayout rlLayOne, rlLayTwo ;
    public static TextView tvNext, tvCancel ;
    public static ImageView ivAlphaImg ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request);
        session = new LoginSession(getApplicationContext());
        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        rlLayOne = (RelativeLayout) findViewById(R.id.rlLayOne);
        rlLayTwo = (RelativeLayout) findViewById(R.id.rlLayTwo);
        tvNext = (TextView)findViewById(R.id.tvNext);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        ivAlphaImg = (ImageView)findViewById(R.id.ivAlphaImg);

        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        allTags = new ArrayList<>();
        newCardModelArrayList = new ArrayList<>();
        new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(), newCardModelArrayList);
      /*  newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(),
                name,company,designation,email,phone,profile,image);
        listView.setAdapter(newCardRequestAdapter);
        newCardRequestAdapter.notifyDataSetChanged();

*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                listView.setEnabled(false);
                ivAlphaImg.setVisibility(View.VISIBLE);
                rlLayTwo.setVisibility(View.VISIBLE);

                tvNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);
                        listView.setEnabled(true);

                        int count = position + 1;
                        Intent i = new Intent(getApplicationContext(), NewCardRequestDetailActivity.class);
                        i.putExtra("person", allTags.get(position).getFirstName() + " " + allTags.get(position).getLastName());
                        i.putExtra("designation", allTags.get(position).getDesignation());
                        i.putExtra("company", allTags.get(position).getCompanyName());
                        i.putExtra("profile", allTags.get(position).getProfile());
                        i.putExtra("image", allTags.get(position).getUserPhoto());
                        i.putExtra("phone", allTags.get(position).getMobile1());
                        i.putExtra("profileID", allTags.get(position).getProfileID());
                        i.putExtra("Card_Front", allTags.get(position).getCard_Front());
                        i.putExtra("Card_Back", allTags.get(position).getCard_Back());
                        i.putExtra("address", allTags.get(position).getAddress1()
                                + " " + allTags.get(position).getAddress3() + " " + allTags.get(position).getAddress4()
                                + " " + allTags.get(position).getCity() + " " + allTags.get(position).getState() + " " + allTags.get(position).getState()
                                + " " + allTags.get(position).getCountry() + " " + allTags.get(position).getPostalcode());
                        startActivity(i);
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);
                        listView.setEnabled(true);
                    }
                });

               /* int count = position + 1;
                Intent i = new Intent(getApplicationContext(), NewCardRequestDetailActivity.class);
                i.putExtra("person", allTags.get(position).getFirstName() + " " + allTags.get(position).getLastName());
                i.putExtra("designation", allTags.get(position).getDesignation());
                i.putExtra("company", allTags.get(position).getCompanyName());
                i.putExtra("profile", allTags.get(position).getProfile());
                i.putExtra("image", allTags.get(position).getUserPhoto());
                i.putExtra("phone", allTags.get(position).getPhone1());
                i.putExtra("profileID", allTags.get(position).getProfileID());
                i.putExtra("Card_Front", allTags.get(position).getCard_Front());
                i.putExtra("Card_Back", allTags.get(position).getCard_Back());
                startActivity(i);*/
            }
        });

    }

    private class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Fetching profiles" ;
            CustomProgressDialog(loading, NewCardRequestActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("numofrecords", "100" );
                jsonObject.accumulate("pageno", "1" );
                jsonObject.accumulate("userid", UserID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
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

                    newCardRequestAdapter = new NewCardRequestAdapter1(NewCardRequestActivity.this, R.layout.new_card_request_parameter, allTags);
                    listView.setAdapter(newCardRequestAdapter);

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


}
