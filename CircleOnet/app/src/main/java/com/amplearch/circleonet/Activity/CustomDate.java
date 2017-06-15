package com.amplearch.circleonet.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.R;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDate extends AppCompatActivity {

    CustomCalendarView calendarView;
    private TextView actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        actionText = (TextView) findViewById(R.id.mytext);
        actionText.setText("Events");

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

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
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(CustomDate.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
               // Toast.makeText(CustomDate.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
