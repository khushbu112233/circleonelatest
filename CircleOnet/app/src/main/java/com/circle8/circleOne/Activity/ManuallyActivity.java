package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ManualLayoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.callSubPAge;
import static com.circle8.circleOne.Utils.Validation.validateManually;

/**
 * Created by ample-arch on 1/30/2018.
 */

public class ManuallyActivity extends AppCompatActivity {

    public static ManualLayoutBinding manualLayoutBinding;
    String  first_name, last_name, phone_no, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manualLayoutBinding = DataBindingUtil.setContentView(this, R.layout.manual_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        manualLayoutBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        manualLayoutBinding.lnrSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name = manualLayoutBinding.etFirstName.getText().toString();
                last_name = manualLayoutBinding.etLastName.getText().toString();
                phone_no = manualLayoutBinding.etPhone.getText().toString();
                email = manualLayoutBinding.etEmail.getText().toString();
                String code;
                try
                {
                    code = manualLayoutBinding.ccp.getSelectedCountryCodeWithPlus().toString();
                }
                catch (Exception e){
                    code = manualLayoutBinding.ccp.getDefaultCountryCodeWithPlus().toString();
                }

                // String code = tvCountryCode.getText().toString();
                String contact = phone_no;
                if (!contact.equals("")) {
                    phone_no = code + " " + contact;
                }
                if (!validateManually(ManuallyActivity.this, first_name, last_name, phone_no, email))
                {
                    Toast.makeText(getApplicationContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                }else
                {
                    new HttpAsyncTask().execute(Utility.BASE_URL + "AddContact");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        callSubPAge("OnTapAddManually","Dashboard");

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Contact_Email", email);
                jsonObject.accumulate("Contact_FirstName",first_name);
                jsonObject.accumulate("Contact_LastName", last_name);
                jsonObject.accumulate("Contact_Phone", phone_no);
                jsonObject.accumulate("My_Email", Pref.getValue(ManuallyActivity.this,"UserEmail",""));
                jsonObject.accumulate("My_FirstName", Pref.getValue(ManuallyActivity.this,"UserFirstName",""));
                jsonObject.accumulate("My_LastName", Pref.getValue(ManuallyActivity.this,"UserLastName",""));
                if(!Pref.getValue(ManuallyActivity.this,"UserPhone","").equalsIgnoreCase("")) {
                    jsonObject.accumulate("My_Phone", Pref.getValue(ManuallyActivity.this, "UserPhone", ""));
                    // jsonObject.accumulate("My_Phone","+65 7869690860");

                }
                jsonObject.accumulate("ReferralCode",Pref.getValue(ManuallyActivity.this,"ReferrenceCode",""));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {
//            dialog.dismiss();
            Log.e("manualresult",""+result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.optString("success").equalsIgnoreCase("1"))
                {
                    String message = jsonObject.optString("message");
                    Toast.makeText(ManuallyActivity.this,message,Toast.LENGTH_SHORT).show();
                    Pref.setValue(ManuallyActivity.this,"manualdone","1");
                    Intent i = new Intent(ManuallyActivity.this,DashboardActivity.class);
                    startActivity(i);
                }else
                {
                    String message = jsonObject.optString("message");
                    Toast.makeText(ManuallyActivity.this,message,Toast.LENGTH_SHORT).show();
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //01-31 14:01:48.615 15167-15167/com.circle8.circleOne E/manualresult: {"success":"1","userId":"1083","Status":null,"message":"Successfully added."}


        }
    }

}
