package com.circle8.circleOne.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.ConnectActivity;
import com.circle8.circleOne.Adapter.ConnectListAdapter;
import com.circle8.circleOne.Adapter.List4Adapter;
import com.circle8.circleOne.Adapter.List5Adapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectList;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectListFragment extends Fragment
{
    private ListView listView;
    private List4Adapter gridAdapter;
    private TextView tvDataInfo ;
    private ConnectListAdapter connectListAdapter ;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;
    public static String find_by = "name" ;

    private List5Adapter list5Adapter ;

    List<NFCModel> allTags ;
    public static List<FriendConnection> allTaggs ;
    //new asign value
    AutoCompleteTextView searchText ;

    ArrayList<NFCModel> nfcModel ;
    public static ArrayList<FriendConnection> nfcModel1 ;

    LoginSession session;
    String UserId = "";

    ArrayList<ConnectList> connectTags = new ArrayList<>();
    ArrayList<ConnectList> connectLists = new ArrayList<>();

    public ConnectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect_list, container, false);
        tvDataInfo = (TextView)view.findViewById(R.id.tvDataInfo);

//        Toast.makeText(getActivity(),"Find by: "+find_by,Toast.LENGTH_SHORT).show();

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
        allTaggs = new ArrayList<>();

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);

        nfcModel = new ArrayList<>();
        nfcModel1 = new ArrayList<>();

//        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
//                    nfcModel1.clear();
//                    connectLists.clear();
                    connectTags.clear();
                    GetData(getContext());
                }
                else if(s.length() == 2)
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());

                    String Findby = "name";
                    String Search = "Circle One" ;
                    String rc_no = "10";
                    String page_no = "1";

                    listView.setVisibility(View.VISIBLE);
//                    connectWithHttpPost(Findby, Search, rc_no, page_no);
                    new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
                }
               /* else
                {
                    listView.setVisibility(View.VISIBLE);
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
//                    new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/SearchConnect");
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    private void connectWithHttpPost(final String FindBy, String Search,
                                     String numofrecords, String pageno )
    {
        // Connect with a server is a time consuming process.
        class HttpGetAsyncTask extends AsyncTask<String, Void, String>
        {
            ProgressDialog dialog;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Searching...");
                //dialog.setTitle("Saving Reminder");
                dialog.show();
                dialog.setCancelable(false);
                //  nfcModel = new ArrayList<>();
                //   allTags = new ArrayList<>();
            }
            @Override
            protected String doInBackground(String... params)
            {
                // As you can see, doInBackground has taken an Array of Strings as the argument
                String findBy = params[0];
                String search = params[1];
                String records = params[2];
                String pages = params[3];

                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);
                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Utility.BASE_URL+"SearchConnect");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("FindBy", findBy));
                nameValuePair.add(new BasicNameValuePair("Search", search));
                nameValuePair.add(new BasicNameValuePair("numofrecords", records));
                nameValuePair.add(new BasicNameValuePair("pageno", pages));

                //Encoding POST data
                try
                {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                }
                catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }
                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL
                try
                {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    System.out.println("httpResponse");
                    // getEntity() ; obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the entity.
                    // Now we need a readable source to read the byte stream that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    // InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
                    //The buffer size is 8K
                    //Therefore we need a mechanism to append the separately coming chunks in to one String element
                    // We have to use a class that can handle modifiable sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    // There may be so many buffered chunks. We have to go through each and every chunk of characters
                    //and assign a each chunk to bufferedStrChunk String variable
                    //and append that value one by one to the stringBuilder
                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }
                    // Now we have the whole response as a String value.
                    //We return that value then the onPostExecute() can handle the content
                    System.out.println("Returning value of doInBackground :" + stringBuilder.toString());
                    // If the Username and Password match, it will return "working" as response
                    // If the Username or Password wrong, it will return "invalid" as response
                    return stringBuilder.toString();
                }
                catch (ClientProtocolException cpe) {
                    System.out.println("Exception generates caz of httpResponse :" + cpe);
                    cpe.printStackTrace();
                }
                catch (IOException ioe) {
                    System.out.println("Second exception generates caz of httpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }
            // Argument comes for this method according to the return type of the doInBackground() and
            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(String result)
            {
                dialog.dismiss();
                super.onPostExecute(result);

                if (result.equals(""))
                {
                    Toast.makeText(getActivity(), "Check For Data Connection..", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"Successfully", Toast.LENGTH_LONG).show();

                    try
                    {
                        JSONObject jsonObject = new JSONObject(result);

//                        JSONObject response = jsonObject.getJSONObject("response");
//                        JSONObject post = response.getJSONObject("Post");

                        JSONArray connect = jsonObject.getJSONArray("connect");

                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (connect.equals(""))
                        {
                            // Toast.makeText(getApplicationContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            for(int i = 0 ; i < connect.length() ; i++)
                            {
                                JSONObject iCon = connect.getJSONObject(i);
                                String firstName = iCon.getString("FirstName");
                                String lastName = iCon.getString("LastName");
                                String userName = iCon.getString("UserName");
                                String userPhoto = iCon.getString("UserPhoto");
                                String card_front = iCon.getString("Card_Front");
                                String card_back = iCon.getString("Card_Back");
                                String profileId = iCon.getString("ProfileId");
                                String phone = iCon.getString("Phone");
                                String companyName = iCon.getString("CompanyName");
                                String designation = iCon.getString("Designation");
                                String facebook = iCon.getString("Facebook");
                                String twitter = iCon.getString("Twitter");
                                String google = iCon.getString("Google");
                                String linkedIn = iCon.getString("LinkedIn");
                                String website = iCon.getString("Website");
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(FindBy, Search, numofrecords, pageno );
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
            jsonObject.accumulate("FindBy", find_by );
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
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String findBy = response.getString("FindBy");
                    String search = response.getString("Search");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

                    if(connect.length() == 0)
                    {
                        tvDataInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvDataInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= connect.length() ; i++ )
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                       /* String firstName = iCon.getString("FirstName");
                        String lastName = iCon.getString("LastName");
                        String userName = iCon.getString("UserName");
                        String userPhoto = iCon.getString("UserPhoto");
                        String card_front = iCon.getString("Card_Front");
                        String card_back = iCon.getString("Card_Back");
                        String profileId = iCon.getString("ProfileId");
                        String phone = iCon.getString("Phone");
                        String companyName = iCon.getString("CompanyName");
                        String designation = iCon.getString("Designation");
                        String facebook = iCon.getString("Facebook");
                        String twitter = iCon.getString("Twitter");
                        String google = iCon.getString("Google");
                        String linkedIn = iCon.getString("LinkedIn");
                        String website = iCon.getString("Website");  */
                            ConnectList connectModel = new ConnectList();
                            connectModel.setFirstname(iCon.getString("FirstName"));
                            connectModel.setLastname(iCon.getString("LastName"));
                            connectModel.setUsername(iCon.getString("UserName"));
                            connectModel.setUserphoto(iCon.getString("UserPhoto"));
                            connectModel.setCard_front(iCon.getString("Card_Front"));
                            connectModel.setCard_back(iCon.getString("Card_Back"));
                            connectModel.setProfile_id(iCon.getString("ProfileId"));
                            connectModel.setPhone(iCon.getString("Phone"));
                            connectModel.setCompanyname(iCon.getString("CompanyName"));
                            connectModel.setDesignation(iCon.getString("Designation"));
                            connectModel.setFacebook(iCon.getString("Facebook"));
                            connectModel.setTwitter(iCon.getString("Twitter"));
                            connectModel.setGoogle(iCon.getString("Google"));
                            connectModel.setLinkedin(iCon.getString("LinkedIn"));
                            connectModel.setWebsite(iCon.getString("Website"));
                            connectTags.add(connectModel);

                            GetData(getContext());
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        nfcModel.clear();
        GetData(getContext());
    }

    private void GetData(Context context)
    {
//        nfcModel.clear();
        nfcModel1.clear();
        connectLists.clear();
        /*for(NFCModel reTag : allTags)
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
        }*/
      /*  for(FriendConnection reTag : allTaggs)
        {
            FriendConnection nfcModelTag = new FriendConnection();
//            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setUser_image(reTag.getUser_image());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            nfcModel1.add(nfcModelTag);
        }*/

       for(ConnectList reTag : connectTags)
       {
           ConnectList connectModelTag = new ConnectList();
           connectModelTag.setFirstname(reTag.getFirstname());
           connectModelTag.setLastname(reTag.getLastname());
           connectModelTag.setCompanyname(reTag.getCompanyname());
           connectModelTag.setUsername(reTag.getUsername());
           connectModelTag.setWebsite(reTag.getWebsite());
           connectModelTag.setPhone(reTag.getPhone());
           connectModelTag.setDesignation(reTag.getDesignation());
           connectModelTag.setCard_front(reTag.getCard_front());
           connectModelTag.setCard_back(reTag.getCard_back());
           connectModelTag.setUserphoto(reTag.getUserphoto());
           connectLists.add(connectModelTag);
       }

     /*   gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, nfcModel1);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();*/

     /*   connectListAdapter = new ConnectListAdapter(getContext(),R.layout.grid_list4_layout, connectLists);
        listView.setAdapter(connectListAdapter);
        connectListAdapter.notifyDataSetChanged();*/

        list5Adapter = new List5Adapter(getContext(), R.layout.grid_list4_layout, connectLists);
        listView.setAdapter(list5Adapter);
        list5Adapter.notifyDataSetChanged();

    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params)
        {
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
            GetData(getContext());
        }

    }


}
