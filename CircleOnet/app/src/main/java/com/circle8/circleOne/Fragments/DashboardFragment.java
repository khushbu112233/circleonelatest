package com.circle8.circleOne.Fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.AddQRActivity;
import com.circle8.circleOne.Activity.DashboardActivity;
import com.circle8.circleOne.Activity.Notification;
import com.circle8.circleOne.Activity.RewardsPointsActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.databinding.FragmentDashboardLayoutBinding;
import com.google.android.gms.maps.model.Dash;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.circle8.circleOne.Fragments.ProfileFragment.TextToImageEncode;
import static com.circle8.circleOne.Fragments.ProfileFragment.encrypt;

/**
 * Created by Ample-Arch on 06-01-2018.
 */

public class DashboardFragment extends Fragment {
    FragmentDashboardLayoutBinding fragmentDashboardLayoutBinding;
    View view;
    Context context;
    int count=0;
    String User_name;
    LoginSession session;
    String profileId = "";
    String UserId= "";
    private String refer;
    AlertDialog QR_AlertDialog ;
    ReferralCodeSession referralCodeSession;
    static Bitmap bitmap ;
    private static String secretKey = "1234567890234561";
    private Fragment fragment;
    static String barName;
    AutoCompleteTextView searchView;
    ImageView imgSearch;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        fragmentDashboardLayoutBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dashboard_layout, container, false);
        view = fragmentDashboardLayoutBinding.getRoot();
        context =getActivity();
        initClick();
        session = new LoginSession(context);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        DashboardActivity.setActionBarTitle("Dashboard");
        DashboardActivity.setDrawerVisibility(false);
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);      // name
        profileId = user.get(LoginSession.KEY_PROFILEID);
        User_name = user.get(LoginSession.KEY_NAME);
        searchView = view.findViewById(R.id.searchView);
        imgSearch = view.findViewById(R.id.imgSearch);
        referralCodeSession = new ReferralCodeSession(context);
        HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);

        try
        {
            barName = encrypt(profileId, secretKey);
            bitmap = TextToImageEncode(barName);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search = searchView.getText().toString();

                if (search.equals("")){
                }
                else {

                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.setActionBarTitle("Dashboard");
        count++;
        if(Pref.getValue(context,"appopen","").equalsIgnoreCase("1"))
        {
            fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            Pref.setValue(context,"appopen","");
        }else
        {
            if(count==1) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard1);
            }else if(count==2) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard2);
            }else if(count==3) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard3);
            }else if(count==4) {
                fragmentDashboardLayoutBinding.includeTapQr.rlMainDashboard.setBackgroundResource(R.drawable.ic_bg_dashboard4);
                count=0;
            }
        }
    }

    public void initClick() {

        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new Notification();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new RewardsPointsActivity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fragmentDashboardLayoutBinding.includeNotiRewardShare.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "I’m ready to connect with you and share our growing network on the CircleOne app. I’m currently a user with CircleOne and would like to invite you to join the Circle so we’ll both be able to take our professional newtorks a step further. Use the code '" + refer +
                        "' for a quick and simple registration! https://circle8.asia/mobileApp.html";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, User_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Profile Via"));
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlTapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QR_AlertDialog = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.person_qrcode, null);
                FrameLayout fl_QRFrame = (FrameLayout)dialogView.findViewById(R.id.fl_QrFrame);
                TextView tvBarName = (TextView)dialogView.findViewById(R.id.tvBarName);
                ImageView ivBarImage = (ImageView)dialogView.findViewById(R.id.ivBarImage);
//                tvBarName.setText(barName);
                //  alertDialog.setFeatureDrawableAlpha(R.color.colorPrimary, 8);

                ColorDrawable dialogColor = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
                dialogColor.setAlpha(70);
                QR_AlertDialog.getWindow().setBackgroundDrawable(dialogColor);
                // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                tvBarName.setText(User_name);
//                    bitmap = TextToImageEncode(barName);
                ivBarImage.setImageBitmap(bitmap);

                fl_QRFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        QR_AlertDialog.dismiss();
                    }
                });

                QR_AlertDialog.setView(dialogView);
                QR_AlertDialog.show();
            }
        });
        fragmentDashboardLayoutBinding.includeTapQr.rlScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddQRActivity.class);
                startActivity(intent);
            }
        });
    }

}