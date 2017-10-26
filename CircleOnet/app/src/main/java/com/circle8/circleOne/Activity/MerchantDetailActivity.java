package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.circle8.circleOne.Adapter.ImageAdAdapter;
import com.circle8.circleOne.Adapter.MerchantAddressAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;

import java.util.ArrayList;

public class MerchantDetailActivity extends AppCompatActivity
{
    ExpandableHeightListView listView1, listView2 ;

    ImageAdAdapter imageAdAdapter ;
    ArrayList<Integer> adImages = new ArrayList<>();

    MerchantAddressAdapter merchantAddressAdapter ;
    ArrayList<String> storeList = new ArrayList<>();
    ArrayList<String> addressList = new ArrayList<>();

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

        storeList.add("100 AM");
        addressList.add("100 Tras Street 01, 100 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");
        storeList.add("200 AM");
        addressList.add("200 Tras Street 01-02, 200 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");
        storeList.add("300 AM");
        addressList.add("300 Tras Street 01-03, 300 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");
        storeList.add("400 AM");
        addressList.add("400 Tras Street 01-04, 400 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");
        storeList.add("500 AM");
        addressList.add("500 Tras Street 01-05, 500 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");
        storeList.add("600 AM");
        addressList.add("600 Tras Street 01-06, 600 Am Singapore 079027 \nOpen 07.00am to 07.00 pm");

        merchantAddressAdapter = new MerchantAddressAdapter(MerchantDetailActivity.this, storeList, addressList);
        listView2.setAdapter(merchantAddressAdapter);
        merchantAddressAdapter.notifyDataSetChanged();


    }
}
