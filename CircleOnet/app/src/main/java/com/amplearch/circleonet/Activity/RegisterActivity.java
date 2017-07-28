package com.amplearch.circleonet.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.AsyncRequest;
import com.amplearch.circleonet.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AsyncRequest.OnAsyncRequestComplete
{
    private EditText etUserName, etFirstName, etLastName, etPassword, etConfirmPass, etPhone, etEmail, etDOB, etAddress;
    private LinearLayout lnrRegister;
    private ImageView ivMale, ivFemale, ivConnect ;
    private View line_view1, line_view2 ;

    private String UrlRegister = "http://circle8.asia:8081/Onet.svc/Registration";
    private ArrayList<NameValuePair> params ;

    private CircleImageView civProfilePic ;
    private String userChoosenTask ;
    CharSequence[] items ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String imagepath = null;
    private File file ;

    private String company_name, first_name, last_name, phone_no, password, user_name, gender ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lnrRegister = (LinearLayout) findViewById(R.id.lnrBottomReg);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etAddress = (EditText) findViewById(R.id.etAddress);

        ivMale = (ImageView)findViewById(R.id.ivMale);
        ivFemale = (ImageView)findViewById(R.id.ivFemale);
        ivConnect = (ImageView)findViewById(R.id.iv_ConnectImg);
        line_view1 = (View)findViewById(R.id.vwDrag1);
        line_view2 = (View)findViewById(R.id.vwDrag2);

        civProfilePic =(CircleImageView)findViewById(R.id.imgProfileCard);

        ivMale.setOnClickListener(this);
        ivFemale.setOnClickListener(this);
        ivConnect.setOnClickListener(this);
        lnrRegister.setOnClickListener(this);
        civProfilePic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if( v == ivMale)
        {
            TranslateAnimation slide1 = new TranslateAnimation(0, -160, 0,0 );
            slide1.setDuration(1000);
            ivConnect.startAnimation(slide1);

            //first things
            line_view2.setBackgroundColor(getResources().getColor(R.color.unselected));
            ivFemale.setImageResource(R.drawable.ic_female_gray);
            //second things
            line_view1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            ivMale.setImageResource(R.drawable.ic_male);

            gender = "Male" ;
        }
        if( v == ivFemale)
        {
            TranslateAnimation slide = new TranslateAnimation(0, 160, 0,0 );
            slide.setDuration(1000);
            ivConnect.startAnimation(slide);

            //first things
            line_view1.setBackgroundColor(getResources().getColor(R.color.unselected));
            ivMale.setImageResource(R.drawable.ic_male_gray);
            //second things
            line_view2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            ivFemale.setImageResource(R.drawable.ic_female);

            gender = "Female" ;

        }
        if( v == lnrRegister)
        {
            company_name = "Ample Arch";
            user_name = etEmail.getText().toString();
            first_name = etFirstName.getText().toString();
            last_name = etLastName.getText().toString();
            phone_no = etPhone.getText().toString();
            password = etPassword.getText().toString();

            new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/Registration");

//            connectWithHttpPost();

//            connectWithHttpPost(company_name,user_name,first_name,last_name,phone_no,password);

//            params = getParams();
//            AsyncRequest getPosts = new AsyncRequest(RegisterActivity.this, "GET", params);
//            getPosts.execute(UrlRegister);

//            AsyncDataClass asyncRequestObject = new AsyncDataClass();
//            asyncRequestObject.execute();
        }
        if( v == civProfilePic)
        {
            selectImage();
        }

    }

    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(RegisterActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
//                        activeTakePhoto();
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
//                        activeGallery();
                        galleryIntent();


                } else if (items[item].equals("Cancel")) {
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

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null)
        {
            try
            {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

//                file = persistImage(bm,"profile_"+ UUID.randomUUID().toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Image too large.", Toast.LENGTH_LONG).show();
            }
        }
        civProfilePic.setImageBitmap(bm);
//        BmToString(bm);
    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

//        file = persistImage(thumbnail,"profile_"+UUID.randomUUID().toString());

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        civProfilePic.setImageBitmap(thumbnail);
//        BmToString(thumbnail);
    }

    public  String POST(String url)
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
            jsonObject.accumulate("CompanyName", company_name );
            jsonObject.accumulate("FName", first_name );
            jsonObject.accumulate("LName", last_name );
            jsonObject.accumulate("Phone", phone_no);
            jsonObject.accumulate("Pwd", password);
            jsonObject.accumulate("UserName", user_name);

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Registering...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try
        {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        }
        catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void BmToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);

//        file = persistImage(bitmap,"ImageName");
//        FileBody bin1 = new FileBody(file);

//        bitmap_image = bitmap ;
//        string_image = image ;

//        Toast.makeText(getApplicationContext(),"Bitmap: "+bitmap_image,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"Image: "+string_image,Toast.LENGTH_LONG).show();
    }

    private void connectWithHttpPost(String company_name, String user_name, String first_name, String last_name,
                                     String phone_no, String password )
    {
        class HttpGetAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String company_name = params[0];
                String user_name = params[1];
                String first_name = params[2];
                String last_name = params[3];
                String phone_no = params[4];
                String password = params[5];


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://circle8.asia:8081/Onet.svc/Registration");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
//                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                nameValuePair.add(new BasicNameValuePair("CompanyName", company_name));
                nameValuePair.add(new BasicNameValuePair("FName", first_name));
                nameValuePair.add(new BasicNameValuePair("LName", last_name ));
                nameValuePair.add(new BasicNameValuePair("Phone", phone_no ));
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
                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
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
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result , Toast.LENGTH_LONG).show();

                if (result.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Check For Data Connection..", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("user");
                        if (res.equals(""))
                        {
                            Toast.makeText(getApplicationContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
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
        httpGetAsyncTask.execute(company_name,user_name,first_name,last_name,phone_no,password);
    }

    private ArrayList<NameValuePair> getParams()
    {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("CompanyName", "Ample arch"));
        nameValuePair.add(new BasicNameValuePair("FName", "Ample"));
        nameValuePair.add(new BasicNameValuePair("LName", "Arch" ));
        nameValuePair.add(new BasicNameValuePair("Phone", "9876543210" ));
        nameValuePair.add(new BasicNameValuePair("Pwd", "ample123"));
        nameValuePair.add(new BasicNameValuePair("UserName", "amplearch"));
        return nameValuePair;
    }

    @Override
    public void asyncResponse(String response)
    {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        Log.i("SignIn response: ", response);

        if (response.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Attempt Failed..", Toast.LENGTH_LONG).show();
        }

    }

    private class AsyncDataClass extends AsyncTask<Void, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Registering...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 9000);
            HttpConnectionParams.setSoTimeout(httpParameters, 9000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            String urlString = "http://circle8.asia:8081/Onet.svc/Registration";

            HttpPost httpPost = new HttpPost(urlString);

            String jsonResult = "";

            try
            {
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
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            dialog.dismiss();
            System.out.println("Resulted Value: " + result);
            Toast.makeText(getApplicationContext(), "Resulted value" + result, Toast.LENGTH_LONG).show();

            if (result.equals("") || result == null)
            {
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




}
