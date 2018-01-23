package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Fragments.CardsFragment;
import com.circle8.circleOne.Fragments.ConnectFragment;
import com.circle8.circleOne.Fragments.DashboardFragment;
import com.circle8.circleOne.Fragments.EventsFragment;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List4Fragment;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Fragments.SortFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.LocationUtil.PermissionUtils;
import com.circle8.circleOne.MultiContactPicker;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.PrefUtils;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.chat.ChatHelper;
import com.circle8.circleOne.chat.qb.QbDialogHolder;
import com.circle8.circleOne.databinding.ActivityDashboardBinding;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.twitter.sdk.android.Twitter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import static com.circle8.circleOne.Activity.CardsActivity.Connection_Limit;
import static com.circle8.circleOne.Activity.CardsActivity.MIME_TEXT;
import static com.circle8.circleOne.Activity.CardsActivity.decrypt;
import static com.circle8.circleOne.Fragments.DashboardFragment.secretKey;
import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class DashboardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    ActivityCompat.OnRequestPermissionsResultCallback,
    PermissionUtils.PermissionResultCallback{

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int PERMISSION_REQUEST_CONTACT = 111;

    public static ActivityDashboardBinding activityDashboardBinding;

    public static GoogleApiClient mGoogleApiClient;
    LoginSession session;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    public static boolean isPermissionGranted;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    public static Location mLastLocation;
    static TextView textView;
    static ImageView imgDrawer, imgLogo;
    public static String NotificationCount = "0", UserId= "";
    private boolean netCheck = false;
    private NfcAdapter mNfcAdapter;
    public static final byte[] MIME_TEXT1 = "application/com.amplearch.circleone".getBytes();
    boolean done = false;
    ArrayList<String> arrayNFC  = new ArrayList<>();
    String profileId = "", nfcProfileId = "";
    public double latitude;
    public double longitude;
    String lat = "", lng = "";
    String CardCode = "";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDashboardBinding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);

       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
        textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgDrawer.setVisibility(View.INVISIBLE);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logo_white);
        getSupportActionBar().setHomeAsUpIndicator(null);

        //  toggle.setHomeAsUpIndicator(R.id.icon);//add this for custom icon
        fragment = new DashboardFragment();
        Pref.setValue(DashboardActivity.this, "current_frag", "1");

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                .addToBackStack(null).commit();
        netCheck = Utility.isNetworkAvailable(getApplicationContext());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*View header = navigationView.inflateHeaderView(R.layout.nav_header_music);
        TextView profileName = (TextView) header.findViewById(R.id.profile_name);
        profileName.setText("Adele");*/
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        session = new LoginSession(getApplicationContext());
        textView.setText("Dashboard");

        HashMap<String, String> user = session.getUserDetails();

        CardsActivity.Connection_Limit = user.get(LoginSession.KEY_CONNECTION_LIMIT);
        profileId = user.get(LoginSession.KEY_PROFILEID);

        try {
            if (CardsActivity.Connection_Limit.equalsIgnoreCase("100000")) {
                //CardsActivity.Connection_Limit = DecimalFormatSymbols.getInstance().getInfinity();
            }
        }catch (Exception e){

        }

        UserId = user.get(LoginSession.KEY_USERID);      // name

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_my_acc) {
                    Intent intent = new Intent(DashboardActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sync_contact) {
                    if (ContextCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        new MultiContactPicker.Builder(DashboardActivity.this) //Activity/fragment context
                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                .hideScrollbar(false) //Optional - default: false
                                .showTrack(true) //Optional - default: true
                                .searchIconColor(Color.WHITE) //Option - default: White
                                .handleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                .bubbleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                    }else{
                        askForContactPermission();
                    }
                } else if (id == R.id.nav_manage_profile) {
                    Intent intent = new Intent(DashboardActivity.this, ManageMyProfile.class);
                    startActivity(intent);
                } else if (id == R.id.nav_card_request) {
                    Intent intent = new Intent(DashboardActivity.this, NewCardRequestActivity.class);
                    startActivity(intent);
                }else if (id == R.id.nav_connect) {
                    Intent intent = new Intent(DashboardActivity.this, ConnectFragment.class);
                    startActivity(intent);
                }else if(id == R.id.nav_events)
                {
                    Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_circle)
                {
                    Intent intent = new Intent(DashboardActivity.this, GroupsActivity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_history)
                {
                    Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_subscription)
                {
                    Intent intent = new Intent(DashboardActivity.this, SubscriptionActivity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_help)
                {
                    Intent intent = new Intent(DashboardActivity.this, Help2Activity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_contact_us)
                {
                    Intent intent = new Intent(DashboardActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                }else if(id == R.id.nav_logout)
                {
                    CustomProgressDialog("Logout",DashboardActivity.this);

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
                    SubscribeService.unSubscribeFromPushes(DashboardActivity.this);
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

                        PrefUtils.clearCurrentUser(DashboardActivity.this);
                        // We can logout from facebook by calling following method
                        LoginManager.getInstance().logOut();
                        // session.logoutUser();
                    } catch (Exception e) {
                    }

                    LISessionManager.getInstance(getApplicationContext()).clearSession();

                }



                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        initClick();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if (checkPlayServices()) {
            Utility.freeMemory();
            Utility.deleteCache(getApplicationContext());

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFragment() instanceof CardsFragment){
                    /*Intent intent = new Intent(getApplicationContext(), SortAndFilterOption.class);
                    startActivity(intent);*/
                    Pref.setValue(DashboardActivity.this, "current_frag", "4");

                    fragment = new SortFragment();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down);
                    transaction.replace(R.id.main_container_wrapper, fragment).addToBackStack(null).commit();
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);*/
                    setActionBarTitle("Sort & Filter", false);

                }
                else if (getCurrentFragment() instanceof SortFragment){
                    Pref.setValue(DashboardActivity.this, "current_frag", "1");

                    fragment = new CardsFragment();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_out_down, R.anim.slide_out_down);
                    transaction.replace(R.id.main_container_wrapper, fragment).addToBackStack(null).commit();
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);*/
                    //setActionBarTitle("Sort & Filter", false);

                }
            }
        });

        if (netCheck == false){
            netCheck = Utility.isNetworkAvailable(getApplicationContext());
            Utility.freeMemory();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        }
        else
        {
            new HttpAsyncTaskNotification().execute(Utility.BASE_URL + "CountNewNotification");
        }

       /* if (getCurrentFragment() instanceof SortAndFilterOption){
                    *//*Intent intent = new Intent(getApplicationContext(), SortAndFilterOption.class);
                    startActivity(intent);*//*

            setActionBarTitle("Sort & Filter");

        }*/


        if (getCurrentFragment() instanceof Notification){
            setActionBarTitle("Notification - 0", false);

            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.dashboard);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }
        }
        else if (getCurrentFragment() instanceof CardsFragment){
            setActionBarTitle("Cards - 0 / " + Connection_Limit, false);

            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.dashboard);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }

        }
        else if (getCurrentFragment() instanceof SortFragment){
            setActionBarTitle("Sort & Filter", false);

            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }

        }
    }

    public static void setActionBarTitle(String title, Boolean infinity) {
        textView.setText(title);

        if (infinity == true){
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_infinity, 0);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }
    }

    public static void setDrawerVisibility(Boolean visibility) {
        if (visibility == true){
            imgDrawer.setVisibility(View.VISIBLE);
        }else {
            imgDrawer.setVisibility(View.INVISIBLE);

        }
    }

    public static void setLogoVisibility(Boolean visibility) {
        if (visibility == true){
            imgLogo.setVisibility(View.VISIBLE);
        }else {
            imgLogo.setVisibility(View.INVISIBLE);

        }
    }

    public static void changeLogo(int icon) {
        imgLogo.setImageResource(icon);
    }

    public Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container_wrapper);
        //    Log.e("currentFragment",""+currentFragment);
        return currentFragment;

    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
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


    protected synchronized void buildGoogleApiClient() {
        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());

       /* mGoogleApiClient = new GoogleApiClient.Builder(CardsActivity.this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .build();*/


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
                            status.startResolutionForResult(DashboardActivity.this, REQUEST_CHECK_SETTINGS);

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


    @Override
    protected void onStart() {
        super.onStart();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                // Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                //  showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        //     hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
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

        String action = paramIntent.getAction();
        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        // parse through all NDEF messages and their records and pick text type only
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
                  //  Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(getApplicationContext(), String.valueOf(latitude + " " + longitude), Toast.LENGTH_LONG).show();

                try {
                    new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                //  txtNoGroup.setText("Your Card is already verified..");
                //  ivAddCard.setVisibility(View.GONE);
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
                   // Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();

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
                          //  Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
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
                           // Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
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

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            // Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_LONG).show();
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DashboardActivity.this);
            dialog.setMessage("Adding records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
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
                        Pref.setValue(DashboardActivity.this, "current_frag", "1");
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_request_sent), Toast.LENGTH_LONG).show();
                        fragment = new CardsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                                .addToBackStack(null)
                                .commit();

                        activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
                        activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                        activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                        activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
                        activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                        activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
                        if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                            DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                        }else {
                            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                            DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void handleSignInResult(GoogleSignInResult result) {
        //   Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Utility.freeMemory();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //  Log.e(TAG, "display name: " + acct.getDisplayName());

            //  String personName = acct.getDisplayName();
            //    String personPhotoUrl = acct.getPhotoUrl().toString();
            // String email = acct.getEmail();

            //  Log.e(TAG, "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl);

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }

    private void initClick() {

        activityDashboardBinding.includefooter.rlCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
                if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                }else {
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                }





                fragment = new CardsFragment();
                Pref.setValue(DashboardActivity.this, "current_frag", "1");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        activityDashboardBinding.includefooter.rlDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.dashboard);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
                if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                }else {
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                }
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);

                Pref.setValue(DashboardActivity.this, "current_frag", "2");

                fragment = new DashboardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null).commit();
                setActionBarTitle("Dashboard", false);
            }
        });
        activityDashboardBinding.includefooter.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4b);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                }else {
                    activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                    DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                }
                Pref.setValue(DashboardActivity.this, "current_frag", "3");
                fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null).commit();

            }
        });

    }

    public String POST1(String url) {
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
            jsonObject.accumulate("userid", UserId);
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


    private class HttpAsyncTaskNotification extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DashboardActivity.this);
            dialog.setMessage("Adding records...");
            //dialog.setTitle("Saving Reminder");
            //   dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //  dialog.dismiss();
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
                        //NotificationCount = "2";
                       // Toast.makeText(getApplicationContext(), NotificationCount, Toast.LENGTH_LONG).show();
                        if (NotificationCount.equals("0")) {
                            DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);

                        }
                        else {
                            DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);
                            DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setText(DashboardActivity.NotificationCount);
                            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                            activityDashboardBinding.includefooter.txtNotificationCountAction.setText(NotificationCount);
                            if (getCurrentFragment() instanceof DashboardFragment){
                                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);

                            }

                        }
                        activityDashboardBinding.includefooter.txtNotificationCountAction.setText(NotificationCount);

                    }
                    else
                    {
                        activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                        NotificationCount = "0";
                        activityDashboardBinding.includefooter.txtNotificationCountAction.setText(NotificationCount);
                        DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                      //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getCurrentFragment() instanceof DashboardFragment){
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
        else if (getCurrentFragment() instanceof CardsFragment){
            Pref.setValue(DashboardActivity.this, "current_frag", "2");

            fragment = new DashboardFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container_wrapper, fragment).addToBackStack(null).commit();
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);*/
            setActionBarTitle("Dashboard", false);

            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.dashboard);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }
            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);


        }
        else if (getCurrentFragment() instanceof SortFragment){
            Pref.setValue(DashboardActivity.this, "current_frag", "1");

            fragment = new CardsFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container_wrapper, fragment).addToBackStack(null).commit();


            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }

                    /*getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);*/
          //  setActionBarTitle("Dashboard", false);

        }
        else if (getCurrentFragment() instanceof ProfileFragment){
            Pref.setValue(DashboardActivity.this, "current_frag", "2");

            fragment = new DashboardFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container_wrapper, fragment).addToBackStack(null).commit();
                    /*getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);*/
              setActionBarTitle("Dashboard", false);

            activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
            activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.dashboard);
            activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
            activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
            activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
            if (activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
            }else {
                activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

            }
            activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);

        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
    public void askForContactPermission()
    {
        Utility.freeMemory();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
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
                    ActivityCompat.requestPermissions(DashboardActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                new MultiContactPicker.Builder(DashboardActivity.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .showPickerForResult(CONTACT_PICKER_REQUEST);
            }
        }
        else
        {
            new MultiContactPicker.Builder(DashboardActivity.this) //Activity/fragment context
                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .handleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}