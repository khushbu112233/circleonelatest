package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.TestimonialAdapter;
import com.circle8.circleOne.Adapter.TestimonialCardDetailAdapter;
import com.circle8.circleOne.Model.TestimonialModel;
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

/**
 * Created by admin on 10/07/2017.
 */

public class TestimonialCardDetail extends AppCompatActivity
{
    static String TestimonialProfileId = "";
    static TestimonialCardDetailAdapter adapter;
    static ListView lstTestimonial;
    static ArrayList<TestimonialModel> allTaggs;
    static TextView txtTestimonial;
    static Context mContext ;
    TextView textView;
    ImageView imgLogo, imgDrawer;
    String from = "", type = "";

    public static RelativeLayout rlProgressDialog ;
    public static TextView tvProgressing ;
    public static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial);

        mContext = TestimonialCardDetail.this ;

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;
        txtTestimonial = (TextView) findViewById(R.id.txtTestimonial);
        imgDrawer.setVisibility(View.GONE);
        textView.setText("Testimonial");
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);


        Intent intent = getIntent();
        TestimonialProfileId = intent.getStringExtra("ProfileId");
        lstTestimonial = (ListView) findViewById(R.id.lstTestimonial);

        allTaggs = new ArrayList<>();

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();

            }
        });

        callFirst();
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
        new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL+"Testimonial/Fetch");
    }

    public static void webCall()
    {
        allTaggs.clear();
        adapter.notifyDataSetChanged();
        new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL+"Testimonial/Fetch");
    }

    private static class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            dialog = new ProgressDialog(mContext);
//            dialog.setMessage("Fetching Testimonials...");
//            //dialog.setTitle("Saving Reminder");
//            dialog.show();
//            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Fetching Testimonial" ;
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
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Testimonials");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        TestimonialModel nfcModelTag = new TestimonialModel();
                        nfcModelTag.setTestimonial_ID(object.getString("Testimonial_ID"));
                        nfcModelTag.setCompanyName(object.getString("CompanyName"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setFriendProfileID(object.getString("FriendProfileID"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setPurpose(object.getString("Purpose"));
                        nfcModelTag.setStatus(object.getString("Status"));
                        nfcModelTag.setTestimonial_Text(object.getString("Testimonial_Text"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        allTaggs.add(nfcModelTag);
                    }
                    adapter = new TestimonialCardDetailAdapter(mContext, R.layout.full_testimonial_row, allTaggs);
                    lstTestimonial.setAdapter(adapter);
                    if (allTaggs.size() == 0){
                        txtTestimonial.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtTestimonial.setVisibility(View.GONE);
                    }

                }
                else
                {
                    Toast.makeText(mContext, "Not able to load testimonials..", Toast.LENGTH_LONG).show();
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
            jsonObject.accumulate("ProfileId", TestimonialProfileId );
            jsonObject.accumulate("numofrecords", "10" );
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

