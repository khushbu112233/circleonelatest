package com.amplearch.circleonet.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.Utility;
import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    Button btnRescan ;
    ZXingScannerView mScannerView ;
    ViewGroup contentFrame ;
    String scanQr="",scanFormat="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qr);

        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        btnRescan = (Button)findViewById(R.id.btnRescan);


        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        CameraScann();

        btnRescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mScannerView.stopCamera();
                CameraScann();
            }
        });

      /*  mScannerView = new ZXingScannerView(this)
        {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };*/

       /* Boolean aBoolean = Utility.checkCameraPermission(AddQRActivity.this);
        if (aBoolean == true) {*/

//            mScannerView.setResultHandler(this);
//            mScannerView.startCamera();
//      /*  }*/
    }



    @Override
    public void handleResult(Result rawResult)
    {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString()+
//                ", Points = "+rawResult.getResultPoints(), Toast.LENGTH_SHORT).show();

        scanQr = rawResult.getText();
        scanFormat = rawResult.getBarcodeFormat().toString();
        AlertDisplay();

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


    @Override
    public void onResume()
    {
        super.onResume();
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

//    @Override
//    public void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
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
        public static final String TRADE_MARK_TEXT = "O Circle!";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
//            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
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
    }
}
