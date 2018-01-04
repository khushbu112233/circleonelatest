package com.circle8.circleOne.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Activity.ConnectActivity;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Activity.Notification;
import com.circle8.circleOne.Activity.WriteTestimonialActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.NotificationModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

/**
 * Created by admin on 08/29/2017.
 */

public class NotificationAdapter extends BaseAdapter
{
    private Context activity;
    ArrayList<NotificationModel> testimonialModels;
    private static LayoutInflater inflater = null;
    LinearLayout lnrTestReq, lnrTestRec, lnrFriend, lnrShare, lnrNFC;
    private int posi = 0;
    LoginSession loginSession;
    String profileId = "", UserId = "";
    String accept = "";

    String testimonial;
    EditText etTextMonial ;

    public NotificationAdapter(Context a, ArrayList<NotificationModel> testimonialModels)
    {
        this.activity = a;
        this.testimonialModels = testimonialModels;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return testimonialModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        CircleImageView imgTestReq, imgTestRec, imgFriend, imgShare, imgNfc;
        TextView txtNfcPurpose, txtNfcName, txtSharePurpose, txtShareName, txtTestPurpose, txtTestName, txtTestPurposeRec, txtTestNameRec, txtFriendPurpose, txtFriendName, txtRequested, txtRequestedTestReq, txtRequestedTestRec;
        Button btnAllowNfc, btnNfcCancel, btnTestWrite, btnTestReject, btnTestAcceptRec, btnTestRejectRec, btnAcceptFriend, btnRejectFriend, btnShareRequest, btnShareCancel;
        FrameLayout fm_imgTestReq, fm_imgTestRec, fm_imgFriend, fm_imgShare, fm_imgNFC;
        ProgressBar progressBarTestReq, progressBarTestRec, progressBarFriend, progressBarShare, progressBarNFC;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder = null;

        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.notification_item, null);
            holder = new ViewHolder();
            holder.fm_imgTestReq = (FrameLayout) vi.findViewById(R.id.fm_imgTestReq);
            holder.fm_imgTestRec = (FrameLayout) vi.findViewById(R.id.fm_imgTestRec);
            holder.fm_imgFriend = (FrameLayout) vi.findViewById(R.id.fm_imgFriend);
            holder.fm_imgShare = (FrameLayout) vi.findViewById(R.id.fm_imgShare);
            holder.fm_imgNFC = (FrameLayout) vi.findViewById(R.id.fm_imgNFC);


            holder.progressBarTestReq = (ProgressBar) vi.findViewById(R.id.progressBarTestReq);
            holder.progressBarTestRec = (ProgressBar) vi.findViewById(R.id.progressBarTestRec);
            holder.progressBarFriend = (ProgressBar) vi.findViewById(R.id.progressBarFriend);
            holder.progressBarShare = (ProgressBar) vi.findViewById(R.id.progressBarShare);
            holder.progressBarNFC = (ProgressBar) vi.findViewById(R.id.progressBarNFC);

            holder.imgShare = (CircleImageView) vi.findViewById(R.id.imgShare);
            holder.imgTestReq = (CircleImageView) vi.findViewById(R.id.imgTestReq);
            holder.imgTestRec = (CircleImageView) vi.findViewById(R.id.imgTestRec);
            holder.imgFriend = (CircleImageView) vi.findViewById(R.id.imgFriend);
            holder.txtRequested = (TextView) vi.findViewById(R.id.txtRequested);
            holder.txtTestPurpose = (TextView) vi.findViewById(R.id.txtTestPurpose);
            holder.txtTestName = (TextView) vi.findViewById(R.id.txtTestName);
            holder.txtTestPurposeRec = (TextView) vi.findViewById(R.id.txtTestPurposeRec);
            holder.txtTestNameRec = (TextView) vi.findViewById(R.id.txtTestNameRec);
            holder.txtFriendPurpose = (TextView) vi.findViewById(R.id.txtFriendPurpose);
            holder.txtFriendName = (TextView) vi.findViewById(R.id.txtFriendName);
            holder.txtRequestedTestReq = (TextView) vi.findViewById(R.id.txtRequestedTestReq);
            holder.txtRequestedTestRec = (TextView) vi.findViewById(R.id.txtRequestedTestRec);
            holder.btnTestWrite = (Button) vi.findViewById(R.id.btnTestWrite);
            holder.btnTestReject = (Button) vi.findViewById(R.id.btnTestReject);
            holder.btnTestAcceptRec = (Button) vi.findViewById(R.id.btnTestAcceptRec);
            holder.btnTestRejectRec = (Button) vi.findViewById(R.id.btnTestRejectRec);
            holder.btnAcceptFriend = (Button) vi.findViewById(R.id.btnAcceptFriend);
            holder.btnRejectFriend = (Button) vi.findViewById(R.id.btnRejectFriend);
            holder.txtShareName = (TextView) vi.findViewById(R.id.txtShareName);
            holder.txtSharePurpose = (TextView) vi.findViewById(R.id.txtSharePurpose);
            holder.btnShareRequest = (Button) vi.findViewById(R.id.btnReqShare);
            holder.btnShareCancel = (Button) vi.findViewById(R.id.btnShareCancel);
            holder.txtNfcName = (TextView) vi.findViewById(R.id.txtNfcName);
            holder.txtNfcPurpose = (TextView) vi.findViewById(R.id.txtNfcPurpose);
            holder.btnAllowNfc = (Button) vi.findViewById(R.id.btnAllowNfc);
            holder.btnNfcCancel = (Button) vi.findViewById(R.id.btnNfcCancel);
            holder.imgNfc = (CircleImageView) vi.findViewById(R.id.imgNfc);

            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }

        loginSession = new LoginSession(activity);
        HashMap<String, String> user = loginSession.getUserDetails();

        profileId = user.get(LoginSession.KEY_PROFILEID);
        UserId = user.get(LoginSession.KEY_USERID);
        lnrFriend = (LinearLayout) vi.findViewById(R.id.lnrFriend);
        lnrTestReq = (LinearLayout) vi.findViewById(R.id.lnrTestReq);
        lnrTestRec = (LinearLayout) vi.findViewById(R.id.lnrTestRec);
        lnrShare = (LinearLayout) vi.findViewById(R.id.lnrShare);
        lnrNFC = (LinearLayout) vi.findViewById(R.id.lnrNFC);

        String purpose = testimonialModels.get(position).getPurpose();

        if (purpose.equalsIgnoreCase("Recieved Testimonial"))
        {
            lnrNFC.setVisibility(View.GONE);
            lnrShare.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.GONE);

            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarTestRec.setVisibility(View.GONE);
                holder.imgTestRec.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarTestRec.setVisibility(View.VISIBLE);
               /* Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgTestRec);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgTestRec) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarTestRec.setVisibility(View.GONE);
                                finalHolder.imgTestRec.setImageBitmap(drawable);
                            }
                        });

            }
            /* if (testimonialModels.get(position).getStatus().equalsIgnoreCase("Requested"))
            {
                holder.btnTestAcceptRec.setVisibility(View.GONE);
                holder.btnTestRejectRec.setVisibility(View.GONE);
                holder.txtRequestedTestRec.setVisibility(View.VISIBLE);
            }*/
            holder.txtTestPurposeRec.setText(purpose);
            holder.txtTestNameRec.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }

        /*else if (purpose.equalsIgnoreCase("Connection Accepted"))
        {
            lnrFriend.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.imgFriend.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(holder.imgFriend);
            }
            if (testimonialModels.get(position).getStatus().equalsIgnoreCase("Accepted"))
            {
                holder.btnAcceptFriend.setVisibility(View.GONE);
                holder.btnRejectFriend.setVisibility(View.GONE);
                holder.txtRequested.setVisibility(View.VISIBLE);
                holder.txtRequested.setText("Accepted");
            }
            holder.txtFriendPurpose.setText(purpose);
            holder.txtFriendName.setText(testimonialModels.get(position).getFirstName());
        }*/

        /*else if (purpose.equalsIgnoreCase("Access Right Accepted"))
        {
            lnrFriend.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.imgFriend.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(holder.imgFriend);
            }

                holder.btnAcceptFriend.setVisibility(View.GONE);
                holder.btnRejectFriend.setVisibility(View.GONE);
                holder.txtRequested.setVisibility(View.VISIBLE);
            holder.txtRequested.setText("Accepted");
            holder.txtFriendPurpose.setText(purpose);
            holder.txtFriendName.setText(testimonialModels.get(position).getFirstName());
        }*/

        else if (purpose.equalsIgnoreCase("Access Right Requested") || purpose.equalsIgnoreCase("Connection Requested"))
        {
            lnrNFC.setVisibility(View.GONE);
            lnrShare.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarFriend.setVisibility(View.GONE);
                holder.imgFriend.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarFriend.setVisibility(View.VISIBLE);

              /*  Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgFriend);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgFriend) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarFriend.setVisibility(View.GONE);
                                finalHolder.imgFriend.setImageBitmap(drawable);
                            }
                        });
            }
            holder.btnAcceptFriend.setVisibility(View.VISIBLE);
            holder.btnRejectFriend.setVisibility(View.VISIBLE);
            /*if (testimonialModels.get(position).getStatus().equalsIgnoreCase("Requested"))
            {
                holder.btnAcceptFriend.setVisibility(View.GONE);
                holder.btnRejectFriend.setVisibility(View.GONE);
                holder.txtRequested.setVisibility(View.VISIBLE);
            }*/
            if (purpose.equalsIgnoreCase("Access Right Requested")) {
                holder.txtFriendPurpose.setText("Do you wish to connect?");
            }
            else if (purpose.equalsIgnoreCase("Connection Requested")) {
                holder.txtFriendPurpose.setText("I would like to connect with you!");
            }
            holder.txtFriendName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }

        else if (purpose.equalsIgnoreCase("Recieved Testimonial Request"))
        {
            lnrNFC.setVisibility(View.GONE);
            lnrShare.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.GONE);
            lnrTestReq.setVisibility(View.VISIBLE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarTestReq.setVisibility(View.GONE);
                holder.imgTestReq.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarTestReq.setVisibility(View.VISIBLE);
               /* Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgTestReq);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgTestReq) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarTestReq.setVisibility(View.GONE);
                                finalHolder.imgTestReq.setImageBitmap(drawable);
                            }
                        });
            }

            /*if (testimonialModels.get(position).getStatus().equals("Requested")){
                holder.btnTestReject.setVisibility(View.GONE);
                holder.btnTestWrite.setVisibility(View.GONE);
                holder.txtRequestedTestReq.setVisibility(View.VISIBLE);
            }*/

            holder.txtTestPurpose.setText("I would like to receive a testimonial from you!");
            holder.txtTestName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }

        else if (purpose.equalsIgnoreCase("Card Shared"))
        {
            lnrNFC.setVisibility(View.GONE);
            lnrShare.setVisibility(View.VISIBLE);
            lnrFriend.setVisibility(View.GONE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarShare.setVisibility(View.GONE);
                holder.imgShare.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarShare.setVisibility(View.VISIBLE);
                /*Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgShare);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgShare) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarShare.setVisibility(View.GONE);
                                finalHolder.imgShare.setImageBitmap(drawable);
                            }
                        });
            }

            /*if (testimonialModels.get(position).getStatus().equals("Requested")){
                holder.btnTestReject.setVisibility(View.GONE);
                holder.btnTestWrite.setVisibility(View.GONE);
                holder.txtRequestedTestReq.setVisibility(View.VISIBLE);
            }*/

            holder.txtSharePurpose.setText(purpose);
            holder.txtShareName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }
        else if (purpose.equalsIgnoreCase("Connection Card Access Request"))
        {
            lnrNFC.setVisibility(View.VISIBLE);
            lnrShare.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.GONE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarNFC.setVisibility(View.GONE);
                holder.imgNfc.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarNFC.setVisibility(View.VISIBLE);
                /*Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgNfc);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgNfc) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarNFC.setVisibility(View.GONE);
                                finalHolder.imgNfc.setImageBitmap(drawable);
                            }
                        });
            }
            holder.btnAllowNfc.setVisibility(View.VISIBLE);
            holder.btnNfcCancel.setVisibility(View.VISIBLE);
            /*if (testimonialModels.get(position).getStatus().equals("Requested")){
                holder.btnTestReject.setVisibility(View.GONE);
                holder.btnTestWrite.setVisibility(View.GONE);
                holder.txtRequestedTestReq.setVisibility(View.VISIBLE);
            }*/

            holder.txtNfcPurpose.setText(purpose);
            holder.txtNfcName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }

        /*else if (purpose.equalsIgnoreCase("Sent Testimonial Request"))
        {
            lnrFriend.setVisibility(View.GONE);
            lnrTestReq.setVisibility(View.VISIBLE);
            lnrTestRec.setVisibility(View.GONE);
            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.imgTestReq.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(holder.imgTestReq);
            }

            if (testimonialModels.get(position).getStatus().equals("Requested")){
                holder.btnTestReject.setVisibility(View.GONE);
                holder.btnTestWrite.setVisibility(View.GONE);
                holder.txtRequestedTestReq.setVisibility(View.VISIBLE);
            }

            holder.txtTestPurpose.setText(purpose);
            holder.txtTestName.setText(testimonialModels.get(position).getFirstName());
        }*/
        else
        {
            lnrNFC.setVisibility(View.GONE);
            lnrShare.setVisibility(View.GONE);
            lnrFriend.setVisibility(View.VISIBLE);
            lnrTestReq.setVisibility(View.GONE);
            lnrTestRec.setVisibility(View.GONE);

            if (testimonialModels.get(position).getUserPhoto().equals(""))
            {
                holder.progressBarFriend.setVisibility(View.GONE);
                holder.imgFriend.setImageResource(R.drawable.usr);
            }
            else
            {
                holder.progressBarFriend.setVisibility(View.VISIBLE);
                /*Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgFriend);
*/
                final ViewHolder finalHolder = holder;
                Glide.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imgFriend) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                finalHolder.progressBarFriend.setVisibility(View.GONE);
                                finalHolder.imgFriend.setImageBitmap(drawable);
                            }
                        });
            }
            holder.btnAcceptFriend.setVisibility(View.GONE);
            holder.btnRejectFriend.setVisibility(View.GONE);
            holder.txtRequested.setVisibility(View.GONE);
            holder.txtRequested.setText("Accepted");

            if (purpose.equalsIgnoreCase("Connection Accepted")) {
                holder.txtFriendPurpose.setText("We are now connected!");
            }
            else if (purpose.equalsIgnoreCase("Connection Rejected")) {
                holder.txtFriendPurpose.setText("Connection Rejected");
            }
            else if (purpose.equalsIgnoreCase("Access Right Accepted")) {
                holder.txtFriendPurpose.setText("We are now connected!");
            }
            else if (purpose.equalsIgnoreCase("Received Testimonial")) {
                holder.txtFriendPurpose.setText("You have received a testimonial!");
            }
            else if (purpose.equalsIgnoreCase("Sent Testimonial Request")) {
                holder.txtFriendPurpose.setText("Testimonial Request Accepted!");
            }
            else if (purpose.equalsIgnoreCase("Received Testimonial Request")) {
                holder.txtFriendPurpose.setText("I would like to receive a testimonial from you!");
            }
            else {

                holder.txtFriendPurpose.setText(purpose);
            }
            holder.txtFriendName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if ((testimonialModels.get(position).getShared_ProfileID().toString().equalsIgnoreCase("") ||
                        testimonialModels.get(position).getShared_ProfileID().toString().equalsIgnoreCase("null") ||
                        testimonialModels.get(position).getShared_ProfileID().toString().equalsIgnoreCase(null))
                        && (testimonialModels.get(position).getShared_UserID().toString().equalsIgnoreCase("") ||
                        testimonialModels.get(position).getShared_UserID().toString().equalsIgnoreCase("null") ||
                        testimonialModels.get(position).getShared_UserID().toString().equalsIgnoreCase(null)))
                {
                    Intent intent = new Intent(activity, ConnectActivity.class);
                    intent.putExtra("friendProfileID", testimonialModels.get(position).getFriendProfileID());
                    intent.putExtra("friendUserID", testimonialModels.get(position).getFriendUserID());
                    intent.putExtra("ProfileID", profileId);
                    activity.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(activity, ConnectActivity.class);
                    intent.putExtra("friendProfileID", testimonialModels.get(position).getShared_ProfileID());
                    intent.putExtra("friendUserID", testimonialModels.get(position).getShared_UserID());
                    intent.putExtra("ProfileID", profileId);
                    activity.startActivity(intent);
                }
            }
        });

        holder.btnTestWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                posi = position;

                String userImg = testimonialModels.get(posi).getUserPhoto();
                String userName = testimonialModels.get(posi).getFirstName()+" "+testimonialModels.get(posi).getLastName();
                String friendProfileId = testimonialModels.get(posi).getFriendProfileID();

//                Toast.makeText(activity,friendProfileId+" "+userName,Toast.LENGTH_LONG).show();

                Intent in = new Intent(activity, WriteTestimonialActivity.class);
                in.putExtra("ProfileID", profileId);
                in.putExtra("FriendProfileID", friendProfileId);
                in.putExtra("UserImg", userImg);
                in.putExtra("UserName", userName);
                activity.startActivity(in);

                /*final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                final View dialogView = inflater.inflate(R.layout.textimonial_write, null);
                etTextMonial = (EditText)dialogView.findViewById(R.id.etTestiMonial);
                Button btnWrite = (Button)dialogView.findViewById(R.id.btnWrite);
                Button btnCancel = (Button)dialogView.findViewById(R.id.btnCancel);
                btnWrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        posi = position;
                        final String text = etTextMonial.getText().toString();
                        testimonial = text ;
                        if(testimonial.isEmpty())
                        {
                            etTextMonial.setError("Please write testimonial");
                        }
                        else
                        {
                            etTextMonial.setText(null);
                            alertDialog.dismiss();
                            new HttpAsyncWriteTextimonial().execute(Utility.BASE_URL+"Testimonial/Write");
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();*/
            }
        });

        holder.btnNfcCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                new HttpAsyncTaskShareCancel().execute(Utility.BASE_URL+"ShareProfile/CancelNotification");
            }
        });

        holder.btnAllowNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                new HttpAsyncTaskRequestFriend().execute(Utility.BASE_URL+"FriendConnection_Operation");
            }
        });
        holder.btnShareRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                //  new HttpAsyncTaskAcceptFriend().execute(Utility.BASE_URL+"ShareProfile/Request");
                Intent intent = new Intent(activity, ConnectActivity.class);
                intent.putExtra("friendProfileID", testimonialModels.get(position).getShared_ProfileID());
                intent.putExtra("friendUserID", testimonialModels.get(position).getShared_UserID());
                intent.putExtra("ProfileID", profileId);
                activity.startActivity(intent);
            }
        });

        holder.btnShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                new HttpAsyncTaskShareCancel().execute(Utility.BASE_URL+"ShareProfile/CancelNotification");
            }
        });

        holder.btnAcceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                new HttpAsyncTaskAcceptFriend().execute(Utility.BASE_URL+"FriendConnection_Operation");
            }
        });

        holder.btnRejectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                new HttpAsyncTaskRejectFriend().execute(Utility.BASE_URL+"FriendConnection_Operation");
            }
        });

        holder.btnTestAcceptRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                accept = "1";
                new HttpAsyncTaskAcceptTestimonial().execute(Utility.BASE_URL+"Testimonial/Accept_Reject");
            }
        });

        holder.btnTestRejectRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                accept = "0";
                new HttpAsyncTaskAcceptTestimonial().execute(Utility.BASE_URL+"Testimonial/Accept_Reject");
            }
        });

        holder.btnTestReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                accept = "0";
                new HttpAsyncTaskAcceptTestimonial().execute(Utility.BASE_URL+"Testimonial/Accept_Reject");
            }
        });

        holder.imgFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (testimonialModels.get(position).getUserPhoto().equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto()).placeholder(R.drawable.usr_1)
                            .resize(300,300).onlyScaleDown().into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto());
                        activity.startActivity(intent);
                    }
                });
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                dialog.show();
            }
        });

        holder.imgTestRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (testimonialModels.get(position).getUserPhoto().equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto()).placeholder(R.drawable.usr_1)
                            .resize(300,300).onlyScaleDown().into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto());
                        activity.startActivity(intent);
                    }
                });
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                dialog.show();
            }
        });

        holder.imgTestReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (testimonialModels.get(position).getUserPhoto().equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto()).placeholder(R.drawable.usr_1)
                            .resize(300,300).onlyScaleDown().into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"UserProfile/"+testimonialModels.get(position).getUserPhoto());
                        activity.startActivity(intent);
                    }
                });
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                dialog.show();
            }
        });

        return vi;
    }

    private class HttpAsyncTaskAcceptTestimonial extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Please Wait..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Please wait";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTAcceptTest(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        if (accept.equals("1"))
                        {
                            Toast.makeText(activity, "Testimonial Accepted..", Toast.LENGTH_LONG).show();
                            Notification.webCall();
                        }
                        else if (accept.equals("0"))
                        {
                            Toast.makeText(activity, "Testimonial Rejected..", Toast.LENGTH_LONG).show();
                            Notification.webCall();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskRejectFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Please Wait..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Please wait";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POSTReject(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Friend Request Rejected..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncWriteTextimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Writing Testimonial..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Writing testimonial";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POSTWriteTextimonial(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result.equals(""))
                {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("TestimonialId");
                    String success = response.getString("Success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Testimonial Written Successfully..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskRequestFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Accepting Friend Request..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Requesting";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST5(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);

//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Connection Request sent..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskShareCancel extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Accepting Friend Request..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Cancelling";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST6(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);

//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Share Request Rejected..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskAcceptFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(activity);
            dialog.setMessage("Accepting Friend Request..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Accepting friend request";
            Notification.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            Notification.activityNotificationBinding.rlProgressDialog.setVisibility(View.GONE);

//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result.equals("")) {
                    Toast.makeText(activity, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if (success.equals("1"))
                    {
                        Toast.makeText(activity, "Friend Request Accepted..", Toast.LENGTH_LONG).show();
                        Notification.webCall();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Operation", "Accept");
            jsonObject.accumulate("RequestType", "");
            jsonObject.accumulate("connection_date", Utility.currentDate());
            jsonObject.accumulate("friendProfileId", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myProfileId", profileId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POST5(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Operation", "Request");
            jsonObject.accumulate("RequestType", "");
            jsonObject.accumulate("connection_date", Utility.currentDate());
            jsonObject.accumulate("friendProfileId", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myProfileId", profileId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POST6(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("NotificationID", testimonialModels.get(posi).getNotificationID());
            jsonObject.accumulate("NotificationStatus", testimonialModels.get(posi).getStatus());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POSTWriteTextimonial(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Testimonial_text", testimonial);
            jsonObject.accumulate("friendprofileID", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myprofileID", profileId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


    public String POSTAcceptTest(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Accept", accept);
            jsonObject.accumulate("TestimonialId", testimonialModels.get(posi).getNotificationID());
            jsonObject.accumulate("userId", UserId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    public String POSTReject(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Operation", "Reject");
            jsonObject.accumulate("RequestType", "");
            jsonObject.accumulate("connection_date", Utility.currentDate());
            jsonObject.accumulate("friendProfileId", testimonialModels.get(posi).getFriendProfileID());
            jsonObject.accumulate("myProfileId", profileId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


}