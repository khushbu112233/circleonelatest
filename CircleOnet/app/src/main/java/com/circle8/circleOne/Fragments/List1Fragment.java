package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Adapter.GalleryAdapter;
import com.circle8.circleOne.Adapter.GalleryAdapter1;
import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentList1Binding;

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
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class List1Fragment extends Fragment
{
    public static RelativeLayout lnrSearch;
    public static View line;
    public static List<FriendConnection> allTags;
    public static ArrayList<FriendConnection> nfcModel;
    public static int pageno = 1;
    View view;
    public static ArrayList<Integer> images;
    public static ArrayList<Integer> images1;
    public static GalleryAdapter mAdapter;
    public static GalleryAdapter1 mAdapter1;
    public static CarouselLayoutManager manager1, manager2;
    private int draggingView = -1;
    LoginSession session;
    static String UserId = "";
    public static Context mContext;
    public static String progressStatus = "FIRST";
    static int number_cards = 0;
    static int numberCount, recycleSize;
    public static String count = "";
    static FragmentList1Binding fragmentList1Binding;
    public static String SearchFromDashboard = "";
    public List1Fragment() {
        // Required empty public constructor
    }
    public static List1Fragment newInstance() {
        return new List1Fragment();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        fragmentList1Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list1, container, false);
        view = fragmentList1Binding.getRoot();

        ViewTreeObserver vto = fragmentList1Binding.lnrList.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    fragmentList1Binding.lnrList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    fragmentList1Binding.lnrList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = fragmentList1Binding.lnrList.getMeasuredWidth();
                int height = fragmentList1Binding.lnrList.getMeasuredHeight();

                View view_instance = (View) view.findViewById(R.id.list_horizontal1);
                ViewGroup.LayoutParams params = view_instance.getLayoutParams();
                params.height = (height / (29 / 10)) - 10;
                view_instance.setLayoutParams(params);

                View view_instance1 = (View) view.findViewById(R.id.list_horizontal2);
                ViewGroup.LayoutParams params1 = view_instance1.getLayoutParams();
                params1.height = (height / (29 / 10)) - 10;
                view_instance1.setLayoutParams(params1);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        mContext = getContext();
        pageno = 1;
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        line = view.findViewById(R.id.view);

        initRecyclerView1(fragmentList1Binding.includeCarousel1.listHorizontal1, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter);
        initRecyclerView2(fragmentList1Binding.includeCarousel2.listHorizontal2, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1);

        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(fragmentList1Binding.searchView.getWindowToken(), 0);

        line.setVisibility(View.VISIBLE);
        CardsFragment.fragmentCardsBinding.tabs1.setVisibility(View.VISIBLE);

        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();
        allTags.clear();
        nfcModel.clear();
        fragmentList1Binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                try
                {
                    if (s.length() == 0)
                    {
                        // allTags = db.getActiveNFC();
//                    GetData(getContext());
                        nfcModel.clear();
                        pageno = 1;
                        allTags.clear();
                        try {
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }

                        try {
                            mAdapter1.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                        callFirst();
                        fragmentList1Binding.tvFriendInfo.setVisibility(View.GONE);
                    }
                    else {

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                {
                    pageno = 1;
                    allTags.clear();
                    try {
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    try {
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                }
            }
        });

        fragmentList1Binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                if (fragmentList1Binding.searchView.getText().toString().length() == 0)
                {
                    nfcModel.clear();
                    pageno = 1;
                    allTags.clear();
                    try {
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }

                    try {
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    fragmentList1Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList1Binding.searchView.getText().toString().length() > 0)
                {
                    nfcModel.clear();
                    allTags.clear();
                    try
                    {
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    try {
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }
            }
        });

        fragmentList1Binding.searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                if (fragmentList1Binding.searchView.getText().toString().length() == 0)
                {
                    nfcModel.clear();
                    pageno = 1;
                    allTags.clear();
                    try {
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }

                    try {
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    fragmentList1Binding.tvFriendInfo.setVisibility(View.GONE);
                }

                if (fragmentList1Binding.searchView.getText().toString().length() > 0)
                {
                    nfcModel.clear();
                    allTags.clear();
                    try
                    {
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    try {
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }

                return true;
            }
        });

        final RecyclerView.OnScrollListener[] scrollListeners = new RecyclerView.OnScrollListener[2];
        scrollListeners[0] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                fragmentList1Binding.includeCarousel2.listHorizontal2.removeOnScrollListener(scrollListeners[1]);
                fragmentList1Binding.includeCarousel2.listHorizontal2.scrollBy(dx, dy);
                fragmentList1Binding.includeCarousel2.listHorizontal2.addOnScrollListener(scrollListeners[1]);
            }
        };
        scrollListeners[1] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                fragmentList1Binding.includeCarousel1.listHorizontal1.removeOnScrollListener(scrollListeners[0]);
                fragmentList1Binding.includeCarousel1.listHorizontal1.scrollBy(dx, dy);
                fragmentList1Binding.includeCarousel1.listHorizontal1.addOnScrollListener(scrollListeners[0]);
            }
        };
        fragmentList1Binding.includeCarousel1.listHorizontal1.addOnScrollListener(scrollListeners[0]);
        fragmentList1Binding.includeCarousel2.listHorizontal2.addOnScrollListener(scrollListeners[1]);

    }

    public static void callFirst()
    {

        nfcModel.clear();
        pageno = 1;
        new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
        // CallApi();
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
            String loading = "Fetching cards" ;
            CustomProgressDialog(loading);
        }
        else if (progressStatus.equalsIgnoreCase("DELETE"))
        {
            String loading = "Refreshing cards" ;
            CustomProgressDialog(loading);
        }
        else if (progressStatus.equalsIgnoreCase("FILTER"))
        {
            String loading = "Fetching cards" ;
            CustomProgressDialog(loading);
        }
        else
        {

        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {

                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", "1");
                jsonObject.accumulate("userid", UserId);
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                jsonObject.accumulate("ProfileID", SortFragment.ProfileArrayId);
                jsonObject.accumulate("Type", SortFragment.SortType);
                jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", "1");
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                jsonObject.accumulate("group_ID", SortFragment.groupId);
                jsonObject.accumulate("profileId", SortFragment.ProfileArrayId);
                jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                jsonObject.accumulate("pageno", "1");
            }
            else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
                jsonObject.accumulate("FindBy", SortFragment.FindBY );
                jsonObject.accumulate("Search", SortFragment.Search );
                jsonObject.accumulate("SearchType", "Local" );
                jsonObject.accumulate("UserID", UserId );
                jsonObject.accumulate("numofrecords", "1000" );
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
                            if (result != null)
                            {
                                JSONObject jsonObject = new JSONObject(result);
                                //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                                count = jsonObject.getString("count");

                                if (count.equals("") || count.equals("null"))
                                {
                                    numberCount = 0;
                                }
                                else
                                {
                                    numberCount = Integer.parseInt(count);
                                }

//                    nfcModel.clear();
                                allTags.clear();
                                try
                                {
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter1.notifyDataSetChanged();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JSONArray jsonArray;
                                if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                                    jsonArray = jsonObject.getJSONArray("connect");
                                }else {
                                    jsonArray = jsonObject.getJSONArray("connection");
                                }
                                number_cards = jsonArray.length();
                                fragmentList1Binding.includeCarousel1.rlLoadMore1.setVisibility(View.GONE);
                                fragmentList1Binding.includeCarousel2.rlLoadMore2.setVisibility(View.GONE);

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
                                    allTags.add(nfcModelTag);

                                }
                                GetData(mContext);
                                if (allTags.size() == 0)
                                {
                                    if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection"))
                                    {
                                        fragmentList1Binding.txtNoCard1.setText("No members have been added to the circle");
                                        fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        fragmentList1Binding.txtNoCard1.setText(mContext.getResources().getString(R.string.no_friend_connection));
                                        fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    fragmentList1Binding.txtNoCard1.setVisibility(View.GONE);
                                }

                                recycleSize = allTags.size();

                            }
                            else
                            {
                                Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                            }
                            fragmentList1Binding.rlProgressDialog.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentList1Binding.rlProgressDialog.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!SearchFromDashboard.equals("")) {
            new HttpAsyncTaskSearch().execute(Utility.BASE_URL + "SearchConnect");
        } else {
            callFirst();
        }
    }

    public static void webCall()
    {
        pageno = 1;
        try
        {
            nfcModel.clear();
            allTags.clear();
            mAdapter.notifyDataSetChanged();
            mAdapter1.notifyDataSetChanged();
        }  catch (Exception e) {
        }
        callFirst();
    }

    public static class HttpAsyncTask extends AsyncTask<String, Void, String>
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
                String loading = "Fetching cards" ;
                CustomProgressDialog(loading);
            }
            else if (progressStatus.equalsIgnoreCase("DELETE"))
            {
                String loading = "Refreshing cards" ;
                CustomProgressDialog(loading);
            }
            else if (progressStatus.equalsIgnoreCase("FILTER"))
            {
                String loading = "Fetching cards" ;
                CustomProgressDialog(loading);
            }

        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {

                    jsonObject.accumulate("Type", SortFragment.SortType);

                    jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                    jsonObject.accumulate("pageno", "1");
                    jsonObject.accumulate("userid", UserId);
                }
                else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                    jsonObject.accumulate("ProfileID", SortFragment.ProfileArrayId);
                    jsonObject.accumulate("Type", SortFragment.SortType);
                    jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                    jsonObject.accumulate("pageno", "1");
                }
                else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                    jsonObject.accumulate("group_ID", SortFragment.groupId);
                    jsonObject.accumulate("profileId", SortFragment.ProfileArrayId);
                    jsonObject.accumulate("numofrecords", "1000");
//            jsonObject.accumulate("pageno", pageno);
                    jsonObject.accumulate("pageno", "1");
                }
                else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
                    jsonObject.accumulate("FindBy", SortFragment.FindBY );
                    jsonObject.accumulate("Search", SortFragment.Search );
                    jsonObject.accumulate("SearchType", "Local" );
                    jsonObject.accumulate("UserID", UserId );
                    jsonObject.accumulate("numofrecords", "1000" );
                    jsonObject.accumulate("pageno", "1" );

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            customProgressBarStatus = "stop";
          /*  String status = "false" ;
            String loading = "Fetching Cards..." ;
            CustomProgressBar(loading, status);*/
//            dialog.dismiss();

            fragmentList1Binding.rlProgressDialog.setVisibility(View.GONE);
            Utility.freeMemory();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    count = jsonObject.getString("count");

                    if (count.equals("") || count.equals("null"))
                    {
                        numberCount = 0;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(count);
                    }

//                    nfcModel.clear();
                    allTags.clear();
                    try
                    {
                        mAdapter.notifyDataSetChanged();
                        mAdapter1.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArray;
                    if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")){
                        jsonArray = jsonObject.getJSONArray("connect");
                    }else {
                        jsonArray = jsonObject.getJSONArray("connection");
                    }
                    number_cards = jsonArray.length();
                    fragmentList1Binding.includeCarousel1.rlLoadMore1.setVisibility(View.GONE);
                    fragmentList1Binding.includeCarousel2.rlLoadMore2.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

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
                        allTags.add(nfcModelTag);

                    }
                    GetData(mContext);
                    if (allTags.size() == 0)
                    {
                        if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection"))
                        {
                            fragmentList1Binding.txtNoCard1.setText("No members have been added to the circle");
                            fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            fragmentList1Binding.txtNoCard1.setText(mContext.getResources().getString(R.string.no_friend_connection));
                            fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        fragmentList1Binding.txtNoCard1.setVisibility(View.GONE);
                    }

                    recycleSize = allTags.size();


                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static void initRecyclerView1(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter mAdapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager1 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    public static void initRecyclerView2(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter1 mAdapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager2 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());

    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener()
    {
        public int y = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {
            super.onScrollStateChanged(recyclerView, newState);
            if (fragmentList1Binding.includeCarousel1.listHorizontal1 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING)
            {
                draggingView = 1;
                fragmentList1Binding.includeCarousel2.listHorizontal2.stopScroll();
                // GalleryAdapter.position = Integer.parseInt(GalleryAdapter.imageView.getTag().toString());
            }
            else if (fragmentList1Binding.includeCarousel2.listHorizontal2 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING)
            {
                draggingView = 2;
                fragmentList1Binding.includeCarousel1.listHorizontal1.stopScroll();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            super.onScrolled(recyclerView, dx, dy);
            // y=dy;
            if (draggingView == 1 && recyclerView == fragmentList1Binding.includeCarousel1.listHorizontal1)
            {
                fragmentList1Binding.includeCarousel2.listHorizontal2.scrollBy(dx, dy);
            }
            else if (draggingView == 2 && recyclerView == fragmentList1Binding.includeCarousel2.listHorizontal2)
            {
                fragmentList1Binding.includeCarousel1.listHorizontal1.scrollBy(dx, dy);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        callSubPAge("CarouselDetail","Card");
    }

    public static void CustomProgressDialog(final String loading)
    {
        fragmentList1Binding.rlProgressDialog.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anticlockwise);
        fragmentList1Binding.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        fragmentList1Binding.imgConnecting2.startAnimation(anim1);
        fragmentList1Binding.txtProgressing.setText(loading+"...");

    }

    public static class HttpAsyncTaskSearch extends AsyncTask<String, Void, String> {
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
            // dialog.dismiss();
            fragmentList1Binding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if (result == "")
                {
                    Toast.makeText(mContext, "Slow Internet Connection", Toast.LENGTH_LONG).show();
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

                    allTags.clear();
                    try {

                        mAdapter.notifyDataSetChanged();
                        mAdapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }

                    if (connect.length() == 0)
                    {
                        fragmentList1Binding.tvFriendInfo.setVisibility(View.VISIBLE);
                        allTags.clear();
                        try {
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                        try {
                            mAdapter1.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                    else
                    {
                        fragmentList1Binding.tvFriendInfo.setVisibility(View.GONE);

                        for (int i = 0; i < connect.length(); i++)
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            FriendConnection connectModel = new FriendConnection();
                            connectModel.setUserID(iCon.getString("UserID"));
                            connectModel.setFirstName(iCon.getString("FirstName"));
                            connectModel.setLastName(iCon.getString("LastName"));
                            connectModel.setName(iCon.getString("FirstName") + " " + iCon.getString("LastName"));
                            connectModel.setUser_image(iCon.getString("UserPhoto"));
                            connectModel.setEmail(iCon.getString("UserName"));
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
                            connectModel.setAddress(iCon.getString("Address1") + " " + iCon.getString("Address2")
                                    + " " + iCon.getString("Address3") + " " + iCon.getString("Address4"));
                            allTags.add(connectModel);


                        }
                        GetData(mContext);
                        if (allTags.size() == 0) {
                            fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                        } else {
                            fragmentList1Binding.txtNoCard1.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POSTSearch(String url)
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
            if (!SearchFromDashboard.equals("")){
                jsonObject.accumulate("Search", SearchFromDashboard);
            }
            else {
                jsonObject.accumulate("Search", fragmentList1Binding.searchView.getText().toString());
            }
            jsonObject.accumulate("SearchType", "Local" );
            jsonObject.accumulate("UserID", UserId);
            jsonObject.accumulate("numofrecords", "30");
            jsonObject.accumulate("pageno", "1");

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
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

    public static void GetData(Context context)
    {
        images = new ArrayList<>();
        images1 = new ArrayList<>();
        //newly added
        nfcModel.clear();
        for (FriendConnection reTag : allTags)
        {

            FriendConnection nfcModelTag = new FriendConnection();
            // nfcModelTag.setId(reTag.getId());
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

        mAdapter = new GalleryAdapter(context, nfcModel);
        mAdapter1 = new GalleryAdapter1(context, nfcModel);
        mAdapter.notifyDataSetChanged();
        mAdapter1.notifyDataSetChanged();

        if (nfcModel.size() == 0)
        {
            fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
        }
        else
        {
            fragmentList1Binding.txtNoCard1.setVisibility(View.GONE);
        }

        if (Pref.getValue(context, "current_frag", "").equalsIgnoreCase("1")) {

            if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
                try {
                    if (Connection_Limit.equalsIgnoreCase("100000")) {
                        DashboardActivity.setActionBarTitle("Cards - " + count + "/", true);

                    } else {
                        DashboardActivity.setActionBarTitle("Cards - " + count + "/" + CardsActivity.Connection_Limit, false);
                    }
                } catch (Exception e) {

                }
            } else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
                DashboardActivity.setActionBarTitle("Cards - " + count, false);

            } else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
                DashboardActivity.setActionBarTitle("Cards - " + count, false);

            } else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect")) {
                // DashboardActivity.setActionBarTitle("Cards - " + count + "/", true);
                if (Connection_Limit.equalsIgnoreCase("100000")) {
                    DashboardActivity.setActionBarTitle("Cards - " + count + "/", true);

                } else {
                    DashboardActivity.setActionBarTitle("Cards - " + count + "/" + CardsActivity.Connection_Limit, false);

                }
            }
        }
//        CardsActivity.setActionBarTitle("Cards - "+number_cards);
        initRecyclerView1(fragmentList1Binding.includeCarousel1.listHorizontal1, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter);
        initRecyclerView2(fragmentList1Binding.includeCarousel2.listHorizontal2, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1);
    }

}