package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.AddQRActivity;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Activity.Notification;
import com.circle8.circleOne.Activity.RewardsPointsActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.FragmentDashboardLayoutBinding;
import com.google.android.gms.maps.model.Dash;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.graphics.Color.WHITE;
import static com.circle8.circleOne.Fragments.ProfileFragment.TextToImageEncode;
import static com.circle8.circleOne.Fragments.ProfileFragment.encrypt;

/**
 * Created by Ample-Arch on 06-01-2018.
 */

public class DashboardFragment extends Fragment {
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


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        fragmentDashboardLayoutBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dashboard_layout, container, false);
        view = fragmentDashboardLayoutBinding.getRoot();
        context =getActivity();
        session = new LoginSession(context);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        DashboardActivity.setActionBarTitle("Dashboard", false);
        DashboardActivity.setDrawerVisibility(false);

        fragmentDashboardLayoutBinding.includeNotiRewardShare.txtNotificationCountAction1.setText(DashboardActivity.NotificationCount);

        return view;
    }

    public void fetchData(){
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

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
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

                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();

                }
                return true;
            }
        });

        //  Toast.makeText(getContext(), DashboardActivity.NotificationCount, Toast.LENGTH_LONG).show();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    fragment = new CardsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                            .addToBackStack(null)
                            .commit();

                }
            }
        });



        final Handler handler = new Handler();
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
        }, 5000);


    }

    public  void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){


        try {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }
/* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
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

    public void initClick() {

        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new Notification();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new RewardsPointsActivity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional newtorks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, User_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlTapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(getContext(), "Your device does not support NFC", Toast.LENGTH_LONG).show();
                    //Your device doesn't support NFC
                }
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QR_AlertDialog = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Light).create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                FrameLayout fl_QRFrame = (FrameLayout)dialogView.findViewById(R.id.fl_QrFrame);
                TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
//                tvBarName.setText(barName);
                //  alertDialog.setFeatureDrawableAlpha(R.color.colorPrimary, 8);

                ColorDrawable dialogColor = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                dialogColor.setAlpha(70);
                QR_AlertDialog.getWindow().setBackgroundDrawable(dialogColor);
                QR_AlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                tvBarName.setText(User_name);
//                    bitmap = TextToImageEncode(barName);
                ivBarImage.setImageBitmap(mergeBitmaps(overlay,bitmap));

                fl_QRFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        QR_AlertDialog.dismiss();
                    }
                });

                QR_AlertDialog.setView(dialogView);
                QR_AlertDialog.show();
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddQRActivity.class);
                startActivity(intent);
            }
        });
    }

}