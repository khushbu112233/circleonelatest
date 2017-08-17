package com.amplearch.circleonet.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Activity.ConnectActivity;
import com.amplearch.circleonet.Adapter.List4Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CustomViewPager;
import com.amplearch.circleonet.ZoomOutPageTransformer;

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
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectListFragment extends Fragment {

    private ListView listView;
    private List4Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;

    List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    ArrayList<NFCModel> nfcModel ;

    public ConnectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect_list, container, false);

        /*List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getUser_image());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }
*/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        listView = (ListView) view.findViewById(R.id.listViewType4);
        listView.setVisibility(View.GONE);
        allTags = new ArrayList<>();

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();
        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       // gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, imgf, desc, name, designation);
        //listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                intent.putExtra("tag_id", nfcModel.get(position).getNfc_tag());
                getContext().startActivity(intent);
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() <= 0)
                {
                    listView.setVisibility(View.GONE);
                    nfcModel.clear();
                    GetData();

                }
                else
                {
                    listView.setVisibility(View.VISIBLE);
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                    new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/SearchConnect");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
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
            jsonObject.accumulate("FindBy", "name" );
            jsonObject.accumulate("Search", searchText.getText().toString() );
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", "1" );

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
            dialog.setMessage("Searching Records...");
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


    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel.clear();
        GetData();
    }

    private void GetData()
    {
        nfcModel.clear();
        for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setUser_image(reTag.getUser_image());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            nfcModel.add(nfcModelTag);
        }
        gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, nfcModel);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseHelper(getContext());
            imgf = new ArrayList<byte[]>();
            name = new ArrayList<>();
            desc = new ArrayList<>();
          //  allTags = new ArrayList<>();
            designation = new ArrayList<>();

            allTags = db.getAllNFC();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData();
        }

    }


}
