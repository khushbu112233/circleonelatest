package com.circle8.circleOne.Fragments;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Adapter.SortAndFilterAdapter;
import com.circle8.circleOne.Adapter.SortAndFilterProfileAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivitySortAndFilterOptionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.callMainPage;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortFragment extends Fragment {

    private int actionBarHeight;
    public static String SortType = "desc";
    public static String CardListApi = "GetFriendConnection";
    public static String ProfileArrayId = "", FindBY = "", Search = "";
    public static String groupId = "";
    private LoginSession session;
    private String user_id, profile_id ;
    private String arrowStatus = "RIGHT";
    private String arrowStatus1 = "RIGHT";
    private String arrowStatus2 = "RIGHT";
    public static ArrayList<GroupModel> groupModelArrayList=new ArrayList<>();
    public static ArrayList<ProfileModel> profileModelArrayList =new ArrayList<>();
    SortAndFilterAdapter sortAndFilterAdapter ;
    SortAndFilterProfileAdapter sortAndFilterProfileAdapter ;
    ActivitySortAndFilterOptionBinding activitySortAndFilterOptionBinding;
    View view;
    private Fragment fragment;

    public SortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activitySortAndFilterOptionBinding = DataBindingUtil.inflate(inflater, R.layout.activity_sort_and_filter_option, container, false);
        view = activitySortAndFilterOptionBinding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DashboardActivity.setActionBarTitle("Sort & Filter", false);
        DashboardActivity.setDrawerVisibility(true);
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        groupModelArrayList = new ArrayList<>();
        profileModelArrayList = new ArrayList<>();

        sortAndFilterProfileAdapter = new SortAndFilterProfileAdapter(getContext(), profileModelArrayList);
        activitySortAndFilterOptionBinding.listViewEx2.setAdapter(sortAndFilterProfileAdapter);
        activitySortAndFilterOptionBinding.listViewEx2.setExpanded(true);

        sortAndFilterAdapter = new SortAndFilterAdapter(getContext(), groupModelArrayList);
        activitySortAndFilterOptionBinding.listViewEx1.setAdapter(sortAndFilterAdapter);
        activitySortAndFilterOptionBinding.listViewEx1.setExpanded(true);

        new HttpAsyncTaskfetchGroup().execute(Utility.BASE_URL+"Group/Fetch");

        activitySortAndFilterOptionBinding.lnrAllCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "desc";
                CardListApi = "GetFriendConnection";
                callSubPAge("AllCardsTapped","CardFilter");
                Fragment1and2call("FILTER");
                Fragment4call();
                Fragment1call();
                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        activitySortAndFilterOptionBinding.lnrSortRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SortType = "asc";
                CardListApi = "GetFriendConnection";
                callSubPAge("SortByOldestTapped","CardFilter");
                Fragment1and2call("FILTER");
                Fragment4call();

                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        activitySortAndFilterOptionBinding.lnrSortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "Name";
                CardListApi = "GetFriendConnection";
                callSubPAge("SortByNameTapped","CardFilter");
                Fragment1and2call("FILTER");
                Fragment4call();

                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

        activitySortAndFilterOptionBinding.lnrSortCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "CompanyName";
                CardListApi = "GetFriendConnection";
                callSubPAge("SortByCompanyNameTapped","CardFilter");
                Fragment1and2call("FILTER");
                Fragment4call();

                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        activitySortAndFilterOptionBinding.listViewEx2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SortType = "desc";
                CardListApi = "GetProfileConnection";
                ProfileArrayId = profileModelArrayList.get(position).getProfileID();

                Fragment1and2call("FILTER");
                Fragment4call();

                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

        activitySortAndFilterOptionBinding.lnrCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activitySortAndFilterOptionBinding.searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "COMPANY";
                    Search = activitySortAndFilterOptionBinding.searchView.getText().toString();
                    callSubPAge("SortByCompanyTapped","CardFilter");
                    Fragment1and2call("FILTER");
                    Fragment4call();
                    Fragment1call();
                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        activitySortAndFilterOptionBinding.lnrTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activitySortAndFilterOptionBinding.searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "JOB_ROLE";
                    Search = activitySortAndFilterOptionBinding.searchView.getText().toString();
                    callSubPAge("SortByTitleTapped","CardFilter");
                    Fragment1and2call("FILTER");
                    Fragment4call();
                    Fragment1call();
                    try {

                        List1Fragment.nfcModel.clear();
                        List1Fragment.GetData(getContext());
                    } catch (Exception e) {

                    }
                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        activitySortAndFilterOptionBinding.lnrAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubPAge("SortByProfileTapped","CardFilter");
                if (activitySortAndFilterOptionBinding.searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "ASSOCIATION";
                    Search = activitySortAndFilterOptionBinding.searchView.getText().toString();
                    callSubPAge("SortByAssociationTapped","CardFilter");
                    Fragment1and2call("FILTER");
                    Fragment4call();
                    Fragment1call();
                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


        activitySortAndFilterOptionBinding.lnrIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activitySortAndFilterOptionBinding.searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "INDUSTRY";
                    Search = activitySortAndFilterOptionBinding.searchView.getText().toString();
                    callSubPAge("SortByIndustryTapped","CardFilter");
                    Fragment1and2call("FILTER");
                    Fragment4call();
                    Fragment1call();
                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        activitySortAndFilterOptionBinding.listViewEx1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                CardListApi = "Group/FetchConnection";
                ProfileArrayId = profile_id;
                groupId = groupModelArrayList.get(position).getGroup_ID();

                Fragment1and2call("FILTER");
                Fragment4call();
                Fragment1call();
                Pref.setValue(getActivity(), "current_frag", "1");
                fragment = new CardsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        activitySortAndFilterOptionBinding.rltCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callSubPAge("SortByCircleTapped","CardFilter");
                if (arrowStatus1.equalsIgnoreCase("RIGHT"))
                {
                    activitySortAndFilterOptionBinding.ivArrowImg1.setImageResource(R.drawable.ic_down_arrow_blue);
                    activitySortAndFilterOptionBinding.listViewEx1.setVisibility(View.VISIBLE);
                    arrowStatus1 = "DOWN";
                }
                else if (arrowStatus1.equalsIgnoreCase("DOWN"))
                {
                    activitySortAndFilterOptionBinding.ivArrowImg1.setImageResource(R.drawable.ic_right_arrow_blue);
                    activitySortAndFilterOptionBinding.listViewEx1.setVisibility(View.GONE);
                    arrowStatus1 = "RIGHT";
                }
            }
        });

        activitySortAndFilterOptionBinding.rltProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (arrowStatus2.equalsIgnoreCase("RIGHT"))
                {
                    activitySortAndFilterOptionBinding.ivArrowImg2.setImageResource(R.drawable.ic_down_arrow_blue);
                    activitySortAndFilterOptionBinding.listViewEx2.setVisibility(View.VISIBLE);
                    arrowStatus2 = "DOWN";
                }
                else if (arrowStatus2.equalsIgnoreCase("DOWN"))
                {
                    activitySortAndFilterOptionBinding.ivArrowImg2.setImageResource(R.drawable.ic_right_arrow_blue);
                    activitySortAndFilterOptionBinding.listViewEx2.setVisibility(View.GONE);
                    arrowStatus2 = "RIGHT";
                }
            }
        });

        return view;
    }
    public void Fragment1call()
    {
        try {
            List1Fragment.nfcModel.clear();
            List1Fragment.GetData(getActivity());
        } catch (Exception e) {

        }
    }
    public void Fragment4call()
    {
        try {
            List4Fragment.gridAdapter.notifyDataSetChanged();
            List4Fragment.nfcModel.clear();
            List4Fragment.callFirst();
        } catch (Exception e) {

        }

    }
    public void Fragment1and2call(String status){
        List1Fragment.allTags.clear();
        List1Fragment.progressStatus =status;
        try
        {
            List1Fragment.callFirst();
            List2Fragment.gridAdapter.notifyDataSetChanged();
            List2Fragment.nfcModel.clear();
            List2Fragment.callFirst();
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callMainPage("CardFilter");

    }

    private class HttpAsyncTaskfetchGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("UserId", user_id);
                jsonObject.accumulate("numofrecords", "100");
                jsonObject.accumulate("pageno", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");

                    if (jsonArray.length() == 0)
                    {
                        activitySortAndFilterOptionBinding.listViewEx1.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length() ; i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        nfcModelTag.setGroup_Desc(object.getString("group_desc"));
                        nfcModelTag.setGroup_Photo(object.getString("group_photo"));
                        groupModelArrayList.add(nfcModelTag);
                    }
                    sortAndFilterAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getActivity(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

                new HttpAsyncTaskFetchProfile().execute(Utility.BASE_URL+"MyProfiles");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskFetchProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("numofrecords", "100");
                jsonObject.accumulate("pageno", "1");
                jsonObject.accumulate("userid", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Profiles");

                    if (jsonArray.length() == 0)
                    {
                        activitySortAndFilterOptionBinding.listViewEx2.setVisibility(View.GONE);
                    }

                    profileModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length() ; i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        ProfileModel nfcModelTag = new ProfileModel();
                        nfcModelTag.setUserID(object.getString("UserID"));
                        nfcModelTag.setProfileID(object.getString("ProfileID"));
                        nfcModelTag.setProfileName(object.getString("ProfileName"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        profileModelArrayList.add(nfcModelTag);
                    }

                    sortAndFilterProfileAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getActivity(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}


