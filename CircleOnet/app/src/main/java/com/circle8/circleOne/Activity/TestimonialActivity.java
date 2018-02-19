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

import com.circle8.circleOne.Adapter.TestimonialAdapter;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.circle8.circleOne.Utils.Utility.POST2;

public class TestimonialActivity extends AppCompatActivity
{
    static String TestimonialProfileId = "";
    static TestimonialAdapter adapter;
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

        mContext = TestimonialActivity.this ;

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action3);
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
       // imgLogo.setVisibility(View.VISIBLE);

        textView.setText("Testimonial");
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);


        Intent intent = getIntent();
        TestimonialProfileId = intent.getStringExtra("ProfileId");
        from = intent.getStringExtra("from");
        type = intent.getStringExtra("type");
        lstTestimonial = (ListView) findViewById(R.id.lstTestimonial);

        allTaggs = new ArrayList<>();

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (from.equalsIgnoreCase("profile")) {
                    finish();
                } else if (from.equalsIgnoreCase("editprofile")) {
                    /*Intent go = new Intent(getApplicationContext(),EditProfileActivity.class);

                    // you pass the position you want the viewpager to show in the extra,
                    // please don't forget to define and initialize the position variable
                    // properly
                    go.putExtra("profile_id", TestimonialProfileId);
                    go.putExtra("type", type);
                    startActivity(go);*/
                    finish();
                }

            }
        });

        callFirst();
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

            String loading = "Fetching testimonial" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ProfileId", TestimonialProfileId );
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", "1" );

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
                    adapter = new TestimonialAdapter(mContext, R.layout.full_testimonial_row, allTaggs);
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
