package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityAddManuallyBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class AddManuallyActivity extends AppCompatActivity implements View.OnClickListener
{

    ActivityAddManuallyBinding activityAddManuallyBinding ;
    List<String> scanTextLineList = new ArrayList<>();
    static String cardType = "";
    String NAME = "", EMAIL = "", PHONE = "", WEBSITE = "", ADDRESS = "", COMPANY = "";
    boolean result, result1;
    String front_image = "";
    private LoginSession session;
    private static String final_ImgBase64 = "";
    HashMap<String, String> user;
    ReferralCodeSession referralCodeSession;
    private String refer = "";
    String first_name = "";
    String last_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddManuallyBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_manually);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        result= Utility.checkPermission(AddManuallyActivity.this);
        result1=Utility.checkCameraPermission(AddManuallyActivity.this);

        session = new LoginSession(getApplicationContext());
        user = session.getUserDetails();

        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);

        String name1 = user.get(LoginSession.KEY_NAME);
        first_name = name1.substring(0, name1.indexOf(" "));
        last_name = name1.substring(name1.indexOf(" ") + 1, name1.length());

        activityAddManuallyBinding.tvMoreInfo.setOnClickListener(this);
        activityAddManuallyBinding.imgBack.setOnClickListener(this);
        activityAddManuallyBinding.txtfrontDelete.setOnClickListener(this);
        activityAddManuallyBinding.txtbackDelete.setOnClickListener(this);
        activityAddManuallyBinding.ivAttachFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardType = "front";
                if (result && result1) {
                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddManuallyActivity.this);
                }

//                Utility.freeMemory();
//                selectImage();
            }
        });

        activityAddManuallyBinding.ivAttachBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardType = "back";
                if (result && result1) {
                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddManuallyActivity.this);
                }
//                Utility.freeMemory();
//                selectImage();
            }
        });

        /*now get ArrayString*/
        Intent iGet = getIntent();
        String [] scanTextArray = iGet.getStringArrayExtra("ScanTextArray");
        front_image = iGet.getStringExtra("card");
        Toast.makeText(getApplicationContext(), front_image, Toast.LENGTH_LONG).show();
        activityAddManuallyBinding.txtCardFront.setText(front_image);
        /*convert StringArray to List<>*/
        List<String> scanTextLineList1 = new ArrayList<String>(Arrays.asList(scanTextArray));
        scanTextLineList = scanTextLineList1;

        NAME = "^([A-Z]([a-z]*|\\.) *){1,2}([A-Z][a-z]+-?)+$";
        EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        PHONE = "(?:^|\\D)(\\d{3})[)\\-. ]*?(\\d{3})[\\-. ]*?(\\d{4})(?:$|\\D)";

        for (int i = 0; i < scanTextLineList.size(); i++)
        {
            String size = String.valueOf(scanTextLineList.size());

            String scanItems = scanTextLineList.get(i);
            if(scanItems.length()>2)
            {
                if (scanItems.matches(NAME))
                {
                    String[] name = scanItems.split(" ");

                    activityAddManuallyBinding.etFirstName.setText(name[0]);
                    activityAddManuallyBinding.etLastName.setText(name[1]);
                    scanTextLineList.remove(i);
                }
                else if (scanItems.matches(EMAIL) || scanItems.contains("@"))
                {
                    activityAddManuallyBinding.etEmail.append(scanItems+"\n");
                    scanTextLineList.remove(i);
                }
                else if (scanItems.matches(PHONE) || scanItems.startsWith("+") || scanItems.startsWith("0"))
                {
                    activityAddManuallyBinding.etPhone.append(scanItems+"\n");
                    scanTextLineList.remove(i);
                }
                else if (scanItems.startsWith("www"))
                {
                    activityAddManuallyBinding.etWebsite.setText(scanItems);
                    scanTextLineList.remove(i);
                }
                else if (scanItems.contains("pvt") || scanItems.contains("ltd") || scanItems.contains("llp"))
                {
                    activityAddManuallyBinding.etCompany.setText(scanItems);
                    scanTextLineList.remove(i);
                }
                else
                {
                    int n = scanTextLineList.size();

                    String[] scanTextArray1 = scanTextLineList.toArray(new String[n]);
                    StringBuilder builder = new StringBuilder();
                    for (String string : scanTextArray1)
                    {
                        builder.append(string+"\n");

                    }
                    activityAddManuallyBinding.etAddress.setText(builder);
//                Toast.makeText(getApplicationContext(),builder,Toast.LENGTH_SHORT).show();
//                activityAddManuallyBinding.etAddress.setText(Arrays.toString(scanTextArray1));
                }
            }

        }
        scanTextLineList.clear();

        activityAddManuallyBinding.lnrSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if ( v == activityAddManuallyBinding.tvMoreInfo)
        {
            if (activityAddManuallyBinding.llMoreLay.getVisibility() == View.VISIBLE)
            {
                activityAddManuallyBinding.llMoreLay.setVisibility(View.GONE);
            }
            else
            {
                activityAddManuallyBinding.llMoreLay.setVisibility(View.VISIBLE);
            }
        }
        if ( v == activityAddManuallyBinding.txtfrontDelete)
        {
            activityAddManuallyBinding.txtCardFront.setText("");
        }
        if ( v == activityAddManuallyBinding.txtbackDelete)
        {
            activityAddManuallyBinding.txtCardBack.setText("");
        }
        if ( v == activityAddManuallyBinding.lnrSubmit)
        {
            if (activityAddManuallyBinding.etEmail.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "please enter email-id", Toast.LENGTH_LONG).show();
            }
            else if (activityAddManuallyBinding.etFirstName.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "please enter first name", Toast.LENGTH_LONG).show();
            }
            else if (activityAddManuallyBinding.etLastName.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "please enter last name", Toast.LENGTH_LONG).show();
            }
            else if (activityAddManuallyBinding.etPhone.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "please enter phone number", Toast.LENGTH_LONG).show();
            }
            else {
                new HttpAsyncTaskAddContact().execute(Utility.BASE_URL + "AddContact");
            }
        }
        if ( v == activityAddManuallyBinding.imgBack)
        {
            scanTextLineList.clear();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        scanTextLineList.clear();
        finish();
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


                    final_ImgBase64 = BitMapToString(bitmap);
                    //   Upload();

                    if (cardType.equals("front")) {

                        new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                    else if (cardType.equals("back")) {

                        new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }

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

    private class HttpAsyncTaskBackUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Uploading" ;
            CustomProgressDialog(loading, AddManuallyActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ImgBase64", final_ImgBase64);
                jsonObject.accumulate("classification", "card");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
            //fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        activityAddManuallyBinding.txtCardBack.setText(ImgName);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private class HttpAsyncTaskAddContact extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Adding Card" ;
            CustomProgressDialog(loading, AddManuallyActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Contact_Email", activityAddManuallyBinding.etEmail.getText().toString());
                jsonObject.accumulate("Contact_FirstName", activityAddManuallyBinding.etFirstName.getText().toString());
                jsonObject.accumulate("Contact_LastName", activityAddManuallyBinding.etLastName.getText().toString());
                jsonObject.accumulate("Contact_Phone", activityAddManuallyBinding.etPhone.getText().toString());
                jsonObject.accumulate("My_Email", user.get(LoginSession.KEY_EMAIL));
                jsonObject.accumulate("My_FirstName", first_name);
                jsonObject.accumulate("My_LastName", last_name);
                jsonObject.accumulate("My_Phone", user.get(LoginSession.KEY_PHONE));
                jsonObject.accumulate("my_profileid", user.get(LoginSession.KEY_PROFILEID));
                jsonObject.accumulate("ReferralCode", refer);
                jsonObject.accumulate("Card_Back", activityAddManuallyBinding.txtCardBack.getText().toString());
                jsonObject.accumulate("Card_Front", activityAddManuallyBinding.txtCardFront.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
            //fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") ) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        Toast.makeText(getApplicationContext(), "Add Contact request sent successfully", Toast.LENGTH_LONG).show();
                        //activityAddManuallyBinding.txtCardBack.setText(ImgName);
                    } else {
                        Toast.makeText(getApplicationContext(), "Request not send", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to send request..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String loading = "Uploading" ;
            CustomProgressDialog(loading, AddManuallyActivity.this);

        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ImgBase64", final_ImgBase64);
                jsonObject.accumulate("classification", "card");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
              dismissProgress();
           // fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        activityAddManuallyBinding.txtCardFront.setText(ImgName);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
