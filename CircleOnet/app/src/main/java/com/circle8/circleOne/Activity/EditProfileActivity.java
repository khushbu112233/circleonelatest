package com.circle8.circleOne.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.AddEventAdapter;
import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CardViewDataAdapter;
import com.circle8.circleOne.Adapter.CustomAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ProfileSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.Model.AssociationModel;
import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentEditProfileBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
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
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.neovisionaries.i18n.CountryCode;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static junit.framework.Assert.assertEquals;

public class EditProfileActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    public static final String PACKAGE = "com.circle8.circleOne";
    private static final int PICKFILE_RESULT_CODE = 1;
    public static ArrayList<TestimonialModel> allTaggs;
    //for adding event and expande & collapse
    public static ArrayList<String> addEventList = new ArrayList<>();
    public static AddEventAdapter addEventAdapter;
    ArrayList<String> company, designation, industry, designation_id, company_id, industry_id;
    String association_ID, association_NAME;
    String profileId = "";
    public static String Card_Front = "",Card_Back = "", FirstName = "", LastName = "",UserPhoto = "",Phone1 = "", Phone2 = "", Mobile1 = "", Mobile2 = ""
            ,Fax1 = "", Fax2 = "", Email1 = "",Email2 = "", Youtube = "",Facebook = "", Twitter = "", Google = "",LinkedIn = "", IndustryName = ""
            , CompanyName = "", CompanyProfile = "",Designation = "", ProfileDesc = "",Status = "", Address1 = "", Address2 = "", Address3 = "", Address4 = "", City = "", State = "", Country = "", Postalcode = "", Website = "", Attachment_FileName = "";
    //  public static ViewPager mViewPager, viewPager1;
    CustomAdapter customAdapter;
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    String type = "";
    private ProgressDialog progressDialog;
    String UserID = "";
    private String userChoosenTask;
    private String[] array;
    private CharSequence[] items;
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1, REQUEST_DOCUMENT = 2, REQUEST_AUDIO = 3;
    private int camera_permission;
    private String addEventString;
    private String arrowStatus = "DOWN";
    private String arrowStatus1 = "DOWN";
    private LoginSession session;
    private int SELECT_GALLERY_CARD = 500;
    private int REQUEST_CAMERA_CARD = 501;
    private static String final_ImgBase64 = "";
    private int cardposition = 0;
    static String cardType = "";
    String Attach_String = "";
    String companyID, designationID, industryID, associationID, addressID;
    private ProgressDialog mProgressDialog;
    ArrayList<String> AssoNameList = new ArrayList<>();
    ArrayList<String> AssoIdList = new ArrayList<>();
    public static String strFB = "", strLinkedin = "", strGoogle = "", strTwitter = "", strYoutube = "";
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private static final int PERMISSIONS_REQUEST_CAMERA = 314;
    FrameLayout FrameScanBotCamera;
    ProfileSession profileSession;
    private Toast userGuidanceToast;
    private List<AssociationModel> associationList, eventList;
    private boolean flashEnabled = false;
    private boolean autoSnappingEnabled = true;
    private Bitmap documentImage;
    public static ArrayList<String> final_associationIdArray, final_eventIdArray;
    public static ArrayList<String> final_associationNameArray, final_eventNameArray;
    private Bitmap originalBitmap;
    private ImageView resultImageView;
    private Button cropButton;
    private Button backButton;
    RelativeLayout rltGallery;
    public static Activity activity;
    private boolean LinkedInFlag = false;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(id,email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private FirebaseAuth mAuth;
    private TwitterAuthClient client;
    JSONArray arrayAssociation, arrayEvents;
    private ArrayList<EventModel> eventModelArrayList = new ArrayList<>();
    private ArrayList<String> eventCategoryIDList = new ArrayList<>();
    private ArrayList<String> eventCategoryNameList = new ArrayList<>();
    public static String ProfileName;
    private RecyclerView.Adapter mAdapter, mAdapter1;
    String fromActivity = "";
    String cropType = "";
    ReferralCodeSession referralCodeSession;
    private String refer;
    private ProgressDialog progress;
    public static FragmentEditProfileBinding fragmentEditProfileBinding;
    boolean result, result1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        fragmentEditProfileBinding  = DataBindingUtil.setContentView(this,R.layout.fragment_edit_profile);
        Utility.freeMemory();
        activity = EditProfileActivity.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       /* AsyncTaskRunner runner = new AsyncTaskRunner();
        String sleepTime = "200";
        runner.execute(sleepTime);*/
        Log.e("ac","ac");
        initUI();
        //  new LongOperation().execute();
        fragmentEditProfileBinding.includeTop.imgProfileShare.setOnClickListener(this);

        SpannableString ss = new SpannableString("Ask your friends to write a Testimonial for you(100 words or less),Please choose from your CircleOne contacts and send a request.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent1 = new Intent(getApplicationContext(), SearchGroupMembers.class);
                intent1.putExtra("from", "profile");
                intent1.putExtra("ProfileId", profileId);
                startActivity(intent1);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 91, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result=Utility.checkPermission(EditProfileActivity.this);
        result1=Utility.checkCameraPermission(EditProfileActivity.this);

        // TextView textView = (TextView) findViewById(R.id.hello);
        fragmentEditProfileBinding.txtTestimonial.setText(ss);
        fragmentEditProfileBinding.txtTestimonial.setMovementMethod(LinkMovementMethod.getInstance());
        fragmentEditProfileBinding.txtTestimonial.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        fragmentEditProfileBinding.btnSignIn.setOnClickListener(this);
        fragmentEditProfileBinding.imgGoogle.setOnClickListener(this);
        fragmentEditProfileBinding.txtAttachDelete.setOnClickListener(this);
        fragmentEditProfileBinding.imgFb.setOnClickListener(this);
        fragmentEditProfileBinding.includeTop.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fragmentEditProfileBinding.txtbackDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentEditProfileBinding.txtCardBack.setText("");
            }
        });

        fragmentEditProfileBinding.txtfrontDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentEditProfileBinding.txtCardFront.setText("");
            }
        });
        fragmentEditProfileBinding.imgLinkedin.setOnClickListener(this);
        fragmentEditProfileBinding.imgAdd.setOnClickListener(this);
        fragmentEditProfileBinding.ivAttachFrontImage.setOnClickListener(this);
        fragmentEditProfileBinding.ivAttachBackImage.setOnClickListener(this);
        fragmentEditProfileBinding.ivArrowImg.setOnClickListener(this);
        fragmentEditProfileBinding.ivArrowImg1.setOnClickListener(this);
        fragmentEditProfileBinding.includeTop.ivProfileDelete.setOnClickListener(this);
        fragmentEditProfileBinding.ivAttachFile.setOnClickListener(this);
        fragmentEditProfileBinding.ivTeleAdd.setOnClickListener(this);
        fragmentEditProfileBinding.ivMobAdd.setOnClickListener(this);
        fragmentEditProfileBinding.ivFaxAdd.setOnClickListener(this);
        fragmentEditProfileBinding.includeTop.imgDone.setOnClickListener(this);
        fragmentEditProfileBinding.txtMore.setOnClickListener(this);
        fragmentEditProfileBinding.imgYoutube.setOnClickListener(this);
        fragmentEditProfileBinding.imgTwitter.setOnClickListener(this);
        fragmentEditProfileBinding.includeTop.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropType = "profile";
                selectImageToCrop();
            }
        });
        fragmentEditProfileBinding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), array[position].toString(), Toast.LENGTH_SHORT).show();

                addEventString = array[position].toString();
                addEventList.add(addEventString);

                addEventAdapter = new AddEventAdapter(getApplicationContext(), addEventList);
                fragmentEditProfileBinding.gridViewAdded.setAdapter(addEventAdapter);
                fragmentEditProfileBinding.gridViewAdded.setExpanded(true);
                addEventAdapter.notifyDataSetChanged();

                if (addEventList.size() != 0) {
                    fragmentEditProfileBinding.tvEventInfo.setVisibility(View.GONE);
                } else if (addEventList.size() == 0) {
                    fragmentEditProfileBinding.tvEventInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        fragmentEditProfileBinding.autoCompleteDesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentEditProfileBinding.includeTop.tvDesignation.setText(fragmentEditProfileBinding.autoCompleteDesignation.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        fragmentEditProfileBinding.edtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentEditProfileBinding.includeTop.tvPersonName.setText(fragmentEditProfileBinding.edtFirstName.getText().toString() + " " + fragmentEditProfileBinding.edtLastName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fragmentEditProfileBinding.edtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentEditProfileBinding.includeTop.tvPersonName.setText(fragmentEditProfileBinding.edtFirstName.getText().toString() + " " + fragmentEditProfileBinding.edtLastName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fragmentEditProfileBinding.autoCompleteCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentEditProfileBinding.includeTop.tvCompany.setText(fragmentEditProfileBinding.autoCompleteCompany.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        fragmentEditProfileBinding.autoCompleteDesignation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Utility.freeMemory();
                int selectedPos = designation.indexOf((String) fragmentEditProfileBinding.autoCompleteDesignation.getText().toString());
                designationID = designation_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

        fragmentEditProfileBinding.autoCompleteCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                int selectedPos = company.indexOf((String) fragmentEditProfileBinding.autoCompleteCompany.getText().toString());
                companyID = company_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

        fragmentEditProfileBinding.autoCompleteIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Utility.freeMemory();
                int selectedPos = industry.indexOf((String) fragmentEditProfileBinding.autoCompleteIndustry.getText().toString());
                industryID = industry_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

    }

    private void selectImageToCrop() {
        final CharSequence[] items = { "Upload Picture", "Remove Picture",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Select to Upload Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(EditProfileActivity.this);
                boolean result1=Utility.checkCameraPermission(EditProfileActivity.this);

                if (items[item].equals("Upload Picture")) {
                    if (result && result1) {
                        CropImage.activity(null)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(EditProfileActivity.this);
                    }
                } else if (items[item].equals("Remove Picture")) {
                    UserPhoto = "";
                    fragmentEditProfileBinding.includeTop.imgProfile.setImageResource(R.drawable.usr_white1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.imgTwitter:
                client.authorize(EditProfileActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Toast.makeText(EditProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                        handleTwitterSession(twitterSessionResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        // Toast.makeText(EditProfileActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.txtMore:
                Utility.freeMemory();
                Intent intent = new Intent(getApplicationContext(), TestimonialActivity.class);
                intent.putExtra("ProfileId", profileId);
                intent.putExtra("from", "editprofile");
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.imgDone:
                arrayAssociation = new JSONArray();
                String data = "";
                List<AssociationModel> stList = ((CardViewDataAdapter) mAdapter)
                        .getStudentist();
                Utility.freeMemory();
                for (int i = 0; i < stList.size(); i++)
                {
                    AssociationModel singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true)
                    {
                        data = data + "\n" + singleStudent.getId().toString();
                        arrayAssociation.put(Integer.parseInt(singleStudent.getId().toString()));
                          /*
                           * Toast.makeText( CardViewActivity.this, " " +
                           * singleStudent.getName() + " " +
                           * singleStudent.getEmailId() + " " +
                           * singleStudent.isSelected(),
                           * Toast.LENGTH_SHORT).show();
                           */
                    }
                }

                arrayEvents = new JSONArray();
                String data1 = "";
                List<AssociationModel> stList1 = ((CardViewDataAdapter) mAdapter1).getStudentist();

                for (int i = 0; i < stList1.size(); i++)
                {
                    AssociationModel singleStudent = stList1.get(i);
                    if (singleStudent.isSelected() == true)
                    {
                        data1 = data1 + "\n" + singleStudent.getId().toString();
                        arrayEvents.put(Integer.parseInt(singleStudent.getId().toString()));
                          /*
                           * Toast.makeText( CardViewActivity.this, " " +
                           * singleStudent.getName() + " " +
                           * singleStudent.getEmailId() + " " +
                           * singleStudent.isSelected(),
                           * Toast.LENGTH_SHORT).show();
                           */
                    }
                }

               /* Toast.makeText(EditProfileActivity.this,
                     "Selected Students: \n" + arrayEvents, Toast.LENGTH_LONG)
                        .show();*/

                if (!fragmentEditProfileBinding.edtWork.getText().toString().equals("")) {
                    try {
                        if (fragmentEditProfileBinding.ccp1.getSelectedCountryCodeWithPlus() != null) {
                            Phone1 = fragmentEditProfileBinding.ccp1.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtWork.getText().toString();
                        }
                    } catch (Exception e) {
                        Phone1 = fragmentEditProfileBinding.ccp1.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtWork.getText().toString();
                    }
                }

                if (!fragmentEditProfileBinding.edtWork2.getText().toString().equals("")) {
                    try {
                        if (fragmentEditProfileBinding.ccp2.getSelectedCountryCodeWithPlus() != null) {
                            Phone2 = fragmentEditProfileBinding.ccp2.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtWork2.getText().toString();
                        }
                    } catch (Exception e) {
                        Phone2 = fragmentEditProfileBinding.ccp2.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtWork2.getText().toString();
                    }
                }

                if (!fragmentEditProfileBinding.edtPrimary.getText().toString().equals("")) {
                    try {
                        if (fragmentEditProfileBinding.ccp3.getSelectedCountryCodeWithPlus() != null) {
                            Mobile1 = fragmentEditProfileBinding.ccp3.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtPrimary.getText().toString();
                        }
                    } catch (Exception e) {
                        Mobile1 = fragmentEditProfileBinding.ccp3.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtPrimary.getText().toString();
                    }
                }

                if (!fragmentEditProfileBinding.edtPrimary2.getText().toString().equals("")) {
                    try {
                        if (fragmentEditProfileBinding.ccp4.getSelectedCountryCodeWithPlus() != null) {
                            Mobile2 = fragmentEditProfileBinding.ccp4.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtPrimary2.getText().toString();
                        }
                    } catch (Exception e) {
                        Mobile2 = fragmentEditProfileBinding.ccp4.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtPrimary2.getText().toString();
                    }
                }

                if (!fragmentEditProfileBinding.edtFax1.getText().toString().equals(""))
                {
                    try
                    {
                        if (fragmentEditProfileBinding.ccp5.getSelectedCountryCodeWithPlus() != null)
                        {
                            Fax1 = fragmentEditProfileBinding.ccp5.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtFax1.getText().toString();
                        }
                    }
                    catch (Exception e)
                    {
                        Fax1 = fragmentEditProfileBinding.ccp5.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtFax1.getText().toString();
                    }
                }

                if (!fragmentEditProfileBinding.edtFax2.getText().toString().equals("")) {
                    try {
                        if (fragmentEditProfileBinding.ccp6.getSelectedCountryCodeWithPlus() != null) {
                            Fax2 = fragmentEditProfileBinding.ccp6.getSelectedCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtFax2.getText().toString();
                        }
                    } catch (Exception e) {
                        Fax2 = fragmentEditProfileBinding.ccp6.getDefaultCountryCodeWithPlus() + " " + fragmentEditProfileBinding.edtFax2.getText().toString();
                    }
                }

                try
                {
                    associationID = AssoIdList.get(fragmentEditProfileBinding.spnAssociation.getSelectedItemPosition()).toString();
                }
                catch (Exception e){}

                if (fragmentEditProfileBinding.edtProfileName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter profile name", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.edtFirstName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.edtLastName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.autoCompleteDesignation.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.autoCompleteCompany.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter company name", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.edtEmail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_LONG).show();
                }
                else if (fragmentEditProfileBinding.edtPrimary.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter your mobile number.", Toast.LENGTH_LONG).show();
                }
                else {

                    if (type.equals("add")) {
                        new HttpAsyncTaskAddProfile().execute(Utility.BASE_URL + "AddProfile");
                    } else if (type.equals("edit")) {
                        new HttpAsyncTask().execute(Utility.BASE_URL + "UpdateProfile");
                    }
                }

                break;
            case R.id.imgYoutube:
                Utility.freeMemory();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
                final EditText input = new EditText(EditProfileActivity.this);
                input.setHint("Enter Youtube Url");
                alertDialog.setMessage("Youtube");
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        if (input.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Enter youtube url", Toast.LENGTH_LONG).show();
                        }
                        else {
                            dialog.dismiss();
                            strYoutube = input.getText().toString();
                        }
                        // new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.ivAttachFile:
                /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);*/
                Utility.freeMemory();
                selectFile();
                break;
            case R.id.ivTeleAdd:
                Utility.freeMemory();
                if (fragmentEditProfileBinding.llTelephoneBox1.getVisibility() == View.GONE)
                {
                    fragmentEditProfileBinding.llTelephoneBox2.setVisibility(View.VISIBLE);
                    fragmentEditProfileBinding.ivTeleAdd.setImageResource(R.drawable.ic_minus_blue);
                }
                else
                {
                    fragmentEditProfileBinding.llTelephoneBox2.setVisibility(View.GONE);
                    fragmentEditProfileBinding.ivTeleAdd.setImageResource(R.drawable.ic_plus_blue);
                }
                break;
            case R.id.ivMobAdd:
                if (fragmentEditProfileBinding.llMobileBox2.getVisibility() == View.GONE)
                {
                    fragmentEditProfileBinding.llMobileBox2.setVisibility(View.VISIBLE);
                    fragmentEditProfileBinding.ivMobAdd.setImageResource(R.drawable.ic_minus_blue);
                }
                else
                {
                    fragmentEditProfileBinding.llMobileBox2.setVisibility(View.GONE);
                    fragmentEditProfileBinding.ivMobAdd.setImageResource(R.drawable.ic_plus_blue);
                }
                break;
            case R.id.ivFaxAdd:
                if (fragmentEditProfileBinding.llFaxBox2.getVisibility() == View.GONE)
                {
                    fragmentEditProfileBinding.llFaxBox2.setVisibility(View.VISIBLE);
                    fragmentEditProfileBinding.ivFaxAdd.setImageResource(R.drawable.ic_minus_blue);
                }
                else
                {
                    fragmentEditProfileBinding.llFaxBox2.setVisibility(View.GONE);
                    fragmentEditProfileBinding.ivFaxAdd.setImageResource(R.drawable.ic_plus_blue);
                }
                break;
            case R.id.imgGoogle:
                signIn();
                break;
            case R.id.txtAttachDelete:
                fragmentEditProfileBinding.etAttachFile.setText("");
                break;
            case R.id.imgLinkedin:
                LinkedInFlag = true;
                login_linkedin();
                break;
            case R.id.imgAdd:
                Intent intent1 = new Intent(getApplicationContext(), SearchGroupMembers.class);
                intent1.putExtra("from", "profile");
                intent1.putExtra("ProfileId", profileId);
                startActivity(intent1);
                break;
            case R.id.imgFb:
                String loading = "Loading" ;
                CustomProgressDialog(loading);

                fragmentEditProfileBinding.loginButton.performClick();

                fragmentEditProfileBinding.loginButton.setPressed(true);

                fragmentEditProfileBinding.loginButton.invalidate();

                fragmentEditProfileBinding.loginButton.registerCallback(callbackManager, mCallBack);

                fragmentEditProfileBinding.loginButton.setPressed(false);

                fragmentEditProfileBinding.loginButton.invalidate();

                break;
            case R.id.ivAttachFrontImage:
                cardType = "front";
                if (result && result1) {
                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(EditProfileActivity.this);
                }
                break;
            case R.id.ivAttachBackImage:
                cardType = "back";
                if (result && result1) {
                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(EditProfileActivity.this);
                }
                break;
            case R.id.ivArrowImg:
                if (arrowStatus.equalsIgnoreCase("RIGHT"))
                {
                    fragmentEditProfileBinding.ivArrowImg.setImageResource(R.drawable.ic_down_arrow_blue);
                    fragmentEditProfileBinding.recyclerEvents.setVisibility(View.VISIBLE);
                    arrowStatus = "DOWN";
                }
                else if (arrowStatus.equalsIgnoreCase("DOWN"))
                {
                    fragmentEditProfileBinding.ivArrowImg.setImageResource(R.drawable.ic_right_arrow_blue);
                    fragmentEditProfileBinding.recyclerEvents.setVisibility(View.GONE);
                    arrowStatus = "RIGHT";
                }
                break;
            case R.id.ivArrowImg1:
                if (arrowStatus1.equalsIgnoreCase("RIGHT"))
                {
                    fragmentEditProfileBinding.ivArrowImg1.setImageResource(R.drawable.ic_down_arrow_blue);
                    fragmentEditProfileBinding.recyclerAssociation.setVisibility(View.VISIBLE);
                    arrowStatus1 = "DOWN";
                }
                else if (arrowStatus1.equalsIgnoreCase("DOWN"))
                {
                    fragmentEditProfileBinding.ivArrowImg1.setImageResource(R.drawable.ic_right_arrow_blue);
                    fragmentEditProfileBinding.recyclerAssociation.setVisibility(View.GONE);
                    arrowStatus1 = "RIGHT";
                }
                break;
            case R.id.ivProfileDelete:
                Utility.freeMemory();
                AlertDialog.Builder alert = new AlertDialog.Builder(EditProfileActivity.this, R.style.Blue_AlertDialog);
                alert.setMessage("Are you sure want to delete profile?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        dialog.dismiss();
                        new HttpAsyncTaskProfileDelete().execute(Utility.BASE_URL+"DeleteProfile");
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.imgProfileShare:
                Utility.freeMemory();
                String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional newtorks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, fragmentEditProfileBinding.includeTop.tvPersonName.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
                break;
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            populate();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            initUI();
            progressDialog = ProgressDialog.show(EditProfileActivity.this,
                    "ProgressDialog",
                    "Wait for seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {

            //finalResult.setText(text[0]);

        }
    }

    private void populate(){
        new HttpAsyncTaskAssociation().execute(Utility.BASE_URL+"GetAssociationList");
        if (type.equals("edit"))
        {
            Utility.freeMemory();
            new HttpAsyncTaskUserProfile().execute(Utility.BASE_URL+"GetUserProfile");
        }
    }

    public void initUI()
    {
        client = new TwitterAuthClient();
        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);
        mAuth = FirebaseAuth.getInstance();
        profileSession = new ProfileSession(getApplicationContext());
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        fromActivity = intent.getStringExtra("activity");
        profileId = intent.getStringExtra("profile_id");
        allTaggs = new ArrayList<>();
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
        fragmentEditProfileBinding.btnSignIn.setSize(SignInButton.SIZE_ICON_ONLY);
        fragmentEditProfileBinding.btnSignIn.setScopes(gso.getScopeArray());

        array = new String[]{"Accommodations", "Information", "Accounting", "Information technology", "Advertising",
                "Insurance", "Aerospace", "Journalism & News", "Agriculture & Agribusiness", "Legal Services", "Air Transportation",
                "Manufacturing", "Apparel & Accessories", "Media & Broadcasting", "Auto", "Medical Devices & Supplies", "Banking",
                "Motions Picture & Video", "Beauty & Cosmetics", "Music", "Biotechnology", "Pharmaceutical", "Chemical", "Public Administration",
                "Communications", "Public Relations", "Computer", "Publishing", "Construction", "Rail", "Consulting", "Real Estate",
                "Consumer Products", "Retail", "Education", "Service", "Electronics", "Sports", "Employment", "Technology", "Energy",
                "Telecommunications", "Entertainment & Recreation", "Tourism", "Fashion", "Transportation", "Financial Services",
                "Travel", "Fine Arts", "Utilities", "Food & Beverage", "Video Game", "Green Technology", "Web Services", "Health"};
        fragmentEditProfileBinding.gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, array));
        fragmentEditProfileBinding.gridView.setExpanded(true);

        if (addEventList.size() == 0) {
            fragmentEditProfileBinding.tvEventInfo.setVisibility(View.VISIBLE);
        } else {
            fragmentEditProfileBinding.tvEventInfo.setVisibility(View.GONE);
        }

        // generateHashkey();

        camera_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera_permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Camera Permission", "Permission to record denied");
            makeRequest();
        }

        fragmentEditProfileBinding.ccp1.setCountryForNameCode("SG");
        fragmentEditProfileBinding.ccp2.setCountryForNameCode("SG");
        fragmentEditProfileBinding.ccp3.setCountryForNameCode("SG");
        fragmentEditProfileBinding.ccp4.setCountryForNameCode("SG");
        fragmentEditProfileBinding.ccp5.setCountryForNameCode("SG");
        fragmentEditProfileBinding.ccp6.setCountryForNameCode("SG");
        populate();
    }
    @Override
    protected void onPause() {

        super.onPause();
        Utility.freeMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }
    /*  public class LongOperation extends AsyncTask<String, Void, String> {

          @Override
          protected String doInBackground(String... params) {
              initUI();
              return "1";
          }

          @Override
          protected void onPostExecute(String result) {
              if(result.equalsIgnoreCase("1"))
              {


                  populate();
              }
          }



      }*/
    private void handleTwitterSession(TwitterSession session)
    {
        Log.d("TAG", "handleTwitterSession:" + session);
        // [START_EXCLUDE silent]

        showProgressDialog();
        Utility.freeMemory();
       /* String loading = "Google Login" ;

            CustomProgressDialog(loading,activity);*/

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
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*loginSession.createLoginSession("", user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
                            Intent homeIntent = new Intent(LoginActivity.this, CardsActivity.class);
                            homeIntent.putExtra("viewpager_position", 0);
                            startActivity(homeIntent);

                            finish();
*/
                            strTwitter = "https://twitter.com/" + user.getDisplayName();
                            fragmentEditProfileBinding.imgTwitter.setImageResource(R.drawable.icon_twitter);

                            mAuth.signOut();
                            com.twitter.sdk.android.Twitter.logOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           /* Toast.makeText(EditProfileActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();*/
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
                        //hideProgressDialog();
//                    //    dismissProgress();
                       // fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
    }

    public void login_linkedin() {
        Utility.freeMemory();
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                //  Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                // login_linkedin_btn.setVisibility(View.GONE);

            }

            @Override
            public void onAuthError(LIAuthError error) {

                //Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraDialog();
                    Utility.freeMemory();
                }
                return;
            }
        }
    }

    public  String POST9(String url)
    {
        Utility.freeMemory();
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
            jsonObject.accumulate("ProfileID", profileId );
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

    private class HttpAsyncTaskProfileDelete extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Deleting Profile...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Deleting profile" ;

            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST9(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
            try
            {

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equalsIgnoreCase("1")){
                        profileSession.createProfileSession("0");
                        finish();
                        Toast.makeText(getApplicationContext(), "Profile deleted successfully..", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to delete profile..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void openCameraDialog() {
        Utility.freeMemory();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
       /* DialogFragment newFragment = CameraDialogFragment.newInstance();
        newFragment.show(ft, "dialog");*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("TAG", "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            if (mGoogleApiClient.isConnected()) {
                if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                    if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                        Log.e("TAG", "display name: " + acct.getDisplayName());

                        String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
                        String email = acct.getEmail();
                        String personPhotoUrl = currentPerson.getImage().getUrl();
                        Log.e("TAG", "Name: " + personName + ", email: " + email
                        );
                        strGoogle = "https://plus.google.com/" + acct.getId()+"/";
                        fragmentEditProfileBinding.imgGoogle.setImageResource(R.drawable.icon_google);
                        signOut();
                    }
                }
            } else {
                //connect it
                mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);

                updateUI(true);
            }
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<com.google.android.gms.common.api.Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult)
        {

//            progressDialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                strFB = "http://www.facebook.com/" + object.getString("id").toString();
                                // Toast.makeText(getApplicationContext(), strFB, Toast.LENGTH_LONG).show();
                                fragmentEditProfileBinding.imgFb.setImageResource(R.drawable.icon_fb);
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
        public void onCancel()
        {
//            progressDialog.dismiss();
            //dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
        }

        @Override
        public void onError(FacebookException e)
        {
//            progressDialog.dismiss();
          //  dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        // initUI();
        //
        // populate();
        callbackManager = CallbackManager.Factory.create();


        fragmentEditProfileBinding.loginButton.setReadPermissions("public_profile", "email");

        fragmentEditProfileBinding.imgFb.setOnClickListener(this);
    }


    private void selectImage() {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(EditProfileActivity.this);
                boolean result1 = Utility.checkCameraPermission(EditProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result1) {
//                        activeTakePhoto();
                        //   cameraCardIntent();
                      /*  FrameScanBotCamera.setVisibility(View.VISIBLE);
                        cameraView.startPreview();
                       // getSupportActionBar().hide();
                        contourDetectorFrameHandler = ContourDetectorFrameHandler.attach(cameraView);

                        // Please note: https://github.com/doo/Scanbot-SDK-Examples/wiki/Detecting-and-drawing-contours#contour-detection-parameters
                        contourDetectorFrameHandler.setAcceptedAngleScore(60);
                        contourDetectorFrameHandler.setAcceptedSizeScore(70);

                        polygonView = (PolygonView) findViewById(R.id.polygonView);
                        contourDetectorFrameHandler.addResultHandler(polygonView);
                        contourDetectorFrameHandler.addResultHandler(EditProfileActivity.this);

                        autoSnappingController = AutoSnappingController.attach(cameraView, contourDetectorFrameHandler);

                        cameraView.addPictureCallback(EditProfileActivity.this);

                        userGuidanceToast = Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT);
                        userGuidanceToast.setGravity(Gravity.CENTER, 0, 0);
                        setAutoSnapEnabled(true);*/

                        /*Intent intent1 = new Intent(getApplicationContext(), ScanbotCamera.class);
                        intent1.putExtra("from", "edit");
                        startActivity(intent1);*/
                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
//                        activeGallery();
                        galleryCardIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryCardIntent() {
        //  rltGallery.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_GALLERY_CARD);
    }

    private void cameraCardIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_CARD);
    }

    public String POST6(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            if (companyID == null){
                companyID = "";
            }

            if (industryID == null){
                industryID = "";
            }

            if (designationID == null){
                designationID = "";
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(1011);

            String name = fragmentEditProfileBinding.edtFirstName.getText().toString()+" "+fragmentEditProfileBinding.edtLastName.getText().toString();
            String kept = name.substring(0, name.indexOf(" "));
            String remainder = name.substring(name.indexOf(" ") + 1, name.length());
            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.put(1);

            List<String> al = new ArrayList<>();
            if (arrayEvents != null) {
                for (int i=0;i<arrayEvents.length();i++){
                    al.add(arrayEvents.getString(i));
                }
            }
// add elements to al, including duplicates
            Set<String> hs = new HashSet<>();
            hs.addAll(al);
            al.clear();
            al.addAll(hs);
            arrayEvents = new JSONArray(al);



            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Address1", fragmentEditProfileBinding.edtAddress1.getText().toString());
            jsonObject.accumulate("Address2", fragmentEditProfileBinding.edtAddress2.getText().toString());
            jsonObject.accumulate("Address3", fragmentEditProfileBinding.edtAddress3.getText().toString() + " " + fragmentEditProfileBinding.edtAddress4.getText().toString());
            jsonObject.accumulate("Address4", fragmentEditProfileBinding.ccpAddress5.getSelectedCountryName().toString() + " " + fragmentEditProfileBinding.edtAddress6.getText().toString());
            jsonObject.accumulate("Address_ID", "1");
            jsonObject.accumulate("Address_Type", "work");
            jsonObject.accumulate("AssociationIDs", arrayAssociation);
            jsonObject.accumulate("Attachment_FileName",fragmentEditProfileBinding.etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", fragmentEditProfileBinding.txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", fragmentEditProfileBinding.txtCardFront.getText().toString());
            jsonObject.accumulate("City", fragmentEditProfileBinding.edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", fragmentEditProfileBinding.edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", companyID);
            jsonObject.accumulate("CompanyName", fragmentEditProfileBinding.autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", fragmentEditProfileBinding.ccpAddress5.getSelectedCountryName().toString());
            jsonObject.accumulate("Designation", fragmentEditProfileBinding.autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", designationID);
            jsonObject.accumulate("Email1", fragmentEditProfileBinding.edtEmail.getText().toString());
            jsonObject.accumulate("Email2", fragmentEditProfileBinding.edtEmail2.getText().toString());
            jsonObject.accumulate("Facebook", strFB);
            jsonObject.accumulate("Fax1", Fax1);
            jsonObject.accumulate("Fax2", Fax2);
            jsonObject.accumulate("Google", strGoogle);
            jsonObject.accumulate("IndustryID", industryID);
            jsonObject.accumulate("IndustryName", fragmentEditProfileBinding.autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", strLinkedin);
            jsonObject.accumulate("Mobile1", Mobile1);
            jsonObject.accumulate("Mobile2", Mobile2);
            jsonObject.accumulate("Phone1", Phone1);
            jsonObject.accumulate("Phone2", Phone2);
            jsonObject.accumulate("Postalcode", fragmentEditProfileBinding.edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", fragmentEditProfileBinding.edtProfileDesc.getText().toString());
            jsonObject.accumulate("Profile_Type", "");
            jsonObject.accumulate("State", fragmentEditProfileBinding.edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", strTwitter);
            jsonObject.accumulate("UserID", UserID);
            jsonObject.accumulate("Website", fragmentEditProfileBinding.edtWebsite.getText().toString());
            jsonObject.accumulate("Youtube", strYoutube);
            jsonObject.accumulate("Event_Cat_IDs", arrayEvents);
            jsonObject.accumulate("ProfileName", fragmentEditProfileBinding.edtProfileName.getText().toString());
            jsonObject.accumulate("UserPhoto", UserPhoto);
            jsonObject.accumulate("FirstName", fragmentEditProfileBinding.edtFirstName.getText().toString());
            jsonObject.accumulate("LastName", fragmentEditProfileBinding.edtLastName.getText().toString());


         /*   jsonObject.accumulate("Facebook", strFB);
            jsonObject.accumulate("Google", strGoogle);
            jsonObject.accumulate("IndustryID", industryID);
            jsonObject.accumulate("IndustryName", autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", strLinkedin);
            jsonObject.accumulate("Phone", edtWork.getText().toString());
            jsonObject.accumulate("Phone_type", "work");
            jsonObject.accumulate("Postalcode", edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", ProfileDesc);
            jsonObject.accumulate("Profile_Type", "work");
            jsonObject.accumulate("State", edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", strTwitter);
            jsonObject.accumulate("UserID", UserID);
            jsonObject.accumulate("Website", edtWebsite.getText().toString());*/

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

    public String POST4(String url) {
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
            jsonObject.accumulate("ProfileId", profileId);
            jsonObject.accumulate("numofrecords", "10");
            jsonObject.accumulate("pageno", "1");

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


    public String POST3(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

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

    public static String POST10(String url) {
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64);
            jsonObject.accumulate("classification", "userphoto");

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


    public static String POST7(String url) {
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64);
            jsonObject.accumulate("classification", "card");

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

    public String POST8(String url) {
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
            jsonObject.accumulate("FileName", fragmentEditProfileBinding.etAttachFile.getText().toString());
            jsonObject.accumulate("ImgBase64", Attach_String);
            jsonObject.accumulate("classification", "others");

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

    public String POST1(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

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

    public String POST5(String url) {
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
            jsonObject.accumulate("profileid", profileId);
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

    public String POST2(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

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

    public String PostAssociate(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            List<String> stringList = new ArrayList<String>(Arrays.asList(array));
            eventList = new ArrayList<AssociationModel>();
            for (int i = 0; i < array.length; i++) {
                Utility.freeMemory();
                AssociationModel st = new AssociationModel(String.valueOf(i), stringList.get(i), false );

                eventList.add(st);

            }
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

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            if (companyID == null){
                companyID = "";
            }

            if (industryID == null){
                industryID = "";
            }

            if (designationID == null){
                designationID = "";
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(1011);

            List<String> al = new ArrayList<>();
            if (arrayEvents != null) {
                for (int i=0;i<arrayEvents.length();i++){
                    al.add(arrayEvents.getString(i));
                }
            }
// add elements to al, including duplicates
            Set<String> hs = new HashSet<>();
            hs.addAll(al);
            al.clear();
            al.addAll(hs);
            arrayEvents = new JSONArray(al);

//            String name = edtUserName.getText().toString();
            String name = fragmentEditProfileBinding.edtFirstName.getText().toString()+" "+fragmentEditProfileBinding.edtLastName.getText().toString();
            String kept = name.substring(0, name.indexOf(" "));
            String remainder = name.substring(name.indexOf(" ") + 1, name.length());
            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.put(1);
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Address1", fragmentEditProfileBinding.edtAddress1.getText().toString());
            jsonObject.accumulate("Address2", fragmentEditProfileBinding.edtAddress2.getText().toString());
            jsonObject.accumulate("Address3", fragmentEditProfileBinding.edtAddress3.getText().toString() + " " + fragmentEditProfileBinding.edtAddress4.getText().toString());
            jsonObject.accumulate("Address4", fragmentEditProfileBinding.ccpAddress5.getSelectedCountryName().toString() + " " + fragmentEditProfileBinding.edtAddress6.getText().toString());
            jsonObject.accumulate("Address_ID", "");
            jsonObject.accumulate("Address_Type", "");
            jsonObject.accumulate("AssociationIDs", arrayAssociation);
            jsonObject.accumulate("Attachment_FileName", fragmentEditProfileBinding.etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", fragmentEditProfileBinding.txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", fragmentEditProfileBinding.txtCardFront.getText().toString());
            jsonObject.accumulate("City", fragmentEditProfileBinding.edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", fragmentEditProfileBinding.edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", companyID);
            jsonObject.accumulate("CompanyName", fragmentEditProfileBinding.autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", fragmentEditProfileBinding.ccpAddress5.getSelectedCountryName().toString());
            jsonObject.accumulate("Designation", fragmentEditProfileBinding.autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", designationID);
            jsonObject.accumulate("Email1", fragmentEditProfileBinding.edtEmail.getText().toString());
            jsonObject.accumulate("Email2", fragmentEditProfileBinding.edtEmail2.getText().toString());
            jsonObject.accumulate("Facebook", strFB);
            jsonObject.accumulate("Fax1", Fax1);
            jsonObject.accumulate("Fax2", Fax2);
            jsonObject.accumulate("Google", strGoogle);
            jsonObject.accumulate("IndustryID", industryID);
            jsonObject.accumulate("IndustryName", fragmentEditProfileBinding.autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", strLinkedin);
            jsonObject.accumulate("Mobile1", Mobile1);
            jsonObject.accumulate("Mobile2", Mobile2);
            jsonObject.accumulate("Phone1", Phone1);
            jsonObject.accumulate("Phone2", Phone2);
            jsonObject.accumulate("Postalcode", fragmentEditProfileBinding.edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", fragmentEditProfileBinding.edtProfileDesc.getText().toString());
            jsonObject.accumulate("Profile_Type", "");
            jsonObject.accumulate("State", fragmentEditProfileBinding.edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", strTwitter);
            jsonObject.accumulate("UserID", UserID);
            jsonObject.accumulate("Website", fragmentEditProfileBinding.edtWebsite.getText().toString());
            jsonObject.accumulate("Youtube", strYoutube);
            jsonObject.accumulate("Event_Cat_IDs", arrayEvents);
            jsonObject.accumulate("ProfileName", fragmentEditProfileBinding.edtProfileName.getText().toString());
            jsonObject.accumulate("UserPhoto", UserPhoto);
            jsonObject.accumulate("FirstName", fragmentEditProfileBinding.edtFirstName.getText().toString());
            jsonObject.accumulate("LastName", fragmentEditProfileBinding.edtLastName.getText().toString());

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

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    private void selectFile() {
        items = new CharSequence[]{"Upload Document", "Take Photo","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Attach File");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {
                        cameraIntent();
                    }
                } /*else if (items[item].equals("Choose from Media")) {
                    userChoosenTask = "Choose from Media";
                    if (result) {

                        cropType = "attachment";
                        CropImage.activity(null)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(EditProfileActivity.this);

                       // galleryIntent();
                    }
                } */else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Upload Document")) {
                    userChoosenTask = "Upload Document";
                    if (result) {
                        documentIntent();
                    }
                }/* else if (items[item].equals("Take Audio")) {
                    userChoosenTask = "Take Audio";
                    if (result) {
                        audioIntent();
                    }
                }*/
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void documentIntent() {
        Intent intent = new Intent();
        intent.setType("file//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_DOCUMENT);
    }

    private void audioIntent() {
        /*Intent intent_upload = new Intent();
        intent_upload.setType("audio*//*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,REQUEST_AUDIO);*/

     /*   Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_AUDIO);  */

     /* Intent intent = new Intent();
        intent.setType("audio*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio"), REQUEST_AUDIO); */

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Audio"), REQUEST_AUDIO);
    }

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
            Log.d("TAG", e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("TAG", e.getMessage(), e);
        }
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void setprofile(JSONObject response) {

        try {
            Log.d("response link ", response.toString());
            // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

            strLinkedin = response.get("publicProfileUrl").toString().replaceAll("/", "");

            fragmentEditProfileBinding.imgLinkedin.setImageResource(R.drawable.icon_linkedin);
            LISessionManager.getInstance(getApplicationContext()).clearSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(EditProfileActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    setprofile(result.getResponseDataAsJson());
                    progress.dismiss();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
        if (LinkedInFlag == true)
        {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
            /*progress = new ProgressDialog(this);
            progress.setMessage("Logging in...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();*/

            String loading = "Logging in" ;
            CustomProgressDialog(loading);

            linkededinApiHelper();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
            if (resultCode == PICKFILE_RESULT_CODE) {
                String FilePath = data.getData().getPath();

                File file = new File(FilePath);
                String file_name = file.getName();

                fragmentEditProfileBinding.etAttachFile.setText(file_name);
                try {
                    byte[] data1 = file_name.getBytes("UTF-8");
                    String base64 = Base64.encodeToString(data1, Base64.DEFAULT);
                    //  Toast.makeText(getApplicationContext(), base64, Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_GALLERY) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_DOCUMENT) {
                onSelectFromFiles(data);
            } else if (requestCode == REQUEST_AUDIO) {
                onSelectFromAudio(data);
            }
            if (requestCode == SELECT_GALLERY_CARD)
                onSelectFromGalleryResultCard(data);
            else if (requestCode == REQUEST_CAMERA_CARD)
                onCaptureImageResultCard(data);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result.getUri()));
                    if (bitmap.equals("") || bitmap == null) {
                        bitmap=BitmapFactory.decodeFile(getRealPathFromURI(result.getUri()));
                    }
                    // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    long size = Utility.imageCalculateSize(bitmap);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    if (size > 500000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    }
                    else if (size > 400000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 55, bytes);
                    }
                    else if (size > 300000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                    }
                    else if (size > 200000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    }
                    else if (size > 100000){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                    }
                    else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    }
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    final_ImgBase64 = BitMapToString(bitmap);
                    //   Upload();

                    if (cardType.equals("front")) {
                        CardSwipe.holder.imageView.setImageBitmap(bitmap);
                        new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                    else if (cardType.equals("back")) {
                        CardSwipe.holder.imageView.setImageBitmap(bitmap);
                        new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                   /* else if (cropType.equals("attachment")){
                        Uri imgUri = getImageUri(getApplicationContext(), bitmap);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File imgFile = new File(getRealPathFromURI(imgUri));

                        String imgPath = getRealPathFromURI(imgUri);
                        size_calculate(imgPath);

                      //  size_calculate(picturePath);
                    }*/
                    else {
                        fragmentEditProfileBinding.includeTop.imgProfile.setImageBitmap(bitmap);
                        new HttpAsyncTaskUserUpload().execute(Utility.BASE_URL+"ImgUpload");
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

//            showProgressDialog();

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void showProgressDialog()
    {
       /* if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Google Login..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();*/

        String loading = "Google login" ;
        CustomProgressDialog(loading);

    }




    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            fragmentEditProfileBinding.btnSignIn.setVisibility(View.GONE);
            //loginSession.createLoginSession(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
          /*  Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
            intent.putExtra("viewpager_position", 0);
            startActivity(intent);
            finish();*/
            // btnSignOut.setVisibility(View.VISIBLE);
            //  btnRevokeAccess.setVisibility(View.VISIBLE);
            // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            fragmentEditProfileBinding.btnSignIn.setVisibility(View.VISIBLE);
            //  btnSignOut.setVisibility(View.GONE);
            //  btnRevokeAccess.setVisibility(View.GONE);
            // llProfileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void onCaptureImageResultCard(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final_ImgBase64 = BitMapToString(thumbnail);
        //   Upload();
        CardSwipe.holder.imageView.setImageBitmap(thumbnail);
        if (cardType.equals("front"))
            new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
        else if (cardType.equals("back"))
            new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void onSelectFromGalleryResultCard(Intent data) {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

               /* Intent intent = new Intent(getApplicationContext(), com.circle8.circleOne.ScanBotGallery.MainActivity.class);
                intent.putExtra("bitmap", originalBitmap);
                intent.putExtra("from", "edit");
                startActivity(intent);*/
             /*   editPolygonView.setImageBitmap(originalBitmap);


                new InitImageViewTask().executeOnExecutor(Executors.newSingleThreadExecutor(), originalBitmap);
*/

                // image = ConvertBitmapToString(resizedBitmap);
                /*final_ImgBase64 = BitMapToString(resizedBitmap);
                // final_ImgBase64 = resizeBase64Image(s);
                Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                // Upload();
                CardSwipe.imageView.setImageBitmap(resizedBitmap);
                if (cardType.equals("front"))
                    new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                else if (cardType.equals("back"))
                    new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");*/
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//        BmToString(bm);
    }

    public static void crop(Bitmap bitmap) {
        // crop & warp image by selected polygon (editPolygonView.getPolygon())
       /* final Bitmap documentImage = new ContourDetector().processImageF(
                originalBitmap, editPolygonView.getPolygon(), ContourDetector.IMAGE_FILTER_NONE);

        editPolygonView.setVisibility(View.GONE);
        cropButton.setVisibility(View.GONE);

        resultImageView.setImageBitmap(documentImage);
        resultImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        rltGallery.setVisibility(View.GONE);*/
        final_ImgBase64 = BitMapToString(bitmap);
        // final_ImgBase64 = resizeBase64Image(s);
        Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
        // Upload();
        CardSwipe.holder.imageView.setImageBitmap(bitmap);
        if (cardType.equals("front"))
            new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
        else if (cardType.equals("back"))
            new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
    }

    public String POSTImage(String url) {
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64);
            jsonObject.accumulate("classification", "userphoto");

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

    private void onSelectFromGalleryResult(Intent data) {
       /* Uri selectedImageUri = data.getData();
        String imgPath = getPath(selectedImageUri);

        File imgFile = new File(imgPath);
        String imgName = imgFile.getName();
*/
//        etAttachFile.setText(imgName);
        //call method


        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        size_calculate(picturePath);
        cursor.close();


        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Image too large.", Toast.LENGTH_LONG).show();
            }
        }
//        ivProfileImg.setImageBitmap(bm);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri imgUri = getImageUri(getApplicationContext(), thumbnail);
        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File imgFile = new File(getRealPathFromURI(imgUri));

        String imgPath = getRealPathFromURI(imgUri);

        String imgName = imgFile.getName();

//        etAttachFile.setText(imgName);

        //call method
        size_calculate(imgPath);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ivProfileImg.setImageBitmap(thumbnail);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void onSelectFromFiles(Intent data) {
        String docsPath = data.getData().getPath();
        File docsFile = new File(docsPath);
        String docsName = docsFile.getName();

        size_calculate(docsPath);

        String totalSpace = String.valueOf(docsFile.getTotalSpace());
        String freeSpace = String.valueOf(docsFile.getFreeSpace());
        String usableSpace = String.valueOf(docsFile.getUsableSpace());

        /*float fileSize = docsFile.length();
              fileSize = fileSize / 1024 ;

        String value = null ;

        float final_fileSize = 0;
        float mb_size = 0;

        if(fileSize >= 1024)
        {
            value = (fileSize/1024)+"MB";

            mb_size = fileSize/1024 ;
        }
        else
        {
            value = (fileSize)+"KB";

            final_fileSize = fileSize ;
        }

        if(mb_size > 3.00)
        {
//            Toast.makeText(getApplicationContext(),"File is greater than 3MB"+mb_size,Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setTitle("Warning!");
            alertDialogBuilder.setMessage("Please select file less than 3MB.");
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else
        {
            etAttachFile.setText(docsName);
        }*/

//        Toast.makeText(getApplicationContext(),"Space:- \nTotal: "+totalSpace+
//                "\n Free: "+freeSpace+"\n Usable: "+usableSpace+"\n Size: "+value+"\n Final Size: "+final_fileSize,Toast.LENGTH_LONG).show();

    }

    private void onSelectFromAudio(Intent data) {
        //the selected audio.
//        Uri uri = data.getData();
       /* String audioPath = data.getData().getPath();
        File audioFile = new File(audioPath);
        String audioName = audioFile.getName();
        etAttachFile.setText(audioName);

        Toast.makeText(getApplicationContext(),"Audio path: "+audioPath,Toast.LENGTH_LONG).show();*/

//        mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        Uri selectedImageUri = data.getData();
        String audioPath = getPath(selectedImageUri);

        File audioFile = new File(audioPath);
        String audioName = audioFile.getName();

//        etAttachFile.setText(audioName);

        size_calculate(audioPath);
    }

    public void size_calculate(String file_Path) {
        File n_file = new File(file_Path);
        String fileName = n_file.getName();

        float fileSize = n_file.length();
        fileSize = fileSize / 1024;

        String value = null;

        float final_fileSize = 0;
        float mb_size = 0;

        if (fileSize >= 1024) {
            value = (fileSize / 1024) + "MB";
            mb_size = fileSize / 1024;
        } else {
            value = (fileSize) + "KB";
            final_fileSize = fileSize;
        }

        if (mb_size > 3.00) {
            Toast.makeText(getApplicationContext(), "File is greater than 3MB" + mb_size, Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setTitle("Sorry :");
            alertDialogBuilder.setMessage("Please select a file not more than 3MB.");
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fragmentEditProfileBinding.etAttachFile.setText("Attachment Name");
                                    dialog.dismiss();
                                    selectFile();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            // ivAttachFile.setEnabled(false);
            fragmentEditProfileBinding.etAttachFile.setText(fileName);
            File imgFile = new File(fileName);
            new HttpAsyncTaskDocUpload().execute(Utility.BASE_URL+"ImgUpload");
            try {
                byte[] data = fileName.getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Attach_String = base64;
                // Toast.makeText(getApplicationContext(), base64, Toast.LENGTH_LONG).show();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private void createFile() {
        final File file = new File(Environment.getExternalStorageDirectory(), "read.me");
        Uri uri = Uri.fromFile(file);
        File auxFile = new File(uri.toString());
        assertEquals(file.getAbsolutePath(), auxFile.getAbsolutePath());

       /* new File(uri.getPath());
//        or
        new File(uri.toString());

//        NOTE: url.toString() return a String in the format: "file:///mnt/sdcard/myPicture.jpg",
// whereas url.getPath() returns a String in the format: "/mnt/sdcard/myPicture.jpg",  */

        // and
//        new File(new URI(androidURI.toString()));

    }

    private class HttpAsyncTaskAssociation extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Get Association..");
            dialog.show();
            dialog.setCancelable(false);*/

            /*String loading = "Get Association" ;

            CustomProgressDialog(loading,activity);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostAssociate(urls[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
//          dismissProgress();
           // fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {

                mAdapter1 = new CardViewDataAdapter(eventList);
                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(EditProfileActivity.this, 5, GridLayoutManager.HORIZONTAL, false);
                fragmentEditProfileBinding.recyclerEvents.setAdapter(mAdapter1);
                fragmentEditProfileBinding.recyclerEvents.setLayoutManager(gridLayoutManager1);
                if (result != null) {
                    associationList = new ArrayList<AssociationModel>();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("association");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        AssociationModel st = new AssociationModel(object.getString("AssociationID"), object.getString("AssociationName"), false );

                        associationList.add(st);


                        association_NAME = object.getString("AssociationName");
                        association_ID = object.getString("AssociationID");

                        AssoIdList.add(association_ID);
                        AssoNameList.add(association_NAME);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this,
                                android.R.layout.simple_spinner_item, AssoNameList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fragmentEditProfileBinding.spnAssociation.setAdapter(dataAdapter);

                        mAdapter = new CardViewDataAdapter(associationList);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditProfileActivity.this, 5, GridLayoutManager.HORIZONTAL, false);
                        fragmentEditProfileBinding.recyclerAssociation.setAdapter(mAdapter);
                        fragmentEditProfileBinding.recyclerAssociation.setLayoutManager(gridLayoutManager);


                    }
                } else {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class HttpAsyncTaskAddProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Creating Profile..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Creating profile" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST6(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);

//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String UserID = jsonObject.getString("UserID");
                    String ProfileID = jsonObject.getString("ProfileID");
                    profileSession.createProfileSession("0");
                    if (success.equalsIgnoreCase("1")) {

                        if (fromActivity.equalsIgnoreCase("manage")){
                            Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                           /* Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                            // you pass the position you want the viewpager to show in the extra,
                            // please don't forget to define and initialize the position variable
                            // properly
                            go.putExtra("viewpager_position", 3);
                            startActivity(go);*/
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String> {
        // ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Testimonials...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST4(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Testimonials");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0) {
                        fragmentEditProfileBinding.lstTestimonial.setVisibility(View.GONE);
                        fragmentEditProfileBinding.txtMore.setVisibility(View.GONE);
                        fragmentEditProfileBinding.txtTestimonial.setVisibility(View.VISIBLE);
                    } else {
                        fragmentEditProfileBinding.lstTestimonial.setVisibility(View.VISIBLE);
                        fragmentEditProfileBinding.txtMore.setVisibility(View.VISIBLE);
                        fragmentEditProfileBinding.txtTestimonial.setVisibility(View.GONE);
                    }
                    allTaggs.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (i < 3) {
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
                            //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                            allTaggs.add(nfcModelTag);
                        }
                    }
                    customAdapter = new CustomAdapter(EditProfileActivity.this, allTaggs);
                    fragmentEditProfileBinding.lstTestimonial.setAdapter(customAdapter);
                    fragmentEditProfileBinding.lstTestimonial.setExpanded(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load testimonial..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class HttpAsyncTaskDesignation extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST3(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("designation");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    designation = new ArrayList<>();
                    designation_id = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        designation.add(object.getString("DesignationName"));
                        designation_id.add(object.getString("DesignationID"));
                    }
                } else {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new
                    ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_list_item_1, designation);

            fragmentEditProfileBinding.autoCompleteDesignation.setAdapter(adapter);
        }
    }

    private class HttpAsyncTaskCompany extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST1(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("company");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    company = new ArrayList<>();
                    company_id = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        company.add(object.getString("CompanyName"));
                        company_id.add(object.getString("CompanyID"));
                        // companyID = object.getString("CompanyID");
                    }
                } else {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new
                    ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_list_item_1, company);

            fragmentEditProfileBinding.autoCompleteCompany.setAdapter(adapter);
        }
    }

    private class HttpAsyncTaskUserProfile extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Profile loading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST5(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
          //  dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);

//          dismissProgress();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL+"Testimonial/Fetch");

            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    Card_Front = jsonObject.getString("Card_Front");
                    Card_Back = jsonObject.getString("Card_Back");
                    FirstName = jsonObject.getString("FirstName");
                    LastName = jsonObject.getString("LastName");
                    UserPhoto = jsonObject.getString("UserPhoto");

                    Phone1 = jsonObject.getString("Phone1");
                    Phone2 = jsonObject.getString("Phone2");
                    Mobile1 = jsonObject.getString("Mobile1");
                    Mobile2 = jsonObject.getString("Mobile2");
                    Fax1 = jsonObject.getString("Fax1");
                    Fax2 = jsonObject.getString("Fax2");
                    Email1 = jsonObject.getString("Email1");
                    Email2 = jsonObject.getString("Email2");
                    Facebook = jsonObject.getString("Facebook");
                    strFB = jsonObject.getString("Facebook");
                    Twitter = jsonObject.getString("Twitter");
                    strTwitter = jsonObject.getString("Twitter");
                    Google = jsonObject.getString("Google");
                    strGoogle = jsonObject.getString("Google");
                    LinkedIn = jsonObject.getString("LinkedIn");
                    Youtube = jsonObject.getString("Youtube");
                    strYoutube = jsonObject.getString("Youtube");
                    strLinkedin = jsonObject.getString("LinkedIn");
                    IndustryName = jsonObject.getString("IndustryName");
                    CompanyName = jsonObject.getString("CompanyName");
                    CompanyProfile = jsonObject.getString("CompanyProfile");
                    Designation = jsonObject.getString("Designation");
                    ProfileDesc = jsonObject.getString("ProfileDesc");
                    Status = jsonObject.getString("Status");
                    Address1 = jsonObject.getString("Address1");
                    Address2 = jsonObject.getString("Address2");
                    Address3 = jsonObject.getString("Address3");
                    Address4 = jsonObject.getString("Address4");
                    City = jsonObject.getString("City");
                    State = jsonObject.getString("State");
                    Country = jsonObject.getString("Country");
                    Postalcode = jsonObject.getString("Postalcode");
                    Website = jsonObject.getString("Website");
                    Attachment_FileName = jsonObject.getString("Attachment_FileName");
                    ProfileName = jsonObject.getString("ProfileName");
                    final_associationIdArray = new ArrayList<>();
                    final_associationNameArray = new ArrayList<>();
                    final_eventIdArray = new ArrayList<>();
                    final_eventNameArray = new ArrayList<>();


                    try {
                        JSONArray jsonArrayAsso = jsonObject.getJSONArray("Association_Name");
                        if (jsonArrayAsso != null) {

                            for (int i = 0; i < jsonArrayAsso.length(); i++) {
                                String name = jsonArrayAsso.getString(i).toString();
                                String kept = name.substring(0, name.indexOf(":"));
                                String remainder = name.substring(name.indexOf(":") + 1, name.length());

                                final_associationIdArray.add(kept);
                                final_associationNameArray.add(remainder);

                            }

                            try {

                                for (int i = 0; i < final_associationNameArray.size(); i++) {

                                    if (AssoNameList.contains(final_associationNameArray.get(i).toString())) {
                                        int i1 = AssoNameList.indexOf(final_associationNameArray.get(i).toString());
                                        AssociationModel st = new AssociationModel(final_associationIdArray.get(i).toString(), final_associationNameArray.get(i).toString(), true);
                                        associationList.remove(i1);
                                        associationList.add(i1, st);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditProfileActivity.this, 5, GridLayoutManager.HORIZONTAL, false);
                                        fragmentEditProfileBinding.recyclerAssociation.setAdapter(mAdapter);
                                        fragmentEditProfileBinding.recyclerAssociation.setLayoutManager(gridLayoutManager);
                                    }
                                }
                            }catch (Exception e){}

                        }
                    }catch (Exception e){}


                    try{
                        JSONArray jsonArrayEvents = jsonObject.getJSONArray("Event_Cat_Name");
                        if (jsonArrayEvents != null) {
                            for (int i = 0; i < jsonArrayEvents.length(); i++) {
                                String name = jsonArrayEvents.getString(i).toString();
                                String kept = name.substring(0, name.indexOf(":"));
                                String remainder = name.substring(name.indexOf(":") + 1, name.length());

                                final_eventIdArray.add(kept);
                                final_eventNameArray.add(remainder);

                            }

                            try{
                                List<String> stringList = new ArrayList<String>(Arrays.asList(array));
                                for (int i = 0; i < final_eventNameArray.size(); i++) {

                                    if (stringList.contains(final_eventNameArray.get(i).toString())){
                                        int i1 = stringList.indexOf(final_eventNameArray.get(i).toString());
                                        AssociationModel st = new AssociationModel(final_eventIdArray.get(i).toString(), final_eventNameArray.get(i).toString(), true );
                                        eventList.remove(i1);
                                        eventList.add(i1, st);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(EditProfileActivity.this, 5, GridLayoutManager.HORIZONTAL, false);
                                        fragmentEditProfileBinding.recyclerEvents.setAdapter(mAdapter1);
                                        fragmentEditProfileBinding.recyclerEvents.setLayoutManager(gridLayoutManager);
                                    }
                                }
                            }catch (Exception e){}
                        }
                    }catch (Exception e){}


                    if (Phone1.contains(" ")){
                        String name = Phone1;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        fragmentEditProfileBinding.ccp1.setCountryForPhoneCode(Integer.parseInt(kept));
                        fragmentEditProfileBinding.edtWork.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtWork.setText(Phone1);
                    }

                    if (Phone2.contains(" ")){
                        String name = Phone2;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        fragmentEditProfileBinding.ccp2.setCountryForPhoneCode(Integer.parseInt(kept));
                        fragmentEditProfileBinding.edtWork2.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtWork2.setText(Phone2);
                    }

                    if (Mobile1.contains(" ")){
                        String name = Mobile1;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        try {
                            fragmentEditProfileBinding.ccp3.setCountryForPhoneCode(Integer.parseInt(kept));
                        }catch (Exception e){

                        }
                        fragmentEditProfileBinding.edtPrimary.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtPrimary.setText(Mobile1);
                    }

                    if (Mobile2.contains(" ")){
                        String name = Mobile2;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        fragmentEditProfileBinding.ccp4.setCountryForPhoneCode(Integer.parseInt(kept));
                        fragmentEditProfileBinding.edtPrimary2.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtPrimary2.setText(Mobile2);
                    }

                    if (Fax1.contains(" ")){
                        String name = Fax1;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        fragmentEditProfileBinding.ccp5.setCountryForPhoneCode(Integer.parseInt(kept));
                        fragmentEditProfileBinding.edtFax1.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtFax1.setText(Fax1);
                    }

                    if (Fax2.contains(" ")){
                        String name = Fax2;
                        String kept = name.substring(0, name.indexOf(" "));
                        String remainder = name.substring(name.indexOf(" ") + 1, name.length());
                        kept = kept.replaceAll("//+", "");
                        fragmentEditProfileBinding.ccp6.setCountryForPhoneCode(Integer.parseInt(kept));
                        fragmentEditProfileBinding.edtFax2.setText(remainder);
                    }
                    else {
                        fragmentEditProfileBinding.edtFax2.setText(Fax2);
                    }

                    fragmentEditProfileBinding.edtProfileName.setText(ProfileName);
                    try {

                        int selectedPos = company.indexOf((String) CompanyName);
                        companyID = company_id.get(selectedPos);
                    } catch (Exception e) {

                    }
                    try {

                        int selectedPos1 = designation.indexOf((String) Designation);
                        designationID = designation_id.get(selectedPos1);
                    } catch (Exception e) {

                    }

                    try {

                        int selectedPos2 = industry.indexOf((String) IndustryName);
                        industryID = industry_id.get(selectedPos2);
                    } catch (Exception e) {

                    }


                    if (strFB.equals("") || strFB.equals(null))
                    {
                        fragmentEditProfileBinding.imgFb.setImageResource(R.drawable.ic_fb_gray);
                    }
                    else {
                        fragmentEditProfileBinding.imgFb.setImageResource(R.drawable.icon_fb);
                    }

                    if (strYoutube.equals("") || strYoutube.equals(null))
                    {
                        fragmentEditProfileBinding.imgYoutube.setImageResource(R.drawable.icon_utube_gray);
                    }
                    else {
                        fragmentEditProfileBinding.imgYoutube.setImageResource(R.drawable.icon_utube_red);
                    }

                    if (strGoogle.equals("") || strGoogle.equals(null))
                    {
                        fragmentEditProfileBinding.imgGoogle.setImageResource(R.drawable.ic_google_gray);
                    }
                    else {
                        fragmentEditProfileBinding.imgGoogle.setImageResource(R.drawable.icon_google);
                    }

                    if (strTwitter.equals("") || strTwitter.equals(null))
                    {
                        fragmentEditProfileBinding.imgTwitter.setImageResource(R.drawable.icon_twitter_gray);
                    }
                    else {
                        fragmentEditProfileBinding.imgTwitter.setImageResource(R.drawable.icon_twitter);
                    }

                    if (strLinkedin.equals("") || strLinkedin.equals(null))
                    {
                        fragmentEditProfileBinding.imgLinkedin.setImageResource(R.drawable.icon_linkedin_gray);
                    }
                    else {
                        fragmentEditProfileBinding.imgLinkedin.setImageResource(R.drawable.icon_linkedin);
                    }


                    fragmentEditProfileBinding.includeTop.tvCompany.setText(CompanyName);
                    fragmentEditProfileBinding.includeTop.tvDesignation.setText(Designation);
                    fragmentEditProfileBinding.includeTop.tvPersonName.setText(FirstName + " " + LastName);
                    fragmentEditProfileBinding.autoCompleteCompany.setText(CompanyName);
                    fragmentEditProfileBinding.autoCompleteIndustry.setText(IndustryName);
                    fragmentEditProfileBinding.autoCompleteDesignation.setText(Designation);
                    fragmentEditProfileBinding.edtFirstName.setText(FirstName);
                    fragmentEditProfileBinding.edtLastName.setText(LastName);
//                    edtUserName.setText(FirstName + " " + LastName);
                    fragmentEditProfileBinding.edtCompanyDesc.setText(CompanyProfile);
                    fragmentEditProfileBinding.edtProfileDesc.setText(ProfileDesc);
                    fragmentEditProfileBinding.edtEmail.setText(Email1);
                    fragmentEditProfileBinding.edtEmail2.setText(Email2);
                    //  edtPrimary.setText(Mobile1);
                    //  edtWork.setText(Phone1);
                    // edtWork2.setText(Phone2);
                    // edtPrimary2.setText(Mobile2);
                    //   edtFax1.setText(Fax1);
                    //  edtFax2.setText(Fax2);
                    fragmentEditProfileBinding.edtWebsite.setText(Website);
                   /* edtAddress1.setText(Address1 + " " + Address2);
                    edtAddress2.setText(Address3 + " " + Address4);*/
                    fragmentEditProfileBinding.edtAddress1.setText(Address1);
                    fragmentEditProfileBinding.edtAddress2.setText(Address2);
                    fragmentEditProfileBinding.edtAddress3.setText(City);
                    fragmentEditProfileBinding.edtAddress4.setText(State);
                    fragmentEditProfileBinding.edtAddress6.setText(Postalcode);
                    try {
                        String code = CountryCode.findByName(Country).get(0).name();
                        fragmentEditProfileBinding.ccpAddress5.setCountryForNameCode(code);
                    }catch (Exception e){}
                    //   edtAddress5.setText(Country);

                    fragmentEditProfileBinding.etAttachFile.setText(Attachment_FileName);

                   /* if (Card_Front.equalsIgnoreCase("") || Card_Back.equalsIgnoreCase("")) {
                        appbar.setVisibility(View.GONE);
                    } else {
                        appbar.setVisibility(View.VISIBLE);
                    }*/

                    fragmentEditProfileBinding.txtCardFront.setText(Card_Front);
                    fragmentEditProfileBinding.txtCardBack.setText(Card_Back);
                    if (UserPhoto.equals("")) {
                        fragmentEditProfileBinding.includeTop.imgProfile.setImageResource(R.drawable.usr_white1);
                    } else {
                        Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/" + UserPhoto).resize(300,300)
                                .onlyScaleDown().skipMemoryCache().into(fragmentEditProfileBinding.includeTop.imgProfile);
                    }

                   /* image = new ArrayList<>();
                    image.add(Utility.BASE_IMAGE_URL+"Cards/" + Card_Front);
                    image.add(Utility.BASE_IMAGE_URL+"Cards/" + Card_Back);
                    myPager = new CardSwipe(getApplicationContext(), image);*/

                   /* mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(1);
                    //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    mViewPager.setAdapter(myPager);
*/
                   /* viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(1);
                    // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    viewPager1.setAdapter(myPager);*/

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load profile..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskIndustry extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Loading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST2(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           // dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("industry");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    industry = new ArrayList<>();
                    industry_id = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        industry.add(object.getString("IndustryName"));
                        industry_id.add(object.getString("IndustryID"));
                        //industryID = object.getString("IndustryID");
                    }
                } else {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_list_item_1, industry);
            fragmentEditProfileBinding.autoCompleteIndustry.setAdapter(adapter);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Updating Profile..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Updating profile" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
           // dismissProgress();
            Log.e("result",""+result);
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String UserID = jsonObject.getString("UserID");
                    String ProfileID = jsonObject.getString("ProfileID");

                    if (success.equalsIgnoreCase("1"))
                    {

                        if (fromActivity.equalsIgnoreCase("manage"))
                        {
                            Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {


                            //ProfileFragment.callMyProfile();
                            Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                            finish();

                           /* Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);*/

                          /*  ProfileFragment.mHandler = new Handler();

                            ProfileFragment.mHandler.postDelayed(ProfileFragment.m_Runnable,1000);
*/
                           /* new Thread(new Runnable() {

                                @Override
                                public void run() {

                                    // TODO Auto-generated method stub
                                    try {

                                     //   Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                                        Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                                        // you pass the position you want the viewpager to show in the extra,
                                        // please don't forget to define and initialize the position variable
                                        // properly
                                        go.putExtra("viewpager_position", 3);
                                        startActivity(go);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                }


                            }).start();*/
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    private static class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String loading = "Uploading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST7(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
          //  dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        fragmentEditProfileBinding.txtCardFront.setText(ImgName);
                    } else {
                        Toast.makeText(activity, "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskDocUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String loading = "Uploading" ;
            CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST8(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            //dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        fragmentEditProfileBinding.etAttachFile.setText(ImgName);
                    } else {
                        Toast.makeText(getBaseContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private static class HttpAsyncTaskUserUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Uploading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST10(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
          //  dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        Toast.makeText(activity, "uploaded", Toast.LENGTH_LONG).show();
                       /* Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        UserPhoto = ImgName;
                        // fragmentEditProfileBinding.txtCardBack.setText(ImgName);
                    } else {
                        Toast.makeText(activity, "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private static class HttpAsyncTaskBackUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Uploading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST7(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            //dismissProgress();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        // Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        fragmentEditProfileBinding.txtCardBack.setText(ImgName);
                    } else {
                        Toast.makeText(activity, "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskEventList extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Finding Events...");
            dialog.show();*/

            String loading = "Finding events" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return EventListPost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check data connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String numofrecords = response.getString("numofrecords");

                    JSONArray eventList = response.getJSONArray("EventList");

                    if(eventList.length() == 0)
                    {

                    }
                    else
                    {
                        for(int i = 0 ; i <= eventList.length() ; i++ )
                        {
                            JSONObject eList = eventList.getJSONObject(i);
                            EventModel eventModel = new EventModel();
                            eventModel.setEvent_ID(eList.getString("Event_ID"));
                            eventModel.setEvent_Name(eList.getString("Event_Name"));
                            eventModel.setEvent_Type(eList.getString("Event_Type"));
                            eventModel.setEvent_Category_ID(eList.getString("Event_Category_ID"));
                            eventModel.setEvent_Category_Name(eList.getString("Event_Category_Name"));
                            eventModelArrayList.add(eventModel);

                            String Event_ID = eList.getString("Event_ID") ;
                            String Event_Name = eList.getString("Event_Name") ;
                            String Event_Type = eList.getString("Event_Type") ;
                            String Event_Category_ID = eList.getString("Event_Category_ID") ;
                            String Event_Category_Name = eList.getString("Event_Category_Name") ;

                            eventCategoryIDList.add(Event_Category_ID);

                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void CustomProgressDialog(final String loading)
    {
        fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.anticlockwise);
        fragmentEditProfileBinding.imgConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(activity,R.anim.clockwise);
        fragmentEditProfileBinding.imgConnecting2.startAnimation(anim1);
        fragmentEditProfileBinding.txtProgressing.setText(loading+"...");

    }
    public  String EventListPost(String url)
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
            jsonObject.accumulate("my_userid", "" );
            jsonObject.accumulate("numofrecords", "10");
            jsonObject.accumulate("pageno", "1" );

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



   /* private void getFile()
    {
        File file = new File(getPath(uri));
    }*/

   /* public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }*/




}
