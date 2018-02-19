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
import com.circle8.circleOne.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class TestimonialRequest extends AppCompatActivity
{
    static ListView lstTestimonial;
    static Context mContext ;
    static String TestimonialProfileId = "";
    static ArrayList<TestimonialModel> allTaggs;
    TextView textView;
    ImageView imgLogo, imgMenu;

    public static RelativeLayout rlProgressDialog ;
    public static TextView tvProgressing ;
    public static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

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
        imgMenu = (ImageView)findViewById(R.id.drawer);
        imgMenu.setVisibility(View.GONE);
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

        new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL+"GetFriends_Profile");
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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("SearchType", "local" );
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", "1" );
                jsonObject.accumulate("profileId", TestimonialProfileId );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }
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

    public static void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading+"...");

        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);


    }

}
