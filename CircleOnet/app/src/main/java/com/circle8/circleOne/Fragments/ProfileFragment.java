package com.circle8.circleOne.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Activity.AttachmentDisplay;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.EditProfileActivity;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Activity.SearchGroupMembers;
import com.circle8.circleOne.Activity.TestimonialActivity;
import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CustomAdapter;
import com.circle8.circleOne.Adapter.PopupMenuAdapter;
import com.circle8.circleOne.Adapter.TextRecyclerAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ProfileSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentProfileBinding;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import static android.graphics.Color.WHITE;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.mergeBitmaps;

public class ProfileFragment extends Fragment implements View.OnClickListener
{
    static Bitmap bitmapQR,overlay;
    static ProgressDialog progressDialog ;
    static ArrayList<String> profile_array, NameArray, DesignationArray, profileImage_array;
    private static LoginSession session;
    public static ArrayList<ProfileModel> allTags ;
    static JSONArray array, arrayEvents,jsonArray;
    static List<String> listAssociation, listEvents;
    static ProfileModel nfcModelTag;
    private static ArrayList<String> image = new ArrayList<>();
    private static CardSwipe myPager ;
    public static ArrayList<TestimonialModel> allTaggs ;
    static CustomAdapter customAdapter;
    static String displayProfile,barName,refer,recycle_image1, recycle_image2,strfbUrl, strlinkedInUrl, strtwitterUrl, strgoogleUrl, stryoutubeUrl,personName , personAddress;
    static ArrayList<String> title_array = new ArrayList<String>();
    static ArrayList<String> notice_array = new ArrayList<String>();
    static View view;
    public static int profileIndex;
    public static ProfileSession profileSession;
    static ReferralCodeSession referralCodeSession;
    static String secretKey = "1234567890234561",Q_ID = "", profileId = "",TestimonialProfileId = "",UserID = "",associationString = "", eventString = "";
    AlertDialog QR_AlertDialog ;
    private long lastClickTime = 0;
    public static Activity mContext ;
    public static FragmentProfileBinding fragmentProfileBinding;
    private PopupWindow popupWindow;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Utility.freeMemory();
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        Utility.deleteCache(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);
        view = fragmentProfileBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        mContext =getActivity();
        allTaggs = new ArrayList<>();
        profileSession = new ProfileSession(getContext());
        session = new LoginSession(getContext());
        progressDialog = new ProgressDialog(getActivity());
        referralCodeSession = new ReferralCodeSession(getContext());

        SpannableString ss = new SpannableString("Ask your friends to write a Testimonial for you(100 words or less),Please choose from your CircleOne contacts and send a request.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent1 = new Intent(getContext(), SearchGroupMembers.class);
                intent1.putExtra("from", "profile");
                intent1.putExtra("ProfileId", TestimonialProfileId);
                startActivity(intent1);

            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 91, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // TextView textView = (TextView) findViewById(R.id.hello);
        fragmentProfileBinding.txtTestimonial.setText(ss);
        fragmentProfileBinding.txtTestimonial.setMovementMethod(LinkMovementMethod.getInstance());
        fragmentProfileBinding.txtTestimonial.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        fragmentProfileBinding.includeFrame2.imgProfile.setOnClickListener(this);
        fragmentProfileBinding.txtAttachment.setOnClickListener(this);
        fragmentProfileBinding.fbUrl.setOnClickListener(this);
        fragmentProfileBinding.googleUrl.setOnClickListener(this);
        fragmentProfileBinding.youtubeUrl.setOnClickListener(this);
        fragmentProfileBinding.twitterUrl.setOnClickListener(this);
        fragmentProfileBinding.linkedInUrl.setOnClickListener(this);
        fragmentProfileBinding.txtMore.setOnClickListener(this);
        fragmentProfileBinding.imgAdd.setOnClickListener(this);
        fragmentProfileBinding.includeFrame1.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                getActivity().finish();
                Utility.freeMemory();
            }
        });
        fragmentProfileBinding.includeFrame1.imgProfileShare.setOnClickListener(this);
        fragmentProfileBinding.includeFrame1.imgProfileMenu.setOnClickListener(this);
        fragmentProfileBinding.imgQR.setOnClickListener(this);
        fragmentProfileBinding.includeFrame1.ivEditProfile.setOnClickListener(this);
        fragmentProfileBinding.viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                fragmentProfileBinding.viewPager.scrollTo(fragmentProfileBinding.viewPager1.getScrollX(), fragmentProfileBinding.viewPager1.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    fragmentProfileBinding.viewPager.setCurrentItem(fragmentProfileBinding.viewPager1.getCurrentItem(), false);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        callMyProfile();

    }

    public static void CustomProgressDialog(final String loading)
    {
        fragmentProfileBinding.rlProgressDialog.setVisibility(View.VISIBLE);
        fragmentProfileBinding.txtProgressing.setText(loading+"...");
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anticlockwise);
        fragmentProfileBinding.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        fragmentProfileBinding.imgConnecting2.startAnimation(anim1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fbUrl:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (strfbUrl!=null) {
                    if (!strfbUrl.startsWith("http://") && !strfbUrl.startsWith("https://"))
                        strfbUrl = "http://" + strfbUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strfbUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.googleUrl:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (strgoogleUrl!=null) {
                    if (!strgoogleUrl.startsWith("http://") && !strgoogleUrl.startsWith("https://"))
                        strgoogleUrl = "http://" + strgoogleUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strgoogleUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.youtubeUrl:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (stryoutubeUrl!=null) {
                    if (!stryoutubeUrl.startsWith("http://") && !stryoutubeUrl.startsWith("https://"))
                        stryoutubeUrl = "http://" + stryoutubeUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stryoutubeUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.twitterUrl:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                if (strtwitterUrl!=null) {
                    if (!strtwitterUrl.startsWith("http://") && !strtwitterUrl.startsWith("https://"))
                        strtwitterUrl = "http://" + strtwitterUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strtwitterUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.linkedInUrl:
                if (strlinkedInUrl!=null) {
                    if (!strlinkedInUrl.startsWith("http://") && !strlinkedInUrl.startsWith("https://"))
                        strlinkedInUrl = "http://" + strlinkedInUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strlinkedInUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.txtAttachment:
                Utility.freeMemory();
                Utility.deleteCache(getContext());
                Intent intent = new Intent(getContext(), AttachmentDisplay.class);
                intent.putExtra("url", Utility.BASE_IMAGE_URL+"Other_doc/"+fragmentProfileBinding.txtAttachment.getText().toString());
                startActivity(intent);
                break ;
            case R.id.txtMore:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                Intent intent1 = new Intent(getContext(), TestimonialActivity.class);
                intent1.putExtra("ProfileId", TestimonialProfileId);
                intent1.putExtra("from", "profile");
                startActivity(intent1);
                break;
            case R.id.imgProfile:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.imageview_popup);
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (displayProfile.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_white1);
                }
                else
                {
                    Picasso.with(getActivity()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+displayProfile).placeholder(R.drawable.usr_1)
                            .resize(300,300).onlyScaleDown().skipMemoryCache().into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"UserProfile/"+displayProfile);
                        startActivity(intent);
                    }
                });

                dialog.show();
                break;
            case R.id.imgAdd:
                Utility.freeMemory();
                Utility.deleteCache(getContext());

                Intent intent_search = new Intent(getContext(), SearchGroupMembers.class);
                intent_search.putExtra("from", "profile");
                intent_search.putExtra("ProfileId", TestimonialProfileId);
                startActivity(intent_search);

                break;

            case R.id.imgProfileShare:
                Pref.setValue(mContext,"share","1");
                String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional networks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, fragmentProfileBinding.includeFrame2.tvPersonName.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));

                break ;
            case  R.id.imgQR:

                QR_AlertDialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme).create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                FrameLayout fl_QRFrame = (FrameLayout)dialogView.findViewById(R.id.fl_QrFrame);
                TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                ColorDrawable dialogColor = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                dialogColor.setAlpha(70);
                QR_AlertDialog.getWindow().setBackgroundDrawable(dialogColor);
                // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                tvBarName.setText(fragmentProfileBinding.includeFrame2.tvPersonName.getText().toString());
//                    bitmap = TextToImageEncode(barName);
                //ivBarImage.setImageBitmap(bitmap);
                ivBarImage.setImageBitmap(mergeBitmaps(overlay,bitmapQR));

                fl_QRFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        QR_AlertDialog.dismiss();
                    }
                });

                QR_AlertDialog.setView(dialogView);
                QR_AlertDialog.show();
                break;
            case R.id.imgProfileMenu:

                Utility.freeMemory();
                Utility.deleteCache(getContext());


                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();


                popupWindow = popupWindowsort();
                popupWindow.showAtLocation(v, Gravity.TOP | Gravity.RIGHT, 0, 0);

                break;
            case R.id.ivEditProfile:
                fragmentProfileBinding.includeFrame1.ivEditProfile.setBackground(getResources().getDrawable(R.drawable.ic_edit_gray));
                Intent intent_edit = new Intent(mContext, EditProfileActivity.class);
                intent_edit.putExtra("type", "edit");
                intent_edit.putExtra("profile_id", TestimonialProfileId);
                intent_edit.putExtra("activity", "profile");
                startActivity(intent_edit);
                getActivity().overridePendingTransition (0, 0);
                Log.e("fr","fr");


                break;
        }
    }

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(getContext());

        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view1 = inflator.inflate(R.layout.custom_dialog, null);
        popupWindow.setContentView(view1);
        popupWindow.setOutsideTouchable(true);

        PopupMenuAdapter adapter = new PopupMenuAdapter((Activity)getContext(), profile_array, profileImage_array);
        // the drop down list is a list view
        ListView listViewSort = view1.findViewById(R.id.ListPopupMenu);
        ImageView imgDismiss = view1.findViewById(R.id.imgMenu);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (profile_array.get(i).toString().equals("Add New Profile")) {
                    // new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8999/Onet.svc/AddProfile");
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("profile_id", TestimonialProfileId);
                    intent.putExtra("type", "add");
                    intent.putExtra("activity", "profile");
                    startActivity(intent);
                    popupWindow.dismiss();
                    // getActivity().overridePendingTransition(0, 0);
                } else {
                    profileSession.createProfileSession(String.valueOf(i));
                    profileIndex = i;
//                                    Toast.makeText(getContext(), profile_array.get(i).toString(), Toast.LENGTH_LONG).show();

                    fragmentProfileBinding.includeFrame2.tvPersonName.setText(allTags.get(i).getFirstName() + " " + allTags.get(i).getLastName());
                    //DashboardFragment.User_name = allTags.get(i).getFirstName() + " "+ allTags.get(i).getLastName();
                    fragmentProfileBinding.includeFrame2.tvDesignation.setText(allTags.get(i).getDesignation());
                    fragmentProfileBinding.includeFrame2.tvCompany.setText(allTags.get(i).getCompanyName());
                    fragmentProfileBinding.tvName.setText(allTags.get(i).getFirstName() + " " + allTags.get(i).getLastName());
                    fragmentProfileBinding.tvCompanyName.setText(allTags.get(i).getCompanyName());
                    fragmentProfileBinding.tvDesi.setText(allTags.get(i).getDesignation());
                    fragmentProfileBinding.tvMob.setText(allTags.get(i).getMobile1());
                    fragmentProfileBinding.tvWork.setText(allTags.get(i).getPhone1());
                    fragmentProfileBinding.tvProfileName.setText(allTags.get(i).getProfile());

                    if (allTags.get(i).getIndustry().equalsIgnoreCase("")
                            || allTags.get(i).getIndustry().equalsIgnoreCase("null")) {
                        fragmentProfileBinding.llIndustryBox.setVisibility(View.GONE);
                    } else {
                        fragmentProfileBinding.textIndustry.setText(allTags.get(i).getIndustry());
                    }

                    if (allTags.get(i).getAttachment_FileName().toString().equals("") || allTags.get(i).getAttachment_FileName().toString() == null ||
                            allTags.get(i).getAttachment_FileName().toString().equals("null")) {

                        fragmentProfileBinding.txtAttachment.setVisibility(View.GONE);
                        fragmentProfileBinding.lblAttachment.setVisibility(View.GONE);
                    } else {
                        fragmentProfileBinding.txtAttachment.setVisibility(View.VISIBLE);
                        fragmentProfileBinding.lblAttachment.setVisibility(View.VISIBLE);

                        fragmentProfileBinding.txtAttachment.setText(allTags.get(i).getAttachment_FileName());
                    }

                    String add = allTags.get(i).getAddress1() + " ";

                    if (allTags.get(i).getAddress2().toString().equals("") ||
                            allTags.get(i).getAddress2().toString().equals("null") ||
                            allTags.get(i).getAddress2().toString().equals(null)) {
                        add += allTags.get(i).getAddress2().toString() + "\n";
                    }
                    add += allTags.get(i).getAddress3() + " ";

                    if (allTags.get(i).getAddress4().toString().equals("") ||
                            allTags.get(i).getAddress4().toString().equals("null") ||
                            allTags.get(i).getAddress4().toString().equals(null)) {
                        add += allTags.get(i).getAddress4().toString() + "\n";
                    }
                    add += allTags.get(i).getCity() + " ";

                    if (allTags.get(i).getState().toString().equals("") ||
                            allTags.get(i).getState().toString().equals("null") ||
                            allTags.get(i).getState().toString().equals(null)) {
                        add += allTags.get(i).getState().toString() + "\n";
                    }
                    add += allTags.get(i).getCountry() + " "
                            + allTags.get(i).getPostalcode();
                    fragmentProfileBinding.tvAddress.setText(
                            add);


                    if (allTags.get(i).getWebsite().equalsIgnoreCase("")
                            || allTags.get(i).getWebsite().equalsIgnoreCase("null")
                            || allTags.get(i).getWebsite().equalsIgnoreCase(null)) {
                        fragmentProfileBinding.lnrWebsite.setVisibility(View.GONE);
                    } else {
                        fragmentProfileBinding.tvWebsite.setText(allTags.get(i).getWebsite());
                    }
                    displayProfile = allTags.get(i).getUserPhoto();
                    TestimonialProfileId = allTags.get(i).getProfileID();

                    HashMap<String, String> user = session.getUserDetails();
                    String user_id = user.get(LoginSession.KEY_USERID);
                    String email_id = user.get(LoginSession.KEY_EMAIL);
                    String user_img = user.get(LoginSession.KEY_IMAGE);
                    String user_pass = user.get(LoginSession.KEY_PASSWORD);
                    String name = user.get(LoginSession.KEY_NAME);
                    String kept1 = name.substring(0, name.indexOf(" "));
                    String remainder1 = name.substring(name.indexOf(" ") + 1, name.length());

                    String first_name = kept1;
                    String last_name = remainder1;

                    String gender = user.get(LoginSession.KEY_GENDER);
                    String date_DOB = user.get(LoginSession.KEY_DOB);
                    String phone_no = user.get(LoginSession.KEY_PHONE);
                    String Connection_Limit = user.get(LoginSession.KEY_CONNECTION_LIMIT);
                    String Connection_Left = user.get(LoginSession.KEY_CONNECTION_LEFT);

                    session.createLoginSession(Q_ID, allTags.get(i).getProfileID(), user_id, allTags.get(i).getFirstName() + " " + allTags.get(i).getLastName(), email_id, user_img, gender, user_pass, date_DOB, phone_no, Connection_Limit, Connection_Left);

                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        try {
                            array = object.getJSONArray("Association_Name");
                            associationString = "";
                            listAssociation = new ArrayList<String>();
                            for (int i1 = 0; i1 < array.length(); i1++) {

                                String name1 = array.getString(i1).toString();
                                String kept = name1.substring(0, name1.indexOf(":"));
                                String remainder = name1.substring(name1.indexOf(":") + 1, name1.length());


                                listAssociation.add(remainder);

                                if (i1 == array.length() - 1) {
                                    associationString += remainder;
                                } else {
                                    associationString += remainder + " / ";
                                }


                                // associationString += remainder + " / ";
                            }
                            fragmentProfileBinding.txtAssociationList.setText(associationString);
                            int countAssociation;
                            if (array.length() >= 5) {
                                countAssociation = 5;
                            } else {
                                countAssociation = array.length();
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), countAssociation, GridLayoutManager.HORIZONTAL, false);
                            fragmentProfileBinding.recyclerAssociation.setAdapter(new TextRecyclerAdapter(listAssociation));
                            fragmentProfileBinding.recyclerAssociation.setLayoutManager(gridLayoutManager);

                            if (listAssociation.size() == 0) {
                                fragmentProfileBinding.txtAssociationList.setVisibility(View.GONE);
                                //  recyclerAssociation.setVisibility(View.GONE);
                                fragmentProfileBinding.txtNoAssociation.setVisibility(View.VISIBLE);
                            } else {
                                fragmentProfileBinding.txtAssociationList.setVisibility(View.VISIBLE);
                                // recyclerAssociation.setVisibility(View.VISIBLE);
                                fragmentProfileBinding.txtNoAssociation.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                        }

                        try {
                            arrayEvents = object.getJSONArray("Event_Cat_Name");

                            listEvents = new ArrayList<String>();
                            eventString = "";
                            for (int i1 = 0; i1 < arrayEvents.length(); i1++) {

                                listEvents.add(arrayEvents.getString(i1));
                                String remainder = "";
                                String name1 = arrayEvents.getString(i1);
                                if (name1.contains(":")) {
                                    //String kept = name.substring(0, name.indexOf(":"));
                                    remainder = name1.substring(name1.indexOf(":") + 1, name1.length());
                                } else {
                                    remainder = name1;
                                }
                                if (i1 == arrayEvents.length() - 1) {
                                    eventString += remainder;
                                } else {
                                    eventString += remainder + " / ";
                                }


                            }
                            fragmentProfileBinding.txtEventsListfinal.setText(eventString);
                            int countEvents;
                            if (arrayEvents.length() >= 5) {
                                countEvents = 5;
                            } else {
                                countEvents = arrayEvents.length();
                            }

                            GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), countEvents, GridLayoutManager.HORIZONTAL, false);
                            fragmentProfileBinding.recyclerEvents.setAdapter(new TextRecyclerAdapter(listEvents));
                            fragmentProfileBinding.recyclerEvents.setLayoutManager(gridLayoutManager1);


                            if (listEvents.size() == 0) {
                                fragmentProfileBinding.txtEventsListfinal.setVisibility(View.GONE);
                                // recyclerEvents.setVisibility(View.GONE);
                                fragmentProfileBinding.txtNoEvent.setVisibility(View.VISIBLE);
                            } else {
                                fragmentProfileBinding.txtEventsListfinal.setVisibility(View.VISIBLE);
                                // recyclerEvents.setVisibility(View.VISIBLE);
                                fragmentProfileBinding.txtNoEvent.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (allTags.get(i).getUserPhoto().equals("")) {
                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.GONE);
                        fragmentProfileBinding.includeFrame2.imgProfile.setImageResource(R.drawable.usr_white1);
                    } else {
                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(Utility.BASE_IMAGE_URL + "UserProfile/" + allTags.get(i).getUserPhoto())
                                .asBitmap()
                                .into(new BitmapImageViewTarget(fragmentProfileBinding.includeFrame2.imgProfile) {
                                    @Override
                                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                        super.onResourceReady(drawable, anim);
                                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.GONE);
                                        fragmentProfileBinding.includeFrame2.imgProfile.setImageBitmap(drawable);
                                    }
                                });

                    }

                    if (allTags.get(i).getCard_Front().equalsIgnoreCase("") && allTags.get(i).getCard_Back().equalsIgnoreCase("")) {
                        fragmentProfileBinding.appbar.setVisibility(View.GONE);
                    } else {
                        fragmentProfileBinding.appbar.setVisibility(View.VISIBLE);
                    }

                    if (allTags.get(i).getFacebook().equals("") || allTags.get(i).getFacebook().equals(null)) {
                        fragmentProfileBinding.fbUrl.setImageResource(R.drawable.ic_fb_gray);
                        fragmentProfileBinding.fbUrl.setEnabled(false);
                    } else {
                        fragmentProfileBinding.fbUrl.setImageResource(R.drawable.icon_fb);
                        fragmentProfileBinding.fbUrl.setEnabled(true);
                        strfbUrl = allTags.get(i).getFacebook().toString();
                    }

                    if (allTags.get(i).getGoogle().equals("") || allTags.get(i).getGoogle().equals(null)) {
                        fragmentProfileBinding.googleUrl.setImageResource(R.drawable.ic_google_gray);
                        fragmentProfileBinding.googleUrl.setEnabled(false);
                    } else {
                        fragmentProfileBinding.googleUrl.setImageResource(R.drawable.icon_google);
                        fragmentProfileBinding.googleUrl.setEnabled(true);
                        strgoogleUrl = allTags.get(i).getGoogle().toString();
                    }

                    if (allTags.get(i).getTwitter().equals("") || allTags.get(i).getTwitter().equals(null)) {
                        fragmentProfileBinding.twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                        fragmentProfileBinding.twitterUrl.setEnabled(false);
                    } else {
                        fragmentProfileBinding.twitterUrl.setImageResource(R.drawable.icon_twitter);
                        fragmentProfileBinding.twitterUrl.setEnabled(true);
                        strtwitterUrl = allTags.get(i).getTwitter().toString();
                    }

                    if (allTags.get(i).getLinkedin().equals("") || allTags.get(i).getLinkedin().equals(null)) {
                        fragmentProfileBinding.linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                        fragmentProfileBinding.linkedInUrl.setEnabled(false);
                    } else {
                        fragmentProfileBinding.linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                        fragmentProfileBinding.linkedInUrl.setEnabled(true);
                        strlinkedInUrl = allTags.get(i).getLinkedin().toString();
                    }

                    new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL + "Testimonial/Fetch");

                    try {
                        if (allTags.get(i).getCard_Front().equals("")) {
                            recycle_image1 = "";
                        } else {
                            recycle_image1 = Utility.BASE_IMAGE_URL + "Cards/" + allTags.get(i).getCard_Front();
                        }
                    } catch (Exception e) {
                        recycle_image1 = "";
                    }

                    try {
                        if (allTags.get(i).getCard_Back().equals("")) {
                            recycle_image2 = "";
                        } else {
                            recycle_image2 = Utility.BASE_IMAGE_URL + "Cards/" + allTags.get(i).getCard_Back();
                        }
                    } catch (Exception e) {
                        recycle_image2 = "";
                    }

                    image = new ArrayList<>();
                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getContext(), image);

                    fragmentProfileBinding.viewPager.setClipChildren(false);
                    fragmentProfileBinding.viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    fragmentProfileBinding.viewPager.setOffscreenPageLimit(2);
                    //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    fragmentProfileBinding.viewPager.setAdapter(myPager);

                    fragmentProfileBinding.viewPager1.setClipChildren(false);
                    fragmentProfileBinding.viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    fragmentProfileBinding.viewPager1.setOffscreenPageLimit(2);
                    // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    fragmentProfileBinding.viewPager1.setAdapter(myPager);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms

                            try {
                                barName = encrypt(TestimonialProfileId, secretKey);
                                //  bitmap = TextToImageEncode(barName);
                            } catch (UnsupportedEncodingException e) {
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
                            }


                            try {
                                //setting size of qr code
                                int width = 600;
                                int height = 600;
                                int smallestDimension = width < height ? width : height;

                                String qrCodeData = barName;
                                //setting parameters for qr code
                                String charset = "UTF-8";
                                Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                                CreateQRCode(getContext(), qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

                            } catch (Exception ex) {
                                Log.e("QrGenerate", ex.getMessage());
                            }
                        }
                    }, 5000);
                }
            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(460);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content

        return popupWindow;
    }

    public static void CreateQRCode(Context context, String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){


        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    pixels[offset + x] = matrix.get(x, y) ?
                            ResourcesCompat.getColor(context.getResources(),R.color.qrColor,null) :WHITE;
                }
            }

            bitmapQR = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapQR.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view

            overlay = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);


        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }


    public static void callMyProfile()
    {
        new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");
    }

    @Override
    public void onResume()
    {
        super.onResume();
       /* HashMap<String, String> profile = profileSession.getProfileDetails();
        profileIndex = Integer.parseInt(profile.get(ProfileSession.KEY_PROFILE_INDEX));
        callMyProfile();*/

        if(!Pref.getValue(mContext,"share","").equalsIgnoreCase("1")) {
            HashMap<String, String> user = session.getUserDetails();
            UserID = user.get(LoginSession.KEY_USERID);

            HashMap<String, String> profile = profileSession.getProfileDetails();
            profileIndex = Integer.parseInt(profile.get(ProfileSession.KEY_PROFILE_INDEX));

            new HttpAsyncTaskProfiles().execute(Utility.BASE_URL + "MyProfiles");
        }else
        {
            Pref.setValue(mContext,"share","");
        }
//        new HttpAsyncTaskProfiles().execute(Utility.BASE_URL+"MyProfiles");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            Utility.freeMemory();
            Utility.deleteCache(getContext());
        }
    }

    public static String encrypt(String value, String key)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        byte[] value_bytes = value.getBytes("UTF-8");
        byte[] key_bytes = getKeyBytes(key);
        return Base64.encodeToString(encrypt(value_bytes, key_bytes, key_bytes), 0);
    }

    public static byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        // setup AES cipher in CBC mode with PKCS #5 padding
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // encrypt
        localCipher.init(1, new SecretKeySpec(paramArrayOfByte2, "AES"), new IvParameterSpec(paramArrayOfByte3));
        return localCipher.doFinal(paramArrayOfByte1);
    }

    private static byte[] getKeyBytes(String paramString)
            throws UnsupportedEncodingException
    {
        byte[] arrayOfByte1 = new byte[16];
        byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
        return arrayOfByte1;
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

    public static String POST5(String url)
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



    public static class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String loading = "Fetching profile" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            allTags = new ArrayList<>();
            HashMap<String, String> referral = referralCodeSession.getReferralDetails();
            try {
                refer = referral.get(ReferralCodeSession.KEY_REFERRAL);
            }catch (Exception e){}
            HashMap<String, String> profile = profileSession.getProfileDetails();
            profileIndex = Integer.parseInt(profile.get(ProfileSession.KEY_PROFILE_INDEX));

            listAssociation = new ArrayList<>();
            HashMap<String, String> user = session.getUserDetails();
            UserID = user.get(LoginSession.KEY_USERID);
            profileId = user.get(LoginSession.KEY_PROFILEID);
            Q_ID = user.get(LoginSession.KEY_QID);

            return POST5(urls[0]);
        }
        @Override
        protected void onPostExecute(String result)
        {

            fragmentProfileBinding.rlProgressDialog.setVisibility(View.GONE);
            try
            {

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("Profiles");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    profile_array = new ArrayList<String>();
                    profileImage_array = new ArrayList<>();
                    NameArray = new ArrayList<>();
                    DesignationArray = new ArrayList<>();
                    listAssociation = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        if (object.getString("ProfileName").toString().equals("")){
                            profile_array.add(object.getString("FirstName") + " " + object.getString("LastName"));
                        }
                        else {
                            profile_array.add(object.getString("ProfileName"));
                        }

                        profileImage_array.add(object.getString("UserPhoto"));

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
                        nfcModelTag.setIndustry(object.getString("IndustryName"));
                        allTags.add(nfcModelTag);

                    }

                    profile_array.add("Add New Profile");
                    profileImage_array.add("");

                    try {
                        displayProfile = allTags.get(profileIndex).getUserPhoto();
                    }catch (Exception e){
                        profileSession.createProfileSession("0");
                        profileIndex = 0;
                        displayProfile = allTags.get(profileIndex).getUserPhoto();
                    }

                    TestimonialProfileId = allTags.get(profileIndex).getProfileID();
                    personName = allTags.get(profileIndex).getFirstName() + " "+ allTags.get(profileIndex).getLastName() ;
                    if(personName.equalsIgnoreCase("") || personName.equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.includeFrame2.tvPersonName.setVisibility(View.GONE);
                        fragmentProfileBinding.llNameBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvName.setText(personName);
                        fragmentProfileBinding.includeFrame2.tvPersonName.setText(personName);
                    }

                    fragmentProfileBinding.tvProfileName.setText(allTags.get(profileIndex).getProfile());

                    if (allTags.get(profileIndex).getAttachment_FileName().toString().equals("") || allTags.get(profileIndex).getAttachment_FileName().toString() == null ||
                            allTags.get(profileIndex).getAttachment_FileName().toString().equals("null")) {

                        fragmentProfileBinding.txtAttachment.setVisibility(View.GONE);
                        fragmentProfileBinding.lblAttachment.setVisibility(View.GONE);
                    }
                    else {
                        fragmentProfileBinding.txtAttachment.setVisibility(View.VISIBLE);
                        fragmentProfileBinding.lblAttachment.setVisibility(View.VISIBLE);

                        fragmentProfileBinding.txtAttachment.setText(allTags.get(profileIndex).getAttachment_FileName());
                    }

                    try
                    {
                        JSONObject object = jsonArray.getJSONObject(profileIndex);
                        array = object.getJSONArray("Association_Name");
                        arrayEvents = object.getJSONArray("Event_Cat_Name");
                        listAssociation = new ArrayList<String>();
                        listEvents = new ArrayList<String>();
                        eventString = "";
                        for (int i1 = 0; i1 < arrayEvents.length(); i1++) {

                            listEvents.add(arrayEvents.getString(i1));
                            String remainder = "";
                            String name = arrayEvents.getString(i1);
                            if (name.contains(":")) {
                                //String kept = name.substring(0, name.indexOf(":"));
                                remainder = name.substring(name.indexOf(":") + 1, name.length());
                            }
                            else {
                                remainder = name;
                            }
                            if (i1 == arrayEvents.length()-1){
                                eventString += remainder ;
                            }else {
                                eventString += remainder + " / ";
                            }
                        }
                        fragmentProfileBinding.txtEventsListfinal.setText(eventString);
                        associationString = "";
                        for (int i1 = 0; i1 < array.length(); i1++) {

                            listAssociation.add(array.getString(i1));


                            String name = array.getString(i1);
                            String remainder;
                            if (name.contains(":")) {
                                String kept = name.substring(0, name.indexOf(":"));
                                remainder = name.substring(name.indexOf(":") + 1, name.length());
                            }
                            else {
                                remainder = name;
                            }
                            if (i1 == array.length()-1){
                                associationString += remainder ;
                            }else {
                                associationString += remainder + " / ";
                            }
                        }
                        fragmentProfileBinding.txtAssociationList.setText(associationString);
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

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, countAssociation, GridLayoutManager.HORIZONTAL, false);
                        fragmentProfileBinding.recyclerAssociation.setAdapter(new TextRecyclerAdapter(listAssociation));
                        fragmentProfileBinding.recyclerAssociation.setLayoutManager(gridLayoutManager);

                        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, countEvents, GridLayoutManager.HORIZONTAL, false);
                        fragmentProfileBinding.recyclerEvents.setAdapter(new TextRecyclerAdapter(listEvents));
                        fragmentProfileBinding.recyclerEvents.setLayoutManager(gridLayoutManager1);
                        if (listAssociation.size() == 0){
                            fragmentProfileBinding.txtAssociationList.setVisibility(View.GONE);
                            // recyclerAssociation.setVisibility(View.GONE);
                            fragmentProfileBinding.txtNoAssociation.setVisibility(View.VISIBLE);
                        }
                        else {
                            fragmentProfileBinding.txtAssociationList.setVisibility(View.VISIBLE);
                            // recyclerAssociation.setVisibility(View.VISIBLE);
                            fragmentProfileBinding.txtNoAssociation.setVisibility(View.GONE);
                        }

                        if (listEvents.size() == 0){
                            fragmentProfileBinding.txtEventsListfinal.setVisibility(View.GONE);
                            // recyclerEvents.setVisibility(View.GONE);
                            fragmentProfileBinding.txtNoEvent.setVisibility(View.VISIBLE);
                        }
                        else {
                            fragmentProfileBinding.txtEventsListfinal.setVisibility(View.VISIBLE);
                            // recyclerEvents.setVisibility(View.VISIBLE);
                            fragmentProfileBinding.txtNoEvent.setVisibility(View.GONE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (allTags.get(profileIndex).getCard_Front().equalsIgnoreCase("") || allTags.get(profileIndex).getCard_Back().equalsIgnoreCase("")) {
                        fragmentProfileBinding.appbar.setVisibility(View.GONE);
                    } else {
                        fragmentProfileBinding.appbar.setVisibility(View.VISIBLE);
                    }

                    if (allTags.get(profileIndex).getFacebook().equals("") || allTags.get(profileIndex).getFacebook().equals(null))
                    {
                        fragmentProfileBinding.fbUrl.setImageResource(R.drawable.ic_fb_gray);
                        fragmentProfileBinding.fbUrl.setEnabled(false);
                    }
                    else {
                        fragmentProfileBinding.fbUrl.setImageResource(R.drawable.icon_fb);
                        fragmentProfileBinding.fbUrl.setEnabled(true);
                        strfbUrl = allTags.get(profileIndex).getFacebook().toString();
                    }

                    if (allTags.get(profileIndex).getGoogle().equals("") || allTags.get(profileIndex).getGoogle().equals(null))
                    {
                        fragmentProfileBinding.googleUrl.setImageResource(R.drawable.ic_google_gray);
                        fragmentProfileBinding.googleUrl.setEnabled(false);
                    }
                    else {
                        fragmentProfileBinding.googleUrl.setImageResource(R.drawable.icon_google);
                        fragmentProfileBinding.googleUrl.setEnabled(true);
                        strgoogleUrl = allTags.get(profileIndex).getGoogle().toString();
                    }

                    if (allTags.get(profileIndex).getTwitter().equals("") || allTags.get(profileIndex).getTwitter().equals(null))
                    {
                        fragmentProfileBinding.twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                        fragmentProfileBinding.twitterUrl.setEnabled(false);
                    }
                    else {
                        fragmentProfileBinding.twitterUrl.setImageResource(R.drawable.icon_twitter);
                        fragmentProfileBinding.twitterUrl.setEnabled(true);
                        strtwitterUrl = allTags.get(profileIndex).getTwitter().toString();
                    }

                    if (allTags.get(profileIndex).getLinkedin().equals("") || allTags.get(profileIndex).getLinkedin().equals(null))
                    {
                        fragmentProfileBinding.linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                        fragmentProfileBinding.linkedInUrl.setEnabled(false);
                    }
                    else {
                        fragmentProfileBinding.linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                        fragmentProfileBinding.linkedInUrl.setEnabled(true);
                        strlinkedInUrl = allTags.get(profileIndex).getLinkedin().toString();
                    }

                    if(allTags.get(profileIndex).getDesignation().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getDesignation().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.includeFrame2.tvDesignation.setVisibility(View.GONE);
                        fragmentProfileBinding.llDesignationBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.includeFrame2.tvDesignation.setText(allTags.get(profileIndex).getDesignation());
                        fragmentProfileBinding.tvDesi.setText(allTags.get(profileIndex).getDesignation());
                    }
                    if(allTags.get(profileIndex).getCompanyName().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getCompanyName().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.includeFrame2.tvCompany.setVisibility(View.GONE);
                        fragmentProfileBinding.llCompanyBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.includeFrame2.tvCompany.setText(allTags.get(profileIndex).getCompanyName());
                        fragmentProfileBinding.tvCompanyName.setText(allTags.get(profileIndex).getCompanyName());
                    }
//                    tvMob.setText(allTags.get(0).getPhone());
                    if(allTags.get(profileIndex).getMobile1().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getMobile1().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.lnrMob.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvMob.setText(allTags.get(profileIndex).getMobile1()+"   "+allTags.get(profileIndex).getMobile2());
                    }
//                    tvWebsite.setText(allTags.get(0).getWebsite());
                    if(allTags.get(profileIndex).getWebsite().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getWebsite().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.lnrWebsite.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvWebsite.setText(allTags.get(profileIndex).getWebsite());
                    }

                    if(allTags.get(profileIndex).getUserName().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getUserName().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.llMailBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvMail.setText(allTags.get(profileIndex).getUserName());
                    }

                    if (allTags.get(profileIndex).getEmail2().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getEmail2().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.llMailBox1.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvMail1.setText(allTags.get(profileIndex).getEmail2());
                    }

                    if(allTags.get(profileIndex).getAssociation().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getAssociation().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding. llAssociationBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvAssociation.setText(allTags.get(profileIndex).getAssociation());
                    }
                    if(allTags.get(profileIndex).getPhone1().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getPhone1().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.lnrWork.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvWork.setText(allTags.get(profileIndex).getPhone1()+"   "+allTags.get(profileIndex).getPhone2());
                    }
                    if(allTags.get(profileIndex).getIndustry().equalsIgnoreCase("")
                            || allTags.get(profileIndex).getIndustry().equalsIgnoreCase("null"))
                    {
                        fragmentProfileBinding.llIndustryBox.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.textIndustry.setText(allTags.get(profileIndex).getIndustry());
                    }

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
                        fragmentProfileBinding.lnrMap.setVisibility(View.GONE);
                    }
                    else
                    {
                        fragmentProfileBinding.tvAddress.setText(personAddress);
                    }

                    image = new ArrayList<>();


                    if (allTags.get(profileIndex).getUserPhoto().equals(""))
                    {
                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.GONE);
                        fragmentProfileBinding.includeFrame2.imgProfile.setImageResource(R.drawable.usr_white1);
                    }
                    else {
                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(Utility.BASE_IMAGE_URL+"UserProfile/"+allTags.get(profileIndex).getUserPhoto())
                                .asBitmap()
                                .into(new BitmapImageViewTarget(fragmentProfileBinding.includeFrame2.imgProfile) {
                                    @Override
                                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                        super.onResourceReady(drawable, anim);
                                        fragmentProfileBinding.includeFrame2.progressBar1.setVisibility(View.GONE);
                                        fragmentProfileBinding.includeFrame2.imgProfile.setImageBitmap(drawable);
                                    }
                                });
                    }

                    new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL+"Testimonial/Fetch");

                    try
                    {
                        if(allTags.get(profileIndex).getCard_Front().equals(""))
                        {
                            recycle_image1 ="";
                        }
                        else
                        {
                            recycle_image1 = Utility.BASE_IMAGE_URL+"Cards/"+allTags.get(profileIndex).getCard_Front();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image1 ="";
                    }

                    try
                    {
                        if(allTags.get(profileIndex).getCard_Back().equals(""))
                        {
                            recycle_image2 ="";
                        }
                        else
                        {
                            recycle_image2 = Utility.BASE_IMAGE_URL+"Cards/"+allTags.get(profileIndex).getCard_Back();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image2 ="";
                    }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(mContext, image);

                    fragmentProfileBinding.viewPager.setClipChildren(false);
                    fragmentProfileBinding.viewPager.setPageMargin(mContext.getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    fragmentProfileBinding.viewPager.setOffscreenPageLimit(1);
                    //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    fragmentProfileBinding.viewPager.setAdapter(myPager);

                    fragmentProfileBinding.viewPager1.setClipChildren(false);
                    fragmentProfileBinding.viewPager1.setPageMargin(mContext.getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    fragmentProfileBinding.viewPager1.setOffscreenPageLimit(1);
                    //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    fragmentProfileBinding.viewPager1.setAdapter(myPager);



                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            try
                            {
                                barName = encrypt(TestimonialProfileId, secretKey);
                                // bitmap = TextToImageEncode(barName);
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
                            }
                            try {
                                //setting size of qr code
                                int width =600;
                                int height = 600;
                                int smallestDimension = width < height ? width : height;

                                String qrCodeData = barName;
                                //setting parameters for qr code
                                String charset = "UTF-8";
                                Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                                CreateQRCode(mContext, qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

                            } catch (Exception ex) {
                                Log.e("QrGenerate",ex.getMessage());
                            }
                        }
                    }, 5000);
                    // for bar code generating

                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Profiles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private static class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ProfileId", TestimonialProfileId );
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", "1" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(Utility.BASE_URL + "Testimonial/Fetch",jsonObject);
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
                        fragmentProfileBinding.lstTestimonial.setVisibility(View.GONE);
                        fragmentProfileBinding.txtMore.setVisibility(View.GONE);
                        fragmentProfileBinding.txtTestimonial.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        fragmentProfileBinding.lstTestimonial.setVisibility(View.VISIBLE);
                        fragmentProfileBinding.txtMore.setVisibility(View.VISIBLE);
                        fragmentProfileBinding.txtTestimonial.setVisibility(View.GONE);
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
                    customAdapter = new CustomAdapter(mContext, allTaggs);
                    fragmentProfileBinding.lstTestimonial.setAdapter(customAdapter);
                    fragmentProfileBinding.lstTestimonial.setExpanded(true);
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
}
