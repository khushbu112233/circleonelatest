package com.circle8.circleOne.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.RxContacts.Contact;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView ivMessage, ivPhone ;
    private TextView tvAddress1, tvAddress2, tvWebsite, tvEmail, tvPhone, tvFax ;
    private TextView tvCompany, tvPartner, tvSend, tvCancel;
    private EditText etSubject, etDescription ;
    private ImageView imgBack;
    private LinearLayout lnrAddress, lnrEmail, lnrContact, lnrWebsite ;
    private String subject, description ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvCompany = (TextView)findViewById(R.id.tvCompanyName);
        tvPartner = (TextView)findViewById(R.id.tvPartner);
        tvAddress1 = (TextView)findViewById(R.id.tvAddress1);
        tvWebsite = (TextView)findViewById(R.id.tvWebsite);
        tvEmail = (TextView)findViewById(R.id.tvMail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvFax = (TextView)findViewById(R.id.tvWork);
        tvSend = (TextView)findViewById(R.id.tvSend);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        etSubject = (EditText)findViewById(R.id.etSubject);
        etDescription = (EditText)findViewById(R.id.etDescription);

        lnrAddress = (LinearLayout)findViewById(R.id.lnrAddress);
        lnrEmail = (LinearLayout)findViewById(R.id.llMailBox);
        lnrContact = (LinearLayout)findViewById(R.id.lnrPhone);
        lnrWebsite = (LinearLayout)findViewById(R.id.lnrWebsite);
        ivMessage = (ImageView) findViewById(R.id.ivMessage);
        ivPhone = (ImageView) findViewById(R.id.ivPhone);

        imgBack.setOnClickListener(this);
        lnrAddress.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        lnrWebsite.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if ( v == imgBack)
        {
            finish();
        }
        if ( v == lnrAddress)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Google Map")
                    .setMessage("Are you sure you want to redirect to Google Map ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            String map = "http://maps.google.co.in/maps?q=" + tvAddress1.getText().toString();
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
        if ( v == ivMessage)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);
            builder.setTitle("Mail to "+ tvCompany.getText().toString())
                    .setMessage("Are you sure you want to drop Mail ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            try
                            {
                                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + tvEmail.getText().toString()));
                                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                intent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(intent);
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
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
        if ( v == ivPhone)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

            builder.setTitle("Call to "+ tvCompany.getText().toString())
                    .setMessage("Are you sure you want to make a Call ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+tvPhone.getText().toString()));
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
        if ( v == lnrWebsite)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(ContactUsActivity.this, R.style.Blue_AlertDialog);

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
        if ( v == tvSend)
        {
            subject = etSubject.getText().toString();
            description = etDescription.getText().toString();

            if (subject.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Enter Subject",Toast.LENGTH_SHORT).show();
            }
            else if (description.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Enter Description",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_SHORT).show();
            }
        }
        if ( v == tvCancel)
        {
            finish();
        }
    }
}
