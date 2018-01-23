package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Adapter.List4Adapter;
import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentList4Binding;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Activity.CardsActivity.Connection_Limit;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;


public class List4Fragment extends Fragment
{
    public static List4Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    View line;
    public static String parent = "";
    public static List<NFCModel> allTags;
    public static ArrayList<FriendConnection> allTaggs;
    public static ArrayList<NFCModel> nfcModel ;
    public static ArrayList<FriendConnection> nfcModel1 ;
    LoginSession session ;
    static String UserId = "" ;
    public static Context mContext ;
    public static int pageno = 1 ;
    static RelativeLayout rlLoadMore ;
    public static String progressStatus = "FIRST";
    static int numberCount, listSize;
    public static String count, counts;
    private Fragment fragment;
    View view;
    static FragmentList4Binding fragmentList4Binding;
    public List4Fragment() {
        // Required empty public constructor
    }

    public static List4Fragment newInstance() {
        return new List4Fragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentList4Binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list4, container, false);
        view = fragmentList4Binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext = getActivity();
        pageno = 1;
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();
        line = view.findViewById(R.id.view);
        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();
        session = new LoginSession(mContext);
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        nfcModel = new ArrayList<>();
        nfcModel1 = new ArrayList<>();
        gridAdapter = new List4Adapter(getActivity(), R.layout.grid_list4_layout, nfcModel1);
        fragmentList4Binding.listViewType4.setAdapter(gridAdapter);

        if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
            fragmentList4Binding.searchView.setText(SortFragment.Search );
        }
        fragmentList4Binding.listViewType4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Utility.freeMemory();
                Utility.deleteCache(getContext());
                CardDetail.profile_id = nfcModel1.get(position).getProfile_id();
                CardDetail.DateInitiated = nfcModel1.get(position).getDateInitiated();
                CardDetail.lat = nfcModel1.get(position).getLatitude();
                CardDetail.lon = nfcModel1.get(position).getLongitude();
                fragment = new CardDetail();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        fragmentList4Binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                        fragmentList4Binding.tvFriendInfo.setVisibility(View.GONE);
//                    GetData(getContext());
                    }
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

        fragmentList4Binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                if (fragmentList4Binding.searchView.getText().toString().length() == 0)
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
                    fragmentList4Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList4Binding.searchView.getText().toString().length() > 0 )
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

        fragmentList4Binding.searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Utility.freeMemory();
                Utility.deleteCache(getContext());
                if (fragmentList4Binding.searchView.getText().toString().length() == 0)
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
                    fragmentList4Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList4Binding.searchView.getText().toString().length() > 0 )
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
    }

    @Override
    public void onStart() {
        super.onStart();
        callFirst();
    }
    public static void CustomProgressDialog(final String loading)
    {
        fragmentList4Binding.rlProgressDialog.setVisibility(View.VISIBLE);
        fragmentList4Binding.txtProgressing.setText(loading+"...");

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anticlockwise);
        fragmentList4Binding.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        fragmentList4Binding.imgConnecting2.startAnimation(anim1);

    }

    public String POSTSearch(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("FindBy", "NAME");
            jsonObject.accumulate("Search", fragmentList4Binding.searchView.getText().toString());
            jsonObject.accumulate("SearchType", "Local" );
            jsonObject.accumulate("UserID", UserId);
            jsonObject.accumulate("numofrecords", "30");
            jsonObject.accumulate("pageno", "1");
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
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
            fragmentList4Binding.rlProgressDialog.setVisibility(View.GONE);
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
                    counts = response.getString("count");
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
                        fragmentList4Binding.tvFriendInfo.setVisibility(View.VISIBLE);
                        allTaggs.clear();
                        try
                        {
                            gridAdapter.notifyDataSetChanged();
                        }catch (Exception e){}
                    }
                    else
                    {
                        fragmentList4Binding.tvFriendInfo.setVisibility(View.GONE);

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

                            GetData(getContext());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void CallApi() {
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
        JSONObject jsonObject = new JSONObject();
        try {
            if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("userid", UserId);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                jsonObject.accumulate("ProfileID", SortFragment.ProfileArrayId);
                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                jsonObject.accumulate("group_ID", SortFragment.groupId);
                jsonObject.accumulate("profileId", SortFragment.ProfileArrayId);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
                jsonObject.accumulate("FindBy", SortFragment.FindBY );
                jsonObject.accumulate("Search", SortFragment.Search );
                jsonObject.accumulate("SearchType", "Local" );
                jsonObject.accumulate("UserID", UserId );
                jsonObject.accumulate("numofrecords", "100" );
                jsonObject.accumulate("pageno", "1" );

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Utility.BASE_URL+SortFragment.CardListApi, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("11", response.toString());
                        String result = response.toString();
                        try
                        {
                            if (result != null) {
                                JSONObject jsonObject = new JSONObject(result);
//                    numberCount = Integer.parseInt(jsonObject.getString("count")) ;
                                count = jsonObject.getString("count");

                                if (pageno == 2) {
                                    allTaggs.clear();
                                    counts = jsonObject.getString("count");
                                }
                                if (count.equals("") || count.equals("null")) {
                                    numberCount = 0;
                                } else {
                                    numberCount = Integer.parseInt(count);
                                }

                                JSONArray jsonArray;
                                if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                                    allTaggs.clear();
                                    nfcModel1.clear();
                                    jsonArray = jsonObject.getJSONArray("connect");
                                }else {
                                    jsonArray = jsonObject.getJSONArray("connection");
                                }
                                //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
//                    numberCount = jsonArray.length();
                                fragmentList4Binding.rlLoadMore.setVisibility(View.GONE);

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
                                    nfcModel1.add(nfcModelTag);
                                }
                                Log.e("allTaggs",""+allTaggs.size());

                                // GetData(mContext);

                                listSize = allTaggs.size();

                                fragmentList4Binding.listViewType4.setOnScrollListener(new AbsListView.OnScrollListener()
                                {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState)
                                    {
                                        // TODO Auto-generated method stub

                                        progressStatus = "LOAD MORE";

                                        int threshold = 1;
                                        int count = fragmentList4Binding.listViewType4.getCount();

                                        if (scrollState == SCROLL_STATE_IDLE)
                                        {
                                            if (listSize <= numberCount)
                                            {
                                                if (fragmentList4Binding.listViewType4.getLastVisiblePosition() >= count - threshold)
                                                {
                                                    //  rlLoadMore.setVisibility(View.VISIBLE);
                                                    // Execute LoadMoreDataTask AsyncTask
                                                   // CallApi();
                                                     new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
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
                            fragmentList4Binding.rlProgressDialog.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentList4Binding.rlProgressDialog.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "");
    }


    public static void callFirst()
    {
        pageno = 1;
        allTaggs.clear();
        Utility.freeMemory();
        //CallApi();
           new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
    }

    public static void webCall()
    {

        pageno = 1;
        Utility.freeMemory();
        try
        {
            nfcModel1.clear();
            nfcModel.clear();
            allTaggs.clear();
            gridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        //CallApi();
         new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
    }


    private static class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


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

  String loading = "Fetching Cards" ;
            CustomProgressDialog(loading);

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
            fragmentList4Binding.rlProgressDialog.setVisibility(View.GONE);
            Utility.freeMemory();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
//                    numberCount = Integer.parseInt(jsonObject.getString("count")) ;
                    count = jsonObject.getString("count");

                    if (pageno == 2) {
                        allTaggs.clear();
                        counts = jsonObject.getString("count");
                    }
                    if (count.equals("") || count.equals("null")) {
                        numberCount = 0;
                    } else {
                        numberCount = Integer.parseInt(count);
                    }

                    JSONArray jsonArray;
                    if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                        allTaggs.clear();
                        nfcModel1.clear();
                        jsonArray = jsonObject.getJSONArray("connect");
                    }else {
                        jsonArray = jsonObject.getJSONArray("connection");
                    }
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
//                    numberCount = jsonArray.length();
                    fragmentList4Binding.rlLoadMore.setVisibility(View.GONE);

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

                    }
                    Log.e("allTaggs",""+allTaggs.size());

                    GetData(mContext);

                    listSize = allTaggs.size();

                    fragmentList4Binding.listViewType4.setOnScrollListener(new AbsListView.OnScrollListener()
                    {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            // TODO Auto-generated method stub

                            progressStatus = "LOAD MORE";

                            int threshold = 1;
                            int count = fragmentList4Binding.listViewType4.getCount();

                            if (scrollState == SCROLL_STATE_IDLE)
                            {
                                if (listSize <= numberCount)
                                {
                                    if (fragmentList4Binding.listViewType4.getLastVisiblePosition() >= count - threshold)
                                    {
                                        //  rlLoadMore.setVisibility(View.VISIBLE);
                                        // Execute LoadMoreDataTask AsyncTask
                                        //CallApi();
                                           new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
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
            if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("userid", UserId);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                jsonObject.accumulate("ProfileID", SortFragment.ProfileArrayId);
                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                jsonObject.accumulate("group_ID", SortFragment.groupId);
                jsonObject.accumulate("profileId", SortFragment.ProfileArrayId);
                jsonObject.accumulate("numofrecords", "10");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", pageno);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
                jsonObject.accumulate("FindBy", SortFragment.FindBY );
                jsonObject.accumulate("Search", SortFragment.Search );
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



    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel.clear();
        nfcModel1.clear();
        allTaggs.clear();

        //  callFirst();
//        nfcModel.clear();
//        GetData(getContext());
    }


    public static void GetData(Context context)
    {
        nfcModel1.clear();

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
            fragmentList4Binding.txtNoCard1.setVisibility(View.VISIBLE);
        } else {
            fragmentList4Binding.txtNoCard1.setVisibility(View.GONE);
        }

//        rlLoadMore.setVisibility(View.GONE);

        gridAdapter.notifyDataSetChanged();

        if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {


            try {
                if (Connection_Limit.equalsIgnoreCase("100000")) {
                    if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {
                        DashboardActivity.setActionBarTitle("Cards - " + counts + "/", true);
                    }
                }
                else {
                    if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {
                        DashboardActivity.setActionBarTitle("Cards - " + counts + "/" + CardsActivity.Connection_Limit, false);
                    }

                }
            }catch (Exception e){

            }
        }
        else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
            if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {
                DashboardActivity.setActionBarTitle("Cards - " + counts, false);
            }

        }
        else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
            if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {
                DashboardActivity.setActionBarTitle("Cards - " + counts, false);
            }
        }
        else  if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
            if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {
                //DashboardActivity.setActionBarTitle("Cards - " + counts, false);
                if (Connection_Limit.equalsIgnoreCase("100000")) {
                    DashboardActivity.setActionBarTitle("Cards - " + counts + "/", true);
                }
                else {
                    DashboardActivity.setActionBarTitle("Cards - " + counts + "/" + CardsActivity.Connection_Limit, false);
                }
            }
        }
    }
}
