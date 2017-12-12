package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.RxContacts.Contact;
import com.circle8.circleOne.Utils.GeocodingLocation;
import com.circle8.circleOne.Utils.Utility;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
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
import java.util.List;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    private ImageView ivMessage, ivPhone ;
    private TextView tvAddress1, tvAddress2, tvWebsite, tvEmail, tvPhone, tvFax ;
    private TextView tvCompany, tvPartner, tvSend, tvCancel;
    private EditText etSubject, etDescription ;
    private ImageView imgBack;
    private LinearLayout lnrAddress, lnrEmail, lnrContact, lnrWebsite ;
    private ImageView fbUrl, linkedInUrl, twitterUrl, googleUrl, youtubeUrl;
    private Spinner spContactType ;

    private String subject, description, email, contactNo, contactType ;
    private LoginSession session;

    private RelativeLayout rlProgressDialog;
    private TextView tvProgressing;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Utility.freeMemory();

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(LoginSession.KEY_EMAIL);
        try
        {
            if (user.get(LoginSession.KEY_PHONE).contains(" "))
            {
                String name = user.get(LoginSession.KEY_PHONE);
                String kept = name.substring(0, name.indexOf(" "));
                String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                kept = kept.replaceAll("//+", "");

                contactNo = kept+" "+remainder ;
            }
            else
            {
                contactNo = user.get(LoginSession.KEY_PHONE) ;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvCompany = (TextView)findViewById(R.id.tvCompanyName);
        tvPartner = (TextView)findViewById(R.id.tvPartner);
        tvAddress1 = (TextView)findViewById(R.id.tvAddress1);
        tvWebsite = (TextView)findViewById(R.id.tvWebsite);
        tvEmail = (TextView)findViewById(R.id.tvMail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvFax = (TextView)findViewById(R.id.tvWork);
        tvSend = (TextView)findViewById(R.id.tvSend);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        etSubject = (EditText)findViewById(R.id.etSubject);
        etDescription = (EditText)findViewById(R.id.etDescription);

        lnrAddress = (LinearLayout)findViewById(R.id.lnrAddress);
        lnrEmail = (LinearLayout)findViewById(R.id.llMailBox);
        lnrContact = (LinearLayout)findViewById(R.id.lnrPhone);
        lnrWebsite = (LinearLayout)findViewById(R.id.lnrWebsite);
        ivMessage = (ImageView) findViewById(R.id.ivMessage);
        ivPhone = (ImageView) findViewById(R.id.ivPhone);
        fbUrl = (ImageView) findViewById(R.id.fbUrl);
        linkedInUrl = (ImageView) findViewById(R.id.linkedInUrl);
        twitterUrl = (ImageView) findViewById(R.id.twitterUrl);
        googleUrl = (ImageView) findViewById(R.id.googleUrl);
        youtubeUrl = (ImageView) findViewById(R.id.youtubeUrl);

        spContactType = (Spinner)findViewById(R.id.spContactType);
        spContactType.setOnItemSelectedListener(this);

        rlProgressDialog = (RelativeLayout) findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView) findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView) findViewById(R.id.imgConnecting1);
        ivConnecting2 = (ImageView) findViewById(R.id.imgConnecting2);
        ivConnecting3 = (ImageView) findViewById(R.id.imgConnecting3);

        imgBack.setOnClickListener(this);
        lnrAddress.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        lnrWebsite.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        fbUrl.setOnClickListener(this);
        linkedInUrl.setOnClickListener(this);
        twitterUrl.setOnClickListener(this);
        googleUrl.setOnClickListener(this);
        youtubeUrl.setOnClickListener(this);

        // Spinner Drop down elements
        List<String> contactType = new ArrayList<String>();
        contactType.add("Contact Type");
        contactType.add("General");
        contactType.add("Sales");
        contactType.add("Accounts");
        contactType.add("Marketing");
        contactType.add("Business Development");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contactType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spContactType.setAdapter(dataAdapter);

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
    public void onClick(View v)
    {
        if ( v == imgBack)
        {
            Utility.freeMemory();
            finish();
        }
        if ( v == fbUrl)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("CircleOne")
                    .setMessage("Are you sure you want to exit to Facebook?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Utility.freeMemory();
                            Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
                            intent.putExtra("url", "https://www.facebook.com/circleoneasia/");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
        if ( v == linkedInUrl)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("CircleOne")
                    .setMessage("Are you sure you want to exit to LinkedIn?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Utility.freeMemory();
                            Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
                            intent.putExtra("url", "https://www.linkedin.com/company/13454864/");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
        if ( v == twitterUrl)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("CircleOne")
                    .setMessage("Are you sure you want to exit to Twitter?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Utility.freeMemory();
                            Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
                            intent.putExtra("url", "https://twitter.com/Circle8Asia");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
        if ( v == googleUrl)
        {
            Utility.freeMemory();
            /*Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
            intent.putExtra("url", Utility.BASE_IMAGE_URL+"Other_doc/"+txtAttachment.getText().toString());
            startActivity(intent);*/
        }
        if ( v == youtubeUrl)
        {
            Utility.freeMemory();
           /* Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
            intent.putExtra("url", Utility.BASE_IMAGE_URL+"Other_doc/"+txtAttachment.getText().toString());
            startActivity(intent);*/
        }

        if ( v == lnrAddress)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Google Map")
                    .setMessage("Are you sure you want to redirect to Google Map ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            String map = "http://maps.google.co.in/maps?q=" + tvAddress1.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
        if ( v == ivMessage)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("Mail to "+ tvCompany.getText().toString())
                    .setMessage("Are you sure you want to drop Mail ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            try
                            {
                                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + tvEmail.getText().toString()));
                                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                intent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(intent);
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
        }
        if ( v == ivPhone)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Call to "+ tvCompany.getText().toString())
                    .setMessage("Are you sure you want to make a Call ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+tvPhone.getText().toString()));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_menu_call)
                    .show();
        }
        if ( v == lnrWebsite)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("Redirect to Web Browser")
                    .setMessage("Are you sure you want to redirect to Web Browser ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            String url = tvWebsite.getText().toString();
                            if (url!=null) {
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    url = "http://" + url;
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_menu_set_as)
                    .show();
        }
        if ( v == tvSend)
        {
            Utility.freeMemory();
            subject = etSubject.getText().toString();
            description = etDescription.getText().toString();

            if (contactType.equalsIgnoreCase("Contact Type"))
            {
                Toast.makeText(getApplicationContext(),"Select Contact Type",Toast.LENGTH_SHORT).show();
            }
            else if (subject.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Enter Subject",Toast.LENGTH_SHORT).show();
            }
            else if (description.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Enter Description",Toast.LENGTH_SHORT).show();
            }
            else
            {
               /* Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"general@circle.asia"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, description);
                startActivity(Intent.createChooser(emailIntent, "Choose mail options..."));*/

                new HttpAsyncTaskSendMessage().execute("http://circle8.asia:8082/Onet.svc/ContactUs");
            }
        }
        if ( v == tvCancel)
        {
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        contactType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class HttpAsyncTaskSendMessage extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(ContactUsActivity.this);
            dialog.setMessage("Sending...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Sending" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return SendMessagePost(urls[0]);
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
                        etSubject.setText(null);
                        etDescription.setText(null);

                        Toast.makeText(getApplicationContext(),"Send Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String SendMessagePost(String url) {
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
            jsonObject.accumulate("Contact_Type", contactType);
            jsonObject.accumulate("Desc", description);
            jsonObject.accumulate("Email", email);
            jsonObject.accumulate("Mobile", contactNo);
            jsonObject.accumulate("Subject", subject);

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

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(ContactUsActivity.this,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(ContactUsActivity.this,R.anim.clockwise);
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


}
