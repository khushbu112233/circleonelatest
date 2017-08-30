package com.amplearch.circleonet.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Activity.EditProfileActivity;
import com.amplearch.circleonet.Activity.Profile;
import com.amplearch.circleonet.Activity.TestimonialActivity;
import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Adapter.CustomAdapter;
import com.amplearch.circleonet.Adapter.GalleryAdapter;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.ProfileModel;
import com.amplearch.circleonet.Model.TestimonialModel;
import com.amplearch.circleonet.Model.User;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.Utils.ExpandableHeightListView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
{
   // private ProgressBar firstBar = null;
    ImageView imgProfileShare, imgProfileMenu, imgQR, ivEditProfile;
    TextView tvPersonName ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    ProgressDialog progressDialog ;
    ArrayList<String> profile_array;

    private LoginSession session;
    private String UserID = "";
    ImageView imgBack;

    public static ArrayList<ProfileModel> allTags ;
    String profileId = "";

    private int i = 0;
    TextView tvDesignation, tvCompany, tvName, tvCompanyName, tvDesi, tvAssociation, tvAddress, tvWebsite, tvMail,
            tvMob, tvWork;
    ProfileModel nfcModelTag;
    CircleImageView imgProfile;
    LinearLayout lnrMob, lnrWork, lnrWebsite, lnrMap;
    ViewPager mViewPager, viewPager1;
    String recycle_image1, recycle_image2 ;
    private ArrayList<String> image = new ArrayList<>();;
    private CardSwipe myPager ;
    public static ArrayList<TestimonialModel> allTaggs ;
    String TestimonialProfileId = "";
    ExpandableHeightListView lstTestimonial;
    TextView txtTestimonial, txtMore;
    CustomAdapter customAdapter;

    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    String Address1 = "", Address2 = "", Address3 = "", Address4 = "", City = "", State = "", Country = "", Postalcode = "", Website = "", Attachment_FileName = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
      //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        tvDesignation = (TextView)view.findViewById(R.id.tvDesignation);
        tvCompany = (TextView)view.findViewById(R.id.tvCompany);
        tvName = (TextView)view.findViewById(R.id.tvName);
        tvCompanyName = (TextView)view.findViewById(R.id.tvCompanyName);
        tvDesi = (TextView)view.findViewById(R.id.tvDesi);
        tvAssociation = (TextView)view.findViewById(R.id.tvAssociation);
        tvAddress = (TextView)view.findViewById(R.id.tvAddress);
        tvWebsite = (TextView)view.findViewById(R.id.tvWebsite);
        tvMail = (TextView)view.findViewById(R.id.tvMail);
        tvMob = (TextView)view.findViewById(R.id.tvMob);
        tvWork = (TextView)view.findViewById(R.id.tvWork);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        lnrMob = (LinearLayout) view.findViewById(R.id.lnrMob);
        lnrWork = (LinearLayout) view.findViewById(R.id.lnrWork);
        lnrWebsite = (LinearLayout) view.findViewById(R.id.lnrWebsite);
        lnrMap = (LinearLayout) view.findViewById(R.id.lnrMap);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) view.findViewById(R.id.viewPager1);
        lstTestimonial = (ExpandableHeightListView) view.findViewById(R.id.lstTestimonial);
        txtTestimonial = (TextView) view.findViewById(R.id.txtTestimonial);
        txtMore = (TextView) view.findViewById(R.id.txtMore);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Generating Qr Code...");
        progressDialog.setCancelable(false);
        allTags = new ArrayList<>();
        imgQR = (ImageView) view.findViewById(R.id.imgQR);
      //  firstBar = (ProgressBar)view.findViewById(R.id.firstBar);
        tvPersonName = (TextView)view.findViewById(R.id.tvPersonName);
        imgProfileShare = (ImageView) view.findViewById(R.id.imgProfileShare);
        imgProfileMenu = (ImageView) view.findViewById(R.id.imgProfileMenu);
        ivEditProfile = (ImageView)view.findViewById(R.id.ivEditProfile);

        session = new LoginSession(getContext());
        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetUserProfile");

        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        profileId = user.get(LoginSession.KEY_PROFILEID);

        allTaggs = new ArrayList<>();
        lnrMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Google Map")
                        .setMessage("Are you sure you want to redirect to Google Map ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String map = "http://maps.google.co.in/maps?q=" + tvAddress.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), TestimonialActivity.class);
                intent.putExtra("ProfileId", TestimonialProfileId);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                getActivity().finish();
            }
        });

        lnrMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Call to "+ tvPersonName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+tvMob.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
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


        lnrWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Redirect to Web Browser")
                        .setMessage("Are you sure you want to redirect to Web Browser ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String url = tvWebsite.getText().toString();
                                if (url!=null) {
                                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                                        url = "http://" + url;
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_set_as)
                        .show();
            }
        });

        lnrWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Call to "+ tvPersonName.getText().toString())
                        .setMessage("Are you sure you want to make a Call ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+tvWork.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });

        tvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvMail.getText().toString().equals(""))
                {

                }
                else
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Mail to "+ tvPersonName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try
                                    {
                                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + tvMail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    }
                                    catch(Exception e)
                                    {
                                        Toast.makeText(getContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }
        });
        imgProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hello, This is " +tvPersonName.getText().toString()+ " working as " + tvDesignation.getText().toString() + " at "+ tvCompany.getText().toString()+".";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tvPersonName.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });

        new HttpAsyncTaskProfiles().execute("http://circle8.asia:8081/Onet.svc/MyProfiles");


        imgProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), imgProfileMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile_popup_menu, popup.getMenu());
                for (String s : profile_array) {
                    popup.getMenu().add(s);
                }
                popup.getMenu().add("Add New Profile");
                popup.getMenu().add("Close");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();


                        if (item.getTitle().toString().equals("Add New Profile")){
                           // new HttpAsyncTaskAddProfile().execute("http://circle8.asia:8081/Onet.svc/AddProfile");
                            Intent intent = new Intent(getContext(), EditProfileActivity.class);
                            intent.putExtra("profile_id", TestimonialProfileId);
                            intent.putExtra("type", "add");
                            startActivity(intent);


                        }
                        else {
                            for (int i = 0; i < profile_array.size(); i++)
                            {
                                if (item.getTitle().toString().equals(profile_array.get(i).toString()))
                                {
//                                    Toast.makeText(getContext(), profile_array.get(i).toString(), Toast.LENGTH_LONG).show();
                                    tvPersonName.setText(allTags.get(i).getFirstName() + " "+ allTags.get(i).getLastName());
                                    tvDesignation.setText(allTags.get(i).getDesignation());
                                    tvCompany.setText(allTags.get(i).getCompanyName());
                                    tvName.setText(allTags.get(i).getFirstName() + " "+ allTags.get(i).getLastName());
                                    tvCompanyName.setText(allTags.get(i).getCompanyName());
                                    tvDesi.setText(allTags.get(i).getDesignation());
                                    tvMob.setText(allTags.get(i).getPhone());
                                    tvAddress.setText(allTags.get(i).getAddress1()+ " "+allTags.get(i).getAddress2() + " "
                                           + allTags.get(i).getAddress3()  + " "
                                            + allTags.get(i).getAddress4() + " "
                                            + allTags.get(i).getCity() + " "
                                            + allTags.get(i).getState() + " "
                                            + allTags.get(i).getCountry() + " "
                                            + allTags.get(i).getPostalcode());
                                    tvWebsite.setText(allTags.get(i).getWebsite());
                                    TestimonialProfileId = allTags.get(i).getProfileID();
                                    if (allTags.get(i).getUserPhoto().equals(""))
                                    {
                                        imgProfile.setImageResource(R.drawable.usr);
                                    }
                                    else {
                                        Picasso.with(getContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+allTags.get(i).getUserPhoto()).into(imgProfile);
                                    }

                                    new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Fetch");

                                    try
                                    {
                                        if(allTags.get(i).getCard_Front().equals(""))
                                        {
                                            recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                        }
                                        else
                                        {
                                            recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(i).getCard_Front();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                    }

                                    try
                                    {
                                        if(allTags.get(i).getCard_Back().equals(""))
                                        {
                                            recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                        }
                                        else
                                        {
                                            recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(i).getCard_Back();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                                    }

                                    image = new ArrayList<>();
                                    image.add(recycle_image1);
                                    image.add(recycle_image2);
                                    myPager = new CardSwipe(getContext(), image);

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




                                }
                            }
                        }
                        return true;
                    }
                });

                popup.show();//showing popup men
        }
        });

        imgQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Generating QR Code.. Please Wait..", Toast.LENGTH_LONG).show();
                String barName = "Westley Wan";
                try
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                    ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                    tvBarName.setText(barName);
                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(bitmap);
                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }

//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage("Generating Qr Code...");
//                progressDialog.setCancelable(false);



               /* try
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                    ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
                    tvBarName.setText(barName);
                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(bitmap);
                    alertDialog.setView(dialogView);
                    progressDialog.dismiss();
                    alertDialog.show();
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }*/

            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getContext(),"Edit Profile",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("profile_id", TestimonialProfileId);
                startActivity(intent);
            }
        });

        return view;
    }

    public  String POST5(String url)
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
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("userid", UserID);

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

    private class HttpAsyncTaskProfiles extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Profiles...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST5(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Profiles");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    profile_array = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        profile_array.add(object.getString("CompanyName"));

                        nfcModelTag = new ProfileModel();
                        nfcModelTag.setUserID(object.getString("UserID"));
                        nfcModelTag.setFirstName(object.getString("FirstName"));
                        nfcModelTag.setLastName(object.getString("LastName"));
                        nfcModelTag.setUserName(object.getString("UserName"));
                        nfcModelTag.setProfileID(object.getString("ProfileID"));
                        nfcModelTag.setCard_Front(object.getString("Card_Front"));
                        nfcModelTag.setCard_Back(object.getString("Card_Back"));
                        nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCompanyName(object.getString("CompanyName"));
                        nfcModelTag.setPhone(object.getString("Phone"));
                        nfcModelTag.setAddress1(object.getString("Address1"));
                        nfcModelTag.setAddress2(object.getString("Address2"));
                        nfcModelTag.setAddress3(object.getString("Address3"));
                        nfcModelTag.setAddress4(object.getString("Address4"));
                        nfcModelTag.setCity(object.getString("City"));
                        nfcModelTag.setState(object.getString("State"));
                        nfcModelTag.setCountry(object.getString("Country"));
                        nfcModelTag.setPostalcode(object.getString("Postalcode"));
                        nfcModelTag.setWebsite(object.getString("Website"));
                        nfcModelTag.setFacebook(object.getString("Facebook"));
                        nfcModelTag.setTwitter(object.getString("Twitter"));
                        nfcModelTag.setGoogle(object.getString("Google"));
                        nfcModelTag.setLinkedin(object.getString("Linkedin"));
                        nfcModelTag.setAttachment_FileName(object.getString("Attachment_FileName"));
                        allTags.add(nfcModelTag);
                      //  GetData(getContext());
                    }
                    TestimonialProfileId = allTags.get(0).getProfileID();
                    tvPersonName.setText(allTags.get(0).getFirstName() + " "+ allTags.get(0).getLastName());
                    tvDesignation.setText(allTags.get(0).getDesignation());
                    tvCompany.setText(allTags.get(0).getCompanyName());
                    tvName.setText(allTags.get(0).getFirstName() + " "+ allTags.get(0).getLastName());
                    tvCompanyName.setText(allTags.get(0).getCompanyName());
                    tvDesi.setText(allTags.get(0).getDesignation());
                    tvMob.setText(allTags.get(0).getPhone());
                    tvAddress.setText(allTags.get(0).getAddress1()+ " "+allTags.get(0).getAddress2() + " "
                            + allTags.get(0).getAddress3()  + " "
                            + allTags.get(0).getAddress4() + " "
                            + allTags.get(0).getCity() + " "
                            + allTags.get(0).getState() + " "
                            + allTags.get(0).getCountry() + " "
                            + allTags.get(0).getPostalcode());
                    tvWebsite.setText(allTags.get(0).getWebsite());
                    image = new ArrayList<>();
                    if (allTags.get(0).getUserPhoto().equals(""))
                    {
                        imgProfile.setImageResource(R.drawable.usr);
                    }
                    else {
                        Picasso.with(getContext()).load("http://circle8.asia/App_ImgLib/UserProfile/"+allTags.get(0).getUserPhoto()).into(imgProfile);
                    }

                    new HttpAsyncTaskTestimonial().execute("http://circle8.asia:8081/Onet.svc/Testimonial/Fetch");

                    try
                    {
                        if(allTags.get(0).getCard_Front().equals(""))
                        {
                            recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                        }
                        else
                        {
                            recycle_image1 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(0).getCard_Front();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image1 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    }

                    try
                    {
                        if(allTags.get(0).getCard_Back().equals(""))
                        {
                            recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                        }
                        else
                        {
                            recycle_image2 = "http://circle8.asia/App_ImgLib/Cards/"+allTags.get(0).getCard_Back();
                        }
                    }
                    catch (Exception e)
                    {
                        recycle_image2 ="http://circle8.asia/App_ImgLib/Cards/Back_for_all.jpg";
                    }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getContext(), image);

                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(1);
                 //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    mViewPager.setAdapter(myPager);

                    viewPager1.setClipChildren(false);
                    viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    viewPager1.setOffscreenPageLimit(1);
                 //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer
                    viewPager1.setAdapter(myPager);



                }
                else
                {
                    Toast.makeText(getContext(), "Not able to load Profiles..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public  String POST4(String url)
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
            jsonObject.accumulate("AssociationID", "1" );
            jsonObject.accumulate("Card_Back", "000000002.jpg" );
            jsonObject.accumulate("Card_Front", "000000002.jpg" );
            jsonObject.accumulate("CompanyID", "1" );
            jsonObject.accumulate("CompanyName", "Circle One" );
            jsonObject.accumulate("Designation", "Director" );
            jsonObject.accumulate("DesignationID", "1" );
            jsonObject.accumulate("Email", "kajal.patadia@ample-arch.com" );
            jsonObject.accumulate("Email_Type", "gmail" );
            jsonObject.accumulate("IndustryID", "2" );
            jsonObject.accumulate("IndustryName", "IT" );
            jsonObject.accumulate("Phone", "+6588559632" );
            jsonObject.accumulate("Phone_type", "mobile" );
            jsonObject.accumulate("Profile_Desc", "fbvfbvvvf" );
            jsonObject.accumulate("UserID", "25" );

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

    private class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Testimonials...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST2(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Testimonials");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0){
                        lstTestimonial.setVisibility(View.GONE);
                        txtMore.setVisibility(View.GONE);
                        txtTestimonial.setVisibility(View.VISIBLE);
                    }
                    else {
                        lstTestimonial.setVisibility(View.VISIBLE);
                        txtMore.setVisibility(View.VISIBLE);
                        txtTestimonial.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++){

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
                            title_array.add(object.getString("Testimonial_Text").toString());
                            notice_array.add(String.valueOf(i));
//                        Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                            allTaggs.add(nfcModelTag);
                        }
                    }
                    customAdapter = new CustomAdapter(getActivity(), allTaggs);
                    lstTestimonial.setAdapter(customAdapter);
                    lstTestimonial.setExpanded(true);

                }
                else
                {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST2(String url)
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
            jsonObject.accumulate("ProfileId", TestimonialProfileId );
            jsonObject.accumulate("numofrecords", "10" );
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

    private class HttpAsyncTaskAddProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST4(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
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


    public  String POST(String url)
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
            jsonObject.accumulate("profileid", TestimonialProfileId );
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();


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


    Bitmap TextToImageEncode(String Value) throws WriterException
    {
        BitMatrix bitMatrix;
        try
        {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null  );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }*/

}
