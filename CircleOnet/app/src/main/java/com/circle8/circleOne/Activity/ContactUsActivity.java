package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityContactUsBinding;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{


    private String subject, description, email, contactNo, contactType ;
    private LoginSession session;
    ActivityContactUsBinding activityContactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityContactUsBinding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);

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

        activityContactUsBinding.spContactType.setOnItemSelectedListener(this);


        activityContactUsBinding.imgBack.setOnClickListener(this);
        activityContactUsBinding.lnrAddress.setOnClickListener(this);
        activityContactUsBinding.ivMessage.setOnClickListener(this);
        activityContactUsBinding.ivPhone.setOnClickListener(this);
        activityContactUsBinding.lnrWebsite.setOnClickListener(this);
        activityContactUsBinding.tvSend.setOnClickListener(this);
        activityContactUsBinding.tvCancel.setOnClickListener(this);
        activityContactUsBinding.fbUrl.setOnClickListener(this);
        activityContactUsBinding.linkedInUrl.setOnClickListener(this);
        activityContactUsBinding.twitterUrl.setOnClickListener(this);
        activityContactUsBinding.googleUrl.setOnClickListener(this);
        activityContactUsBinding.youtubeUrl.setOnClickListener(this);

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
        activityContactUsBinding.spContactType.setAdapter(dataAdapter);

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
        if ( v == activityContactUsBinding.imgBack)
        {
            Utility.freeMemory();
            finish();
        }
        if ( v == activityContactUsBinding.fbUrl)
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
        if ( v == activityContactUsBinding.linkedInUrl)
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
        if ( v == activityContactUsBinding.twitterUrl)
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
        if ( v == activityContactUsBinding.googleUrl)
        {
            Utility.freeMemory();
            /*Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
            intent.putExtra("url", Utility.BASE_IMAGE_URL+"Other_doc/"+txtAttachment.getText().toString());
            startActivity(intent);*/
        }
        if ( v == activityContactUsBinding.youtubeUrl)
        {
            Utility.freeMemory();
           /* Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
            intent.putExtra("url", Utility.BASE_IMAGE_URL+"Other_doc/"+txtAttachment.getText().toString());
            startActivity(intent);*/
        }

        if ( v == activityContactUsBinding.lnrAddress)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Google Map")
                    .setMessage("Are you sure you want to redirect to Google Map ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            String map = "http://maps.google.co.in/maps?q=" + activityContactUsBinding.tvAddress1.getText().toString();
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
        if ( v == activityContactUsBinding.ivMessage)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("Mail to "+ activityContactUsBinding.tvCompanyName.getText().toString())
                    .setMessage("Are you sure you want to drop Mail ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            try
                            {
                                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + activityContactUsBinding.tvMail.getText().toString()));
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
        if ( v == activityContactUsBinding.ivPhone)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Call to "+ activityContactUsBinding.tvCompanyName.getText().toString())
                    .setMessage("Are you sure you want to make a Call ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+activityContactUsBinding.tvPhone.getText().toString()));
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
        if ( v == activityContactUsBinding.lnrWebsite)
        {
            Utility.freeMemory();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("Redirect to Web Browser")
                    .setMessage("Are you sure you want to redirect to Web Browser ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            String url = activityContactUsBinding.tvWebsite.getText().toString();
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
        if ( v == activityContactUsBinding.tvSend)
        {
            Utility.freeMemory();
            subject = activityContactUsBinding.etSubject.getText().toString();
            description = activityContactUsBinding.etDescription.getText().toString();

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
        if ( v == activityContactUsBinding.tvCancel)
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
            CustomProgressDialog(loading, ContactUsActivity.this);
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
            dismissProgress();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();

                    if (success.equals("1"))
                    {
                        activityContactUsBinding.etSubject.setText(null);
                        activityContactUsBinding.etDescription.setText(null);

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

}
