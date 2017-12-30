package com.circle8.circleOne.Activity;

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

import com.circle8.circleOne.Fragments.EventsFragment;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityEventsSelectOptionBinding;

public class EventsSelectOption extends AppCompatActivity
{

    private TextView actionText;
    private ImageView imgDrawer, imgCards, imgConnect, imgEvents, imgProfile;
    LinearLayout lnrDate;
    AutoCompleteTextView searchText ;
    public static String searchKeyWord = "";
    public static String searchKeyWord1 = "";
    public static String searchBy = "";
    public static String searchOpt = "AllEvents";
    public static String eventOpt = "AllEvents";
    ActivityEventsSelectOptionBinding activityEventsSelectOptionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityEventsSelectOptionBinding = DataBindingUtil.setContentView(this,R.layout.activity_events_select_option);
        Utility.freeMemory();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        actionText = (TextView) findViewById(R.id.mytext);
        imgDrawer = (ImageView) findViewById(R.id.drawer);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
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

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utility.freeMemory();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utility.freeMemory();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Utility.freeMemory();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utility.freeMemory();
            }
        });
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

    public void allEvents(View v)
    {
       // CardsActivity.setActionBarTitle("Events");
        searchOpt = "AllEvents";
        searchKeyWord = "";
//        EventsFragment.callSecond();
        EventsFragment.callFirst();
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

//            EventsFragment.searchEvent();

            new EventsFragment.HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");

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

//            EventsFragment.searchEvent();

            new EventsFragment.HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");

            finish();
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


        }
    }

}
