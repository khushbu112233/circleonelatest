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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.TestimonialRequestAdapter;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;

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

public class TestimonialRequest extends AppCompatActivity
{
    static ListView lstTestimonial;
    static Context mContext ;
    static String TestimonialProfileId = "";
    static ArrayList<TestimonialModel> allTaggs;
    TextView textView;
    ImageView imgLogo;

    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial_request);
        mContext = TestimonialRequest.this ;

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        textView.setText("Testimonial");

        Intent intent = getIntent();
        TestimonialProfileId = intent.getStringExtra("ProfileId");
        allTaggs = new ArrayList<>();
        lstTestimonial = (ListView) findViewById(R.id.lstTestimonial);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8999/Onet.svc/GetFriends_Profile");
    }

    private static class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;
        private TestimonialRequestAdapter adapter;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
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
                    String success = jsonObject.getString("success");
                    if (success.equals("1"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("Profiles");
                        //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                        for (int i = 0; i < jsonArray.length(); i++)
                        {

                            JSONObject object = jsonArray.getJSONObject(i);
                            //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                            TestimonialModel nfcModelTag = new TestimonialModel();
                            nfcModelTag.setFriendProfileID(object.getString("Friend_ProfileID"));
                            nfcModelTag.setUserPhoto(object.getString("Friend_Photo"));
                            nfcModelTag.setFirstName(object.getString("Friend_FirstName"));
                            nfcModelTag.setLastName(object.getString("Friend_LastName"));
                            nfcModelTag.setCompanyName(object.getString("Friend_Company"));
                            nfcModelTag.setDesignation(object.getString("Friend_Designation"));
                            nfcModelTag.setPurpose(object.getString("Testimonial_Request_Status"));
                            allTaggs.add(nfcModelTag);
                        }
                        adapter = new TestimonialRequestAdapter(mContext, R.layout.full_testimonial_row, allTaggs);
                        lstTestimonial.setAdapter(adapter);
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST2(String url)
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
            jsonObject.accumulate("numofrecords", "30" );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("profileId", TestimonialProfileId );

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

}
