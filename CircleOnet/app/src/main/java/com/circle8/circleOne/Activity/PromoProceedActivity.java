package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Adapter.EventDetailAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PromoProceedActivity extends AppCompatActivity {

    EditText edtQuantity;
    Spinner spnLocation;
    Button btnSubmit;
    ImageView imgBack;
    String quantity = "", location = "";
    LoginSession session;
    private String UserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_proceed);

        edtQuantity = findViewById(R.id.edtQuantity);
        spnLocation = findViewById(R.id.spnLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgBack = findViewById(R.id.imgBack);
        session = new LoginSession(getApplicationContext());

        edtQuantity.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this,
                R.array.location_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spnLocation.setAdapter(adapterCountry);
        HashMap<String, String> user = session.getUserDetails();


        UserId = user.get(LoginSession.KEY_USERID);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtQuantity.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_LONG).show();
                }
                else if (spnLocation.getSelectedItem().toString().equalsIgnoreCase("Pickup Location")){
                    Toast.makeText(getApplicationContext(), "Please select location", Toast.LENGTH_LONG).show();
                }
                else {
                    quantity = edtQuantity.getText().toString();
                    location = String.valueOf(spnLocation.getSelectedItemPosition());

                    new HttpAsyncTask().execute(Utility.BASE_URL+"Promotion");

                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(PromoProceedActivity.this);
            dialog.setMessage("Submitting...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Location",   location);
                jsonObject.accumulate("Qty",   quantity);
                jsonObject.accumulate("userid",   UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Utility.POST2(urls[0],jsonObject );
        }
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check data connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equalsIgnoreCase("1")){

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromoProceedActivity.this);
                        alertDialogBuilder.setTitle("Success");
                        alertDialogBuilder.setMessage("Your request has been sent successfully, we will get in touch with you soon.");
                                alertDialogBuilder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                finish();
                                            }
                                        });


                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);



                       // Toast.makeText(getApplicationContext(), "Your request has been sent successfully, we will get in touch with you soon.", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_LONG).show();

                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
