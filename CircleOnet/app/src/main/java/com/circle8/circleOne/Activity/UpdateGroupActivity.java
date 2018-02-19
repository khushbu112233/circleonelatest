package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.ConvertBitmapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.rotateImage;
import static com.circle8.circleOne.Utils.Utility.POST2;

public class UpdateGroupActivity extends AppCompatActivity
{
    CircleImageView ivGroupImage;
    ImageView ivMiniCamera;
    EditText etCircleName, etCircleDesc;
    TextView tvCreateOrUpdate, tvCancel, tvCircleNameInfo, tvCircleDescInfo, tvProfileInfo;

    LoginSession session;
    String profile_id, user_id, group_id;
    String GroupName, GroupDesc, GroupImage = "", final_ImgBase64 = "";

    CharSequence[] items;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String image;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        user_id = user.get(LoginSession.KEY_USERID);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        ivGroupImage = (CircleImageView) findViewById(R.id.imgProfile);
        ivMiniCamera = (ImageView) findViewById(R.id.imgCamera);
        etCircleName = (EditText) findViewById(R.id.etCircleName);
        etCircleDesc = (EditText) findViewById(R.id.etCircleDesc);
        tvCreateOrUpdate = (TextView) findViewById(R.id.tvCreateOrUpdate);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCircleNameInfo = (TextView) findViewById(R.id.tvCircleNameInfo);
        tvCircleDescInfo = (TextView) findViewById(R.id.tvCircleDescInfo);
        tvProfileInfo = (TextView) findViewById(R.id.tvProfileInfo);
        tvCreateOrUpdate.setText("Update");

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        Intent iget = getIntent();
        etCircleName.setText(iget.getStringExtra("GroupName"));
        etCircleDesc.setText(iget.getStringExtra("GroupDesc"));
        group_id = iget.getStringExtra("GroupID");

//        Toast.makeText(getApplicationContext(),"GroupID: "+group_id, Toast.LENGTH_SHORT).show();

        final String groupPhoto = iget.getStringExtra("GroupImage");

        if (groupPhoto.equals("")) {
            ivGroupImage.setImageResource(R.drawable.usr_1);
        } else {
            Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"Group/" + groupPhoto).placeholder(R.drawable.usr_1).resize(300,300).onlyScaleDown().skipMemoryCache().into(ivGroupImage);
        }

        ivGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UpdateGroupActivity.this);
            }
        });

        ivMiniCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectImage();
                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UpdateGroupActivity.this);
            }
        });

        tvCreateOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupName = etCircleName.getText().toString();
                GroupDesc = etCircleDesc.getText().toString();

                if (GroupName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Circle Name", Toast.LENGTH_LONG).show();
//                            tvCircleNameInfo.setVisibility(View.VISIBLE);
                } else if (GroupDesc.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Circle Description", Toast.LENGTH_LONG).show();
//                            tvCircleDescInfo.setVisibility(View.VISIBLE);
                } else
                {
                    if (final_ImgBase64.equals(""))
                    {
                        GroupImage = groupPhoto;
                        new HttpAsyncTaskGroupUpdate().execute(Utility.BASE_URL+"Group/Update");
                        // Toast.makeText(getApplicationContext(), "Upload Circle Image", Toast.LENGTH_LONG).show();
//                            tvProfileInfo.setVisibility(View.VISIBLE);
                    }else {
                        new HttpAsyncTaskPhotoUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                    // new HttpAsyncTaskGroupCreate().execute("http://circle8.asia:8999/Onet.svc/Group/Create");
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GroupsActivity.backStatus = "UpdateBack";
                finish();
                GroupsActivity.ivAlphaImg.setVisibility(View.GONE);
            }
        });

    }



    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(UpdateGroupActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ImgBase64", final_ImgBase64);
                jsonObject.accumulate("classification", "group");
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
                        GroupImage = ImgName;
                        new HttpAsyncTaskGroupUpdate().execute(Utility.BASE_URL+"Group/Update");

                    } else {
                        Toast.makeText(getApplicationContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskGroupUpdate extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            String loading = "Updating circle" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupDesc", GroupDesc);
                jsonObject.accumulate("GroupName", GroupName);
                jsonObject.accumulate("GroupPhoto", GroupImage);
                jsonObject.accumulate("UserId", user_id);
                jsonObject.accumulate("GroupId", group_id);

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
                    String Success = jsonObject.getString("Success").toString();
                    String Message = jsonObject.getString("Message").toString();
                    if (Success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Circle Updated..", Toast.LENGTH_LONG).show();
                        if (type.equalsIgnoreCase("group"))
                        {
                            GroupsActivity.backStatus = "UpdateGroup";
                            finish();
                            GroupsActivity.ivAlphaImg.setVisibility(View.GONE);
                        }
                        else if (type.equals("group_detail"))
                        {
                            Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                            intent.putExtra("group_id", group_id);
                            intent.putExtra("groupName", GroupName);
                            intent.putExtra("groupDesc", GroupDesc);
                            intent.putExtra("groupImg", GroupImage);
                            startActivity(intent);
                            finish();
                        }
                       /* tvGroupName.setText(GroupName);
                        tvGroupDesc.setText(GroupDesc);

                        if (GroupImage.equals(""))
                        {
                            imgProfile.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+GroupImage).placeholder(R.drawable.usr_1).into(imgProfile);
                        }*/
                    } else {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to create Circle..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

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


                    image = ConvertBitmapToString(bitmap);
                    final_ImgBase64 = BitMapToString(bitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                    ivGroupImage.setImageBitmap(bitmap);

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
        //  Upload();
        ivGroupImage.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            Uri targetUri = data.getData();

            ExifInterface ei = null;
            Bitmap bitmap = null;
            Bitmap rotatedBitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    final_ImgBase64 = BitMapToString(resizedBitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                    // Upload();
                    ivGroupImage.setImageBitmap(resizedBitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    ei = new ExifInterface(String.valueOf(targetUri));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            //  Upload();
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            //  Upload();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
//        BmToString(bm);
    }

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading+"...");

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);
    }
}
