package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.GroupDetailAdapter;
import com.circle8.circleOne.Adapter.GroupsItemsAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.ConvertBitmapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.rotateImage;

public class GroupDetailActivity extends AppCompatActivity
{
    private ListView listView ;

    private CircleImageView imgProfile ;
    private ImageView ivChangeProfImg, ivBackImg, ivMenuImg, ivShareImg, ivEditImg ;
    private  TextView tvGroupName, tvGroupDesc, tvMemberInfo ;

    private GroupDetailAdapter groupDetailAdapter ;
    ImageView imgBack;
    private ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();

    private LoginSession session;
    private String profile_id, user_id ;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> website = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> imgprofile = new ArrayList<>();

    String group_id = "", group_Name, group_Desc, group_Img ;

    CircleImageView ivGroupImage ;
    String GroupName, GroupDesc, GroupImage = "";
    String final_ImgBase64 = "";

    CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        listView = (ListView)findViewById(R.id.listView);
        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvGroupName = (TextView)findViewById(R.id.tvGroupName);
        tvGroupDesc = (TextView)findViewById(R.id.tvGroupPartner);
        tvMemberInfo = (TextView)findViewById(R.id.tvMemberInfo);

        ivMenuImg = (ImageView)findViewById(R.id.imgProfileMenu);
        ivChangeProfImg = (ImageView)findViewById(R.id.imgCamera);
        ivShareImg = (ImageView)findViewById(R.id.ivProfileShare);
        ivEditImg = (ImageView)findViewById(R.id.ivEdit);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        user_id = user.get(LoginSession.KEY_USERID);

        Intent intent = getIntent();
        group_id = intent.getStringExtra("group_id");
        group_Name = intent.getStringExtra("groupName");
        group_Desc = intent.getStringExtra("groupDesc");
        group_Img = intent.getStringExtra("groupImg");

        tvGroupName.setText(group_Name);
        tvGroupDesc.setText(group_Desc);

        if (group_Img.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/Group/"+group_Img).placeholder(R.drawable.usr_1).into(imgProfile);
        }

        new HttpAsyncTaskGroup().execute("http://circle8.asia:8999/Onet.svc/Group/FetchConnection");

        /*name.add("Kajal Patadia");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Jay Nagar");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Sameer Desai");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Nagar Joy");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");
*/

      /*  groupDetailAdapter = new GroupDetailAdapter(getApplicationContext(), R.layout.group_detail_items,
                name,designation,company,website,email,phone,mobile,address,imgprofile);
        listView.setAdapter(groupDetailAdapter);
        groupDetailAdapter.notifyDataSetChanged();*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LinearLayout layout = new LinearLayout(GroupDetailActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                ivGroupImage = new CircleImageView(GroupDetailActivity.this);
                ivGroupImage.setBorderColor(getResources().getColor(R.color.colorPrimary));
                ivGroupImage.setBorderWidth(1);
                ivGroupImage.setImageResource(R.drawable.usr_1);
                int width=200;
                int height=200;
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
                parms.gravity = Gravity.CENTER;
                ivGroupImage.setLayoutParams(parms);
                layout.addView(ivGroupImage);

                final EditText titleBox = new EditText(GroupDetailActivity.this);
                titleBox.setText(group_Name);
                layout.addView(titleBox);

                final EditText descriptionBox = new EditText(GroupDetailActivity.this);
                descriptionBox.setText(group_Desc);
                layout.addView(descriptionBox);


                ivGroupImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        selectImage();
                    }
                });
                //   dialog.setView(layout);

                //text_entry is an Layout XML file containing two text field to display in alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(GroupDetailActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Update Circle").setView(layout).setPositiveButton("Update",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                GroupName = titleBox.getText().toString();
                                GroupDesc = descriptionBox.getText().toString();

                                if (GroupName.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter Circle Name", Toast.LENGTH_LONG).show();
                                }
                                else if (GroupDesc.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter Circle Description", Toast.LENGTH_LONG).show();
                                }
                                else if (final_ImgBase64.equals("")){
                                    Toast.makeText(getApplicationContext(), "Upload Circle Image", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    new HttpAsyncTaskPhotoUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                                    // new HttpAsyncTaskGroupCreate().execute("http://circle8.asia:8999/Onet.svc/Group/Create");
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                 /*
                                 * User clicked cancel so do some stuff
                                 */
                                dialog.dismiss();

                            }
                        });
                alert.show();
            }
        });
    }

    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
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
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName!=null)
                    {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        //   Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        GroupImage = ImgName;
                        new HttpAsyncTaskGroupUpdate().execute("http://circle8.asia:8999/Onet.svc/Group/Update");

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
            jsonObject.accumulate("classification", "group" );

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

    private class HttpAsyncTaskGroupUpdate extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Creating Circle...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return GroupUpdatePost(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
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

                        tvGroupName.setText(GroupName);
                        tvGroupDesc.setText(GroupDesc);

                        if (GroupImage.equals(""))
                        {
                            imgProfile.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/Group/"+GroupImage).placeholder(R.drawable.usr_1).into(imgProfile);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to create Circle..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GroupUpdatePost(String url)
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
            jsonObject.accumulate("GroupDesc", GroupDesc);
            jsonObject.accumulate("GroupName", GroupName);
            jsonObject.accumulate("GroupPhoto", GroupImage);
            jsonObject.accumulate("UserId", user_id);
            jsonObject.accumulate("GroupId", group_id);

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





    public String POST4(String url) {
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
            jsonObject.accumulate("group_ID", group_id);
            jsonObject.accumulate("profileId", profile_id);
            jsonObject.accumulate("pageno", "1");
            jsonObject.accumulate("numofrecords", "50");

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Fetching Connections...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST4(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        tvMemberInfo.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvMemberInfo.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        GroupDetailModel groupDetailModel = new GroupDetailModel();
                        groupDetailModel.setFirstname(object.getString("FirstName"));
                        groupDetailModel.setLastname(object.getString("LastName"));
                        groupDetailModel.setDesignation(object.getString("Designation"));
                        groupDetailModel.setCompany(object.getString("CompanyName"));
                        groupDetailModel.setEmail(object.getString("UserName"));
                        groupDetailModel.setWebsite(object.getString("Website"));
                        groupDetailModel.setMobile(object.getString("Phone"));
                        groupDetailModel.setAddress1(object.getString("Address1"));
                        groupDetailModel.setAddress2(object.getString("Address2"));
                        groupDetailModel.setAddress3(object.getString("Address3"));
                        groupDetailModel.setAddress4(object.getString("Address4"));
                        groupDetailModel.setCity(object.getString("City"));
                        groupDetailModel.setState(object.getString("State"));
                        groupDetailModel.setCountry(object.getString("Country"));
                        groupDetailModel.setPostalcode(object.getString("Postalcode"));
                        groupDetailModel.setImgProfile(object.getString("UserPhoto"));
                        groupDetailModelArrayList.add(groupDetailModel);
                    }

                    groupDetailAdapter = new GroupDetailAdapter(getApplicationContext(), R.layout.group_detail_items, groupDetailModelArrayList);
                    listView.setAdapter(groupDetailAdapter);
                    groupDetailAdapter.notifyDataSetChanged();

                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(GroupDetailActivity.this);
                boolean result1 = Utility.checkCameraPermission(GroupDetailActivity.this);
                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask ="Take Photo";
                    if(result1)
//                        activeTakePhoto();
                        cameraIntent();
                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask ="Choose from Library";
                    if(result)
//                        activeGallery();
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
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final_ImgBase64 = BitMapToString(thumbnail);
        //  Upload();
        ivGroupImage.setImageBitmap(thumbnail);
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                    // Upload();
                    ivGroupImage.setImageBitmap(resizedBitmap);
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
                }
                catch (Exception e)
                {
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


}
