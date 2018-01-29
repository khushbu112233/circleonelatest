package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.SearchGroupMembers;
import com.circle8.circleOne.Adapter.SearchGroupMemberAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectList;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class ByNameGroupFragment extends Fragment {

    private ListView listView;
    private TextView tvDataInfo ;
    private String find_by = "NAME" ;
    private SearchGroupMemberAdapter connectListAdapter ;
    private AutoCompleteTextView searchText ;
    private ArrayList<ConnectList> connectTags = new ArrayList<>();
    private ArrayList<ConnectList> connectLists = new ArrayList<>();
    LoginSession session;
    String profileID, userID ;
    ImageView imgSearch;
    public ByNameGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_by_name_group, container, false);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);

        tvDataInfo = (TextView)view.findViewById(R.id.tvDataInfo);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.listViewType4);
        searchText.setHint("Search by name");
        listView.setVisibility(View.GONE);
//        tvDataInfo.setVisibility(View.VISIBLE);

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        profileID = user.get(LoginSession.KEY_PROFILEID);
        userID = user.get(LoginSession.KEY_USERID);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString().toLowerCase(Locale.getDefault());

                listView.setVisibility(View.VISIBLE);
                connectTags.clear();
                new HttpAsyncTask().execute(Utility.BASE_URL+"SearchConnect");
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
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

                    tvDataInfo.setVisibility(View.VISIBLE);
                    connectTags.clear();
                    connectLists.clear();
                    SearchGroupMembers.selectedStrings = new JSONArray();
//                    GetData(getContext());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
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

            String loading = "Searching" ;
            CustomProgressDialog(loading,getActivity());
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("FindBy", find_by );
                jsonObject.accumulate("Search", searchText.getText().toString() );
                jsonObject.accumulate("SearchType", "Local" );
                jsonObject.accumulate("UserID", userID );
                jsonObject.accumulate("numofrecords", "1000" );
                jsonObject.accumulate("pageno", "1" );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            // dialog.dismiss();
            dismissProgress();
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

//                    connectTags.clear();
                    try
                    {
                        connectListAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if(connect.length() == 0)
                    {
                        tvDataInfo.setVisibility(View.VISIBLE);
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

                            /*connectListAdapter = new SearchGroupMemberAdapter(getContext(),R.layout.row_add_group_member, connectTags);
                            listView.setAdapter(connectListAdapter);
                            connectListAdapter.notifyDataSetChanged();*/

//                            GetData(getContext());
                        }

                        GetData(getContext());

                        /*connectListAdapter = new SearchGroupMemberAdapter(getContext(),R.layout.row_add_group_member, connectTags);
                        listView.setAdapter(connectListAdapter);
                        connectListAdapter.notifyDataSetChanged();*/
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
            connectModelTag.setFacebook(reTag.getFacebook());
            connectModelTag.setTwitter(reTag.getTwitter());
            connectModelTag.setGoogle(reTag.getGoogle());
            connectModelTag.setLinkedin(reTag.getLinkedin());
            connectLists.add(connectModelTag);
        }

        if (connectLists.size() == 0)
        {
            tvDataInfo.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDataInfo.setVisibility(View.GONE);
            connectListAdapter = new SearchGroupMemberAdapter(context, R.layout.row_add_group_member, connectLists);
            listView.setAdapter(connectListAdapter);
        }
    }

}
