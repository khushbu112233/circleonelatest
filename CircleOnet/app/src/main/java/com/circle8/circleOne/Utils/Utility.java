package com.circle8.circleOne.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utility
{
    public static final int MY_PERMISSIONS_REQUEST_Location = 125;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 121;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 122;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 124;
    private static final String TAG = "Utility";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 001;
    private static List<String> questionsList=new ArrayList<>();
    private static List<String> answersList=new ArrayList<>();
    private static Map<String, String> collectionList=new LinkedHashMap<>();
    public static ProgressDialog mProgressDialog;
    //    public static final String BASE_URL = "http://circle8.asia:8999/Onet.svc/";
    // public static final String MERCHANT_BASE_URL = "http://circle8.asia:8082/Onet.svc/Merchant/";
    //  public static final String REWARDS_BASE_URL = "http://circle8.asia:8082/Onet.svc/Rewards/";
    public static RelativeLayout rlProgressDialog;
    /**
     * Uat for 8082
     */
//    public static final String BASE_IMAGE_URL = "http://circle8.asia:8083/";
    //khushbu last commented
    public static final String BASE_URL = "http://circle8.asia:8999/Onet.svc/";

    /**
     * Development for 8081
     */
    //  public static final String BASE_IMAGE_URL = "http://circle8.asia:8083/";
//    public static final String BASE_URL = "http://circle8.asia:8999/Onet.svc/";


    /**
     *Production  for 8999
     */
    public static final String BASE_IMAGE_URL = "http://circle8.asia/App_imgLib/";
    // public static final String BASE_URL = "http://circle8.asia:8999/Onet.svc/";

    // public static final String BASE_URL = "http://circle8.asia:8082/Onet.svc/";

    public static void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static String currentDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static long imageCalculateSize(Bitmap bitmapOrg){
        Bitmap bitmap = bitmapOrg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        long MEGABYTE = 1024L * 1024L;
        long b = lengthbmp / MEGABYTE;

        return b;
    }


    public static boolean checkStoragePermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity)context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                ((Activity)context).finish();
            }
            return false;
        }
        return true;
    }

    public static boolean checkLocationPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Location permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_Location);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_Location);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }



    public static void CustomProgressDialog(final String loading,Context context)
    {


        ImageView imgConnecting1,imgConnecting2,imgConnecting3;
        final TextView txtProgressing;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.show();
        //  mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setContentView(R.layout.progressdialog_layout);
        rlProgressDialog = (RelativeLayout)mProgressDialog.findViewById(R.id.rlProgressDialog);
        txtProgressing = (TextView)mProgressDialog.findViewById(R.id.txtProgressing);
        imgConnecting1= (ImageView) mProgressDialog.findViewById(R.id.imgConnecting1);
        imgConnecting2= (ImageView) mProgressDialog.findViewById(R.id.imgConnecting2);
        imgConnecting3= (ImageView) mProgressDialog.findViewById(R.id.imgConnecting3);
        txtProgressing.setText(loading+"...");
        rlProgressDialog.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.anticlockwise);
        imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(context,R.anim.clockwise);
        imgConnecting2.startAnimation(anim1);

    }

    public static void dismissProgress() {


        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean checkSMSPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkCameraPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Allow to Access Camera");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkContactPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Allow to Access Contact");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACT);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACT);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String POST2(String url,JSONObject jsonObject)
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
    public static Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(bitmap, new Matrix(), null);
        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);
        return combined;
    }

    /**
     * for help screen static value
     */
    public static List<String>  createGroupList()
    {
        Utility.freeMemory();
        questionsList = new ArrayList<String>();
        questionsList.add("My Account:");
        questionsList.add("Changing Your Password:");
        questionsList.add("Scan QR Code:");
        questionsList.add("Lost or Stolen Cards:");
        questionsList.add("Request for a New Card:");
        questionsList.add("Damaged Cards:");
        questionsList.add("Request for a new Account:");
        questionsList.add("Requested Card/Cards did not arrive:");
        questionsList.add("Sync Contacts:");
        questionsList.add("Reward Points:");
        questionsList.add("Notifications:");
        questionsList.add("Circles:");
        questionsList.add("Subscription:");
        questionsList.add("History:");
        questionsList.add("Security:");
        questionsList.add("Sort and Filter:");
        questionsList.add("Profile:");
        questionsList.add("How do I edit my Profile?");
        questionsList.add("Who can access the information on my profile?");
        questionsList.add("Event:");
        questionsList.add("Connect:");
        return questionsList;
    }

    public static Map<String, String>  createCollection()
    {
        Utility.freeMemory();
        // preparing laptops collection(child)
        String firstAnswer = "Editing Your Profile:"+"\n"+"1)Under ‘My Account’, tap on the pencil icon"+"\n"+"2)Here you can update your contact number, email as well as your profile pitcture"+"\n"+"3)* fields are compulsory and have to be filled before saving"+"\n"+"4)Tap ‘Save’ once you’re done to finalise changes"+"\n"+"Changing Your Password"+"\n"+"1)Under ‘My Account’, tap on the pencil icon"+"\n"+"2)Tap on the masked field below your name to change your password"+"\n"+"3)Re-enter your new password in the field directly below it"+"\n"+"4)Tap ‘Save’ once you’re done to finalise changes";
        String secondAnswer = "1) Under ‘My Account’, tap on the pencil icon"+"\n"+"2) Tap on the masked field below your name to change your password"+"\n"+"3) Re-enter your new password in the field directly below it"+"\n"+"4) Tap ‘Save’ once you’re done to finalise changes";
        String thirdAnswer = "CircleOne works for those whose mobile devices aren’t equipped with NFC capabilities too."+"\n"+"Each user has a unique QR code beside their name on their Profile."+"\n"+"The ‘Profile’ tab can be found on the bottom right of the screen in the dashboard."+"\n"+"To access your QR code for another user to scan, simply tap on it to enlarge the image.";
        String forthAnswer = "If your CircleOne Card is lost or stolen, you should report it right away and we will cancel it with immediate effect"+"\n"+"If you have more than 1 card, please follow these steps to identify your lost/stolen card:"+"\n"+"1) Tap each card against your NFC-enabled mobile device and determine which profile is missing."+"\n"+"2) Select the “Report Lost/Stolen Card” menu from the “Contact Us” page on your App or web platform and tell us which profile is missing.";
        String fifthAnswer = "1) Log in to your CircleOne account."+"\n"+"2) Select tab named “Request for a new card”.\n3) Follow the instructions regarding your artwork and details.\n4) Check that your details are correct and proceed with your payment.\n5) Once the order confirmation is received on our end, the card will be mailed within two - four weeks.\nAs of 25 September 2017: The card is currently free while stocks last.";
        String sixthAnswer = "You can reach us through the “Request for A New Card” option from the side bar.\nAlternatively, you may also reach us through the “Contact Us” option from the side bar or\nemail us at general@circle8.asia to request for a replacement card.\nPlease note that a fee of SGD $50.00 will be charged for each replacement card.";
        String seventhAnswer = "The NFC cards issued and connections established to each account is unique.\nTherefore, your account is securely tied to your card.\nIf you require a new account for security or other reasons, we can terminate your existing cards and import your connections after verifying your identity.\nYou will need to complete the acknowledgement slip and return it to us for verification and activation of a new account with your imported contacts.";
        String eightAnqwer = "If you do not receive your card within 4 weeks upon registration of the card, please contact us via the “Contact Us” page.\nSelect “Cards” under the dropdown menu and drop us a message with your transaction ID in the message box and we will get back to you within 24 hours.";
        String ninethAnswer = "The ‘Sync Contact’ tab in the sidebar allows you to sync your contacts from your device on to the CircleOne app.\nDoing so allows you to connect better with other users, especially if the both of you are already acquainted, but have yet to connect via the CircleOne app.";
        String tenthanswer = "Points can be earned through a variety of actions.\nThese accumulated points can be spent or exchanged for discounts and services at our various CircleOne merchants.\nYou’ll be able to see how many points you’ve earned and how you’ve earned it in this tab.\nYou can also view the various merchants and rewards we have by tapping on ‘Merchants’.";
        String eleventhAnswer = "CircleOne provides a handy way to show alerts whenever someone attempts to connect with them or if they receive a message.\nSimilar to other applications, you’ll be able to receive notifications on your mobile with a quick view feature.";
        String twelveAnswer = "You can sort contacts into groups of your own preference.\nSort them according to industry, interests or backgrounds, the choice is yours.\nTo group your contacts, proceed with the\nfollowing steps:\n1) Go to “Circles” in the sidebar menu.\n2) Click on the “+” symbol to create a new group.\n3) Enter a name or tag as well as a description of your group.\n4) You can modify the groups anytime you want.\n5) To delete a group click “delete” from the options drop bar on the top right.";
        String thirteenAnswer = "Our subscription rates are as follows:\n1) Default Subscription – Free with 200 contacts and up to 4 circles.\n2) Package 1 – SGD$1.00/month for 500 contacts and up to 100000 circles.\n3) Package 2 – SGD$5.00/month for 500 contacts and up to 100000 circles.\n4) Package 3 – SGD$10.00/month for 1000 contacts and up to 100000 circles.\n5) Ultimate Package – SGD$30.00/month for 10000 contacts and up to 100000 circles.\nOur subscriptions are auto-renewable.\nThis means that you’ll need to opt out of our subscriptions manually if you wish to terminate our services.\nSimply select “Billing” under “Contact” and let us know of your decision to cancel your subscription.\n(This isn’t in the app, how do they opt out?)";
        String fourteenAnswer = "The “History” tab gives you a summary of your activities, with all your events highlighted.\nIn there, you will find that all your events have been logged and you can search for a particular activity by keying in keywords in the search bar anytime you like.";
        String fifteenAnswer = "Security deals with all aspects related to the protection of sensitive information in your account.\nWe currently utilise fingerprint authentication on top of traditional password-and-username verification.\nThe single-touch sign-in provides convenience and added security as your fingerprint is unique to you.\nTo “register” a fingerprint with your device, navigate to your app’s security screen.\nWhen the screen prompts you to place your finger on the sensor, place your desired finger for your fingerprint to be locked in, just as you would on your phone home screen.\nOnce the sensor has fully captured your fingerprint, tap the ‘Enter’ key on your app to finish the process.\nA notification will appear to confirm that you’ve successfully registered a new fingerprint.";
        String sixteenAnswer = "Here’s how you can sort and filter your contacts in CircleOne quickly.\nClick on the drop-down menu in the top right corner of your device, select the attribute you wish to sort by and it will filter accordingly.\nAll Cards: This basically searches through all your contacts in your database.\nShared Cards: This option will only sift through contacts that you have shared with other people or contacts that have been shared with you.\nSort by Recently Added: This arranges your contacts by when they were added, with most recent at the top followed by the next and so forth."+"\n"+"Sort by Name: The contact list default sort order starts with A. However, there is an option to begin searches from Z through to A.\nby Company: This allows you to sort by name of the Organization.\nby Title: This sorts your list of contacts by their job titles.\nby Industry: This filter your contacts by specific industries. Simply enter the industry you would like to find within the search bar.\nby Association:";
        String seventeen = "By default, your profile lists both your personal and professional details."+"\n"+"You can choose to create additional profiles if you have more than one personal/professional identity."+"\n"+"Your profile contains the following fields:"+"\n"+"Name: Your given name will be displayed here."+"\n"+"Company: The name of the company you are associated with appear here."+"\n"+"Industry: The nature of business you are in."+"\n"+"Title: Your position within your company."+"\n"+"Association: Any or all associations you are involved with."+"\n"+"Location: Address of your current company."+"\n"+"Website: URL of your company’s website."+"\n"+"Email: Your professional email address.\nPhone: Your mobile number or your office number.\nFax: Fax number.\nSocial Media Profiles: Collate your various social media links on your main profile – Facebook/Linkedin/Twitter/Google+.\nPersonal Profile: Introduce yourself, write a short bio about yourself to set yourself apart from the crowd.\nCompany Profile: Share with other users what your company does.";
        String eighteen = "To edit your profile:"+"\n"+"1) Go to your profile tab located at the bottom of your screen, click on the pencil icon to begin editing."+"\n"+"2) Select the fields that you wish to edit."+"\n"+"3) Finalise your changes by clicking on \"Save\".";
        String nineteen = "Only your contacts and people who are connected to you can view your complete profile."+"\n"+"People that are not connected to you can only view your profile picture, name and title.";
        String twenty = "For Participants:"+"\n"+"You can view a full list of participating events by tapping on the events icon located at the bottom of your page."+"\n"+"Once in there, you will be able to view upcoming events announcements, updates and choose to sign up directly for an event via our app."+"\n"+"Through the app you will be able to engage with other people that have registered for the event."+"\n"+"You will be able to view a comprehensive list of participants that have registered for the event via our app."+"\n"+"For Event Organizers:"+"\n"+"Please email us at: general@circle8.asia for further enquires or please contact us via the contact form on the app."+"\n"+"Due to a high volume of organizers requesting to be listed on our app, please bear with us if we take some time to get back to you.";
        String twentyOne ="Connect enables you to add multiple new connections simply by tapping on your network’s extended contacts."+"\n"+"1) Connect with others by looking up their name/title/company and our platform will match you with your intended connection."+"\n"+"2) If your intended connection is an immediate contact of your current network, you will be allowed to send them a request to connect with them directly."+"\n"+"3) If the intended connection is an unknown contact outside of your current network, our system will build a path for you that allows you to connect with that particular individual."+"\n"+"We are proud to say we are the only platform that allows you to connect up to six degrees outside your own immediate connections."+"\n"+"This basically allows you to connect with anyone in the world (based on Frigyes Karinthy’s idea of the Six Degrees of Separation)."+"\n"+"4) You will need to unlock each level of connection in order to move on to the next – each connection costs SGD$0.99."+"\n"+"You will also receive invitations to be connected."+"\n"+"Simply accept the invitation and that person will be added to your network immediately.";

        answersList = new ArrayList<>();
        answersList.add(firstAnswer);
        answersList.add(secondAnswer);
        answersList.add(thirdAnswer);
        answersList.add(forthAnswer);
        answersList.add(fifthAnswer);
        answersList.add(sixthAnswer);
        answersList.add(seventhAnswer);
        answersList.add(eightAnqwer);
        answersList.add(ninethAnswer);
        answersList.add(tenthanswer);
        answersList.add(eleventhAnswer);
        answersList.add(twelveAnswer);
        answersList.add(thirteenAnswer);
        answersList.add(fourteenAnswer);
        answersList.add(fifteenAnswer);
        answersList.add(sixteenAnswer);
        answersList.add(seventeen);
        answersList.add(eighteen);
        answersList.add(nineteen);
        answersList.add(twenty);
        answersList.add(twentyOne);
        collectionList = new LinkedHashMap<String, String>();
        for(int i=0;i<questionsList.size();i++)
        {
            collectionList.put(questionsList.get(i),answersList.get(i));
        }

        return collectionList;
    }


}