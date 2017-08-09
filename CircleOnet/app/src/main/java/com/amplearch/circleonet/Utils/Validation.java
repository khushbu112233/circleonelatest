package com.amplearch.circleonet.Utils;

import com.amplearch.circleonet.Activity.LoginActivity;
import com.amplearch.circleonet.Activity.RegisterActivity;

/**
 * Created by ample-arch on 7/28/2017.
 */

public class Validation
{
    public static boolean validate(String userName,String firstName, String lastName, String password, String rePassword, String contactNo, String emailAddress)
    {
        boolean valid = true ;

        if(userName.isEmpty() || userName.length() < 3 )
        {
            RegisterActivity.etUserName.setError("Minimum 3 Characters.");
            valid = false ;
        }
        else
        {
            RegisterActivity.etUserName.setError(null);
        }

        if(firstName.isEmpty() || firstName.length() < 3 )
        {
            RegisterActivity.etFirstName.setError("Minimum 3 Characters.");
            valid = false ;
        }
        else
        {
            RegisterActivity.etFirstName.setError(null);
        }

        if(lastName.isEmpty() || lastName.length() < 3 )
        {
            RegisterActivity.etLastName.setError("Minimum 3 Characters.");
            valid = false ;
        }
        else
        {
            RegisterActivity.etLastName.setError(null);
        }

        if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            RegisterActivity.etEmail.setError("Not a valid Email Address");
            valid = false;
        }
        else {
            RegisterActivity.etEmail.setError(null);
        }

        if(contactNo.isEmpty() || contactNo.length() != 10  )
        {
            RegisterActivity.etPhone.setError("10 Characters Required");
            valid = false ;
        }
        else if(contactNo.length() > 10){
            RegisterActivity.etPhone.setError("10 Characters Required");
        }
        else {
            RegisterActivity.etPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 4)
        {
            RegisterActivity.etPassword.setError("Minimum 4 Characters");
            valid = false;
        }
        else {
            RegisterActivity.etPassword.setError(null);
        }

        if (rePassword.equals(password))
        {

            RegisterActivity.etConfirmPass.setError(null);
        }
        else {
            RegisterActivity.etConfirmPass.setError("Password does not match");
            valid = false;
        }

        return valid;
    }

    public static boolean validateLogin(String userName, String password)
    {
        boolean valid = true ;

        if(userName.isEmpty())
        {
            LoginActivity.etLoginUser.setError("Enter UserName");
            valid = false ;
        }
        else
        {
            LoginActivity.etLoginUser.setError(null);
        }

        if(password.isEmpty())
        {
            LoginActivity.etLoginPass.setError("Enter Password");
            valid = false ;
        }
        else
        {
            LoginActivity.etLoginPass.setError(null);
        }

        return valid;
    }

}
