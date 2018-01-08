package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.circle8.circleOne.Fragments.CardsFragment;
import com.circle8.circleOne.Fragments.ConnectFragment;
import com.circle8.circleOne.Fragments.DashboardFragment;
import com.circle8.circleOne.Fragments.EventsFragment;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.LocationUtil.PermissionUtils;
import com.circle8.circleOne.MultiContactPicker;
import com.circle8.circleOne.R;
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

import java.util.ArrayList;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;

public class DashboardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    ActivityCompat.OnRequestPermissionsResultCallback,
    PermissionUtils.PermissionResultCallback{

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int PERMISSION_REQUEST_CONTACT = 111;

    ActivityDashboardBinding activityDashboardBinding;

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        //  toggle.setHomeAsUpIndicator(R.id.icon);//add this for custom icon
        fragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                .addToBackStack(null).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*View header = navigationView.inflateHeaderView(R.layout.nav_header_music);
        TextView profileName = (TextView) header.findViewById(R.id.profile_name);
        profileName.setText("Adele");*/

        session = new LoginSession(getApplicationContext());

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

        activityDashboardBinding.includefooter.imgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));

                fragment = new CardsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        activityDashboardBinding.includefooter.imgDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard3x);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));

                fragment = new DashboardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null).commit();
            }
        });
        activityDashboardBinding.includefooter.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1);
                activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4b);
                activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.colorPrimary));

                fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null).commit();

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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