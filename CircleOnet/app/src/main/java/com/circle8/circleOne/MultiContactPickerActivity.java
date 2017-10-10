package com.circle8.circleOne;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MultiContactPickerActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    private FastScrollRecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private TextView tvSelectBtn;
    private MultiContactPickerAdapter adapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ProgressBar progressBar;
    private MenuItem searchMenuItem;
    private MultiContactPicker.Builder builder;
    String user_id, profile_id;
    LoginSession loginSession;
    JSONArray selectedStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) return;

        builder = (MultiContactPicker.Builder) intent.getSerializableExtra("builder");

        setTheme(builder.theme);

        setContentView(R.layout.activity_multi_contact_picker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select Contact");
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvSelectBtn = (TextView) findViewById(R.id.tvSelect);
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


        adapter = new MultiContactPickerAdapter(contactList, new MultiContactPickerAdapter.ContactSelectListener() {
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

        loadContacts();

        recyclerView.setAdapter(adapter);

        tvSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(EXTRA_RESULT_SELECTION, MultiContactPicker.buildResult(adapter.getSelectedContacts()));
                List<ContactResult> results = MultiContactPicker.obtainResult(result);
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < results.size(); i++) {

                    String num = results.get(i).getPhoneNumbers().toString();
                    //arrayList.add(num);
                    if (num.contains("+")){
                        num = num.replaceAll("\\+", "");
                    }

                    if (num.contains(" ")){
                        num = num.replaceAll(" ", "");
                    }
                    arrayList.add(num);
                   // Toast.makeText(getApplicationContext(), num, Toast.LENGTH_LONG).show();
                    Log.d("MyTag", results.get(i).getPhoneNumbers().toString());
                }
                selectedStrings = new JSONArray(arrayList);
                new HttpAsyncTaskImportContacts().execute("http://circle8.asia:8999/Onet.svc/ImportContacts");
               // setResult(RESULT_OK, result);
               // finish();
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
          /*  dialog = new ProgressDialog(ContactsImportActivity.this);
            dialog.setMessage("Sending Request...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Sending Request" ;
           // CustomProgressDialog(loading);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
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
