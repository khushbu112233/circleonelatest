package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.NewCardRequestAdapter;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.NewCardModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

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

public class NewCardRequestActivity extends AppCompatActivity
{
    private ListView listView ;
    private ArrayList<NewCardModel> newCardModelArrayList ;
    private NewCardRequestAdapter newCardRequestAdapter ;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> image = new ArrayList<>();
    private ArrayList<String> profile = new ArrayList<>();
    public static ArrayList<ProfileModel> allTags ;
    private String UserID = "";
    private LoginSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request);
        session = new LoginSession(getApplicationContext());
        listView = (ListView)findViewById(R.id.listView);
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        allTags = new ArrayList<>();
        newCardModelArrayList = new ArrayList<>();
        new HttpAsyncTaskProfiles().execute("http://circle8.asia:8081/Onet.svc/MyProfiles");


        NewCardModel newCardModel = new NewCardModel();

        newCardModel.setPersonName("Wesley Wen");
        newCardModel.setPersonDesignation("Business Development Director");
        newCardModel.setPersonCompany("UNICO Creative Pvt. Ltd.");
        newCardModel.setPersonPhone("+65 6842 6188  +65 9735 4641");
        newCardModel.setPersonEmail("wesley.wan@unico-creative.com");
        newCardModel.setPersonProfile("Profile 1");
        newCardModel.setPersonImage(String.valueOf(R.drawable.final_profile5));
        newCardModelArrayList.add(newCardModel);

        newCardModel.setPersonName("Shani Shah");
        newCardModel.setPersonDesignation("Business Development Director");
        newCardModel.setPersonCompany("Ample Arch Pvt. Ltd.");
        newCardModel.setPersonPhone("+65 6842 6188  +65 9735 4641");
        newCardModel.setPersonEmail("shani.shah@unico-creative.com");
        newCardModel.setPersonProfile("Profile 1");
        newCardModel.setPersonImage(String.valueOf(R.drawable.final_profile14));
        newCardModelArrayList.add(newCardModel);

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 1");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 2");
        image.add(String.valueOf(R.drawable.final_profile14));

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 3");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 4");
        image.add(String.valueOf(R.drawable.final_profile14));

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 5");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 6");
        image.add(String.valueOf(R.drawable.final_profile14));


//        newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(), newCardModelArrayList);
      /*  newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(),
                name,company,designation,email,phone,profile,image);
        listView.setAdapter(newCardRequestAdapter);
        newCardRequestAdapter.notifyDataSetChanged();

*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int count = position + 1;
                Intent i = new Intent(getApplicationContext(), NewCardRequestDetailActivity.class);
                i.putExtra("person", allTags.get(position).getFirstName() + " " + allTags.get(position).getLastName());
                i.putExtra("designation", allTags.get(position).getDesignation());
                i.putExtra("company", allTags.get(position).getCompanyName());
                i.putExtra("profile", "Profile "+count);
                i.putExtra("image", allTags.get(position).getUserPhoto());
                i.putExtra("phone", allTags.get(position).getPhone1());
                startActivity(i);
            }
        });
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
            jsonObject.accumulate("numofrecords", "10" );
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewCardRequestActivity.this);
            dialog.setMessage("Fetching Profiles...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
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
            dialog.dismiss();
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
                        allTags.add(nfcModelTag);
                        //  GetData(getContext());
                    }

                    newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(),
                            allTags);
                    listView.setAdapter(newCardRequestAdapter);
                    newCardRequestAdapter.notifyDataSetChanged();



                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Profiles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
