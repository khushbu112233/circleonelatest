package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.circle8.circleOne.Activity.ConnectActivity;
import com.circle8.circleOne.Adapter.ConnectListAdapter;
import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectList;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentConnectListBinding;
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
import java.util.Locale;
import java.util.Map;
import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class ByAssociationFragment  extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener
{
    private boolean netCheck = false;
    private String find_by = "ASSOCIATION" ;
    private ConnectListAdapter connectListAdapter ;
    private ArrayList<ConnectList> connectTags = new ArrayList<>();
    private ArrayList<ConnectList> connectLists = new ArrayList<>();
    LoginSession session;
    String profileID, userID ;
    static int numberCount, listSize;
    public static int pageno = 1 ;
    static String counts = "0" ;
    public static String progressStatus = "FIRST";
    Context context;
    View view;
    public ByAssociationFragment() {    }
    public static FragmentConnectListBinding fragmentConnectListBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentConnectListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_connect_list, container, false);
        view = fragmentConnectListBinding.getRoot();

        initUi();
        initClick();

        return view;
    }

    public void initClick() {
        fragmentConnectListBinding.imgSearch.setOnClickListener(this);
        fragmentConnectListBinding.listViewType4.setOnItemClickListener(this);

        fragmentConnectListBinding.searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Utility.freeMemory();
                if (netCheck == false){
                    Utility.freeMemory();
                    Toast.makeText(getContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                }
                else {
                    connectTags.clear();
                    //new HttpAsyncTask().execute(Utility.BASE_URL + "SearchConnect");
                    makeJsonObjectRequest();
                }
                return true;
            }
        });

        fragmentConnectListBinding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Utility.freeMemory();
                if(s.length() == 0)
                {
                    pageno = 1 ;
                    connectTags.clear();
                    connectLists.clear();
                    fragmentConnectListBinding.listViewType4.setStackFromBottom(false);
                    fragmentConnectListBinding.tvDataInfo.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initUi() {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        Utility.freeMemory();
        context = getActivity();
        fragmentConnectListBinding.searchView.setHint("Search by association");
        netCheck = Utility.isNetworkAvailable(getActivity());
        fragmentConnectListBinding.listViewType4.setVisibility(View.GONE);
        pageno = 1;
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        profileID = user.get(LoginSession.KEY_PROFILEID);
        userID = user.get(LoginSession.KEY_USERID);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.imgSearch:
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                String text = fragmentConnectListBinding.searchView.getText().toString().toLowerCase(Locale.getDefault());
                Utility.freeMemory();
                String Findby = "name";
                String Search = "Circle One" ;
                String rc_no = "10";
                String page_no = "1";
                fragmentConnectListBinding.listViewType4.setVisibility(View.VISIBLE);
                connectTags.clear();
                if (netCheck == false){
                    Utility.freeMemory();
                    Toast.makeText(getActivity(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                }
                else {
                    makeJsonObjectRequest();

                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {

        int id = v.getId();

        switch (id) {
            case R.id.listViewType4:
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                intent.putExtra("friendProfileID", connectLists.get(position).getProfile_id());
                intent.putExtra("friendUserID", connectLists.get(position).getUserID());
                intent.putExtra("ProfileID", profileID);
                getContext().startActivity(intent);
                Utility.freeMemory();
                break;
        }
    }

    private void makeJsonObjectRequest() {
        String loading = "Searching" ;
        CustomProgressDialog(loading,context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("FindBy", find_by );
            jsonObject.accumulate("Search", fragmentConnectListBinding.searchView.getText().toString() );
            jsonObject.accumulate("SearchType", "Global" );
            jsonObject.accumulate("UserID", userID );
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", pageno );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Utility.BASE_URL + "SearchConnect", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("", response.toString());

                try {
                    counts = response.getString("count");
                    JSONArray connect = response.getJSONArray("connect");

                    if (counts.equals("0") || counts == null)
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(counts);
                    }
                    fragmentConnectListBinding.rlLoadMore.setVisibility(View.GONE);

                    if(connect.length() == 0)
                    {
//                        tvDataInfo.setVisibility(View.VISIBLE);
                        connectTags.clear();
                        try {connectListAdapter.notifyDataSetChanged();}
                        catch (Exception e) { e.printStackTrace();}
                    }
                    else
                    {
                        fragmentConnectListBinding.tvDataInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i < connect.length() ; i++ )
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            ConnectList connectModel = new ConnectList();
                            connectModel.setUserID(iCon.getString("UserID"));
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

                        }

                        GetData(getContext());
                        listSize = connectTags.size();

                        fragmentConnectListBinding.listViewType4.setOnScrollListener(new AbsListView.OnScrollListener()
                        {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState)
                            {
                                // TODO Auto-generated method stub

                                progressStatus = "LOAD MORE";

                                if (listSize > 7)
                                {
                                    fragmentConnectListBinding.listViewType4.setStackFromBottom(true);
                                }

                                int threshold = 1;
                                int count = fragmentConnectListBinding.listViewType4.getCount();

                                if (scrollState == SCROLL_STATE_IDLE)
                                {
                                    if (listSize <= numberCount)
                                    {
                                        if (fragmentConnectListBinding.listViewType4.getLastVisiblePosition() >= count - threshold)
                                        {
                                            // rlLoadMore.setVisibility(View.VISIBLE);
                                            // Execute LoadMoreDataTask AsyncTask
                                            // new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
                                            makeJsonObjectRequest();
                                        }
                                    }
                                    else {  }
                                }
                            }
                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem,
                                                 int visibleItemCount, int totalItemCount) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }
                    dismissProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("te", "Error: " + error.getMessage());

                dismissProgress();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json; charset=utf-8");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressStatus.equalsIgnoreCase("LOAD MORE"))
            {

            }
            else
            {
                String loading = "Searching" ;
                CustomProgressDialog(loading,context);
            }
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("FindBy", find_by );
                jsonObject.accumulate("Search", fragmentConnectListBinding.searchView.getText().toString() );
                jsonObject.accumulate("SearchType", "Global" );
                jsonObject.accumulate("UserID", userID );
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", pageno );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
            //  dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getContext(), "Slow Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    counts = response.getString("count");

                    JSONArray connect = response.getJSONArray("connect");

                    if (counts.equals("0") || counts == null)
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(counts);
                    }
                    fragmentConnectListBinding.rlLoadMore.setVisibility(View.GONE);

                    if(connect.length() == 0)
                    {
//                        tvDataInfo.setVisibility(View.VISIBLE);
                        connectTags.clear();
                        try {connectListAdapter.notifyDataSetChanged();}
                        catch (Exception e) { e.printStackTrace();}
                    }
                    else
                    {
                        fragmentConnectListBinding.tvDataInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i < connect.length() ; i++ )
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            ConnectList connectModel = new ConnectList();
                            connectModel.setUserID(iCon.getString("UserID"));
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

                            /*connectListAdapter = new ConnectListAdapter(getContext(),R.layout.grid_list5_layout, connectTags);
                            listView.setAdapter(connectListAdapter);
                            connectListAdapter.notifyDataSetChanged();*/
                        }

                        GetData(getContext());
                        listSize = connectTags.size();

                        fragmentConnectListBinding.listViewType4.setOnScrollListener(new AbsListView.OnScrollListener()
                        {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState)
                            {
                                // TODO Auto-generated method stub

                                progressStatus = "LOAD MORE";

                                if (listSize > 7)
                                {
                                    fragmentConnectListBinding.listViewType4.setStackFromBottom(true);
                                }

                                int threshold = 1;
                                int count = fragmentConnectListBinding.listViewType4.getCount();

                                if (scrollState == SCROLL_STATE_IDLE)
                                {
                                    if (listSize <= numberCount)
                                    {
                                        if (fragmentConnectListBinding.listViewType4.getLastVisiblePosition() >= count - threshold)
                                        {
                                            // rlLoadMore.setVisibility(View.VISIBLE);
                                            // Execute LoadMoreDataTask AsyncTask
                                            // new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
                                            makeJsonObjectRequest();
                                        }
                                    }
                                    else {  }
                                }
                            }
                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem,
                                                 int visibleItemCount, int totalItemCount) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void GetData(Context context)
    {
        connectLists.clear();

        for(ConnectList reTag : connectTags)
        {
            ConnectList connectModelTag = new ConnectList();
            connectModelTag.setUserID(reTag.getUserID());
            connectModelTag.setProfile_id(reTag.getProfile_id());
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

        if (connectLists.size() == 0)
        {
            fragmentConnectListBinding.tvDataInfo.setVisibility(View.VISIBLE);
        }
        else
        {
            fragmentConnectListBinding.tvDataInfo.setVisibility(View.GONE);

            connectListAdapter = new ConnectListAdapter(getContext(), R.layout.grid_list5_layout, connectLists);
            fragmentConnectListBinding.listViewType4.setAdapter(connectListAdapter);
            connectListAdapter.notifyDataSetChanged();
        }

     /*   gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, nfcModel1);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();*/

        /*list5Adapter = new List5Adapter(getContext(), R.layout.grid_list4_layout, connectLists);
        listView.setAdapter(list5Adapter);
        list5Adapter.notifyDataSetChanged();*/
    }
}
