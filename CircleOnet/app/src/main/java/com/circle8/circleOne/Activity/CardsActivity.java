package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Fragments.CardsFragment;
import com.circle8.circleOne.Fragments.EventsFragment;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List4Fragment;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ProfileSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.LocationUtil.PermissionUtils;
import com.circle8.circleOne.MultiContactPicker;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.CircularTextView;
import com.circle8.circleOne.Utils.CustomViewPager;
import com.circle8.circleOne.Utils.PrefUtils;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.chat.ChatHelper;
import com.circle8.circleOne.chat.qb.QbDialogHolder;
import com.circle8.circleOne.databinding.ActivityCardsBinding;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.plus.Plus;
import com.google.firebase.auth.FirebaseAuth;
import com.linkedin.platform.LISessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.twitter.sdk.android.Twitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
public class CardsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback
{
    public static CustomViewPager mViewPager;
    ImageView imgDrawer;
    ImageView imgLogo;
    private int actionBarHeight;
    static TextView textView, txtNotificationCountAction;
    public static int position = 0, nested_position = 0;
    private Date location;
    boolean done = false;
    public static GoogleApiClient mGoogleApiClient;
    LoginSession session;
    private FirebaseAuth mAuth;
    String profileId = "", nfcProfileId = "";
    public String secretKey = "1234567890234561";
    CircularTextView txtNotificationCount;
    String UserId= "", NotificationCount ;
    boolean doubleBackToExitPressedOnce = false;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int PERMISSION_REQUEST_CONTACT = 111;
    private static final String TAG = CardsActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    public static Location mLastLocation;
    GoogleSignInOptions gso;
    public double latitude;
    public double longitude;
    String lat = "", lng = "";
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    public static boolean isPermissionGranted;
    public static String Connection_Limit, Connection_Left;
    ReferralCodeSession referralCodeSession;
    private String refer;
    String User_name;
    ArrayList<QBUser> selectedUsers = new ArrayList<QBUser>();
    private NfcAdapter mNfcAdapter;
    ArrayList<String> arrayNFC  = new ArrayList<>();
    String CardCode = "";
    Boolean netCheck= false;
    public static final byte[] MIME_TEXT = "application/com.circle8.circleOne".getBytes();
    ActivityCardsBinding activityCardsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityCardsBinding = DataBindingUtil.setContentView(this,R.layout.activity_cards);

        session = new LoginSession(getApplicationContext());
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        textView = (TextView) findViewById(R.id.mytext);
        txtNotificationCountAction = (TextView) findViewById(R.id.txtNotificationCountAction);
        txtNotificationCountAction.setVisibility(View.GONE);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        activityCardsBinding.container.setOffscreenPageLimit(2);
        activityCardsBinding.container.setAdapter(mSectionsPagerAdapter);
        activityCardsBinding.container.setPagingEnabled(false);

        activityCardsBinding.tabs.setupWithViewPager(activityCardsBinding.container);
        activityCardsBinding.tabs.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(CardsActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());

        try {

            final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
            switch (requestCode) {
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            // All required changes were successfully made
                            getLocation();
                            break;
                        case Activity.RESULT_CANCELED:
                            // The user was asked to change settings, but chose not to
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }catch (Exception e){}
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Utility.freeMemory();


        netCheck = Utility.isNetworkAvailable(getApplicationContext());
        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);
        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
            nested_position = extras.getInt("nested_viewpager_position");
        }
        permissionUtils = new PermissionUtils(CardsActivity.this);
        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (netCheck == false){
            netCheck = Utility.isNetworkAvailable(getApplicationContext());
            Utility.freeMemory();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        }
        else
        {
            new HttpAsyncTaskNotification().execute(Utility.BASE_URL + "CountNewNotification");
        }
        getSupportActionBar().setShowHideAnimationEnabled(false);
//        new HttpAsyncTaskNotification().execute(Utility.BASE_URL+"CountNewNotification");
        // tags.add(App.getSampleConfigs().getUsersTag());

        activityCardsBinding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());

                activityCardsBinding.container.setCurrentItem(tab.getPosition(), false);
                getSupportActionBar().setShowHideAnimationEnabled(false);
                if (tab.getPosition() == 3) {
                    getSupportActionBar().hide();
                }
                int i = tab.getPosition();
                if (i == 0) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (i == 1) {
                    //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else if (i == 2) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (i == 3) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon4b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Profile");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());

                int i = tab.getPosition();
                if (i == 0) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                } else if (i == 1) {
                    //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                } else if (i == 2) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                } else if (i == 3) {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon4);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Profile");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Utility.freeMemory();
            }
        });

        activityCardsBinding.container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Utility.freeMemory();
            }

            @Override
            public void onPageSelected(int position) {
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());

                getSupportActionBar().setShowHideAnimationEnabled(false);
                if (position == 0) {
                    CardsFragment.fragmentCardsBinding.container1.setCurrentItem(nested_position);
                    getSupportActionBar().show();
                    setActionBarTitle("Cards - " + List1Fragment.count + "/"+ Connection_Limit);
                    setActionBarRightImage(R.drawable.ic_drawer);
                    setActionBarRightImagevisible();
                } else if (position == 1) {
                    getSupportActionBar().show();
                    setActionBarTitle("Connect");
                    setActionBarRightImageGone();
                } else if (position == 2) {
                    getSupportActionBar().show();
                    setActionBarTitle("Events");
                    setActionBarRightImage(R.drawable.ic_drawer);
                    setActionBarRightImagevisible();
                } else if (position == 3) {
                    //getSupportActionBar().hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

                TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                }

                showDialog(CardsActivity.this, 0, actionBarHeight);
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());

                int pos = activityCardsBinding.container.getCurrentItem();
                if (pos == 0) {

                } else if (pos == 2) {
                    Intent intent = new Intent(getApplicationContext(), EventsSelectOption.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
            }
        });


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //     hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    public void handleSignInResult(GoogleSignInResult result) {
        //   Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Utility.freeMemory();

        } else {
        }
    }
    public static void getLocation() {
        Utility.freeMemory();

        if (isPermissionGranted) {
            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        }

    }

    private boolean checkPlayServices() {
        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {
        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            // cardCount = db.getActiveNFCCount();

            //  activityCardsBinding.container.setPageTransformer(false, new ZoomOutPageTransformer());
            HashMap<String, String> user = session.getUserDetails();


            UserId = user.get(LoginSession.KEY_USERID);      // name
            profileId = user.get(LoginSession.KEY_PROFILEID);
            User_name = user.get(LoginSession.KEY_NAME);
            Connection_Limit = user.get(LoginSession.KEY_CONNECTION_LIMIT);
            Connection_Left = user.get(LoginSession.KEY_CONNECTION_LEFT);
//        Toast.makeText(getApplicationContext(), name + " " + email + " " + image + " " + gender, Toast.LENGTH_LONG).show();

            try {
                if (Connection_Limit.equalsIgnoreCase("100000")) {
                    Connection_Limit = DecimalFormatSymbols.getInstance().getInfinity();
                }
            }catch (Exception e){

            }
            if (checkPlayServices()) {
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());

                // Building the GoogleApi client
                buildGoogleApiClient();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            setupTabIcons();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Utility.freeMemory();
            getSupportActionBar().setShowHideAnimationEnabled(false);
            if (position == 0) {
                getSupportActionBar().show();
                //  setActionBarTitle("Cards - "+cardCount);
                setActionBarRightImage(R.drawable.ic_drawer);
            } else if (position == 1) {
                getSupportActionBar().show();
                setActionBarTitle("Connect");
                setActionBarRightImage(R.drawable.ic_dehaze_black_24dp);
            } else if (position == 2) {
                getSupportActionBar().show();
                setActionBarTitle("Events");
                setActionBarRightImage(R.drawable.ic_drawer);
            } else if (position == 3) {
                // getSupportActionBar().hide();
            }
            activityCardsBinding.container.setCurrentItem(position, false);
            if (nested_position != 0) {
                CardsFragment.fragmentCardsBinding.container1.setCurrentItem(nested_position);
            }
        }
    }

    public void showDialog(Context context, int x, int y)
    {
        Utility.freeMemory();
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        final Dialog dialog = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.listview_with_text_image);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout lnrMyAccount = (LinearLayout) dialog.findViewById(R.id.lnrMyAcc);
        LinearLayout lnrShare = (LinearLayout) dialog.findViewById(R.id.lnrShare);
        LinearLayout lnrLogout = (LinearLayout) dialog.findViewById(R.id.lnrLogout);
        LinearLayout lnrAddQR = (LinearLayout) dialog.findViewById(R.id.lnrAddQR);
        LinearLayout lnrManageProfile = (LinearLayout) dialog.findViewById(R.id.lnrManageProfile);
        LinearLayout lnrGroup = (LinearLayout) dialog.findViewById(R.id.lnrGroup);
        LinearLayout lnrNotification = (LinearLayout) dialog.findViewById(R.id.lnrNotification);
        LinearLayout lnrRequestNewCard = (LinearLayout) dialog.findViewById(R.id.lnrRequestNewCard);
        LinearLayout lnrContactUs = (LinearLayout) dialog.findViewById(R.id.lnrContactUs);
        LinearLayout lnrSubscription = (LinearLayout) dialog.findViewById(R.id.lnrSubscription);
        LinearLayout lnrHelp = (LinearLayout) dialog.findViewById(R.id.lnrHelp);
        LinearLayout lnrSyncContacts = (LinearLayout) dialog.findViewById(R.id.lnrSyncContacts);
        LinearLayout lnrRewardsPoints = (LinearLayout)dialog.findViewById(R.id.lnrRewardsPoints);
        LinearLayout lnrHistory = (LinearLayout)dialog.findViewById(R.id.lnrHistory);
        LinearLayout lnrCardVerification = (LinearLayout)dialog.findViewById(R.id.lnrCardVerification);
        LinearLayout lnrVPA = (LinearLayout)dialog.findViewById(R.id.lnrVPA);
        txtNotificationCount = (CircularTextView) dialog.findViewById(R.id.txtNotificationCount);

        try
        {
            if (NotificationCount.equals("0"))
            {
                txtNotificationCount.setVisibility(View.GONE);
            }
            else
            {
                txtNotificationCount.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){}

        txtNotificationCount.setText(NotificationCount);
        lnrVPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DialogsActivity.start(CardsActivity.this);
                finish();
*/
                //  Toast.makeText(getApplicationContext(), selectedUsers.toString(), Toast.LENGTH_LONG).show();

                ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                        new QBEntityCallback<QBChatDialog>() {
                            @Override
                            public void onSuccess(QBChatDialog dialog, Bundle args) {
                                        }

                            @Override
                            public void onError(QBResponseException e) {
                                // ProgressDialogFragment.hide(getSupportFragmentManager());
                            }
                        }
                );
            }
        });

        lnrShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional newtorks a step further. Use the code 'S17806DR' for a quick and simple registration!
                String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional newtorks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, User_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });

        lnrSyncContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), ContactsImportActivity.class);
                startActivity(intent);*/
                if (ContextCompat.checkSelfPermission(CardsActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    new MultiContactPicker.Builder(CardsActivity.this) //Activity/fragment context
                            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Option - default: White
                            .handleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .showPickerForResult(CONTACT_PICKER_REQUEST);
                }else{
                    askForContactPermission();
                }
                //dialog.dismiss();
            }
        });

        lnrManageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageMyProfile.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrCardVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CardVerificationActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
//                Intent intent = new Intent(getApplicationContext(), GroupTag.class);
                Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrAddQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddQRActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrRequestNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Help2Activity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrRewardsPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RewardsPointsActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                dialog.dismiss();
                Utility.freeMemory();
            }
        });

        lnrLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CustomProgressDialog("Logout",CardsActivity.this);
                Utility.freeMemory();
                try
                {
                    List1Fragment.allTags.clear();
                    List1Fragment.nfcModel.clear();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                ChatHelper.getInstance().destroy();
                SubscribeService.unSubscribeFromPushes(CardsActivity.this);
                SharedPrefsHelper.getInstance().removeQbUser();
                //  LoginActivity.start(DialogsActivity.this);
                QbDialogHolder.getInstance().clear();

                session.logoutUser();
                try {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //  session.logoutUser();
                                    // dialog.dismiss();
                                }
                            });
                } catch (Exception e) {
                }

                try {
                    mAuth.signOut();
                    Twitter.logOut();
                } catch (Exception e) {
                }

                try {

                    PrefUtils.clearCurrentUser(CardsActivity.this);
                    // We can logout from facebook by calling following method
                    LoginManager.getInstance().logOut();
                    // session.logoutUser();
                } catch (Exception e) {
                }

                LISessionManager.getInstance(getApplicationContext()).clearSession();

            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.x = x;
        lp.y = y;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void askForContactPermission()
    {
        Utility.freeMemory();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(CardsActivity.this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardsActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                }
                else
                {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(CardsActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                new MultiContactPicker.Builder(CardsActivity.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .showPickerForResult(CONTACT_PICKER_REQUEST);
            }
        }
        else
        {
            new MultiContactPicker.Builder(CardsActivity.this) //Activity/fragment context
                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .handleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        Utility.freeMemory();
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode)
        {
            case PERMISSION_REQUEST_CONTACT:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    new MultiContactPicker.Builder(CardsActivity.this) //Activity/fragment context
                            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Option - default: White
                            .handleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(CardsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .showPickerForResult(CONTACT_PICKER_REQUEST);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No permission for contacts", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public static void setActionBarTitle(String title) {
        textView.setText(title);
    }

    public void setActionBarRightImage(int image) {
        imgDrawer.setImageResource(image);
    }

    public void setActionBarRightImageGone() {
        imgDrawer.setVisibility(View.GONE);
    }


    public void setActionBarRightImagevisible() {
        imgDrawer.setVisibility(View.VISIBLE);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            //do nothing here! no call to super.restoreState(arg0, arg1);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                // getSupportActionBar().show();
                // setActionBarTitle("Connect");
                return new CardsFragment();
            } else if (position == 1) {
                //  getSupportActionBar().show();
                // setActionBarTitle("Cards");
                return new CardsFragment();
            } else if (position == 2) {
                // getSupportActionBar().show();
                //setActionBarTitle("Connect");
                return new EventsFragment();
            } else if (position == 3) {
                //  getSupportActionBar().hide();
                //  setActionBarTitle("Events");
                return new ProfileFragment();
            } else {
                //  getSupportActionBar().show();
                //  setActionBarTitle("Cards");
                return new CardsFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

    private void setupTabIcons() {
        View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
        // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
        ImageView imageView = (ImageView) view1.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.ic_icon1b);
        TextView textView = (TextView) view1.findViewById(R.id.txtTab);
        textView.setText("Cards");
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));

        View view2 = getLayoutInflater().inflate(R.layout.tab_view, null);
        //view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon2);
        ImageView imageView1 = (ImageView) view2.findViewById(R.id.icon);
        imageView1.setImageResource(R.drawable.ic_icon2);
        TextView textView1 = (TextView) view2.findViewById(R.id.txtTab);
        textView1.setText("Connect");


        View view3 = getLayoutInflater().inflate(R.layout.tab_view, null);
        // view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon3);
        ImageView imageView2 = (ImageView) view3.findViewById(R.id.icon);
        imageView2.setImageResource(R.drawable.ic_icon3);
        TextView textView2 = (TextView) view3.findViewById(R.id.txtTab);
        textView2.setText("Events");

        View view4 = getLayoutInflater().inflate(R.layout.tab_view, null);
        //  view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon4);
        ImageView imageView3 = (ImageView) view4.findViewById(R.id.icon);
        imageView3.setImageResource(R.drawable.ic_icon4);
        TextView textView3 = (TextView) view4.findViewById(R.id.txtTab);
        textView3.setText("Profile");
        activityCardsBinding.tabs.getTabAt(0).setCustomView(view1);
        activityCardsBinding.tabs.getTabAt(1).setCustomView(view2);
        activityCardsBinding.tabs.getTabAt(2).setCustomView(view3);
        activityCardsBinding.tabs.getTabAt(3).setCustomView(view4);
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

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    public static final byte[] MIME_TEXT1 = "application/com.amplearch.circleone".getBytes();

    @Override
    public void onResume() {
        super.onResume();



        checkPlayServices();

        if (activityCardsBinding.container.getCurrentItem() == 2) {
            setActionBarTitle("Events");
        }

        if (activityCardsBinding.container.getCurrentItem() == 3) {
            HashMap<String, String> user = session.getUserDetails();
            ProfileFragment.UserID = user.get(LoginSession.KEY_USERID);

            HashMap<String, String> profile = ProfileFragment.profileSession.getProfileDetails();
            ProfileFragment.profileIndex = Integer.parseInt(profile.get(ProfileSession.KEY_PROFILE_INDEX));

            ProfileFragment.callMyProfile();
        }

        getSupportActionBar().setShowHideAnimationEnabled(false);
        if (!done) {
            NdefMessage[] msgs = null;

            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (rawMsgs != null) {
                    try {
                        arrayNFC = new ArrayList<>();
                        for (int i = 0; i < rawMsgs.length; i++) {
                            NdefRecord[] recs = ((NdefMessage) rawMsgs[i]).getRecords();
                            for (int j = 0; j < recs.length; j++) {
                                if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA &&
                                        Arrays.equals(recs[j].getType(), MIME_TEXT)) {

                                    byte[] payload = recs[j].getPayload();
                                    String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                                    int langCodeLen = payload[0] & 0077;

                                    /*s += ("\n" +
                                            new String(payload, langCodeLen + 1,
                                                    payload.length - langCodeLen - 1, textEncoding) );
*/
                                    String s1 = new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding);
                                    String decryptstr = decrypt(s1, secretKey);
                                    arrayNFC.add(decryptstr);
                                } else if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA && Arrays.equals(recs[j].getType(), MIME_TEXT1)) {

                                    byte[] payload = recs[j].getPayload();
                                    String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                                    int langCodeLen = payload[0] & 0077;

                                   /* s += ("\n" +
                                            new String(payload, langCodeLen + 1,
                                                    payload.length - langCodeLen - 1, textEncoding) );*/
                                    String s1 = new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding);
                                    String decryptstr = decrypt(s1, secretKey);
                                    arrayNFC.add(decryptstr);
                                } else {
                                    try {
                                        msgs = new NdefMessage[rawMsgs.length];
                                        for (int i1 = 0; i1 < rawMsgs.length; i1++) {
                                            msgs[i1] = (NdefMessage) rawMsgs[i1];
                                        }

                                        byte[] payload = msgs[0].getRecords()[0].getPayload();

                                        String message = new String(payload);
                /* 把tag的資訊放到textview裡面 */
                                        // mEtMessage.setText(new String(payload));

                                        message = message.substring(1, message.length());

                                        String decryptstr = decrypt(message, secretKey);
                                        arrayNFC.add(decryptstr);

                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TagDispatch", e.toString());
                    }

                }
                String ProfileId = "", card_code = "";

                if (netCheck == false) {
                    netCheck = Utility.isNetworkAvailable(getApplicationContext());
                    Utility.freeMemory();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), arrayNFC.toString(), Toast.LENGTH_LONG).show();
                    if (arrayNFC.size() == 1) {
                        nfcProfileId = arrayNFC.get(0).toString();
                        //  txtNoGroup.setText("Your Card is already verified..");
                        //  ivAddCard.setVisibility(View.GONE);
                        if (mLastLocation != null) {
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                        } else {
                            lat = "";
                            lng = "";
                            getLocation();
                            Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                        }


                        try {
                            new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else if (arrayNFC.size() == 2) {
                        nfcProfileId = arrayNFC.get(0).toString();
                        CardCode = arrayNFC.get(1).toString();

                        if (mLastLocation != null) {
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                        } else {
                            lat = "";
                            lng = "";
                            getLocation();
                            Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                        }


                        try {
                            new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                        // Toast.makeText(getApplicationContext(), ProfileId + " " + CardCode, Toast.LENGTH_LONG).show();
                        // new HttpAsyncActivateNFC().execute(Utility.BASE_URL+"NFCSecurity/ActivateNFC");
                    } else {
                        nfcProfileId = "";
                        Toast.makeText(getApplicationContext(), "Please use only CircleOne NFC-Card for unlock", Toast.LENGTH_LONG).show();
                        //txtNoGroup.setText("Your Card is already verified..");
                        //ivAddCard.setVisibility(View.GONE);
                    }
                }

            }

            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            if (mNfcAdapter != null)
                mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);


            if (activityCardsBinding.tabs.getSelectedTabPosition() == 3) {
                CardsActivity.setActionBarTitle("Profile");
                //ProfileFragment.callMyProfile();
            } else if (activityCardsBinding.tabs.getSelectedTabPosition() == 2) {
                CardsActivity.setActionBarTitle("Events");
            } else if (activityCardsBinding.tabs.getSelectedTabPosition() == 1) {
                CardsActivity.setActionBarTitle("Connect");
            }
        }
    }


    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);

        getSupportActionBar().setShowHideAnimationEnabled(false);


        String action = paramIntent.getAction();
        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";
        Parcelable[] data = paramIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                arrayNFC = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA && Arrays.equals(recs[j].getType(), MIME_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n" +
                                    new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding) );
                            String s1 = new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding);
                            String decryptstr = decrypt(s1, secretKey);
                            arrayNFC.add(decryptstr);
                        }
                        else if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA && Arrays.equals(recs[j].getType(), MIME_TEXT1)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n" +
                                    new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding) );
                            String s1 = new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding);
                            String decryptstr = decrypt(s1, secretKey);
                            arrayNFC.add(decryptstr);
                        }
                        else {
                            try {


                                NdefMessage[] msgs = null;
                                msgs = new NdefMessage[data.length];
                                for (int i1 = 0; i1 < data.length; i1++) {
                                    msgs[i1] = (NdefMessage) data[i1];
                                }

                                byte[] payload = msgs[0].getRecords()[0].getPayload();

                                String message = new String(payload);

                                // mEtMessage.setText(new String(payload));

                                message = message.substring(1, message.length());

                                String decryptstr = decrypt(message, secretKey);
                                arrayNFC.add(decryptstr);

                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }
        String ProfileId = "", card_code = "";

        if (netCheck == false){
            netCheck = Utility.isNetworkAvailable(getApplicationContext());
            Utility.freeMemory();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        }
        else {

            //Toast.makeText(getApplicationContext(), arrayNFC.toString(), Toast.LENGTH_LONG).show();
            if (arrayNFC.size() == 1) {
                nfcProfileId = arrayNFC.get(0).toString();


                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                } else {
                    lat = "";
                    lng = "";
                    getLocation();
                    Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(getApplicationContext(), String.valueOf(latitude + " " + longitude), Toast.LENGTH_LONG).show();

                try {
                    new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (arrayNFC.size() == 2) {
                nfcProfileId = arrayNFC.get(0).toString();
                CardCode = arrayNFC.get(1).toString();


                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                } else {
                    lat = "";
                    lng = "";
                    getLocation();
                    Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(getApplicationContext(), String.valueOf(latitude + " " + longitude), Toast.LENGTH_LONG).show();

                try {
                    new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getApplicationContext(), ProfileId + " " + CardCode, Toast.LENGTH_LONG).show();
                // new HttpAsyncActivateNFC().execute(Utility.BASE_URL+"NFCSecurity/ActivateNFC");
            } else {
                nfcProfileId = "";
                Toast.makeText(getApplicationContext(), "Please use only CircleOne NFC-Card for unlock", Toast.LENGTH_LONG).show();
                //txtNoGroup.setText("Your Card is already verified..");
                //ivAddCard.setVisibility(View.GONE);
            }
        }
    }
    public static String decrypt(String value, String key)
            throws GeneralSecurityException, IOException {
        byte[] value_bytes = Base64.decode(value, 0);
        byte[] key_bytes = getKeyBytes(key);
        return new String(decrypt(value_bytes, key_bytes, key_bytes), "UTF-8");
    }

    public static byte[] decrypt(byte[] ArrayOfByte1, byte[] ArrayOfByte2, byte[] ArrayOfByte3)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // setup AES cipher in CBC mode with PKCS #5 padding
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // decrypt
        localCipher.init(2, new SecretKeySpec(ArrayOfByte2, "AES"), new IvParameterSpec(ArrayOfByte3));
        return localCipher.doFinal(ArrayOfByte1);
    }

    private static byte[] getKeyBytes(String paramString)
            throws UnsupportedEncodingException {
        byte[] arrayOfByte1 = new byte[16];
        byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
        return arrayOfByte1;
    }

    private class HttpAsyncTaskNotification extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CardsActivity.this);
            dialog.setMessage("Adding records...");
            //dialog.setTitle("Saving Reminder");
            //   dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("userid", UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //  dialog.dismiss();
            //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String Count = response.getString("Count");

                    if (success.equals("1"))
                    {
                        NotificationCount = Count;
                        if (NotificationCount.equals("0"))
                        {
                            txtNotificationCountAction.setVisibility(View.GONE);
                        }
                        else
                        {
                            txtNotificationCountAction.setVisibility(View.VISIBLE);
                        }
                        txtNotificationCountAction.setText(NotificationCount);
                    }
                    else
                    {
                        txtNotificationCountAction.setVisibility(View.GONE);
                        NotificationCount = "0";
                        txtNotificationCountAction.setText(NotificationCount);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CardsActivity.this);
            dialog.setMessage("Adding records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            // Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_LONG).show();
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Latitude", lat);
                jsonObject.accumulate("Location", "");
                jsonObject.accumulate("Longitude", lng);
                jsonObject.accumulate("Operation", "Request");
                jsonObject.accumulate("RequestType", "NFC");
                jsonObject.accumulate("connection_date", formattedDate);
                jsonObject.accumulate("friendProfileId", nfcProfileId);
                jsonObject.accumulate("myProfileId", profileId);
                if (!CardCode.equals("")){
                    jsonObject.accumulate("card_code", CardCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Utility.freeMemory();
            //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result == "") {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_request_sent), Toast.LENGTH_LONG).show();

                        List1Fragment.webCall();
                        List2Fragment.webCall();
                                                List4Fragment.webCall();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Utility.freeMemory();
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
