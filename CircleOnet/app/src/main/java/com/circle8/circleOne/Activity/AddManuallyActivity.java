package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.circle8.circleOne.R;
import com.circle8.circleOne.databinding.ActivityAddManuallyBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddManuallyActivity extends AppCompatActivity implements View.OnClickListener
{

    ActivityAddManuallyBinding activityAddManuallyBinding ;
    List<String> scanTextLineList = new ArrayList<>();

    String NAME = "", EMAIL = "", PHONE = "", WEBSITE = "", ADDRESS = "", COMPANY = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddManuallyBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_manually);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        activityAddManuallyBinding.tvMoreInfo.setOnClickListener(this);
        activityAddManuallyBinding.imgBack.setOnClickListener(this);

        /*now get ArrayString*/
        Intent iGet = getIntent();
        String [] scanTextArray = iGet.getStringArrayExtra("ScanTextArray");

        /*convert StringArray to List<>*/
        List<String> scanTextLineList1 = new ArrayList<String>(Arrays.asList(scanTextArray));
        scanTextLineList = scanTextLineList1;

        NAME = "^([A-Z]([a-z]*|\\.) *){1,2}([A-Z][a-z]+-?)+$";
        EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        PHONE = "(?:^|\\D)(\\d{3})[)\\-. ]*?(\\d{3})[\\-. ]*?(\\d{4})(?:$|\\D)";

        for (int i = 0; i < scanTextLineList.size(); i++)
        {
            String size = String.valueOf(scanTextLineList.size());

            String scanItems = scanTextLineList.get(i);
            if (scanItems.matches(NAME))
            {
                String[] name = scanItems.split(" ");

                activityAddManuallyBinding.etFirstName.setText(name[0]);
                activityAddManuallyBinding.etLastName.setText(name[1]);
                scanTextLineList.remove(i);
            }
            else if (scanItems.matches(EMAIL) || scanItems.contains("@"))
            {
                activityAddManuallyBinding.etEmail.append(scanItems+"\n");
                scanTextLineList.remove(i);
            }
            else if (scanItems.matches(PHONE) || scanItems.startsWith("+") || scanItems.startsWith("0"))
            {
                activityAddManuallyBinding.etPhone.append(scanItems+"\n");
                scanTextLineList.remove(i);
            }
            else if (scanItems.startsWith("www"))
            {
                activityAddManuallyBinding.etWebsite.setText(scanItems);
                scanTextLineList.remove(i);
            }
            else if (scanItems.contains("pvt") || scanItems.contains("ltd") || scanItems.contains("llp"))
            {
                activityAddManuallyBinding.etCompany.setText(scanItems);
                scanTextLineList.remove(i);
            }
            else
            {
                int n = scanTextLineList.size();

                String[] scanTextArray1 = scanTextLineList.toArray(new String[n]);
                StringBuilder builder = new StringBuilder();
                for (String string : scanTextArray1)
                {
                    builder.append(System.lineSeparator());
                    builder.append(string);

                }
                activityAddManuallyBinding.etAddress.setText(builder);
//                Toast.makeText(getApplicationContext(),builder,Toast.LENGTH_SHORT).show();
//                activityAddManuallyBinding.etAddress.setText(Arrays.toString(scanTextArray1));
            }
        }
        scanTextLineList.clear();
    }

    @Override
    public void onClick(View v)
    {
        if ( v == activityAddManuallyBinding.tvMoreInfo)
        {
            if (activityAddManuallyBinding.llMoreLay.getVisibility() == View.VISIBLE)
            {
                activityAddManuallyBinding.llMoreLay.setVisibility(View.GONE);
            }
            else
            {
                activityAddManuallyBinding.llMoreLay.setVisibility(View.VISIBLE);
            }
        }
        if ( v == activityAddManuallyBinding.imgBack)
        {
            scanTextLineList.clear();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        scanTextLineList.clear();
        finish();
    }
}
