package com.circle8.circleOne.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.EditProfileActivity;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Activity.LoginActivity;
import com.circle8.circleOne.Activity.MyAccountActivity;
import com.circle8.circleOne.Activity.SearchGroupMembers;
import com.circle8.circleOne.Activity.TestimonialActivity;
import com.circle8.circleOne.Activity.TestimonialRequest;
import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CustomAdapter;
import com.circle8.circleOne.Adapter.TextRecyclerAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Exchanger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
{
   // private ProgressBar firstBar = null;
    ImageView imgProfileShare, imgProfileMenu, imgQR, ivEditProfile;
    TextView tvPersonName, tvProfileName ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    ProgressDialog progressDialog ;
    ArrayList<String> profile_array, NameArray, DesignationArray;
    private LoginButton loginButton;
    private LoginSession session;
    private String UserID = "";
    ImageView imgBack, imgAdd;

    public static ArrayList<ProfileModel> allTags ;
    JSONArray array, arrayEvents;
    List<String> listAssociation, listEvents;
    String profileId = "";
    TextView txtNoAssociation, txtNoEvent;
    private int i = 0;
    TextView tvDesignation, tvCompany, tvName, tvCompanyName, tvDesi,
            tvAssociation, tvAddress, tvWebsite, tvMail, tvMob, tvWork, textIndustry;
    ProfileModel nfcModelTag;
    CircleImageView imgProfile;
    LinearLayout lnrMob, lnrWork, lnrWebsite, lnrMap, llNameBox, llCompanyBox,
                 llIndustryBox, llDesignationBox, llAssociationBox , llMailBox;
    ViewPager mViewPager, viewPager1;
    String recycle_image1, recycle_image2 ;
    private ArrayList<String> image = new ArrayList<>();
    private CardSwipe myPager ;
    public static ArrayList<TestimonialModel> allTaggs ;
    String TestimonialProfileId = "";
    ExpandableHeightListView lstTestimonial;
    TextView txtTestimonial, txtMore;
    CustomAdapter customAdapter;
    ImageView fbUrl, linkedInUrl, twitterUrl, googleUrl, youtubeUrl;
    String strfbUrl, strlinkedInUrl, strtwitterUrl, strgoogleUrl, stryoutubeUrl;
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    String Address1 = "", Address2 = "", Address3 = "", Address4 = "", City = "", State = "", Country = "", Postalcode = "", Website = "", Attachment_FileName = "";
    String personName , personAddress ;
    private CallbackManager callbackManager;
    View view;
    AppBarLayout appbar;
    private String displayProfile ;
    private String secretKey = "1234567890234561";
    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    RecyclerView recyclerAssociation, recyclerEvents;
    String barName;
    JSONArray jsonArray;
    public static int profileIndex = 0;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
     //   getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
      //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        callbackManager = CallbackManager.Factory.create();
        llNameBox = (LinearLayout)view.findViewById(R.id.llNameBox);
        llCompanyBox = (LinearLayout)view.findViewById(R.id.llCompanyBox);
        llIndustryBox = (LinearLayout)view.findViewById(R.id.llIndustryBox);
        llDesignationBox = (LinearLayout)view.findViewById(R.id.llDesignationBox);
        llAssociationBox = (LinearLayout)view.findViewById(R.id.llAssociationBox);
        llMailBox = (LinearLayout)view.findViewById(R.id.llMailBox);
        fbUrl = (ImageView) view.findViewById(R.id.fbUrl);
        googleUrl = (ImageView) view.findViewById(R.id.googleUrl);
        youtubeUrl = (ImageView) view.findViewById(R.id.youtubeUrl);
        twitterUrl = (ImageView) view.findViewById(R.id.twitterUrl);
        linkedInUrl = (ImageView) view.findViewById(R.id.linkedInUrl);
        tvDesignation = (TextView)view.findViewById(R.id.tvDesignation);
        tvCompany = (TextView)view.findViewById(R.id.tvCompany);
        tvName = (TextView)view.findViewById(R.id.tvName);
        tvCompanyName = (TextView)view.findViewById(R.id.tvCompanyName);
        tvDesi = (TextView)view.findViewById(R.id.tvDesi);
        tvAssociation = (TextView)view.findViewById(R.id.tvAssociation);
        textIndustry = (TextView)view.findViewById(R.id.textIndustry);
        tvAddress = (TextView)view.findViewById(R.id.tvAddress);
        tvWebsite = (TextView)view.findViewById(R.id.tvWebsite);
        tvMail = (TextView)view.findViewById(R.id.tvMail);
        tvMob = (TextView)view.findViewById(R.id.tvMob);
        tvWork = (TextView)view.findViewById(R.id.tvWork);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        lnrMob = (LinearLayout) view.findViewById(R.id.lnrMob);
        lnrWork = (LinearLayout) view.findViewById(R.id.lnrWork);
        lnrWebsite = (LinearLayout) view.findViewById(R.id.lnrWebsite);
        lnrMap = (LinearLayout) view.findViewById(R.id.lnrMap);
        txtNoEvent = (TextView) view.findViewById(R.id.txtNoEvent);
        txtNoAssociation = (TextView) view.findViewById(R.id.txtNoAssociation);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) view.findViewById(R.id.viewPager1);
        lstTestimonial = (ExpandableHeightListView) view.findViewById(R.id.lstTestimonial);
        txtTestimonial = (TextView) view.findViewById(R.id.txtTestimonial);
        txtMore = (TextView) view.findViewById(R.id.txtMore);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        recyclerAssociation = (RecyclerView) view.findViewById(R.id.recyclerAssociation);
        recyclerEvents = (RecyclerView) view.findViewById(R.id.recyclerEvents);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Generating Qr Code...");
        progressDialog.setCancelable(false);
        allTags = new ArrayList<>();
        listAssociation = new ArrayList<>();
        imgQR = (ImageView) view.findViewById(R.id.imgQR);
        imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
      //  firstBar = (ProgressBar)view.findViewById(R.id.firstBar);
        tvPersonName = (TextView)view.findViewById(R.id.tvPersonName);
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        imgProfileShare = (ImageView) view.findViewById(R.id.imgProfileShare);
        imgProfileMenu = (ImageView) view.findViewById(R.id.imgProfileMenu);
        ivEditProfile = (ImageView)view.findViewById(R.id.ivEditProfile);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        session = new LoginSession(getContext());
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);

        rlProgressDialog = (RelativeLayout)view.findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)view.findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)view.findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)view.findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)view.findViewById(R.id.imgConnecting3) ;

//        new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/GetUserProfile");
        new HttpAsyncTaskProfiles().execute("http://circle8.asia:8999/Onet.svc/MyProfiles");

        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        profileId = user.get(LoginSession.KEY_PROFILEID);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (displayProfile.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_white1);
                }
                else
                {
                    Picasso.with(getActivity()).load("http://circle8.asia/App_ImgLib/UserProfile/"+displayProfile).placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), ImageZoom.class);
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

        fbUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strfbUrl!=null) {
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
                if (strgoogleUrl!=null) {
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
                if (stryoutubeUrl!=null) {
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
                if (strtwitterUrl!=null) {
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
                if (strlinkedInUrl!=null) {
                    if (!strlinkedInUrl.startsWith("http://") && !strlinkedInUrl.startsWith("https://"))
                        strlinkedInUrl = "http://" + strlinkedInUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strlinkedInUrl));
                    startActivity(browserIntent);
                }
            }
        });

        allTaggs = new ArrayList<>();
        lnrMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Google Map")
                        .setMessage("Are you sure you want to redirect to Google Map ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String map = "http://maps.google.co.in/maps?q=" + tvAddress.getText().toString();
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

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), TestimonialActivity.class);
                intent.putExtra("ProfileId", TestimonialProfileId);
                intent.putExtra("from", "profile");
                startActivity(intent);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getContext(), SearchGroupMembers.class);
                intent1.putExtra("from", "profile");
                intent1.putExtra("ProfileId", TestimonialProfileId);
                startActivity(intent1);

//
//                Intent intent = new Intent(getContext(), TestimonialRequest.class);
//                intent.putExtra("ProfileId", TestimonialProfileId);
//                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                getActivity().finish();
            }
        });

        lnrMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Call to "+ tvPersonName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+tvMob.getText().toString()));
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


        lnrWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Redirect to Web Browser")
                        .setMessage("Are you sure you want to redirect to Web Browser ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String url = tvWebsite.getText().toString();
                                if (url!=null) {
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

        lnrWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Call to "+ tvPersonName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+tvWork.getText().toString()));
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

        tvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvMail.getText().toString().equals(""))
                {

                }
                else
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Mail to "+ tvPersonName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try
                                    {
                                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + tvMail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(getContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
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
        imgProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "I'm giving you a free redemption points on the Circle app (up to ₹25). To accept, use code '"+ LoginActivity.ReferrenceCode+"' to sign up. Enjoy!"
                        +System.lineSeparator() + "Details: https://www.circle8.asia/invite/"+LoginActivity.ReferrenceCode;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tvPersonName.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });

        imgProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), imgProfileMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile_popup_menu, popup.getMenu());
                for (String s : profile_array) {
                    popup.getMenu().add(s);
                }
                popup.getMenu().add("Add New Profile");
                popup.getMenu().add("Close");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


                        if (item.getTitle().toString().equals("Add New Profile"))
                        {
                           // new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8999/Onet.svc/AddProfile");
                            Intent intent = new Intent(getContext(), EditProfileActivity.class);
                            intent.putExtra("profile_id", TestimonialProfileId);
                            intent.putExtra("type", "add");
                            intent.putExtra("activity", "profile");
                            startActivity(intent);
                        }
                        else
                        {
                            for (int i = 0; i < profile_array.size(); i++)
                            {
                                if (item.getTitle().toString().equals(profile_array.get(i).toString()))
                                {
                                    profileIndex = i;
//                                    Toast.makeText(getContext(), profile_array.get(i).toString(), Toast.LENGTH_LONG).show();
                                    tvProfileName.setText(allTags.get(i).getProfile());
                                    tvPersonName.setText(allTags.get(i).getFirstName() + " "+ allTags.get(i).getLastName());
                                    tvDesignation.setText(allTags.get(i).getDesignation());
                                    tvCompany.setText(allTags.get(i).getCompanyName());
                                    tvName.setText(allTags.get(i).getFirstName() + " "+ allTags.get(i).getLastName());
                                    tvCompanyName.setText(allTags.get(i).getCompanyName());
                                    tvDesi.setText(allTags.get(i).getDesignation());
                                    tvMob.setText(allTags.get(i).getMobile1());
                                    tvWork.setText(allTags.get(i).getPhone1());

                                    tvAddress.setText(
                                              allTags.get(i).getAddress1()+ " "
                                            + allTags.get(i).getAddress2() + "\n"
                                            + allTags.get(i).getAddress3()  + " "
                                            + allTags.get(i).getAddress4() + "\n"
                                            + allTags.get(i).getCity() + " "
                                            + allTags.get(i).getState() + "\n"
                                            + allTags.get(i).getCountry() + " "
                                            + allTags.get(i).getPostalcode());
                                    tvWebsite.setText(allTags.get(i).getWebsite());
                                    TestimonialProfileId = allTags.get(i).getProfileID();

                                    try {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        try {
                                            array = object.getJSONArray("Association_Name");
                                            listAssociation = new ArrayList<String>();
                                            for (int i1 = 0; i1 < array.length(); i1++) {

                                                listAssociation.add(array.getString(i1));

                                            }

                                            int countAssociation;
                                            if (array.length()>=5){
                                                countAssociation = 5;
                                            }else {
                                                countAssociation = array.length();
                                            }
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), countAssociation, GridLayoutManager.HORIZONTAL, false);
                                            recyclerAssociation.setAdapter(new TextRecyclerAdapter(listAssociation));
                                            recyclerAssociation.setLayoutManager(gridLayoutManager);

                                            if (listAssociation.size() == 0){
                                                recyclerAssociation.setVisibility(View.GONE);
                                                txtNoAssociation.setVisibility(View.VISIBLE);
                                            }
                                            else {
                                                recyclerAssociation.setVisibility(View.VISIBLE);
                                                txtNoAssociation.setVisibility(View.GONE);
                                            }
                                        }catch (Exception e){}

                                        try {
                                            arrayEvents = object.getJSONArray("Event_Cat_Name");

                                            listEvents = new ArrayList<String>();

                                            for (int i1 = 0; i1 < arrayEvents.length(); i1++) {

                                                listEvents.add(arrayEvents.getString(i1));

                                            }

                                            int countEvents;
                                            if (arrayEvents.length() >= 5) {
                                                countEvents = 5;
                                            } else {
                                                countEvents = arrayEvents.length();
                                            }

                                            GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), countEvents, GridLayoutManager.HORIZONTAL, false);
                                            recyclerEvents.setAdapter(new TextRecyclerAdapter(listEvents));
                                            recyclerEvents.setLayoutManager(gridLayoutManager1);


                                            if (listEvents.size() == 0) {
                                                recyclerEvents.setVisibility(View.GONE);
                                                txtNoEvent.setVisibility(View.VISIBLE);
                                            } else {
                                                recyclerEvents.setVisibility(View.VISIBLE);
                                                txtNoEvent.setVisibility(View.GONE);
                                            }

                                        }catch (Exception e){}
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    if (allTags.get(i).getUserPhoto().equals(""))
                                    {
                                        imgProfile.setImageResource(R.drawable.usr_white1);
                                    }
                                    else {
                                        Picasso.with(getContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+allTags.get(i).getUserPhoto()).into(imgProfile);
                                    }

                                    if (allTags.get(i).getCard_Front().equalsIgnoreCase("") && allTags.get(i).getCard_Back().equalsIgnoreCase("")) {
                                        appbar.setVisibility(View.GONE);
                                    } else {
                                        appbar.setVisibility(View.VISIBLE);
                                    }

                                    if (allTags.get(i).getFacebook().equals("") || allTags.get(i).getFacebook().equals(null))
                                    {
                                        fbUrl.setImageResource(R.drawable.ic_fb_gray);
                                        fbUrl.setEnabled(false);
                                    }
                                    else {
                                        fbUrl.setImageResource(R.drawable.icon_fb);
                                        fbUrl.setEnabled(true);
                                        strfbUrl = allTags.get(i).getFacebook().toString();
                                    }

                                    if (allTags.get(i).getGoogle().equals("") || allTags.get(i).getGoogle().equals(null))
                                    {
                                        googleUrl.setImageResource(R.drawable.ic_google_gray);
                                        googleUrl.setEnabled(false);
                                    }
                                    else {
                                        googleUrl.setImageResource(R.drawable.icon_google);
                                        googleUrl.setEnabled(true);
                                        strgoogleUrl = allTags.get(i).getGoogle().toString();
                                    }

                                    if (allTags.get(i).getTwitter().equals("") || allTags.get(i).getTwitter().equals(null))
                                    {
                                        twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                                        twitterUrl.setEnabled(false);
                                    }
                                    else {
                                        twitterUrl.setImageResource(R.drawable.icon_twitter);
                                        twitterUrl.setEnabled(true);
                                        strtwitterUrl = allTags.get(i).getTwitter().toString();
                                    }

                                    if (allTags.get(i).getLinkedin().equals("") || allTags.get(i).getLinkedin().equals(null))
                                    {
                                        linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                                        linkedInUrl.setEnabled(false);
                                    }
                                    else {
                                        linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                                        linkedInUrl.setEnabled(true);
                                        strlinkedInUrl = allTags.get(i).getLinkedin().toString();
                                    }

                                    new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8999/Onet.svc/Testimonial/Fetch");

                                    try
                                    {
                                        if(allTags.get(i).getCard_Front().equals(""))
                                        {
                                            recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                        }
                                        else
                                        {
                                            recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(i).getCard_Front();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                    }

                                    try
                                    {
                                        if(allTags.get(i).getCard_Back().equals(""))
                                        {
                                            recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                        }
                                        else
                                        {
                                            recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(i).getCard_Back();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                    }

                                    image = new ArrayList<>();
                                    image.add(recycle_image1);
                                    image.add(recycle_image2);
                                    myPager = new CardSwipe(getContext(), image);

                                    mViewPager.setClipChildren(false);
                                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                                    mViewPager.setOffscreenPageLimit(1);
                                  //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                                    mViewPager.setAdapter(myPager);

                                    viewPager1.setClipChildren(false);
                                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                                    viewPager1.setOffscreenPageLimit(1);
                                   // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                                    viewPager1.setAdapter(myPager);
                                    try
                                    {
                                        barName = encrypt(TestimonialProfileId, secretKey);
                                        bitmap = TextToImageEncode(barName);
                                    }
                                    catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (InvalidKeyException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchPaddingException e) {
                                        e.printStackTrace();
                                    } catch (InvalidAlgorithmParameterException e) {
                                        e.printStackTrace();
                                    } catch (IllegalBlockSizeException e) {
                                        e.printStackTrace();
                                    } catch (BadPaddingException e) {
                                        e.printStackTrace();
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        return true;
                    }
                });

                popup.show();//showing popup men
        }
        });

        imgQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              //  Toast.makeText(getContext(), "Generating QR Code.. Please Wait..", Toast.LENGTH_LONG).show();

                //                    barName = encrypt(TestimonialProfileId, secretKey);


                // for another way to get qr code image using zxing
                /*MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(barName, BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);*/

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
//                tvBarName.setText(barName);
                tvBarName.setText(tvPersonName.getText().toString());
//                    bitmap = TextToImageEncode(barName);
                ivBarImage.setImageBitmap(bitmap);
                alertDialog.setView(dialogView);
                alertDialog.show();

//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Generating Qr Code...");
//                progressDialog.setCancelable(false);

               /* try
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                    ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                    tvBarName.setText(barName);
                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(bitmap);
                    alertDialog.setView(dialogView);
                    progressDialog.dismiss();
                    alertDialog.show();
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }*/

            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getContext(),"Edit Profile",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("profile_id", TestimonialProfileId);
                intent.putExtra("activity", "profile");
                startActivity(intent);
            }
        });

       /* // for bar code generating
        try
        {
            barName = encrypt(TestimonialProfileId, secretKey);
            bitmap = TextToImageEncode(barName);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }*/

        return view;
    }

   /* @Override
    public void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email");

        fbUrl = (ImageView) view.findViewById(R.id.fbUrl);
        fbUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }*/

    public String encrypt(String value, String key)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] value_bytes = value.getBytes("UTF-8");
        byte[] key_bytes = getKeyBytes(key);
        return Base64.encodeToString(encrypt(value_bytes, key_bytes, key_bytes), 0);
    }

    public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        // setup AES cipher in CBC mode with PKCS #5 padding
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // encrypt
        localCipher.init(1, new SecretKeySpec(paramArrayOfByte2, "AES"), new IvParameterSpec(paramArrayOfByte3));
        return localCipher.doFinal(paramArrayOfByte1);
    }

    private byte[] getKeyBytes(String paramString)
            throws UnsupportedEncodingException
    {
        byte[] arrayOfByte1 = new byte[16];
        byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
        return arrayOfByte1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            // Toast.makeText(AccountActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                String Facebook_url = "https://www.facebook.com/profile.php?id="+object.getString("id").toString();
                                Toast.makeText(getContext(), Facebook_url, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


    public  String POST5(String url)
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
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("userid", UserID);

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

    private class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Profiles...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Fetching Profile" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
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

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("Profiles");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    profile_array = new ArrayList<String>();
                    NameArray = new ArrayList<>();
                    DesignationArray = new ArrayList<>();
                    listAssociation = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        profile_array.add(object.getString("ProfileName")+ " " + object.getString("CompanyName")+ " " + object.getString("FirstName") + " " + object.getString("LastName")
                        + " " + object.getString("Designation") );
                        NameArray.add(object.getString("FirstName") + " " + object.getString("LastName"));
                        DesignationArray.add(object.getString("Designation"));

                        nfcModelTag = new ProfileModel();
                        nfcModelTag.setUserID(object.getString("UserID"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setUserName(object.getString("UserName"));
                        nfcModelTag.setProfileID(object.getString("ProfileID"));
                        nfcModelTag.setCard_Front(object.getString("Card_Front"));
                        nfcModelTag.setCard_Back(object.getString("Card_Back"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCompanyName(object.getString("CompanyName"));
                        nfcModelTag.setCompany_Profile(object.getString("Company_Profile"));
                        nfcModelTag.setPhone1(object.getString("Phone1"));
                        nfcModelTag.setPhone2(object.getString("Phone2"));
                        nfcModelTag.setMobile1(object.getString("Mobile1"));
                        nfcModelTag.setMobile2(object.getString("Mobile2"));
                        nfcModelTag.setFax1(object.getString("Fax1"));
                        nfcModelTag.setFax2(object.getString("Fax2"));
                        nfcModelTag.setEmail1(object.getString("Email1"));
                        nfcModelTag.setEmail2(object.getString("Email2"));
                        nfcModelTag.setAddress1(object.getString("Address1"));
                        nfcModelTag.setAddress2(object.getString("Address2"));
                        nfcModelTag.setAddress3(object.getString("Address3"));
                        nfcModelTag.setAddress4(object.getString("Address4"));
                        nfcModelTag.setCity(object.getString("City"));
                        nfcModelTag.setState(object.getString("State"));
                        nfcModelTag.setCountry(object.getString("Country"));
                        nfcModelTag.setPostalcode(object.getString("Postalcode"));
                        nfcModelTag.setWebsite(object.getString("Website"));
                        nfcModelTag.setFacebook(object.getString("Facebook"));
                        nfcModelTag.setTwitter(object.getString("Twitter"));
                        nfcModelTag.setGoogle(object.getString("Google"));
                        nfcModelTag.setLinkedin(object.getString("Linkedin"));
                        nfcModelTag.setYoutube(object.getString("Youtube"));
                        nfcModelTag.setAttachment_FileName(object.getString("Attachment_FileName"));
                        nfcModelTag.setProfile(object.getString("ProfileName"));

                       /* array = object.getJSONArray("Association_Name");
                        for (int i1 = 0; i1 < array.length(); i1++){

                            listAssociation.add(array.getString(i1));

                        }
                        Toast.makeText(getContext(), i + listAssociation.toString(), Toast.LENGTH_LONG).show();*/
                        allTags.add(nfcModelTag);
                      //  GetData(getContext());

                        displayProfile = object.getString("UserPhoto");
                    }

                    TestimonialProfileId = allTags.get(profileIndex).getProfileID();

//                    tvName.setText(allTags.get(0).getFirstName() + " "+ allTags.get(0).getLastName());
//                    tvPersonName.setText(allTags.get(0).getFirstName() + " "+ allTags.get(0).getLastName());
                    personName = allTags.get(profileIndex).getFirstName() + " "+ allTags.get(profileIndex).getLastName() ;
                    if(personName.equalsIgnoreCase("") || personName.equalsIgnoreCase("null"))
                    {
                        tvPersonName.setVisibility(View.GONE);
                        llNameBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvName.setText(personName);
                        tvPersonName.setText(personName);
                    }

                    tvProfileName.setText(allTags.get(profileIndex).getProfile());

                    try
                    {
                        JSONObject object = jsonArray.getJSONObject(profileIndex);
                        array = object.getJSONArray("Association_Name");
                        arrayEvents = object.getJSONArray("Event_Cat_Name");
                        listAssociation = new ArrayList<String>();
                        listEvents = new ArrayList<String>();

                        for (int i1 = 0; i1 < arrayEvents.length(); i1++) {

                            listEvents.add(arrayEvents.getString(i1));
                        }

                        for (int i1 = 0; i1 < array.length(); i1++) {

                            listAssociation.add(array.getString(i1));
                        }

                        int countAssociation;
                        if (array.length()>=5){
                            countAssociation = 5;
                        }else {
                            countAssociation = array.length();
                        }

                        int countEvents;
                        if (arrayEvents.length()>=5){
                            countEvents = 5;
                        }else {
                            countEvents = arrayEvents.length();
                        }

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), countAssociation, GridLayoutManager.HORIZONTAL, false);
                        recyclerAssociation.setAdapter(new TextRecyclerAdapter(listAssociation));
                        recyclerAssociation.setLayoutManager(gridLayoutManager);

                        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), countEvents, GridLayoutManager.HORIZONTAL, false);
                        recyclerEvents.setAdapter(new TextRecyclerAdapter(listEvents));
                        recyclerEvents.setLayoutManager(gridLayoutManager1);
                        if (listAssociation.size() == 0){
                            recyclerAssociation.setVisibility(View.GONE);
                            txtNoAssociation.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerAssociation.setVisibility(View.VISIBLE);
                            txtNoAssociation.setVisibility(View.GONE);
                        }

                        if (listEvents.size() == 0){
                            recyclerEvents.setVisibility(View.GONE);
                            txtNoEvent.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerEvents.setVisibility(View.VISIBLE);
                            txtNoEvent.setVisibility(View.GONE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (allTags.get(profileIndex).getCard_Front().equalsIgnoreCase("") || allTags.get(profileIndex).getCard_Back().equalsIgnoreCase("")) {
                        appbar.setVisibility(View.GONE);
                    } else {
                        appbar.setVisibility(View.VISIBLE);
                    }

                    if (allTags.get(profileIndex).getFacebook().equals("") || allTags.get(profileIndex).getFacebook().equals(null))
                    {
                        fbUrl.setImageResource(R.drawable.ic_fb_gray);
                        fbUrl.setEnabled(false);
                    }
                    else {
                        fbUrl.setImageResource(R.drawable.icon_fb);
                        fbUrl.setEnabled(true);
                        strfbUrl = allTags.get(profileIndex).getFacebook().toString();
                    }

                    if (allTags.get(profileIndex).getGoogle().equals("") || allTags.get(profileIndex).getGoogle().equals(null))
                    {
                        googleUrl.setImageResource(R.drawable.ic_google_gray);
                        googleUrl.setEnabled(false);
                    }
                    else {
                        googleUrl.setImageResource(R.drawable.icon_google);
                        googleUrl.setEnabled(true);
                        strgoogleUrl = allTags.get(profileIndex).getGoogle().toString();
                    }

                    if (allTags.get(profileIndex).getTwitter().equals("") || allTags.get(profileIndex).getTwitter().equals(null))
                    {
                        twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                        twitterUrl.setEnabled(false);
                    }
                    else {
                        twitterUrl.setImageResource(R.drawable.icon_twitter);
                        twitterUrl.setEnabled(true);
                        strtwitterUrl = allTags.get(profileIndex).getTwitter().toString();
                    }

                    if (allTags.get(profileIndex).getLinkedin().equals("") || allTags.get(profileIndex).getLinkedin().equals(null))
                    {
                        linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                        linkedInUrl.setEnabled(false);
                    }
                    else {
                        linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                        linkedInUrl.setEnabled(true);
                        strlinkedInUrl = allTags.get(profileIndex).getLinkedin().toString();
                    }

//                    tvDesignation.setText(allTags.get(0).getDesignation());
//                    tvDesi.setText(allTags.get(0).getDesignation());
                    if(allTags.get(profileIndex).getDesignation().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getDesignation().equalsIgnoreCase("null"))
                    {
                        tvDesignation.setVisibility(View.GONE);
                        llDesignationBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvDesignation.setText(allTags.get(profileIndex).getDesignation());
                        tvDesi.setText(allTags.get(profileIndex).getDesignation());
                    }
//                    tvCompany.setText(allTags.get(0).getCompanyName());
//                    tvCompanyName.setText(allTags.get(0).getCompanyName());
                    if(allTags.get(profileIndex).getCompanyName().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getCompanyName().equalsIgnoreCase("null"))
                    {
                        tvCompany.setVisibility(View.GONE);
                        llCompanyBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvCompany.setText(allTags.get(profileIndex).getCompanyName());
                        tvCompanyName.setText(allTags.get(profileIndex).getCompanyName());
                    }
//                    tvMob.setText(allTags.get(0).getPhone());
                    if(allTags.get(profileIndex).getMobile1().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getMobile1().equalsIgnoreCase("null"))
                    {
                        lnrMob.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvMob.setText(allTags.get(profileIndex).getMobile1()+"   "+allTags.get(profileIndex).getMobile2());
                    }
//                    tvWebsite.setText(allTags.get(0).getWebsite());
                    if(allTags.get(profileIndex).getWebsite().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getWebsite().equalsIgnoreCase("null"))
                    {
                        lnrWebsite.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvWebsite.setText(allTags.get(profileIndex).getWebsite());
                    }

                    if(allTags.get(profileIndex).getUserName().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getUserName().equalsIgnoreCase("null"))
                    {
                       llMailBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvMail.setText(allTags.get(profileIndex).getUserName());
                    }
                    if(allTags.get(profileIndex).getAssociation().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getAssociation().equalsIgnoreCase("null"))
                    {
                        llAssociationBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvAssociation.setText(allTags.get(profileIndex).getAssociation());
                    }
                    if(allTags.get(profileIndex).getPhone1().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getPhone1().equalsIgnoreCase("null"))
                    {
                        lnrWork.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvWork.setText(allTags.get(profileIndex).getPhone1()+"   "+allTags.get(profileIndex).getPhone2());
                    }
                  /*  if(allTags.get(0).getIndustry().equalsIgnoreCase("")
                            || allTags.get(0).getIndustry().equalsIgnoreCase("null"))
                    {
                        llIndustryBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        textIndustry.setText(allTags.get(0).getIndustry());
                    }*/
                    /*tvAddress.setText(allTags.get(0).getAddress1()+ " "+allTags.get(0).getAddress2() + " "
                            + allTags.get(0).getAddress3()  + " "
                            + allTags.get(0).getAddress4() + " "
                            + allTags.get(0).getCity() + " "
                            + allTags.get(0).getState() + " "
                            + allTags.get(0).getCountry() + " "
                            + allTags.get(0).getPostalcode());*/
                    personAddress =
                            allTags.get(profileIndex).getAddress1()+ " "
                            +allTags.get(profileIndex).getAddress2() + " "
                            /*+ allTags.get(profileIndex).getAddress3()  + " "
                            + allTags.get(profileIndex).getAddress4() + " "*/
                            + allTags.get(profileIndex).getCity() + " "
                            + allTags.get(profileIndex).getState() + " "
                            + allTags.get(profileIndex).getCountry() + " "
                            + allTags.get(profileIndex).getPostalcode() ;
                    if(personAddress.equalsIgnoreCase("")
                            || personAddress.equalsIgnoreCase("null")
                            || personAddress.startsWith(" "))
                    {
                        lnrMap.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvAddress.setText(personAddress);
                    }

                    image = new ArrayList<>();
                    if (allTags.get(profileIndex).getUserPhoto().equals(""))
                    {
                        imgProfile.setImageResource(R.drawable.usr_white1);
                    }
                    else {
                        Picasso.with(getContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+allTags.get(profileIndex).getUserPhoto()).into(imgProfile);
                    }

                    new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8999/Onet.svc/Testimonial/Fetch");

                    try
                    {
                        if(allTags.get(profileIndex).getCard_Front().equals(""))
                        {
                            recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                        }
                        else
                        {
                            recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(profileIndex).getCard_Front();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    }

                    try
                    {
                        if(allTags.get(profileIndex).getCard_Back().equals(""))
                        {
                            recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                        }
                        else
                        {
                            recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(profileIndex).getCard_Back();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(1);
                 //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(1);
                 //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    viewPager1.setAdapter(myPager);

                    // for bar code generating
                    try
                    {
                        barName = encrypt(TestimonialProfileId, secretKey);
                        bitmap = TextToImageEncode(barName);
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Not able to load Profiles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public  String POST4(String url)
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
            jsonObject.accumulate("AssociationID", "1" );
            jsonObject.accumulate("Card_Back", "000000002.jpg" );
            jsonObject.accumulate("Card_Front", "000000002.jpg" );
            jsonObject.accumulate("CompanyID", "1" );
            jsonObject.accumulate("CompanyName", "Circle One" );
            jsonObject.accumulate("Designation", "Director" );
            jsonObject.accumulate("DesignationID", "1" );
            jsonObject.accumulate("Email", "kajal.patadia@ample-arch.com" );
            jsonObject.accumulate("Email_Type", "gmail" );
            jsonObject.accumulate("IndustryID", "2" );
            jsonObject.accumulate("IndustryName", "IT" );
            jsonObject.accumulate("Phone", "+6588559632" );
            jsonObject.accumulate("Phone_type", "mobile" );
            jsonObject.accumulate("Profile_Desc", "fbvfbvvvf" );
            jsonObject.accumulate("UserID", "25" );

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

    private class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Testimonials...");
            //dialog.setTitle("Saving Reminder");
          // dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST2(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
           // dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Testimonials");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        lstTestimonial.setVisibility(View.GONE);
                        txtMore.setVisibility(View.GONE);
                        txtTestimonial.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lstTestimonial.setVisibility(View.VISIBLE);
                        txtMore.setVisibility(View.VISIBLE);
                        txtTestimonial.setVisibility(View.GONE);
                    }
                    allTaggs.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        if (i < 3)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                            TestimonialModel nfcModelTag = new TestimonialModel();
                            nfcModelTag.setCompanyName(object.getString("CompanyName"));
                            nfcModelTag.setDesignation(object.getString("Designation"));
                            nfcModelTag.setFirstName(object.getString("FirstName"));
                            nfcModelTag.setFriendProfileID(object.getString("FriendProfileID"));
                            nfcModelTag.setLastName(object.getString("LastName"));
                            nfcModelTag.setPurpose(object.getString("Purpose"));
                            nfcModelTag.setStatus(object.getString("Status"));
                            nfcModelTag.setTestimonial_Text(object.getString("Testimonial_Text"));
                            nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                            nfcModelTag.setTestimonial_ID(object.getString("Testimonial_ID"));
                            title_array.add(object.getString("Testimonial_Text").toString());
                            notice_array.add(String.valueOf(i));
//                        Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                            allTaggs.add(nfcModelTag);
                        }
                    }
                    customAdapter = new CustomAdapter(getActivity(), allTaggs);
                    lstTestimonial.setAdapter(customAdapter);
                    lstTestimonial.setExpanded(true);
                }
                else
                {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST2(String url)
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
            jsonObject.accumulate("ProfileId", TestimonialProfileId );
            jsonObject.accumulate("numofrecords", "10" );
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

    private class HttpAsyncTaskAddProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST4(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
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
            jsonObject.accumulate("profileid", TestimonialProfileId );
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
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


          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException
    {
        BitMatrix bitMatrix;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null  );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }*/

    public  void CustomProgressDialog(final String loading)
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


}
