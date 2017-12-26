package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.Walkthrough.HelpActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class ContactsImportActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CONTACT = 111;
    ArrayList<String> arrayListPhoneNumber;
    ArrayList<String> arrayListPhoneName;
    TextView txtSend, txtCancel;
    ListView listView;
    private TextView textView;
    ImageView imgLogo;
    ArrayList<String> arrayList;

    public static JSONArray selectedStrings = new JSONArray();
    private LoginSession session;
    private String user_id, profile_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_import);
        Utility.freeMemory();
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        profile_id = user.get(LoginSession.KEY_PROFILEID);

        txtSend = (TextView) findViewById(R.id.txtSend);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        arrayList = new ArrayList<>();

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        listView = (ListView) findViewById(R.id.listContact);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        textView.setText("Contact List");
        ImageView drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setVisibility(View.GONE);
        imgLogo.setVisibility(View.GONE);
        askForContactPermission();
        /*boolean result = Utility.checkContactPermission(ContactsImportActivity.this);
        if (result) {*/
       // }

        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox chk = (CheckBox) v;
                int itemCount = listView.getCount();
                for(int i=0 ; i < itemCount ; i++){
                    listView.setItemChecked(i, chk.isChecked());
                }
                Utility.freeMemory();
            }
        };

        /** Defining click event listener for the listitem checkbox */
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                CheckBox chk = (CheckBox) findViewById(R.id.chkAll);
                int checkedItemCount = getCheckedItemCount();

                if(listView.getCount()==checkedItemCount)
                    chk.setChecked(true);
                else
                    chk.setChecked(false);
            }
        };

        /** Getting reference to checkbox available in the main.xml layout */
        CheckBox chkAll =  ( CheckBox ) findViewById(R.id.chkAll);

        /** Setting a click listener for the checkbox **/
        chkAll.setOnClickListener(clickListener);

        /** Setting a click listener for the listitem checkbox **/
        listView.setOnItemClickListener(itemClickListener);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactsImportActivity.this, CardsActivity.class));
                finish();
                Utility.freeMemory();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                System.out.println("check"+ listView.getCheckItemIds().length);
                selectedStrings = new JSONArray();
                for (int i = 0; i < listView.getCheckItemIds().length; i++)
                {
                    //listView.getAdapter().getItem((int)listView.getCheckItemIds()[i]);
                    String num = arrayListPhoneNumber.get(arrayListPhoneName.indexOf(listView.getAdapter().getItem((int)listView.getCheckItemIds()[i]).toString()));
                    //arrayList.add(num);
                    if (num.contains("+")){
                        num = num.replaceAll("\\+", "");
                    }

                    if (num.contains(" ")){
                        num = num.replaceAll(" ", "");
                    }
                    selectedStrings.put(num);
                  //  Toast.makeText(getApplicationContext(), num, Toast.LENGTH_LONG).show();
                   // Toast.makeText(getApplicationContext(), listView.getAdapter().getItem((int)listView.getCheckItemIds()[i]).toString(), Toast.LENGTH_LONG).show();
                }
               // Toast.makeText(getApplicationContext(), selectedStrings.toString(), Toast.LENGTH_LONG).show();

                new HttpAsyncTaskImportContacts().execute(Utility.BASE_URL+"ImportContacts");
                Utility.freeMemory();
            }
        });
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


    private int getCheckedItemCount()
    {
        Utility.freeMemory();
        int cnt = 0;
        SparseBooleanArray positions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();

        for(int i=0;i<itemCount;i++)
        {
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }

    public void askForContactPermission()
    {
        Utility.freeMemory();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsImportActivity.this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsImportActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                }
                else
                {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(ContactsImportActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                ReadPhoneContacts(ContactsImportActivity.this);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayListPhoneName);
                listView.setAdapter(adapter);
            }
        }
        else
        {
            ReadPhoneContacts(ContactsImportActivity.this);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayListPhoneName);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        Utility.freeMemory();
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CONTACT:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    ReadPhoneContacts(ContactsImportActivity.this);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayListPhoneName);
                    listView.setAdapter(adapter);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No permission for contacts", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void ReadPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        Utility.freeMemory();
        ProgressDialog progressDialog;
        arrayListPhoneName = new ArrayList<>();
        arrayListPhoneNumber = new ArrayList<>();

        String loading = "Loading contacts" ;
        CustomProgressDialog(loading, ContactsImportActivity.this);

        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list

        if (contactsCount > 0)
        {
           /* progressDialog = new ProgressDialog(ContactsImportActivity.this);
            progressDialog.setMessage("Loading Contacts..");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/

            while(cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext())
                    {
                        int phoneType = pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        switch (phoneType)
                        {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.e(contactName + ": TYPE_MOBILE", " " + phoneNo);

                                String number = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String name = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                                if (number.startsWith("+65") || number.startsWith("065") || number.startsWith("65") || number.length() == 8 )
                                {
                                    if (number.contains("\\+") || number.contains("-") || number.contains(" ")) {
                                       number = number.replaceAll("\\+", "");
                                    }
                                    if (number.contains("-"))
                                    {
                                        number = number.replaceAll("-", "");
                                    }
                                    if (number.contains(" "))
                                    {
                                        number = number.replaceAll(" ", "");
                                    }
                                    if (number.length() == 8){
                                        number = number = "65"+number;
                                    }
                                    number = number.trim();
                                    arrayListPhoneName.add(pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                                    arrayListPhoneNumber.add(number);
                                }
                                break;
                           /* case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.e(contactName + ": TYPE_HOME", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.e(contactName + ": TYPE_WORK", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                Log.e(contactName + ": TYPE_WORK_MOBILE", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.e(contactName + ": TYPE_OTHER", " " + phoneNo);
                                break;*/
                            default:
                                break;
                        }
                    }
                    pCursor.close();
                }
            }
            cursor.close();
//            progressDialog.dismiss();
            dismissProgress();        }
        else
        {

        }
    }

    private class HttpAsyncTaskImportContacts extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
          /*  dialog = new ProgressDialog(ContactsImportActivity.this);
            dialog.setMessage("Sending Request...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Sending request" ;
            CustomProgressDialog(loading, ContactsImportActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return ContactUploadPost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    String success = jsonObject.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(getBaseContext(), getString(R.string.successful_request_sent), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ContactsImportActivity.this, CardsActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
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

    public  String ContactUploadPost(String url)
    {
        Utility.freeMemory();
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
            jsonObject.accumulate("MobileNumber", selectedStrings );
            jsonObject.accumulate("ProfileId", profile_id );
            jsonObject.accumulate("UserId", user_id );

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


}
