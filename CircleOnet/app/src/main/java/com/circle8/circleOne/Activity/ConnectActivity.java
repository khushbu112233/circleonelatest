package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.EditGroupAdapter;
import com.circle8.circleOne.Adapter.GroupsRecyclerAdapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectProfileModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityConnect2Binding;
import com.squareup.picasso.Picasso;

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

public class ConnectActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CODE = 200;
    int left;
    int right;
    View tvConnectLine2, tvConnectLine1;
    private String tag_id, profile_id, friendProfile_id;
    List<CharSequence> list = new ArrayList<CharSequence>();
    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();
    ArrayList<String> listGroupId = new ArrayList<>();
    DatabaseHelper db;
    LoginSession loginSession;
    String profileImg = "", friendUserID = "", user_id = "", profileName = "";
    public static JSONArray selectedStrings1 = new JSONArray();
    int motionLength = 0;
    int lineWidth = 0 , roundWidth = 0;
    private ArrayList<ConnectProfileModel> connectingTags = new ArrayList<>();
    private String Mobile1 = "", Mobile2 = "";
    private String displayProfile;
    ActivityConnect2Binding activityConnect2Binding;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityConnect2Binding = DataBindingUtil.setContentView(this,R.layout.activity_connect2);
        Utility.freeMemory();
        db = new DatabaseHelper(getApplicationContext());


        tvConnectLine1 = findViewById(R.id.tvConnectLine1);
        tvConnectLine2 = findViewById(R.id.tvConnectLine2);



        boolean result1 = Utility.checkContactPermission(ConnectActivity.this);

        activityConnect2Binding.rlAdd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = activityConnect2Binding.rlAdd.getHeight();
                int width = activityConnect2Binding.rlAdd.getWidth();
                int L = activityConnect2Binding.rlAdd.getLeft();
                int T = activityConnect2Binding.rlAdd.getTop();
                int R = activityConnect2Binding.rlAdd.getRight();
                int B = activityConnect2Binding.rlAdd.getBottom();

                roundWidth = width / 2;
                motionLength = motionLength + roundWidth;

                System.out.print("ivMale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                activityConnect2Binding.rlAdd.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        activityConnect2Binding.rlConnect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = activityConnect2Binding.rlConnect.getHeight();
                int width = activityConnect2Binding.rlConnect.getWidth();
                int L = activityConnect2Binding.rlConnect.getLeft();
                int T = activityConnect2Binding.rlConnect.getTop();
                int R = activityConnect2Binding.rlConnect.getRight();
                int B = activityConnect2Binding.rlConnect.getBottom();

                roundWidth = width / 2;
                motionLength = motionLength + roundWidth;

                System.out.print("ivMale" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                activityConnect2Binding.rlConnect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        activityConnect2Binding.ivConnectImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = activityConnect2Binding.ivConnectImg.getHeight();
                int width = activityConnect2Binding.ivConnectImg.getWidth();
                int L = activityConnect2Binding.ivConnectImg.getLeft();
                int T = activityConnect2Binding.ivConnectImg.getTop();
                int R = activityConnect2Binding.ivConnectImg.getRight();
                int B = activityConnect2Binding.ivConnectImg.getBottom();
                lineWidth = width;
                motionLength = motionLength + lineWidth;
                System.out.print("ivConnect" + height + " " + width + " " + L + " " + R + " " + T + " " + B);
//                Toast.makeText(RegisterActivity.this, height+" "+width+" "+L+" "+R+" "+T+" "+B,Toast.LENGTH_LONG).show();
                //don't forget to remove the listener to prevent being called again by future layout events:
                activityConnect2Binding.ivConnectImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

/*
        rlConnect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                Utility.freeMemory();
                int height = rlConnect.getHeight();
                int width = rlConnect.getWidth();
                left = rlConnect.getLeft();
                right = rlConnect.getTop();
                //don't forget to remove the listener to prevent being called again by future layout events:
                rlConnect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Utility.freeMemory();
            motionLength = 180 ;
        }
        else
        {
            motionLength = 180 ;
        }*/

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
        Utility.freeMemory();
        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

        if (friendProfile_id.equals(""))
        {
            Toast.makeText(ConnectActivity.this, "Do not have friend profile ID",Toast.LENGTH_LONG).show();
        }
        else
        {
            new HttpAsyncTask().execute(Utility.BASE_URL+"ConnectProfile");
        }

        new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/Fetch");
        new HttpAsyncTaskGroupsFetch().execute(Utility.BASE_URL+"Group/MyGroupsTaggedWithFriendProfile");


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

        activityConnect2Binding.ivConnectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
            }
        });

        activityConnect2Binding.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                final Dialog dialog = new Dialog(ConnectActivity.this);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (displayProfile.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+displayProfile).resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"UserProfile/"+displayProfile);
                        startActivity(intent);
                    }
                });

               /* WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                wmlp.y = 300;*/   //y position
                dialog.show();
            }
        });

        activityConnect2Binding.rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    //Boolean aBoolean = contactExists(getApplicationContext(), txtMob.getText().toString());

                    TranslateAnimation slide1 = new TranslateAnimation(0, -(motionLength+13), 0, 0);
                    slide1.setDuration(1000);
                    activityConnect2Binding.ivConnectImg.startAnimation(slide1);

                    //first things
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activityConnect2Binding.ivConnectRound.setImageResource(R.drawable.round_gray);
                            activityConnect2Binding.tvConnect.setTextColor(getResources().getColor(R.color.unselected));
                            // tvConnectLine2.setTextColor(getResources().getColor(R.color.unselected));
                            tvConnectLine2.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            new HttpAsyncAddFriendTask().execute(Utility.BASE_URL+"FriendConnection_Operation");

                        }
                    }, 1700);
                    // Second Things
                    activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                    activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                    // tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                }
            }
        });

      /*  boolean result = Utility.checkContactPermission(ConnectActivity.this);
        if (result)
        {
            contactExists(getApplicationContext(), "+91 ");
        }
*/
        activityConnect2Binding.rlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                boolean result = Utility.checkContactPermission(ConnectActivity.this);
                if (result) {
                    Boolean aBoolean = contactExists(getApplicationContext(), "+91 9737032082");

                    if (aBoolean == true)
                    {
                        TranslateAnimation slide1 = new TranslateAnimation(0, (motionLength+13), 0, 0);
                        slide1.setDuration(1000);
                        activityConnect2Binding.ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                                activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                                //tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                go.putExtra("profile", profileImg);
                                go.putExtra("friendUserID", friendUserID);
                                go.putExtra("profileName", profileName);
                                startActivity(go);
                                // finish();
                            }
                        }, 1700);
                        // Second Things
                        activityConnect2Binding.ivConnectRound.setImageResource(R.drawable.round_blue);
                        activityConnect2Binding.tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        //   tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine2.setBackground(getResources().getDrawable(R.drawable.dotted));
                    }
                    else
                    {
                        TranslateAnimation slide1 = new TranslateAnimation(0, motionLength+13, 0, 0);
                        slide1.setDuration(1000);
                        activityConnect2Binding.ivConnectImg.startAnimation(slide1);

                        //first things
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                                activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                //tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                                Intent go = new Intent(getApplicationContext(),Connect3Activity.class);
                                go.putExtra("profile", profileImg);
                                go.putExtra("friendUserID", friendUserID);
                                go.putExtra("profileName", profileName);
                                startActivity(go);
                                // finish();
                            }
                        }, 1600);
                        // Second Things
                        activityConnect2Binding.ivConnectRound.setImageResource(R.drawable.round_blue);
                        activityConnect2Binding.tvConnect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        //  tvConnectLine2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                    }
                }
            }
        });

        activityConnect2Binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
               /* Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);
                startActivity(go);*/
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

        activityConnect2Binding.imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);

                startActivity(go);
                finish();
            }
        });

        activityConnect2Binding.imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);

                startActivity(go);
                finish();
            }
        });

        activityConnect2Binding.imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();
            }
        });

        activityConnect2Binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });

        activityConnect2Binding.imgAddGroupFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
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
                        new HttpAsyncTaskGroupAddFriend().execute(Utility.BASE_URL+"AddMemberToGroups");
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


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

    public String POSTRequest(String url) {
        Utility.freeMemory();
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
            jsonObject.accumulate("Operation", "Request");
            jsonObject.accumulate("RequestType", "");
            jsonObject.accumulate("connection_date", Utility.currentDate());
            jsonObject.accumulate("friendProfileId", friendProfile_id);
            jsonObject.accumulate("myProfileId", profile_id);

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


    private class HttpAsyncAddFriendTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Requesting Friend...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Requesting friend" ;
            CustomProgressDialog(loading, ConnectActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTRequest(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result == "") {
                    Toast.makeText(getApplicationContext(), "Check data connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.successful_request_sent), Toast.LENGTH_LONG).show();
                        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                        activityConnect2Binding.rlAdd.setEnabled(false);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean contactExists(Context context, String number) {
        Utility.freeMemory();
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                //Toast.makeText(getApplicationContext(), "Contact Exists", Toast.LENGTH_LONG).show();
                activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                //tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        //Toast.makeText(getApplicationContext(), "Contact doesn't Exists", Toast.LENGTH_LONG).show();
        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
        //tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //new HttpAsyncTask().execute(Utility.BASE_URL+"ConnectProfile");
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Displaying Records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Displaying records" ;
            CustomProgressDialog(loading, ConnectActivity.this);
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
//            dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            Utility.freeMemory();
            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check data connection", Toast.LENGTH_LONG).show();
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

                    activityConnect2Binding.tvPersonName.setText(profile.getString("FirstName")+" "+profile.getString("LastName"));

                    if (profile.getString("Designation").equalsIgnoreCase(""))
                    {
                        activityConnect2Binding.tvPersonDesignation.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityConnect2Binding.tvPersonDesignation.setText(profile.getString("Designation"));
                    }

                    if (profile.getString("CompanyName").equalsIgnoreCase(""))
                    {
                        activityConnect2Binding.tvCompanyName.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityConnect2Binding.tvCompanyName.setText(profile.getString("CompanyName"));
                    }

                    activityConnect2Binding.txtWeb.setText(profile.getString("Website"));
                    activityConnect2Binding.txtMail.setText(profile.getString("Email1"));
                    activityConnect2Binding.txtNum.setText(profile.getString("Phone1"));
                    // txtWork.setText(tag1.getWork_no());
                    activityConnect2Binding.txtMob.setText(profile.getString("Mobile1"));
                    Mobile1 = profile.getString("Mobile1");
                    Mobile2 = profile.getString("Mobile2");

                    profileImg = Utility.BASE_IMAGE_URL+"UserProfile/"+profile.getString("UserPhoto");
                    displayProfile = profile.getString("UserPhoto");
                    profileName = profile.getString("FirstName")+" "+profile.getString("LastName");

                    if(profile.getString("UserPhoto").equalsIgnoreCase(""))
                    {
                        activityConnect2Binding.ivProfileImage.setImageResource(R.drawable.usr_white1);
                    }
                    else
                    {
                        Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+profile.getString("UserPhoto")).resize(300,300).onlyScaleDown().skipMemoryCache().into(activityConnect2Binding.ivProfileImage);
                    }

                    if (Matched.equals("1"))
                    {
                        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                        //  tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                        activityConnect2Binding. rlAdd.setEnabled(true);

                    }
                    else if (Matched.equals("0"))
                    {
                        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                        // tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                        activityConnect2Binding.rlAdd.setEnabled(false);

                        Intent intent = new Intent(getApplicationContext(), CardDetail.class);
                        intent.putExtra("tag_id", "");
                        intent.putExtra("profile_id", friendProfile_id);
                        intent.putExtra("DateInitiated","");
                        intent.putExtra("lat", "");
                        intent.putExtra("long", "");
                        startActivity(intent);
                        finish();
                    }
                    else if (Matched.equals("-1"))
                    {

                        if (Mobile1.equalsIgnoreCase("") && Mobile2.equalsIgnoreCase("")){
                            activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                            activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                            //  tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                            tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                            activityConnect2Binding.rlAdd.setEnabled(false);
                        }
                        else if (!Mobile1.equalsIgnoreCase("")){
                            boolean result1 = Utility.checkContactPermission(ConnectActivity.this);
                            if (result1) {
                                Boolean aBoolean = contactExists(getApplicationContext(), Mobile1.toString());
                                if (aBoolean == true) {
                                    activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                                    activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    //   tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                                    activityConnect2Binding.rlAdd.setEnabled(true);
                                } else {
                                    activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                                    activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                    //  tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                    tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                                    activityConnect2Binding.rlAdd.setEnabled(false);
                                }
                            }
                        }
                        else if (!Mobile2.equalsIgnoreCase("")){
                            boolean result1 = Utility.checkContactPermission(ConnectActivity.this);
                            if (result1) {
                                Boolean aBoolean = contactExists(getApplicationContext(), Mobile2.toString());
                                if (aBoolean == true) {
                                    activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                                    activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    // tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                                    activityConnect2Binding.rlAdd.setEnabled(true);
                                } else {
                                    activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                                    activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                    //  tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                    tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                                    activityConnect2Binding.rlAdd.setEnabled(false);
                                }
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
                                        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_blue);
                                        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        // tvConnectLine1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted));
                                        activityConnect2Binding.rlAdd.setEnabled(true);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        activityConnect2Binding.ivAddRound.setImageResource(R.drawable.round_gray);
                                        activityConnect2Binding.tvAdd.setTextColor(getResources().getColor(R.color.unselected));
                                        //tvConnectLine1.setTextColor(getResources().getColor(R.color.unselected));
                                        tvConnectLine1.setBackground(getResources().getDrawable(R.drawable.dotted_gray));
                                        activityConnect2Binding.rlAdd.setEnabled(false);
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
            Utility.freeMemory();
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


    private class HttpAsyncTaskGroupAddFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Adding Friend...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Adding friend" ;
            CustomProgressDialog(loading, ConnectActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            return GroupAddFriendPost(urls[0]);
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
                    Toast.makeText(getApplicationContext(), "Not able to add friend in circle", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GroupAddFriendPost(String url) {
        Utility.freeMemory();
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
            jsonObject.accumulate("GroupIDs", selectedStrings1);
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
           /* dialog = new ProgressDialog(ConnectActivity.this);
            dialog.setMessage("Fetching Circles...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Fetching data" ;
            CustomProgressDialog(loading, ConnectActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            return GroupFetchPost(urls[0]);
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
            Utility.freeMemory();
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
                        activityConnect2Binding.tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        activityConnect2Binding.recyclerView.setVisibility(View.GONE);
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
                            activityConnect2Binding.tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            activityConnect2Binding.tvAddedGroupInfo.setVisibility(View.GONE);
                        }
                            /*GroupsInCardDetailAdapter groupsInCardDetailAdapter = new GroupsInCardDetailAdapter(CardDetail.this, img,name,desc);
                            groupListView.setAdapter(groupsInCardDetailAdapter);
                            groupsInCardDetailAdapter.notifyDataSetChanged();*/

                        GroupsRecyclerAdapter groupsRecyclerAdapter = new GroupsRecyclerAdapter(ConnectActivity.this, img, name, desc);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        activityConnect2Binding.recyclerView.setLayoutManager(new LinearLayoutManager(ConnectActivity.this, LinearLayoutManager.HORIZONTAL, true));
//                             recycler_view.setLayoutManager(mLayoutManager);
                        activityConnect2Binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                        activityConnect2Binding.recyclerView.setAdapter(groupsRecyclerAdapter);
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not able to fetch circles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public  String FetchGroupDataPost(String url)
    {
        Utility.freeMemory();
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
