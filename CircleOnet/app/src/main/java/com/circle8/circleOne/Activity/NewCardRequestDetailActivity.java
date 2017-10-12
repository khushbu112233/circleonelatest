package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
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
import java.io.File;
import java.io.FileNotFoundException;
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
    public static String CardFront = "";
    public static String CardBack = "";
    public static Activity activity;
    private String profileID , Card_Front = "", Card_Back = "";
    ImageView imgUse;
    TextView txtUse, txtUse1;
    AppBarLayout appbar;

    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

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

        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/"+Card_Front ;
        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/"+Card_Back ;

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
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+image).into(imgProfile);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                startActivity(i);
            }
        });

        llNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectCardType();
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

    private void selectCardType()
    {
        type = new CharSequence[]{"Add Front Card", "Add Back Card", "Next"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewCardRequestDetailActivity.this);
        builder.setTitle("Add Card");
        builder.setItems(type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(NewCardRequestDetailActivity.this);
                boolean result1 = Utility.checkCameraPermission(NewCardRequestDetailActivity.this);
                if (type[item].equals("Add Front Card")) {
                    cardType = "front";
                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(NewCardRequestDetailActivity.this);
                   // selectImage();
                } else if (type[item].equals("Add Back Card")) {
                    cardType = "back";

                    CropImage.activity(null)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(NewCardRequestDetailActivity.this);
                   // selectImage();
                } else if (type[item].equals("Next")) {
                    if (CardFront.equals("")){
                        Toast.makeText(getApplicationContext(), "Please Upload Front Card Image.", Toast.LENGTH_LONG).show();
                    }else if (CardBack.equals("")){
                        Toast.makeText(getApplicationContext(), "Please Upload Back Card Image.", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Successfully Uploaded Card..", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), NewCardRequestActivity1.class);
                        i.putExtra("person", name);
                        i.putExtra("designation", designation);
                        i.putExtra("company", company);
                        i.putExtra("profile", profile);
                        i.putExtra("image", image);
                        i.putExtra("phone",profile);
                        i.putExtra("card_front", CardFront);
                        i.putExtra("card_back", CardBack);
                        i.putExtra("profileID",profileID);
                        startActivity(i);
                    }
                }
            }
        });
        builder.show();
    }

    private void selectImage() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_GALLERY_CARD)
                onSelectFromGalleryResultCard(data);
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

                    final_ImgBase64 = BitMapToString(bitmap);
                    //   Upload();
                    CardSwipe.imageView.setImageBitmap(bitmap);
                    myPager.notifyDataSetChanged();
                    if (cardType.equals("front"))
                        new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
                    else if (cardType.equals("back"))
                        new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
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
            new HttpAsyncTaskFrontUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
        else if (cardType.equals("back"))
            new HttpAsyncTaskBackUpload().execute("http://circle8.asia:8999/Onet.svc/ImgUpload");
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
                        CardBack = ImgName;
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
                        Toast.makeText(activity, "Front Card Uploaded Successfully. Add Back Card..", Toast.LENGTH_LONG).show();
                        CardFront = ImgName;
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
