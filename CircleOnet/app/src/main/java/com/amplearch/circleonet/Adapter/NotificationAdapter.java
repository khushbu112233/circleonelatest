package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.Notification;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.NotificationModel;
import com.amplearch.circleonet.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 08/29/2017.
 */

public class NotificationAdapter extends BaseAdapter
{
    private Context activity;
    ArrayList<NotificationModel> testimonialModels;
    private static LayoutInflater inflater = null;
    LinearLayout lnrTestReq, lnrTestRec, lnrFriend;
    private int posi = 0;
    LoginSession loginSession;
    String profileId = "", UserId = "";
    String accept = "";

    String testimonial;
    EditText etTextMonial ;

    public NotificationAdapter(Context a, ArrayList<NotificationModel> testimonialModels)
    {
        this.activity = a;
        this.testimonialModels = testimonialModels;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return testimonialModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.notification_item, null);

        loginSession = new LoginSession(activity);
        HashMap<String, String> user = loginSession.getUserDetails();

        profileId = user.get(LoginSession.KEY_PROFILEID);
        UserId = user.get(LoginSession.KEY_USERID);
        lnrFriend = (LinearLayout) vi.findViewById(R.id.lnrFriend);
        lnrTestReq = (LinearLayout) vi.findViewById(R.id.lnrTestReq);
        lnrTestRec = (LinearLayout) vi.findViewById(R.id.lnrTestRec);

        CircleImageView imgTestReq, imgTestRec, imgFriend;
        TextView txtTestPurpose, txtTestName, txtTestPurposeRec, txtTestNameRec, txtFriendPurpose, txtFriendName;
        Button btnTestWrite, btnTestReject, btnTestAcceptRec, btnTestRejectRec, btnAcceptFriend, btnRejectFriend;

        imgTestReq = (CircleImageView) vi.findViewById(R.id.imgTestReq);
        imgTestRec = (CircleImageView) vi.findViewById(R.id.imgTestRec);
        imgFriend = (CircleImageView) vi.findViewById(R.id.imgFriend);

        txtTestPurpose = (TextView) vi.findViewById(R.id.txtTestPurpose);
        txtTestName = (TextView) vi.findViewById(R.id.txtTestName);
        txtTestPurposeRec = (TextView) vi.findViewById(R.id.txtTestPurposeRec);
        txtTestNameRec = (TextView) vi.findViewById(R.id.txtTestNameRec);
        txtFriendPurpose = (TextView) vi.findViewById(R.id.txtFriendPurpose);
        txtFriendName = (TextView) vi.findViewById(R.id.txtFriendName);

        btnTestWrite = (Button) vi.findViewById(R.id.btnTestWrite);
        btnTestReject = (Button) vi.findViewById(R.id.btnTestReject);
        btnTestAcceptRec = (Button) vi.findViewById(R.id.btnTestAcceptRec);
        btnTestRejectRec = (Button) vi.findViewById(R.id.btnTestRejectRec);
        btnAcceptFriend = (Button) vi.findViewById(R.id.btnAcceptFriend);
        btnRejectFriend = (Button) vi.findViewById(R.id.btnRejectFriend);

        posi = position;
        String purpose = testimonialModels.get(position).getPurpose();

        if (purpose.equalsIgnoreCase("Recieved Testimonial"))
        {
            lnrTestRec.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.GONE);

            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                imgTestRec.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(imgTestRec);
            }
            txtTestPurposeRec.setText(purpose);
            txtTestNameRec.setText(testimonialModels.get(position).getFirstName());
        }
        else if (purpose.equalsIgnoreCase("Connection Requested"))
        {
            lnrFriend.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                imgFriend.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(imgFriend);
            }
            txtFriendPurpose.setText(purpose);
            txtFriendName.setText(testimonialModels.get(position).getFirstName());
        }
        else if (purpose.equalsIgnoreCase("Recieved Testimonial Request"))
        {
            lnrFriend.setVisibility(View.GONE);
            lnrTestReq.setVisibility(View.VISIBLE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                imgTestReq.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(imgTestReq);
            }
            txtTestPurpose.setText(purpose);
            txtTestName.setText(testimonialModels.get(position).getFirstName());
        }

        btnTestWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
//                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.textimonial_write, null);

                etTextMonial = (EditText)dialogView.findViewById(R.id.etTestiMonial);
                Button btnWrite = (Button)dialogView.findViewById(R.id.btnWrite);
                Button btnCancel = (Button)dialogView.findViewById(R.id.btnCancel);

                btnWrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final String text = etTextMonial.getText().toString();
                        testimonial = text ;
                        if(testimonial.isEmpty())
                        {
                            etTextMonial.setError("Please write testimonial");
                        }
                        else
                        {
                            etTextMonial.setText(null);
                            alertDialog.dismiss();
                            new HttpAsyncWriteTextimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Write");
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        btnAcceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTaskAcceptFriend().execute("http://circle8.asia:8081/Onet.svc/FriendConnection_Operation");
            }
        });

        btnRejectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTaskRejectFriend().execute("http://circle8.asia:8081/Onet.svc/FriendConnection_Operation");
            }
        });

        btnTestAcceptRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = "1";
                new HttpAsyncTaskAcceptTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Accept_Reject");
            }
        });

        btnTestRejectRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = "0";
                new HttpAsyncTaskAcceptTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Accept_Reject");
            }
        });

        btnTestReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept = "0";
                new HttpAsyncTaskAcceptTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Accept_Reject");
            }
        });

        return vi;

    }

    private class HttpAsyncTaskAcceptTestimonial extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Please Wait..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTAcceptTest(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        if (accept.equals("1"))
                        {
                            Toast.makeText(activity, "Testimonial Accepted..", Toast.LENGTH_LONG).show();
                            Notification.webCall();
                        }
                        else if (accept.equals("0"))
                        {
                            Toast.makeText(activity, "Testimonial Rejected..", Toast.LENGTH_LONG).show();
                            Notification.webCall();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskRejectFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Please Wait..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTReject(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Friend Request Rejected..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncWriteTextimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Writing Testimonial..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POSTWriteTextimonial(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result.equals(""))
                {
                    Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("TestimonialId");
                    String success = response.getString("Success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Testimonial Written Successfully..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskAcceptFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Accepting Friend Request..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Friend Request Accepted..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url)
    {
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
            jsonObject.accumulate("Operation", "Accept");
            jsonObject.accumulate("friendProfileId", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myProfileId", profileId);

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

    public String POSTWriteTextimonial(String url)
    {
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
            jsonObject.accumulate("Testimonial_text", testimonial);
            jsonObject.accumulate("friendprofileID", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myprofileID", profileId);

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


    public String POSTAcceptTest(String url) {
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
            jsonObject.accumulate("Accept", accept);
            jsonObject.accumulate("TestimonialId", testimonialModels.get(posi).getTestimonial_ID());
            jsonObject.accumulate("userId", UserId);

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

    public String POSTReject(String url) {
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
            jsonObject.accumulate("Operation", "Reject");
            jsonObject.accumulate("friendProfileId", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myProfileId", profileId);

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

}