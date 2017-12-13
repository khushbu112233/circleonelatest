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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.ConnectActivity;
import com.circle8.circleOne.Activity.SearchGroupMembers;
import com.circle8.circleOne.Adapter.ConnectListAdapter;
import com.circle8.circleOne.Adapter.List5Adapter;
import com.circle8.circleOne.Adapter.SearchGroupMemberAdapter;
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

/**
 * Created by admin on 09/25/2017.
 */

public class ByIndustryGroupFragment extends Fragment
{
    public ByIndustryGroupFragment() {    }

    private ListView listView;
    private TextView tvDataInfo ;
    private String find_by = "INDUSTRY" ;
    private SearchGroupMemberAdapter connectListAdapter ;
    private AutoCompleteTextView searchText ;

    private ArrayList<ConnectList> connectTags = new ArrayList<>();
    private ArrayList<ConnectList> connectLists = new ArrayList<>();

    LoginSession session;
    String profileID, userID ;
    ImageView imgSearch;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    static RelativeLayout rlLoadMore ;
    static int numberCount, listSize;
    public static int pageno = 1 ;
    static String counts = "0" ;
    public static String progressStatus = "FIRST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_by_name_group, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);
        tvDataInfo = (TextView)view.findViewById(R.id.tvDataInfo);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.listViewType4);

        searchText.setHint("Search by industry");

        rlProgressDialog = (RelativeLayout)view.findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)view.findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)view.findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)view.findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)view.findViewById(R.id.imgConnecting3) ;

        listView.setVisibility(View.GONE);

        rlLoadMore = (RelativeLayout)view.findViewById(R.id.rlLoadMore);
        pageno = 1;

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        profileID = user.get(LoginSession.KEY_PROFILEID);
        userID = user.get(LoginSession.KEY_USERID);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                connectTags.clear();
                new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
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
                    pageno = 1 ;
                    tvDataInfo.setVisibility(View.VISIBLE);
                    connectTags.clear();
                    connectLists.clear();
                    listView.setStackFromBottom(false);
                    SearchGroupMembers.selectedStrings = new JSONArray();
                }
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
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
                String loading = "Searching records" ;
                CustomProgressDialog(loading);
            }
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
            //  dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
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

                           /* connectListAdapter = new SearchGroupMemberAdapter(getContext(),R.layout.row_add_group_member, connectTags);
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
                                            rlLoadMore.setVisibility(View.VISIBLE);
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
            jsonObject.accumulate("SearchType", "Local" );
            jsonObject.accumulate("UserID", userID );
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", pageno );

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

        pageno ++;
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

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getActivity(),R.anim.clockwise);
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

            connectListAdapter = new SearchGroupMemberAdapter(getContext(),R.layout.row_add_group_member, connectLists);
            listView.setAdapter(connectListAdapter);
            connectListAdapter.notifyDataSetChanged();
        }
    }

}
