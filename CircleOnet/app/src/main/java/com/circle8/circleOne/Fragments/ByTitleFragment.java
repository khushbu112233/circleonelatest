package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.ConnectActivity;
import com.circle8.circleOne.Adapter.ConnectListAdapter;
import com.circle8.circleOne.Adapter.List5Adapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectList;
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
import java.util.Locale;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

/**
 * Created by ample-arch on 8/28/2017.
 */

public class ByTitleFragment extends Fragment
{
    public ByTitleFragment() {    }

    private ListView listView;
    private TextView tvDataInfo ;
    private String find_by = "JOB_ROLE" ;
    private List5Adapter list5Adapter ;
    private ConnectListAdapter connectListAdapter ;
    private AutoCompleteTextView searchText ;
    private ArrayList<ConnectList> connectTags = new ArrayList<>();
    private ArrayList<ConnectList> connectLists = new ArrayList<>();
    LoginSession session;
    String profileID, userID ;
    ImageView imgSearch;
    static RelativeLayout rlLoadMore ;
    static int numberCount, listSize;
    public static int pageno = 1 ;
    static String counts = "0" ;
    public static String progressStatus = "FIRST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_connect_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        tvDataInfo = (TextView)view.findViewById(R.id.tvDataInfo);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.listViewType4);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);
        searchText.setHint("Search by title");
        rlLoadMore = (RelativeLayout)view.findViewById(R.id.rlLoadMore);
        pageno = 1;

        listView.setVisibility(View.GONE);

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        profileID = user.get(LoginSession.KEY_PROFILEID);
        userID = user.get(LoginSession.KEY_USERID);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                listView.setVisibility(View.VISIBLE);
                connectTags.clear();
                new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");

            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                listView.setVisibility(View.VISIBLE);
                connectTags.clear();
                new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");

                return true;
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
                if(s.length() == 0)
                {
//                    listView.setVisibility(View.GONE);
                    pageno = 1;
                    connectTags.clear();
                    connectLists.clear();
                    listView.setStackFromBottom(false);
                    tvDataInfo.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                intent.putExtra("friendProfileID", connectLists.get(position).getProfile_id());
                intent.putExtra("friendUserID", connectLists.get(position).getUserID());
                intent.putExtra("ProfileID", profileID);
                getContext().startActivity(intent);
            }
        });

        return view;
    }


   /* @Override
    public void onResume()
    {
        super.onResume();
//        connectLists.clear();
        connectTags.clear();
        GetData(getContext());
    }*/

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Searching Records...");
            //dialog.setTitle("Saving Reminder");
           // dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            if (progressStatus.equalsIgnoreCase("LOAD MORE"))
            {

            }
            else
            {
                String loading = "Searching" ;
                CustomProgressDialog(loading,getActivity());
            }
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("FindBy", find_by );
                jsonObject.accumulate("Search", searchText.getText().toString() );
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
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String findBy = response.getString("FindBy");
                    String search = response.getString("Search");
                    counts = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

//                    connectTags.clear();

                    if (counts.equals("0") || counts == null)
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(counts);
                    }
                    rlLoadMore.setVisibility(View.GONE);

                    if(connect.length() == 0)
                    {
//                        tvDataInfo.setVisibility(View.VISIBLE);
                        connectTags.clear();
                        try {connectListAdapter.notifyDataSetChanged();}
                        catch (Exception e) { e.printStackTrace();}
                    }
                    else
                    {
                        tvDataInfo.setVisibility(View.GONE);
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

                           /* connectListAdapter = new ConnectListAdapter(getContext(),R.layout.grid_list5_layout, connectTags);
                            listView.setAdapter(connectListAdapter);
                            connectListAdapter.notifyDataSetChanged();*/
                        }

                        GetData(getContext());
                        listSize = connectTags.size();

                        listView.setOnScrollListener(new AbsListView.OnScrollListener()
                        {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState)
                            {
                                // TODO Auto-generated method stub

                                progressStatus = "LOAD MORE";

                                if (listSize > 7)
                                {
                                    listView.setStackFromBottom(true);
                                }

                                int threshold = 1;
                                int count = listView.getCount();

                                if (scrollState == SCROLL_STATE_IDLE)
                                {
                                    if (listSize <= numberCount)
                                    {
                                        if (listView.getLastVisiblePosition() >= count - threshold)
                                        {
                                           // rlLoadMore.setVisibility(View.VISIBLE);
                                            // Execute LoadMoreDataTask AsyncTask
                                            new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
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
            tvDataInfo.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDataInfo.setVisibility(View.GONE);

            connectListAdapter = new ConnectListAdapter(getContext(), R.layout.grid_list5_layout, connectLists);
            listView.setAdapter(connectListAdapter);
            connectListAdapter.notifyDataSetChanged();
        }

     /*   gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, nfcModel1);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();*/

      /*  list5Adapter = new List5Adapter(getContext(), R.layout.grid_list4_layout, connectLists);
        listView.setAdapter(list5Adapter);
        list5Adapter.notifyDataSetChanged();*/
    }
}