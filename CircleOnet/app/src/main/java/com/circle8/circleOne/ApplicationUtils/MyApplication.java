package com.circle8.circleOne.ApplicationUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Activity.EventsActivity;
import com.circle8.circleOne.Activity.GroupsActivity;
import com.circle8.circleOne.Activity.NewCardRequestActivity;
import com.circle8.circleOne.Activity.NotificationActivity;
import com.circle8.circleOne.Activity.RewardsPointsActivity;
import com.circle8.circleOne.Activity.SubscriptionActivity;
import com.circle8.circleOne.ConnectivityReceiver;
import com.circle8.circleOne.Fragments.ConnectFragment;
import com.circle8.circleOne.Helper.CustomSharedPreference;
import com.circle8.circleOne.Model.SampleConfigs;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ConfigUtils;
import com.circle8.circleOne.Utils.Consts;
import com.circle8.circleOne.Utils.Pref;
import com.facebook.FacebookSdk;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


public class MyApplication extends CoreApp
{
    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance ;
    private static SampleConfigs sampleConfigs;
    private Gson gson;
    private GsonBuilder builder;
    String CircleOnePage = "";
    Object activityToLaunch = null;
    public static String notiStatus = "";
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
        initImageLoader(getApplicationContext());
        mInstance = this ;
        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(this, "DXJDJBZWD7XQKHR3CVXW");

        FacebookSdk.sdkInitialize(getApplicationContext());
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.circle8.circleOne", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) { }
        catch ( NoSuchAlgorithmException e ) {  }

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        ActivityLifecycle.init(this);
        initSampleConfigs();

        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CustomSharedPreference(getApplicationContext());
        notiStatus = CircleOnePage;
       /* Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
    //    intent.putExtra("openURL", openURL);
        startActivity(intent);*/
        //      if(CircleOnePage!=null)
        // {
       /* if(CircleOnePage.equalsIgnoreCase("dashboard"))
        {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if(CircleOnePage.equalsIgnoreCase("event"))
        {
            Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(getApplicationContext(),"events",Toast.LENGTH_SHORT).show();
        }
        else if(CircleOnePage.equalsIgnoreCase("rewards"))
        {
            Intent intent = new Intent(getApplicationContext(), RewardsPointsActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("notification"))
        {
            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("connect"))
        {
            Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("newcard"))
        {
            Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("circle"))
        {
            Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("updateapp"))
        {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("http://circle8.asia/mobileapp.html"));
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(viewIntent);
                *//*Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*//*

        }
        else if(CircleOnePage.equalsIgnoreCase("card"))
        {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(CircleOnePage.equalsIgnoreCase("subscription"))
        {
            Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }*/
   /*     Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
             // startActivity(intent);
        startActivity(intent);
*/

        // }
        OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
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

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            //BackgroundImageLayout backgroundImageLayout = notification.payload.backgroundImageLayout;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            if (data != null) {
                customKey = data.optString("customkey", null);
               /* CircleOnePage = data.optString("CircleOnePage",null);

                    if(CircleOnePage!=null)
                    {
                   *//* notiStatus = CircleOnePage;
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("openURL", openURL);
                    startActivity(intent);*//*
                    if(CircleOnePage.equalsIgnoreCase("dashboard")) {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("event"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),  EventsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //    intent.putExtra("openURL", openURL);
                        startActivity(intent);

                     //   Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("rewards"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),RewardsPointsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                      //  intent.putExtra("openURL", openURL);
                        startActivity(intent);

                     //   Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("notification"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                     //   intent.putExtra("openURL", openURL);
                        startActivity(intent);

                       // Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("connect"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), ConnectFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                     //   intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("newcard"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                      //  intent.putExtra("openURL", openURL);
                        startActivity(intent);

                       // Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("circle"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),GroupsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                     //   Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("updateapp"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://circle8.asia/mobileapp.html"));
                        viewIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(viewIntent);
                    }else if(CircleOnePage.equalsIgnoreCase("card"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else if(CircleOnePage.equalsIgnoreCase("subscription"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }*/
                if (customKey != null) {
                    Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();
                    Log.e("OneSignalExample", "customkey set with value: " + customKey);
                }
            }
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String customKey = null;
            String openURL = null;/*
            String CircleOnePage = null;
            Object activityToLaunch=null;*/

            if (data != null) {
                customKey = data.optString("customkey", null);
                openURL = data.optString("openURL", null);
                 CircleOnePage = data.optString("CircleOnePage",null);

                if(CircleOnePage!=null)
                {
                   /* notiStatus = CircleOnePage;
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("openURL", openURL);
                    startActivity(intent);*/
                    if(CircleOnePage.equalsIgnoreCase("dashboard")) {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                      //  intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("event"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),  EventsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("rewards"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),RewardsPointsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                       // Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("notification"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("connect"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), ConnectFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                       // Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("newcard"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), NewCardRequestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("circle"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(),GroupsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                       // Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("updateapp"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("http://circle8.asia/mobileapp.html"));
                        viewIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(viewIntent);
                    }else if(CircleOnePage.equalsIgnoreCase("card"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                      //  intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }else if(CircleOnePage.equalsIgnoreCase("subscription"))
                    {
                        Pref.setValue(getApplicationContext(),"noti","1");
                        Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                       // intent.putExtra("openURL", openURL);
                        startActivity(intent);

                      //  Toast.makeText(getApplicationContext(), customKey, Toast.LENGTH_LONG).show();

                    }
                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
                if (result.action.actionID.equals("id1")) {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                    // activityToLaunch = CardsActivity.class;
                } else {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                }

            }

        }
    }

}
