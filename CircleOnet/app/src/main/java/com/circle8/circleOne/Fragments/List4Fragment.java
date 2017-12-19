package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.SortAndFilterOption;
import com.circle8.circleOne.Adapter.List4Adapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

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
import java.util.List;

public class List4Fragment extends Fragment
{
    public static ListView listView;
    public static List4Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db;
    RelativeLayout lnrSearch;
    View line;
    public static String parent = "";

    private GestureDetector gestureDetector1;

    public static List<NFCModel> allTags;
    public static ArrayList<FriendConnection> allTaggs;

    //new asign value
    AutoCompleteTextView searchText ;
    ImageView imgSearch ;
    public static TextView tvFriendInfo, txtNoCard1 ;
    public static ArrayList<NFCModel> nfcModel ;
    public static ArrayList<FriendConnection> nfcModel1 ;
    LoginSession session ;
    static String UserId = "" ;

    public static Context mContext ;
    public static int pageno = 1 ;

    static RelativeLayout rlLoadMore ;

    public static String progressStatus = "FIRST";

    static int numberCount, listSize;

    public static RelativeLayout rlProgressDialog ;
    public static TextView tvProgressing ;
    public static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    static String count;

    public List4Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.freeMemory();
        Utility.deleteCache(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list4, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Utility.freeMemory();
        Utility.deleteCache(getContext());

        mContext = List4Fragment.this.getContext();
        pageno = 1;
        db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();

        /*GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();
        this.gestureDetector1= new GestureDetector(getContext(), gestureListener);
        this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);*/

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        imgSearch = (ImageView)view.findViewById(R.id.imgSearch);
        tvFriendInfo = (TextView)view.findViewById(R.id.tvFriendInfo);
        txtNoCard1 = (TextView) view.findViewById(R.id.txtNoCard1);

        line = view.findViewById(R.id.view);
        /*lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE); */

        listView = (ListView) view.findViewById(R.id.listViewType4);
        rlLoadMore = (RelativeLayout) view.findViewById(R.id.rlLoadMore);
        rlProgressDialog = (RelativeLayout)view.findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)view.findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)view.findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)view.findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)view.findViewById(R.id.imgConnecting3) ;

        //considering from Database
//        allTags = db.getActiveNFC();
        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);

        searchText = (AutoCompleteTextView) view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();
        nfcModel1 = new ArrayList<>();

        callFirst();

        /*List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getUser_image());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }


        listView = (ListView) view.findViewById(R.id.listViewType4);
        gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, imgf, desc, name, designation);
        listView.setAdapter(gridAdapter);*/
        /*listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

      /*  listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                return gestureDetector1.onTouchEvent(me);
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), CardDetail.class);
                intent.putExtra("tag_id", nfcModel1.get(position).getNfc_tag());
                intent.putExtra("profile_id", nfcModel1.get(position).getProfile_id());
                intent.putExtra("DateInitiated",nfcModel1.get(position).getDateInitiated());
                intent.putExtra("lat", nfcModel1.get(position).getLatitude());
                intent.putExtra("long", nfcModel1.get(position).getLongitude());
                getContext().startActivity(intent);
                Utility.freeMemory();
                Utility.deleteCache(getContext());

            }
        });

//        GetData(getContext());


       /* listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

       /* listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

       /* listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });*/

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if(s.length() <= 0)
                {
                    nfcModel1.clear();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                }*/
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                try
               {
                   if (s.length() <= 0)
                   {
                       pageno = 1;
                       nfcModel1.clear();
                       allTaggs.clear();
                       try
                       {
                           gridAdapter.notifyDataSetChanged();
                       } catch (Exception e) {
                       }
                       callFirst();
                       tvFriendInfo.setVisibility(View.GONE);
//                    GetData(getContext());
                   }
                  /* else if (s.length() > 0)
                   {
                       String text = searchText.getText().toString().toLowerCase(Locale.getDefault());

                       nfcModel1.clear();
                       allTaggs.clear();
                       try
                       {
                           gridAdapter.notifyDataSetChanged();
                       }
                       catch (Exception e)
                       {
                            e.printStackTrace();
                       }
                       new HttpAsyncTaskSearch().execute("http://circle8.asia:8999/Onet.svc/SearchConnect");
                   }*/
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel1.clear();
                    allTaggs.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0 )
                {
                    nfcModel1.clear();
                    allTaggs.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel1.clear();
                    allTaggs.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0 )
                {
                    nfcModel1.clear();
                    allTaggs.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }

                return true;
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        Utility.deleteCache(getContext());

        super.onPause();
    }

    public String POSTSearch(String url) {
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
            jsonObject.accumulate("FindBy", "NAME");
            jsonObject.accumulate("Search", searchText.getText().toString());
            jsonObject.accumulate("SearchType", "Local" );
            jsonObject.accumulate("UserID", UserId);
            jsonObject.accumulate("numofrecords", "30");
            jsonObject.accumulate("pageno", "1");

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

    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Searching Records...");
            //dialog.setTitle("Saving Reminder");
            //  dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Searching" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTSearch(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            //   dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            Utility.freeMemory();
            try
            {
                if (result == "")
                {
                    Toast.makeText(getContext(), "Slow Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String findBy = response.getString("FindBy");
                    String search = response.getString("Search");
                    count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

                    allTaggs.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) { }

                    if (connect.length() == 0)
                    {
                        tvFriendInfo.setVisibility(View.VISIBLE);
                        allTaggs.clear();
                        try
                        {
                            gridAdapter.notifyDataSetChanged();
                        }catch (Exception e){}
                    }
                    else
                    {
                          tvFriendInfo.setVisibility(View.GONE);

                        for (int i = 0; i <= connect.length(); i++)
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            FriendConnection connectModel = new FriendConnection();
                            connectModel.setUserID(iCon.getString("UserID"));
                            connectModel.setFirstName(iCon.getString("FirstName"));
                            connectModel.setLastName(iCon.getString("LastName"));
                            connectModel.setName(iCon.getString("FirstName") + " " + iCon.getString("LastName"));
                            connectModel.setUser_image(iCon.getString("UserPhoto"));
                            connectModel.setCard_front(iCon.getString("Card_Front"));
                            connectModel.setCard_back(iCon.getString("Card_Back"));
                            connectModel.setProfile_id(iCon.getString("ProfileId"));
                            connectModel.setPh_no(iCon.getString("Phone"));
                            connectModel.setCompany(iCon.getString("CompanyName"));
                            connectModel.setDesignation(iCon.getString("Designation"));
                            connectModel.setFb_id(iCon.getString("Facebook"));
                            connectModel.setTwitter_id(iCon.getString("Twitter"));
                            connectModel.setGoogle_id(iCon.getString("Google"));
                            connectModel.setLinkedin_id(iCon.getString("LinkedIn"));
                            connectModel.setWebsite(iCon.getString("Website"));
                            connectModel.setLatitude(iCon.getString("Latitude"));
                            connectModel.setLongitude(iCon.getString("Longitude"));
                            allTaggs.add(connectModel);

                            gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, allTaggs);
                            listView.setAdapter(gridAdapter);
                            gridAdapter.notifyDataSetChanged();
//                            GetData(getContext());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void callFirst()
    {
        Utility.freeMemory();
        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
    }

    public static void webCall()
    {
        /*try
        {
            nfcModel1.clear();
            allTaggs.clear();
            gridAdapter.notifyDataSetChanged();
            new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        pageno = 1;
        Utility.freeMemory();
        try
        {
            nfcModel1.clear();
            allTaggs.clear();
            gridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
    }


    private static class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*dialog = new ProgressDialog(mContext);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.setCancelable(false);*/
           /* if (comeAtTime.equalsIgnoreCase("FIRST")) {
                dialog.show();
                comeAtTime = "SECOND";
            } else {
                dialog.dismiss();
            }*/

            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            if (progressStatus.equalsIgnoreCase("FIRST"))
            {
                String loading = "Fetching cards" ;
                CustomProgressDialog(loading);

                progressStatus = "SECOND";
            }
            else if (progressStatus.equalsIgnoreCase("SECOND"))
            {

            }
            else if (progressStatus.equalsIgnoreCase("LOAD MORE"))
            {

            }
            else if (progressStatus.equalsIgnoreCase("DELETE"))
            {
                String loading = "Refreshing cards" ;
                CustomProgressDialog(loading);
            }
            else
            {

            }

          /*  String loading = "Fetching Cards" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
            Utility.freeMemory();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
//                    numberCount = Integer.parseInt(jsonObject.getString("count")) ;

                    count = jsonObject.getString("count");
                    if (count.equals("") || count.equals("null")) {
                        numberCount = 0;
                    } else {
                        numberCount = Integer.parseInt(count);
                    }

                    JSONArray jsonArray;
                    if (SortAndFilterOption.CardListApi.equalsIgnoreCase("SearchConnect")){
                        jsonArray = jsonObject.getJSONArray("connect");
                    }else {
                        jsonArray = jsonObject.getJSONArray("connection");
                    }
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    numberCount = jsonArray.length();
                    rlLoadMore.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));
                        nfcModelTag.setNfc_tag("en000000001");
                        nfcModelTag.setAddress(object.getString("Address1") + " " + object.getString("Address2") + " "
                                + object.getString("Address3") + object.getString("Address4"));
                        nfcModelTag.setDateInitiated(object.getString("DateInitiated"));
                        nfcModelTag.setLatitude(object.getString("Latitude"));
                        nfcModelTag.setLongitude(object.getString("Longitude"));
                        allTaggs.add(nfcModelTag);
                        GetData(mContext);
                    }

                    listSize = allTaggs.size();

                    listView.setOnScrollListener(new AbsListView.OnScrollListener()
                    {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            // TODO Auto-generated method stub

                            progressStatus = "LOAD MORE";

                            int threshold = 1;
                            int count = listView.getCount();

                            if (scrollState == SCROLL_STATE_IDLE)
                            {
                                if (listSize <= numberCount)
                                {
                                    if (listView.getLastVisiblePosition() >= count - threshold)
                                    {
                                        rlLoadMore.setVisibility(View.VISIBLE);
                                        // Execute LoadMoreDataTask AsyncTask
                                        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
                                    }
                                } else {

                                }
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem,
                                             int visibleItemCount, int totalItemCount) {
                            // TODO Auto-generated method stub

                        }
                    });

                } else {
                    Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url) {
        Utility.freeMemory();
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
            if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
                jsonObject.accumulate("Type", SortAndFilterOption.SortType);
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("userid", UserId);
            }
            else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                jsonObject.accumulate("ProfileID", SortAndFilterOption.ProfileArrayId);
                jsonObject.accumulate("Type", SortAndFilterOption.SortType);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                jsonObject.accumulate("group_ID", SortAndFilterOption.groupId);
                jsonObject.accumulate("profileId", SortAndFilterOption.ProfileArrayId);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("SearchConnect")) {
                jsonObject.accumulate("FindBy", SortAndFilterOption.FindBY );
                jsonObject.accumulate("Search", SortAndFilterOption.Search );
                jsonObject.accumulate("SearchType", "Local" );
                jsonObject.accumulate("UserID", UserId );
                jsonObject.accumulate("numofrecords", "100" );
                jsonObject.accumulate("pageno", "1" );

            }



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
        pageno++;
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


  /*  @Override
    public void onResume()
    {
        super.onResume();
        callFirst();
//        nfcModel.clear();
//        GetData(getContext());
    }*/

   /* class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            //  Toast.makeText(getContext(), "onDown", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
           // Toast.makeText(getContext(), "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //   Log.e(TAG, "onSingleTapUp");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            //Log.e(TAG, "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
           // Toast.makeText(getContext(), "onLongPress", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Toast.makeText(getContext(), "onFling", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e1.getX() + ":" + e1.getY() + "  " + e2.getX() + ":" + e2.getY());
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // onSwipeRight();
                        } else {
                            // onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //Toast.makeText(getContext(), "Down", Toast.LENGTH_LONG).show();
                            lnrSearch.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        } else {
                            //  Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
                            lnrSearch.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }


    }*/

   /* class MyOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //  Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
           // Toast.makeText(getContext(), "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
          //  Toast.makeText(getContext(), "onDoubleTapEvent", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX() + ":" + e.getY());
            //  Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }*/


   /* GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            try {
                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    swipe += "Swipe Right\n";
                } else {
                    swipe += "\n";
                }

                if ((e1.getY() - e2.getY()) > sensitvity) {
                    swipe += "Swipe Up\n";
                    *//*lnrSearch.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);*//*
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    *//*lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);*//*
                } else {
                    swipe += "\n";
                }

                //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

                return super.onFling(e1, e2, velocityX, velocityY);
            }
            catch (Exception e ){
                return true;
            }
        }
    };*/

//    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

    public static void GetData(Context context)
    {
        nfcModel1.clear();
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

        for (FriendConnection reTag : allTaggs)
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
            nfcModelTag.setProfile_id(reTag.getProfile_id());
            nfcModelTag.setAddress(reTag.getAddress());
            nfcModelTag.setDateInitiated(reTag.getDateInitiated());
            nfcModelTag.setLatitude(reTag.getLatitude());
            nfcModelTag.setLongitude(reTag.getLongitude());
            nfcModel1.add(nfcModelTag);
        }

        if (nfcModel1.size() == 0) {
            txtNoCard1.setVisibility(View.VISIBLE);
        } else {
            txtNoCard1.setVisibility(View.GONE);
        }

//        rlLoadMore.setVisibility(View.GONE);

        gridAdapter = new List4Adapter(context, R.layout.grid_list4_layout, nfcModel1);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + count + "/" + CardsActivity.Connection_Limit);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + count);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + count);
            }
        }
        else  {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + count);
            }
        }

       // CardsActivity.setActionBarTitle("Cards - " + nfcModel1.size() + "/"+ CardsActivity.Connection_Limit);
    }

    public static void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
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
