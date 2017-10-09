package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.TextRecyclerAdapter;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.google.zxing.WriterException;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.ConvertBitmapToString;
import static com.circle8.circleOne.Utils.Validation.updateRegisterValidate;
import static com.circle8.circleOne.Utils.Validation.validate;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText etUserName, etFirstName, etLastName, etPassword,
            etPasswordAgain, etEmail, etDOB, etAddress1 , etAddress2, etPhone ;
    private CircleImageView imgProfile ;
    private TextView tvSave, tvCancel, txtGender ;
    private ImageView ivFemaleround, ivFemaleImg, ivConnect, ivMiniCamera,
            ivMaleRound, ivMaleImg, ivEditImg;
    private RelativeLayout rlMale, rlFemale ;
    View viewCenter;
    RelativeLayout ivMale, ivFemale;
    private View line_view1, line_view2 ;

    private String gender = "", final_ImgBase64 = "", Image = "";
    private ImageView imgBack;
    private CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String image;
    private ProgressDialog pDialog;
    private String  first_name, last_name, phone_no, password, c_password, user_name ;
    public static TextView tvFirstNameInfo, tvLastNameInfo, tvPasswordInfo, tvAgainPasswordInfo, tvPhoneInfo ;

    private LoginSession session;
    private String user_id, email_id, user_img, user_pass ;
    private String encodedImageData, register_img;
    private String user_Photo ;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    int motionLength;
    int roundWidth = 0, lineWidth = 0;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        email_id = user.get(LoginSession.KEY_EMAIL);
        user_img = user.get(LoginSession.KEY_IMAGE);
        user_pass = user.get(LoginSession.KEY_PASSWORD);

        Toast.makeText(getApplicationContext(),email_id+" "+user_pass,Toast.LENGTH_LONG).show();

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        etUserName = (EditText)findViewById(R.id.etUserName);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etPasswordAgain = (EditText)findViewById(R.id.etPasswordAgain);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etDOB = (EditText)findViewById(R.id.etDob);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);
        etPhone = (EditText)findViewById(R.id.etPhone);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvSave = (TextView)findViewById(R.id.tvSave);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        txtGender = (TextView)findViewById(R.id.txtGender);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        rlMale = (RelativeLayout)findViewById(R.id.ivMale);
        rlFemale = (RelativeLayout)findViewById(R.id.ivFemale);

        line_view1 = (View)findViewById(R.id.vwDrag1);
        line_view2 = (View)findViewById(R.id.vwDrag2);

        ivFemaleround = (ImageView)findViewById(R.id.ivFemaleround);
//        ivFemaleImg = (ImageView)findViewById(R.id.ivFemaleImg);
        ivConnect = (ImageView)findViewById(R.id.iv_ConnectImg);
        ivMaleRound = (ImageView)findViewById(R.id.ivMaleRound);
//        ivMaleImg = (ImageView)findViewById(R.id.ivMaleImg);
        ivMiniCamera = (ImageView)findViewById(R.id.ivMiniCamera);
        ivEditImg = (ImageView)findViewById(R.id.ivEditImg);
        viewCenter = findViewById(R.id.viewCenter);

        tvFirstNameInfo = (TextView)findViewById(R.id.tvFirstNameInfo);
        tvLastNameInfo = (TextView)findViewById(R.id.tvLastNameInfo);
        tvPasswordInfo = (TextView)findViewById(R.id.tvPasswordInfo);
        tvAgainPasswordInfo = (TextView)findViewById(R.id.tvAgainPasswordInfo);
        tvPhoneInfo = (TextView)findViewById(R.id.tvPhoneInfo);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        pDialog = new ProgressDialog(this);
        etUserName.setText(email_id);

        new HttpAsyncTaskFetchLoginData().execute("http://circle8.asia:8999/Onet.svc/UserLogin");
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

        ivFemaleround.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = ivFemaleround.getHeight();
                int width = ivFemaleround.getWidth();
                int L = ivFemaleround.getLeft();
                int T = ivFemaleround.getTop();
                int R = ivFemaleround.getRight();
                int B = ivFemaleround.getBottom();

                roundWidth = width / 2;
                motionLength = motionLength + roundWidth;
                System.out.print("ivFemale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
                //  Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                ivFemaleround.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ivMaleRound.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = ivMaleRound.getHeight();
                int width = ivMaleRound.getWidth();
                int L = ivMaleRound.getLeft();
                int T = ivMaleRound.getTop();
                int R = ivMaleRound.getRight();
                int B = ivMaleRound.getBottom();

                System.out.print("ivMale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                ivMaleRound.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ivConnect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = ivConnect.getHeight();
                int width = ivConnect.getWidth();
                int L = ivConnect.getLeft();
                int T = ivConnect.getTop();
                int R = ivConnect.getRight();
                int B = ivConnect.getBottom();
                lineWidth = width;
                motionLength = motionLength + lineWidth;
                System.out.print("ivConnect" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                ivConnect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvSave.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);
        ivMiniCamera.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        ivEditImg.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void onClick(View v)
    {
        if ( v == imgProfile)
        {
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
            CropImage.activity(null)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(MyAccountActivity.this);
        }
        if ( v == ivEditImg)
        {
            etFirstName.setEnabled(true);
            etLastName.setEnabled(true);
            etPassword.setEnabled(true);
            etPasswordAgain.setEnabled(true);
            etPhone.setEnabled(true);
        }
        if ( v == imgBack)
        {
            finish();
        }
        if ( v == ivMiniCamera)
        {
           // selectImage();
            CropImage.activity(null)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(MyAccountActivity.this);
        }
        if( v == tvSave)
        {
            first_name = etFirstName.getText().toString();
            last_name = etLastName.getText().toString();
            password = etPassword.getText().toString();
            c_password = etPasswordAgain.getText().toString();
            phone_no = etPhone.getText().toString();

            String code;
            try {
                code = ccp.getSelectedCountryCodeWithPlus().toString();
            }catch (Exception e){
                code = ccp.getDefaultCountryCodeWithPlus().toString();
            }

            // String code = tvCountryCode.getText().toString();
            String contact = phone_no;
            if (!contact.equals("")) {
                phone_no = code + " " + contact;
            }


            if (!updateRegisterValidate(first_name, last_name, password, c_password, phone_no))
            {
//                Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
            }
            else if (phone_no.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Enter Contact Number", Toast.LENGTH_SHORT).show();
            }
            else if (gender.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (final_ImgBase64.equals(""))
                {
                   // Toast.makeText(getApplicationContext(), "Upload Image", Toast.LENGTH_SHORT).show();
                    register_img = user_img;
                    new HttpAsyncTaskUpdateRegister().execute("http://circle8.asia:8999/Onet.svc/UpdateRegistration");
                }
                else
                {
                    new HttpAsyncTaskPhotoUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                }
            }
        }
        if( v == tvCancel)
        {
            finish();
        }
        if( v == rlMale)
        {
           /* TranslateAnimation slide1 = new TranslateAnimation(0, -190, 0, 0);
            slide1.setDuration(1000);
            ivConnect.startAnimation(slide1);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    line_view2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                    ivFemaleround.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            line_view1.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivMaleImg.setImageResource(R.drawable.ic_male);
            ivMaleRound.setImageResource(R.drawable.round_blue);
            gender = "M";
            txtGender.setText(R.string.male);*/

            TranslateAnimation slide1 = new TranslateAnimation(0, -(motionLength-15), 0, 0);
            slide1.setDuration(1000);
            ivConnect.startAnimation(slide1);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // line_view2.setBackgroundColor(getResources().getColor(R.color.unselected));
                    // ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                    ivMaleRound.setImageResource(R.drawable.ic_man_blue);

                }
            }, 1300);
            ivFemaleround.setImageResource(R.drawable.ic_girl_gray);
            ivConnect.setVisibility(View.INVISIBLE);
            //second things
            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //   ivMaleImg.setImageResource(R.drawable.ic_male);

            gender = "M";
            txtGender.setText("Gender: Male");
        }
        if( v == rlFemale)
        {
           /* TranslateAnimation slide = new TranslateAnimation(0, 190, 0, 0);
            slide.setDuration(1000);
            ivConnect.startAnimation(slide);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    line_view1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                    ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                    ivMaleRound.setImageResource(R.drawable.round_gray);
                }
            }, 1100);
            //second things
            line_view2.setBackground(getResources().getDrawable(R.drawable.dotted));
            ivFemaleImg.setImageResource(R.drawable.ic_female);
            ivFemaleround.setImageResource(R.drawable.round_blue);
            gender = "F";
            txtGender.setText(R.string.female);*/
            TranslateAnimation slide = new TranslateAnimation(0, motionLength-15, 0, 0);
            slide.setDuration(1000);
            ivConnect.startAnimation(slide);

            //first things
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //  line_view1.setBackgroundColor(getResources().getColor(R.color.unselected));
                    //   ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                    ivFemaleround.setImageResource(R.drawable.ic_girl_blue);

                }
            }, 1300);
            ivConnect.setVisibility(View.INVISIBLE);
            ivMaleRound.setImageResource(R.drawable.ic_man_gray);
            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //second things
            // line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //   line_view2.setBackground(getResources().getDrawable(R.drawable.dotted));
            //  ivFemaleImg.setImageResource(R.drawable.ic_female);

            gender = "F";
            txtGender.setText("Gender: Female");

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmap;

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result.getUri()));
                    // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

                    image = ConvertBitmapToString(bitmap);
                    final_ImgBase64 = BitMapToString(bitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                    //    Upload();
                    imgProfile.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final_ImgBase64 = BitMapToString(thumbnail);
        Upload();
        imgProfile.setImageBitmap(thumbnail);
    }

    private void Upload()
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new UploadFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://circle8.asia:8999/Onet.svc/ImgUpload");
            }
            else
            {
                new UploadFile().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
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
                    imgProfile.setImageBitmap(resizedBitmap);
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
                            imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                            rotatedBitmap = bitmap ;
                            imgProfile.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            Upload();
                            break;

                        default:
                            rotatedBitmap = bitmap;
                            imgProfile.setImageBitmap(rotatedBitmap);
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

            String loading = "Upload Photo" ;
            CustomProgressDialog(loading);
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
            rlProgressDialog.setVisibility(View.GONE);
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
                        new HttpAsyncTaskUpdateRegister().execute("http://circle8.asia:8999/Onet.svc/UpdateRegistration");

                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
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

            String loading = "Update Registering" ;
            CustomProgressDialog(loading);
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
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    String userId = jsonObject.getString("userId").toString();

                    if (success.equals("1"))
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to Update Register..", Toast.LENGTH_LONG).show();
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

            String loading = "My Account" ;
            CustomProgressDialog(loading);
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
            rlProgressDialog.setVisibility(View.GONE);

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
                        etUserName.setText(profile.getString("UserName"));
                        etFirstName.setText(profile.getString("FirstName"));
                        etLastName.setText(profile.getString("LastName"));
                        if (profile.getString("Phone").contains(" ")){
                            String name = profile.getString("Phone");
                            String kept = name.substring(0, name.indexOf(" "));
                            String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                            kept = kept.replaceAll("//+", "");
                            ccp.setCountryForPhoneCode(Integer.parseInt(kept));
                            etPhone.setText(remainder);
                        }
                        else {
                            etPhone.setText(profile.getString("Phone"));
                        }
                        //etPhone.setText(profile.getString("Phone"));
                        etPassword.setText(user_pass);
                        etPasswordAgain.setText(user_pass);
                        String user_Gender = profile.getString("Gender");
                         user_Photo = profile.getString("UserPhoto");

                        if (user_Photo.equals(""))
                        {
                            imgProfile.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+user_Photo).placeholder(R.drawable.usr_1).into(imgProfile);
                        }

                        if (user_Gender.equals("M"))
                        {
                            /*//first things
                            line_view2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            ivFemaleImg.setImageResource(R.drawable.ic_female_gray);
                            ivFemaleround.setImageResource(R.drawable.round_gray);
                            //second things
                            line_view1.setBackground(getResources().getDrawable(R.drawable.dotted));
                            ivMaleImg.setImageResource(R.drawable.ic_male);
                            ivMaleRound.setImageResource(R.drawable.round_blue);
                            gender = "M";
                            txtGender.setText(R.string.male);*/
                            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ivMaleRound.setImageResource(R.drawable.ic_man_blue);
                            ivFemaleround.setImageResource(R.drawable.ic_girl_gray);
                            ivConnect.setVisibility(View.INVISIBLE);
                            //second things
                            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            gender = "M";
                            txtGender.setText("Gender: Male");
                        }
                        else if (user_Gender.equals("F"))
                        {
                            /*//first things
                            line_view1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            ivMaleImg.setImageResource(R.drawable.ic_male_gray);
                            ivMaleRound.setImageResource(R.drawable.round_gray);
                            //second things
                            line_view2.setBackground(getResources().getDrawable(R.drawable.dotted));
                            ivFemaleImg.setImageResource(R.drawable.ic_female);
                            ivFemaleround.setImageResource(R.drawable.round_blue);
                            gender = "F";
                            txtGender.setText(R.string.female);*/
                            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ivFemaleround.setImageResource(R.drawable.ic_girl_blue);
                            ivConnect.setVisibility(View.INVISIBLE);
                            ivMaleRound.setImageResource(R.drawable.ic_man_gray);

                            line_view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            line_view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            viewCenter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            gender = "F";
                            txtGender.setText("Gender: Female");
                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not able to Update Register..", Toast.LENGTH_LONG).show();
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


    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
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

            String loading = "My Account" ;
            CustomProgressDialog(loading);
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
            rlProgressDialog.setVisibility(View.GONE);


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
