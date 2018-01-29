package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Utils.Utility.POST2;

public class WriteTestimonialActivity extends AppCompatActivity implements View.OnClickListener
{
    private CircleImageView imgProfile ;
    private ImageView imgBack ;
    private TextView tvUserName, tvSend, tvCancel ;
    private EditText etTestimonial1, etTestimonial2, etTestimonial3,
            etTestimonial4, etTestimonial5, etTestimonial6 ;
    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    String testimonial_String = "";
    String userImg = "", userName = "", profileId = "", friendProfileId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_testimonial);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        imgBack = (ImageView)findViewById(R.id.imgBack);

        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvSend = (TextView)findViewById(R.id.tvSend);
        tvCancel = (TextView)findViewById(R.id.tvCancel);

        etTestimonial1 = (EditText)findViewById(R.id.etTestimonial1);
        etTestimonial2 = (EditText)findViewById(R.id.etTestimonial2);
        etTestimonial3 = (EditText)findViewById(R.id.etTestimonial3);
        etTestimonial4 = (EditText)findViewById(R.id.etTestimonial4);
        etTestimonial5 = (EditText)findViewById(R.id.etTestimonial5);
        etTestimonial6 = (EditText)findViewById(R.id.etTestimonial6);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        Intent in = getIntent();
        userName = in.getStringExtra("UserName");
        userImg = in.getStringExtra("UserImg");
        profileId = in.getStringExtra("ProfileID");
        friendProfileId = in.getStringExtra("FriendProfileID");

        tvUserName.setText(userName);

        if (userImg.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr);
        }
        else
        {
            Picasso.with(WriteTestimonialActivity.this).load(Utility.BASE_IMAGE_URL+"UserProfile/" +userImg).resize(300,300).onlyScaleDown().skipMemoryCache().into(imgProfile);
        }
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
    public void onClick(View v)
    {
        if( v == tvSend)
        {
            testimonial_String = etTestimonial1.getText().toString()+" "+etTestimonial2.getText().toString()+" "+
                    etTestimonial3.getText().toString()+" "+etTestimonial4.getText().toString()+" "+
                    etTestimonial5.getText().toString()+" "+etTestimonial6.getText().toString();
            String testimonial1 = etTestimonial1.getText().toString();
            String testimonial2 = etTestimonial2.getText().toString();
            String testimonial3 = etTestimonial3.getText().toString();

            if (testimonial_String.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please write testimonial",Toast.LENGTH_SHORT).show();
            }
            else if (testimonial_String.length() > 100)
            {
                Toast.makeText(getApplicationContext(),"Please write Testimonial less than 100 words.",Toast.LENGTH_SHORT).show();
            }
            else
            {
//                Toast.makeText(getApplicationContext(),"Written Testimonial",Toast.LENGTH_SHORT).show();
                new HttpAsyncWriteTextimonial().execute(Utility.BASE_URL+"Testimonial/Write");
            }

        }
        if( v == tvCancel)
        {
            finish();
        }
        if ( v == imgBack)
        {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class HttpAsyncWriteTextimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Writing Testimonial..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Writing testimonial";
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Testimonial_text", testimonial_String);
                jsonObject.accumulate("friendprofileID", friendProfileId);
                jsonObject.accumulate("myprofileID", profileId);

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
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Server connection error", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("TestimonialId");
                    String success = response.getString("Success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Successfully testimonial has been written.", Toast.LENGTH_SHORT).show();
                        Notification.webCall();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public  void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
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
