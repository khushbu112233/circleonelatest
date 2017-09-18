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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.AddEventAdapter;
import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CustomAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightGridView;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
import com.circle8.circleOne.Utils.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public static TextView tvEventInfo;
    ImageView imgDone, imgAdd, imgFb, imgLinkedin, imgTwitter, imgGoogle, imgYoutube;
    AutoCompleteTextView autoCompleteCompany, autoCompleteDesignation, autoCompleteIndustry;
    //String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    ArrayList<String> company, designation, industry, designation_id, company_id, industry_id;
    String association_ID, association_NAME ;
    String profileId = "", Card_Front = "", Card_Back = "", FirstName = "", LastName = "", UserPhoto = "", Phone1 = "", Phone2 = "", Mobile1 = "", Mobile2 = "",
            Fax1 = "", Fax2 = "", Email1 = "", Email2 = "", Youtube = "",
            Facebook = "", Twitter = "", Google = "", LinkedIn = "", IndustryName = "", CompanyName = "", CompanyProfile = "", Designation = "", ProfileDesc = "", Status = "";
    String Address1 = "", Address2 = "", Address3 = "", Address4 = "", City = "", State = "", Country = "", Postalcode = "", Website = "", Attachment_FileName = "";
    EditText edtUserName, edtWork, edtPrimary, edtEmail, edtProfileDesc, edtCompanyDesc;
    public static ViewPager mViewPager, viewPager1;
    CircleImageView imgProfile;
    TextView tvPersonName, tvDesignation, tvCompany;
    ExpandableHeightListView lstTestimonial;
    TextView txtTestimonial, txtMore;
    CustomAdapter customAdapter;
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    String type = "";
    private ProgressDialog progressDialog;
    EditText edtAddress1, edtAddress2, edtAddress3, edtAddress4, edtAddress5, edtAddress6, edtWebsite, etAssociationName;
    String UserID = "";
    ImageView imgBack;
    private ExpandableHeightGridView gridView, gridViewAdded;
    private String[] array;
    private EditText etAttachFile;
    private ImageView ivAttachFile;
    private CharSequence[] items;
    private String userChoosenTask;
    private String file_path = "";
    private File file;
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1, REQUEST_DOCUMENT = 2, REQUEST_AUDIO = 3;
    private int camera_permission;
    private ArrayList<String> image = new ArrayList<>();
    private CardSwipe myPager;
    private String addEventString;
    private LinearLayout llEventBox;
    private ImageView ivArrowImg;
    private String arrowStatus = "RIGHT";
    private LoginSession session;
    private int SELECT_GALLERY_CARD = 500;
    private int REQUEST_CAMERA_CARD = 501;
    private String final_ImgBase64 = "";
    private int cardposition = 0;
    ImageView ivAttachBackImage, ivAttachFrontImage, ivAddAssociate;
    TextView txtCardFront, txtCardBack;
    String cardType = "";
    String Attach_String = "";
    String companyID, designationID, industryID, associationID, addressID ;
    private ProgressDialog mProgressDialog;
    ArrayList<String> AssoNameList = new ArrayList<>();
    ArrayList<String> AssoIdList = new ArrayList<>();
    Spinner spnAssociation;
    String strFB = "", strLinkedin = "", strGoogle = "", strTwitter = "", strYoutube = "";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    EditText edtWork2, edtPrimary2, edtEmail2, edtFax1, edtFax2;

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_edit_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        edtWork2 = (EditText) findViewById(R.id.edtWork2);
        edtPrimary2 = (EditText) findViewById(R.id.edtPrimary2);
        edtEmail2 = (EditText) findViewById(R.id.edtEmail2);
        edtFax1 = (EditText) findViewById(R.id.edtFax1);
        edtFax2 = (EditText) findViewById(R.id.edtFax2);

        autoCompleteCompany = (AutoCompleteTextView) findViewById(R.id.autoCompleteCompany);
        autoCompleteDesignation = (AutoCompleteTextView) findViewById(R.id.autoCompleteDesignation);
        autoCompleteIndustry = (AutoCompleteTextView) findViewById(R.id.autoCompleteIndustry);
        gridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
        gridViewAdded = (ExpandableHeightGridView) findViewById(R.id.gridViewAdded);
        etAttachFile = (EditText) findViewById(R.id.etAttachFile);
        ivAttachFile = (ImageView) findViewById(R.id.ivAttachFile);
        session = new LoginSession(getApplicationContext());
        imgDone = (ImageView) findViewById(R.id.imgDone);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtAddress1 = (EditText) findViewById(R.id.edtAddress1);
        edtAddress2 = (EditText) findViewById(R.id.edtAddress2);
        edtAddress3 = (EditText) findViewById(R.id.edtAddress3);
        edtAddress4 = (EditText) findViewById(R.id.edtAddress4);
        edtAddress5 = (EditText) findViewById(R.id.edtAddress5);
        edtAddress6 = (EditText) findViewById(R.id.edtAddress6);
        edtWork = (EditText) findViewById(R.id.edtWork);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtProfileDesc = (EditText) findViewById(R.id.edtProfileDesc);
        edtCompanyDesc = (EditText) findViewById(R.id.edtCompanyDesc);
        edtPrimary = (EditText) findViewById(R.id.edtPrimary);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) findViewById(R.id.viewPager1);
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvDesignation = (TextView) findViewById(R.id.tvDesignation);
        tvPersonName = (TextView) findViewById(R.id.tvPersonName);
        lstTestimonial = (ExpandableHeightListView) findViewById(R.id.lstTestimonial);
        txtTestimonial = (TextView) findViewById(R.id.txtTestimonial);
        txtMore = (TextView) findViewById(R.id.txtMore);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        ivArrowImg = (ImageView) findViewById(R.id.ivArrowImg);
        tvEventInfo = (TextView) findViewById(R.id.tvEventInfo);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        llEventBox = (LinearLayout) findViewById(R.id.llEventBox);
        ivAttachBackImage = (ImageView) findViewById(R.id.ivAttachBackImage);
        ivAttachFrontImage = (ImageView) findViewById(R.id.ivAttachFrontImage);
        txtCardFront = (TextView) findViewById(R.id.txtCardFront);
        txtCardBack = (TextView) findViewById(R.id.txtCardBack);
        ivAddAssociate = (ImageView)findViewById(R.id.ivAddAssociate);
      //  etAssociationName = (EditText)findViewById(R.id.etAssociationName);
        spnAssociation = (Spinner) findViewById(R.id.spnAssociation);
        imgYoutube = (ImageView) findViewById(R.id.imgYoutube);
        imgGoogle = (ImageView) findViewById(R.id.imgGoogle);
        imgTwitter = (ImageView) findViewById(R.id.imgTwitter);
        imgLinkedin = (ImageView) findViewById(R.id.imgLinkedin);
        imgFb = (ImageView) findViewById(R.id.imgFb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        profileId = intent.getStringExtra("profile_id");
        allTaggs = new ArrayList<>();

        btnSignIn.setOnClickListener(this);
        imgGoogle.setOnClickListener(this);
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
        btnSignIn.setSize(SignInButton.SIZE_ICON_ONLY);
        btnSignIn.setScopes(gso.getScopeArray());


        array = new String[]{"Accommodations", "Information", "Accounting", "Information technology", "Advertising",
                "Insurance", "Aerospace", "Journalism & News", "Agriculture & Agribusiness", "Legal Services", "Air Transportation",
                "Manufacturing", "Apparel & Accessories", "Media & Broadcasting", "Auto", "Medical Devices & Supplies", "Banking",
                "Motions Picture & Video", "Beauty & Cosmetics", "Music", "Biotechnology", "Pharmaceutical", "Chemical", "Public Administration",
                "Communications", "Public Relations", "Computer", "Publishing", "Construction", "Rail", "Consulting", "Real Estate",
                "Consumer Products", "Retail", "Education", "Service", "Electronics", "Sports", "Employment", "Technology", "Energy",
                "Telecommunications", "Entertainment & Recreation", "Tourism", "Fashion", "Transportation", "Financial Services",
                "Travel", "Fine Arts", "Utilities", "Food & Beverage", "Video Game", "Green Technology", "Web Services", "Health"};
        gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, array));
        gridView.setExpanded(true);

        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestimonialRequest.class);
                intent.putExtra("ProfileId", profileId);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Toast.makeText(getApplicationContext(), array[position].toString(), Toast.LENGTH_SHORT).show();

                addEventString = array[position].toString();
                addEventList.add(addEventString);

                addEventAdapter = new AddEventAdapter(getApplicationContext(), addEventList);
                gridViewAdded.setAdapter(addEventAdapter);
                gridViewAdded.setExpanded(true);
                addEventAdapter.notifyDataSetChanged();

                if (addEventList.size() != 0) {
                    tvEventInfo.setVisibility(View.GONE);
                } else if (addEventList.size() == 0) {
                    tvEventInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        ivAttachFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardType = "front";
                Intent intent1 = new Intent(getApplicationContext(), ScanbotCamera.class);
                startActivity(intent1);
               // selectImage();
            }
        });

        ivAttachBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardType = "back";
                selectImage();
            }
        });

        if (addEventList.size() == 0) {
            tvEventInfo.setVisibility(View.VISIBLE);
        } else {
            tvEventInfo.setVisibility(View.GONE);
        }

        viewPager1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager1.getCurrentItem() == 0) {
                    selectImage();
                }
            }
        });

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager1.getCurrentItem() == 0) {
                    selectImage();
                }
            }
        });

        ivArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrowStatus.equalsIgnoreCase("RIGHT")) {
                    ivArrowImg.setImageResource(R.drawable.ic_down_arrow_blue);
                    llEventBox.setVisibility(View.VISIBLE);
                    arrowStatus = "DOWN";
                } else if (arrowStatus.equalsIgnoreCase("DOWN")) {
                    ivArrowImg.setImageResource(R.drawable.ic_right_arrow_blue);
                    llEventBox.setVisibility(View.GONE);
                    arrowStatus = "RIGHT";
                }
            }
        });

      /*  ivAddAssociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog alertDialog = new AlertDialog.Builder(EditProfileActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.association_dialog, null);

                ListView listView1 = (ListView)dialogView.findViewById(R.id.listView_Asso);

//                AssociationAdapter associationAdapter = new AssociationAdapter(EditProfileActivity.this, AssoIdList, AssoNameList );
                AssociationAdapter associationAdapter = new AssociationAdapter(EditProfileActivity.this, R.layout.association_value, AssoIdList, AssoNameList );
                listView1.setAdapter(associationAdapter);
                associationAdapter.notifyDataSetChanged();

                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
//                        Toast.makeText(getApplicationContext(),"ID NAME: "+
//                                AssoIdList.get(position)+" "+AssoNameList.get(position),Toast.LENGTH_LONG).show();

                        associationID = AssoIdList.get(position);
                        etAssociationName.setText(AssoNameList.get(position));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
*/
        new HttpAsyncTaskCompany().execute("http://circle8.asia:8081/Onet.svc/GetCompanyList");
        new HttpAsyncTaskIndustry().execute("http://circle8.asia:8081/Onet.svc/GetIndustryList");
        new HttpAsyncTaskDesignation().execute("http://circle8.asia:8081/Onet.svc/GetDesignationList");
        new HttpAsyncTaskAssociation().execute("http://circle8.asia:8081/Onet.svc/GetAssociationList");

        if (type.equals("edit")) {
            new HttpAsyncTaskUserProfile().execute("http://circle8.asia:8081/Onet.svc/GetUserProfile");
            new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Fetch");
        }


        autoCompleteDesignation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                int selectedPos = designation.indexOf((String) autoCompleteDesignation.getText().toString());
                designationID = designation_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

        autoCompleteCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                int selectedPos = company.indexOf((String) autoCompleteCompany.getText().toString());
                companyID = company_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

        autoCompleteIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                int selectedPos = industry.indexOf((String) autoCompleteIndustry.getText().toString());
                industryID = industry_id.get(selectedPos);
                //s1.get(position) is name selected from autocompletetextview
                // now you can show the value on textview.
            }
        });

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                associationID = AssoIdList.get(spnAssociation.getSelectedItemPosition()).toString();
                if (type.equals("add"))
                {
                    new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8081/Onet.svc/AddProfile");
                }
                else if (type.equals("edit"))
                {
                    new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/UpdateProfile");
                }
            }
        });
        generateHashkey();
        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestimonialActivity.class);
                intent.putExtra("ProfileId", profileId);
                intent.putExtra("from", "editprofile");
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager.scrollTo(viewPager1.getScrollX(), viewPager1.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(viewPager1.getCurrentItem(), false);
                }
            }
        });

        ivAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);*/
                selectFile();
            }
        });

        camera_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera_permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Camera Permission", "Permission to record denied");
            makeRequest();
        }
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
                        strGoogle = "https://plus.google.com/"+acct.getId();
                        imgGoogle.setImageResource(R.drawable.icon_google);
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


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                strFB = "https://www.facebook.com/profile.php?id=" + object.getString("id").toString();
                                Toast.makeText(getApplicationContext(), strFB, Toast.LENGTH_LONG).show();
                                imgFb.setImageResource(R.drawable.icon_fb);
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
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email");

        imgFb= (ImageView) findViewById(R.id.imgFb);
        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }


    private void selectImage() {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(EditProfileActivity.this);
                boolean result1 = Utility.checkCameraPermission(EditProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result1)
//                        activeTakePhoto();
                        cameraCardIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
//                        activeGallery();
                        galleryCardIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryCardIntent() {
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

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Address1", edtAddress1.getText().toString());
            jsonObject.accumulate("Address2", edtAddress2.getText().toString());
            jsonObject.accumulate("Address3", edtAddress3.getText().toString() + " " + edtAddress4.getText().toString());
            jsonObject.accumulate("Address4", edtAddress5.getText().toString() + " " + edtAddress6.getText().toString());
            jsonObject.accumulate("Address_ID", "1");
            jsonObject.accumulate("Address_Type", "work");
            jsonObject.accumulate("AssociationID", associationID);
            jsonObject.accumulate("Attachment_FileName", etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", txtCardFront.getText().toString());
            jsonObject.accumulate("City", edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", companyID);
            jsonObject.accumulate("CompanyName", autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", edtAddress5.getText().toString());
            jsonObject.accumulate("Designation", autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", designationID);
            jsonObject.accumulate("Email", edtEmail.getText().toString());
            jsonObject.accumulate("Email_Type", "work");
            jsonObject.accumulate("Facebook", strFB);
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
            jsonObject.accumulate("Website", edtWebsite.getText().toString());

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

    public  String POST7(String url)
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64 );
            jsonObject.accumulate("classification", "card" );

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

    public  String POST8(String url)
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
            jsonObject.accumulate("FileName", etAttachFile.getText().toString() );
            jsonObject.accumulate("ImgBase64", Attach_String );
            jsonObject.accumulate("classification", "others" );

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

    public String POST5(String url)
    {
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

    public String POST2(String url)
    {
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

    public String PostAssociate(String url)
    {
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

    public String POST(String url)
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
            jsonObject.accumulate("Address1", edtAddress1.getText().toString());
            jsonObject.accumulate("Address2", edtAddress2.getText().toString());
            jsonObject.accumulate("Address3", edtAddress3.getText().toString() + " " + edtAddress4.getText().toString());
            jsonObject.accumulate("Address4", edtAddress5.getText().toString() + " " + edtAddress6.getText().toString());
            jsonObject.accumulate("Address_ID", "");
            jsonObject.accumulate("Address_Type", "");
            jsonObject.accumulate("AssociationID", associationID);
            jsonObject.accumulate("Attachment_FileName", etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", txtCardFront.getText().toString());
            jsonObject.accumulate("City", edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", companyID);
            jsonObject.accumulate("CompanyName", autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", edtAddress5.getText().toString());
            jsonObject.accumulate("Designation", autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", designationID);
            jsonObject.accumulate("Email1", edtEmail.getText().toString());
            jsonObject.accumulate("Email2", edtEmail2.getText().toString());
            jsonObject.accumulate("Facebook", strFB);
            jsonObject.accumulate("Fax1", edtFax1.getText().toString());
            jsonObject.accumulate("Fax2", edtFax2.getText().toString());
            jsonObject.accumulate("Google", strGoogle);
            jsonObject.accumulate("IndustryID", industryID);
            jsonObject.accumulate("IndustryName", autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", strLinkedin);
            jsonObject.accumulate("Mobile1", edtPrimary.getText().toString());
            jsonObject.accumulate("Mobile2", edtPrimary2.getText().toString());
            jsonObject.accumulate("Phone1", edtWork.getText().toString());
            jsonObject.accumulate("Phone2", edtWork2.getText().toString());
            jsonObject.accumulate("Postalcode", edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", ProfileDesc);
            jsonObject.accumulate("Profile_Type", "");
            jsonObject.accumulate("State", edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", strTwitter);
            jsonObject.accumulate("UserID", UserID);
            jsonObject.accumulate("Website", edtWebsite.getText().toString());
            jsonObject.accumulate("Youtube", strYoutube);

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
        items = new CharSequence[]{"Take Document", "Take Picture", "Choose from Media", "Take Audio", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Attach File!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfileActivity.this);

                if (items[item].equals("Take Picture")) {
                    userChoosenTask = "Take Picture";
                    if (result) {
                        cameraIntent();
                    }
                } else if (items[item].equals("Choose from Media")) {
                    userChoosenTask = "Choose from Media";
                    if (result) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Take Document")) {
                    userChoosenTask = "Take Document";
                    if (result) {
                        documentIntent();
                    }
                } else if (items[item].equals("Take Audio")) {
                    userChoosenTask = "Take Audio";
                    if (result) {
                        audioIntent();
                    }
                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
            if (resultCode == PICKFILE_RESULT_CODE) {
                String FilePath = data.getData().getPath();

                File file = new File(FilePath);
                String file_name = file.getName();

                etAttachFile.setText(file_name);
                try {
                    byte[] data1 = file_name.getBytes("UTF-8");
                    String base64 = Base64.encodeToString(data1, Base64.DEFAULT);
                    Toast.makeText(getApplicationContext(), base64, Toast.LENGTH_LONG).show();
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
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Google Login..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.imgGoogle:
                signIn();
                break;
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            //loginSession.createLoginSession(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
          /*  Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
            intent.putExtra("viewpager_position", 0);
            startActivity(intent);
            finish();*/
            // btnSignOut.setVisibility(View.VISIBLE);
            //  btnRevokeAccess.setVisibility(View.VISIBLE);
            // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
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
        CardSwipe.imageView.setImageBitmap(thumbnail);
        if (cardType.equals("front"))
            new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
        else if (cardType.equals("back"))
            new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
    }

    public String BitMapToString(Bitmap bitmap) {
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
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                // image = ConvertBitmapToString(resizedBitmap);
                final_ImgBase64 = BitMapToString(resizedBitmap);
                // final_ImgBase64 = resizeBase64Image(s);
                Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                // Upload();
                CardSwipe.imageView.setImageBitmap(resizedBitmap);
                if (cardType.equals("front"))
                    new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
                else if (cardType.equals("back"))
                    new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//        BmToString(bm);
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
        Uri selectedImageUri = data.getData();
        String imgPath = getPath(selectedImageUri);

        File imgFile = new File(imgPath);
        String imgName = imgFile.getName();

//        etAttachFile.setText(imgName);
        //call method
        size_calculate(imgPath);


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
            Toast.makeText(getApplicationContext(),"File is greater than 3MB"+mb_size,Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setTitle("Warning!");
            alertDialogBuilder.setMessage("Please select file less than 3MB.");
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    etAttachFile.setText("Attachment Name");
                                    dialog.dismiss();
                                    selectFile();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            etAttachFile.setText(fileName);
            File imgFile = new File(fileName);
            new HttpAsyncTaskDocUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
            try {
                byte[] data = fileName.getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Attach_String = base64;
                Toast.makeText(getApplicationContext(), base64, Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Get Association..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return PostAssociate(urls[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("association");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        association_NAME = object.getString("AssociationName");
                        association_ID = object.getString("AssociationID");

                        AssoIdList.add(association_ID);
                        AssoNameList.add(association_NAME);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this,
                                android.R.layout.simple_spinner_item, AssoNameList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnAssociation.setAdapter(dataAdapter);
                    }
                }
                else
                {
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
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Creating Profile..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST6(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
           try
           {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String UserID = jsonObject.getString("UserID");
                    String ProfileID = jsonObject.getString("ProfileID");

                    if(success.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Successfully Added",Toast.LENGTH_SHORT).show();
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go.putExtra("viewpager_position", 3);
                        startActivity(go);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                }
           }
           catch (JSONException e1)
           {
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
                        lstTestimonial.setVisibility(View.GONE);
                        txtMore.setVisibility(View.GONE);
                        txtTestimonial.setVisibility(View.VISIBLE);
                    } else  {
                        lstTestimonial.setVisibility(View.VISIBLE);
                        txtMore.setVisibility(View.VISIBLE);
                        txtTestimonial.setVisibility(View.GONE);
                    }
                    allTaggs.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        if (i < 3)
                        {
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
                    lstTestimonial.setAdapter(customAdapter);
                    lstTestimonial.setExpanded(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST3(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
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

            autoCompleteDesignation.setAdapter(adapter);
        }
    }

    private class HttpAsyncTaskCompany extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
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
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
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

            autoCompleteCompany.setAdapter(adapter);
        }
    }

    private class  HttpAsyncTaskUserProfile extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST5(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
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

                    tvCompany.setText(CompanyName);
                    tvDesignation.setText(Designation);
                    tvPersonName.setText(FirstName + " " + LastName);
                    autoCompleteCompany.setText(CompanyName);
                    autoCompleteIndustry.setText(IndustryName);
                    autoCompleteDesignation.setText(Designation);
                    edtUserName.setText(FirstName + " " + LastName);
                    edtCompanyDesc.setText(CompanyProfile);
                    edtProfileDesc.setText(ProfileDesc);
                    edtEmail.setText(Email1);
                    edtPrimary.setText(Mobile1);
                    edtWork.setText(Phone1);
                    edtWork2.setText(Phone2);
                    edtPrimary2.setText(Mobile2);
                    edtEmail2.setText(Email2);
                    edtFax1.setText(Fax1);
                    edtFax2.setText(Fax2);
                    edtWebsite.setText(Website);
                    edtAddress1.setText(Address1 + " "+ Address2);
                    edtAddress2.setText(Address3 + " "+ Address4);
                    edtAddress3.setText(City);
                    edtAddress4.setText(State);
                    edtAddress5.setText(Country);
                    edtAddress6.setText(Postalcode);
                    etAttachFile.setText(Attachment_FileName);

                    txtCardFront.setText(Card_Front);
                    txtCardBack.setText(Card_Back);
                    if (UserPhoto.equals("")) {
                        imgProfile.setImageResource(R.drawable.usr);
                    } else {
                        Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/" + UserPhoto).into(imgProfile);
                    }

                    image = new ArrayList<>();
                    image.add("http://circle8.asia/App_ImgLib/Cards/" + Card_Front);
                    image.add("http://circle8.asia/App_ImgLib/Cards/" + Card_Back);
                    myPager = new CardSwipe(getApplicationContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(1);
                    //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(1);
                    // viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    viewPager1.setAdapter(myPager);

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Profile..", Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls) {
            return POST2(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("industry");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    industry = new ArrayList<>();
                    industry_id = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        industry.add(object.getString("IndustryName"));
                        industry_id.add(object.getString("IndustryID"));
                        //industryID = object.getString("IndustryID");
                    }
                }
                else
                {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_list_item_1, industry);
            autoCompleteIndustry.setAdapter(adapter);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Updating Profile..");
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
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String UserID = jsonObject.getString("UserID");
                    String ProfileID = jsonObject.getString("ProfileID");

                    if(success.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go.putExtra("viewpager_position", 3);
                        startActivity(go);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e1)
            {
                e1.printStackTrace();
            }

        }
    }

    private class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST7(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
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
                        txtCardFront.setText(ImgName);
                    } else {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST8(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
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
                        etAttachFile.setText(ImgName);
                    } else {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private class HttpAsyncTaskBackUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST7(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
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
                        txtCardBack.setText(ImgName);
                    } else {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
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
