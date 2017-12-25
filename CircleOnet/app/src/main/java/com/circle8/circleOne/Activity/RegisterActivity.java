package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.AsyncRequest;
import com.circle8.circleOne.Utils.Utility;
import com.hbb20.CountryCodePicker;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Validation.validate;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AsyncRequest.OnAsyncRequestComplete
{
    public static EditText etFirstName, etLastName, etPassword, etConfirmPass, etPhone, etEmail;
    public static TextView tvUsernameInfo, tvFirstnameInfo, tvLastnameInfo, tvPasswordInfo, tvRePasswordInfo, tvEmailInfo, tvPhoneInfo;
    private LinearLayout lnrRegister;
    private ImageView ivConnect;
    RelativeLayout ivMale, ivFemale;
    private View line_view1, line_view2;
    ImageView ivMaleRound, ivFemaleround, imgBack;
    TextView txtGender;
    String final_ImgBase64 = "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";
    CountryCodePicker ccp;
    private String UrlRegister = Utility.BASE_URL+"Registration";
    private ArrayList<NameValuePair> params;
    TextView txtprofile;
    private CircleImageView civProfilePic;
    private String userChoosenTask;
    CharSequence[] items;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String imagepath = null;
    private File file;
    String refferelCode = "";
    private String company_name, first_name, last_name, phone_no, password, c_password, user_name, gender = "", email;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String image;
    ProgressDialog pDialog;
    String encodedImageData, register_img = "";
    String UserID = "", Facebook = "", Google = "", Linkedin = "", Twitter = "", UserName = "", Email = "", Image = "";
    EditText etReferrelCode;
    TextView tvReferrelInfo;
    int motionLength;
    int roundWidth = 0, lineWidth = 0;
    View viewCenter;
    String Q_ID = "";
    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    EditText etDD, etMM, etYYYY;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        txtprofile = (TextView) findViewById(R.id.txtprofile);
        etReferrelCode = (EditText) findViewById(R.id.etReferrelCode);
        tvReferrelInfo = (TextView) findViewById(R.id.tvReferrelInfo);
        txtGender = (TextView) findViewById(R.id.txtGender);
        lnrRegister = (LinearLayout) findViewById(R.id.lnrBottomReg);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ivMaleRound = (ImageView) findViewById(R.id.ivMaleRound);
        ivFemaleround = (ImageView) findViewById(R.id.ivFemaleround);
        ivMale = (RelativeLayout) findViewById(R.id.ivMale);
        ivFemale = (RelativeLayout) findViewById(R.id.ivFemale);
        ivConnect = (ImageView) findViewById(R.id.iv_ConnectImg);
        line_view1 = (View) findViewById(R.id.vwDrag1);
        line_view2 = (View) findViewById(R.id.vwDrag2);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        etDD = (EditText) findViewById(R.id.etDD);
        etMM = (EditText) findViewById(R.id.etMM);
        etYYYY = (EditText) findViewById(R.id.etYYYY);
        imgBack.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        civProfilePic = (CircleImageView) findViewById(R.id.imgProfileCard);
        viewCenter = findViewById(R.id.viewCenter);
        tvUsernameInfo = (TextView) findViewById(R.id.tvUsernameInfo);
        tvFirstnameInfo = (TextView) findViewById(R.id.tvFirstNameInfo);
        tvLastnameInfo = (TextView) findViewById(R.id.tvLastNameInfo);
        tvPasswordInfo = (TextView) findViewById(R.id.tvPasswordInfo);
        tvRePasswordInfo = (TextView) findViewById(R.id.tvAgainPasswordInfo);
        tvEmailInfo = (TextView) findViewById(R.id.tvEmailInfo);
        tvPhoneInfo = (TextView) findViewById(R.id.tvPhoneInfo);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        Intent intent = getIntent();
        Twitter = intent.getStringExtra("Twitter");
        Linkedin = intent.getStringExtra("Linkedin");
        Google = intent.getStringExtra("Google");
        Facebook = intent.getStringExtra("Facebook");
        UserName = intent.getStringExtra("UserName");
        Email = intent.getStringExtra("Email");
        Image = intent.getStringExtra("Image");

        Typeface font = Typeface.createFromAsset(getAssets(), "century-gothic-1361531616.ttf");
        txtprofile.setTypeface(font);
        etEmail.setText(Email);
        try {
            String kept = UserName.substring(0, UserName.indexOf(" "));
            String remainder = UserName.substring(UserName.indexOf(" ") + 1, UserName.length());
            etFirstName.setText(kept);
            etLastName.setText(remainder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  etUserName.setText(UserName);

        Uri targetUri = Uri.parse(Image);
        try {
            if (!Image.equals("")) {
                Glide.with(getApplicationContext())
                        .load(targetUri)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(civProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                super.setResource(resource);
                                final_ImgBase64 = BitMapToString(resource);
                                // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                civProfilePic.setImageResource(R.drawable.usr_white1);
            }
        } catch (Exception e) {
            civProfilePic.setImageResource(R.drawable.usr_white1);
        }

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                // Toast.makeText(getApplicationContext(), "Updated " + ccp.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
            }
        });
       /* Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    civProfilePic.setImageBitmap(getBitmapFromURL(Image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();*/


       /* URL imageURL = null;
        try {
            imageURL = new URL(Image);
            civProfilePic.setImageBitmap(downloadImage(Image));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        */

        ivMale.setOnClickListener(this);
        ivFemale.setOnClickListener(this);
        ivConnect.setOnClickListener(this);
        lnrRegister.setOnClickListener(this);
        civProfilePic.setOnClickListener(this);

        ivConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

        etConfirmPass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = etPassword.getText().toString();
                String strPass2 = etConfirmPass.getText().toString();
                if (strPass1.startsWith(strPass2)) {
                    tvRePasswordInfo.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    tvRePasswordInfo.setVisibility(View.VISIBLE);
                    tvRePasswordInfo.setText("Password does not match");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
       // motionLength = lineWidth+roundWidth;

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

    private Bitmap downloadImage(String stringUrl) {
        URL url;
        Bitmap bm = null;
        try {
            url = new URL(stringUrl);
            URLConnection ucon = url.openConnection();
            InputStream is;
            if (ucon instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                int statusCode = httpConn.getResponseCode();
                if (statusCode == 200) {
                    is = httpConn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    BufferedInputStream bis = new BufferedInputStream(is, 8192);
                    ByteArrayBuffer baf = new ByteArrayBuffer(1024);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    byte[] rawImage = baf.toByteArray();
                    bm = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
                    bis.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack)
        {
            Utility.freeMemory();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this, R.style.Blue_AlertDialog);
            alert.setMessage("Are you sure you want to leave this page");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do your work here
                    dialog.dismiss();
                    finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        if (v == ivMale) {
            Utility.freeMemory();
          //  Toast.makeText(getApplicationContext(), String.valueOf(motionLength), Toast.LENGTH_LONG).show();
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
            txtGender.setText("Male");
        }
        if (v == ivFemale) {
            Utility.freeMemory();
          //  Toast.makeText(getApplicationContext(), String.valueOf(motionLength), Toast.LENGTH_LONG).show();
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
            txtGender.setText("Female");

        }
        if (v == lnrRegister) {
            Utility.freeMemory();
            company_name = "Ample Arch";
            refferelCode = etReferrelCode.getText().toString();
            user_name = etFirstName.getText().toString() + " " + etLastName.getText().toString();
            first_name = etFirstName.getText().toString();
            last_name = etLastName.getText().toString();
            phone_no = etPhone.getText().toString();
            password = etPassword.getText().toString();
            c_password = etConfirmPass.getText().toString();
            email = etEmail.getText().toString();
            String code;
            try
            {
                code = ccp.getSelectedCountryCodeWithPlus().toString();
            }
            catch (Exception e){
                code = ccp.getDefaultCountryCodeWithPlus().toString();
            }

           // String code = tvCountryCode.getText().toString();
            String contact = phone_no;
            if (!contact.equals("")) {
                phone_no = code + " " + contact;
            }

            if (!validate(user_name, first_name, last_name, password, c_password, phone_no, email))
            {
                Toast.makeText(getApplicationContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            }
           /* else if (refferelCode.equals("")){
                Toast.makeText(getApplicationContext(), "Enter Referral Code", Toast.LENGTH_SHORT).show();
            }*/
           /* else if (phone_no.equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Contact Number", Toast.LENGTH_SHORT).show();
            }
            else if (etDOB.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter DOB", Toast.LENGTH_SHORT).show();
            }*/
            else if (gender.equals("")) {
                Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
            }
            else {
                if (final_ImgBase64.equals("")) {
                    new HttpAsyncTask().execute(Utility.BASE_URL+"Registration");

                }else {
                    new HttpAsyncTaskPhotoUpload().execute(Utility.BASE_URL+"ImgUpload");
                }
            }
        }
        if (v == civProfilePic) {
           // selectImage();
            Utility.freeMemory();
            CropImage.activity(null)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(RegisterActivity.this);
        }
    }

    public boolean date_validation(String d1, String d2) {
        boolean valid = true;
        try {
            // If you already have date objects then skip 1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1" + sdf.format(date1));
            System.out.println("Date2" + sdf.format(date2));

//            Toast.makeText(getApplicationContext(), "S date: "+date1, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), "C date: "+date2, Toast.LENGTH_LONG).show();

            // Create 2 dates ends
            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                valid = false;
//                Toast.makeText(getApplicationContext(), "Selected date is AFTER current date.", Toast.LENGTH_LONG).show();
            }

            // before() will return true if and only if date1 is before date2
            if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                valid = true;
//                Toast.makeText(getApplicationContext(), "Selected date is BEFORE current Date.", Toast.LENGTH_LONG).show();
            }

            //equals() returns true if both the dates are equal
            if (date1.equals(date2)) {
                System.out.println("Date1 is equal Date2");
                valid = false;
//                Toast.makeText(getApplicationContext(), "Selected date is EQUAL current date.", Toast.LENGTH_LONG).show();
            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return valid;
    }


    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(RegisterActivity.this);
                boolean result1 = Utility.checkCameraPermission(RegisterActivity.this);
                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask = "Take Photo";
                    if (result1)
//                        activeTakePhoto();
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
//                        activeGallery();
                        galleryIntent();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result.getUri()));
                    if (bitmap.equals("") || bitmap == null) {
                        bitmap=BitmapFactory.decodeFile(getRealPathFromURI(result.getUri()));
                    }
                    // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    long size = Utility.imageCalculateSize(bitmap);

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

                    civProfilePic.setImageBitmap(bitmap);




                    image = ConvertBitmapToString(bitmap);
                    final_ImgBase64 = BitMapToString(bitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                  //  civProfilePic.setImageBitmap(bitmap);

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

    private class UploadFile extends AsyncTask<String, Void, Void> {
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

        protected Void doInBackground(String... urls) {

            HttpURLConnection connection = null;
            try {
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
    }

    public static String BitMapToString(Bitmap bitmap) {
        Utility.freeMemory();
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null)
        {
            Uri targetUri = data.getData();

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
                    civProfilePic.setImageBitmap(resizedBitmap);
                } catch (FileNotFoundException e) {
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

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            civProfilePic.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            civProfilePic.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            civProfilePic.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                            rotatedBitmap = bitmap;
                            civProfilePic.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            break;

                        default:
                            rotatedBitmap = bitmap;
                            civProfilePic.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

           /* try
            {
                ei = new ExifInterface(photoPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/

//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

//            Bitmap bitmap = null;
//            Bitmap rotatedBitmap = null;

           /* try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
//                image = ConvertBitmapToString(resizedBitmap);
//                final_ImgBase64 = BitMapToString(resizedBitmap);
               // final_ImgBase64 = resizeBase64Image(s);
                Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
//                Upload();
//                civProfilePic.setImageBitmap(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

/*
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
            }
*/

        }
//        BmToString(bm);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {

            String s = URLDecoder.decode(encodedString, "UTF-8");
            byte[] encodeByte = Base64.decode(s, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }


    private void onCaptureImageResult(Intent data)
    {
        /*Uri targetUri = data.getData();

        String imagePath = getPath(targetUri);
*/

        Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

//        ExifInterface ei = null;
//        Bitmap rotatedBitmap = null;

        final_ImgBase64 = BitMapToString(thumbnail);
      //  Upload();
        civProfilePic.setImageBitmap(thumbnail);

/*
        try
        {
            ei = new ExifInterface(String.valueOf(targetUri));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(thumbnail, 90);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(thumbnail, 180);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(thumbnail, 270);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    rotatedBitmap = thumbnail;
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                default:
                    rotatedBitmap = thumbnail;
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
*/
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            String date_DOB = "";
            if (etDD.getText().toString().equals("") || etMM.getText().toString().equals("") || etYYYY.getText().toString().equals("")){
                date_DOB = "";
            }
            else {
                date_DOB = etDD.getText().toString() + "/" + etMM.getText().toString() + "/" + etYYYY.getText().toString();
            }
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Facebook", Facebook);
            jsonObject.accumulate("FirstName", first_name);
            jsonObject.accumulate("Gender", gender);
            jsonObject.accumulate("Google", Google);
            jsonObject.accumulate("LastName", last_name);
            jsonObject.accumulate("Linkedin", Linkedin);
            jsonObject.accumulate("Password", password);
            jsonObject.accumulate("Phone", phone_no);
            jsonObject.accumulate("Photo_String", register_img);
            jsonObject.accumulate("Platform", "Android");
            jsonObject.accumulate("ReferralCode", refferelCode);
            jsonObject.accumulate("Token", LoginActivity.pushToken);
            jsonObject.accumulate("Twitter", Twitter);
            jsonObject.accumulate("UserName", email);
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POST1(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ImgBase64", final_ImgBase64);
            jsonObject.accumulate("classification", "userphoto");

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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POSTQ_ID(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Q_ID", Q_ID);
            jsonObject.accumulate("UserId", UserID);

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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Registering...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Registering" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    final String Status = jsonObject.getString("Status").toString();
                    UserID = jsonObject.getString("userId").toString();
                    if (success.equals("1") && message.equalsIgnoreCase("Successfully Registered.")) {
                       // Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                        final QBUser user = new QBUser(email, "circle@123");
                        user.setExternalId("");
                        user.setEmail(email);
                        user.setFullName(etFirstName.getText().toString() + " " + etLastName.getText().toString());
                        StringifyArrayList<String> tags = new StringifyArrayList<String>();
                        tags.add("dev");
                        user.setTags(tags);

                        try {
                            QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(final QBUser user, Bundle args) {
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {

                                            Q_ID = String.valueOf(user.getId());
                                            new HttpAsyncTaskUpdateQ_ID().execute(Utility.BASE_URL+"User/Update_QID");

                                            final Dialog dialog = new Dialog(RegisterActivity.this);
                                            dialog.setContentView(R.layout.register_custom_popup);

                                            dialog.show();

                                            if (Status.equalsIgnoreCase("Verified")) {
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                new HttpAsyncTaskVerify().execute(Utility.BASE_URL+"AccVerification/" + UserID);
                                            }

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                    finish();
                                                }
                                            },2500);

//                Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.img_anim);
//                imageView1.startAnimation(anim);
                                        }
                                    }, 2500);

                                }

                                @Override
                                public void onError(QBResponseException errors) {
                                    Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e) {

                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskVerify extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Verifying..please Check your mail..");
            //dialog.setTitle("Saving Reminder");
           // dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST2(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
          //  dialog.dismiss();
            /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();*/
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String POST2(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        byte[] encodeValue = Base64.encode(imgString.getBytes(), Base64.DEFAULT);

        //  Toast.makeText(getApplicationContext(), new String(encodeValue), Toast.LENGTH_LONG).show();
        return new String(encodeValue);
    }

    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        //   Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        register_img = ImgName;
                        new HttpAsyncTask().execute(Utility.BASE_URL+"Registration");

                    } else {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskUpdateQ_ID extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
           // CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTQ_ID(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
          //  rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success").toString();
                    String Message = jsonObject.getString("Message").toString();

                    if (Success.equals("1")) {
                       // Toast.makeText(getBaseContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), Message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);


        if (image.getHeight() <= 400 && image.getWidth() <= 400) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 200, 200, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }



    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void BmToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);

//        file = persistImage(bitmap,"ImageName");
//        FileBody bin1 = new FileBody(file);

//        bitmap_image = bitmap ;
//        string_image = image ;

//        Toast.makeText(getApplicationContext(),"Bitmap: "+bitmap_image,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"Image: "+string_image,Toast.LENGTH_LONG).show();
    }

    private void connectWithHttpPost(String company_name, String user_name, String first_name, String last_name,
                                     String phone_no, String password) {
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String company_name = params[0];
                String user_name = params[1];
                String first_name = params[2];
                String last_name = params[3];
                String phone_no = params[4];
                String password = params[5];


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Utility.BASE_URL+"Registration");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
//                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                nameValuePair.add(new BasicNameValuePair("CompanyName", company_name));
                nameValuePair.add(new BasicNameValuePair("FName", first_name));
                nameValuePair.add(new BasicNameValuePair("LName", last_name));
                nameValuePair.add(new BasicNameValuePair("Phone", phone_no));
                nameValuePair.add(new BasicNameValuePair("Pwd", password));
                nameValuePair.add(new BasicNameValuePair("UserName", user_name));

                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL
                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    System.out.println("httpResponse");

                    InputStream inputStream = httpResponse.getEntity().getContent();

                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // We have to use a class that can handle modifiable sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    //and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }
                    //We return that value then the onPostExecute() can handle the content
                    System.out.println("Returning value of doInBackground :" + stringBuilder.toString());

                    // If the Username or Password wrong, it will return "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exception generates caz of httpResponse :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second exception generates caz of httpResponse :" + ioe);
                    ioe.printStackTrace();
                }
                return null;
            }

            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

//                Toast.makeText(getApplicationContext(), result , Toast.LENGTH_LONG).show();

                if (result.equals("")) {
                    Toast.makeText(getApplicationContext(), "Check For Data Connection..", Toast.LENGTH_LONG).show();
                } else {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("user");
                        if (res.equals("")) {
                            Toast.makeText(getApplicationContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        } else {
                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("user");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(company_name, user_name, first_name, last_name, phone_no, password);
    }

    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("CompanyName", "Ample arch"));
        nameValuePair.add(new BasicNameValuePair("FName", "Ample"));
        nameValuePair.add(new BasicNameValuePair("LName", "Arch"));
        nameValuePair.add(new BasicNameValuePair("Phone", "9876543210"));
        nameValuePair.add(new BasicNameValuePair("Pwd", "ample123"));
        nameValuePair.add(new BasicNameValuePair("UserName", "circle8"));
        return nameValuePair;
    }

    @Override
    public void asyncResponse(String response) {
//        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        Log.i("SignIn response: ", response);

        if (response.equals("")) {
            Toast.makeText(getApplicationContext(), "Attempt Failed..", Toast.LENGTH_LONG).show();
        }

    }

    private class AsyncDataClass extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
           // dialog.setMessage("Registering...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setContentView(R.layout.custom_progress);
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 9000);
            HttpConnectionParams.setSoTimeout(httpParameters, 9000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            String urlString = Utility.BASE_URL+"Registration";

            HttpPost httpPost = new HttpPost(urlString);

            String jsonResult = "";

            try {
//                FileBody bin1 = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("CompanyName", new StringBody(company_name));
                reqEntity.addPart("FName", new StringBody("Ample"));
                reqEntity.addPart("LName", new StringBody("Arch"));
                reqEntity.addPart("Phone", new StringBody("9876543210"));
                reqEntity.addPart("Pwd", new StringBody("ample123"));
                reqEntity.addPart("UserName", new StringBody("ample arch"));
//                reqEntity.addPart("image", bin1);

                httpPost.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            System.out.println("Resulted Value: " + result);
//            Toast.makeText(getApplicationContext(), "Resulted value" + result, Toast.LENGTH_LONG).show();

            if (result.equals("") || result == null) {
                Toast.makeText(getApplicationContext(), "Server connection failed...", Toast.LENGTH_LONG).show();
                return;
            }
            String error = returnParsedJsonObject(result);

            String message = returnParsedJsonObject1(result);
            //Toast.makeText(getApplicationContext(),"error_code : " + error,Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(),"msg : " + message,Toast.LENGTH_LONG).show();

//            if (message.equals("success"))
//            {
//                Toast.makeText(getApplicationContext(), "Register Successfully.", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                finish();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "Email ID already Exists.", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                finish();
//            }
        }
    }

    private String returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        String returnedResult = "";
        try {

            resultObject = new JSONObject(result);
            JSONArray jsonArray = resultObject.getJSONArray("response");
            // Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
            returnedResult = jsonArray.getJSONObject(0).getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    private String returnParsedJsonObject1(String result) {

        JSONObject resultObject = null;
        String returnedResult = "";
        try {
            resultObject = new JSONObject(result);
            JSONArray jsonArray = resultObject.getJSONArray("response");
            // Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
            returnedResult = jsonArray.getJSONObject(0).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }

    public void CustomProgressDialog(final String loading)
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this, R.style.Blue_AlertDialog);
        alert.setMessage("Are you sure you want to leave this page");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                dialog.dismiss();
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
