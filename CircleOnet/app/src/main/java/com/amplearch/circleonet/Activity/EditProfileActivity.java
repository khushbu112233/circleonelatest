package com.amplearch.circleonet.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.AddEventAdapter;
import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Adapter.CustomAdapter;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.TestimonialModel;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.ExpandableHeightGridView;
import com.amplearch.circleonet.Utils.Utility;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static junit.framework.Assert.assertEquals;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    public static List<TestimonialModel> allTaggs;
    //for adding event and expande & collapse
    public static ArrayList<String> addEventList = new ArrayList<>();
    public static AddEventAdapter addEventAdapter;
    public static TextView tvEventInfo;
    ImageView imgDone;
    AutoCompleteTextView autoCompleteCompany, autoCompleteDesignation, autoCompleteIndustry;
    //String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    ArrayList<String> company, designation, industry;
    String profileId = "", Card_Front = "", Card_Back = "", FirstName = "", LastName = "", UserPhoto = "", OfficePhone = "", PrimaryPhone = "", Emailid = "",
            Facebook = "", Twitter = "", Google = "", IndustryName = "", CompanyName = "", CompanyProfile = "", Designation = "", ProfileDesc = "", Status = "";
    EditText edtUserName, edtWork, edtPrimary, edtEmail, edtProfileDesc, edtCompanyDesc;
    public static ViewPager mViewPager, viewPager1;
    CircleImageView imgProfile;
    TextView tvPersonName, tvDesignation, tvCompany;
    ListView lstTestimonial;
    TextView txtTestimonial, txtMore;
    CustomAdapter customAdapter;
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    ;
    String type = "";
    EditText edtAddress1, edtAddress2, edtAddress3, edtAddress4, edtAddress5, edtAddress6, edtWebsite;
    String UserID = "";
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
    ImageView ivAttachBackImage, ivAttachFrontImage;
    TextView txtCardFront, txtCardBack;
    String cardType = "";

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        autoCompleteCompany = (AutoCompleteTextView) findViewById(R.id.autoCompleteCompany);
        autoCompleteDesignation = (AutoCompleteTextView) findViewById(R.id.autoCompleteDesignation);
        autoCompleteIndustry = (AutoCompleteTextView) findViewById(R.id.autoCompleteIndustry);
        gridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
        gridViewAdded = (ExpandableHeightGridView) findViewById(R.id.gridViewAdded);
        etAttachFile = (EditText) findViewById(R.id.etAttachFile);
        ivAttachFile = (ImageView) findViewById(R.id.ivAttachFile);
        session = new LoginSession(getApplicationContext());
        imgDone = (ImageView) findViewById(R.id.imgDone);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtAddress1 = (EditText) findViewById(R.id.edtAddress1);
        edtAddress2 = (EditText) findViewById(R.id.edtAddress2);
        edtAddress3 = (EditText) findViewById(R.id.edtAddress3);
        edtAddress4 = (EditText) findViewById(R.id.edtAddress4);
        edtAddress5 = (EditText) findViewById(R.id.edtAddress5);
        edtAddress6 = (EditText) findViewById(R.id.edtAddress6);
        edtWork = (EditText) findViewById(R.id.edtWork);
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
        lstTestimonial = (ListView) findViewById(R.id.lstTestimonial);
        txtTestimonial = (TextView) findViewById(R.id.txtTestimonial);
        txtMore = (TextView) findViewById(R.id.txtMore);
        ivArrowImg = (ImageView) findViewById(R.id.ivArrowImg);
        tvEventInfo = (TextView) findViewById(R.id.tvEventInfo);
        llEventBox = (LinearLayout) findViewById(R.id.llEventBox);
        ivAttachBackImage = (ImageView) findViewById(R.id.ivAttachBackImage);
        ivAttachFrontImage = (ImageView) findViewById(R.id.ivAttachFrontImage);
        txtCardFront = (TextView) findViewById(R.id.txtCardFront);
        txtCardBack = (TextView) findViewById(R.id.txtCardBack);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        profileId = intent.getStringExtra("profile_id");
        allTaggs = new ArrayList<>();

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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), array[position].toString(), Toast.LENGTH_SHORT).show();

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
                selectImage();
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

        new HttpAsyncTaskCompany().execute("http://circle8.asia:8081/Onet.svc/GetCompanyList");
        new HttpAsyncTaskIndustry().execute("http://circle8.asia:8081/Onet.svc/GetIndustryList");
        new HttpAsyncTaskDesignation().execute("http://circle8.asia:8081/Onet.svc/GetDesignationList");

        if (type.equals("edit")) {
            new HttpAsyncTaskUserProfile().execute("http://circle8.asia:8081/Onet.svc/GetUserProfile");
            new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Fetch");
        }

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("add")) {

                    new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8081/Onet.svc/AddProfile");
                } else if (type.equals("edit")) {
                    new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/UpdateProfile");
                }

            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestimonialActivity.class);
                intent.putExtra("ProfileId", profileId);
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
            jsonObject.accumulate("AssociationID", "1");
            jsonObject.accumulate("Attachment_FileName", etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", txtCardFront.getText().toString());
            jsonObject.accumulate("City", edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", "1");
            jsonObject.accumulate("CompanyName", autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", edtAddress5.getText().toString());
            jsonObject.accumulate("Designation", autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", "1");
            jsonObject.accumulate("Email", edtEmail.getText().toString());
            jsonObject.accumulate("Email_Type", "");
            jsonObject.accumulate("Facebook", "");
            jsonObject.accumulate("Google", "");
            jsonObject.accumulate("IndustryID", "1");
            jsonObject.accumulate("IndustryName", autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", "");
            jsonObject.accumulate("Phone", edtWork.getText().toString());
            jsonObject.accumulate("Phone_type", "work");
            jsonObject.accumulate("Postalcode", edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", ProfileDesc);
            jsonObject.accumulate("Profile_Type", "");
            jsonObject.accumulate("State", edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", "");
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
            jsonObject.accumulate("Address1", edtAddress1.getText().toString());
            jsonObject.accumulate("Address2", edtAddress2.getText().toString());
            jsonObject.accumulate("Address3", edtAddress3.getText().toString() + " " + edtAddress4.getText().toString());
            jsonObject.accumulate("Address4", edtAddress5.getText().toString() + " " + edtAddress6.getText().toString());
            jsonObject.accumulate("Address_ID", "1");
            jsonObject.accumulate("Address_Type", "work");
            jsonObject.accumulate("AssociationID", "1");
            jsonObject.accumulate("Attachment_FileName", etAttachFile.getText().toString());
            jsonObject.accumulate("Card_Back", txtCardBack.getText().toString());
            jsonObject.accumulate("Card_Front", txtCardFront.getText().toString());
            jsonObject.accumulate("City", edtAddress3.getText().toString());
            jsonObject.accumulate("CompanyDesc", edtCompanyDesc.getText().toString());
            jsonObject.accumulate("CompanyID", "1");
            jsonObject.accumulate("CompanyName", autoCompleteCompany.getText().toString());
            jsonObject.accumulate("Country", edtAddress5.getText().toString());
            jsonObject.accumulate("Designation", autoCompleteDesignation.getText().toString());
            jsonObject.accumulate("DesignationID", "1");
            jsonObject.accumulate("Email", edtEmail.getText().toString());
            jsonObject.accumulate("Email_Type", "");
            jsonObject.accumulate("Facebook", "");
            jsonObject.accumulate("Google", "");
            jsonObject.accumulate("IndustryID", "1");
            jsonObject.accumulate("IndustryName", autoCompleteIndustry.getText().toString());
            jsonObject.accumulate("Linkedin", "");
            jsonObject.accumulate("Phone", edtWork.getText().toString());
            jsonObject.accumulate("Phone_type", "work");
            jsonObject.accumulate("Postalcode", edtAddress6.getText().toString());
            jsonObject.accumulate("ProfileID", profileId);
            jsonObject.accumulate("Profile_Desc", ProfileDesc);
            jsonObject.accumulate("Profile_Type", "");
            jsonObject.accumulate("State", edtAddress4.getText().toString());
            jsonObject.accumulate("Twitter", "");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (resultCode == PICKFILE_RESULT_CODE) {
                String FilePath = data.getData().getPath();

                File file = new File(FilePath);
                String file_name = file.getName();

                etAttachFile.setText(file_name);
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
                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
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
//            Toast.makeText(getApplicationContext(),"File is greater than 3MB"+mb_size,Toast.LENGTH_LONG).show();
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

    private class HttpAsyncTaskAddProfile extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
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
        protected String doInBackground(String... urls) {
            return POST6(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
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
                    } else if (jsonArray.length() > 3) {
                        lstTestimonial.setVisibility(View.VISIBLE);
                        txtMore.setVisibility(View.VISIBLE);
                        txtTestimonial.setVisibility(View.GONE);
                    } else {
                        lstTestimonial.setVisibility(View.VISIBLE);
                        txtMore.setVisibility(View.GONE);
                        txtTestimonial.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

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
                        title_array.add(object.getString("Testimonial_Text").toString());
                        notice_array.add(String.valueOf(i));
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                        allTaggs.add(nfcModelTag);
                    }
                    customAdapter = new CustomAdapter(EditProfileActivity.this, title_array, notice_array);
                    lstTestimonial.setAdapter(customAdapter);

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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("designation");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    designation = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        designation.add(object.getString("DesignationName"));
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("company");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    company = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        company.add(object.getString("CompanyName"));
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

    private class HttpAsyncTaskUserProfile extends AsyncTask<String, Void, String> {
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
                    OfficePhone = jsonObject.getString("OfficePhone");
                    PrimaryPhone = jsonObject.getString("PrimaryPhone");
                    Emailid = jsonObject.getString("Emailid");
                    Facebook = jsonObject.getString("Facebook");
                    Twitter = jsonObject.getString("Twitter");
                    Google = jsonObject.getString("Google");
                    IndustryName = jsonObject.getString("IndustryName");
                    CompanyName = jsonObject.getString("CompanyName");
                    CompanyProfile = jsonObject.getString("CompanyProfile");
                    Designation = jsonObject.getString("Designation");
                    ProfileDesc = jsonObject.getString("ProfileDesc");
                    Status = jsonObject.getString("Status");
                    tvCompany.setText(CompanyName);
                    tvDesignation.setText(Designation);
                    tvPersonName.setText(FirstName + " " + LastName);
                    autoCompleteCompany.setText(CompanyName);
                    autoCompleteIndustry.setText(IndustryName);
                    autoCompleteDesignation.setText(Designation);
                    edtUserName.setText(FirstName + " " + LastName);
                    edtCompanyDesc.setText(CompanyProfile);
                    edtProfileDesc.setText(ProfileDesc);
                    edtEmail.setText(Emailid);
                    edtPrimary.setText(PrimaryPhone);
                    edtWork.setText(OfficePhone);

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
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("industry");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    industry = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        industry.add(object.getString("IndustryName"));
                    }
                } else {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new
                    ArrayAdapter(EditProfileActivity.this, android.R.layout.simple_list_item_1, industry);

            autoCompleteIndustry.setAdapter(adapter);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
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
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
          /*  try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();



                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        *//*nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));*//*
                        nfcModelTag.setCard_front("000000002.jpg");
                        nfcModelTag.setCard_back("000000006.jpg");


                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);
                        GetData(getContext());
                    }
                }else {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
