package com.amplearch.circleonet.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.amplearch.circleonet.Activity.LoginActivity;

import java.util.HashMap;

/**
 * Created by admin on 07/20/2017.
 */

public class LoginSession {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LoginSession";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_USERID = "userid";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_IMAGE = "image";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PROFILEID = "profile_id";

    // Constructor
    public LoginSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String profileid, String userid, String name, String email, String image, String gender){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PROFILEID, profileid);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_GENDER, gender);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_PROFILEID, pref.getString(KEY_PROFILEID, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
