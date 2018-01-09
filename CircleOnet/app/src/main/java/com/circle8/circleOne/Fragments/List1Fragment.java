package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Adapter.GalleryAdapter;
import com.circle8.circleOne.Adapter.GalleryAdapter1;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
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

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class List1Fragment extends Fragment
{
    // private ArrayList<Integer> imageFront = new ArrayList<>();
    // private ArrayList<Integer> imageBack = new ArrayList<>();
    // public static MyPager myPager ;
    private GestureDetector gestureDetector1;
    /*ArrayList<byte[]> imgf;
    ArrayList<byte[]> imgb;
    ArrayList<String> tag_id;*/
    public static RelativeLayout lnrSearch;
    public static View line;
    private String DEBUG_TAG = "gesture";
    //  private GestureDetector gestureDetector;
    //  private View.OnTouchListener gestureListener;
    public static List<FriendConnection> allTags;
    //new asign value
    public static ArrayList<FriendConnection> nfcModel;
    ViewConfiguration vc;
    private int mTouchSlop;
    public static int pageno = 1;
    View view;
    private String TAG = CardsActivity.class.getSimpleName();
    public static ArrayList<Integer> images;
    public static ArrayList<Integer> images1;
    public static GalleryAdapter mAdapter;
    public static GalleryAdapter1 mAdapter1;
    public static CarouselLayoutManager manager1, manager2;
    private int draggingView = -1;
    RelativeLayout rlt;
    LoginSession session;
    static String UserId = "";
    public static Context mContext;
    public static String progressStatus = "FIRST";
    static int number_cards = 0;
    static int numberCount, recycleSize;
    static AlertDialog customProgressBar ;
    static String customProgressBarStatus = "";
    public static String count = "";
    static FragmentList1Binding fragmentList1Binding;
    public List1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        fragmentList1Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list1, container, false);
        view = fragmentList1Binding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        mContext = getContext();
        pageno = 1;
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        // name
        UserId = user.get(LoginSession.KEY_USERID);
        //viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        // lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);

        initRecyclerView1(fragmentList1Binding.includeCarousel1.listHorizontal1, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter);
        initRecyclerView2(fragmentList1Binding.includeCarousel2.listHorizontal2, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1);

        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(fragmentList1Binding.searchView.getWindowToken(), 0);

        line.setVisibility(View.VISIBLE);
        CardsFragment.tabLayout.setVisibility(View.VISIBLE);

       /* GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();
        gestureDetector1= new GestureDetector(getContext(), gestureListener);
        gestureDetector1.setOnDoubleTapListener(doubleTapListener);*/

        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();

        // new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");

        pageno = 1;
        allTags.clear();
        nfcModel.clear();
//        callFirst();

//        fragmentList1Binding.includeCarousel1.listHorizontal1.addOnScrollListener(scrollListener);
//        fragmentList1Binding.includeCarousel2.listHorizontal2.addOnScrollListener(scrollListener);

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

       /* frame.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                gestureDetector1.onTouchEvent(me);
                recyclerView1.requestFocus();
                recyclerView1.dispatchTouchEvent(me); // don't cause scrolling
                //recyclerView1.dispatchTouchEvent(me); // don't cause scrolling? Alternative solutoin?

                // fragmentList1Binding.includeCarousel2.listHorizontal2.requestFocus();
                // fragmentList1Binding.includeCarousel2.listHorizontal2.dispatchTouchEvent(mBackupTouchDownEvent); // don't cause scrolling
                //fragmentList1Binding.includeCarousel2.listHorizontal2.dispatchTouchEvent(me);
                return true;
            }
        });*/

       /* frame1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                gestureDetector1.onTouchEvent(me);
                fragmentList1Binding.includeCarousel2.listHorizontal2.requestFocus();
                fragmentList1Binding.includeCarousel2.listHorizontal2.dispatchTouchEvent(me); // don't cause scrolling
                //fragmentList1Binding.includeCarousel1.listHorizontal1.dispatchTouchEvent(me); // don't cause scrolling? Alternative solutoin?
                //fragmentList1Binding.includeCarousel2.listHorizontal2.requestFocus();
                // fragmentList1Binding.includeCarousel2.listHorizontal2.dispatchTouchEvent(mBackupTouchDownEvent); // don't cause scrolling
                //fragmentList1Binding.includeCarousel2.listHorizontal2.dispatchTouchEvent(me);
                return true;
            }
        });*/


        /*frame.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                fragmentList1Binding.includeCarousel1.listHorizontal1.addOnScrollListener(scrollListener);
                fragmentList1Binding.includeCarousel2.listHorizontal2.addOnScrollListener(scrollListener);
            }
        });*/
       /* fragmentList1Binding.includeCarousel1.listHorizontal1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector1.onTouchEvent(event);
            }
        });*/
        /*fragmentList1Binding.includeCarousel1.listHorizontal1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });*/


        /*fragmentList1Binding.includeCarousel2.listHorizontal2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
*/
        /*view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

// Start the animation
        view.animate()
                .translationY(view.getHeight())
                .alpha(1.0f);

        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });*/

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

       /* imgf = new ArrayList<byte[]>();
        imgb = new ArrayList<byte[]>();
        tag_id = new ArrayList<String>();*/

        //myPager = new MyPager(getContext(), imgf, imgb, tag_id);
        //viewPager.setAdapter(myPager);

        fragmentList1Binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               /* if(s.length() == 0)
                {
                    // allTags = db.getActiveNFC();
//                    GetData(getContext());
                    nfcModel.clear();
                    pageno = 1;
                    allTags.clear();
                    try
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){}
                    try
                    {
                        mAdapter1.notifyDataSetChanged();
                    }
                    catch (Exception e){}
                    callFirst();
                }*/
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
              /*  else if (s.length() > 0)
                {
                    String text = fragmentList1Binding.searchView.getText().toString().toLowerCase(Locale.getDefault());

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
                    new HttpAsyncTaskSearch().execute("http://circle8.asia:8999/Onet.svc/SearchConnect");
                }*/
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

        return view;
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        Utility.deleteCache(getContext());

        super.onPause();
    }

    public static void callFirst()
    {
        Utility.freeMemory();
        // tvNoCard.setVisibility(View.GONE);
        nfcModel.clear();
        pageno = 1;
        new HttpAsyncTask().execute(Utility.BASE_URL+SortFragment.CardListApi);
    }

    @Override
    public void onStart() {
        super.onStart();
        callFirst();
    }

    public static void webCall()
    {
       /* nfcModel.clear();
        allTags.clear();
        try
        {
            mAdapter.notifyDataSetChanged();
            mAdapter1.notifyDataSetChanged();
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
           /* dialog = new ProgressDialog(mContext);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.setCancelable(false);
            dialog.show();*/
           /* if(comeAtTime.equalsIgnoreCase("FIRST"))
            {
                dialog.show();
                comeAtTime = "SECOND";
            }
            else
            {
                dialog.dismiss();
            }*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

//            customProgressBarStatus = "run";
           /* String status = "true" ;
            String loading = "Fetching Cards..." ;
            CustomProgressBar(loading, status);*/

//            rlProgressDialog.setVisibility(View.VISIBLE);
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
                            fragmentList1Binding.txtNoCard1.setText("No such information found,\nplease try again");
                            fragmentList1Binding.txtNoCard1.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        fragmentList1Binding.txtNoCard1.setVisibility(View.GONE);
                    }

                    recycleSize = allTags.size();

                    // Load More in recycler view
                    /*final CarouselLayoutManager linearLayoutManager1 = (CarouselLayoutManager) fragmentList1Binding.includeCarousel1.listHorizontal1.getLayoutManager();
                    fragmentList1Binding.includeCarousel1.listHorizontal1.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                            super.onScrollStateChanged(recyclerView, scrollState);
                            int threshold = 1;
                            int count = linearLayoutManager1.getItemCount();
                            int lastVisibleItem, totalItemCount;

                            totalItemCount = linearLayoutManager1.getItemCount();
                            lastVisibleItem = linearLayoutManager1.getMaxVisibleItems();

                            if (scrollState == SCROLL_STATE_IDLE) {
                                if (recycleSize <= numberCount) {
                                    if (lastVisibleItem >= (count - threshold)) {
                                        rlLoadMore1.setVisibility(View.VISIBLE);
                                        rlLoadMore2.setVisibility(View.VISIBLE);
                                        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetFriendConnection");
                                    }
                                }
                            }
                        }
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });*/
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

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {

            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("Loading data..");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // allTags = db.getActiveNFC();

            images = new ArrayList<>();
            images1 = new ArrayList<>();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData(getContext());
            progressBar.dismiss();
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

   /* class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1000;

        @Override
        public boolean onDown(MotionEvent e) {
            //  Toast.makeText(getContext(), "onDown", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // Toast.makeText(context, "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //   Log.e(TAG, "onSingleTapUp");

            // final_position = List1Fragment.viewPager.getCurrentItem();
           *//* Intent intent = new Intent(getContext(), CardDetail.class);
            intent.putExtra("tag_id", GalleryAdapter.nfcModelList.get(GalleryAdapter.posi).getNfc_tag());
            getContext().startActivity(intent);
*//*            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            //Log.e(TAG, "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // Toast.makeText(context, "onLongPress", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Toast.makeText(getContext(), "onFling", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e1.getX() + ":" + e1.getY() + "  " + e2.getX() + ":" + e2.getY());
            boolean result = true;
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
                       *//* fragmentList1Binding.includeCarousel1.listHorizontal1.addOnScrollListener(scrollListener);
                        recyclerView2.addOnScrollListener(scrollListener);*//*
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //Toast.makeText(getContext(), "Down", Toast.LENGTH_LONG).show();
                            List1Fragment.lnrSearch.setVisibility(View.VISIBLE);
                            List1Fragment.line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        } else {
                            //  Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
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


    }
*/
   /* class MyOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //  Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTapEvent", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX() + ":" + e.getY());
            //  Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }*/

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

   /* GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            try {
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
                    lnrSearch.setVisibility(View.INVISIBLE);
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

    /*GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);*/

    @Override
    public void onResume() {
        super.onResume();
//        CardsActivity.setActionBarTitle("Cards - "+nfcModel.size());
//        callFirst();
//        nfcModel.clear();
//        GetData(getContext());
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

    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(getActivity());
//            dialog.setMessage("Searching Records...");
            //dialog.setTitle("Saving Reminder");
            //  dialog.show();
            //  dialog.setCancelable(false);
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
            // dialog.dismiss();
            fragmentList1Binding.rlProgressDialog.setVisibility(View.GONE);
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
            jsonObject.accumulate("Search", fragmentList1Binding.searchView.getText().toString());
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

        if (SortFragment.CardListApi.equalsIgnoreCase("GetFriendConnection"))
        {
                DashboardActivity.setActionBarTitle("Cards - " + count + "/" + CardsActivity.Connection_Limit);

        }
        else if (SortFragment.CardListApi.equalsIgnoreCase("GetProfileConnection"))
        {
                DashboardActivity.setActionBarTitle("Cards - " + count);
        }
        else if (SortFragment.CardListApi.equalsIgnoreCase("Group/FetchConnection"))
        {

                DashboardActivity.setActionBarTitle("Cards - " + count);
        }
        else if (SortFragment.CardListApi.equalsIgnoreCase("SearchConnect"))
        {
                DashboardActivity.setActionBarTitle("Cards - " + count);

        }

//        CardsActivity.setActionBarTitle("Cards - "+number_cards);
        initRecyclerView1(fragmentList1Binding.includeCarousel1.listHorizontal1, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter);
        initRecyclerView2(fragmentList1Binding.includeCarousel2.listHorizontal2, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1);

        // myPager = new MyPager(context, R.layout.cardview_list, nfcModel);
        //viewPager.setAdapter(myPager);
        //  myPager.notifyDataSetChanged();

        //gridAdapter.setMode(Attributes.Mode.Single);
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