package com.amplearch.circleonet.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.EditProfileActivity;
import com.amplearch.circleonet.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
{
    private ProgressBar firstBar = null;
    ImageView imgProfileShare, imgProfileMenu, imgQR, ivEditProfile;
    TextView tvPersonName ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    ProgressDialog progressDialog ;

    private int i = 0;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
      //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Generating Qr Code...");
        progressDialog.setCancelable(false);
        imgQR = (ImageView) view.findViewById(R.id.imgQR);
        firstBar = (ProgressBar)view.findViewById(R.id.firstBar);
        tvPersonName = (TextView)view.findViewById(R.id.tvPersonName);
        imgProfileShare = (ImageView) view.findViewById(R.id.imgProfileShare);
        imgProfileMenu = (ImageView) view.findViewById(R.id.imgProfileMenu);
        ivEditProfile = (ImageView)view.findViewById(R.id.ivEditProfile);
        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetUserProfile");

        imgProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hello, This is Westley Wan working as General Manager at Unico Creative.";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Westley Wan Profile");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });

        imgProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), imgProfileMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().toString().equals("Add New Profile")){
                            new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8081/Onet.svc/AddProfile");

                        }
                        return true;
                    }
                });

                popup.show();//showing popup men
        }
        });

        imgQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Generating QR Code.. Please Wait..", Toast.LENGTH_LONG).show();
                String barName = "Westley Wan";
                try
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                    ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                    tvBarName.setText(barName);
                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(bitmap);
                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }

//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Generating Qr Code...");
//                progressDialog.setCancelable(false);



               /* try
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                    ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                    tvBarName.setText(barName);
                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(bitmap);
                    alertDialog.setView(dialogView);
                    progressDialog.dismiss();
                    alertDialog.show();
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }*/

            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getContext(),"Edit Profile",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        return view;
    }

    public  String POST4(String url)
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
            jsonObject.accumulate("AssociationID", "1" );
            jsonObject.accumulate("Card_Back", "000000002.jpg" );
            jsonObject.accumulate("Card_Front", "000000002.jpg" );
            jsonObject.accumulate("CompanyID", "1" );
            jsonObject.accumulate("CompanyName", "Circle One" );
            jsonObject.accumulate("Designation", "Director" );
            jsonObject.accumulate("DesignationID", "1" );
            jsonObject.accumulate("Email", "kajal.patadia@ample-arch.com" );
            jsonObject.accumulate("Email_Type", "gmail" );
            jsonObject.accumulate("IndustryID", "2" );
            jsonObject.accumulate("IndustryName", "IT" );
            jsonObject.accumulate("Phone", "+6588559632" );
            jsonObject.accumulate("Phone_type", "mobile" );
            jsonObject.accumulate("Profile_Desc", "fbvfbvvvf" );
            jsonObject.accumulate("UserID", "25" );

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


    private class HttpAsyncTaskAddProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST4(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
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
            jsonObject.accumulate("profileid", "1" );
            jsonObject.accumulate("userid", "1" );
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

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
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }


    Bitmap TextToImageEncode(String Value) throws WriterException
    {
        BitMatrix bitMatrix;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null  );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }*/

}
