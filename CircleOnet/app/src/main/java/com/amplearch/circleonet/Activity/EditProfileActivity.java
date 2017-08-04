package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.amplearch.circleonet.R;

public class EditProfileActivity extends AppCompatActivity
{
    private GridView gridView ;
    String[] array ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        gridView = (GridView)findViewById(R.id.gridView);

        array = new String[]{"Accommodations","Information","Accounting","Information technology","Advertising",
                "Insurance","Aerospace","Journalism & News","Agriculture & Agribusiness","Legal Services","Air Transportation",
                "Manufacturing","Apparel & Accessories","Media & Broadcasting","Auto","Medical Devices & Supplies","Banking",
                "Motions Picture & Video","Beauty & Cosmetics","Music","Biotechnology","Pharmaceutical","Chemical","Public Administration",
                "Communications","Public Relations","Computer","Publishing","Construction","Rail","Consulting","Real Estate",
                "Consumer Products","Retail","Education","Service","Electronics","Sports","Employment","Technology","Energy",
                "Telecommunications","Entertainment & Recreation","Tourism","Fashion","Transportation","Financial Services",
                "Travel","Fine Arts","Utilities","Food & Beverage","Video Game","Green Technology","Web Services","Health"};

        gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1, array));

    }
}
