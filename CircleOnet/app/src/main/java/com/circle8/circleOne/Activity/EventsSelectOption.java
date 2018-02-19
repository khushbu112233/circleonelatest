package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityEventsSelectOptionBinding;

public class EventsSelectOption extends AppCompatActivity
{

    private TextView actionText;
    private ImageView imgDrawer, imgLogo;
    LinearLayout lnrDate;
    AutoCompleteTextView searchText ;
    public static String searchKeyWord = "";
    public static String searchKeyWord1 = "";
    public static String searchBy = "";
    public static String searchOpt = "AllEvents";
    public static String eventOpt = "AllEvents";
    ActivityEventsSelectOptionBinding activityEventsSelectOptionBinding;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityEventsSelectOptionBinding = DataBindingUtil.setContentView(this,R.layout.activity_events_select_option);
        Utility.freeMemory();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_2);
        activity = EventsSelectOption.this;
        actionText = (TextView) findViewById(R.id.mytext);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setVisibility(View.VISIBLE);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);

        lnrDate = (LinearLayout) findViewById(R.id.lnrDate);
        searchText = (AutoCompleteTextView)findViewById(R.id.searchView);

        actionText.setText("Events");

        lnrDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchOpt = "Date";
                searchBy = "Date" ;

                Intent go = new Intent(getApplicationContext(),CustomDate.class);
                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly

                startActivity(go);
                Utility.freeMemory();
            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();*/

                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                Utility.freeMemory();
            }
        });

    }

    public static void kill()
    {
        activity.finish();
    }


    public void allEvents(View v)
    {
       // CardsActivity.setActionBarTitle("Events");
        searchOpt = "AllEvents";
        searchKeyWord = "";
//        EventsActivity.callSecond();
        EventsActivity.callFirst();
        finish();
//        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }

    public void byCompanyAssociation(View v)
    {
        if (searchText.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please type some keyword",Toast.LENGTH_SHORT).show();
        }
        else
        {
           // CardsActivity.setActionBarTitle("Events");

            searchOpt = "CompanyAssociation";

            searchKeyWord = searchText.getText().toString() ;
            searchBy = "Company";

//            EventsActivity.searchEvent();

            new EventsActivity.HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");

            finish();
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

    }

    public void byIndustry(View v)
    {
        if (searchText.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please type some keyword",Toast.LENGTH_SHORT).show();
        }
        else
        {
          //  CardsActivity.setActionBarTitle("Events");

            searchOpt = "Industry";

            searchKeyWord = searchText.getText().toString() ;
            searchBy = "Industry";

//            EventsActivity.searchEvent();

            new EventsActivity.HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");

            finish();
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


        }
    }

}
