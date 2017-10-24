package com.circle8.circleOne.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.ApplicationUtils.MyApplication;
import com.circle8.circleOne.Helper.CustomSharedPreference;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.UserObject;
import com.circle8.circleOne.MultiContactPicker;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Walkthrough.HelpActivity;
import com.google.gson.Gson;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FingerPrintLogin extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    TextView tvCancel;

    private FingerprintHandler fingerprintHandler;

    private static final String FINGERPRINT_KEY = "key_name";

    private static final int REQUEST_USE_FINGERPRINT = 300;

    protected static Gson mGson;
    protected static CustomSharedPreference mPref;
    private static UserObject mUser;
    private static String userString;
    private static LoginSession loginSession;
    SharedPreferences prefs = null;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int PERMISSION_REQUEST_CONTACT = 111;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_login);

        mGson = ((MyApplication) getApplication()).getGsonObject();
        mPref = ((MyApplication) getApplication()).getShared();
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        fingerprintHandler = new FingerprintHandler(this);
        loginSession = new LoginSession(getApplicationContext());
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
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

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       /* txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userString = mPref.getUserData();
                mUser = mGson.fromJson(userString, UserObject.class);
                if (mUser != null) {
                    loginSession.createLoginSession(mUser.getProfileid(), mUser.getUserId(), mUser.getUsername(), mUser.getEmail(), mUser.getImage(), mUser.getGender(), mUser.getPassword(), mUser.getDob(), mUser.getPhone());
                 //   Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();

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
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You must register before login with fingerprint", Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }

    private void checkDeviceFingerprintSupport() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!fingerprintManager.isHardwareDetected()) {
                    Toast.makeText(FingerPrintLogin.this, "Fingerprint is not supported in this device", Toast.LENGTH_LONG).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast.makeText(FingerPrintLogin.this, "Fingerprint not yet configured", Toast.LENGTH_LONG).show();
                }
            }
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(FingerPrintLogin.this, "Screen lock is not secure and enable", Toast.LENGTH_LONG).show();
            }
            return;
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
        }
        catch (Exception e){
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
            userString = mPref.getUserData();
            mUser = mGson.fromJson(userString, UserObject.class);
            if (mUser != null) {
                loginSession.createLoginSession(mUser.getProfileid(), mUser.getUserId(), mUser.getUsername(), mUser.getEmail(), mUser.getImage(), mUser.getGender(), mUser.getPassword(), mUser.getDob(), mUser.getPhone());
                // Toast.makeText(getApplicationContext(), getString(R.string.auth_successful), Toast.LENGTH_LONG).show();

                // login with only fingerprint
                if (prefs.getBoolean("firstrun", true)) {
                    // Do first run stuff here then set 'firstrun' as false
                    // using the following line to edit/commit prefs
                   /* Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);*/

                    if (ContextCompat.checkSelfPermission(FingerPrintLogin.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        new MultiContactPicker.Builder(FingerPrintLogin.this) //Activity/fragment context
                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                .hideScrollbar(false) //Optional - default: false
                                .showTrack(true) //Optional - default: true
                                .searchIconColor(Color.WHITE) //Option - default: White
                                .handleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                .bubbleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
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
            } else {
                Toast.makeText(getApplicationContext(), "You must register before login with fingerprint", Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }

    public void askForContactPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(FingerPrintLogin.this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FingerPrintLogin.this);
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
                    ActivityCompat.requestPermissions(FingerPrintLogin.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                new MultiContactPicker.Builder(FingerPrintLogin.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleTextColor(Color.WHITE) //Optional - default: White
                        .showPickerForResult(CONTACT_PICKER_REQUEST);
            }
        }
        else
        {
            new MultiContactPicker.Builder(FingerPrintLogin.this) //Activity/fragment context
                    .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .handleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
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
                Toast.makeText(this, R.string.permission_refused, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PERMISSION_REQUEST_CONTACT){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                new MultiContactPicker.Builder(FingerPrintLogin.this) //Activity/fragment context
                        .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                        .hideScrollbar(false) //Optional - default: false
                        .showTrack(true) //Optional - default: true
                        .searchIconColor(Color.WHITE) //Option - default: White
                        .handleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                        .bubbleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
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
        }else {
            Toast.makeText(this, getString(R.string.Unknown_permission_request), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private final String TAG = FingerprintHandler.class.getSimpleName();

        private Context context;

        public FingerprintHandler(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Toast.makeText(context, helpString, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            userString = mPref.getUserData();
            mUser = mGson.fromJson(userString, UserObject.class);
            if (mUser != null) {
                loginSession.createLoginSession(mUser.getProfileid(), mUser.getUserId(), mUser.getUsername(), mUser.getEmail(), mUser.getImage(), mUser.getGender(), mUser.getPassword(), mUser.getDob(), mUser.getPhone());
               // Toast.makeText(context, "LoggedIn Successfully..", Toast.LENGTH_LONG).show();

                // login with only fingerprint
                if (prefs.getBoolean("firstrun", true)) {
                    // Do first run stuff here then set 'firstrun' as false
                    // using the following line to edit/commit prefs
                    /*Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);*/

                    if (ContextCompat.checkSelfPermission(FingerPrintLogin.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        new MultiContactPicker.Builder(FingerPrintLogin.this) //Activity/fragment context
                                .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                .hideScrollbar(false) //Optional - default: false
                                .showTrack(true) //Optional - default: true
                                .searchIconColor(Color.WHITE) //Option - default: White
                                .handleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                .bubbleColor(ContextCompat.getColor(FingerPrintLogin.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                .bubbleTextColor(Color.WHITE) //Optional - default: White
                                .showPickerForResult(CONTACT_PICKER_REQUEST);
                    }else{
                        askForContactPermission();
                    }

                    prefs.edit().putBoolean("firstrun", false).commit();
                } else {
                    Intent userIntent = new Intent(context, CardsActivity.class);
                    userIntent.putExtra("viewpager_position", 0);
                    startActivity(userIntent);
                    finish();
                }
            } else {
                Toast.makeText(context, "You must register before login with fingerprint", Toast.LENGTH_LONG).show();
            }
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

}
