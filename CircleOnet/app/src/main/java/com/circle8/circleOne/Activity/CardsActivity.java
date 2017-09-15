package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
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
import com.circle8.circleOne.Fragments.ConnectFragment;
import com.circle8.circleOne.Fragments.EventsFragment;
import com.circle8.circleOne.Fragments.ProfileFragment;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Utils.CustomViewPager;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.PrefUtils;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linkedin.platform.LISessionManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import io.fabric.sdk.android.Fabric;

public class CardsActivity extends NfcActivity implements GoogleApiClient.OnConnectionFailedListener
{
    public static CustomViewPager mViewPager;
    TabLayout tabLayout;
    ImageView imgDrawer, imgLogo;
    private int actionBarHeight;
    static TextView textView;
    public static int position = 0, nested_position = 0;
    DatabaseHelper db;
    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    private Date location;
    private int currentPage;
    int cardCount = 0 ;
    private NfcAdapter mNfcAdapter;
    Tag tag;
    boolean done = false;
    public static GoogleApiClient mGoogleApiClient;
    LoginSession session;
    private FirebaseAuth mAuth;
    String profileId = "", nfcProfileId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_cards);
        /*SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss a");
        String date1 = format.format(Date.parse(stringDate));

        Toast.makeText(getApplicationContext(), "Time: " + date1, Toast.LENGTH_LONG).show();   */

        mAuth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            position = extras.getInt("viewpager_position");
            nested_position = extras.getInt("nested_viewpager_position");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();


        String name = user.get(LoginSession.KEY_NAME);      // name
        profileId = user.get(LoginSession.KEY_PROFILEID);
        String email = user.get(LoginSession.KEY_EMAIL);    // email
        String image = user.get(LoginSession.KEY_IMAGE);
        String gender = user.get(LoginSession.KEY_GENDER);
        Toast.makeText(getApplicationContext(), name + " " + email + " " + image + " " + gender, Toast.LENGTH_LONG).show();


        mGoogleApiClient = new GoogleApiClient.Builder(CardsActivity.this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .build();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);

        if (mNfcAdapter == null || mNfcAdapter.isEnabled()== false) {
            // adapter exists and is enabled.
            //txtMessage.setVisibility(View.VISIBLE);
        }
        else {
           // txtMessage.setVisibility(View.GONE);
            // handleIntent(getIntent());
        }
        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       // mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
       /* mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

            }
        });*/
        getSupportActionBar().setShowHideAnimationEnabled(false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                mViewPager.setCurrentItem(tab.getPosition(), false);
                getSupportActionBar().setShowHideAnimationEnabled(false);
                if (tab.getPosition() == 3){
                    getSupportActionBar().hide();
                }
                int  i = tab.getPosition();
                if ( i == 0 )
                {
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else if ( i == 1 ){
                 //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));

                }
                else if ( i == 2 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else if ( i == 3 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon4b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Profile");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int  i = tab.getPosition();
                if ( i == 0 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
                else if ( i == 1 ){
                    //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);

                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.unselected));

                }
                else if ( i == 2 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
                else if ( i == 3 ){
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

            }
        });

               // createTabIcons();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setShowHideAnimationEnabled(false);
                if (position == 0) {
                    CardsFragment.mViewPager.setCurrentItem(nested_position);
                    getSupportActionBar().show();
                    setActionBarTitle("Cards - "+cardCount);
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

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

                TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }

                showDialog(CardsActivity.this, 0, actionBarHeight);
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();
                if (pos == 0) {
                    Intent intent = new Intent(getApplicationContext(), SortAndFilterOption.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
                else if (pos == 2) {
                    Intent intent = new Intent(getApplicationContext(), EventsSelectOption.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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



    private class LoadDataForActivity extends AsyncTask<Void, Void, Void>
    {
        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute()
        {
            db = new DatabaseHelper(getApplicationContext());
           /* List<NFCModel> allTags = db.getAllNFC();
            for (NFCModel tag : allTags) {
                Log.d("StoreLocation Name", tag.getCard_front().toString());
                // Toast.makeText(getApplicationContext(), tag.getName() + " " + tag.getCard_front().toString() + " " + tag.getActive(), Toast.LENGTH_LONG).show();

            }*/

           // db.getAllNFC();
            //SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            //- db.onCreate(sqLiteDatabase);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            final ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar);
            getSupportActionBar().setShowHideAnimationEnabled(false);
            textView = (TextView) findViewById(R.id.mytext);
           // cardCount = db.getActiveNFCCount();
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // textView.setText("Cards 256");
            // Set up the ViewPager with the sections adapter.
            mViewPager = (CustomViewPager) findViewById(R.id.container);
            mViewPager.setOffscreenPageLimit(4);
            imgDrawer = (ImageView) findViewById(R.id.drawer);
            imgLogo = (ImageView) findViewById(R.id.imgLogo);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setPagingEnabled(false);
          //  mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        }
        @Override
        protected Void doInBackground(Void... params) {
            setupTabIcons();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
            mViewPager.setCurrentItem(position, false);
            if (nested_position != 0) {
                CardsFragment.mViewPager.setCurrentItem(nested_position);
            }
        }

    }

   /* private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab1, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab2, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab3, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabThree1 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabThree1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab4, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabThree1);
    }*/

    public void showDialog(Context context, int x, int y)
    {
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        final Dialog dialog  = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.listview_with_text_image);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout lnrMyAccount = (LinearLayout)dialog.findViewById(R.id.lnrMyAcc);
        LinearLayout lnrLogout = (LinearLayout) dialog.findViewById(R.id.lnrLogout);
        LinearLayout lnrAddQR = (LinearLayout) dialog.findViewById(R.id.lnrAddQR);
        LinearLayout lnrManageProfile = (LinearLayout) dialog.findViewById(R.id.lnrManageProfile);
        LinearLayout lnrGroup = (LinearLayout) dialog.findViewById(R.id.lnrGroup);
        LinearLayout lnrNotification = (LinearLayout) dialog.findViewById(R.id.lnrNotification);
        LinearLayout lnrRequestNewCard = (LinearLayout)dialog.findViewById(R.id.lnrRequestNewCard);
        LinearLayout lnrContactUs = (LinearLayout)dialog.findViewById(R.id.lnrContactUs);
        LinearLayout lnrSubscription = (LinearLayout)dialog.findViewById(R.id.lnrSubscription);
        LinearLayout lnrHelp = (LinearLayout)dialog.findViewById(R.id.lnrHelp);
        LinearLayout lnrSyncContacts = (LinearLayout) dialog.findViewById(R.id.lnrSyncContacts);

        lnrSyncContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactsImportActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrManageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageMyProfile.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), GroupTag.class);
                Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrAddQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddQRActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrRequestNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Help2Activity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        lnrLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    session.logoutUser();
                                   // dialog.dismiss();
                                }
                            });
                }
                catch (Exception e){}


                try {
                    mAuth.signOut();
                    Twitter.logOut();
                    session.logoutUser();
                }catch (Exception e){}

                try {

                    PrefUtils.clearCurrentUser(CardsActivity.this);
                    // We can logout from facebook by calling following method
                    LoginManager.getInstance().logOut();
                    session.logoutUser();
                }catch (Exception e){}

                LISessionManager.getInstance(getApplicationContext()).clearSession();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
                /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();*/
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

    public static void setActionBarTitle(String title){
        textView.setText(title);
    }

    public void setActionBarRightImage(int image){
        imgDrawer.setImageResource(image);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
                return new ConnectFragment();
            }
            else if (position == 2) {
               // getSupportActionBar().show();
                //setActionBarTitle("Connect");
                return new EventsFragment();
            }
            else if (position == 3) {
              //  getSupportActionBar().hide();
              //  setActionBarTitle("Events");
                return new ProfileFragment();
            }
            else {
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
            /*switch (position) {
                case 0:
                    return getString(R.string.app_name);
                case 1:
                    return getString(R.string.hello_blank_fragment);
            }*/
            return null;
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.tab1,
                R.drawable.tab2,
                R.drawable.tab3,
                R.drawable.tab4
               // R.drawable.ic_tab_contacts
        };

        /*View view = getLayoutInflater().inflate(R.layout.tab_view, null);
        for (int i = 0; i < 3; i++) {
            view.findViewById(R.id.icon).setBackgroundResource(tabIcons[i]);
            tabLayout.addTab(tabLayout.newTab().setCustomView(view));
        }
*/
       /* tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);*/


        View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
       // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
        ImageView imageView = (ImageView) view1.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.ic_icon1b);
        TextView textView = (TextView) view1.findViewById(R.id.txtTab);
        textView.setText("Cards");
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view1));


        View view2 = getLayoutInflater().inflate(R.layout.tab_view, null);
        //view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon2);
        ImageView imageView1 = (ImageView) view2.findViewById(R.id.icon);
        imageView1.setImageResource(R.drawable.ic_icon2);
        TextView textView1 = (TextView) view2.findViewById(R.id.txtTab);
        textView1.setText("Connect");
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view2));


        View view3 = getLayoutInflater().inflate(R.layout.tab_view, null);
       // view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon3);
        ImageView imageView2 = (ImageView) view3.findViewById(R.id.icon);
        imageView2.setImageResource(R.drawable.ic_icon3);
        TextView textView2 = (TextView) view3.findViewById(R.id.txtTab);
        textView2.setText("Events");
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.tab_view, null);
      //  view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon4);
        ImageView imageView3 = (ImageView) view4.findViewById(R.id.icon);
        imageView3.setImageResource(R.drawable.ic_icon4);
        TextView textView3 = (TextView) view4.findViewById(R.id.txtTab);
        textView3.setText("Profile");
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);
        tabLayout.getTabAt(3).setCustomView(view4);
    }

   /* class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }*/

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
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().setShowHideAnimationEnabled(false);
        if (!done) {
            NdefMessage[] msgs = null;

            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMsgs != null) {
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }

                    byte[] payload = msgs[0].getRecords()[0].getPayload();

                    String message = new String(payload);
                /* 把tag的資訊放到textview裡面 */
                    // mEtMessage.setText(new String(payload));
                    done = true;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    message = message.substring(1, message.length());

                    nfcProfileId = message;
                    try {
                        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/FriendConnection_Operation");
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    /*Boolean aBoolean = db.verification(message);
                    if (aBoolean == true) {
                        Toast.makeText(getApplicationContext(), "Already Activated", Toast.LENGTH_LONG).show();
                    } else {
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = format.format(Date.parse(stringDate));

                        int i = db.makeCardActive(message, date1);
                        if (i == 1) {
                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
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
                                List4Fragment.gridAdapter.notifyDataSetChanged();
                                List4Fragment.allTags = db.getActiveNFC();
                                List4Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List4Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }

                            try {
                                //List1Fragment.myPager.notifyDataSetChanged();
                              //  List1Fragment.allTags = db.getActiveNFC();
                                List1Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List1Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }
                        } else if (i == 11) {
                            Toast.makeText(getApplicationContext(), "Data Does not exists in Database..", Toast.LENGTH_LONG).show();
                        }*/
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

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent
     *         containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);

        getSupportActionBar().setShowHideAnimationEnabled(false);
        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag == null){
            Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
            //textViewInfo.setText("tag == null");
        }else {
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
            /*Boolean aBoolean = db.verification(id);
            if (aBoolean == true)
            {
                Toast.makeText(getApplicationContext(), "already exists in database", Toast.LENGTH_LONG).show();
            }else {

                int i = db.makeCardActive(id);
                if (i == 1){
                    Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_LONG).show();
                }
              //  Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
               // notifyAll();

                if (mViewPager.getCurrentItem() == 0){
                    Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                    // you pass the position you want the viewpager to show in the extra,
                    // please don't forget to define and initialize the position variable
                    // properly
                    go.putExtra("viewpager_position", 0);
                    go.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());

                    startActivity(go);
                    finish();
                    //Toast.makeText(getApplicationContext(), String.valueOf(CardsFragment.mViewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                    startActivity(go);
                    finish();
                }
            }*/

               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


           /* List<NFCModel> modelList = db.getNFCbyTag(id);
            image = new ArrayList<>();
            try {

                if (modelList != null){

                    for (NFCModel tag1 : modelList) {
                        // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                        Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                        txtName.setText(tag1.getName());
                        txtCompany.setText(tag1.getCompany());
                        txtWebsite.setText(tag1.getWebsite());
                        txtEmail.setText(tag1.getEmail());
                        txtPH.setText(tag1.getPh_no());
                        txtWork.setText(tag1.getWork_no());
                        txtMob.setText(tag1.getMob_no());
                        txtAddress.setText(tag1.getAddress());
                        txtRemark.setText(tag1.getRemark());
                        image.add(tag1.getCard_front());
                        image.add(tag1.getCard_back());
                        myPager = new CardSwipe(getApplicationContext(), image);
                        mViewPager.setClipChildren(false);
                        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                        mViewPager.setOffscreenPageLimit(3);
                        mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                        mViewPager.setAdapter(myPager);
                    }
                }

            }catch (Exception e){

            }*/

         //   Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            // callData(id);
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values()) {
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                nfcProfileId = data;
                try {
                    new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/FriendConnection_Operation");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                /*Boolean aBoolean = db.verification(data);
                if (aBoolean == true)
                {
                    Toast.makeText(getApplicationContext(), "Already Activated", Toast.LENGTH_LONG).show();
                }else {
                    Date date = new Date();
                    String stringDate = DateFormat.getDateTimeInstance().format(date);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = format.format(Date.parse(stringDate));

                    int i = db.makeCardActive(data, date1);
                    if (i == 1){
                        Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                        try {
                            List2Fragment.gridAdapter.notifyDataSetChanged();
                            List2Fragment.allTags = db.getActiveNFC();
                            List2Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List2Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }

                        try {
                            List3Fragment.gridAdapter.notifyDataSetChanged();
                            List3Fragment.allTags = db.getActiveNFC();
                            List3Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
//                            List3Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }
                        try {
                            List4Fragment.gridAdapter.notifyDataSetChanged();
                            List4Fragment.allTags = db.getActiveNFC();
                            List4Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List4Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }

                        try {
                            //List1Fragment.myPager.notifyDataSetChanged();
                          //  List1Fragment.allTags = db.getActiveNFC();
                            List1Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List1Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }
                    }
                    else if (i == 11){
                        Toast.makeText(getApplicationContext(), "Data Does not exists in Database..", Toast.LENGTH_LONG).show();
                    }
                    //  Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
                    // notifyAll();

                    *//*if (mViewPager.getCurrentItem() == 0){
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go.putExtra("viewpager_position", 0);
                        go.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());

                        startActivity(go);
                        finish();
                        //Toast.makeText(getApplicationContext(), String.valueOf(CardsFragment.mViewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                        startActivity(go);
                        finish();
                    }*//*
                }*/
              //  Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(CardsActivity.this);
            dialog.setMessage("Adding Records...");
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if(success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                        intent.putExtra("viewpager_position", CardsActivity.mViewPager.getCurrentItem());
                        intent.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());
                        startActivity(intent);
                        finish();
                        /*CardsFragment.mViewPager.getCurrentItem();
                        List1Fragment.webCall();
                        List2Fragment.webCall();
                        List3Fragment.webCall();
                        List4Fragment.webCall();*/
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                 /*   try
                    {
//                    List2Fragment.allTaggs.clear();
                        List2Fragment.nfcModel.clear();
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List3Fragment.allTaggs.clear();
                        List3Fragment.nfcModel1.clear();
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List4Fragment.allTaggs.clear();
                        List4Fragment.nfcModel1.clear();
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List1Fragment.allTags.clear();
                        List1Fragment.nfcModel.clear();
                        List1Fragment.mAdapter.notifyDataSetChanged();
                        List1Fragment.mAdapter1.notifyDataSetChanged();
                        List1Fragment.GetData(context);
                    }
                    catch(Exception e) {    }*/

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
            jsonObject.accumulate("Operation", "Request" );
            jsonObject.accumulate("friendProfileId", nfcProfileId);
            jsonObject.accumulate("myProfileId", profileId );

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();

    }
}
