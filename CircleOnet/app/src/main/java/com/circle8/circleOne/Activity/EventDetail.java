package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.EventDetailAdapter;
import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class EventDetail extends AppCompatActivity
{
    private TextView actionText;
    private ImageView imgDrawer, imgCards, imgConnect, imgEvents, imgProfile, imgBack, imgEvent;
    private int actionBarHeight;
    private TextView tvEventTitle,tvEventTitle1, tvEventDate, tvEventDesc, tvEventType, tvEventAddress, tvCompanyName, tvIndustryName ;
    private ListView listViewTimeShow ;
    private LinearLayout llShowTime ;

    private ArrayList<EventModel> eventModelArrayList = new ArrayList<>();
    private ArrayList<EventModel> eventModelArrayList1 = new ArrayList<>();
    String event_ID ;

    private ArrayList<String> eventDate = new ArrayList<>();
    private ArrayList<String> startTime = new ArrayList<>();
    private ArrayList<String> endTime = new ArrayList<>();
    TextView txtBook, txtRegister;
    String eventBook = "", eventRegister = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Utility.freeMemory();
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgLogo);
        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Events");
        imgBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        imgDrawer.setVisibility(View.GONE);

        txtBook = (TextView) findViewById(R.id.txtBook);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        tvEventTitle = (TextView)findViewById(R.id.tvEventTitle);
        tvEventTitle1 = (TextView)findViewById(R.id.tvEventTitle1);
        tvEventDate = (TextView)findViewById(R.id.tvEventDate);
        tvEventType = (TextView)findViewById(R.id.tvEventType);
        tvEventDesc = (TextView)findViewById(R.id.tvEventDesc);
        tvEventAddress = (TextView)findViewById(R.id.tvEventAddress);
        tvCompanyName = (TextView)findViewById(R.id.tvCompanyName);
        tvIndustryName = (TextView)findViewById(R.id.tvIndustryName);
        listViewTimeShow = (ListView)findViewById(R.id.listViewTimeShow);
        llShowTime = (LinearLayout)findViewById(R.id.llShowTime);
        imgEvent = (ImageView) findViewById(R.id.imgEvent);
        Intent i = getIntent();
        event_ID = i.getStringExtra("Event_ID");

        new HttpAsyncTask().execute(Utility.BASE_URL+"Events/GetDetails");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
               /* TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }
                showDialog(EventDetail.this, 0, actionBarHeight);*/
               finish();
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),EventsSelectOption.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);
                finish();
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);
                startActivity(go);
                finish();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
            }
        });

        txtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(EventDetail.this, R.style.Blue_AlertDialog);
                builder.setTitle("Circle One")
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
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);
                startActivity(go);
                finish();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);
                startActivity(go);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);
                startActivity(go);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
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
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
            dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
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
                        txtBook.setVisibility(View.GONE);
                    }
                    else {
                        eventBook = eventDetail.getString("Event_Book_Stand").toString();
                        txtBook.setVisibility(View.VISIBLE);
                    }

                    if (eventDetail.getString("Event_Registration").equalsIgnoreCase("") ||
                            eventDetail.getString("Event_Registration").equalsIgnoreCase("null") ||
                            eventDetail.getString("Event_Registration").equalsIgnoreCase(null)){
                        txtRegister.setVisibility(View.GONE);
                    }
                    else {
                        eventRegister = eventDetail.getString("Event_Registration").toString();
                        txtRegister.setVisibility(View.VISIBLE);
                    }

                    if(eventDetail.getString("Event_Name").equals("")
                            || eventDetail.getString("Event_Name").equals("null"))
                    {
                        tvEventTitle.setVisibility(View.GONE);
                        tvEventTitle1.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvEventTitle.setText(eventDetail.getString("Event_Name"));
                        tvEventTitle1.setText(eventDetail.getString("Event_Name"));
                    }

                    if(eventDetail.getString("Event_Type").equals("")
                            || eventDetail.getString("Event_Type").equals("null"))
                    {
                        tvEventType.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvEventType.setText(""+eventDetail.getString("Event_Type")+"");
                    }

                    if(eventDetail.getString("Event_Desc").equals("")
                            || eventDetail.getString("Event_Desc").equals("null"))
                    {
                        tvEventDesc.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvEventDesc.setText(eventDetail.getString("Event_Desc"));
                    }

                    if(eventDetail.getString("CompanyName").equals("")
                            || eventDetail.getString("CompanyName").equals("null"))
                    {
                        tvCompanyName.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvCompanyName.setText(eventDetail.getString("CompanyName"));
                    }

                    if(eventDetail.getString("IndustryName").equals("")
                            || eventDetail.getString("IndustryName").equals("null"))
                    {
                        tvIndustryName.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvIndustryName.setText("("+eventDetail.getString("IndustryName")+")");
                    }

                    if(eventDetail.getString("Event_Image").equals("")
                            || eventDetail.getString("Event_Image").equals(null))
                    {
                        imgEvent.setImageResource(R.drawable.ic_event_default);
                    }
                    else
                    {
                        Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"Events/"+eventDetail.getString("Event_Image")).resize(400,280).onlyScaleDown().skipMemoryCache().into(imgEvent);
                    }

                    tvEventDate.setText(eventDetail.getString("Event_StartDate")
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

                    tvEventAddress.setText(address1+address2+address3+address4+city+state+country+postalCode);

                   /* tvEventAddress.setText(eventDetail.getString("Address1")+", "+eventDetail.getString("Address2")+", "+
                            eventDetail.getString("Address3")+", "+eventDetail.getString("Address4")+", "+
                            eventDetail.getString("City")+", "+eventDetail.getString("State")+", "+
                            eventDetail.getString("Country")+"("+eventDetail.getString("PostalCode")+")");*/

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
                        listViewTimeShow.setAdapter(eventDetailAdapter);
                        eventDetailAdapter.notifyDataSetChanged();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        llShowTime.setVisibility(View.GONE);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    public  String POST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("event_id", event_ID );
            Utility.freeMemory();
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
            if(inputStream != null)
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

