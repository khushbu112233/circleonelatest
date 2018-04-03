package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.AddManuallyActivity;
import com.circle8.circleOne.Activity.AddQRActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Activity.ManuallyActivity;
import com.circle8.circleOne.Activity.RewardsPointsActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentDashboardLayoutBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.WHITE;
import static com.circle8.circleOne.Activity.AddManuallyActivity.BitMapToString;
import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.callMainPage;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;
import static com.circle8.circleOne.Utils.Utility.encrypt;

/**
 * Created by Ample-Arch on 06-01-2018.
 */

public class DashboardFragment extends Fragment
{
    public static FragmentDashboardLayoutBinding fragmentDashboardLayoutBinding;

    View view;
    Context context;
    int count=0;
    String User_name;
    LoginSession session;
    String profileId = "";
    String UserId= "";
    private String refer;
    AlertDialog QR_AlertDialog ;
    ReferralCodeSession referralCodeSession;
    static Bitmap bitmap ;
    public static String secretKey = "1234567890234561";
    private Fragment fragment;
    static String barName;
    AutoCompleteTextView searchView;
    ImageView imgSearch;
    ImageView ivBarImage;
    Bitmap overlay;
    private boolean netCheck = false;
    private boolean isViewShown = false;

    /*for texture image to text*/
    List<TextBlock> textBlocks = new ArrayList<>();
    List<String> scanTextLineList = new ArrayList<>();
    private static String final_ImgBase64 = "";
    private String ImgName1="";
    private String[] scanTextArray;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        fragmentDashboardLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_layout, container, false);
        view = fragmentDashboardLayoutBinding.getRoot();
        context = getActivity();
        session = new LoginSession(context);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        DashboardActivity.setActionBarTitle("Dashboard", false);
        DashboardActivity.setDrawerVisibility(false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void fetchData()
    {
        initClick();

        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);      // name
        profileId = user.get(LoginSession.KEY_PROFILEID);
        User_name = user.get(LoginSession.KEY_NAME);
        searchView = view.findViewById(R.id.searchView);
        imgSearch = view.findViewById(R.id.imgSearch);
        referralCodeSession = new ReferralCodeSession(context);
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);
        if(Pref.getValue(context,"first_time_run","").equalsIgnoreCase("1")) {
            fragmentDashboardLayoutBinding.ivBlackImg.setVisibility(View.VISIBLE);
            fragmentDashboardLayoutBinding.rlGuiderDisplay.setVisibility(View.VISIBLE);
        }else
        {
            fragmentDashboardLayoutBinding.ivBlackImg.setVisibility(View.INVISIBLE);
            fragmentDashboardLayoutBinding.rlGuiderDisplay.setVisibility(View.INVISIBLE);
        }
        if (DashboardActivity.NotificationCount.equals("0")) {
            fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
        }
        else {
            fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);
            fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setText(DashboardActivity.NotificationCount);
        }

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                String search = searchView.getText().toString();
                if (view != null)
                {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (search.equals(""))
                {

                }
                else
                {
                    SortFragment.CardListApi = "SearchConnect";
                    SortFragment.FindBY = "NAME";
                    SortFragment.Search = search;

                    /*fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();*/

                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();

                    DashboardActivity.activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
                    DashboardActivity.activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                    DashboardActivity.activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                    DashboardActivity.activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
                    DashboardActivity.activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                    DashboardActivity.activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
                    if (DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                        DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                        DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                    }else {
                        DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                        DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                    }

                }
                return true;
            }
        });

        //  Toast.makeText(getContext(), DashboardActivity.NotificationCount, Toast.LENGTH_LONG).show();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String search = searchView.getText().toString();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (search.equals("")){
                }
                else {

                    SortFragment.CardListApi = "SearchConnect";
                    SortFragment.FindBY = "NAME";
                    SortFragment.Search = search;


                    Pref.setValue(getActivity(), "current_frag", "1");
                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();

                    DashboardActivity.activityDashboardBinding.includefooter.imgCard.setImageResource(R.drawable.ic_icon1b);
                    DashboardActivity.activityDashboardBinding.includefooter.imgDashboard.setImageResource(R.drawable.ic_dashboard_gray);
                    DashboardActivity.activityDashboardBinding.includefooter.imgProfile.setImageResource(R.drawable.ic_icon4);
                    DashboardActivity.activityDashboardBinding.includefooter.tvCards.setTextColor(getResources().getColor(R.color.colorPrimary));
                    DashboardActivity.activityDashboardBinding.includefooter.tvDashboard.setTextColor(getResources().getColor(R.color.unselected));
                    DashboardActivity.activityDashboardBinding.includefooter.tvProfile.setTextColor(getResources().getColor(R.color.unselected));
                    if (DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.getText().toString().equals("0")){
                        DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.GONE);
                        DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.GONE);
                    }else {
                        DashboardActivity.activityDashboardBinding.includefooter.txtNotificationCountAction.setVisibility(View.VISIBLE);
                        DashboardFragment.fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        try
        {
            barName = encrypt(profileId, secretKey);
            //bitmap = TextToImageEncode(barName);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        try {
            //setting size of qr code
            int width =600;
            int height = 600;
            int smallestDimension = width < height ? width : height;

            String qrCodeData = barName;
            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            CreateQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                try
                {
                    barName = encrypt(profileId, secretKey);
                    //bitmap = TextToImageEncode(barName);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

                try {
                    //setting size of qr code
                    int width =600;
                    int height = 600;
                    int smallestDimension = width < height ? width : height;

                    String qrCodeData = barName;
                    //setting parameters for qr code
                    String charset = "UTF-8";
                    Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                    CreateQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

                } catch (Exception ex) {
                    Log.e("QrGenerate",ex.getMessage());
                }
            }
        }, 5000);*/


    }

    public  void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
    {
        try
        {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    pixels[offset + x] = matrix.get(x, y) ?
                            ResourcesCompat.getColor(getResources(),R.color.qrColor,null) :WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view

            overlay = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        callMainPage("Dashboard");
        DashboardActivity.setActionBarTitle("Dashboard", false);
        count++;
        if(Pref.getValue(context,"appopen","").equalsIgnoreCase("1"))
        {
            fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            Pref.setValue(context,"appopen","");
        }
        else
        {
            if(count==1) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            }else if(count==2) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard2);
            }else if(count==3) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard3);
            }else if(count==4) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard4);
                count=0;
            }
            else if (count == 5){
                count = 1;
                Pref.setValue(context,"count",count);
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);

            }
        }
    }

    public void initClick()
    {

        fragmentDashboardLayoutBinding.ivBlackImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentDashboardLayoutBinding.ivBlackImg.setVisibility(View.INVISIBLE);
                fragmentDashboardLayoutBinding.rlGuiderDisplay.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSubPAge("OnTapNotification","Dashboard");

                /*fragment = new Notification();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();*/

                Intent intent = new Intent(getContext(), com.circle8.circleOne.ui.activities.authorization.SplashActivity.class);
                startActivity(intent);
            }
        });

        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSubPAge("OnTapMyQRCode","Dashboard");


                if (mergeBitmaps(overlay,bitmap) != null) {
                    QR_AlertDialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme).create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                    FrameLayout fl_QRFrame = (FrameLayout) dialogView.findViewById(R.id.fl_QrFrame);
                    TextView tvBarName = (TextView) dialogView.findViewById(R.id.tvBarName);
                    ivBarImage = (ImageView) dialogView.findViewById(R.id.ivBarImage);
//                tvBarName.setText(barName);
                    //  alertDialog.setFeatureDrawableAlpha(R.color.colorPrimary, 8);

                    ColorDrawable dialogColor = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                    dialogColor.setAlpha(70);
                    QR_AlertDialog.getWindow().setBackgroundDrawable(dialogColor);
                    QR_AlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                    tvBarName.setText(User_name);
//                    bitmap = TextToImageEncode(barName);
                    ivBarImage.setImageBitmap(mergeBitmaps(overlay, bitmap));

                    fl_QRFrame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            QR_AlertDialog.dismiss();
                        }
                    });

                    QR_AlertDialog.setView(dialogView);
                    QR_AlertDialog.show();
                }
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional networks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, User_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));*/
                callSubPAge("OnTapRewards","Dashboard");

                fragment = new RewardsPointsActivity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSubPAge("OnTapScanCard","Dashboard");
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlTapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSubPAge("OnTapCard","Dashboard");
                NfcManager manager = (NfcManager) getContext().getSystemService(Context.NFC_SERVICE);
                NfcAdapter adapter = manager.getDefaultAdapter();
                if (adapter != null && adapter.isEnabled()) {
                    Toast.makeText(getContext(), "You can scan CircleOne NFC-Cards by holding them behind NFC sensor of your Android device. It could be either on top, middle or bottom.", Toast.LENGTH_LONG).show();
                    //Yes NFC available
                }
                else if (adapter != null && !adapter.isEnabled()){


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Please ensure that NFC has been enabled under your phone settings. You will still be able to add contacts with the QR scan feature if your device isn't NFC capable.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else{
                    Toast.makeText(getContext(), "Your device does not support NFC. You will still be able to add contacts with the QR scan feature if your device isn't NFC capable.", Toast.LENGTH_LONG).show();
                    //Your device doesn't support NFC
                }
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ManuallyActivity.class);
                startActivity(intent);
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddQRActivity.class);
                startActivity(intent);
            }
        });

        fragmentDashboardLayoutBinding.includeTapQr.rlScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                scanTextLineList.clear();
                textBlocks.clear();
                selectImageToCrop();

              /*  Intent intent = new Intent(context, AddCardScanActivity.class);
                startActivity(intent);*/
            }
        });
    }

    public static Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap)
    {
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
     *
     */
    private void selectImageToCrop()
    {
        final CharSequence[] items = { "Select Card", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select card to scan");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(context);
                boolean result1=Utility.checkCameraPermission(context);

                if (items[item].equals("Select Card"))
                {
                    if (result && result1)
                    {
                        CropImage.activity(null)
                                .setActivityTitle("Card Scanner")
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropMenuCropButtonTitle("Scan")
                                .start(getActivity());
                    }
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void inspectFromBitmap(Bitmap bitmap)
    {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        try
        {
            if (!textRecognizer.isOperational())
            {
                new android.app.AlertDialog.Builder(context).setMessage("Text recognizer could not be set up on your device").show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);

            String blocks = "";
            String lines = "";
            String words = "";
            for (int index = 0; index < origTextBlocks.size(); index++)
            {
                //extract scanned text blocks here
                TextBlock tBlock = origTextBlocks.valueAt(index);
                blocks = blocks + tBlock.getValue() + "\n" + "\n";
                for (Text line : tBlock.getComponents())
                {
                    //extract scanned text lines here
                    lines = lines + line.getValue() + "\n";

                    /*adding single line in arrayList*/
                    scanTextLineList.add(line.getValue());

                    for (Text element : line.getComponents())
                    {
                        //extract scanned text words here
                        words = words + element.getValue() + ", ";
                    }
                }
            }

            for (int i = 0; i < origTextBlocks.size(); i++)
            {
                TextBlock textBlock = origTextBlocks.valueAt(i);
                textBlocks.add(textBlock);
            }

            Collections.sort(textBlocks, new Comparator<TextBlock>()
            {
                @Override
                public int compare(TextBlock o1, TextBlock o2)
                {
                    int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                    int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                    if (diffOfTops != 0)
                    {
                        return diffOfTops;
                    }
                    return diffOfLefts;
                }
            });

            StringBuilder detectedText = new StringBuilder();
            for (TextBlock textBlock : textBlocks)
            {
                if (textBlock != null && textBlock.getValue() != null)
                {
                    detectedText.append(textBlock.getValue());
                    detectedText.append("\n");
                }
            }

            /*this complete text scan value from scanned*/
            String detectText = String.valueOf(detectedText);
            /*now convert List<> to StringArray[]*/
            int n = scanTextLineList.size();
            scanTextArray = scanTextLineList.toArray(new String[n]);
            new HttpAsyncTaskFrontUpload().execute(Utility.BASE_URL+"ImgUpload");

            /*now pass arrayList of TextLines data to another activity*/
            /*Intent iPut = new Intent(context, AddManuallyActivity.class);
            iPut.putExtra("ScanTextArray", scanTextArray);
            iPut.putExtra("card", ImgName1);
            startActivity(iPut);*/
        }
        finally
        {
            textRecognizer.release();
        }
    }

    public void ResultData(int resultCode, Intent data, DashboardActivity dashboardActivity)
    {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK)
        {
            Bitmap bitmap = null;
            try
            {
                bitmap = BitmapFactory.decodeStream(dashboardActivity.getContentResolver().openInputStream(result.getUri()));
                if (bitmap.equals("") || bitmap == null)
                {
                    bitmap = BitmapFactory.decodeFile(getRealPathFromURI(result.getUri()));
                }
                // originalBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                Utility.freeMemory();

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
                //   bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

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

                inspectFromBitmap(bitmap);

                bitmap.recycle();

                Toast.makeText(getActivity(),"Success:",Toast.LENGTH_SHORT).show();
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();

                Toast.makeText(getActivity(),"Error:",Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
        {
            Utility.freeMemory();
            Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
        }
    }


    private class HttpAsyncTaskFrontUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String loading = "Uploading" ;
            CustomProgressDialog(loading, getActivity());

        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ImgBase64", final_ImgBase64);
                jsonObject.accumulate("classification", "card");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
            //fragmentEditProfileBinding.rlProgressDialog.setVisibility(View.GONE);
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
                        //Toast.makeText(getContext(), ImgName, Toast.LENGTH_LONG).show();
                        ImgName1 = ImgName;
                        Intent iPut = new Intent(context, AddManuallyActivity.class);
                        iPut.putExtra("ScanTextArray", scanTextArray);
                        iPut.putExtra("card", ImgName1);
                        startActivity(iPut);
                    } else {
                        Toast.makeText(getContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}