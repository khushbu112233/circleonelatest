package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.MyAccountBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;
import static com.circle8.circleOne.Utils.Validation.updateRegisterValidate;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener
{

    private String gender = "", final_ImgBase64 = "", Image = "";
    private ImageView imgBack;
    private CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String image;
    private ProgressDialog pDialog;
    private String  first_name, last_name, phone_no, password, c_password ;
      private LoginSession session;
    private String user_id, email_id, user_img, user_pass, user_Gender, user_Photo ;
    private String encodedImageData, register_img;

    int motionLength;
    int roundWidth = 0, lineWidth = 0;
    String user_name, dob, profile_id;
    String Connection_Limit, Connection_Left;
    String Q_ID = "";
    String editStatus = "None";

    boolean profilePicPress = false ;
    TextView tvReferral;
    ReferralCodeSession referralCodeSession;
    public static MyAccountBinding myAccountBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        myAccountBinding = DataBindingUtil.setContentView(this,R.layout.my_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        myAccountBinding.txtedit.setVisibility(View.VISIBLE);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        email_id = user.get(LoginSession.KEY_EMAIL);
        user_img = user.get(LoginSession.KEY_IMAGE);
        user_pass = user.get(LoginSession.KEY_PASSWORD);
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        Connection_Limit = user.get(LoginSession.KEY_CONNECTION_LIMIT);
        Connection_Left = user.get(LoginSession.KEY_CONNECTION_LEFT);
        Q_ID = user.get(LoginSession.KEY_QID);

        tvReferral = (TextView)findViewById(R.id.tvReferral);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        pDialog = new ProgressDialog(this);
        myAccountBinding.etUserName.setText(email_id);

        myAccountBinding.etFirstName.setVisibility(View.VISIBLE);
        myAccountBinding.tvFirstName.setVisibility(View.GONE);
        myAccountBinding.etLastName.setVisibility(View.VISIBLE);
        myAccountBinding.tvLastName.setVisibility(View.GONE);
        myAccountBinding.etPassword.setVisibility(View.VISIBLE);
        myAccountBinding.tvPassword.setVisibility(View.GONE);
        myAccountBinding.etPasswordAgain.setVisibility(View.VISIBLE);
        myAccountBinding.tvRePassword.setVisibility(View.GONE);
        myAccountBinding.etPhone.setVisibility(View.VISIBLE);
        myAccountBinding.tvPhone.setVisibility(View.GONE);
        myAccountBinding.etDD.setVisibility(View.VISIBLE);
        myAccountBinding.tvDate.setVisibility(View.GONE);
        myAccountBinding.etMM.setVisibility(View.VISIBLE);
        myAccountBinding.tvMonth.setVisibility(View.GONE);
        myAccountBinding.etYYYY.setVisibility(View.VISIBLE);
        myAccountBinding.tvYear.setVisibility(View.GONE);

       // new HttpAsyncTaskFetchLoginData().execute("http://circle8.asia:8999/Onet.svc/UserLogin");

        myAccountBinding.etUserName.setText(user.get(LoginSession.KEY_EMAIL));
        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        tvReferral.setText(referral.get(ReferralCodeSession.KEY_REFERRAL));
        try {
            String str[] = user.get(LoginSession.KEY_DOB).split("/");
            String day = str[0];
            String month = str[1];
            String year = str[2];
            myAccountBinding.etDD.setText(day);
            myAccountBinding.etMM.setText(month);
            myAccountBinding.etYYYY.setText(year);

            myAccountBinding.tvDate.setText(day);
            myAccountBinding.tvMonth.setText(month);
            myAccountBinding.tvYear.setText(year);

        }
        catch (Exception e){}

        String name1 = user.get(LoginSession.KEY_NAME);
        String kept1 = name1.substring(0, name1.indexOf(" "));
        String remainder1 = name1.substring(name1.indexOf(" ") + 1, name1.length());

        myAccountBinding.etFirstName.setText(kept1);
        myAccountBinding.etLastName.setText(remainder1);

        myAccountBinding.tvFirstName.setText(kept1);
        myAccountBinding.tvLastName.setText(remainder1);

        try
        {
            if (user.get(LoginSession.KEY_PHONE).contains(" ")){
                String name = user.get(LoginSession.KEY_PHONE);
                String kept = name.substring(0, name.indexOf(" "));
                String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                kept = kept.replaceAll("//+", "");
                myAccountBinding.ccp.setCountryForPhoneCode(Integer.parseInt(kept));
                myAccountBinding.etPhone.setText(remainder);
                myAccountBinding.tvPhone.setText(remainder);
            }
            else {
                myAccountBinding.etPhone.setText(user.get(LoginSession.KEY_PHONE));
                myAccountBinding.tvPhone.setText(user.get(LoginSession.KEY_PHONE));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //etPhone.setText(profile.getString("Phone"));

        try
        {
            myAccountBinding.etPassword.setText(user_pass);
            myAccountBinding.etPasswordAgain.setText(user_pass);
            myAccountBinding.tvPassword.setText(user_pass);
            myAccountBinding.tvRePassword.setText(user_pass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            user_Photo = user.get(LoginSession.KEY_IMAGE);
            if (user_Photo.equals(""))
            {
                myAccountBinding.progressBar1.setVisibility(View.GONE);
                myAccountBinding.imgProfile.setImageResource(R.drawable.usr_white1);
            }
            else
            {
                myAccountBinding.progressBar1.setVisibility(View.VISIBLE);
               // Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+user_Photo).resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(imgProfile);

                Glide.with(this).load(Utility.BASE_IMAGE_URL+"UserProfile/"+user_Photo)
                        .asBitmap()
                        .override(300,300)
                        .into(new BitmapImageViewTarget(myAccountBinding.imgProfile) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                myAccountBinding.progressBar1.setVisibility(View.GONE);
                                myAccountBinding.imgProfile.setImageBitmap(drawable);
                            }
                        });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            user_Gender = user.get(LoginSession.KEY_GENDER);

            if (user_Gender.equals("M"))
            {
                            /*//first things
                            myAccountBinding.vwDrag2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                            ivFemaleround.setImageResource(R.drawable.round_gray);
                            //second things
                            myAccountBinding.vwDrag1.setBackground(getResources().getDrawable(R.drawable.dotted));
                            ivMaleImg.setImageResource(R.drawable.ic_male);
                            ivMaleRound.setImageResource(R.drawable.round_blue);
                            gender = "M";
                            txtGender.setText(R.string.male);*/
                myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.ivMaleRound.setImageResource(R.drawable.ic_man_blue);
                myAccountBinding.ivFemaleround.setImageResource(R.drawable.ic_girl_gray);
                myAccountBinding.ivConnectImg.setVisibility(View.INVISIBLE);
                //second things
                myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                gender = "M";
                myAccountBinding.txtGender.setText("Gender: Male");
            }
            else if (user_Gender.equals("F"))
            {
                            /*//first things
                            myAccountBinding.vwDrag1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                            ivMaleRound.setImageResource(R.drawable.round_gray);
                            //second things
                            myAccountBinding.vwDrag2.setBackground(getResources().getDrawable(R.drawable.dotted));
                            ivFemaleImg.setImageResource(R.drawable.ic_female);
                            ivFemaleround.setImageResource(R.drawable.round_blue);
                            gender = "F";
                            txtGender.setText(R.string.female);*/
                myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.ivFemaleround.setImageResource(R.drawable.ic_girl_blue);
                myAccountBinding.ivConnectImg.setVisibility(View.INVISIBLE);
                myAccountBinding.ivMaleRound.setImageResource(R.drawable.ic_man_gray);

                myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                gender = "F";
                myAccountBinding.txtGender.setText("Gender: Female");
            }
            else
            {

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        new HttpAsyncTaskProfiles().execute("http://circle8.asia:8999/Onet.svc/MyProfiles");

      /*  if (user_img.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+user_img).placeholder(R.drawable.usr_1).into(imgProfile);
        }*/

       /* Uri targetUri = Uri.parse(Image);
        try
        {
            if(!Image.equals(""))
            {
                Glide.with(getApplicationContext())
                        .load(targetUri)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(imgProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                super.setResource(resource);
                                final_ImgBase64 = BitMapToString(resource);
                                // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                            }
                        });
            }
            else
            {
                imgProfile.setImageResource(R.drawable.usr);
            }
        }
        catch (Exception e)
        {
            imgProfile.setImageResource(R.drawable.usr);
        }*/

        myAccountBinding.ivFemaleround.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = myAccountBinding.ivFemaleround.getHeight();
                int width = myAccountBinding.ivFemaleround.getWidth();
                int L = myAccountBinding.ivFemaleround.getLeft();
                int T = myAccountBinding.ivFemaleround.getTop();
                int R = myAccountBinding.ivFemaleround.getRight();
                int B = myAccountBinding.ivFemaleround.getBottom();

                roundWidth = width / 2;
                motionLength = motionLength + roundWidth;
                System.out.print("ivFemale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
                //  Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                myAccountBinding.ivFemaleround.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        myAccountBinding.ivMaleRound.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = myAccountBinding.ivMaleRound.getHeight();
                int width = myAccountBinding.ivMaleRound.getWidth();
                int L = myAccountBinding.ivMaleRound.getLeft();
                int T = myAccountBinding.ivMaleRound.getTop();
                int R = myAccountBinding.ivMaleRound.getRight();
                int B = myAccountBinding.ivMaleRound.getBottom();

                System.out.print("ivMale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                myAccountBinding.ivMaleRound.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        myAccountBinding.ivConnectImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = myAccountBinding.ivConnectImg.getHeight();
                int width = myAccountBinding.ivConnectImg.getWidth();
                int L = myAccountBinding.ivConnectImg.getLeft();
                int T = myAccountBinding.ivConnectImg.getTop();
                int R = myAccountBinding.ivConnectImg.getRight();
                int B = myAccountBinding.ivConnectImg.getBottom();
                lineWidth = width;
                motionLength = motionLength + lineWidth;
                System.out.print("ivConnect" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                myAccountBinding.ivConnectImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        myAccountBinding.tvSave.setOnClickListener(this);
        myAccountBinding.tvCancel.setOnClickListener(this);
        myAccountBinding.rlMale.setOnClickListener(this);
        myAccountBinding.rlFemale.setOnClickListener(this);

        imgBack.setOnClickListener(this);
        myAccountBinding.ivEditImg.setOnClickListener(this);
        myAccountBinding.imgProfile.setOnClickListener(this);

        myAccountBinding.etFirstName.setOnClickListener(this);

        myAccountBinding.rlFemale.setEnabled(false);
        myAccountBinding.rlMale.setEnabled(false);
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
    protected void onResume()
    {
        super.onResume();
        myAccountBinding.txtedit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        editStatus = "None";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
        if ( v == myAccountBinding.imgProfile)
        {

            if (profilePicPress)
            {
                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MyAccountActivity.this);
                return;
            }


          /*  final AlertDialog alertDialog = new AlertDialog.Builder(MyAccountActivity.this).create();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.imageview_popup, null);

            ImageView ivViewImage = (ImageView)dialogView.findViewById(R.id.ivViewImage);

            if (user_Photo.equals(""))
            {
                ivViewImage.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+user_Photo).placeholder(R.drawable.usr_1).into(ivViewImage);
            }

            alertDialog.setView(dialogView);
            alertDialog.show();*/

            /*final Dialog dialog = new Dialog(MyAccountActivity.this);
            dialog.setContentView(R.layout.imageview_popup);

            ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
            if (user_Photo.equals(""))
            {
                ivViewImage.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+user_Photo).placeholder(R.drawable.usr_1).into(ivViewImage);
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ivViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), ImageZoom.class);
                    intent.putExtra("displayProfile", "http://circle8.asia/App_ImgLib/UserProfile/"+user_Photo);
                    startActivity(intent);
                }
            });

            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            wmlp.y = 400;   //y position
            dialog.show();
*/
        }
        if ( v == myAccountBinding.ivEditImg)
        {
           // Toast.makeText(getApplicationContext(), "Now you can edit your information", Toast.LENGTH_LONG).show();
            myAccountBinding.etFirstName.setEnabled(true);
            myAccountBinding.etFirstName.requestFocus();
            myAccountBinding.etLastName.setEnabled(true);
            myAccountBinding.etPassword.setEnabled(true);
            myAccountBinding.etPasswordAgain.setEnabled(true);
            myAccountBinding.etPhone.setEnabled(true);
            myAccountBinding.etDD.setEnabled(true);
            myAccountBinding.etMM.setEnabled(true);
            myAccountBinding.etYYYY.setEnabled(true);
//            imgProfile.setEnabled(true);
            myAccountBinding.rltGender.setEnabled(true);
            myAccountBinding.ivMiniCamera.setVisibility(View.VISIBLE);

            profilePicPress = true ;

/*
            etFirstName.setVisibility(View.VISIBLE);
            tvFirstName.setVisibility(View.GONE);
            etLastName.setVisibility(View.VISIBLE);
            tvLastName.setVisibility(View.GONE);
            etPassword.setVisibility(View.VISIBLE);
            tvPassword.setVisibility(View.GONE);
            etPasswordAgain.setVisibility(View.VISIBLE);
            tvPasswordAgain.setVisibility(View.GONE);
            etPhone.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.GONE);
            etDD.setVisibility(View.VISIBLE);
            tvDD.setVisibility(View.GONE);
            etMM.setVisibility(View.VISIBLE);
            tvMM.setVisibility(View.GONE);
            etYYYY.setVisibility(View.VISIBLE);
            tvYYYY.setVisibility(View.GONE);
*/

            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary));
            myAccountBinding.etFirstName.setBackgroundTintList(colorStateList);
            myAccountBinding.etLastName.setBackgroundTintList(colorStateList);
            myAccountBinding.etPassword.setBackgroundTintList(colorStateList);
            myAccountBinding.etPasswordAgain.setBackgroundTintList(colorStateList);
            myAccountBinding.etPhone.setBackgroundTintList(colorStateList);
            myAccountBinding.etDD.setBackgroundTintList(colorStateList);
            myAccountBinding.etMM.setBackgroundTintList(colorStateList);
            myAccountBinding.etYYYY.setBackgroundTintList(colorStateList);

            myAccountBinding.etFirstName.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etLastName.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etPasswordAgain.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etPhone.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etDD.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etMM.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            myAccountBinding.etYYYY.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            myAccountBinding.rlFemale.setEnabled(true);
            myAccountBinding.rlMale.setEnabled(true);
            myAccountBinding.ivEditImg.setImageResource(R.drawable.ic_edit_white);
            myAccountBinding.txtedit.setVisibility(View.INVISIBLE);
        }
        if ( v == imgBack)
        {
            finish();
        }
        if( v == myAccountBinding.tvSave)
        {
            first_name = myAccountBinding.etFirstName.getText().toString();
            last_name = myAccountBinding.etLastName.getText().toString();
            password = myAccountBinding.etPassword.getText().toString();
            c_password = myAccountBinding.etPasswordAgain.getText().toString();
            phone_no = myAccountBinding.etPhone.getText().toString();

            String code;
            try {
                code = myAccountBinding.ccp.getSelectedCountryCodeWithPlus().toString();
            }catch (Exception e){
                code = myAccountBinding.ccp.getDefaultCountryCodeWithPlus().toString();
            }

            // String code = tvCountryCode.getText().toString();
            String contact = phone_no;
            if (!contact.equals("")) {
                phone_no = code + " " + contact;
            }

            if (!updateRegisterValidate(MyAccountActivity.this,first_name, last_name, password, c_password, phone_no))
            {
//                Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
            }
            /*else if (phone_no.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Enter Contact Number", Toast.LENGTH_SHORT).show();
            }
            else if (etDOB.getText().toString().equals(""))
            {
                Toast.makeText(getApplicationContext(), "Enter DOB", Toast.LENGTH_SHORT).show();
            }*/
            else if (gender.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Select gender", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (final_ImgBase64.equals(""))
                {
                   // Toast.makeText(getApplicationContext(), "Upload Image", Toast.LENGTH_SHORT).show();
                    register_img = user_img;
                    new HttpAsyncTaskUpdateRegister().execute(Utility.BASE_URL+"UpdateRegistration");
                }
                else
                {
                    new HttpAsyncTaskPhotoUpload().execute(Utility.BASE_URL+"ImgUpload");
                }
            }
        }
        if( v == myAccountBinding.tvCancel)
        {
            finish();
        }
        if( v == myAccountBinding.rlMale)
        {
           /* TranslateAnimation slide1 = new TranslateAnimation(0, -190, 0, 0);
            slide1.setDuration(1000);
            myAccountBinding.ivConnectImg.startAnimation(slide1);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    myAccountBinding.vwDrag2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                    ivFemaleround.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            myAccountBinding.vwDrag1.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivMaleImg.setImageResource(R.drawable.ic_male);
            ivMaleRound.setImageResource(R.drawable.round_blue);
            gender = "M";
            txtGender.setText(R.string.male);*/

            TranslateAnimation slide1 = new TranslateAnimation(0, -(motionLength-15), 0, 0);
            slide1.setDuration(1000);
            myAccountBinding.ivConnectImg.startAnimation(slide1);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.unselected));
                    // ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                    myAccountBinding.ivMaleRound.setImageResource(R.drawable.ic_man_blue);

                }
            }, 1300);
            myAccountBinding.ivFemaleround.setImageResource(R.drawable.ic_girl_gray);
            myAccountBinding.ivConnectImg.setVisibility(View.INVISIBLE);
            //second things
            myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //   ivMaleImg.setImageResource(R.drawable.ic_male);

            gender = "M";
            myAccountBinding.txtGender.setText("Gender: Male");
        }
        if( v == myAccountBinding.rlFemale)
        {
           /* TranslateAnimation slide = new TranslateAnimation(0, 190, 0, 0);
            slide.setDuration(1000);
            myAccountBinding.ivConnectImg.startAnimation(slide);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myAccountBinding.vwDrag1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                    ivMaleRound.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            myAccountBinding.vwDrag2.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivFemaleImg.setImageResource(R.drawable.ic_female);
            ivFemaleround.setImageResource(R.drawable.round_blue);
            gender = "F";
            txtGender.setText(R.string.female);*/
            TranslateAnimation slide = new TranslateAnimation(0, motionLength-15, 0, 0);
            slide.setDuration(1000);
            myAccountBinding.ivConnectImg.startAnimation(slide);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //  myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.unselected));
                    //   ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                    myAccountBinding.ivFemaleround.setImageResource(R.drawable.ic_girl_blue);

                }
            }, 1300);
            myAccountBinding.ivConnectImg.setVisibility(View.INVISIBLE);
            myAccountBinding.ivMaleRound.setImageResource(R.drawable.ic_man_gray);
            myAccountBinding.vwDrag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            myAccountBinding.viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //second things
            // myAccountBinding.vwDrag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //   myAccountBinding.vwDrag2.setBackground(getResources().getDrawable(R.drawable.dotted));
            //  ivFemaleImg.setImageResource(R.drawable.ic_female);

            gender = "F";
            myAccountBinding.txtGender.setText("Gender: Female");
        }
    }



    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(MyAccountActivity.this);
                boolean result1 = Utility.checkCameraPermission(MyAccountActivity.this);
                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask ="Take Photo";
                    if(result1)
                        cameraIntent();
                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            Utility.freeMemory();
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                Utility.freeMemory();
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result.getUri()));
                    if (bitmap.equals("") || bitmap == null) {
                        bitmap=BitmapFactory.decodeFile(getRealPathFromURI(result.getUri()));
                    }
                    // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    Utility.freeMemory();

                    long size = Utility.imageCalculateSize(bitmap);
                    //Toast.makeText(getApplicationContext(), String.valueOf(size), Toast.LENGTH_LONG).show();

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                    if (size > 500000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    }
                    else if (size > 400000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, bytes);
                    }
                    else if (size > 300000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                    }
                    else if (size > 200000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    }
                    else if (size > 100000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                    }
                    else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    }
                 //   bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    image = ConvertBitmapToString(bitmap);
                    final_ImgBase64 = BitMapToString(bitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                    //    Upload();
                    myAccountBinding.imgProfile.setImageBitmap(bitmap);
                    bitmap.recycle();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Utility.freeMemory();
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Utility.freeMemory();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final_ImgBase64 = BitMapToString(thumbnail);
        Upload();
        myAccountBinding.imgProfile.setImageBitmap(thumbnail);
    }

    private void Upload()
    {
        Utility.freeMemory();
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new UploadFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Utility.BASE_URL+"ImgUpload");
            }
            else
            {
                new UploadFile().execute(Utility.BASE_URL+"ImgUpload");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null)
        {
            Uri targetUri = data.getData();

            String photoPath = getPath(targetUri);

            ExifInterface ei = null;
            Bitmap bitmap = null;
            Bitmap rotatedBitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                try
                {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    final_ImgBase64 = BitMapToString(resizedBitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                //    Upload();
                    myAccountBinding.imgProfile.setImageBitmap(resizedBitmap);
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    ei = new ExifInterface(String.valueOf(targetUri));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    switch (orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            myAccountBinding.imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            myAccountBinding.imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            myAccountBinding.imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                            rotatedBitmap = bitmap ;
                            myAccountBinding.imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        default:
                            rotatedBitmap = bitmap;
                            myAccountBinding.imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
//        BmToString(bm);
    }

    public static Bitmap rotateImage(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String ConvertBitmapToString(Bitmap bitmap)
    {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    private class UploadFile extends AsyncTask<String, Void, Void>
    {
        private String Content;
        private String Error = null;
        String data = "";
        private BufferedReader reader;


        protected void onPreExecute() {

            pDialog.show();

            try {

                data += "&" + URLEncoder.encode("ImgBase64", "UTF-8") + "=" + "data:image/png;base64," + image;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        protected Void doInBackground(String... urls)
        {
            HttpURLConnection connection = null;
            try
            {
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Content-Length", "" + data.getBytes().length);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                //make request
                writer.write(data);
                writer.flush();
                writer.close();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Content = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return null;

        }


        protected void onPostExecute(Void unused)
        {
            // NOTE: You can call UI Element here.

            pDialog.dismiss();
            try {

                if (Content != null) {
                    JSONObject jsonResponse = new JSONObject(Content);
//                    Toast.makeText(getApplicationContext(), Content, Toast.LENGTH_LONG).show();
                    String status = jsonResponse.getString("status");
                    if ("200".equals(status))
                    {
                        Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Something is wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(MyAccountActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Upload photo" ;
            CustomProgressDialog(loading, MyAccountActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return PhotoUploadPost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           Utility.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null)
                    {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        //   Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        register_img = ImgName;
                        new HttpAsyncTaskUpdateRegister().execute(Utility.BASE_URL+"UpdateRegistration");

                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public  String PhotoUploadPost(String url)
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64 );
            jsonObject.accumulate("classification", "userphoto" );

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


    private class HttpAsyncTaskUpdateRegister extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(MyAccountActivity.this);
            dialog.setMessage("Update Registering...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Update registering" ;
            CustomProgressDialog(loading, MyAccountActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return UpdateRegisterPost(urls[0]);
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
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    String userId = jsonObject.getString("userId").toString();

                    String date_DOB = "";
                    if (myAccountBinding.etDD.getText().toString().equals("") || myAccountBinding.etMM.getText().toString().equals("") || myAccountBinding.etYYYY.getText().toString().equals("")){
                        date_DOB = "";
                    }
                    else {
                        date_DOB = myAccountBinding.etDD.getText().toString() + "/" + myAccountBinding.etMM.getText().toString() + "/" + myAccountBinding.etYYYY.getText().toString();
                    }

                    if (success.equals("1"))
                    {

                        jsonObject.accumulate("FirstName", first_name );
                        jsonObject.accumulate("Gender", gender );
                        jsonObject.accumulate("LastName", last_name);
                        jsonObject.accumulate("Password", password);
                        jsonObject.accumulate("Phone", phone_no);
                        jsonObject.accumulate("Photo_String",register_img);
                        jsonObject.accumulate("UserId", user_id );
                        jsonObject.accumulate("UserName", email_id);
                        jsonObject.accumulate("dob", date_DOB);


                        session.createLoginSession(Q_ID, profile_id, user_id, first_name + " " + last_name, email_id, register_img, gender, password, date_DOB, phone_no, Connection_Limit, Connection_Left);
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to update..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public  String UpdateRegisterPost(String url)
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

            String date_DOB = "";
            if (myAccountBinding.etDD.getText().toString().equals("") || myAccountBinding.etMM.getText().toString().equals("") || myAccountBinding.etYYYY.getText().toString().equals("")){
                date_DOB = "";
            }
            else {
                date_DOB = myAccountBinding.etDD.getText().toString() + "/" + myAccountBinding.etMM.getText().toString() + "/" + myAccountBinding.etYYYY.getText().toString();
            }

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("FirstName", first_name );
            jsonObject.accumulate("Gender", gender );
            jsonObject.accumulate("LastName", last_name);
            jsonObject.accumulate("Password", password);
            jsonObject.accumulate("Phone", phone_no);
            jsonObject.accumulate("Photo_String",register_img);
            jsonObject.accumulate("UserId", user_id );
            jsonObject.accumulate("UserName", email_id);
            jsonObject.accumulate("dob", date_DOB);

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

    private class HttpAsyncTaskFetchLoginData extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(MyAccountActivity.this);
            dialog.setMessage("Fetching My Account...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "My account" ;
            CustomProgressDialog(loading, MyAccountActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return FetchLoginDataPost(urls[0]);
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
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();

                    if (success.equals("1"))
                    {
//                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                        JSONObject profile = jsonObject.getJSONObject("profile");

                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not able to update..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public  String FetchLoginDataPost(String url)
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
            jsonObject.accumulate("Password", user_pass );
            jsonObject.accumulate("Platform", "Android" );
            jsonObject.accumulate("Token", "1234567890");
            jsonObject.accumulate("UserName", email_id );

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
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Profiles...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "My account" ;
            CustomProgressDialog(loading, MyAccountActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return MyProfilePost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();


        }
    }

    public  String MyProfilePost(String url)
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
            jsonObject.accumulate("userid", user_id);

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



}
