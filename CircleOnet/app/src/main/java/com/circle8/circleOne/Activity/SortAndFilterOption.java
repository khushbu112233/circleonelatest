package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.SortAndFilterAdapter;
import com.circle8.circleOne.Adapter.SortAndFilterProfileAdapter;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List3Fragment;
import com.circle8.circleOne.Fragments.List4Fragment;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
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

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;


public class SortAndFilterOption extends AppCompatActivity
{
    private TextView actionText;
    private ImageView imgCards, imgConnect, imgEvents, imgProfile;
    ImageView imgDrawer, imgLogo;
    private int actionBarHeight;
    LinearLayout lnrSortRecent, lnrSortName, lnrSortCompany;
    DatabaseHelper db;
    public static String SortType = "desc";
    public static String CardListApi = "GetFriendConnection";
    public static String ProfileArrayId = "", FindBY = "", Search = "";
    public static String groupId = "";
    LinearLayout lnrAllCards;
    ExpandableHeightListView listView, listView1, listView2 ;
    private LoginSession session;
    private String user_id, profile_id ;
    private ImageView ivArrowImg, ivArrowImg1, ivArrowImg2;
    private String arrowStatus = "RIGHT";
    private String arrowStatus1 = "RIGHT";
    private String arrowStatus2 = "RIGHT";
    AutoCompleteTextView searchView;
    public static ArrayList<GroupModel> groupModelArrayList;
    public static ArrayList<ProfileModel> profileModelArrayList ;
    SortAndFilterAdapter sortAndFilterAdapter ;
    SortAndFilterProfileAdapter sortAndFilterProfileAdapter ;
    LinearLayout lnrCompany, lnrTitle, lnrIndustry, lnrAssociation;
    RelativeLayout rltCircle, rltProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_filter_option);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        db = new DatabaseHelper(getApplicationContext());
        actionText = (TextView) findViewById(R.id.mytext);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        rltCircle = (RelativeLayout) findViewById(R.id.rltCircle);
        rltProfile = (RelativeLayout) findViewById(R.id.rltProfile);
        lnrTitle = (LinearLayout) findViewById(R.id.lnrTitle);
        lnrIndustry = (LinearLayout) findViewById(R.id.lnrIndustry);
        lnrAssociation = (LinearLayout) findViewById(R.id.lnrAssociation);

        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        lnrSortRecent = (LinearLayout) findViewById(R.id.lnrSortRecent);
        lnrSortName = (LinearLayout) findViewById(R.id.lnrSortName);
        lnrSortCompany = (LinearLayout) findViewById(R.id.lnrSortCompany);
        lnrAllCards = (LinearLayout) findViewById(R.id.lnrAllCards);
        listView1 = (ExpandableHeightListView)findViewById(R.id.listViewEx1);
        listView2 = (ExpandableHeightListView)findViewById(R.id.listViewEx2);
        ivArrowImg1 = (ImageView) findViewById(R.id.ivArrowImg1);
        ivArrowImg2 = (ImageView) findViewById(R.id.ivArrowImg2);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        lnrCompany = (LinearLayout) findViewById(R.id.lnrCompany);
        searchView = (AutoCompleteTextView) findViewById(R.id.searchView);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        profile_id = user.get(LoginSession.KEY_PROFILEID);

        groupModelArrayList = new ArrayList<>();
        profileModelArrayList = new ArrayList<>();

        new HttpAsyncTaskfetchGroup().execute(Utility.BASE_URL+"Group/Fetch");
        new HttpAsyncTaskFetchProfile().execute(Utility.BASE_URL+"MyProfiles");

        lnrAllCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "desc";
                CardListApi = "GetFriendConnection";
                List1Fragment.allTags.clear();
                List1Fragment.progressStatus = "FILTER";
                try
                {
                    List1Fragment.callFirst();
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.callFirst();
                } catch (Exception e) {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.callFirst();
                } catch (Exception e) {

                }
                try {
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.callFirst();
/*
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                } catch (Exception e) {

                }

                try {
                    //List1Fragment.myPager.notifyDataSetChanged();
                    // List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }
               /* Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);*/
                finish();
               // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
               /* Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);*/
            }
        });

        rltCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (arrowStatus1.equalsIgnoreCase("RIGHT"))
                {
                    ivArrowImg1.setImageResource(R.drawable.ic_down_arrow_blue);
                    listView1.setVisibility(View.VISIBLE);
                    arrowStatus1 = "DOWN";
                }
                else if (arrowStatus1.equalsIgnoreCase("DOWN"))
                {
                    ivArrowImg1.setImageResource(R.drawable.ic_right_arrow_blue);
                    listView1.setVisibility(View.GONE);
                    arrowStatus1 = "RIGHT";
                }
            }
        });

        rltProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (arrowStatus2.equalsIgnoreCase("RIGHT"))
                {
                    ivArrowImg2.setImageResource(R.drawable.ic_down_arrow_blue);
                    listView2.setVisibility(View.VISIBLE);
                    arrowStatus2 = "DOWN";
                }
                else if (arrowStatus2.equalsIgnoreCase("DOWN"))
                {
                    ivArrowImg2.setImageResource(R.drawable.ic_right_arrow_blue);
                    listView2.setVisibility(View.GONE);
                    arrowStatus2 = "RIGHT";
                }
            }
        });

        lnrSortRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SortType = "asc";
                CardListApi = "GetFriendConnection";
                List1Fragment.allTags.clear();
                List1Fragment.progressStatus = "FILTER";
                try
                {
                    List1Fragment.callFirst();
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.callFirst();
                } catch (Exception e) {

                }

            /*    try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }*/
                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.callFirst();
                } catch (Exception e) {

                }
                try {
                   /* List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.callFirst();
                } catch (Exception e) {

                }

                /*try {
                    //List1Fragment.myPager.notifyDataSetChanged();
                   // List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }*/
               /* Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);*/
                finish();
               // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
               /* Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);*/
            }
        });

        lnrSortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "Name";
                CardListApi = "GetFriendConnection";
                List1Fragment.progressStatus = "FILTER";
                List1Fragment.allTags.clear();
                try {
                    List1Fragment.callFirst();
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.callFirst();
                } catch (Exception e) {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.callFirst();
                } catch (Exception e) {

                }
                try {
                 /*   List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.callFirst();
                } catch (Exception e) {

                }

                /*try {
                    //List1Fragment.myPager.notifyDataSetChanged();
                   // List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }*/
               /* Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);*/
                finish();
               // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                /*Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);*/
            }
        });

        lnrSortCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortType = "CompanyName";
                CardListApi = "GetFriendConnection";
                List1Fragment.progressStatus = "FILTER";
                List1Fragment.allTags.clear();
                try {
                    List1Fragment.callFirst();
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.callFirst();
                } catch (Exception e) {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.callFirst();
                } catch (Exception e) {

                }
                try {
                   /* List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.callFirst();
                } catch (Exception e) {

                }

              /*  try {
                    //List1Fragment.myPager.notifyDataSetChanged();
                 //   List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }*/
              /*  Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);*/
                finish();
              //  overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                /*Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);*/
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(SortAndFilterOption.this,CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);

                startActivity(go);
                finish();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(SortAndFilterOption.this,CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);

                startActivity(go);
                finish();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(SortAndFilterOption.this,CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(SortAndFilterOption.this,CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SortType = "desc";
                CardListApi = "GetProfileConnection";
                ProfileArrayId = profileModelArrayList.get(position).getProfileID();
                List1Fragment.progressStatus = "FILTER";
                List1Fragment.allTags.clear();
                try {
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }
                try {
                  /*  List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }

                try {
                    //List1Fragment.myPager.notifyDataSetChanged();
                    //   List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }
                Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


            }
        });

        lnrCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "COMPANY";
                    Search = searchView.getText().toString();
                    List1Fragment.progressStatus = "FILTER";
                    List1Fragment.allTags.clear();

                    try {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.allTags = db.getActiveNFC();
                        List2Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List2Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.allTags = db.getActiveNFC();
                        List3Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List3Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    try {
                       /* List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());*/
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        //List1Fragment.myPager.notifyDataSetChanged();
                        //   List1Fragment.allTags = db.getActiveNFC();

                        List1Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List1Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });

        lnrTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "JOB_ROLE";
                    Search = searchView.getText().toString();
                    List1Fragment.progressStatus = "FILTER";
                    List1Fragment.allTags.clear();

                    try {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.allTags = db.getActiveNFC();
                        List2Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List2Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.allTags = db.getActiveNFC();
                        List3Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List3Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    try {
                     /*   List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());*/
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        //List1Fragment.myPager.notifyDataSetChanged();
                        //   List1Fragment.allTags = db.getActiveNFC();

                        List1Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List1Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });

        lnrAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "ASSOCIATION";
                    Search = searchView.getText().toString();
                    List1Fragment.progressStatus = "FILTER";
                    List1Fragment.allTags.clear();

                    try {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.allTags = db.getActiveNFC();
                        List2Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List2Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.allTags = db.getActiveNFC();
                        List3Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List3Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    try {
                      /*  List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());*/
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        //List1Fragment.myPager.notifyDataSetChanged();
                        //   List1Fragment.allTags = db.getActiveNFC();

                        List1Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List1Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });


        lnrIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please type some keyword", Toast.LENGTH_LONG).show();
                }else {
                    CardListApi = "SearchConnect";
                    FindBY = "INDUSTRY";
                    Search = searchView.getText().toString();
                    List1Fragment.progressStatus = "FILTER";
                    List1Fragment.allTags.clear();

                    try {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.allTags = db.getActiveNFC();
                        List2Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List2Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.allTags = db.getActiveNFC();
                        List3Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List3Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    try {
                      /*  List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());*/
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.allTags = db.getActiveNFC();
                        List4Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List4Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }

                    try {
                        //List1Fragment.myPager.notifyDataSetChanged();
                        //   List1Fragment.allTags = db.getActiveNFC();

                        List1Fragment.nfcModel.clear();
                        //  nfcModelList.clear();
                        List1Fragment.GetData(getApplicationContext());
                    } catch (Exception e) {

                    }
                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

               // SortType = "desc";
                CardListApi = "Group/FetchConnection";
                ProfileArrayId = profile_id;
                groupId = groupModelArrayList.get(position).getGroup_ID();
                List1Fragment.progressStatus = "FILTER";
                List1Fragment.allTags.clear();

                try
                {
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.GetData(getApplicationContext());
                }
                catch (Exception e)
                {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }
                try
                {
                  /*  List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());*/
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }

                try
                {
                    //List1Fragment.myPager.notifyDataSetChanged();
                    //   List1Fragment.allTags = db.getActiveNFC();

                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(getApplicationContext());
                } catch (Exception e) {

                }
                Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                userIntent.putExtra("viewpager_position", 0);
                startActivity(userIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

                /*TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }

                showDialog(SortAndFilterOption.this, 0, actionBarHeight);*/
                finish();
            }
        });

        actionText.setText("Sort & Filter");
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

    public void showDialog(Context context, int x, int y){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        final Dialog dialog  = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.listview_with_text_image);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout lnrMyAcc = (LinearLayout) dialog.findViewById(R.id.lnrMyAcc);
       /* lnrMyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });*/

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.x = x;
        lp.y = y;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private class HttpAsyncTaskfetchGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(SortAndFilterOption.this);
            dialog.setMessage("Fetching Groups...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return FetchGroupPost(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        listView1.setVisibility(View.GONE);
                        //txtGroup.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                       // listView.setVisibility(View.VISIBLE);
                        // txtGroup.setVisibility(View.GONE);
                    }
                    groupModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length() ; i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                      //  nfcModelTag.setProfileId1(object.getString("ProfileId"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        nfcModelTag.setGroup_Desc(object.getString("group_desc"));
                        nfcModelTag.setGroup_Photo(object.getString("group_photo"));

                        groupModelArrayList.add(nfcModelTag);

                       /* JSONArray memberArray = object.getJSONArray("Members");

                        if (memberArray.length() == 3)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));

                            //for 2nd member details
                            JSONObject memberObject1 = memberArray.getJSONObject(1);
                            nfcModelTag.setProfileId2(memberObject1.getString("ProfileId"));
                            nfcModelTag.setFirstName2(memberObject1.getString("FirstName"));
                            nfcModelTag.setLastName2(memberObject1.getString("LastName"));
                            nfcModelTag.setUserPhoto2(memberObject1.getString("UserPhoto"));
                            nfcModelTag.setCompanyName2(memberObject1.getString("CompanyName"));
                            nfcModelTag.setDesignation2(memberObject1.getString("Designation"));

                            //for 3rd member details
                            JSONObject memberObject2 = memberArray.getJSONObject(2);
                            nfcModelTag.setProfileId3(memberObject2.getString("ProfileId"));
                            nfcModelTag.setFirstName3(memberObject2.getString("FirstName"));
                            nfcModelTag.setLastName3(memberObject2.getString("LastName"));
                            nfcModelTag.setUserPhoto3(memberObject2.getString("UserPhoto"));
                            nfcModelTag.setCompanyName3(memberObject2.getString("CompanyName"));
                            nfcModelTag.setDesignation3(memberObject2.getString("Designation"));
                        }
                        else if (memberArray.length() == 2)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));

                            //for 2nd member details
                            JSONObject memberObject1 = memberArray.getJSONObject(1);
                            nfcModelTag.setProfileId2(memberObject1.getString("ProfileId"));
                            nfcModelTag.setFirstName2(memberObject1.getString("FirstName"));
                            nfcModelTag.setLastName2(memberObject1.getString("LastName"));
                            nfcModelTag.setUserPhoto2(memberObject1.getString("UserPhoto"));
                            nfcModelTag.setCompanyName2(memberObject1.getString("CompanyName"));
                            nfcModelTag.setDesignation2(memberObject1.getString("Designation"));
                        }
                        else if (memberArray.length() == 1)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));
                        }
                        else if (memberArray.length() == 0)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));
                        }
                        else
                        {

                        }*/
                    }

                    sortAndFilterAdapter = new SortAndFilterAdapter(SortAndFilterOption.this, groupModelArrayList);
                    listView1.setAdapter(sortAndFilterAdapter);
                    listView1.setExpanded(true);
                    sortAndFilterAdapter.notifyDataSetChanged();

                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
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
           /* dialog = new ProgressDialog(SortAndFilterOption.this);
            dialog.setMessage("Fetching Groups...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return FetchProfilePost(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                   // String profileID = jsonObject.getString("profileid");

                    JSONArray jsonArray = jsonObject.getJSONArray("Profiles");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        listView2.setVisibility(View.GONE);
                        //txtGroup.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        // listView.setVisibility(View.VISIBLE);
                        // txtGroup.setVisibility(View.GONE);
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

                    sortAndFilterProfileAdapter = new SortAndFilterProfileAdapter(SortAndFilterOption.this, profileModelArrayList);
                    listView2.setAdapter(sortAndFilterProfileAdapter);
                    listView2.setExpanded(true);
                    sortAndFilterProfileAdapter.notifyDataSetChanged();
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String FetchGroupPost(String url)
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
            jsonObject.accumulate("UserId", user_id);
            jsonObject.accumulate("numofrecords", "100");
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

    public String FetchProfilePost(String url)
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
            jsonObject.accumulate("numofrecords", "100");
            jsonObject.accumulate("pageno", "1");
            jsonObject.accumulate("userid", user_id);

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



}
