package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.circle8.circleOne.databinding.ActivityEventDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDetail extends AppCompatActivity implements View.OnClickListener
{
    private TextView actionText;
    private ImageView imgDrawer,imgBack;
    private ArrayList<EventModel> eventModelArrayList = new ArrayList<>();
    private ArrayList<EventModel> eventModelArrayList1 = new ArrayList<>();
    String event_ID ;
    private LoginSession session;
    String UserID = "";
    String eventBook = "", eventRegister = "";
    ActivityEventDetailBinding activityEventDetailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityEventDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_event_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_2);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgBack = (ImageView) findViewById(R.id.imgLogo);
        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Events");
        imgBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        UserID = user.get(LoginSession.KEY_USERID);
        imgDrawer.setVisibility(View.GONE);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);

        // Intent i = getIntent();
        // event_ID = i.getStringExtra("Event_ID");

        new HttpAsyncTask().execute(Utility.BASE_URL+"Events/GetDetails");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.freeMemory();
                finish();
            }
        });
        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),EventsSelectOption.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);
                finish();
            }
        });
        activityEventDetailBinding.lnreventReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTaskEventRegistration().execute();
            }
        });
        activityEventDetailBinding.txtRegister.setOnClickListener(this);
        activityEventDetailBinding.txtBook.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.txtRegister:
                Utility.freeMemory();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(EventDetail.this, R.style.Blue_AlertDialog);
                builder.setTitle("Circle One")
                        .setMessage("Are you sure you want to exit to "+eventRegister+"?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
                                intent.putExtra("url", eventRegister);
                                startActivity(intent);
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
                break;
            case R.id.txtBook:
                Utility.freeMemory();
                AlertDialog.Builder builder1;
                builder1 = new AlertDialog.Builder(EventDetail.this, R.style.Blue_AlertDialog);
                builder1.setTitle("Circle One")
                        .setMessage("Are you sure you want to exit to "+eventBook+"?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(getApplicationContext(), AttachmentDisplay.class);
                                intent.putExtra("url", eventBook);
                                startActivity(intent);
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
                break;
        }
    }



    public void showDialog(Context context, int x, int y){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        Utility.freeMemory();
        final Dialog dialog  = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.listview_with_text_image);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout lnrMyAcc = (LinearLayout) dialog.findViewById(R.id.lnrMyAcc);
       /* lnrMyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });*/

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.x = x;
        lp.y = y;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private class HttpAsyncTaskEventRegistration extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(EventDetail.this);
            dialog.setMessage("Fetching event...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("userid",UserID);
                jsonObject.accumulate("event_id",   Pref.getValue(EventDetail.this,"Event_ID",""));
                jsonObject.accumulate("operation","Register");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Utility.POST2(Utility.BASE_URL+"Events/Operation",jsonObject );
        }
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            // Log.e("resulteventreg",""+result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.optString("message");
                Toast.makeText(EventDetail.this,message,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(EventDetail.this);
            dialog.setMessage("Fetching event...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("event_id",   Pref.getValue(EventDetail.this,"Event_ID",""));
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

                    JSONObject eventDetail = response.getJSONObject("EventDetail");

                    EventModel eventModel = new EventModel();
                    eventModel.setEvent_ID(eventDetail.getString("Event_ID"));
                    eventModel.setEvent_Name(eventDetail.getString("Event_Name"));
                    eventModel.setEvent_Type(eventDetail.getString("Event_Type"));
                    eventModel.setEvent_Image(eventDetail.getString("Event_Image"));
                    eventModel.setEvent_Desc(eventDetail.getString("Event_Desc"));
                    eventModel.setEvent_StartDate(eventDetail.getString("Event_StartDate"));
                    eventModel.setEvent_EndDate(eventDetail.getString("Event_EndDate"));
                    eventModel.setCompanyName(eventDetail.getString("CompanyName"));
                    eventModel.setIndustryName(eventDetail.getString("IndustryName"));
                    eventModel.setCity(eventDetail.getString("City"));
                    eventModel.setState(eventDetail.getString("State"));
                    eventModel.setCountry(eventDetail.getString("Country"));
                    eventModel.setPostalCode(eventDetail.getString("PostalCode"));
                    eventModel.setAddress1(eventDetail.getString("Address1"));
                    eventModel.setAddress2(eventDetail.getString("Address2"));
                    eventModel.setAddress3(eventDetail.getString("Address3"));
                    eventModel.setAddress4(eventDetail.getString("Address4"));
                    eventModelArrayList.add(eventModel);

                    if (eventDetail.getString("Event_Book_Stand").equalsIgnoreCase("") ||
                            eventDetail.getString("Event_Book_Stand").equalsIgnoreCase("null") ||
                            eventDetail.getString("Event_Book_Stand").equalsIgnoreCase(null)){
                        activityEventDetailBinding.txtBook.setVisibility(View.GONE);
                    }
                    else {
                        eventBook = eventDetail.getString("Event_Book_Stand").toString();
                        activityEventDetailBinding.txtBook.setVisibility(View.VISIBLE);
                    }

                    if (eventDetail.getString("Event_Registration").equalsIgnoreCase("") ||
                            eventDetail.getString("Event_Registration").equals("null") ||
                            eventDetail.getString("Event_Registration")==null){
                        activityEventDetailBinding.txtRegister.setVisibility(View.GONE);
                    }
                    else {
                        eventRegister = eventDetail.getString("Event_Registration").toString();
                        activityEventDetailBinding.txtRegister.setVisibility(View.VISIBLE);
                    }

                    if(eventDetail.getString("Event_Name").equals("")
                            || eventDetail.getString("Event_Name").equals("null"))
                    {
                        activityEventDetailBinding.tvEventTitle.setVisibility(View.GONE);
                        activityEventDetailBinding.tvEventTitle1.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityEventDetailBinding.tvEventTitle.setText(eventDetail.getString("Event_Name"));
                        activityEventDetailBinding.tvEventTitle1.setText(eventDetail.getString("Event_Name"));
                    }

                    if(eventDetail.getString("Event_Type").equals("")
                            || eventDetail.getString("Event_Type").equals("null")
                            ||eventDetail.getString("Event_Type")==null)
                    {
                        activityEventDetailBinding.tvEventType.setVisibility(View.GONE);
                        activityEventDetailBinding.llOld.setVisibility(View.GONE);
                        activityEventDetailBinding.lnreventReg.setVisibility(View.VISIBLE);

                    }
                    else {
                        if (eventDetail.getString("Event_Type").equalsIgnoreCase("Global")||eventDetail.getString("Event_Type").equalsIgnoreCase("global"))
                        {
                            activityEventDetailBinding.llOld.setVisibility(View.VISIBLE);
                            activityEventDetailBinding.lnreventReg.setVisibility(View.GONE);
                            activityEventDetailBinding.tvEventType.setText("" + eventDetail.getString("Event_Type") + "");

                        }
                         else if (eventDetail.getString("Event_Type").equalsIgnoreCase("Local")||eventDetail.getString("Event_Type").equalsIgnoreCase("local")) {
                            activityEventDetailBinding.tvEventType.setText("" + eventDetail.getString("Event_Type") + "");
                            activityEventDetailBinding.llOld.setVisibility(View.GONE);
                            activityEventDetailBinding.lnreventReg.setVisibility(View.VISIBLE);
                        }
                    }

                    if(eventDetail.getString("Event_Desc").equals("")
                            || eventDetail.getString("Event_Desc").equals("null"))
                    {
                        activityEventDetailBinding.tvEventDesc.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityEventDetailBinding.tvEventDesc.setText(eventDetail.getString("Event_Desc"));
                    }

                    if(eventDetail.getString("CompanyName").equals("")
                            || eventDetail.getString("CompanyName").equals("null"))
                    {
                        activityEventDetailBinding.tvCompanyName.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityEventDetailBinding.tvCompanyName.setText(eventDetail.getString("CompanyName"));
                    }

                    if(eventDetail.getString("IndustryName").equals("")
                            || eventDetail.getString("IndustryName").equals("null"))
                    {
                        activityEventDetailBinding.tvIndustryName.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityEventDetailBinding.tvIndustryName.setText("("+eventDetail.getString("IndustryName")+")");
                    }

                    if(eventDetail.getString("Event_Image").equals("")
                            || eventDetail.getString("Event_Image").equals(null))
                    {
                        activityEventDetailBinding.progressBar1.setVisibility(View.GONE);
                        activityEventDetailBinding.imgEvent.setImageResource(R.drawable.ic_event_default);
                    }
                    else
                    {
                        activityEventDetailBinding.progressBar1.setVisibility(View.VISIBLE);

                        Glide.with(EventDetail.this).load(Utility.BASE_IMAGE_URL+"Events/"+eventDetail.getString("Event_Image"))
                                .asBitmap()
                                .into(new BitmapImageViewTarget(activityEventDetailBinding.imgEvent) {
                                    @Override
                                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                        super.onResourceReady(drawable, anim);
                                        activityEventDetailBinding.progressBar1.setVisibility(View.GONE);
                                        activityEventDetailBinding.imgEvent.setImageBitmap(drawable);
                                    }
                                });

                    }

                    activityEventDetailBinding.tvEventDate.setText(eventDetail.getString("Event_StartDate")
                            +"  to  "+ eventDetail.getString("Event_EndDate"));

                    String address1 = eventDetail.getString("Address1") ;
                    String address2 = eventDetail.getString("Address2") ;
                    String address3 = eventDetail.getString("Address3") ;
                    String address4 = eventDetail.getString("Address4") ;
                    String city = eventDetail.getString("City") ;
                    String state = eventDetail.getString("State") ;
                    String country = eventDetail.getString("Country") ;
                    String postalCode = eventDetail.getString("PostalCode") ;

                    String fullAddress = "";

                    if (address1.equals("") || address1.isEmpty())
                    {

                    } else {
                        address1 = address1+", ";
                    }
                    if (address2.equals("") || address2.isEmpty())
                    {

                    } else {
                        address2 = address2+", ";
                    }
                    if (address3.equals("") || address3.isEmpty())
                    {

                    } else {
                        address3 = address3+", ";
                    }
                    if (address4.equals("") || address4.isEmpty())
                    {

                    } else {
                        address4 = address4+", ";
                    }
                    if (city.equals("") || city.isEmpty())
                    {

                    } else {
                        city = city+", ";
                    }
                    if (state.equals("") || state.isEmpty())
                    {

                    } else {
                        state = state+", ";
                    }
                    if (country.equals("") || country.isEmpty())
                    {

                    } else {
                        country = country;
                    }
                    if (postalCode.equals("") || postalCode.isEmpty())
                    {

                    } else {
                        postalCode = "("+postalCode+")";
                    }

                    activityEventDetailBinding.tvEventAddress.setText(address1+address2+address3+address4+city+state+country+postalCode);


                    try
                    {
                        JSONArray showTiming = response.getJSONArray("showTimings");

                        for(int i = 0; i< showTiming.length() ; i++)
                        {
                            JSONObject tList = showTiming.getJSONObject(i);

                            EventModel eventModel1 = new EventModel();
                            tList.getString("Event_Highlights");
                            eventModel1.setEventDate(tList.getString("Event_Highlight_Date"));
                            eventModel1.setStartDate(tList.getString("Start_Time"));
                            eventModel1.setEndDate(tList.getString("End_Time"));
                            eventModelArrayList1.add(eventModel1);
                        }
                        EventDetailAdapter eventDetailAdapter = new EventDetailAdapter(getApplicationContext(), eventModelArrayList1);
                        activityEventDetailBinding.listViewTimeShow.setAdapter(eventDetailAdapter);
                        eventDetailAdapter.notifyDataSetChanged();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        activityEventDetailBinding.llShowTime.setVisibility(View.GONE);
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

