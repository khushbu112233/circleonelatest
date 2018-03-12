package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityAddManuallyBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddManuallyActivity extends AppCompatActivity implements View.OnClickListener
{

    ActivityAddManuallyBinding activityAddManuallyBinding ;
    List<String> scanTextLineList = new ArrayList<>();
    static String cardType = "";
    String NAME = "", EMAIL = "", PHONE = "", WEBSITE = "", ADDRESS = "", COMPANY = "";
    boolean result, result1;
    private static String final_ImgBase64 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddManuallyBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_manually);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        result= Utility.checkPermission(AddManuallyActivity.this);
        result1=Utility.checkCameraPermission(AddManuallyActivity.this);
        activityAddManuallyBinding.tvMoreInfo.setOnClickListener(this);
        activityAddManuallyBinding.imgBack.setOnClickListener(this);
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

                       // HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                    else if (cardType.equals("back")) {

                       // HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
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
}
