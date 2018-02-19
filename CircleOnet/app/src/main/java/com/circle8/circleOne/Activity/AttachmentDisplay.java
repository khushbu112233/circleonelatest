package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityAttachmentDisplayBinding;

public class AttachmentDisplay extends AppCompatActivity
{
    String url;
    Boolean net_check = false;
    ActivityAttachmentDisplayBinding mBinding;
    private ProgressDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_attachment_display);

        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());
        Intent i = getIntent();
        url = i.getStringExtra("url");
        net_check = Utility.isNetworkAvailable(getApplicationContext());

        dialog = new ProgressDialog(AttachmentDisplay.this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        if (net_check == false){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        }
//        startWebView(url);

        mBinding.webView1.getSettings().setJavaScriptEnabled(true);
        mBinding.webView1.setWebViewClient(new myWebClient());

        if (url.contains("pdf") || url.contains("docs"))
        {
            mBinding.webView1.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
//            mBinding.webView1.loadUrl("https://docs.google.com/viewer?url="+url);
        }
        else
        {
            mBinding.webView1.loadUrl(url);
        }
    }


    private void startWebView(String url)
    {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        Utility.freeMemory();
        mBinding.webView1.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(AttachmentDisplay.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        mBinding.webView1.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        mBinding.webView1.loadUrl(url);
    }

    private class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);

            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);

            dialog.dismiss();
        }
    }

}