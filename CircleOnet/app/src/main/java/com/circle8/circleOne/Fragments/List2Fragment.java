package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Adapter.GridViewAdapter;
import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.ImageItem;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentList2Binding;
import com.daimajia.swipe.util.Attributes;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Activity.CardsActivity.Connection_Limit;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class List2Fragment extends Fragment
{
    public static GridViewAdapter gridAdapter;
    ArrayList<byte[]> imgf;
    RelativeLayout lnrSearch;
    View line;
    public static List<NFCModel> allTags;
    public static ArrayList<FriendConnection> allTaggs;
    public static ArrayList<FriendConnection> nfcModel;
    LoginSession session;
    static String UserId = "";
    public static Context mContext;
    public static int pageno = 1;
    View view;
    public static String progressStatus = "FIRST";
    static int numberCount, gridSize;
    public static String count, counts;
    public static FragmentList2Binding fragmentList2Binding;
    public List2Fragment() {
        // Required empty public constructor
    }
    public static List2Fragment newInstance() {
        return new List2Fragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentList2Binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list2, container, false);
        view = fragmentList2Binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext =getActivity();
        pageno = 1;

        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_list2_layout, nfcModel);
        fragmentList2Binding.gridView.setAdapter(gridAdapter);
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);

        if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
            fragmentList2Binding.searchView.setText(SortFragment.Search );
        }
        fragmentList2Binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Utility.freeMemory();

                try
                {
                    if (s.length() <= 0)
                    {
                        pageno = 1;
                        nfcModel.clear();
                        allTaggs.clear();
                        try {
                            gridAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                        callFirst();
                        fragmentList2Binding.tvFriendInfo.setVisibility(View.GONE);
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
        fragmentList2Binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                Utility.freeMemory();
                if (fragmentList2Binding.searchView.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel.clear();
                    allTaggs.clear();
                    try {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    fragmentList2Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList2Binding.searchView.getText().toString().length() > 0)
                {
                    nfcModel.clear();
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

        fragmentList2Binding.searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Utility.freeMemory();

                if (fragmentList2Binding.searchView.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel.clear();
                    allTaggs.clear();
                    try {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    fragmentList2Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList2Binding.searchView.getText().toString().length() > 0)
                {
                    nfcModel.clear();
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
    public void onResume() {
        super.onResume();
        callSubPAge("GridView","Card");
    }

    public static void CustomProgressDialog(final String loading)
    {
        fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.VISIBLE);
        fragmentList2Binding.includeProgress.txtProgressing.setText(loading+"...");
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anticlockwise);
        fragmentList2Binding.includeProgress.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        fragmentList2Binding.includeProgress.imgConnecting2.startAnimation(anim1);
    }

    @Override
    public void onStart() {
        super.onStart();
        callFirst();

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
                        try {
                            if (result != null) {
                                JSONObject jsonObject = new JSONObject(result);
                                count = jsonObject.getString("count");

                                if (pageno == 2) {
                                    counts = jsonObject.getString("count");
                                }
                                if (count.equals("") || count.equals("null")) {
                                    numberCount = 0;
                                } else {
                                    numberCount = Integer.parseInt(count);
                                }

                                JSONArray jsonArray;
                                if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                                    jsonArray = jsonObject.getJSONArray("connect");
                                }else {
                                    jsonArray = jsonObject.getJSONArray("connection");
                                }

                                fragmentList2Binding.rlLoadMore.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    FriendConnection nfcModelTag = new FriendConnection();
                                    nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                                    nfcModelTag.setCompany(object.getString("CompanyName"));
                                    nfcModelTag.setEmail(object.getString("UserName"));
                                    nfcModelTag.setWebsite(object.getString("Website"));
                                    nfcModelTag.setPh_no(object.getString("Phone"));
                                    nfcModelTag.setDesignation(object.getString("Designation"));
                                    nfcModelTag.setCard_front(object.getString("Card_Front"));
                                    nfcModelTag.setCard_back(object.getString("Card_Back"));
                                    nfcModelTag.setUser_image(object.getString("UserPhoto"));
                                    nfcModelTag.setProfile_id(object.getString("ProfileId"));
                                    nfcModelTag.setDateInitiated(object.getString("DateInitiated"));
                                    nfcModelTag.setAddress(object.getString("Address1") + " " + object.getString("Address2")
                                            + " " + object.getString("Address3") + " " + object.getString("Address4"));
//                        Toast.makeText(getActivity(),"Profile_id"+object.getString("ProfileId"),Toast.LENGTH_SHORT).show();
                                    nfcModelTag.setNfc_tag("en000000001");
                                    nfcModelTag.setLatitude(object.getString("Latitude"));
                                    nfcModelTag.setLongitude(object.getString("Longitude"));
                                    allTaggs.add(nfcModelTag);

                                }

                                GetData(mContext);

                                gridSize = allTaggs.size();

                                fragmentList2Binding.gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState)
                                    {
                                        // TODO Auto-generated method stub

                                        progressStatus = "LOAD MORE";

                                        int threshold = 1;
                                        int count = fragmentList2Binding.gridView.getCount();

                                        if (scrollState == SCROLL_STATE_IDLE) {
                                            if (gridSize <= numberCount) {
                                                if (fragmentList2Binding.gridView.getLastVisiblePosition() >= count - threshold) {
                                                    // rlLoadMore.setVisibility(View.VISIBLE);
                                                    // Execute LoadMoreDataTask AsyncTask
                                                    CallApi();
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
                            fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "");
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
            Utility.freeMemory();
            //  dialog.dismiss();
            fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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
                    try {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }

                    if (connect.length() == 0)
                    {
                        fragmentList2Binding.tvFriendInfo.setVisibility(View.VISIBLE);
                        allTaggs.clear();
                        try
                        {
                            gridAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){}
                    }
                    else
                    {
                        fragmentList2Binding.tvFriendInfo.setVisibility(View.GONE);

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
                            connectModel.setEmail(iCon.getString("UserName"));
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
                            connectModel.setAddress(iCon.getString("Address1") + " " + iCon.getString("Address2")
                                    + " " + iCon.getString("Address3") + " " + iCon.getString("Address4"));

                            allTaggs.add(connectModel);

                            gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, allTaggs);
                            fragmentList2Binding.gridView.setAdapter(gridAdapter);
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
        pageno = 1;
        allTaggs.clear();
        nfcModel.clear();
        //CallApi();
        new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
    }

    public static void webCall()
    {
        pageno = 1;
        try
        {
            nfcModel.clear();
            allTaggs.clear();
            gridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        // CallApi();
        new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
    }
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < imgf.size(); i++) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgf.get(i), 0, imgf.get(i).length);

            imageItems.add(new ImageItem(bmp, "Image#" + i));
        }
        return imageItems;
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
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.GONE);

            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    count = jsonObject.getString("count");

                    if (pageno == 2) {
                        counts = jsonObject.getString("count");
                    }
                    if (count.equals("") || count.equals("null")) {
                        numberCount = 0;
                    } else {
                        numberCount = Integer.parseInt(count);
                    }

                    JSONArray jsonArray;
                    if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                        jsonArray = jsonObject.getJSONArray("connect");
                    }else {
                        jsonArray = jsonObject.getJSONArray("connection");
                    }

                    fragmentList2Binding.rlLoadMore.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite(object.getString("Website"));
                        nfcModelTag.setPh_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));
                        nfcModelTag.setDateInitiated(object.getString("DateInitiated"));
                        nfcModelTag.setAddress(object.getString("Address1") + " " + object.getString("Address2")
                                + " " + object.getString("Address3") + " " + object.getString("Address4"));
//                        Toast.makeText(getActivity(),"Profile_id"+object.getString("ProfileId"),Toast.LENGTH_SHORT).show();
                        nfcModelTag.setNfc_tag("en000000001");
                        nfcModelTag.setLatitude(object.getString("Latitude"));
                        nfcModelTag.setLongitude(object.getString("Longitude"));
                        allTaggs.add(nfcModelTag);

                    }

                    GetData(mContext);

                    gridSize = allTaggs.size();

                    fragmentList2Binding.gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            // TODO Auto-generated method stub

                            progressStatus = "LOAD MORE";

                            int threshold = 1;
                            int count = fragmentList2Binding.gridView.getCount();

                            if (scrollState == SCROLL_STATE_IDLE) {
                                if (gridSize <= numberCount) {
                                    if (fragmentList2Binding.gridView.getLastVisiblePosition() >= count - threshold) {
                                        // rlLoadMore.setVisibility(View.VISIBLE);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POSTSearch(String url)
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
            jsonObject.accumulate("FindBy", "NAME");
            jsonObject.accumulate("Search", fragmentList2Binding.searchView.getText().toString());
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

    public static String POST(String url) {
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
    public static void GetData(Context context)
    {
        //newly added
        nfcModel.clear();

        for (FriendConnection reTag : allTaggs)
        {
            FriendConnection nfcModelTag = new FriendConnection();
//            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setPh_no(reTag.getPh_no());
            nfcModelTag.setUser_image(reTag.getUser_image());
            nfcModelTag.setFirstName(reTag.getFirstName());
            nfcModelTag.setLastName(reTag.getLastName());
            nfcModelTag.setUserID(reTag.getUserID());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setCard_back(reTag.getCard_back());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            nfcModelTag.setProfile_id(reTag.getProfile_id());
            nfcModelTag.setDateInitiated(reTag.getDateInitiated());
            nfcModelTag.setLatitude(reTag.getLatitude());
            nfcModelTag.setLongitude(reTag.getLongitude());
            nfcModelTag.setAddress(reTag.getAddress());
            nfcModelTag.setFb_id(reTag.getFb_id());
            nfcModelTag.setLinkedin_id(reTag.getLinkedin_id());
            nfcModelTag.setGoogle_id(reTag.getGoogle_id());
            nfcModelTag.setTwitter_id(reTag.getTwitter_id());
            nfcModel.add(nfcModelTag);
        }

        if (nfcModel.size() == 0) {
            fragmentList2Binding.txtNoCard1.setVisibility(View.VISIBLE);
        } else {
            fragmentList2Binding.txtNoCard1.setVisibility(View.GONE);
        }

        Collections.sort(nfcModel, new Comparator<FriendConnection>() {
            @Override
            public int compare(FriendConnection o1, FriendConnection o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }

            public int compare(NFCModel o1, NFCModel o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });

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
        else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect"))
        {
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
        gridAdapter.setMode(Attributes.Mode.Single);
    }
}
