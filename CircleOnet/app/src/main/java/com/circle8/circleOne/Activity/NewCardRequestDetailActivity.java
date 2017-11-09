package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.EditProfileActivity.BitMapToString;
import static com.circle8.circleOne.Activity.EditProfileActivity.activity;

public class NewCardRequestDetailActivity extends AppCompatActivity
{
    private CircleImageView imgProfile ;
    private TextView tvPerson, tvCompany, tvDesignation, tvProfile ;
    private LinearLayout llDefaultCard, llNewCard ;

    private String name, image, company, designation, phone, profile;
    private static String final_ImgBase64 = "";
    private CardSwipe myPager ;
    private ArrayList<String> swipe_image = new ArrayList<>();
    String recycle_image1, recycle_image2 ;
    ViewPager mViewPager1, mViewPager2;
    ImageView imgBack;
    public static String cardType;
    private CharSequence[] type;
    private CharSequence[] items;
    private String userChoosenTask;
    private int SELECT_GALLERY_CARD = 500;
    private Bitmap originalBitmap;
    public static Bitmap CardFront = null;
    public static Bitmap CardBack = null;
    public static Activity activity;
    private String profileID , Card_Front = "", Card_Back = "";
    ImageView imgUse;
    TextView txtUse, txtUse1;
    AppBarLayout appbar;

    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    RelativeLayout rlLayOne, rlLayTwo ;
    LinearLayout llFrontCard, llBackCard ;
    TextView tvUploadCard, tvCancel, tvNext ;
    ImageView ivAlphaImg ;
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1, REQUEST_DOCUMENT = 2, REQUEST_AUDIO = 3;
    String Attach_String = "", attachDoc = "";
    private String final_attachment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request_detail);

        activity = NewCardRequestDetailActivity.this;

        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        tvPerson = (TextView)findViewById(R.id.tvPersonName);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvDesignation = (TextView)findViewById(R.id.tvDesignation);
        tvProfile = (TextView)findViewById(R.id.tvProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        llDefaultCard = (LinearLayout)findViewById(R.id.llDefaultCard);
        llNewCard = (LinearLayout)findViewById(R.id.llNewCard);
        imgUse = (ImageView) findViewById(R.id.imgUse);
        txtUse = (TextView) findViewById(R.id.txtUse);
        txtUse1 = (TextView) findViewById(R.id.txtUse1);
        mViewPager1 = (ViewPager)findViewById(R.id.viewPager);
        mViewPager2 = (ViewPager)findViewById(R.id.viewPager1);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        rlLayOne = (RelativeLayout)findViewById(R.id.rlLayOne);
        rlLayTwo = (RelativeLayout)findViewById(R.id.rlLayTwo);
        llFrontCard = (LinearLayout)findViewById(R.id.llFrontCard);
        llBackCard = (LinearLayout)findViewById(R.id.llBackCard);
        tvUploadCard = (TextView)findViewById(R.id.tvUploadCard);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        tvNext = (TextView)findViewById(R.id.tvNext);
        ivAlphaImg = (ImageView)findViewById(R.id.ivAlphaImg);

        Intent i = getIntent();
        name = i.getStringExtra("person") ;
        designation = i.getStringExtra("designation") ;
        company = i.getStringExtra("company") ;
        phone = i.getStringExtra("phone") ;
        profile = i.getStringExtra("profile");
        image = i.getStringExtra("image") ;
        profileID = i.getStringExtra("profileID");
        Card_Front = i.getStringExtra("Card_Front");
        Card_Back = i.getStringExtra("Card_Back");

        recycle_image1 =Utility.BASE_IMAGE_URL+"Cards/"+Card_Front ;
        recycle_image2 =Utility.BASE_IMAGE_URL+"Cards/"+Card_Back ;

        swipe_image.add(recycle_image1);
        swipe_image.add(recycle_image2);
        myPager = new CardSwipe(getApplicationContext(), swipe_image);

        mViewPager1.setClipChildren(false);
        mViewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager1.setOffscreenPageLimit(1);
        mViewPager1.setAdapter(myPager);

        mViewPager2.setClipChildren(false);
        mViewPager2.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        mViewPager2.setOffscreenPageLimit(1);
        mViewPager2.setAdapter(myPager);

        // Toast.makeText(getApplicationContext(), Card_Back, Toast.LENGTH_LONG).show();

        tvPerson.setText(i.getStringExtra("person"));
        tvDesignation.setText(i.getStringExtra("designation"));
        tvCompany.setText(i.getStringExtra("company"));
        tvProfile.setText(i.getStringExtra("profile"));

        if (Card_Back.equals("") || Card_Front.equals(""))
        {
            llDefaultCard.setAlpha(0.4f);
            llDefaultCard.setEnabled(false);
            //imgUse.setColorFilter(getApplicationContext().getResources().getColor(R.color.unselected));
            imgUse.setImageResource(R.drawable.unselected_card);
            txtUse.setTextColor(getResources().getColor(R.color.unselected));
            txtUse1.setTextColor(getResources().getColor(R.color.unselected));
            appbar.setVisibility(View.GONE);
        }
        else
        {
            llDefaultCard.setAlpha(1.0f);
            llDefaultCard.setEnabled(true);
            appbar.setVisibility(View.VISIBLE);
        }

        //  imgProfile.setImageResource(Integer.parseInt(i.getStringExtra("image")));
        if (image.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_white1);
        }
        else {
            Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"UserProfile/"+image).into(imgProfile);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardFront = null;
                CardBack = null;
                final_attachment = "";
                finish();
            }
        });

        llDefaultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", name);
                i.putExtra("designation", designation);
                i.putExtra("company", company);
                i.putExtra("profile", profile);
                i.putExtra("image", image);
                i.putExtra("phone",phone);
                i.putExtra("profileID",profileID);
                i.putExtra("card_front", Card_Front);
                i.putExtra("card_back", Card_Back);
                i.putExtra("type", "string");
                startActivity(i);

                rlLayOne.setEnabled(true);
                ivAlphaImg.setVisibility(View.GONE);
                rlLayTwo.setVisibility(View.GONE);

            }
        });

        llNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                selectCardType();

                rlLayOne.setEnabled(false);
                ivAlphaImg.setVisibility(View.VISIBLE);
                rlLayTwo.setVisibility(View.VISIBLE);

                /*Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                i.putExtra("person", name);
                i.putExtra("designation", designation);
                i.putExtra("company", company);
                i.putExtra("profile", profile);
                i.putExtra("image", image);
                i.putExtra("phone",profile);
                startActivity(i);*/
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (CardFront == null)
                {
                    if ( final_attachment.equalsIgnoreCase(""))
                    {
                        Toast.makeText(getApplicationContext(), "Please Upload Front Card or Attachment File.", Toast.LENGTH_LONG).show();
                    }
                    else if (!final_attachment.equalsIgnoreCase(""))
                    {
                        Toast.makeText(getApplicationContext(), "Card Uploaded Sucessfully..", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone",phone);
                        i.putExtra("profileID",profileID);
                        i.putExtra("card_front", final_attachment);
                        i.putExtra("card_back", "");
                        i.putExtra("type", "string");
                        startActivity(i);

                        rlLayOne.setEnabled(true);
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);
                    }
                }
                /*else if (CardFront == null || CardBack != null)
                {
                    Toast.makeText(getApplicationContext(), "Please Upload Front Card.", Toast.LENGTH_LONG).show();
                }*/
                else
                {
                    Toast.makeText(getApplicationContext(), "Card Uploaded Sucessfully..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                    i.putExtra("person", name);
                    i.putExtra("designation", designation);
                    i.putExtra("company", company);
                    i.putExtra("profile", profile);
                    i.putExtra("image", image);
                    i.putExtra("phone",phone);
                    i.putExtra("profileID",profileID);
                    i.putExtra("card_front", CardFront);
                    i.putExtra("card_back", CardBack);
                    i.putExtra("type", "bitmap");
                    startActivity(i);

                    rlLayOne.setEnabled(true);
                    ivAlphaImg.setVisibility(View.GONE);
                    rlLayTwo.setVisibility(View.GONE);
                }
                /*if (Card_Front == null && final_attachment.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Upload Front Card or attach File", Toast.LENGTH_LONG).show();
                }*/
//                else if (Card_Front == null || final_attachment.equals(""))
                /*{
                    if (Card_Front == null && !final_attachment.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Card Uploaded Sucessfully..", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone",phone);
                        i.putExtra("profileID",profileID);
                        i.putExtra("card_front", final_attachment);
                        i.putExtra("card_back", "");
                        i.putExtra("type", "string");
                        startActivity(i);

                        rlLayOne.setEnabled(true);
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);
                    }
                    else if (Card_Front != null && final_attachment.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Card Uploaded Sucessfully..", Toast.LENGTH_LONG).show();
                        //dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone", phone);
                        i.putExtra("card_front", CardFront);
                        i.putExtra("card_back", CardBack);
                        i.putExtra("type", "bitmap");
                        i.putExtra("profileID", profileID);
                        startActivity(i);

                        rlLayOne.setEnabled(true);
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);

                    }
                }*/
//                else
                /*{
                   *//* if (CardFront == null && CardBack == null) {
                        Toast.makeText(getApplicationContext(), "Please Upload Front Card Image.", Toast.LENGTH_LONG).show();
                    } else if (CardFront == null) {
                        Toast.makeText(getApplicationContext(), "Please Upload First Card Image.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Card Uploaded Sucessfully..", Toast.LENGTH_LONG).show();
                        //dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone", phone);
                        i.putExtra("card_front", CardFront);
                        i.putExtra("card_back", CardBack);
                        i.putExtra("type", "bitmap");
                        i.putExtra("profileID", profileID);
                        startActivity(i);

                        rlLayOne.setEnabled(true);
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);

                    }*//*
                }*/
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rlLayOne.setEnabled(true);
                ivAlphaImg.setVisibility(View.GONE);
                rlLayTwo.setVisibility(View.GONE);
            }
        });

        tvUploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectFile();
            }
        });

        llFrontCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cardType = "front";
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(NewCardRequestDetailActivity.this);
            }
        });

        llBackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (CardFront == null)
                {
                    Toast.makeText(getApplicationContext(), "Please Upload Front Card.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    cardType = "back";
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(NewCardRequestDetailActivity.this);
                }
            }
        });

        mViewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mViewPager1.scrollTo(mViewPager2.getScrollX(), mViewPager2.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager1.setCurrentItem(mViewPager2.getCurrentItem(), false);
                }
            }
        });
    }

    private void selectFile() {
        items = new CharSequence[]{"Upload Document", "Take Photo","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewCardRequestDetailActivity.this);
        builder.setTitle("Attach File");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(NewCardRequestDetailActivity.this);

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

    private void selectCardType()
    {
        type = new CharSequence[]{"Upload Design (Front)", "Upload Design (Back)", "Next"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewCardRequestDetailActivity.this);
        builder.setTitle("Add Card");
        builder.setCancelable(false);
        builder.setItems(type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                boolean result = Utility.checkStoragePermission(NewCardRequestDetailActivity.this);
                boolean result1 = Utility.checkCameraPermission(NewCardRequestDetailActivity.this);
                if (type[item].equals("Upload Design (Front)"))
                {
                    cardType = "front";
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                            .start(NewCardRequestDetailActivity.this);
                    // selectImage();
                }
                else if (type[item].equals("Upload Design (Back)"))
                {
                    cardType = "back";

                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                            .start(NewCardRequestDetailActivity.this);
                    // selectImage();
                }
                else if (type[item].equals("Next"))
                {
                    if (CardFront==null)
                    {
                        Toast.makeText(getApplicationContext(), "Please Upload Front Card Image.", Toast.LENGTH_LONG).show();
                    }
                    else if (CardBack == null)
                    {
                        Toast.makeText(getApplicationContext(), "Please Upload Back Card Image.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Successfully Uploaded Card..", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone",phone);
                        i.putExtra("card_front", CardFront);
                        i.putExtra("card_back", CardBack);
                        i.putExtra("type", "bitmap");
                        i.putExtra("profileID",profileID);
                        startActivity(i);

                        rlLayOne.setEnabled(true);
                        ivAlphaImg.setVisibility(View.GONE);
                        rlLayTwo.setVisibility(View.GONE);

                    }
                }
            }
        });
        builder.show();
    }

    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewCardRequestDetailActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(NewCardRequestDetailActivity.this);
                boolean result1 = Utility.checkCameraPermission(NewCardRequestDetailActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result1) {

                        /*Intent intent1 = new Intent(getApplicationContext(), ScanbotCamera.class);
                        intent1.putExtra("from", "newcard");
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

    private void galleryCardIntent()
    {
        //  rltGallery.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_GALLERY_CARD);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewCardRequestDetailActivity.this);
            alertDialogBuilder.setTitle("Sorry :");
            alertDialogBuilder.setMessage("Please select a file not more than 3MB.");
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    attachDoc = "";
                                    //etAttachFile.setText("Attachment Name");
                                    dialog.dismiss();
                                    selectFile();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            // ivAttachFile.setEnabled(false);
            attachDoc = fileName;
            //etAttachFile.setText(fileName);
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
            jsonObject.accumulate("FileName", attachDoc);
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


    private class HttpAsyncTaskDocUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(EditProfileActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

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
            rlProgressDialog.setVisibility(View.GONE);
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
                        tvNext.setEnabled(true);
                        tvNext.setAlpha(1.0f);
                        final_attachment = ImgName;
                        //etAttachFile.setText(ImgName);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_GALLERY_CARD)
                onSelectFromGalleryResultCard(data);
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
            else if (requestCode == REQUEST_DOCUMENT) {
                onSelectFromFiles(data);
            }
           /* else if (requestCode == REQUEST_CAMERA_CARD)
                onCaptureImageResultCard(data);*/
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Bitmap bitmap;

                try
                {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result.getUri()));
                    // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

                    //  final_ImgBase64 = BitMapToString(bitmap);
                    //   Upload();
                    CardSwipe.imageView.setImageBitmap(bitmap);
                    myPager.notifyDataSetChanged();
                    if (cardType.equals("front"))
                    {
                        CardFront = bitmap;
                        tvNext.setEnabled(true);
                        tvNext.setAlpha(1.0f);
                    }
                    //new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                    else if (cardType.equals("back")) {
                        //   new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                        CardBack = bitmap;
                    }
                }
                catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
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

                /*Intent intent = new Intent(getApplicationContext(), com.circle8.circleOne.ScanBotGallery.MainActivity.class);
                intent.putExtra("bitmap", originalBitmap);
                intent.putExtra("from", "newcard");
                startActivity(intent);*/
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//        BmToString(bm);
    }

    @Override
    public void onBackPressed() {
        CardFront = null;
        CardBack = null;
        final_attachment = "";
        finish();
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
        CardSwipe.imageView.setImageBitmap(bitmap);
        if (cardType.equals("front"))
            new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");
        else if (cardType.equals("back"))
            new HttpAsyncTaskBackUpload().execute(Utility.BASE_URL+"ImgUpload");
    }

    private static class HttpAsyncTaskBackUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

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
            rlProgressDialog.setVisibility(View.GONE);
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
                        Toast.makeText(activity, "Back Card Uploaded Successfully.", Toast.LENGTH_LONG).show();
                        // CardBack = ImgName;
                    } else {
                        Toast.makeText(activity, "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    public static String POST7(String url)
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

    private static class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(activity);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

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
            rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName != null)
                    {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        Toast.makeText(activity, "Front Card Uploaded Successfully. Add Back Card..", Toast.LENGTH_LONG).show();
                        // CardFront = ImgName;


                    }
                    else
                    {
                        Toast.makeText(activity, "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public static void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(activity,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(activity,R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);

        int SPLASHTIME = 1000*60 ;  //since 1000=1sec so 1000*60 = 60000 or 60sec or 1 min.
        for (int i = 350; i <= SPLASHTIME; i = i + 350)
        {
            final int j = i;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run()
                {
                    if (j / 350 == 1 || j / 350 == 4 || j / 350 == 7 || j / 350 == 10)
                    {
                        tvProgressing.setText(loading+".");
                    }
                    else if (j / 350 == 2 || j / 350 == 5 || j / 350 == 8)
                    {
                        tvProgressing.setText(loading+"..");
                    }
                    else if (j / 350 == 3 || j / 350 == 6 || j / 350 == 9)
                    {
                        tvProgressing.setText(loading+"...");
                    }

                }
            }, i);
        }
    }
}
