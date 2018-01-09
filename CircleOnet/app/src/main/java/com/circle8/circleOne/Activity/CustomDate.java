package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityCustomDateBinding;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDate extends AppCompatActivity
{
    CustomCalendarView calendarView;
    private TextView actionText, dateSelect;
    private Button btnSearch ;
    private ImageView imgDrawer, imgLogo;
    private int actionBarHeight;
    Date date1, date2;
    int i =0;
    ActivityCustomDateBinding activityCustomDateBinding;
    private String startDate = "", endDate = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityCustomDateBinding  = DataBindingUtil.setContentView(this,R.layout.activity_custom_date);

        Utility.freeMemory();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_2);

        actionText = (TextView) findViewById(R.id.mytext);
        imgDrawer = (ImageView) findViewById(R.id.drawer);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        dateSelect = (TextView) findViewById(R.id.dateSelect);
        actionText.setText("Events");
        imgLogo.setVisibility(View.VISIBLE);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        imgDrawer.setVisibility(View.GONE);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date)
            {
                Utility.freeMemory();
                i++;
                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//                Toast.makeText(CustomDate.this, df.format(date), Toast.LENGTH_SHORT).show();
                if ( i % 2 == 0 ){
                    date2 = date;
                }
                else {
                    date1 = date;
                }

                try
                {
                    if (date1 != null && date2 != null)
                    {
                        long diff = 0;
                        if (date1.getDay() > date2.getDay())
                        {
                            diff = date1.getDay() - date2.getDay(); //result in millis
                        }
                        if (date2.getDay() > date1.getDay())
                        {
                             diff = date2.getDay() - date1.getDay(); //result in millis
                        }
                        // Print what date is today!
                        dateSelect.setText("View events within period : \n" + "From - "+ df.format(date1) + " To - " + df.format(date2) + "\nSelected Days = " + String.valueOf(diff));

                        startDate = df.format(date1);
                        endDate = df.format(date2);

                       // Toast.makeText(getApplicationContext(), String.valueOf(diff), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){

                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
               // Toast.makeText(CustomDate.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

                finish();            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent go = new Intent(getApplicationContext(),EventsSelectOption.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                startActivity(go);*/
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (startDate.equals("") && endDate.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please select start and end date.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    EventsSelectOption.searchKeyWord = startDate ;
                    EventsSelectOption.searchKeyWord1 = endDate ;

//                    EventsFragment.searchEvent();

                    new EventsActivity.HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
                    EventsSelectOption.kill();
                    finish();
//                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    public void showDialog(Context context, int x, int y){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

}
