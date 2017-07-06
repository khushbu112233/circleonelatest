package com.amplearch.circleonet.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.List;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.async.WriteCallbackNfcAsync;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardDetail extends NfcActivity {

    ViewPager mViewPager;
    private ArrayList<byte[]> image;
    private CardSwipe myPager ;
    private ImageView imgCards, imgConnect, imgEvents, imgProfile, imgBack, imgCard;
    private static final String TAG = NFCDemo.class.getName();

    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    ProgressDialog mProgressDialog;
    DatabaseHelper db ;
    TextView txtName, txtCompany, txtWebsite, txtEmail, txtPH, txtWork, txtMob, txtAddress, txtRemark;
    CircleImageView imgProfileCard;
    String tag_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCard = (ImageView) findViewById(R.id.imgCard);
        imgProfileCard = (CircleImageView) findViewById(R.id.imgProfileCard);
        db = new DatabaseHelper(getApplicationContext());
        txtName = (TextView) findViewById(R.id.txtName);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtWebsite = (TextView) findViewById(R.id.txtWebsite);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPH = (TextView) findViewById(R.id.txtPH);
        txtWork = (TextView) findViewById(R.id.txtWork);
        txtMob = (TextView) findViewById(R.id.txtMob);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtRemark = (TextView) findViewById(R.id.txtRemark);

        Intent intent = getIntent();
        tag_id = intent.getStringExtra("tag_id");
        List<NFCModel> modelList = db.getNFCbyTag(tag_id);
        image = new ArrayList<>();
        try {

            if (modelList != null){

                for (NFCModel tag1 : modelList) {
                    // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                    Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                    imgCard.setImageBitmap(bmp);

                    Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                    imgProfileCard.setImageBitmap(bmp1);
                    txtName.setText(tag1.getName());
                    txtCompany.setText(tag1.getCompany());
                    txtWebsite.setText(tag1.getWebsite());
                    txtEmail.setText(tag1.getEmail());
                    txtPH.setText(tag1.getPh_no());
                    txtWork.setText(tag1.getWork_no());
                    txtMob.setText(tag1.getMob_no());
                    txtAddress.setText(tag1.getAddress());
                    txtRemark.setText(tag1.getAddress());
                    image.add(tag1.getCard_front());
                    image.add(tag1.getCard_back());
                    myPager = new CardSwipe(getApplicationContext(), image);
                    mViewPager.setClipChildren(false);
                    mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mViewPager.setOffscreenPageLimit(3);
                    mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                    mViewPager.setAdapter(myPager);
                }
            }

        }catch (Exception e){

        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                finish();
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);

                startActivity(go);
                finish();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);

                startActivity(go);
                finish();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent
     *         containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);


            Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
                Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
                //textViewInfo.setText("tag == null");
            }else {
                String tagInfo = tag.toString() + "\n";
                String id = "";
                tagInfo += "\nTag Id: \n";
                byte[] tagId = tag.getId();
                tagInfo += "length = " + tagId.length + "\n";
                for (int i = 0; i < tagId.length; i++) {
                    tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                    // id += Integer.toHexString(tagId[i] & 0xFF) + " ";
                }
                id = bytesToHex(tagId);
               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


              //  Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
               // callData(id);
                for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values()) {
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                    List<NFCModel> modelList = db.getNFCbyTag(data);
                    image = new ArrayList<>();
                    try {

                        if (modelList != null){

                            for (NFCModel tag1 : modelList) {
                                // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                                Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                                imgCard.setImageBitmap(bmp);

                                Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                                imgProfileCard.setImageBitmap(bmp1);
                                txtName.setText(tag1.getName());
                                txtCompany.setText(tag1.getCompany());
                                txtWebsite.setText(tag1.getWebsite());
                                txtEmail.setText(tag1.getEmail());
                                txtPH.setText(tag1.getPh_no());
                                txtWork.setText(tag1.getWork_no());
                                txtMob.setText(tag1.getMob_no());
                                txtAddress.setText(tag1.getAddress());
                                txtRemark.setText(tag1.getRemark());
                                image.add(tag1.getCard_front());
                                image.add(tag1.getCard_back());
                                myPager = new CardSwipe(getApplicationContext(), image);
                                mViewPager.setClipChildren(false);
                                mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                                mViewPager.setOffscreenPageLimit(3);
                                mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                                mViewPager.setAdapter(myPager);
                            }
                        }

                    }catch (Exception e){

                    }
                }
        }
    }

    public void callData(String id){

    }

}
