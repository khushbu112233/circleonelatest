package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.EditGroupAdapter;
import com.circle8.circleOne.Adapter.GroupsRecyclerAdapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectProfileModel;
import com.circle8.circleOne.Model.ConnectingModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.R.id.rlConnect;
import static com.circle8.circleOne.R.id.view;

public class ConnectActivity extends AppCompatActivity
{
    private ImageView imgBack, imgCards, imgConnect, imgEvents, imgProfile, imgConnecting;
    private static final int PERMISSION_REQUEST_CODE = 200;
    CircleImageView ivProfileImage;

    private  RelativeLayout rlSocial ;
    private ImageView ivConnectImg, ivAddRound, ivConnectRound ;
    private TextView tvAdd, tvConnect, tvConnectLine1, tvConnectLine2, txtWeb, txtMail, txtNum, txtMob ;
    private RelativeLayout rlAdd, rlConnect ;
    private String tag_id, profile_id, friendProfile_id;

    ImageView imgAddGroupFriend;
    TextView tvAddedGroupInfo ;
    RecyclerView recycler_view ;
    ListView listView1, groupListView;
    List<CharSequence> list = new ArrayList<CharSequence>();
    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();
    ArrayList<String> listGroupId = new ArrayList<>();

    DatabaseHelper db;
    TextView tvPersonName, tvPersonDesignation, tvCompanyName;
    LinearLayout lnrWeb, lnrmail, lnrnum, lnrmob;

    LoginSession loginSession;
    String profileImg = "", friendUserID = "", user_id = "";
    public static JSONArray selectedStrings = new JSONArray();

    int motionLength = 0;

    private ArrayList<ConnectProfileModel> connectingTags = new ArrayList<>();
    private String Mobile1 = "", Mobile2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect2);

        db = new DatabaseHelper(getApplicationContext());

        txtWeb = (TextView)findViewById(R.id.txtWeb);
        txtMail = (TextView)findViewById(R.id.txtMail);
        txtNum = (TextView)findViewById(R.id.txtNum);
        txtMob = (TextView)findViewById(R.id.txtMob);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
//        imgConnecting = (ImageView) findViewById(R.id.fab);
        ivProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);
        ivConnectImg = (ImageView)findViewById(R.id.iv_ConnectImg);
        ivAddRound = (ImageView)findViewById(R.id.ivAddRound);
        ivConnectRound = (ImageView)findViewById(R.id.ivConnectRound);

        tvAdd = (TextView)findViewById(R.id.tvAdd);
        tvConnect = (TextView)findViewById(R.id.tvConnect);
        tvConnectLine1 = (TextView)findViewById(R.id.tvConnectLine1);
        tvConnectLine2 = (TextView)findViewById(R.id.tvConnectLine2);

        tvPersonName = (TextView)findViewById(R.id.tvPersonName);
        tvPersonDesignation = (TextView)findViewById(R.id.tvPersonDesignation);
        tvCompanyName = (TextView)findViewById(R.id.tvCompanyName);

        rlAdd = (RelativeLayout)findViewById(R.id.rlAdd);
        rlConnect = (RelativeLayout)findViewById(R.id.rlConnect);
        rlSocial = (RelativeLayout)findViewById(R.id.rlSocial);

        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        tvAddedGroupInfo = (TextView)findViewById(R.id.tvAddedGroupInfo);
        imgAddGroupFriend = (ImageView) findViewById(R.id.imgAddGroupFriend);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            motionLength = 180 ;
        }
        else
        {
            motionLength = 180 ;
        }


        /*Rect loc = new Rect();
        int[] location = new int[2];
        rlConnect.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + rlConnect.getWidth();
        loc.bottom = loc.top + rlConnect.getHeight();*/

//        Rect r_connect =
//        Toast.makeText(getApplicationContext(),"Connect pos: "+rlConnect.getX() +","+rlConnect.getY() ,Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
//        tag_id = intent.getStringExtra("tag_id");
        tag_id = "en100000001";
        friendProfile_id = intent.getStringExtra("friendProfileID");
        profile_id = intent.getStringExtra("ProfileID");
        friendUserID = intent.getStringExtra("friendUserID");

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

        if (friendProfile_id.equals(""))
        {
            Toast.makeText(ConnectActivity.this, "Having no friend profile ID",Toast.LENGTH_LONG).show();
        }
        else
        {
            new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/ConnectProfile");
        }

        new HttpAsyncTaskGroup().execute("http://circle8.asia:8999/Onet.svc/Group/Fetch");
        new HttpAsyncTaskGroupsFetch().execute("http://circle8.asia:8999/Onet.svc/Group/MyGroupsTaggedWithFriendProfile");


//        Toast.makeText(getApplicationContext(),"ProfileID & FriendID "+profile_id+" "+friendProfile_id,Toast.LENGTH_LONG).show();

/*
        if (tag_id.equals("en100000001")){
            level = "2";
        }
        else if (tag_id.equals("en100000002")){
            level = "3";
        }
        else if (tag_id.equals("en100000003")){
            level = "4";
        }
        else if (tag_id.equals("en100000004")){
            level = "5";
        }
        else if (tag_id.equals("en100000005")){
            level = "6";
        }
        else if (tag_id.equals("en100000006")){
            level = "1";
        }
        else if (tag_id.equals("en100000007")){
            level = "4";
        }
        else {
            level = "6";
        }*/
      /*  final List<NFCModel> modelList = db.getNFCbyTag(tag_id);
        try
        {
            if (modelList != null)
            {
                for (NFCModel tag1 : modelList)
                {
                    // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                    //Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                   // imgCard.setImageResource(tag1.getCard_front());

                    //  Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
//                    ivProfileImage.setImageResource(tag1.getUser_image());
//                    profile = tag1.getUser_image();
                    tvPersonName.setText(tag1.getName());
                    tvCompanyName.setText(tag1.getCompany());
                    txtWeb.setText(tag1.getWebsite());
                    txtMail.setText(tag1.getEmail());
                    txtNum.setText(tag1.getPh_no());
                   // txtWork.setText(tag1.getWork_no());
                    txtMob.setText(tag1.getMob_no());
                    tvPersonDesignation.setText(tag1.getDesignation());
                }
            }

        }
        catch (Exception e){  }*/

//        int x_left = rlConnect.getLeft();
//        Toast.makeText(getApplicationContext(),"From Left: "+x_left, Toast.LENGTH_SHORT).show();

        ivConnectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), txtMob.getText().toString());

                    if (aBoolean == true) {
                        TranslateAnimation slide1 = new TranslateAnimation(0, -(motionLength), 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivConnectRound.setImageResource(R.drawable.round_gray);
                                tvConnect.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine2.setTextColor(getResources().getColor(R.color.unselected));
                            }
                        }, 1600);
                        // Second Things
                        ivAddRound.setImageResource(R.drawable.round_blue);
                        tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else {

                    }
                }
            }
        });

        boolean result = Utility.checkContactPermission(ConnectActivity.this);
        if (result)
        {
            contactExists(getApplicationContext(), "+91 9737032082");
        }

        rlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), "+91 9737032082");

                    if (aBoolean == true)
                    {
                        TranslateAnimation slide1 = new TranslateAnimation(0, motionLength, 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                go.putExtra("profile", profileImg);
                                go.putExtra("friendUserID", friendUserID);
                                startActivity(go);
                                finish();
                            }
                        }, 1600);
                        // Second Things
                        ivConnectRound.setImageResource(R.drawable.round_blue);
                        tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    }
                    else
                    {
                        TranslateAnimation slide1 = new TranslateAnimation(0, motionLength, 0, 0);
                        slide1.setDuration(1000);
                        ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                go.putExtra("profile", profileImg);
                                go.putExtra("friendUserID", friendUserID);
                                startActivity(go);
                                finish();
                            }
                        }, 1600);
                        // Second Things
                        ivConnectRound.setImageResource(R.drawable.round_blue);
                        tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);
                startActivity(go);
                finish();
            }
        });

//        imgConnecting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
//
//                // you pass the position you want the viewpager to show in the extra,
//                // please don't forget to define and initialize the position variable
//                // properly
//
//                startActivity(go);
//                finish();
//            }
//        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

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
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

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
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

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
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });

        imgAddGroupFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(ConnectActivity.this);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.edit_groups_popup, null);
                listView1 = (ListView)dialogView.findViewById(R.id.listView);
                TextView tvGroupInfo = (TextView) dialogView.findViewById(R.id.tvGroupInfo);
                Button btnAddToGroup = (Button) dialogView.findViewById(R.id.btnAddToGroup);
                Button btnCancelGroup = (Button) dialogView.findViewById(R.id.btnCancelGroup);
                int count = dialogList.length;
                boolean[] is_checked = new boolean[count];

                final AlertDialog alertDialog = builderDialog.create();
                alertDialog.setCancelable(false);
                if(groupName.size() == 0)
                {
                    tvGroupInfo.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvGroupInfo.setVisibility(View.GONE);
                    ArrayList<GroupModel> groupModelArrayList1 = new ArrayList<GroupModel>();
                    groupModelArrayList1 = groupModelArrayList ;

//                EditGroupAdapter editGroupAdapter = new EditGroupAdapter(CardDetail.this, groupModelArrayList1);
                    EditGroupAdapter editGroupAdapter = new EditGroupAdapter(ConnectActivity.this, groupName, groupPhoto, listGroupId);
                    listView1.setAdapter(editGroupAdapter);
                    editGroupAdapter.notifyDataSetChanged();
                }

                btnCancelGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                btnAddToGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        // make selected item in the comma seprated string
                        //  Toast.makeText(getApplicationContext(), selectedStrings.toString(), Toast.LENGTH_LONG).show();
                        new HttpAsyncTaskGroupAddFriend().execute("http://circle8.asia:8999/Onet.svc/AddMemberToGroups");
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


    }

    public Rect locateView(View view)
    {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null)
        {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();

        return loc;
    }


    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                //Toast.makeText(getApplicationContext(), "Contact Exists", Toast.LENGTH_LONG).show();
                ivAddRound.setImageResource(R.drawable.round_blue);
                tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        //Toast.makeText(getApplicationContext(), "Contact doesn't Exists", Toast.LENGTH_LONG).show();
        ivAddRound.setImageResource(R.drawable.round_gray);
        tvAdd.setTextColor(getResources().getColor(R.color.unselected));
        tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/ConnectProfile");
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Displaying Records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
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
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String Matched = response.getString("Matched");

                    JSONObject profile = response.getJSONObject("Profile");

                    ConnectProfileModel connectingModel = new ConnectProfileModel();
                    connectingModel.setSuccess(profile.getString("success"));
                    connectingModel.setMessage(profile.getString("message"));
                    connectingModel.setCard_Front(profile.getString("Card_Front"));
                    connectingModel.setCard_Back(profile.getString("Card_Back"));
                    connectingModel.setFirstName(profile.getString("FirstName"));
                    connectingModel.setLastName(profile.getString("LastName"));
                    connectingModel.setUserPhoto(profile.getString("UserPhoto"));
                    connectingModel.setPhone1(profile.getString("Phone1"));
                    connectingModel.setPhone2(profile.getString("Phone2"));
                    connectingModel.setMobile1(profile.getString("Mobile1"));
                    connectingModel.setMobile2(profile.getString("Mobile2"));
                    connectingModel.setFax1(profile.getString("Fax1"));
                    connectingModel.setFax2(profile.getString("Fax2"));
                    connectingModel.setEmail1(profile.getString("Email1"));
                    connectingModel.setEmail2(profile.getString("Email2"));
                    connectingModel.setFacebook(profile.getString("Facebook"));
                    connectingModel.setTwitter(profile.getString("Twitter"));
                    connectingModel.setGoogle(profile.getString("Google"));
                    connectingModel.setLinkedIn(profile.getString("LinkedIn"));
                    connectingModel.setYoutube(profile.getString("Youtube"));
                    connectingModel.setIndustryName(profile.getString("IndustryName"));
                    connectingModel.setCompanyName(profile.getString("CompanyName"));
                    connectingModel.setCompanyProfile(profile.getString("CompanyProfile"));
                    connectingModel.setDesignation(profile.getString("Designation"));
                    connectingModel.setProfileDesc(profile.getString("ProfileDesc"));
                    connectingModel.setStatus(profile.getString("Status"));
                    connectingModel.setAddress1(profile.getString("Address1"));
                    connectingModel.setAddress2(profile.getString("Address2"));
                    connectingModel.setAddress3(profile.getString("Address3"));
                    connectingModel.setAddress4(profile.getString("Address4"));
                    connectingModel.setCity(profile.getString("City"));
                    connectingModel.setState(profile.getString("State"));
                    connectingModel.setCountry(profile.getString("Country"));
                    connectingModel.setPostalcode(profile.getString("Postalcode"));
                    connectingModel.setWebsite(profile.getString("Website"));
                    connectingModel.setAttachment_FileName(profile.getString("Attachment_FileName"));
                    connectingTags.add(connectingModel);

                    tvPersonName.setText(profile.getString("FirstName")+" "+profile.getString("LastName"));
                    tvCompanyName.setText(profile.getString("CompanyName"));
                    txtWeb.setText(profile.getString("Website"));
                    txtMail.setText(profile.getString("Email1"));
                    txtNum.setText(profile.getString("Phone1"));
                    // txtWork.setText(tag1.getWork_no());
                    txtMob.setText(profile.getString("Mobile1"));
                    Mobile1 = profile.getString("Mobile1");
                    Mobile2 = profile.getString("Mobile2");
                    tvPersonDesignation.setText(profile.getString("Designation"));

                    profileImg = "http://circle8.asia/App_ImgLib/UserProfile/"+profile.getString("UserPhoto");
                    if(profile.getString("UserPhoto").equalsIgnoreCase(""))
                    {
                        ivProfileImage.setImageResource(R.drawable.usr);
                    }
                    else
                    {
                        Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+profile.getString("UserPhoto")).into(ivProfileImage);
                    }

                    if (Matched.equals("1"))
                    {
                        ivAddRound.setImageResource(R.drawable.round_blue);
                        tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        rlAdd.setEnabled(true);
                    }
                    else if (Matched.equals("0"))
                    {
                        ivAddRound.setImageResource(R.drawable.round_gray);
                        tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                        tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                        rlAdd.setEnabled(false);
                    }
                    else if (Matched.equals("-1"))
                    {

                        if (Mobile1.equalsIgnoreCase("") && Mobile2.equalsIgnoreCase("")){
                            ivAddRound.setImageResource(R.drawable.round_gray);
                            tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                            tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                            rlAdd.setEnabled(false);
                        }
                        else if (!Mobile1.equalsIgnoreCase("")){
                            Boolean aBoolean = contactExists(getApplicationContext(), Mobile1.toString());
                            if (aBoolean == true) {
                                ivAddRound.setImageResource(R.drawable.round_blue);
                                tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                rlAdd.setEnabled(true);
                            }
                            else {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                rlAdd.setEnabled(false);
                            }
                        }
                        else if (!Mobile2.equalsIgnoreCase("")){
                            Boolean aBoolean = contactExists(getApplicationContext(), Mobile2.toString());
                            if (aBoolean == true) {
                                ivAddRound.setImageResource(R.drawable.round_blue);
                                tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                rlAdd.setEnabled(true);
                            }else {
                                ivAddRound.setImageResource(R.drawable.round_gray);
                                tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                rlAdd.setEnabled(false);
                            }
                        }

                    }
                    else if (Matched.equalsIgnoreCase("2")){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        ivAddRound.setImageResource(R.drawable.round_blue);
                                        tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        rlAdd.setEnabled(true);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        ivAddRound.setImageResource(R.drawable.round_gray);
                                        tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                        tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                        rlAdd.setEnabled(false);
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectActivity.this);
                        builder.setMessage("You are already Connected with this Friend to other Profile. Do you want to connect with this Profile?")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
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
            jsonObject.accumulate("friendprofileID", friendProfile_id );
            jsonObject.accumulate("myprofileID", profile_id);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class HttpAsyncTaskGroupAddFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Adding Friend...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return GroupAddFriendPost(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success");
                    String Message = jsonObject.getString("Message");
                    if (Success.equals("1")) {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to Add Friend in groups", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GroupAddFriendPost(String url) {
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
            jsonObject.accumulate("GroupIDs", selectedStrings);
            jsonObject.accumulate("ProfileId", friendProfile_id);
            jsonObject.accumulate("UserId", user_id);

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

    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Fetching Groups...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return GroupFetchPost(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        nfcModelTag.setGroup_Desc(object.getString("group_desc"));
                        nfcModelTag.setGroup_Photo(object.getString("group_photo"));
                        groupModelArrayList.add(nfcModelTag);
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                        list.add(object.getString("group_Name"));
                        listGroupId.add(object.getString("group_ID"));

                        groupName.add(object.getString("group_Name"));
                        groupPhoto.add(object.getString("group_photo"));
                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GroupFetchPost(String url)
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
            jsonObject.accumulate("numofrecords", "10");
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

    private class HttpAsyncTaskGroupsFetch extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching My Account...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return FetchGroupDataPost(urls[0]);
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
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    String counts = jsonObject.getString("Count").toString();

//                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                    JSONArray groupsArray = jsonObject.getJSONArray("Groups");

                    ArrayList<String> img = null;
                    ArrayList<String> name = null;
                    ArrayList<String> desc = null;

                    if (groupsArray.length() == 0)
                    {
                        tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);
                    }
                    else
                    {
                        for (int i = 0; i < groupsArray.length(); i++)
                        {
                            JSONObject groupsObj = groupsArray.getJSONObject(i);

                            String groupid = groupsObj.getString("group_ID");
                            String groupname = groupsObj.getString("group_Name");
                            String groupdesc = groupsObj.getString("group_desc");
                            String groupphoto = groupsObj.getString("group_photo");

                            img = new ArrayList<>();
                            name = new ArrayList<>();
                            desc = new ArrayList<>();

                            img.add(groupphoto);
                            name.add(groupname);
                            desc.add(groupdesc);
                        }

                        if (name.size() == 0)
                        {
                            tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tvAddedGroupInfo.setVisibility(View.GONE);
                        }
                            /*GroupsInCardDetailAdapter groupsInCardDetailAdapter = new GroupsInCardDetailAdapter(CardDetail.this, img,name,desc);
                            groupListView.setAdapter(groupsInCardDetailAdapter);
                            groupsInCardDetailAdapter.notifyDataSetChanged();*/

                        GroupsRecyclerAdapter groupsRecyclerAdapter = new GroupsRecyclerAdapter(ConnectActivity.this, img, name, desc);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recycler_view.setLayoutManager(new LinearLayoutManager(ConnectActivity.this, LinearLayoutManager.HORIZONTAL, true));
//                             recycler_view.setLayoutManager(mLayoutManager);
                        recycler_view.setItemAnimator(new DefaultItemAnimator());
                        recycler_view.setAdapter(groupsRecyclerAdapter);
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not able to Update Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public  String FetchGroupDataPost(String url)
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
            jsonObject.accumulate("ProfileId", friendProfile_id);
            jsonObject.accumulate("UserId", user_id );
            jsonObject.accumulate("numofrecords", "10");
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




}
