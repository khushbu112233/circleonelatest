package com.amplearch.circleonet.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.ConnectivityReceiver;
import com.amplearch.circleonet.Model.User;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.PrefUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnSimpleLogin;
    //Boolean isConnected = false;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ImageView btnLogin;
    private ProgressDialog progressDialog;
    User user;
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        btnSimpleLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin = (ImageView) findViewById(R.id.fbLogin);
        loginButton= (LoginButton)findViewById(R.id.login_button);
      //  isConnected = checkConnection();
        btnSimpleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
            /* Create an Intent that will start the Menu-Activity. */
                        Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                        startActivity(intent);
                    }
                }, 100);


            }
        });

       /* btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* if (isConnected) {*//*
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    loginButton.performClick();

                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
               *//* }*//*
            }
        });

        if(PrefUtils.getCurrentUser(LoginActivity.this) != null){

            Intent homeIntent = new Intent(LoginActivity.this, CardsActivity.class);

            startActivity(homeIntent);

            finish();
        }*/
    }

    /*private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return  isConnected;
    }*/

    private void showSnack(boolean isConnected) {
        String message = "Check For Data Connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

  /*  @Override
    protected void onResume() {
        super.onResume();

        callbackManager=CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email","user_friends", "user_location");

        btnLogin= (ImageView) findViewById(R.id.fbLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                *//*if (isConnected) {*//*
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    loginButton.performClick();

                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
               *//* }*//*
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }*/

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            // Toast.makeText(AccountActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user,LoginActivity.this);
                                Intent intent=new Intent(LoginActivity.this,CardsActivity.class);
                                startActivity(intent);
                                finish();
                                //new UploadFacebook("IMG_" + timestamp1).execute();
                               /* JSONObject object1 = object.getJSONObject("location");
                                Toast.makeText(getApplicationContext(), object1.toString(), Toast.LENGTH_LONG).show();
*/
                                //  session.createUserLoginSession(user.name, user.email, "", "", "");

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //  Toast.makeText(AccountActivity.this,"welcome "+user.name,Toast.LENGTH_LONG).show();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


}
