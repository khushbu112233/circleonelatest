package com.circle8.circleOne.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityAddQrBinding;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;

public class AddQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    ZXingScannerView mScannerView ;
    String scanQr="",scanFormat="" ;
    String profileId = "";
    private LoginSession session;
    public String secretKey = "1234567890234561";
    private double latitude, longitude;
    String lat = "", lng = "";
    private boolean netCheck;
    ActivityAddQrBinding mBinding;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_qr);


        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Utility.deleteCache(getApplicationContext());

        profileId = user.get(LoginSession.KEY_PROFILEID);
       /* mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        CameraScann();*/
        netCheck = Utility.isNetworkAvailable(getApplicationContext());
        mBinding.btnRescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mScannerView.stopCamera();
                CameraScann();
            }
        });

        mScannerView = new ZXingScannerView(this)
        {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        mBinding.contentFrame.addView(mScannerView);
        CameraScann();

       /* Boolean aBoolean = Utility.checkCameraPermission(AddQRActivity.this);
        if (aBoolean == true) {*/

//            mScannerView.setResultHandler(this);
//            mScannerView.startCamera();
//      /*  }*/

        mBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.deleteCache(getApplicationContext());
                Utility.freeMemory();
                Utility.deleteCache(getApplicationContext());
                finish();
            }
        });

    }


    public String decrypt(String value, String key)
            throws GeneralSecurityException, IOException {

        try {
            Utility.deleteCache(getApplicationContext());
            Utility.freeMemory();
            byte[] value_bytes = Base64.decode(value, 0);
            byte[] key_bytes = getKeyBytes(key);
            return new String(decrypt(value_bytes, key_bytes, key_bytes), "UTF-8");
        }catch (Exception e){
            return "Invalid QRCode";
        }
    }

    public byte[] decrypt(byte[] ArrayOfByte1, byte[] ArrayOfByte2, byte[] ArrayOfByte3)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // setup AES cipher in CBC mode with PKCS #5 padding
        Utility.freeMemory();
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // decrypt
        localCipher.init(2, new SecretKeySpec(ArrayOfByte2, "AES"), new IvParameterSpec(ArrayOfByte3));
        return localCipher.doFinal(ArrayOfByte1);
    }

    private byte[] getKeyBytes(String paramString)
            throws UnsupportedEncodingException {
        Utility.freeMemory();
        byte[] arrayOfByte1 = new byte[16];
        byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
        return arrayOfByte1;
    }

    @Override
    public void handleResult(Result rawResult)
    {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString()+
//                ", Points = "+rawResult.getResultPoints(), Toast.LENGTH_SHORT).show();

        //    nfcProfileId = decrypt(scanQr, secretKey);
        // scanQr = rawResult.getText();
        scanFormat = rawResult.getBarcodeFormat().toString();

        try {
            Utility.freeMemory();
            // Toast.makeText(getApplicationContext(), rawResult.getText().toString(), Toast.LENGTH_LONG).show();
            scanQr = decrypt(rawResult.getText().toString(), secretKey);
            if (scanQr.equals("Invalid QRCode")) {
                Toast.makeText(getApplicationContext(), scanQr, Toast.LENGTH_LONG).show();
                mScannerView.stopCamera();
                CameraScann();

            } else
            {
                // Toast.makeText(getApplicationContext(), scanQr, Toast.LENGTH_LONG).show();
                try {
                    mScannerView.stopCamera();
                    CameraScann();
                    Utility.freeMemory();
                    if (netCheck == false){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Utility.freeMemory();
                        if (CardsActivity.mLastLocation != null) {
                            latitude = CardsActivity.mLastLocation.getLatitude();
                            longitude = CardsActivity.mLastLocation.getLongitude();
                            lat = String.valueOf(latitude);
                            lng = String.valueOf(longitude);
                            new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                        } else {
                            lat = "";
                            lng = "";
                            new HttpAsyncTask().execute(Utility.BASE_URL + "FriendConnection_Operation");
                            CardsActivity.getLocation();
                            //Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //AlertDisplay();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(QrActivity.this);
//            }
//        }, 2000);
    }


    public void AlertDisplay()
    {
//        String verify = sharedPreferences.getString("verify","");
        AlertDialog.Builder alert = new AlertDialog.Builder(AddQRActivity.this);
        alert.setTitle("Add QR");
        alert.setMessage("Scan Item: " +scanQr);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface adialog, int which)
            {
                adialog.cancel();
                mScannerView.stopCamera();
                CameraScann();
                new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");
            }
        });
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface adialog, int which)
//            {
//
//            }
//        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddQRActivity.this);
           // dialog.setMessage("Adding records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.accumulate("Latitude", lat);
                jsonObject.accumulate("Location", "");
                jsonObject.accumulate("Longitude", lng);
                jsonObject.accumulate("Operation", "Request");
                jsonObject.accumulate("RequestType", "NFC");
                jsonObject.accumulate("connection_date", formattedDate);
                jsonObject.accumulate("friendProfileId", scanQr);
                jsonObject.accumulate("myProfileId", profileId);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Utility.freeMemory();
            //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result == "") {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1")) {
                        Utility.freeMemory();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successful_request_sent), Toast.LENGTH_LONG).show();
                        Pref.setValue(AddQRActivity.this, "current_frag", "1");
                      /*  fragment = new CardsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
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

                        }*/
                        finish();
                        Pref.setValue(AddQRActivity.this, "AddQr", "1");
                        /*CardsFragment.mViewPager.getCurrentItem();
                        List1Fragment.webCall();
                        List2Fragment.webCall();
                        List3Fragment.webCall();
                        List4Fragment.webCall();*/
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                 /*   try
                    {
//                    List2Fragment.allTaggs.clear();
                        List2Fragment.nfcModel.clear();
                        List2Fragment.gridAdapter.notifyDataSetChanged();
                        List2Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List3Fragment.allTaggs.clear();
                        List3Fragment.nfcModel1.clear();
                        List3Fragment.gridAdapter.notifyDataSetChanged();
                        List3Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List4Fragment.allTaggs.clear();
                        List4Fragment.nfcModel1.clear();
                        List4Fragment.gridAdapter.notifyDataSetChanged();
                        List4Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {

//                    List1Fragment.allTags.clear();
                        List1Fragment.nfcModel.clear();
                        List1Fragment.mAdapter.notifyDataSetChanged();
                        List1Fragment.mAdapter1.notifyDataSetChanged();
                        List1Fragment.GetData(context);
                    }
                    catch(Exception e) {    }*/

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResume()
    {
        Utility.freeMemory();
        super.onResume();
        callSubPAge("OnTapScanQRCode","Dashboard");

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
//        CameraScreen();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }


    public void CameraScann()
    {
//        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }
        else
        {
            mScannerView.startCamera();
        }        // Start camera<br />
    }

    private static class CustomViewFinderView extends ViewFinderView
    {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();
        public Paint mLaserPaint;
        public Paint mFinderMaskPaint;
        public Paint mBorderPaint;

        private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
        private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
        private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
        private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
        private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);

        private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
        private int scannerAlpha;
        private static final int POINT_SIZE = 10;
        private static final long ANIMATION_DELAY = 80l;

        private Rect mFramingRect;

        private static final float SQUARE_DIMENSION_RATIO = 7f/8;
        private static final float PORTRAIT_WIDTH_RATIO = 6f/8;
        private static final float PORTRAIT_WIDTH_HEIGHT_RATIO = 0.75f;

        private static final float LANDSCAPE_HEIGHT_RATIO = 5f/8;
        private static final float LANDSCAPE_WIDTH_HEIGHT_RATIO = 1.4f;
        private static final int MIN_DIMENSION_DIFF = 50;

        protected boolean mSquareViewFinder = true ;

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init()
        {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
//            setSquareViewFinder(true);

            //set up laser paint
            mLaserPaint = new Paint();
            mLaserPaint.setColor(mDefaultLaserColor);
            mLaserPaint.setStyle(Paint.Style.FILL);

            //finder mask paint
            mFinderMaskPaint = new Paint();
            mFinderMaskPaint.setColor(mDefaultMaskColor);

            //border paint
            mBorderPaint = new Paint();
            mBorderPaint.setColor(mDefaultBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);
            mBorderPaint.setAntiAlias(true);
        }

        @Override
        public void setLaserColor(int laserColor) {
            mLaserPaint.setColor(laserColor);
        }

        @Override
        public void setMaskColor(int maskColor) {
            mFinderMaskPaint.setColor(maskColor);
        }

        @Override
        public void setBorderColor(int borderColor) {
            mBorderPaint.setColor(borderColor);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            drawTradeMark(canvas);
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }

        private void drawTradeMark(Canvas canvas)
        {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }

        public void drawViewFinderMask(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Rect framingRect = getFramingRect();

            canvas.drawRect(0, 0, width, framingRect.top, mFinderMaskPaint);
            canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom + 1, mFinderMaskPaint);
            canvas.drawRect(framingRect.right + 1, framingRect.top, width, framingRect.bottom + 1, mFinderMaskPaint);
            canvas.drawRect(0, framingRect.bottom + 1, width, height, mFinderMaskPaint);
        }

        public void drawViewFinderBorder(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Top-left corner
            Path path = new Path();
            path.moveTo(framingRect.left, framingRect.top + mBorderLineLength);
            path.lineTo(framingRect.left, framingRect.top);
            path.lineTo(framingRect.left + mBorderLineLength, framingRect.top);
            canvas.drawPath(path, mBorderPaint);

            // Top-right corner
            path.moveTo(framingRect.right, framingRect.top + mBorderLineLength);
            path.lineTo(framingRect.right, framingRect.top);
            path.lineTo(framingRect.right - mBorderLineLength, framingRect.top);
            canvas.drawPath(path, mBorderPaint);

            // Bottom-right corner
            path.moveTo(framingRect.right, framingRect.bottom - mBorderLineLength);
            path.lineTo(framingRect.right, framingRect.bottom);
            path.lineTo(framingRect.right - mBorderLineLength, framingRect.bottom);
            canvas.drawPath(path, mBorderPaint);

            // Bottom-left corner
            path.moveTo(framingRect.left, framingRect.bottom - mBorderLineLength);
            path.lineTo(framingRect.left, framingRect.bottom);
            path.lineTo(framingRect.left + mBorderLineLength, framingRect.bottom);
            canvas.drawPath(path, mBorderPaint);
        }

        public void drawLaser(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Draw a red "laser scanner" line through the middle to show decoding is active
            mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = framingRect.height() / 2 + framingRect.top;
            canvas.drawRect(framingRect.left + 2, middle - 1, framingRect.right - 1, middle + 2, mLaserPaint);

            postInvalidateDelayed(ANIMATION_DELAY,
                    framingRect.left - POINT_SIZE,
                    framingRect.top - POINT_SIZE,
                    framingRect.right + POINT_SIZE,
                    framingRect.bottom + POINT_SIZE);
        }

        // TODO: Need a better way to configure this. Revisit when working on 2.0
        public void setSquareViewFinder(boolean set) {
            mSquareViewFinder = set;
        }

        public void setupViewFinder() {
            updateFramingRect();
            invalidate();
        }

        public Rect getFramingRect() {
            return mFramingRect;
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            updateFramingRect();
        }

        public synchronized void updateFramingRect()
        {
            Point viewResolution = new Point(getWidth(), getHeight());
            int width;
            int height;
            int orientation = DisplayUtils.getScreenOrientation(getContext());

            if(mSquareViewFinder) {
                if(orientation != Configuration.ORIENTATION_PORTRAIT) {
                    height = (int) (getHeight() * SQUARE_DIMENSION_RATIO);
                    width = height;
                } else {
                    width = (int) (getWidth() * SQUARE_DIMENSION_RATIO);
                    height = width;
                }
            } else {
                if(orientation != Configuration.ORIENTATION_PORTRAIT) {
                    height = (int) (getHeight() * LANDSCAPE_HEIGHT_RATIO);
                    width = (int) (LANDSCAPE_WIDTH_HEIGHT_RATIO * height);
                } else {
                    width = (int) (getWidth() * PORTRAIT_WIDTH_RATIO);
                    height = (int) (PORTRAIT_WIDTH_HEIGHT_RATIO * width);
                }
            }

            if(width > getWidth()) {
                width = getWidth() - MIN_DIMENSION_DIFF;
            }

            if(height > getHeight()) {
                height = getHeight() - MIN_DIMENSION_DIFF;
            }

            int leftOffset = (viewResolution.x - width) / 2;
            int topOffset = (viewResolution.y - height) / 2;
            mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        }
    }

}
