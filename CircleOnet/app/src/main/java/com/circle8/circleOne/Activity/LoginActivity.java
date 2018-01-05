package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.CustomSharedPreference;
import com.circle8.circleOne.Helper.FingerPrintSession;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ProfileSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.Model.User;
import com.circle8.circleOne.Model.UserObject;
import com.circle8.circleOne.MultiContactPicker;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.PrefUtils;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.chat.ChatHelper;
import com.circle8.circleOne.databinding.ActivityLoginBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.fabric.sdk.android.Fabric;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;
import static com.circle8.circleOne.Utils.Validation.validateLogin;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        TextWatcher,
        CompoundButton.OnCheckedChangeListener {


    //Boolean isConnected = false;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;
    User user;
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    ProgressDialog pDialog;
    SharedPreferences prefs = null;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private TwitterLoginButton mLoginButton;
    LoginSession loginSession;
    ProfileSession profileSession;
    FingerPrintSession fingerPrintSession;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PACKAGE = "com.circle8.circleOne";
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(id,email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private ProgressDialog progress;
    String userName, userPassword;
    String SocialMedia_Id = "", SocialMedia_Type = "", UserName = "";
    String Facebook = "", Twitter = "", Google = "", Linkedin = "", final_name = "", final_email = "", final_image = "";
    private boolean LinkedInFlag = false;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintHandler fingerprintHandler;
    private static final String FINGERPRINT_KEY = "key_name";
    private static final int REQUEST_USE_FINGERPRINT = 300;
    private TwitterAuthClient client;
    public static String pushToken = "";
    private CheckBox rem_userpass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    ReferralCodeSession referralCodeSession;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int PERMISSION_REQUEST_CONTACT = 111;
    String Q_ID = "";
    String UserID = "";
    Boolean netCheck= false;
   public static ActivityLoginBinding activityLoginBinding;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));

        loginSession = new LoginSession(getApplicationContext());
        profileSession = new ProfileSession(getApplicationContext());
        fingerPrintSession = new FingerPrintSession(getApplicationContext());
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);

//          btnLoginTwitter = (ImageView) findViewById(R.id.btnLoginTwitter);

        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        rem_userpass = (CheckBox) findViewById(R.id.switchRemember);

        netCheck = Utility.isNetworkAvailable(getApplicationContext());
        if (netCheck == false){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(getApplicationContext(), netCheck.toString(), Toast.LENGTH_LONG).show();
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        activityLoginBinding.etLoginUser.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        activityLoginBinding.etLoginPass.setText(sharedPreferences.getString(KEY_PASS, ""));

        prefs = getSharedPreferences("com.circle8.circleOne", MODE_PRIVATE);
        activityLoginBinding.etLoginPass.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Utility.freeMemory();

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();

        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        boolean subscriptionSetting = status.getSubscriptionStatus().getUserSubscriptionSetting();
        String oneSignaluserID = status.getSubscriptionStatus().getUserId();

        pushToken = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // pushToken = status.getSubscriptionStatus().getPushToken();
        System.out.println("pushtoken " + pushToken);
        // Toast.makeText(getApplicationContext(), pushToken, Toast.LENGTH_LONG).show();

        activityLoginBinding.etLoginPass.addTextChangedListener(this);
        activityLoginBinding.etLoginUser.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);

        activityLoginBinding.etLoginPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (netCheck == false){
                    Utility.freeMemory();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                }
                else {
                    Utility.freeMemory();
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        userName = activityLoginBinding.etLoginUser.getText().toString();
                        userPassword = activityLoginBinding.etLoginPass.getText().toString();

                        if (userName.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
                        } else if (userPassword.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                        } else {
                            if (getCurrentFocus() != null) {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }

                            new HttpAsyncTask().execute(Utility.BASE_URL + "UserLogin");
                        }
                    }
                }
                return false;
            }
        });

        activityLoginBinding.imgForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                //textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
                activityLoginBinding.imgFinger.setVisibility(View.GONE);
            } else {

                activityLoginBinding.imgFinger.setVisibility(View.VISIBLE);
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                prefs = getSharedPreferences("com.circle8.circleOne", MODE_PRIVATE);
                // check support for android fingerprint on device
                checkDeviceFingerprintSupport();
                //generate fingerprint keystore
                generateFingerprintKeyStore();
                //instantiate Cipher class
                Cipher mCipher = instantiateCipher();
                if (mCipher != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                    }
                }
                fingerprintHandler.completeFingerAuthentication(fingerprintManager, cryptoObject);
            }
        } else {
            activityLoginBinding.imgFinger.setVisibility(View.GONE);
        }

        /*etLoginPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etLoginPass.getRight() - etLoginPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                        return true;
                    }
                }
                return false;
            }
        });*/

       /* tvPasswordInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                finish();
            }
        });*/


        activityLoginBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Facebook", "");
                intent.putExtra("Google", "");
                intent.putExtra("Linkedin", "");
                intent.putExtra("Twitter", "");
                intent.putExtra("UserName", "");
                intent.putExtra("Email", "");
                intent.putExtra("Image", "");
                startActivity(intent);
            }
        });

        activityLoginBinding.imgFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                startActivity(intent);
            }
        });

        generateHashkey();
        activityLoginBinding.loginButtonLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinkedInFlag = true;
                Utility.freeMemory();
                login_linkedin();
            }
        });

        if (loginSession.isLoggedIn()) {
            Utility.freeMemory();
            Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
            startActivity(intent);
            finish();
        }
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_twitter_login]
        mLoginButton = (TwitterLoginButton) findViewById(R.id.button_twitter_login);
        client = new TwitterAuthClient();
        activityLoginBinding.btnLoginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mLoginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        Log.d(TAG, "twitterLogin:success" + result);
                        handleTwitterSession(result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.w(TAG, "twitterLogin:failure", exception);
                        //updateUI(null);
                    }
                });*/

                client.authorize(LoginActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        Utility.freeMemory();
                        //Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                        handleTwitterSession(twitterSessionResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        //Toast.makeText(LoginActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //  isConnected = checkConnection();
        activityLoginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = activityLoginBinding.etLoginUser.getText().toString();
                userPassword = activityLoginBinding.etLoginPass.getText().toString();

                if (netCheck == false){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                }else {
                    if (!validateLogin(LoginActivity.this,userName, userPassword)) {
//                    Toast.makeText(getApplicationContext(), "Form Fill Invalid!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (getCurrentFocus() != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                        new HttpAsyncTask().execute(Utility.BASE_URL + "UserLogin");
                    }
                }
            /* Create an Intent that will start the Menu-Activity. */

            }
        });
        activityLoginBinding.btnSignIn.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Customizing G+ button
        activityLoginBinding.btnSignIn.setSize(SignInButton.SIZE_ICON_ONLY);
        activityLoginBinding.btnSignIn.setScopes(gso.getScopeArray());

        activityLoginBinding.fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();*/

                if (netCheck == false){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                }
                else {

                    String loading = "Loading";
                    CustomProgressDialog(loading, LoginActivity.this);

                    loginButton.performClick();

                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
                }
            }
        });

        /*if(PrefUtils.getCurrentUser(LoginActivity.this) != null){

            Intent homeIntent = new Intent(LoginActivity.this, CardsActivity.class);
            homeIntent.putExtra("viewpager_position", 0);
            startActivity(homeIntent);

            finish();
        }*/

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
//        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs() {
        if (rem_userpass.isChecked()) {
            editor.putString(KEY_USERNAME, activityLoginBinding.etLoginUser.getText().toString().trim());
            editor.putString(KEY_PASS, activityLoginBinding.etLoginPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }

    private void checkDeviceFingerprintSupport() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!fingerprintManager.isHardwareDetected()) {
                    //Toast.makeText(LoginActivity.this, "Fingerprint is not supported in this device", Toast.LENGTH_LONG).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    activityLoginBinding.imgFinger.setVisibility(View.GONE);
                    //  Toast.makeText(FingerPrintLogin.this, "Fingerprint not yet configured", Toast.LENGTH_LONG).show();
                }
            }
            if (!keyguardManager.isKeyguardSecure()) {
                activityLoginBinding.imgFinger.setVisibility(View.GONE);
                // Toast.makeText(FingerPrintLogin.this, "Screen lock is not secure and enable", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USE_FINGERPRINT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // check support for android fingerprint on device
                checkDeviceFingerprintSupport();
                //generate fingerprint keystore
                generateFingerprintKeyStore();
                //instantiate Cipher class
                Cipher mCipher = instantiateCipher();
                if (mCipher != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                    }
                }
            } else {
                activityLoginBinding.imgFinger.setVisibility(View.GONE);
                // Toast.makeText(this, R.string.permission_refused, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PERMISSION_REQUEST_CONTACT){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
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
        else {
            activityLoginBinding.imgFinger.setVisibility(View.GONE);
            //  Toast.makeText(this, getString(R.string.Unknown_permission_request), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private final String TAG = FingerPrintLogin.FingerprintHandler.class.getSimpleName();

        private Context context;

        public FingerprintHandler(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            //Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
            // imgFinger.setVisibility(View.GONE);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            //Toast.makeText(context, R.string.auth_successful, Toast.LENGTH_LONG).show();
            //imgFinger.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }

        public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            } catch (SecurityException ex) {
                Log.d(TAG, "An error occurred:\n" + ex.getMessage());
            } catch (Exception ex) {
                Log.d(TAG, "An error occurred\n" + ex.getMessage());
            }
        }
    }


    private void generateFingerprintKeyStore() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(new KeyGenParameterSpec.Builder(FINGERPRINT_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            keyGenerator.generateKey();
        } catch (Exception e) {
        }
    }

    private Cipher instantiateCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(FINGERPRINT_KEY, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher;
        } catch (Exception e) {
            // throw new RuntimeException("Failed to instantiate Cipher class");
            activityLoginBinding.imgFinger.setVisibility(View.GONE);
            return null;
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
            jsonObject.accumulate("Password", userPassword);
            jsonObject.accumulate("Platform", "Android");
            jsonObject.accumulate("Token", pushToken);
            jsonObject.accumulate("UserName", userName);

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

    public String POSTSocialMedia(String url) {
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
            jsonObject.accumulate("Platform", "Android");
            jsonObject.accumulate("SocialMedia_Id", SocialMedia_Id);
            jsonObject.accumulate("SocialMedia_Type", SocialMedia_Type);
            jsonObject.accumulate("Token", pushToken);
            jsonObject.accumulate("UserName", UserName);

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    public String POSTQ_ID(String url) {
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
            jsonObject.accumulate("Q_ID", Q_ID);
            jsonObject.accumulate("UserId", UserID);

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


    private class HttpAsyncTaskUpdateQ_ID extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
            // CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTQ_ID(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            //  rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success").toString();
                    String Message = jsonObject.getString("Message").toString();

                    if (Success.equals("1")) {
                        // Toast.makeText(getBaseContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), Message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to update Q_ID..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Logging In...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Logging in";
            CustomProgressDialog(loading, LoginActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    String profileid = "", FirstName = "", LastName = "", UserPhoto = "";

                    if (success.equals("1")) {
                        //  Toast.makeText(getBaseContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                        //   fingerPrintSession.createLoginSession(UserID, "", userName, "", "");

                        JSONObject jsonArray = jsonObject.getJSONObject("profile");
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        UserID = jsonArray.getString("userid");
                        profileid = jsonArray.getString("profileid");
                        FirstName = jsonArray.getString("FirstName");
                        LastName = jsonArray.getString("LastName");
                        UserPhoto = jsonArray.getString("UserPhoto");
                        String Status = jsonArray.getString("Status");
                        String Gender = jsonArray.getString("Gender");
                        String Phone = jsonArray.getString("Phone");
                        final String UserName = jsonArray.getString("UserName");
                        String dob = jsonArray.getString("dob");
                        String Connection_Limit = jsonArray.getString("Connection_Limit");
                        String Connection_Left = jsonArray.getString("Connection_Left");
                        Q_ID = jsonArray.getString("Q_ID");


                        try {
                            referralCodeSession.createReferral(jsonArray.getString("ReferrenceCode"));
                        } catch (Exception e) {
                        }

                        if (Status.equalsIgnoreCase("Verified")) {

                            if (Q_ID.equals("")){

                                final QBUser user = new QBUser(UserName, "circle@123");
                                user.setExternalId("");
                                user.setEmail(UserName);
                                user.setFullName(FirstName + " " + LastName);
                                StringifyArrayList<String> tags = new StringifyArrayList<String>();
                                tags.add("dev");
                                user.setTags(tags);

                                try {
                                    QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
                                        @Override
                                        public void onSuccess(final QBUser user, Bundle args) {
                                            Q_ID = String.valueOf(user.getId());
                                            user.setPassword("circle@123");
                                            new HttpAsyncTaskUpdateQ_ID().execute(Utility.BASE_URL+"User/Update_QID");
                                            SharedPrefsHelper.getInstance().saveQbUser(user);
                                        }

                                        @Override
                                        public void onError(QBResponseException errors) {
                                            // Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                            QBUser qbUser = new QBUser(UserName, "circle@123");
                                            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                                @Override
                                                public void onSuccess(final QBUser qbUser, Bundle bundle) {
                                                    // progressDialog.dismiss();
                                                    // Toaster.longToast("success");
                                                    Q_ID = String.valueOf(qbUser.getId());
                                                    new HttpAsyncTaskUpdateQ_ID().execute(Utility.BASE_URL+"User/Update_QID");

                                                    // setResult(RESULT_OK);
                                                    // SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                    //  Toast.makeText(getApplicationContext(), "fgbgfb", Toast.LENGTH_LONG).show();
                                                    qbUser.setPassword("circle@123");
                                                    ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                                                        @Override
                                                        public void onSuccess(Void result, Bundle bundle) {
                                                            SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                            //  Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                                                            // DialogsActivity.start(LoginActivity.this);
                                                            // finish();

                                                            //ProgressDialogFragment.hide(getSupportFragmentManager());
                                                        }

                                                        @Override
                                                        public void onError(QBResponseException e) {
                                                            ProgressDialogFragment.hide(getSupportFragmentManager());
                                                            // Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_LONG).show();

                                    /*ErrorUtils.showSnackbar(userListView, R.string.login_chat_login_error, e,
                                            R.string.dlg_retry, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    login(user);
                                                }
                                            });*/
                                                        }
                                                    });

                                                    // DataHolder.getInstance().setSignInQbUser(qbUser);
                                                    // Toaster.longToast(R.string.user_successfully_sign_in);

                                                    //finish();
                                                }

                                                @Override
                                                public void onError(QBResponseException errors) {
//                                    progressDialog.dismiss();
                                                    // Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                    /*showSnackbarError(rootLayout, R.string.errors, errors, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            signIn();
                                        }
                                    });*/
                                                }
                                            });
                                        }
                                    });
                                }catch (Exception e) {

                                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }



                            }else {

                                QBUser qbUser = new QBUser(UserName, "circle@123");
                                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                    @Override
                                    public void onSuccess(final QBUser qbUser, Bundle bundle) {
                                        // progressDialog.dismiss();
                                        //  Toaster.longToast("success");
                                        // setResult(RESULT_OK);
                                        // SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                        //  Toast.makeText(getApplicationContext(), "fgbgfb", Toast.LENGTH_LONG).show();
                                        qbUser.setPassword("circle@123");
                                        ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                                            @Override
                                            public void onSuccess(Void result, Bundle bundle) {
                                                SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                                                // DialogsActivity.start(LoginActivity.this);
                                                // finish();

                                                //ProgressDialogFragment.hide(getSupportFragmentManager());
                                            }

                                            @Override
                                            public void onError(QBResponseException e) {
                                                ProgressDialogFragment.hide(getSupportFragmentManager());
                                                // Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_LONG).show();

                                    /*ErrorUtils.showSnackbar(userListView, R.string.login_chat_login_error, e,
                                            R.string.dlg_retry, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    login(user);
                                                }
                                            });*/
                                            }
                                        });

                                        // DataHolder.getInstance().setSignInQbUser(qbUser);
                                        // Toaster.longToast(R.string.user_successfully_sign_in);

                                        //finish();
                                    }

                                    @Override
                                    public void onError(QBResponseException errors) {
//                                    progressDialog.dismiss();
                                        //   Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                    /*showSnackbarError(rootLayout, R.string.errors, errors, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            signIn();
                                        }
                                    });*/
                                    }
                                });
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Gson gson = ((MyApplication) getApplication()).getGsonObject();

                                HashMap<String, String> user = loginSession.getUserDetails();
                                try {
                                    if ((user.get(LoginSession.KEY_EMAIL).equalsIgnoreCase(UserName)) && (user.get(LoginSession.KEY_PASSWORD).equalsIgnoreCase(userPassword))) {
                                        UserObject userData = new UserObject(Q_ID, user.get(LoginSession.KEY_PROFILEID), user.get(LoginSession.KEY_NAME), user.get(LoginSession.KEY_EMAIL), user.get(LoginSession.KEY_PASSWORD), UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                        String userDataString = gson.toJson(userData);
                                        CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                        pref.setUserData(userDataString);

                                        loginSession.createLoginSession(Q_ID, user.get(LoginSession.KEY_PROFILEID), UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left );
                                        HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession(profile.get(ProfileSession.KEY_PROFILE_INDEX));
                                    } else {
                                        UserObject userData = new UserObject(Q_ID, profileid, FirstName + " " + LastName, UserName, userPassword, UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                        String userDataString = gson.toJson(userData);
                                        CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                        pref.setUserData(userDataString);

                                        loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left);
                                        profileSession.createProfileSession("0");
                                    }
                                }
                                catch (Exception e){
                                    UserObject userData = new UserObject(Q_ID, profileid, FirstName + " " + LastName, UserName, userPassword, UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                    String userDataString = gson.toJson(userData);
                                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                    pref.setUserData(userDataString);

                                    loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left);
                                    profileSession.createProfileSession("0");
                                }
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                   /* Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);*/


                                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                        new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                                .hideScrollbar(false) //Optional - default: false
                                                .showTrack(true) //Optional - default: true
                                                .searchIconColor(Color.WHITE) //Option - default: White
                                                .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                                    }else{
                                        askForContactPermission();
                                    }

                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }

                                // imgFinger.setVisibility(View.VISIBLE);
                               /* if (imgFinger.getVisibility() == View.VISIBLE)
                                {
                                    Gson gson = ((MyApplication) getApplication()).getGsonObject();
                                    UserObject userData = new UserObject(profileid, FirstName + " " + LastName, userName, userPassword, UserID, "", UserPhoto, false);
                                    String userDataString = gson.toJson(userData);
                                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                    pref.setUserData(userDataString);

                                    Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                                    //intent.putExtra("viewpager_position", 0);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    // Either gone or invisible
                                    loginSession.createLoginSession(profileid, UserID, "", userName, "", "",userPassword);
                                    if (prefs.getBoolean("firstrun", true))
                                    {
                                        // Do first run stuff here then set 'firstrun' as false
                                        // using the following line to edit/commit prefs
                                        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                        startActivity(intent);
                                        prefs.edit().putBoolean("firstrun", false).commit();
                                    }
                                    else
                                    {
                                        Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                        userIntent.putExtra("viewpager_position", 0);
                                        startActivity(userIntent);
                                        finish();
                                    }
                                }*/

                              /*  loginSession.createLoginSession(profileid, UserID, FirstName + " " + LastName, userName, UserPhoto, "");
                                // Toast.makeText(getApplicationContext(), getString(R.string.auth_successful), Toast.LENGTH_LONG).show();

                                // login with only fingerprint
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);
                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }*/
                            } else {
                                // imgFinger.setVisibility(View.GONE);
//                                loginSession.createLoginSession(profileid, UserID, FirstName + " " + LastName, final_email, UserPhoto, "");

                                HashMap<String, String> user = loginSession.getUserDetails();
                                try {
                                    if ((user.get(LoginSession.KEY_EMAIL).equalsIgnoreCase(UserName)) && (user.get(LoginSession.KEY_PASSWORD).equalsIgnoreCase(userPassword))) {

                                        loginSession.createLoginSession(Q_ID, user.get(LoginSession.KEY_PROFILEID), UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left);
                                        HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession(profile.get(ProfileSession.KEY_PROFILE_INDEX));

                                    } else {
                                        loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left);
                                        // HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession("0");

                                    }
                                }catch (Exception e){
                                    loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, userPassword, dob, Phone, Connection_Limit, Connection_Left);
                                    // HashMap<String, String> profile = profileSession.getProfileDetails();
                                    profileSession.createProfileSession("0");
                                }
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
//                                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
//                                    startActivity(intent);

                                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                        new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                                .hideScrollbar(false) //Optional - default: false
                                                .showTrack(true) //Optional - default: true
                                                .searchIconColor(Color.WHITE) //Option - default: White
                                                .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                                    }else{
                                        askForContactPermission();
                                    }


                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Pref.setValue(LoginActivity.this,"login_value","1");
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }
                            }
                        } else {
                            try {
                                signOut();
                            } catch (Exception e) {
                            }
                            try {
                                PrefUtils.clearCurrentUser(LoginActivity.this);
                                // We can logout from facebook by calling following method
                                LoginManager.getInstance().logOut();
                            } catch (Exception e) {
                            }
                            try {
                                mAuth.signOut();
                                com.twitter.sdk.android.Twitter.logOut();
                            } catch (Exception e) {
                            }
                            try {
                                LISessionManager.getInstance(getApplicationContext()).clearSession();
                            } catch (Exception e) {
                            }
                            Toast.makeText(getBaseContext(), "You need to verify your account first.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        dismissProgress();
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(getBaseContext(), "Incorrect username or password..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    /*private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return  isConnected;
    }*/



    public void askForContactPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .showPickerForResult(CONTACT_PICKER_REQUEST);
            }
        }
        else
        {
            new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
        }
    }
    public void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                // login_linkedin_btn.setVisibility(View.GONE);

            }

            @Override
            public void onAuthError(LIAuthError error) {

                // Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }


    private void showSnack(boolean isConnected) {
        String message = "Check For Data Connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            if (mGoogleApiClient.isConnected()) {
                if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                        Log.e(TAG, "display name: " + acct.getDisplayName());

                        String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
                        String email = acct.getEmail();
                        String personPhotoUrl = currentPerson.getImage().getUrl();
                        Log.e(TAG, "Name: " + personName + ", email: " + email
                        );

                        Facebook = "";
                        Google = acct.getId();
                        Linkedin = "";
                        Twitter = "";

                        final_name = personName;
                        final_email = email;
                        final_image = personPhotoUrl;
                        SocialMedia_Id = acct.getId();
                        SocialMedia_Type = "Google";
                        UserName = email;

                        new HttpAsyncTaskSocialMedia().execute(Utility.BASE_URL+"SocialMediaLogin");
                        updateUI(true);
                    }
                }
            } else {
                //connect it
                mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);


                Log.e(TAG, "display name: " + acct.getDisplayName());

                String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
                String email = acct.getEmail();
                String personPhotoUrl = "";
                Log.e(TAG, "Name: " + personName + ", email: " + email
                );

                Facebook = "";
                Google = acct.getId();
                Linkedin = "";
                Twitter = "";

                final_name = personName;
                final_email = email;
                final_image = personPhotoUrl;
                SocialMedia_Id = acct.getId();
                SocialMedia_Type = "Google";
                UserName = email;

                new HttpAsyncTaskSocialMedia().execute(Utility.BASE_URL+"SocialMediaLogin");


                //  loginSession.createLoginSession("", personName, email, personPhotoUrl, "");

             /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // imgFinger.setVisibility(View.VISIBLE);
                    Gson gson = ((MyApplication) getApplication()).getGsonObject();
                    UserObject userData = new UserObject("", personName, email, "", "", "", personPhotoUrl, false);
                    String userDataString = gson.toJson(userData);
                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                    pref.setUserData(userDataString);

                    Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                    //intent.putExtra("viewpager_position", 0);
                    startActivity(intent);
                    finish();
                } else {
                    // imgFinger.setVisibility(View.GONE);
                    loginSession.createLoginSession("", "", personName, email, personPhotoUrl, "");
                    if (prefs.getBoolean("firstrun", true)) {
                        // Do first run stuff here then set 'firstrun' as false
                        // using the following line to edit/commit prefs
                        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(intent);
                        prefs.edit().putBoolean("firstrun", false).commit();
                    } else {
                        Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                        userIntent.putExtra("viewpager_position", 0);
                        startActivity(userIntent);
                        finish();
                    }
                }
*/
                // Toast.makeText(getApplicationContext(), "Name: " + personName + ", email: " + email, Toast.LENGTH_LONG).show();

           /* txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

                updateUI(true);
            }
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email");
        activityLoginBinding.fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();*/

                String loading = "Loading";
                CustomProgressDialog(loading, LoginActivity.this);

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);
        // [START_EXCLUDE silent]

        showProgressDialog();

     /*   String loading = "Google Login" ;
        CustomProgressDialog(loading);*/

        // [END_EXCLUDE]

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*loginSession.createLoginSession("", user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
                            Intent homeIntent = new Intent(LoginActivity.this, CardsActivity.class);
                            homeIntent.putExtra("viewpager_position", 0);
                            startActivity(homeIntent);

                            finish();
*/

                            Facebook = "";
                            Google = "";
                            Linkedin = "";
                            Twitter = user.getUid();

                            final_name = user.getDisplayName();
                            final_email = user.getEmail();
                            final_image = String.valueOf(user.getPhotoUrl());
                            SocialMedia_Id = user.getUid();
                            SocialMedia_Type = "Twitter";
                            UserName = user.getEmail();

                            new HttpAsyncTaskSocialMedia().execute(Utility.BASE_URL+"SocialMediaLogin");



                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // imgFinger.setVisibility(View.VISIBLE);
                                Gson gson = ((MyApplication) getApplication()).getGsonObject();
                                UserObject userData = new UserObject("", user.getDisplayName(), user.getEmail(), "", "", "", String.valueOf(user.getPhotoUrl()), false);
                                String userDataString = gson.toJson(userData);
                                CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                pref.setUserData(userDataString);

                                Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                                //intent.putExtra("viewpager_position", 0);
                                startActivity(intent);
                                finish();
                            } else {
                                // imgFinger.setVisibility(View.GONE);
                                loginSession.createLoginSession("", "", user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);
                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }
                            }*/


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                         /*   Toast.makeText(LoginActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();*/
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
//                        rlProgressDialog.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_twitter]

    /*private void signOut() {
        mAuth.signOut();
        Twitter.logOut();

       // updateUI(null);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // mLoginButton.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if (LinkedInFlag == true)
        {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
            /*progress = new ProgressDialog(this);
            progress.setMessage("Logging in...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();*/
            String loading = "Logging in";
            CustomProgressDialog(loading, LoginActivity.this);
            linkededinApiHelper();
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LoginActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    setprofile(result.getResponseDataAsJson());
//                    progress.dismiss();
                    dismissProgress();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
                //  Toast.makeText(getApplicationContext(), "Not able to Login to LinkedIn..", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
       Set User Profile Information in Navigation Bar.
     */

    public void setprofile(JSONObject response) {

        try {
            Log.d("response link ", response.toString());

//            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            Facebook = "";
            Google = "";
            Linkedin = response.get("id").toString();
            Twitter = "";

            final_name = response.get("formattedName").toString();
            final_email = response.get("emailAddress").toString();
            String img = response.get("publicProfileUrl").toString().replaceAll("/", "");
            final_image = img;
            SocialMedia_Id = response.get("id").toString();
            SocialMedia_Type = "Linkedin";
            UserName = response.get("emailAddress").toString();

            new HttpAsyncTaskSocialMedia().execute(Utility.BASE_URL+"SocialMediaLogin");



            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // imgFinger.setVisibility(View.VISIBLE);
                Gson gson = ((MyApplication) getApplication()).getGsonObject();
                UserObject userData = new UserObject("", response.get("formattedName").toString(), response.get("emailAddress").toString(), "", "", "", response.get("publicProfileUrl").toString(), false);
                String userDataString = gson.toJson(userData);
                CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                pref.setUserData(userDataString);

                Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                //intent.putExtra("viewpager_position", 0);
                startActivity(intent);
                finish();
            } else {
                // imgFinger.setVisibility(View.GONE);
                loginSession.createLoginSession("", "", response.get("formattedName").toString(), response.get("emailAddress").toString(), response.get("publicProfileUrl").toString(), "");

                if (prefs.getBoolean("firstrun", true)) {
                    // Do first run stuff here then set 'firstrun' as false
                    // using the following line to edit/commit prefs
                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);
                    prefs.edit().putBoolean("firstrun", false).commit();
                } else {
                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                }
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    // This Method is used to generate "Android Package Name" hash key

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                // ((TextView) findViewById(R.id.package_name)).setText(info.packageName);
                Log.d("KeyHash ", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void showProgressDialog() {
        /*if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("Google Login..");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();*/
        String loading = "Google login";
       // CustomProgressDialog(loading, LoginActivity.this);
    }

    private void hideProgressDialog() {
        /*if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }*/
      //  dismissProgress();
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            activityLoginBinding.btnSignIn.setVisibility(View.GONE);
            //loginSession.createLoginSession(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
          /*  Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
            intent.putExtra("viewpager_position", 0);
            startActivity(intent);
            finish();*/
            // btnSignOut.setVisibility(View.VISIBLE);
            //  btnRevokeAccess.setVisibility(View.VISIBLE);
            // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            activityLoginBinding.btnSignIn.setVisibility(View.VISIBLE);
            //  btnSignOut.setVisibility(View.GONE);
            //  btnRevokeAccess.setVisibility(View.GONE);
            // llProfileLayout.setVisibility(View.GONE);
        }
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

//            progressDialog.dismiss();
            dismissProgress();

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
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                String personPhotoUrl = "https://graph.facebook.com/" + user.facebookID + "/picture?type=large";
                                PrefUtils.setCurrentUser(user, LoginActivity.this);
                               /* loginSession.createLoginSession("", object.getString("name").toString(), object.getString("email").toString(), personPhotoUrl, object.getString("gender").toString());
                                Intent intent = new Intent(LoginActivity.this, CardsActivity.class);
                                intent.putExtra("viewpager_position", 0);
                                startActivity(intent);
                                finish();
*/
                                Facebook = user.facebookID;
                                Google = "";
                                Linkedin = "";
                                Twitter = "";

                                final_name = user.name;
                                final_email = user.email;
                                final_image = personPhotoUrl;
                                SocialMedia_Id = user.facebookID;
                                SocialMedia_Type = "Facebook";
                                UserName = user.email;

                                new HttpAsyncTaskSocialMedia().execute(Utility.BASE_URL+"SocialMediaLogin");

                               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    // imgFinger.setVisibility(View.VISIBLE);
                                    Gson gson = ((MyApplication) getApplication()).getGsonObject();
                                    UserObject userData = new UserObject("", object.getString("name").toString(), object.getString("email").toString(), "", "", object.getString("gender").toString(), personPhotoUrl, false);
                                    String userDataString = gson.toJson(userData);
                                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                    pref.setUserData(userDataString);

                                    Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                                    //intent.putExtra("viewpager_position", 0);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // imgFinger.setVisibility(View.GONE);
                                    loginSession.createLoginSession("", "", object.getString("name").toString(), object.getString("email").toString(), personPhotoUrl, object.getString("gender").toString());

                                    if (prefs.getBoolean("firstrun", true)) {
                                        // Do first run stuff here then set 'firstrun' as false
                                        // using the following line to edit/commit prefs
                                        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                        startActivity(intent);
                                        prefs.edit().putBoolean("firstrun", false).commit();
                                    } else {
                                        Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                        userIntent.putExtra("viewpager_position", 0);
                                        startActivity(userIntent);
                                        finish();
                                    }
                                }*/


                                //new UploadFacebook("IMG_" + timestamp1).execute();
                               /* JSONObject object1 = object.getJSONObject("location");
                                Toast.makeText(getApplicationContext(), object1.toString(), Toast.LENGTH_LONG).show();
*/
                                //  session.createUserLoginSession(user.name, user.email, "", "", "");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //  Toast.makeText(AccountActivity.this,"welcome "+user.name,Toast.LENGTH_LONG).show();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
//            progressDialog.dismiss();
            dismissProgress();
        }

        @Override
        public void onError(FacebookException e) {
//            progressDialog.dismiss();
            dismissProgress();
        }
    };


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    private class HttpAsyncTaskSocialMedia extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Logging In...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            String loading = "Logging in";
            CustomProgressDialog(loading, LoginActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTSocialMedia(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            dialog.dismiss();
            dismissProgress();

            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success").toString();
                    String UserID = "", profileid = "", FirstName = "", LastName = "", UserPhoto = "";
                    if (success.equals("1")) {
                        //  Toast.makeText(getBaseContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                        //   fingerPrintSession.createLoginSession(UserID, "", userName, "", "");

                        JSONObject jsonArray = jsonObject.getJSONObject("profile");
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        UserID = jsonArray.getString("userid");
                        profileid = jsonArray.getString("profileid");
                        FirstName = jsonArray.getString("FirstName");
                        LastName = jsonArray.getString("LastName");
                        UserPhoto = jsonArray.getString("UserPhoto");
                        String Status = jsonArray.getString("Status");
                        String Gender = jsonArray.getString("Gender");
                        String Phone = jsonArray.getString("Phone");
                        final String UserName = jsonArray.getString("UserName");
                        String dob = jsonArray.getString("dob");
                        String Connection_Limit = jsonArray.getString("Connection_Limit");
                        String Connection_Left = jsonArray.getString("Connection_Left");
                        Q_ID = jsonArray.getString("Q_ID");

                        referralCodeSession.createReferral(jsonArray.getString("ReferrenceCode"));
                        if (Status.equalsIgnoreCase("Verified")) {


                            if (Q_ID.equals("")){

                                final QBUser user = new QBUser(UserName, "circle@123");
                                user.setExternalId("");
                                user.setEmail(UserName);
                                user.setFullName(FirstName + " " + LastName);
                                StringifyArrayList<String> tags = new StringifyArrayList<String>();
                                tags.add("dev");
                                user.setTags(tags);

                                try {
                                    QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
                                        @Override
                                        public void onSuccess(final QBUser user, Bundle args) {
                                            Q_ID = String.valueOf(user.getId());
                                            new HttpAsyncTaskUpdateQ_ID().execute(Utility.BASE_URL+"User/Update_QID");

                                        }

                                        @Override
                                        public void onError(QBResponseException errors) {
                                            //Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                            QBUser qbUser = new QBUser(UserName, "circle@123");
                                            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                                @Override
                                                public void onSuccess(final QBUser qbUser, Bundle bundle) {
                                                    // progressDialog.dismiss();
                                                    // Toaster.longToast("success");
                                                    Q_ID = String.valueOf(qbUser.getId());
                                                    new HttpAsyncTaskUpdateQ_ID().execute(Utility.BASE_URL+"User/Update_QID");

                                                    // setResult(RESULT_OK);
                                                    // SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                    // Toast.makeText(getApplicationContext(), "fgbgfb", Toast.LENGTH_LONG).show();
                                                    qbUser.setPassword("circle@123");
                                                    ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                                                        @Override
                                                        public void onSuccess(Void result, Bundle bundle) {
                                                            SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                            // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                                                            // DialogsActivity.start(LoginActivity.this);
                                                            // finish();

                                                            //ProgressDialogFragment.hide(getSupportFragmentManager());
                                                        }

                                                        @Override
                                                        public void onError(QBResponseException e) {
                                                            ProgressDialogFragment.hide(getSupportFragmentManager());
                                                            //  Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_LONG).show();

                                    /*ErrorUtils.showSnackbar(userListView, R.string.login_chat_login_error, e,
                                            R.string.dlg_retry, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    login(user);
                                                }
                                            });*/
                                                        }
                                                    });

                                                    // DataHolder.getInstance().setSignInQbUser(qbUser);
                                                    // Toaster.longToast(R.string.user_successfully_sign_in);

                                                    //finish();
                                                }

                                                @Override
                                                public void onError(QBResponseException errors) {
//                                    progressDialog.dismiss();
                                                    //  Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                    /*showSnackbarError(rootLayout, R.string.errors, errors, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            signIn();
                                        }
                                    });*/
                                                }
                                            });
                                        }
                                    });
                                }catch (Exception e) {

                                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }



                            }else {

                                QBUser qbUser = new QBUser(UserName, "circle@123");
                                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                    @Override
                                    public void onSuccess(final QBUser qbUser, Bundle bundle) {
                                        // progressDialog.dismiss();
                                        //Toaster.longToast("success");
                                        // setResult(RESULT_OK);
                                        // SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                        //  Toast.makeText(getApplicationContext(), "fgbgfb", Toast.LENGTH_LONG).show();
                                        qbUser.setPassword("circle@123");
                                        ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
                                            @Override
                                            public void onSuccess(Void result, Bundle bundle) {
                                                SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                                                // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
                                                // DialogsActivity.start(LoginActivity.this);
                                                // finish();

                                                //ProgressDialogFragment.hide(getSupportFragmentManager());
                                            }

                                            @Override
                                            public void onError(QBResponseException e) {
                                                ProgressDialogFragment.hide(getSupportFragmentManager());
                                                // Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_LONG).show();

                                    /*ErrorUtils.showSnackbar(userListView, R.string.login_chat_login_error, e,
                                            R.string.dlg_retry, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    login(user);
                                                }
                                            });*/
                                            }
                                        });

                                        // DataHolder.getInstance().setSignInQbUser(qbUser);
                                        // Toaster.longToast(R.string.user_successfully_sign_in);

                                        //finish();
                                    }

                                    @Override
                                    public void onError(QBResponseException errors) {
//                                    progressDialog.dismiss();
                                        // Toast.makeText(getApplicationContext(), errors.toString(), Toast.LENGTH_LONG).show();
                                    /*showSnackbarError(rootLayout, R.string.errors, errors, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            signIn();
                                        }
                                    });*/
                                    }
                                });
                            }



                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                Gson gson = ((MyApplication) getApplication()).getGsonObject();

                                HashMap<String, String> user = loginSession.getUserDetails();
                                try {

                                    if ((user.get(LoginSession.KEY_EMAIL).equalsIgnoreCase(UserName))) {
                                        UserObject userData = new UserObject(Q_ID, user.get(LoginSession.KEY_PROFILEID), user.get(LoginSession.KEY_NAME), user.get(LoginSession.KEY_EMAIL), "", UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                        String userDataString = gson.toJson(userData);
                                        CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                        pref.setUserData(userDataString);

                                        loginSession.createLoginSession(Q_ID, user.get(LoginSession.KEY_PROFILEID), UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                        HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession(profile.get(ProfileSession.KEY_PROFILE_INDEX));
                                    } else {
                                        UserObject userData = new UserObject(Q_ID, profileid, FirstName + " " + LastName, UserName, "", UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                        String userDataString = gson.toJson(userData);
                                        CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                        pref.setUserData(userDataString);

                                        loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                        profileSession.createProfileSession("0");
                                    }
                                }
                                catch (Exception e){
                                    UserObject userData = new UserObject(Q_ID, profileid, FirstName + " " + LastName, UserName, "", UserID, Gender, UserPhoto, dob, Phone, Connection_Limit, Connection_Left, false);
                                    String userDataString = gson.toJson(userData);
                                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                    pref.setUserData(userDataString);

                                    loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                    profileSession.createProfileSession("0");
                                }

                              /*  UserObject userData = new UserObject(profileid, FirstName + " " + LastName, UserName, "", UserID, Gender, UserPhoto, dob, Phone, false);
                                String userDataString = gson.toJson(userData);
                                CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                pref.setUserData(userDataString);

                               // Toast.makeText(getApplicationContext(), userName + " " + userPassword, Toast.LENGTH_LONG).show();
                                loginSession.createLoginSession(profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone);
                              */  if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                   /* Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);*/

                                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                        new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                                .hideScrollbar(false) //Optional - default: false
                                                .showTrack(true) //Optional - default: true
                                                .searchIconColor(Color.WHITE) //Option - default: White
                                                .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                                    }else{
                                        askForContactPermission();
                                    }

                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }

                               /* if (imgFinger.getVisibility() == View.VISIBLE) {
                                    Gson gson = ((MyApplication) getApplication()).getGsonObject();
                                    UserObject userData = new UserObject(profileid, FirstName + " " + LastName, userName, userPassword, UserID, "", UserPhoto, false);
                                    String userDataString = gson.toJson(userData);
                                    CustomSharedPreference pref = ((MyApplication) getApplication()).getShared();
                                    pref.setUserData(userDataString);

                                    Intent intent = new Intent(getApplicationContext(), FingerPrintLogin.class);
                                    //intent.putExtra("viewpager_position", 0);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Either gone or invisible
                                    loginSession.createLoginSession(profileid, UserID, "", userName, "", "",userPassword);
                                    if (prefs.getBoolean("firstrun", true)) {
                                        // Do first run stuff here then set 'firstrun' as false
                                        // using the following line to edit/commit prefs
                                        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                        startActivity(intent);
                                        prefs.edit().putBoolean("firstrun", false).commit();
                                    } else {
                                        Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                        userIntent.putExtra("viewpager_position", 0);
                                        startActivity(userIntent);
                                        finish();
                                    }
                                }*/


                                // imgFinger.setVisibility(View.VISIBLE);


                               /* loginSession.createLoginSession(profileid, UserID, FirstName + " " + LastName, userName, UserPhoto, "");
                                // Toast.makeText(getApplicationContext(), getString(R.string.auth_successful), Toast.LENGTH_LONG).show();

                                // login with only fingerprint
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);
                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }*/
                            } else {
                                // imgFinger.setVisibility(View.GONE);


                                HashMap<String, String> user = loginSession.getUserDetails();
                                try {
                                    if ((user.get(LoginSession.KEY_EMAIL).equalsIgnoreCase(UserName))) {

                                        loginSession.createLoginSession(Q_ID, user.get(LoginSession.KEY_PROFILEID), UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                        HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession(profile.get(ProfileSession.KEY_PROFILE_INDEX));

                                    } else {
                                        loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                        // HashMap<String, String> profile = profileSession.getProfileDetails();
                                        profileSession.createProfileSession("0");

                                    }
                                }
                                catch (Exception e){
                                    loginSession.createLoginSession(Q_ID, profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone, Connection_Limit, Connection_Left);
                                    // HashMap<String, String> profile = profileSession.getProfileDetails();
                                    profileSession.createProfileSession("0");
                                }

                                //loginSession.createLoginSession(profileid, UserID, FirstName + " " + LastName, UserName, UserPhoto, Gender, "", dob, Phone);
                                if (prefs.getBoolean("firstrun", true)) {
                                    // Do first run stuff here then set 'firstrun' as false
                                    // using the following line to edit/commit prefs
                                  /*  Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                    startActivity(intent);*/

                                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                        new MultiContactPicker.Builder(LoginActivity.this) //Activity/fragment context
                                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                                .hideScrollbar(false) //Optional - default: false
                                                .showTrack(true) //Optional - default: true
                                                .searchIconColor(Color.WHITE) //Option - default: White
                                                .handleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                                    }else{
                                        askForContactPermission();
                                    }


                                    prefs.edit().putBoolean("firstrun", false).commit();
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(), CardsActivity.class);
                                    userIntent.putExtra("viewpager_position", 0);
                                    startActivity(userIntent);
                                    finish();
                                }
                            }
                        } else {
                            try {
                                if (!Google.equals("")) {
                                    signOut();
                                }
                            } catch (Exception e) {
                            }

                            try {
                                if (!Facebook.equals("")) {
                                    PrefUtils.clearCurrentUser(LoginActivity.this);
                                    // We can logout from facebook by calling following method
                                    LoginManager.getInstance().logOut();
                                }
                            } catch (Exception e) {
                            }

                            try {
                                if (!Twitter.equals("")) {
                                    mAuth.signOut();
                                    com.twitter.sdk.android.Twitter.logOut();
                                }
                            } catch (Exception e) {
                            }

                            try {
                                if (!Linkedin.equals("")) {
                                    LISessionManager.getInstance(getApplicationContext()).clearSession();
                                }
                            } catch (Exception e) {
                            }
                            Toast.makeText(getBaseContext(), "You need to verify your account first.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if (!Google.equals("")) {
                            signOut();
                        }
                        if (!Facebook.equals("")) {
                            PrefUtils.clearCurrentUser(LoginActivity.this);
                            // We can logout from facebook by calling following method
                            LoginManager.getInstance().logOut();
                        }
                        if (!Twitter.equals("")) {
                            mAuth.signOut();
                            com.twitter.sdk.android.Twitter.logOut();
                        }
                        if (!Linkedin.equals("")) {
                            LISessionManager.getInstance(getApplicationContext()).clearSession();
                        }
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        intent.putExtra("Facebook", Facebook);
                        intent.putExtra("Google", Google);
                        intent.putExtra("Linkedin", Linkedin);
                        intent.putExtra("Twitter", Twitter);
                        intent.putExtra("UserName", final_name);
                        intent.putExtra("Email", final_email);
                        intent.putExtra("Image", final_image);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to login..", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Facebook", Facebook);
                intent.putExtra("Google", Google);
                intent.putExtra("Linkedin", Linkedin);
                intent.putExtra("Twitter", Twitter);
                intent.putExtra("UserName", final_name);
                intent.putExtra("Email", final_email);
                intent.putExtra("Image", final_image);
                startActivity(intent);
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
