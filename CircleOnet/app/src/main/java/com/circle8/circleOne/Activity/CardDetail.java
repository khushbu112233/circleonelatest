package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.EditGroupAdapter;
import com.circle8.circleOne.Adapter.GroupsInCardDetailAdapter;
import com.circle8.circleOne.Adapter.GroupsRecyclerAdapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.StickyScrollView;
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

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardDetail extends NfcActivity
{
    ViewPager mViewPager, viewPager1;
    private ArrayList<String> image = new ArrayList<>();
    private CardSwipe myPager;
    private ImageView imgCards, imgConnect, imgEvents, imgProfile, imgBack, imgCard, imgMap;
    private static final String TAG = NFCDemo.class.getName();
    private LinearLayout llWebsiteBox, llEmailBox, llMobileBox, llTeleBox, llFaxBox, llAddressBox;
    ImageView fbUrl, linkedInUrl, twitterUrl, googleUrl, youtubeUrl;
    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    ProgressDialog mProgressDialog;
    DatabaseHelper db;
    TextView txtName, txtCompany, txtWebsite, txtEmail, txtPH, txtWork, txtMob, txtAddress, txtRemark, txtDesi;
    CircleImageView imgProfileCard;
    String user_id = "", profile_id, currentUser_ProfileId = "";
    StickyScrollView scroll;
    ImageView imgCall, imgSMS, imgMail;
    String recycle_image1, recycle_image2;
    ImageView imgAddGroupFriend;
    public static JSONArray selectedStrings = new JSONArray();
    String userImg, frontCardImg, backCardImg, personName, personAddress;
    ImageView imgProfileShare;
    List<CharSequence> list;
    ArrayList<String> listGroupId;
    LoginSession loginSession;
    String strfbUrl = "", strlinkedInUrl = "", strtwitterUrl = "", strgoogleUrl = "", stryoutubeUrl = "";
    AppBarLayout appBarLayout;

    String FirstName = "", LastName = "", UserPhoto = "", Phone1 = "", Phone2 = "", Mobile1 = "", Mobile2 = "", Fax1 = "",
            Fax2 = "", Email1 = "", Email2 = "", IndustryName = "", CompanyName = "", CompanyProfile = "", Designation = "",
            ProfileDesc = "";

    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();
    ArrayList<String> ID_group = new ArrayList<>();
    ArrayList<String> groupDesc = new ArrayList<>();

    ListView listView1, groupListView;
    RecyclerView recycler_view ;
    TextView tvAddedGroupInfo ;
    private String displayProfile;

    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        currentUser_ProfileId = user.get(LoginSession.KEY_PROFILEID);
        imgProfileShare = (ImageView) findViewById(R.id.imgProfileShare);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) findViewById(R.id.viewPager1);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCard = (ImageView) findViewById(R.id.imgCard);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgSMS = (ImageView) findViewById(R.id.imgSMS);
        imgMail = (ImageView) findViewById(R.id.imgMail);
        imgMap = (ImageView) findViewById(R.id.ivMap);
        imgProfileCard = (CircleImageView) findViewById(R.id.imgProfileCard);
        db = new DatabaseHelper(getApplicationContext());
        txtName = (TextView) findViewById(R.id.txtName);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPH = (TextView) findViewById(R.id.txtPH);
        txtWork = (TextView) findViewById(R.id.txtWork);
        txtMob = (TextView) findViewById(R.id.txtMob);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtRemark = (TextView) findViewById(R.id.txtRemark);
        txtDesi = (TextView) findViewById(R.id.txtDesi);
        scroll = (StickyScrollView) findViewById(R.id.scroll);
        imgAddGroupFriend = (ImageView) findViewById(R.id.imgAddGroupFriend);
        llWebsiteBox = (LinearLayout) findViewById(R.id.llWebsiteBox);
        llEmailBox = (LinearLayout) findViewById(R.id.llEmailBox);
        llMobileBox = (LinearLayout) findViewById(R.id.llMobileBox);
        llTeleBox = (LinearLayout) findViewById(R.id.llTeleBox);
        llFaxBox = (LinearLayout) findViewById(R.id.llFaxBox);
        llAddressBox = (LinearLayout) findViewById(R.id.llAddressBox);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        fbUrl = (ImageView) findViewById(R.id.fbUrl);
        googleUrl = (ImageView) findViewById(R.id.googleUrl);
        youtubeUrl = (ImageView) findViewById(R.id.youtubeUrl);
        twitterUrl = (ImageView) findViewById(R.id.twitterUrl);
        linkedInUrl = (ImageView) findViewById(R.id.linkedInUrl);
        groupListView = (ListView)findViewById(R.id.groupListView);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        tvAddedGroupInfo = (TextView)findViewById(R.id.tvAddedGroupInfo);

        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        list = new ArrayList<CharSequence>();
        listGroupId = new ArrayList<String>();

        Intent intent = getIntent();
        profile_id = intent.getStringExtra("profile_id");

        if (profile_id.equals(""))
        {
            Toast.makeText(CardDetail.this, "Having no profile ID",Toast.LENGTH_LONG).show();
        }
        else
        {
            new CardDetail.HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetUserProfile");
        }

        new HttpAsyncTaskGroup().execute("http://circle8.asia:8999/Onet.svc/Group/Fetch");

        new HttpAsyncTaskGroupsFetch().execute("http://circle8.asia:8999/Onet.svc/Group/MyGroupsTaggedWithFriendProfile");

        imgProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "I'm giving you a free redemption points on the Circle app (up to ₹25). To accept, use code '"+ LoginActivity.ReferrenceCode+"' to sign up. Enjoy!"
                        +System.lineSeparator() + "Details: https://www.circle8.asia/invite/"+LoginActivity.ReferrenceCode;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, txtName.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });

        imgProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(CardDetail.this);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (displayProfile.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+displayProfile).placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ImageZoom.class);
                        intent.putExtra("displayProfile", "http://circle8.asia/App_ImgLib/UserProfile/"+displayProfile);
                        startActivity(intent);
                    }
                });

               /* WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                wmlp.y = 300;*/   //y position
                dialog.show();
            }
        });

        imgAddGroupFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(CardDetail.this);
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
                    EditGroupAdapter editGroupAdapter = new EditGroupAdapter(CardDetail.this, groupName, groupPhoto, listGroupId);
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

        fbUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strfbUrl != null) {
                    if (!strfbUrl.startsWith("http://") && !strfbUrl.startsWith("https://"))
                        strfbUrl = "http://" + strfbUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strfbUrl));
                    startActivity(browserIntent);
                }
            }
        });

        googleUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strgoogleUrl != null) {
                    if (!strgoogleUrl.startsWith("http://") && !strgoogleUrl.startsWith("https://"))
                        strgoogleUrl = "http://" + strgoogleUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strgoogleUrl));
                    startActivity(browserIntent);
                }
            }
        });

        youtubeUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stryoutubeUrl != null) {
                    if (!stryoutubeUrl.startsWith("http://") && !stryoutubeUrl.startsWith("https://"))
                        stryoutubeUrl = "http://" + stryoutubeUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stryoutubeUrl));
                    startActivity(browserIntent);
                }
            }
        });

        twitterUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strtwitterUrl != null) {
                    if (!strtwitterUrl.startsWith("http://") && !strtwitterUrl.startsWith("https://"))
                        strtwitterUrl = "http://" + strtwitterUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strtwitterUrl));
                    startActivity(browserIntent);
                }
            }
        });

        linkedInUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strlinkedInUrl != null) {
                    if (!strlinkedInUrl.startsWith("http://") && !strlinkedInUrl.startsWith("https://"))
                        strlinkedInUrl = "http://" + strlinkedInUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strlinkedInUrl));
                    startActivity(browserIntent);
                }
            }
        });


        llWebsiteBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Redirect to Web Browser")
                        .setMessage("Are you sure you want to redirect to Web Browser ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String url = txtWebsite.getText().toString();
                                if (url != null) {
                                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                                        url = "http://" + url;
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_set_as)
                        .show();
            }
        });

        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().equals("")) {

                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);
                    builder.setTitle("Mail to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + txtEmail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }
        });

        imgSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = Utility.checkSMSPermission(CardDetail.this);
                if (result)
                {
                    if (txtMob.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "You are not having contact to SMS..", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", txtMob.getText().toString());
                        smsIntent.putExtra("sms_body", "");
                        startActivity(smsIntent);
                    }
                }
            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtMob.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtMob.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                } else if (!txtWork.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtWork.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                } else if (!txtPH.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);

                    builder.setTitle("Call to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + txtPH.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                }
            }
        });

        llEmailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().equals("")) {

                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this);
                    builder.setTitle("Mail to " + txtName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + txtEmail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }
        });

        llMobileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Call to " + txtName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + txtMob.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });

        llTeleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Call to " + txtName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + txtPH.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });

        llFaxBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        Toast.makeText(getApplicationContext(),"Profile_id: "+profile_id,Toast.LENGTH_SHORT).show();
//        final List<NFCModel> modelList = db.getNFCbyTag(tag_id);

       /* try
        {
            if (modelList != null)
            {
                for (NFCModel tag1 : modelList)
                {
                    // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                    //Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                    imgCard.setImageResource(tag1.getCard_front());

                  //  Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                    imgProfileCard.setImageResource(tag1.getUser_image());
                    txtName.setText(tag1.getName());
                    txtCompany.setText(tag1.getCompany());
                    txtWebsite.setText(tag1.getWebsite());
                    txtEmail.setText(tag1.getEmail());
                    txtPH.setText(tag1.getPh_no());
                    txtWork.setText(tag1.getWork_no());
                    txtMob.setText(tag1.getMob_no());
                    txtAddress.setText(tag1.getAddress());
                    txtRemark.setText(tag1.getAddress());
                    txtDesi.setText(tag1.getDesignation());
                    image.add(tag1.getCard_front());
                    image.add(tag1.getCard_back());

                    myPager = new CardSwipe(getApplicationContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(3);
                    //mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(3);
                   // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                    viewPager1.setAdapter(myPager);

                }
            }

        }catch (Exception e){

        }*/


      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                scroll.scrollTo(0, mViewPager.getBottom());
            }
        });*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent go = new Intent(getApplicationContext(), CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);*/
                finish();
            }
        });

        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager.scrollTo(viewPager1.getScrollX(), viewPager1.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(viewPager1.getCurrentItem(), false);
                }
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

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
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);
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
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

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
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this);

                builder.setTitle("Google Map")
                        .setMessage("Are you sure you want to redirect to Google Map ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String map = "http://maps.google.co.in/maps?q=" + txtAddress.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        });
    }

    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = listView1.getCheckedItemPositions();
        int itemCount = listView1.getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }

    public String POST4(String url) {
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

    public String POST5(String url) {
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
            jsonObject.accumulate("ProfileId", profile_id);
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
            /*dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching Circles...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Fetching Circles" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST4(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

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

    private class HttpAsyncTaskGroupAddFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Adding Friend...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Adding Friend" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST5(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

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
                    Toast.makeText(getApplicationContext(), "Not able to Add Friend in circle", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Fetching Cards" ;
            CustomProgressDialog(loading);
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
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    FirstName = jsonObject.getString("FirstName");
                    LastName = jsonObject.getString("LastName");
                    userImg = jsonObject.getString("UserPhoto");
                    Phone1 = jsonObject.getString("Phone1");
                    Phone2 = jsonObject.getString("Phone2");
                    Mobile1 = jsonObject.getString("Mobile1");
                    Mobile2 = jsonObject.getString("Mobile2");
                    Fax1 = jsonObject.getString("Fax1");
                    Fax2 = jsonObject.getString("Fax2");
                    Email1 = jsonObject.getString("Email1");
                    Email2 = jsonObject.getString("Email2");
                    stryoutubeUrl = jsonObject.getString("Youtube");
                    IndustryName = jsonObject.getString("IndustryName");
                    CompanyName = jsonObject.getString("CompanyName");
                    CompanyProfile = jsonObject.getString("CompanyProfile");
                    Designation = jsonObject.getString("Designation");
                    ProfileDesc = jsonObject.getString("ProfileDesc");
                    strlinkedInUrl = jsonObject.getString("LinkedIn");
                    strfbUrl = jsonObject.getString("Facebook");
                    strtwitterUrl = jsonObject.getString("Twitter");
                    stryoutubeUrl = jsonObject.getString("Youtube");
                    strgoogleUrl = jsonObject.getString("Google");
                    frontCardImg = jsonObject.getString("Card_Front");
                    backCardImg = jsonObject.getString("Card_Back");

                    if (strfbUrl.equals("") || strfbUrl.equals(null))
                    {
                        fbUrl.setImageResource(R.drawable.ic_fb_gray);
                        fbUrl.setEnabled(false);
                    }
                    else {
                        fbUrl.setImageResource(R.drawable.icon_fb);
                        fbUrl.setEnabled(true);
                    }

                    if (stryoutubeUrl.equals("") || stryoutubeUrl.equals(null))
                    {
                        youtubeUrl.setImageResource(R.drawable.icon_utube_gray);
                        youtubeUrl.setEnabled(false);
                    }
                    else {
                        youtubeUrl.setImageResource(R.drawable.icon_utube_red);
                        youtubeUrl.setEnabled(true);
                    }

                    if (strgoogleUrl.equals("") || strgoogleUrl.equals(null))
                    {
                        googleUrl.setImageResource(R.drawable.ic_google_gray);
                        googleUrl.setEnabled(false);
                    }
                    else {
                        googleUrl.setImageResource(R.drawable.icon_google);
                        googleUrl.setEnabled(true);
                    }

                    if (strtwitterUrl.equals("") || strtwitterUrl.equals(null))
                    {
                        twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                        twitterUrl.setEnabled(false);
                    }
                    else {
                        twitterUrl.setImageResource(R.drawable.icon_twitter);
                        twitterUrl.setEnabled(true);
                    }

                    if (stryoutubeUrl.equals("") || stryoutubeUrl.equals(null))
                    {
                        youtubeUrl.setImageResource(R.drawable.icon_utube_gray);
                        youtubeUrl.setEnabled(false);
                    }
                    else {
                        youtubeUrl.setImageResource(R.drawable.icon_utube_red);
                        youtubeUrl.setEnabled(true);
                    }


                    if (strlinkedInUrl.equals("") || strlinkedInUrl.equals(null))
                    {
                        linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                        linkedInUrl.setEnabled(false);
                    }
                    else {
                        linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                        linkedInUrl.setEnabled(true);
                    }


//                        txtName.setText(jsonObject.getString("FirstName")+" "+jsonObject.getString("LastName"));
//                        txtDesi.setText(jsonObject.getString("Designation"));
//                        txtCompany.setText(jsonObject.getString("CompanyName"));
//                        txtEmail.setText(jsonObject.getString("Emailid"));
//                        txtMob.setText(jsonObject.getString("PrimaryPhone"));
//                        txtPH.setText(jsonObject.getString("OfficePhone"));
                        /*txtAddress.setText(jsonObject.getString("Address1") + " " + jsonObject.getString("Address2")
                            + " " + jsonObject.getString("Address3") + " " + jsonObject.getString("Address4")
                            + " " + jsonObject.getString("City")  + " " + jsonObject.getString("State")
                            + " " + jsonObject.getString("Country") + " " + jsonObject.getString("Postalcode"));*/
//                    txtWebsite.setText(jsonObject.getString("Website"));

                    personName = jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName");
                    personAddress = jsonObject.getString("Address1") + " " + jsonObject.getString("Address2")
                            + " " + jsonObject.getString("Address3") + " " + jsonObject.getString("Address4")
                            + " " + jsonObject.getString("City") + " " + jsonObject.getString("State")
                            + " " + jsonObject.getString("Country") + " " + jsonObject.getString("Postalcode");

                    txtRemark.setText(personAddress);
                    if (personName.equalsIgnoreCase("") || personName.equalsIgnoreCase(null)) {
                        txtName.setText("Person");
                    } else {
                        txtName.setText(personName);
                    }

                    if (Designation.equalsIgnoreCase("")
                            || Designation.equalsIgnoreCase(null)) {
                        txtDesi.setText("Designation");
                        txtDesi.setVisibility(View.GONE);
                    } else {
                        txtDesi.setText(Designation);
                    }

                    if (CompanyName.equalsIgnoreCase("")
                            || CompanyName.equalsIgnoreCase(null)) {
                        txtCompany.setText("Company");
                        txtCompany.setVisibility(View.GONE);
                    } else {
                        txtCompany.setText(CompanyName);
                    }

                    if (jsonObject.getString("Website").equalsIgnoreCase("")
                            || jsonObject.getString("Website").equalsIgnoreCase(null)) {
                        txtWebsite.setText("Website");
                        llWebsiteBox.setVisibility(View.GONE);
                    } else {
                        txtWebsite.setText(jsonObject.getString("Website"));
                    }

                    if (Email1.equalsIgnoreCase("")
                            || Email1.equalsIgnoreCase(null)) {
                        txtEmail.setText("Email Address");
                        llEmailBox.setVisibility(View.GONE);
                    } else {
                        txtEmail.setText(Email1);
                    }

                    if (Phone1.equalsIgnoreCase("")
                            || Phone1.equalsIgnoreCase(null)) {
                        txtPH.setText("Phone No.");
                        llTeleBox.setVisibility(View.GONE);
                    } else {
//                        Phone1 = Phone1.trim();
                        Phone1 = Phone1.replaceAll("\\s+", "");
                        /*int number = Integer.parseInt(Phone1);
//                        Phone1 = Phone1.replaceAll("\\s++$", "");
                        String number1 = String.valueOf(number);
                        txtPH.setText(String.valueOf(number));*/
                        txtPH.setText(Phone1);
                    }

                    if (Mobile1.equalsIgnoreCase("")
                            || Mobile1.equalsIgnoreCase(null)) {
                        txtMob.setText("Mobile No.");
                        llMobileBox.setVisibility(View.GONE);
                    } else {
                        Mobile1.trim();
                        txtMob.setText(Mobile1);
                    }

                    if (personAddress.startsWith(" ")
                            || personAddress.equalsIgnoreCase(null)
                            || personAddress.equalsIgnoreCase("")) {
                        txtAddress.setText("Address");
                        llAddressBox.setVisibility(View.GONE);
                    } else {
                        txtAddress.setText(personAddress);
                    }

                    if (Fax1.equalsIgnoreCase("")
                            || Fax1.equalsIgnoreCase(null)) {
                        txtWork.setText("Fax");
                        llFaxBox.setVisibility(View.GONE);
                    } else {
                        Fax1.trim();
                        txtWork.setText(Fax1);
                    }

                    if (userImg.equalsIgnoreCase("")) {
                        imgProfileCard.setImageResource(R.drawable.usr);
                        displayProfile = "";
                    } else {
                        Picasso.with(CardDetail.this).load("http://circle8.asia/App_ImgLib/UserProfile/" + userImg).into(imgProfileCard);
                        displayProfile = userImg;
                    }

                    if (frontCardImg.equalsIgnoreCase("") || backCardImg.equalsIgnoreCase("")) {
                        appBarLayout.setVisibility(View.GONE);
                    } else {
                        appBarLayout.setVisibility(View.VISIBLE);
                    }

                    if (frontCardImg.equalsIgnoreCase("")) {
                        recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    } else {
                        recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/" + frontCardImg;
                    }

                    if (backCardImg.equalsIgnoreCase("")) {
                        recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    } else {
                        recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/" + backCardImg;
                    }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getApplicationContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(3);
                    //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(3);
                    //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    viewPager1.setAdapter(myPager);

                        /*FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));

                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);*/
//                        GetData(getContext());

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url) {
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
            jsonObject.accumulate("profileid", profile_id);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);


        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
        {
//            Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
            //textViewInfo.setText("tag == null");
        }
        else
        {
            String tagInfo = tag.toString() + "\n";
            String id = "";
            tagInfo += "\nTag Id: \n";
            byte[] tagId = tag.getId();
            tagInfo += "length = " + tagId.length + "\n";
            for (int i = 0; i < tagId.length; i++) {
                tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                // id += Integer.toHexString(tagId[i] & 0xFF) + " ";
            }
            id = bytesToHex(tagId);
               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


            //  Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            // callData(id);
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values())
            {
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                List<NFCModel> modelList = db.getNFCbyTag(data);

                try {

                    if (modelList != null) {

                        for (NFCModel tag1 : modelList) {
                            // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                            // Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                            imgCard.setImageResource(tag1.getCard_front());

                            // Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                            imgProfileCard.setImageResource(tag1.getUser_image());
                            txtName.setText(tag1.getName());
                            txtCompany.setText(tag1.getCompany());
                            txtWebsite.setText(tag1.getWebsite());
                            txtEmail.setText(tag1.getEmail());
                            txtPH.setText(tag1.getPh_no());
                            txtWork.setText(tag1.getWork_no());
                            txtMob.setText(tag1.getMob_no());
                            txtAddress.setText(tag1.getAddress());
                            txtRemark.setText(tag1.getRemark());
                            txtDesi.setText(tag1.getDesignation());
                            image.add(String.valueOf(tag1.getCard_front()));   // its change from integer to string
                            image.add(String.valueOf(tag1.getCard_back()));    // its change from integer to string
                            myPager = new CardSwipe(getApplicationContext(), image);

                            mViewPager.setClipChildren(false);
                            mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                            mViewPager.setOffscreenPageLimit(3);
                            //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

                            viewPager1.setAdapter(myPager);

                            viewPager1.setClipChildren(false);
                            viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                            viewPager1.setOffscreenPageLimit(3);
                            //    viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                            viewPager1.setAdapter(myPager);

                        }
                    }

                } catch (Exception e) {

                }
            }
        }
    }

    public void callData(String id) {

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

                            GroupsRecyclerAdapter groupsRecyclerAdapter = new GroupsRecyclerAdapter(CardDetail.this, img, name, desc);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recycler_view.setLayoutManager(new LinearLayoutManager(CardDetail.this, LinearLayoutManager.HORIZONTAL, true));
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
            jsonObject.accumulate("ProfileId", profile_id);
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

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
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


}
