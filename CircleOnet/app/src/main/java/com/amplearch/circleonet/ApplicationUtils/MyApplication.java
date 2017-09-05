package com.amplearch.circleonet.ApplicationUtils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Region;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.amplearch.circleonet.ConnectivityReceiver;
import com.amplearch.circleonet.Helper.CustomSharedPreference;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApplication extends MultiDexApplication
{
    private static MyApplication mInstance ;

    private Gson gson;
    private GsonBuilder builder;

    private CustomSharedPreference shared;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this ;

        FacebookSdk.sdkInitialize(getApplicationContext());
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.amplearch.circleonet", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) { }
        catch ( NoSuchAlgorithmException e ) {  }

        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CustomSharedPreference(getApplicationContext());
        MyApplication app = this;//(BeaconScannerApp)this.getApplication();
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public CustomSharedPreference getShared(){
        return shared;
    }

    public Gson getGsonObject(){
        return gson;
    }

}
