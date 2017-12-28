package com.circle8.circleOne.Utils;

import android.content.Context;
import android.view.View;

import com.circle8.circleOne.Activity.LoginActivity;
import com.circle8.circleOne.Activity.MyAccountActivity;
import com.circle8.circleOne.Activity.RegisterActivity;

/**
 * Created by ample-arch on 7/28/2017.
 */

public class Validation
{
    public static boolean validate(Context context,String userName, String firstName, String lastName, String password, String rePassword, String contactNo, String emailAddress)
    {
        boolean valid = true ;

        if(userName.isEmpty() || userName.length() < 1 )
        {
//            RegisterActivity.etUserName.setError("Minimum 3 Characters.");
            ((RegisterActivity)context).activityRegisterBinding.tvUsernameInfo.setText("Mini 1 Characters");
            ((RegisterActivity)context).activityRegisterBinding.tvUsernameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etUserName.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvUsernameInfo.setVisibility(View.GONE);
        }

        if(firstName.isEmpty() || firstName.length() < 1 )
        {
//            RegisterActivity.etFirstName.setError("Minimum 3 Characters.");
            ((RegisterActivity)context).activityRegisterBinding.tvFirstNameInfo.setText("Mini 1 Characters");
            ((RegisterActivity)context).activityRegisterBinding.tvFirstNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etFirstName.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvFirstNameInfo.setVisibility(View.GONE);
        }

        if(lastName.isEmpty() || lastName.length() < 1 )
        {
//            RegisterActivity.etLastName.setError("Minimum 3 Characters.");
            ((RegisterActivity)context).activityRegisterBinding.tvLastNameInfo.setText("Mini 1 Characters");
            ((RegisterActivity)context).activityRegisterBinding.tvLastNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etLastName.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvLastNameInfo.setVisibility(View.GONE);
        }

        if (emailAddress.isEmpty())
        {
//            RegisterActivity.etEmail.setError("Not a valid Email Address");
            ((RegisterActivity)context).activityRegisterBinding.tvEmailInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            ((RegisterActivity)context).activityRegisterBinding.tvEmailInfo.setText("Not a valid Email Address");
            ((RegisterActivity)context).activityRegisterBinding.tvEmailInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etEmail.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvEmailInfo.setVisibility(View.GONE);
        }

        if(contactNo.isEmpty()  )
        {
//            RegisterActivity.etPhone.setError("10 Characters Required");
            ((RegisterActivity)context).activityRegisterBinding.tvPhoneInfo.setText("Enter Contact Number");
            ((RegisterActivity)context).activityRegisterBinding.tvPhoneInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etPhone.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvPhoneInfo.setVisibility(View.GONE);
        }

        if (password.isEmpty() || password.length() < 4)
        {
//            RegisterActivity.etPassword.setError("Minimum 4 Characters");
            ((RegisterActivity)context).activityRegisterBinding.tvPasswordInfo.setText("Min 4 Characters");
            ((RegisterActivity)context).activityRegisterBinding.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etPassword.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvPasswordInfo.setVisibility(View.GONE);
        }

        if(rePassword.isEmpty())
        {
            ((RegisterActivity)context).activityRegisterBinding.tvAgainPasswordInfo.setText("Repeat password");
            ((RegisterActivity)context).activityRegisterBinding.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (rePassword.equals(password))
        {
//            RegisterActivity.etConfirmPass.setError(null);
            ((RegisterActivity)context).activityRegisterBinding.tvAgainPasswordInfo.setVisibility(View.GONE);
        }
        else
        {
//            RegisterActivity.etConfirmPass.setError("Password does not match");
            ((RegisterActivity)context).activityRegisterBinding.tvAgainPasswordInfo.setText("Password does not match");
            ((RegisterActivity)context).activityRegisterBinding.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }

    public static boolean updateRegisterValidate(Context context, String firstName, String lastName, String password, String rePassword, String contactNo)
    {
        boolean valid = true ;

        if(firstName.isEmpty() || firstName.length() < 1 )
        {
//            RegisterActivity.etFirstName.setError("Minimum 3 Characters.");
            ((MyAccountActivity)context).myAccountBinding.tvFirstNameInfo.setText("Mini 1 Characters");
            ((MyAccountActivity)context).myAccountBinding.tvFirstNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etFirstName.setError(null);
            ((MyAccountActivity)context).myAccountBinding.tvFirstNameInfo.setVisibility(View.GONE);
        }

        if(lastName.isEmpty() || lastName.length() < 1 )
        {
//            RegisterActivity.etLastName.setError("Minimum 3 Characters.");
            ((MyAccountActivity)context).myAccountBinding.tvLastNameInfo.setText("Mini 1 Characters");
            ((MyAccountActivity)context).myAccountBinding.tvLastNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etLastName.setError(null);
            ((MyAccountActivity)context).myAccountBinding.tvLastNameInfo.setVisibility(View.GONE);
        }

        if(contactNo.isEmpty() )
        {
//            RegisterActivity.etPhone.setError("10 Characters Required");
            ((MyAccountActivity)context).myAccountBinding.tvPhoneInfo.setText("Enter Phone Number");
            ((MyAccountActivity)context).myAccountBinding.tvPhoneInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etPhone.setError(null);
            ((MyAccountActivity)context).myAccountBinding.tvPhoneInfo.setVisibility(View.GONE);
        }

        if (password.isEmpty() || password.length() < 4)
        {
//            RegisterActivity.etPassword.setError("Minimum 4 Characters");
            ((MyAccountActivity)context).myAccountBinding.tvPasswordInfo.setText("Mini 4 Characters");
            ((MyAccountActivity)context).myAccountBinding.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etPassword.setError(null);
            ((MyAccountActivity)context).myAccountBinding.tvPasswordInfo.setVisibility(View.GONE);
        }

        if(rePassword.isEmpty())
        {
            ((MyAccountActivity)context).myAccountBinding.tvAgainPasswordInfo.setText("Enter Re-Password");
            ((MyAccountActivity)context).myAccountBinding.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (rePassword.equals(password))
        {
//            RegisterActivity.etConfirmPass.setError(null);
            ((MyAccountActivity)context).myAccountBinding.tvAgainPasswordInfo.setVisibility(View.GONE);
        }
        else
        {
//            RegisterActivity.etConfirmPass.setError("Password does not match");
            ((MyAccountActivity)context).myAccountBinding.tvAgainPasswordInfo.setText("Password does not match");
            ((MyAccountActivity)context).myAccountBinding.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }

    public static boolean validateLogin(Context context, String userName, String password)
    {
        boolean valid = true ;

        if(userName.isEmpty())
        {
//            LoginActivity.etLoginUser.setError(Html.fromHtml("<font color='red'>Username can't be empty</font>"));
           // LoginActivity.tvUsernameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            LoginActivity.etLoginUser.setError(null);
            ((LoginActivity)context).activityLoginBinding.tvUserInfo.setVisibility(View.GONE);
        }

        if(password.isEmpty())
        {
//            LoginActivity.etLoginPass.setError("Enter Password");
           // LoginActivity.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            LoginActivity.etLoginPass.setError(null);
            ((LoginActivity)context).activityLoginBinding.tvPasswordInfo.setVisibility(View.GONE);
        }

        return valid;
    }

}
