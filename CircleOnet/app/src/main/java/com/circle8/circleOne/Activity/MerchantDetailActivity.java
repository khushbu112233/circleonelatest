package com.circle8.circleOne.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.circle8.circleOne.Adapter.ImageAdAdapter;
import com.circle8.circleOne.Adapter.MerchantAddressAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MerchantDetailActivity extends FragmentActivity implements OnMapReadyCallback
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


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        LatLng ahmedabad = new LatLng(23.0225, 72.5714);
        LatLng surat = new LatLng(21.1702, 72.8311);
        LatLng vadodra = new LatLng(22.3072, 73.1812);
        LatLng jaipur = new LatLng(26.9124, 75.7873);
        LatLng ajmer = new LatLng(26.4499, 74.6399);
        LatLng pali = new LatLng(25.7711, 73.3234);
        LatLng sumerpur = new LatLng(25.1526, 73.0823);

        googleMap.addMarker(new MarkerOptions().position(ahmedabad).title("Ahmedabad"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ahmedabad));
        googleMap.addMarker(new MarkerOptions().position(surat).title("Surat"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(surat));
        googleMap.addMarker(new MarkerOptions().position(vadodra).title("Vadodra"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(vadodra));
        googleMap.addMarker(new MarkerOptions().position(jaipur).title("Jaipur"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(jaipur));
        googleMap.addMarker(new MarkerOptions().position(ajmer).title("Ajmer"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ajmer));
        googleMap.addMarker(new MarkerOptions().position(pali).title("Pali"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pali));
        googleMap.addMarker(new MarkerOptions().position(sumerpur).title("Sumerpur"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sumerpur));

    }
}
