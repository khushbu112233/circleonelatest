package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.circle8.circleOne.Adapter.ImageAdAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;

import java.util.ArrayList;

public class MerchantDetailActivity extends AppCompatActivity
{
    ExpandableHeightListView listView1, listView2 ;

    ImageAdAdapter imageAdAdapter ;
    ArrayList<Integer> adImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);

        listView1 = (ExpandableHeightListView)findViewById(R.id.listView1);
        listView2 = (ExpandableHeightListView)findViewById(R.id.listView2);

        adImages.add(R.drawable.cold_coco);
        adImages.add(R.drawable.cold_coco1);
        adImages.add(R.drawable.cold_coco2);
        adImages.add(R.drawable.cold_coco);


        imageAdAdapter = new ImageAdAdapter(MerchantDetailActivity.this, adImages);
        listView1.setAdapter(imageAdAdapter);
        imageAdAdapter.notifyDataSetChanged();

    }
}
