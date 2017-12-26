package com.circle8.circleOne.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.SortAndFilterOption;
import com.circle8.circleOne.Adapter.List3Adapter1;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Interface.OnImageClick;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class List3Fragment1 extends Fragment
{
    private static RecyclerView listView;
    public static List3Adapter1 gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<Integer> id;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;
    RelativeLayout lnrSearch;
    View line;
    private GestureDetector gestureDetector1;

    public static List<NFCModel> allTags ;
    boolean isHavingData = true;
    public static ArrayList<FriendConnection> allTaggs =new ArrayList<>();
    public static ArrayList<FriendConnection> searchTags =new ArrayList<>();

    //new asign value
    AutoCompleteTextView searchText ;
    ImageView imgSearch ;
    int limitpage = 10;
    public static TextView tvFriendInfo, txtNoCard1 ;
    public static ArrayList<NFCModel> nfcModel ;
    public static ArrayList<FriendConnection> nfcModel1 =new ArrayList<>();

    LoginSession session;
    static String UserId = "";
    static String ProfileId="";

    public static Context mContext ;
    public static int pageno = 1;

    static boolean  userScrolled = false;
    static RelativeLayout rlLoadMore ;

    int pos;
    public static String progressStatus = "FIRST" ;
    static String totalArray ;
    static int numberCount, listSize ;
    OnImageClick onImageClick;

    public List3Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list31, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = List3Fragment1.this.getContext() ;
        Utility.deleteCache(getContext());

        Utility.freeMemory();
        db = new DatabaseHelper(getContext());
        id = new ArrayList<Integer>();
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();

        listView = (RecyclerView) view.findViewById(R.id.listViewType3);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        imgSearch = (ImageView)view.findViewById(R.id.imgSearch);
        tvFriendInfo = (TextView)view.findViewById(R.id.tvFriendInfo);
        txtNoCard1 = (TextView) view.findViewById(R.id.txtNoCard1);
        rlLoadMore = (RelativeLayout)view.findViewById(R.id.rlLoadMore);

        pageno = 1;
        nfcModel = new ArrayList<>();
        //nfcModel1 = new ArrayList<>();
        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();
        searchTags = new ArrayList<>();

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        ProfileId = user.get(LoginSession.KEY_PROFILEID);

        //considering from database
//        allTags = db.getActiveNFC();



        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);

    /*    onImageClick = new OnImageClick() {
            @Override
            public void OnImageClick(int position) {
                Log.e("posfr",""+position);
                pos= position;
                new HttpAsyncTask_new().execute(Utility.BASE_URL+"FriendConnection_Operation");


            }
        };*/
        gridAdapter = new List3Adapter1(getContext(), allTaggs);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());

        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        // gridAdapter.onImageClick(onImageClick);

        listView.setAdapter(gridAdapter);

        /*ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listView);
*/
        callFirst();



        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                progressStatus = "LOAD MORE";

                int first = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int count = listView.getChildCount();


                if (first + count == gridAdapter.getItemCount() && isHavingData) {
                    pageno++;

                    if (gridAdapter.getItemCount() < limitpage) {

                    } else {
                        rlLoadMore.setVisibility(View.VISIBLE);
                        new HttpAsyncTask().execute(Utility.BASE_URL + SortAndFilterOption.CardListApi);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

      /*  GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();
        this.gestureDetector1= new GestureDetector(getContext(), gestureListener);
        this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);*/

        /*lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);*/



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



    /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
            }
        });*/
      /*  listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });*/

        //retrive data
//        GetData(getContext());

       /* listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/



        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                /*if(s.length() <= 0)
                {
                    nfcModel1.clear();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                }*/
                Utility.deleteCache(getContext());

                Utility.freeMemory();
                try
                {
                    if (s.length() <= 0)
                    {
                        pageno = 1;
                        //  nfcModel1.clear();
                        allTaggs.clear();

                        callFirst();
                        tvFriendInfo.setVisibility(View.GONE);

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

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.deleteCache(getContext());

                Utility.freeMemory();
                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    //  nfcModel1.clear();
                    allTaggs.clear();

                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0)
                {
                    //  nfcModel1.clear();
                    allTaggs.clear();

                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Utility.deleteCache(getContext());

                Utility.freeMemory();

                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    //nfcModel1.clear();
                    allTaggs.clear();

                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0)
                {
                    //  nfcModel1.clear();
                    allTaggs.clear();

                    new HttpAsyncTaskSearch().execute(Utility.BASE_URL+"SearchConnect");
                }

                return true;
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        Utility.deleteCache(getContext());
        Utility.freeMemory();
        super.onPause();
    }

    public  String POSTSearch(String url)
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
            jsonObject.accumulate("FindBy", "NAME" );
            jsonObject.accumulate("Search", searchText.getText().toString() );
            jsonObject.accumulate("SearchType", "Local" );
            jsonObject.accumulate("UserID", UserId);
            jsonObject.accumulate("numofrecords", "30" );
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



   /* @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        pos = viewHolder.getAdapterPosition();
        new HttpAsyncTask_new().execute(Utility.BASE_URL+"FriendConnection_Operation");
      *//*  ((List3Adapter1.MyViewHolder) viewHolder).viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask_new().execute(Utility.BASE_URL+"FriendConnection_Operation");

            }
        });
*//*


    }*/

    private class HttpAsyncTask_new extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting Records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Deleting records";
            CustomProgressDialog(loading,mContext);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST1(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if(result == "")
                {
                    Toast.makeText(mContext, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if(success.equals("1"))
                    {
                        List1Fragment.progressStatus = "DELETE";
                        List2Fragment.progressStatus = "DELETE";
                        List3Fragment.progressStatus = "DELETE";
                        List4Fragment.progressStatus = "DELETE";

                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        try
                        {
                            List1Fragment.webCall();
                            List2Fragment.webCall();
                            List3Fragment1.webCall();
                            List4Fragment1.webCall();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    }

                    /*try
                    {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
//                        List3Fragment.GetData(context);
//                        List3Fragment.allTaggs.clear();
//                        List3Fragment.nfcModel1.clear();
                    }
                    catch(Exception e) {    }

                    try
                    {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
//                        List2Fragment.allTaggs.clear();
//                        List2Fragment.nfcModel.clear();
//                        List2Fragment.GetData(context);
                    }
                    catch(Exception e) {    }


                    try
                    {
                        List4Fragment.gridAdapter.notifyDataSetChanged();
//                        List4Fragment.allTaggs.clear();
//                        List4Fragment.nfcModel1.clear();
//                        List4Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {
                        List1Fragment.mAdapter.notifyDataSetChanged();
                        List1Fragment.mAdapter1.notifyDataSetChanged();
//                        List1Fragment.allTags.clear();
//                        List1Fragment.nfcModel.clear();
//                        List1Fragment.GetData(context);
                    }
                    catch(Exception e) {    }*/

                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public  String POST1(String url)
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
            jsonObject.accumulate("Operation", "Remove" );
            jsonObject.accumulate("RequestType", "" );
            jsonObject.accumulate("connection_date", Utility.currentDate());
            jsonObject.accumulate("friendProfileId", allTaggs.get(pos).getProfile_id());
            jsonObject.accumulate("myProfileId",ProfileId);

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


    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, String>
    {
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
            CustomProgressDialog(loading,mContext);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POSTSearch(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
            // dialog.dismiss();
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
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

                    allTaggs.clear();

                    if(connect.length() == 0)
                    {
                        tvFriendInfo.setVisibility(View.VISIBLE);
                        allTaggs.clear();

                    }
                    else
                    {
                        tvFriendInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= connect.length() ; i++ )
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
                            connectModel.setAddress(iCon.getString("Address1") + " " + iCon.getString("Address2")
                                    + " " + iCon.getString("Address3") + " " + iCon.getString("Address4"));

                            allTaggs.add(connectModel);


                            gridAdapter.notifyDataSetChanged();
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

    public static void callFirst()
    {
        Utility.freeMemory();
        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
    }

    public static void webCall()
    {
        Utility.freeMemory();

        pageno = 1;

        try
        {
            //  nfcModel1.clear();
            allTaggs.clear();
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
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Fetching cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.setCancelable(false);
            if (progressStatus.equalsIgnoreCase("FIRST"))
            {
                String loading = "Fetching cards" ;
                CustomProgressDialog(loading,mContext);

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
                CustomProgressDialog(loading,mContext);
            }
            else
            {

            }

            /*String loading = "Fetching Cards" ;
            CustomProgressDialog(loading);*/
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
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    totalArray = jsonObject.getString("count");

                    String count = jsonObject.getString("count");
                    if(count.equals("") || count.equals("null"))
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(count);
                    }
//                    Toast.makeText(mContext,"Counts: "+numberCount,Toast.LENGTH_SHORT).show();

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


                        //   GetData(mContext);
                    }

                    listSize = allTaggs.size() ;


//                    Toast.makeText(mContext,"ListView size: "+allTaggs.size(),Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url)
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
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // pageno++;
        // 11. return result
        return result;
    }



    public static void GetData(Context context)
    {
        //nfcModel1.clear();

        for(FriendConnection reTag : allTaggs)
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
            nfcModel1.add(nfcModelTag);
        }

        if (nfcModel1.size() == 0) {
            txtNoCard1.setVisibility(View.VISIBLE);
        } else {
            txtNoCard1.setVisibility(View.GONE);
        }

        gridAdapter.notifyDataSetChanged();


        if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " +nfcModel .size() + "/" + CardsActivity.Connection_Limit);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + nfcModel1.size());
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + nfcModel1.size());
            }
        }
        else{
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + nfcModel1.size());
            }
        }

//        Toast.makeText(mContext,"ListView size: "+nfcModel1.size(),Toast.LENGTH_SHORT).show();
    }
}
