package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.SortAndFilterOption;
import com.circle8.circleOne.Adapter.GridViewAdapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.ImageItem;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
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

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

/**
 * A simple {@link Fragment} subclass.
 */
public class List2Fragment extends Fragment
{
    private static GridView gridView;
    public static GridViewAdapter gridAdapter;
    ArrayList<byte[]> imgf;
    DatabaseHelper db;
    float x1, x2;
    float y1, y2;
    RelativeLayout lnrSearch;
    View line;
    private static final String TAG = "TestGesture";
    private GestureDetector gestureDetector1;

    public static List<NFCModel> allTags;
    public static ArrayList<FriendConnection> allTaggs;
    public static ArrayList<FriendConnection> nfcModel;

    LoginSession session;
    static String UserId = "";

    public static Context mContext;

    //new asign value
    AutoCompleteTextView searchText;
    ImageView imgSearch ;

    public static TextView txtNoCard1, tvFriendInfo;
    //    public static ArrayList<NFCModel> nfcModel ;
    public static int pageno = 1;

    static RelativeLayout rlLoadMore;
    View view;

    public static String progressStatus = "FIRST";

    static int numberCount, gridSize;
    public static String count, counts;
    public static FragmentList2Binding fragmentList2Binding;
    public List2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentList2Binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list2, container, false);
        view = fragmentList2Binding.getRoot();
        // Inflate the layout for this fragment
     //   View view = inflater.inflate(R.layout.fragment_list2, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = List2Fragment.this.getContext();
        pageno = 1;
        /*db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();

        List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getCard_front());
        }*/

        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        db = new DatabaseHelper(getContext());
        gridView = (GridView) view.findViewById(R.id.gridView);
        searchText = (AutoCompleteTextView) view.findViewById(R.id.searchView);
        imgSearch = (ImageView)view.findViewById(R.id.imgSearch);
        tvFriendInfo = (TextView)view.findViewById(R.id.tvFriendInfo);
        txtNoCard1 = (TextView) view.findViewById(R.id.txtNoCard1);
        rlLoadMore = (RelativeLayout) view.findViewById(R.id.rlLoadMore);
        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        Utility.deleteCache(getContext());
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_list2_layout, nfcModel);
        gridView.setAdapter(gridAdapter);

        callFirst();

        /*GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();
        this.gestureDetector1 = new GestureDetector(getContext(), gestureListener);
        this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);*/

        // gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, getData());
        // gridView.setAdapter(gridAdapter);
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        /*lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);*/

//        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

       /* gridView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                return gestureDetector1.onTouchEvent(me);
            }
        });*/

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CardDetail.class);
                intent.putExtra("tag_id", nfcModel.get(position).getNfc_tag());
                getContext().startActivity(intent);
            }
        });*/

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Utility.freeMemory();
               /* if(s.length() <= 0)
                {
                    nfcModel.clear();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                }*/
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
                        tvFriendInfo.setVisibility(View.GONE);
//                        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");
//                    GetData(getContext());
                    }
                   /* else if (s.length() > 0)
                    {
                        String text = searchText.getText().toString().toLowerCase(Locale.getDefault());

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
//                        allTaggs.clear();
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

//        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Utility.deleteCache(getContext());

                Utility.freeMemory();
                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel.clear();
                    allTaggs.clear();
                    try {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0)
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

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (searchText.getText().toString().length() == 0)
                {
                    pageno = 1;
                    nfcModel.clear();
                    allTaggs.clear();
                    try {
                        gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                    callFirst();
                    tvFriendInfo.setVisibility(View.GONE);
                }

                if (searchText.getText().toString().length() > 0)
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


        return view;
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
    public void onPause() {
        Utility.freeMemory();
        Utility.deleteCache(getContext());
        super.onPause();
    }

    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Searching Records...");
            //dialog.setTitle("Saving Reminder");
            //   dialog.show();
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
                        tvFriendInfo.setVisibility(View.VISIBLE);
                        allTaggs.clear();
                        try
                        {
                            gridAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){}
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
                            connectModel.setAddress(iCon.getString("Address1") + " " + iCon.getString("Address2")
                                    + " " + iCon.getString("Address3") + " " + iCon.getString("Address4"));

                            allTaggs.add(connectModel);

                            gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, allTaggs);
                            gridView.setAdapter(gridAdapter);
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
        Utility.freeMemory();
        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
    }

    public static void webCall()
    {
        /*nfcModel.clear();
        allTaggs.clear();
        try
        {
            gridAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");*/
        pageno = 1;

        try
        {
            nfcModel.clear();
            allTaggs.clear();
            gridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        new HttpAsyncTask().execute(Utility.BASE_URL+SortAndFilterOption.CardListApi);
    }


    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseHelper(getContext());
            nfcModel = new ArrayList<>();
            allTags = db.getActiveNFC();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData(getContext());
        }

    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < imgf.size(); i++) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgf.get(i), 0, imgf.get(i).length);
            // ImageView image = (ImageView) findViewById(R.id.imageView1);
            //  imageView.setImageBitmap(bmp);

            /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            Log.d ("image", stream.toByteArray().toString());*/
            //Toast.makeText(getContext(), stream.toByteArray().toString(), Toast.LENGTH_LONG).show();
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
           /* dialog = new ProgressDialog(mContext);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.setCancelable(false);*/

           /* if (comeAtTime.equalsIgnoreCase("FIRST")) {
                dialog.show();
                comeAtTime = "SECOND";
            } else {
                dialog.dismiss();
            }*/

          /*  String status = "true" ;
            String loading = "Fetching Cards..." ;
            CustomProgressBar(loading, status);*/

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

            /*String loading = "Fetching Cards" ;
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

            fragmentList2Binding.includeProgress.rlProgressDialog.setVisibility(View.GONE);

         /*   String status = "false" ;
            String loading = "Fetching Cards..." ;
            CustomProgressBar(loading, status);*/

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
                    if (SortAndFilterOption.CardListApi.equalsIgnoreCase("SearchConnect")){
                        jsonArray = jsonObject.getJSONArray("connect");
                    }else {
                        jsonArray = jsonObject.getJSONArray("connection");
                    }
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
//                    numberCount = jsonArray.length();

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

                    }

                    GetData(mContext);

                    gridSize = allTaggs.size();

                    gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState)
                        {
                            // TODO Auto-generated method stub

                            progressStatus = "LOAD MORE";

                            int threshold = 1;
                            int count = gridView.getCount();

                            if (scrollState == SCROLL_STATE_IDLE) {
                                if (gridSize <= numberCount) {
                                    if (gridView.getLastVisiblePosition() >= count - threshold) {
                                        // rlLoadMore.setVisibility(View.VISIBLE);
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


   /* GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            try
            {
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
                    lnrSearch.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);
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

   /* class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
          //  Toast.makeText(getContext(), "onDown", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
           // Toast.makeText(getContext(), "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
           // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
           // textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onSingleTapUp");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
           // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
           // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            Log.e(TAG, "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
           // Toast.makeText(getContext(), "onLongPress", Toast.LENGTH_LONG).show();
           // textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onLongPress");
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
                           // Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
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

   /* class MyOnDoubleTapListener implements GestureDetector.OnDoubleTapListener
    {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
          //  Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //Toast.makeText(getContext(), "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
           // Toast.makeText(getContext(), "onDoubleTapEvent", Toast.LENGTH_LONG).show();
           // textEvt2.setText(e.getX() + ":" + e.getY());
            Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }*/

    /*GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);
    */
//    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

   /* @Override
    public void onResume()
    {
        super.onResume();
        callFirst();
//        nfcModel.clear();
//        GetData(getContext());
    }*/


/*
    public static void GetData(Context context)
    {
        //newly added
        nfcModel.clear();
        for(FriendConnection reTag : allTags)
        {
            FriendConnection nfcModelTag = new FriendConnection();
            // nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setCard_back(reTag.getCard_back());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            nfcModel.add(nfcModelTag);
        }

    }
*/

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
            txtNoCard1.setVisibility(View.VISIBLE);
        } else {
            txtNoCard1.setVisibility(View.GONE);
        }

       /* Collections.sort(nfcModel, new Comparator<NFCModel>()
        {
            public int compare(NFCModel o1, NFCModel o2)
            {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });*/

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

        if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetFriendConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + counts + "/" + CardsActivity.Connection_Limit);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("GetProfileConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + counts);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("Group/FetchConnection")) {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + counts);
            }
        }
        else if (SortAndFilterOption.CardListApi.equalsIgnoreCase("SearchConnect"))
        {
            if (CardsActivity.mViewPager.getCurrentItem() == 0) {
                CardsActivity.setActionBarTitle("Cards - " + counts);
            }
        }

        gridAdapter.setMode(Attributes.Mode.Single);
    }

    public static void CustomProgressBar(String loading, String status)
    {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_progress_bar, null);
        ImageView imgConnecting = (ImageView)dialogView.findViewById(R.id.imgConnecting);
        ImageView imgConnecting1 = (ImageView)dialogView.findViewById(R.id.imgConnecting1);
        TextView tvProgressing = (TextView)dialogView.findViewById(R.id.txtProgressing);
        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        imgConnecting.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        imgConnecting1.startAnimation(anim1);
        tvProgressing.setText(loading);

        if (status.equals("true"))
        {
            dialog.setView(dialogView);
            dialog.show();
        }
        else if (status.equals("false"))
        {
            dialog.cancel();
        }
        else
        {

        }
    }

}
