package com.circle8.circleOne;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.ContactsImportActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.RxContacts.Contact;
import com.circle8.circleOne.RxContacts.RxContacts;
import com.circle8.circleOne.Utils.Utility;
import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class MultiContactPickerActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    private FastScrollRecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> contactList1 = new ArrayList<>();
    private TextView tvSelectBtn, tvCancel;
    private MultiContactPickerAdapter adapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ProgressBar progressBar;
    private MenuItem searchMenuItem;
    private MultiContactPicker.Builder builder;
    String user_id, profile_id;
    LoginSession loginSession;
    JSONArray selectedStrings;
    Cursor cursor ;
    String name, phonenumber,id ;

    ArrayList<String> StoreContacts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) return;

        builder = (MultiContactPicker.Builder) intent.getSerializableExtra("builder");

        setTheme(builder.theme);

        setContentView(R.layout.activity_multi_contact_picker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvSelectBtn = (TextView) findViewById(R.id.tvSelect);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerView);
        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        initialiseUI(builder);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MultiContactPickerAdapter(contactList1, new MultiContactPickerAdapter.ContactSelectListener() {
            @Override
            public void onContactSelected(Contact contact, int totalSelectedContacts) {
                tvSelectBtn.setEnabled(totalSelectedContacts > 0);
                if(totalSelectedContacts > 0) {
                    tvSelectBtn.setText(getString(R.string.tv_select_btn_text_enabled, String.valueOf(totalSelectedContacts)));
                } else {
                    tvSelectBtn.setText(getString(R.string.tv_select_btn_text_disabled));
                }
            }
        });

        //loadContacts();
        loadContacts1();
        recyclerView.setAdapter(adapter);

        tvSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(EXTRA_RESULT_SELECTION, MultiContactPicker.buildResult(adapter.getSelectedContacts()));
                List<ContactResult> results = MultiContactPicker.obtainResult(result);
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < results.size(); i++) {

                    String num = results.get(i).getPhoneNumbers();
                    //arrayList.add(num);
                    if (num.contains("+")){
                        num = num.replaceAll("\\+", "");
                    }

                    if (num.contains(" ")){
                        num = num.replaceAll(" ", "");
                    }
                    arrayList.add(num);
                    // Toast.makeText(getApplicationContext(), num, Toast.LENGTH_LONG).show();
                    //Log.d("MyTag", results.get(i).getPhoneNumbers().toString());
                }
                selectedStrings = new JSONArray(arrayList);
                new HttpAsyncTaskImportContacts().execute(Utility.BASE_URL+"ImportContacts");
                // setResult(RESULT_OK, result);
                // finish();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiContactPickerActivity.this, CardsActivity.class));
                finish();
            }
        });

    }

    private class HttpAsyncTaskImportContacts extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
                    String loading = "Sending Request" ;

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return ContactUploadPost(urls[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            // rlProgressDialog.setVisibility(View.GONE);
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
                        startActivity(new Intent(MultiContactPickerActivity.this, CardsActivity.class));
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



    private void initialiseUI(MultiContactPicker.Builder builder){
        setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(this);
        if(builder.bubbleColor != 0)
            recyclerView.setBubbleColor(builder.bubbleColor);
        if(builder.handleColor != 0)
            recyclerView.setHandleColor(builder.handleColor);
        if(builder.bubbleTextColor != 0)
            recyclerView.setBubbleTextColor(builder.bubbleTextColor);
        if(builder.trackColor != 0)
            recyclerView.setTrackColor(builder.trackColor);
        recyclerView.setHideScrollbar(builder.hideScrollbar);
        recyclerView.setTrackVisible(builder.showTrack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadContacts1(){

        contactList.clear();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {
            Contact contact =new Contact();

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            contact.setDisplayName(name);
            contact.setmPhoneNumbers(phonenumber);
            contact.setContact_id(id);
            contactList.add(contact);

        }
         contactList1.clear();

        for(int i=0;i<contactList.size();i++)
        {
            if(i==0)
            {
                Contact contact = new Contact();
                contact.setDisplayName(contactList.get(i).getDisplayName());
                contact.setmPhoneNumbers(contactList.get(i).getmPhoneNumbers());
                contact.setContact_id(contactList.get(i).getContact_id());
                contactList1.add(contact);

            }
            else {

                if(!contactList.get(i).getDisplayName().equalsIgnoreCase(contactList.get(i-1).getDisplayName())&&!contactList.get(i).getmPhoneNumbers().equalsIgnoreCase(contactList.get(i-1).getmPhoneNumbers()))
                {
                    Contact contact = new Contact();
                    contact.setDisplayName(contactList.get(i).getDisplayName());
                    contact.setmPhoneNumbers(contactList.get(i).getmPhoneNumbers());
                    contact.setContact_id(contactList.get(i).getContact_id());
                    contactList1.add(contact);
                }
            }
        }

        Collections.sort(contactList1, new Comparator<Contact>() {
            public int compare(Contact o1, Contact o2) {
                return o1.mDisplayName.compareTo(o2.mDisplayName);
            }
        });
        if(adapter != null && contactList1.size() > 0){
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
        cursor.close();


    }
    private void loadContacts(){
        progressBar.setVisibility(View.VISIBLE);
        RxContacts.fetch(this)
                .filter(new Predicate<Contact>() {
                    @Override
                    public boolean test(Contact contact) throws Exception {
                        return contact.getDisplayName() != null;
                    }
                })
                .toSortedList(new Comparator<Contact>() {
                    @Override
                    public int compare(Contact contact, Contact t1) {
                        return contact.getDisplayName().compareToIgnoreCase(t1.getDisplayName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SingleObserver<List<Contact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(List<Contact> contacts) {
                        contactList.clear();
                        contactList.addAll(contacts);
                        if(adapter != null && contacts.size() > 0){
                            adapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.mcp_action_search);
        setSearchIconColor(searchMenuItem, builder.searchIconColor);
        searchView.setMenuItem(searchMenuItem);
        return true;
    }

    private void setSearchIconColor(MenuItem menuItem, final Integer color) {
        if(color != null) {
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), color);
                menuItem.setIcon(drawable);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(adapter != null){
            adapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter != null){
            adapter.getFilter().filter(newText);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            startActivity(new Intent(MultiContactPickerActivity.this, CardsActivity.class));
            finish();
        }
    }
}
