package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.ImageAdAdapter;
import com.circle8.circleOne.Adapter.MerchantAddressAdapter;
import com.circle8.circleOne.Adapter.MerchantProductAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.MerchantLocationModel;
import com.circle8.circleOne.Model.MerchantProductModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityMerchantDetailBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class MerchantDetailActivity extends Fragment implements OnMapReadyCallback
{
    CircleImageView tvMerchantImg;
    TextView tvMerchantName, tvMerchantDesc, tvMoreInfo ;
    ImageView imgBack ;
    ExpandableHeightListView listView1, listView2 ;
    ImageAdAdapter imageAdAdapter ;
    ArrayList<Integer> adImages = new ArrayList<>();
    MerchantAddressAdapter merchantAddressAdapter ;
    MerchantProductAdapter merchantProductAdapter ;
    ArrayList<String> storeNameList = new ArrayList<>();
    ArrayList<String> storeAddressList = new ArrayList<>();
    ArrayList<String> storeTimeList = new ArrayList<>();
    ArrayList<MerchantLocationModel> merchantLocationModelArrayList = new ArrayList<>();
    ArrayList<MerchantProductModel> merchantProductModelArrayList = new ArrayList<>();
    GoogleMap googleMaps ;
    String storeAddress = "" ;
    LoginSession loginSession ;
    public static String userId = "", merchantId = "";
    private TextView tvProductListInfo, tvLocationListInfo ;
    private String websiteURL = "";
    View view;
    ActivityMerchantDetailBinding fragmentProfileBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(
                inflater, R.layout.activity_merchant_detail, container, false);
        view = fragmentProfileBinding.getRoot();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginSession = new LoginSession(getActivity());
        HashMap<String, String> user = loginSession.getUserDetails();
        userId = user.get(LoginSession.KEY_USERID);

        listView1 = (ExpandableHeightListView)view.findViewById(R.id.listView1);
        listView2 = (ExpandableHeightListView)view.findViewById(R.id.listView2);

        tvMoreInfo = (TextView)view.findViewById(R.id.tvMoreInfo);
        tvMerchantName = (TextView)view.findViewById(R.id.tvMerchantName);
        tvMerchantDesc = (TextView)view.findViewById(R.id.tvMerchantDesc);
        tvMerchantImg = (CircleImageView)view.findViewById(R.id.tvMerchantImg);
        imgBack = (ImageView)view.findViewById(R.id.imgBack);

        tvProductListInfo = (TextView)view.findViewById(R.id.tvProductListInfo);
        tvLocationListInfo = (TextView)view.findViewById(R.id.tvLocationListInfo);

        // for ads images
       /* adImages.add(R.drawable.cold_coco);
        adImages.add(R.drawable.cold_coco1);
        adImages.add(R.drawable.cold_coco2);

        imageAdAdapter = new ImageAdAdapter(MerchantDetailActivity.this, adImages);
        listView1.setAdapter(imageAdAdapter);
        imageAdAdapter.notifyDataSetChanged();*/

        // for store location
        /*
        storeNameList.add("100 AM");
        storeAddressList.add("100 Tras Street 01, 100 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");
        storeNameList.add("200 AM");
        storeAddressList.add("200 Tras Street 01-02, 200 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");
        storeNameList.add("300 AM");
        storeAddressList.add("300 Tras Street 01-03, 300 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");
        storeNameList.add("400 AM");
        storeAddressList.add("400 Tras Street 01-04, 400 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");
        storeNameList.add("500 AM");
        storeAddressList.add("500 Tras Street 01-05, 500 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");
        storeNameList.add("600 AM");
        storeAddressList.add("600 Tras Street 01-06, 600 Am Singapore 079027");
        storeTimeList.add("Open 07.00am to 07.00 pm");

        merchantAddressAdapter = new MerchantAddressAdapter(MerchantDetailActivity.this, storeNameList, storeAddressList,storeTimeList);
        listView2.setAdapter(merchantAddressAdapter);
        merchantAddressAdapter.notifyDataSetChanged();*/

        // SupportMapFragment mapFragment = (SupportMapFragment)getActivity().ge().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        new HttpAsyncGetDetails().execute(Utility.BASE_URL+"Merchant/GetDetails");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        tvMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity(), R.style.Blue_AlertDialog);

                builder.setTitle("CircleOne")
                        .setMessage("Are you sure you want to exit to "+websiteURL)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(getActivity(), AttachmentDisplay.class);
                                intent.putExtra("url", websiteURL);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_set_as)
                        .show();
            }
        });
        return view;
    }



    private class GeocoderHandler extends Handler
    {
        @Override
        public void handleMessage(Message message)
        {
            String locationAddress;
            String latLang ;
            String msg ;
            switch (message.what)
            {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    latLang = bundle.getString("latlang");

                    msg = bundle.getString("message");
                    if (msg.equalsIgnoreCase("Get Latitude and Longitude for this address location."))
                    {
                        Double Latitude = bundle.getDouble("latitude");
                        Double Longitude = bundle.getDouble("longitude");
                      //  boolean  = Utility.checkLocationPermission(getActivity());
                        //if(result) {
                            createMarker(Latitude, Longitude);
                       // }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    locationAddress = null;
                    latLang = null ;
            }
//            tvLatLang.setText(latLang+" of "+locationAddress);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        googleMaps = googleMap ;

       /* Circle circle = googleMaps.addCircle(new CircleOptions()
                .center(googleMaps.setMyLocationEnabled(true))
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));*/

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        boolean result = Utility.checkLocationPermission(getActivity());
        if(result) {
            googleMaps.setMyLocationEnabled(true);
            googleMaps.animateCamera(zoom);
        }
       /* LatLng ttc = new LatLng(23.012688,72.522777);
        googleMaps.addMarker(new MarkerOptions().position(ttc).title("Titanium City Center"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(ttc));

        LatLng seemaHall = new LatLng(23.013258,72.520127);
        googleMaps.addMarker(new MarkerOptions().position(seemaHall).title("seemaHall"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(seemaHall));

        LatLng sachinTawer = new LatLng(23.013021,72.524633);
        googleMaps.addMarker(new MarkerOptions().position(sachinTawer).title("sachinTawer"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(sachinTawer));

        LatLng panchtirthPlaza = new LatLng(23.011353,72.522857);
        googleMaps.addMarker(new MarkerOptions().position(panchtirthPlaza).title("panchtirthPlaza"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(panchtirthPlaza));*/

       /* LatLng ahmedabad = new LatLng(23.0225, 72.5714);
        LatLng surat = new LatLng(21.1702, 72.8311);
        LatLng vadodra = new LatLng(22.3072, 73.1812);
        LatLng jaipur = new LatLng(26.9124, 75.7873);
        LatLng ajmer = new LatLng(26.4499, 74.6399);
        LatLng pali = new LatLng(25.7711, 73.3234);
        LatLng sumerpur = new LatLng(25.1526, 73.0823);*/

       /* googleMap.addMarker(new MarkerOptions().position(ahmedabad).title("Ahmedabad"));
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sumerpur));*/
    }

    private void createMarker(Double Lat, Double Lang)
    {
        LatLng location = new LatLng(Lat,Lang);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        googleMaps.addMarker(new MarkerOptions().position(location).title(storeAddress));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(location,12));

//        googleMaps.animateCamera(zoom);
    }

    private class HttpAsyncGetDetails extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(MerchantDetailActivity.this);
            dialog.setMessage("Get Details...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get merchant detail" ;
            CustomProgressDialog(loading, getActivity());
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("merchantid", merchantId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String merchant_id = jsonObject.getString("merchant_id");
                    String merchant_name = jsonObject.getString("merchant_name");
                    String merchant_desc = jsonObject.getString("merchant_desc");
                    String merchant_website = jsonObject.getString("merchant_website");
                    String merchant_image = jsonObject.getString("MerchantImage");

                    websiteURL = merchant_website ;

                    if (websiteURL.equals(""))
                    {
                        tvMoreInfo.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvMoreInfo.setVisibility(View.VISIBLE);
                    }

                    if (merchant_image.equals(""))
                    {
                        tvMerchantImg.setVisibility(View.GONE);
                    }
                    else
                    {
                        Picasso.with(getActivity()).load(Utility.BASE_IMAGE_URL+"Merchant/"+merchant_image).resize(300,300).onlyScaleDown().skipMemoryCache().into(tvMerchantImg);
                    }

                    if (merchant_name.equalsIgnoreCase("null") || merchant_name.equalsIgnoreCase(""))
                    {
                        //tvMerchantName.setText(merchant_name);
                    }
                    else {
                        tvMerchantName.setText(merchant_name);
                    }
                    if (merchant_desc.equalsIgnoreCase("") || merchant_desc.equalsIgnoreCase("null"))
                    {

                    }
                    else
                    {
                        tvMerchantDesc.setText(merchant_desc);
                    }

                    try
                    {
                        JSONArray productsArray = jsonObject.getJSONArray("Products");
                        if (productsArray.length() != 0)
                        {
                            tvProductListInfo.setVisibility(View.GONE);

                            for (int i = 0 ; i < productsArray.length(); i++)
                            {
                                JSONObject productListObj = productsArray.getJSONObject(i);

                                String ProductCategoryID = productListObj.getString("ProductCategoryID");
                                String ProductCategoryName = productListObj.getString("ProductCategoryName");
                                String ProductID = productListObj.getString("ProductID");
                                String ProductName = productListObj.getString("ProductName");
                                String ProductDesc = productListObj.getString("ProductDesc");
                                String Offer = productListObj.getString("Offer");
                                String ProductCost = productListObj.getString("ProductCost");
                                String ProductImage = productListObj.getString("ProductImage");
                                String MerchantImage = productListObj.getString("MerchantImage");
                                String Merchant_ID = productListObj.getString("Merchant_ID");
                                String Merchant_Name = productListObj.getString("Merchant_Name");
                                String Merchant_Desc = productListObj.getString("Merchant_Desc");

                                MerchantProductModel merchantProductModel = new MerchantProductModel();
                                merchantProductModel.setProductCategoryId(ProductCategoryID);
                                merchantProductModel.setProductCategoryName(ProductCategoryName);
                                merchantProductModel.setProductId(ProductID);
                                merchantProductModel.setProductName(ProductName);
                                merchantProductModel.setProductDesc(ProductDesc);
                                merchantProductModel.setProductOffer(Offer);
                                merchantProductModel.setProductCost(ProductCost);
                                merchantProductModel.setProductImage(ProductImage);
                                merchantProductModel.setMerchantImage(MerchantImage);
                                merchantProductModel.setMerchantId(Merchant_ID);
                                merchantProductModel.setMerchantName(Merchant_Name);
                                merchantProductModel.setMerchantDesc(Merchant_Desc);
                                merchantProductModelArrayList.add(merchantProductModel);

                                merchantProductAdapter = new MerchantProductAdapter(getActivity(), merchantProductModelArrayList);
                                listView1.setAdapter(merchantProductAdapter);
                                merchantProductAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            tvProductListInfo.setVisibility(View.VISIBLE);
                            //                        Toast.makeText(getApplicationContext(), "No Product_List Avail", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        tvProductListInfo.setVisibility(View.VISIBLE);
                    }

                    try
                    {
                        JSONArray locationsArray = jsonObject.getJSONArray("Locations");
                        if (locationsArray.length() != 0)
                        {
                            tvLocationListInfo.setVisibility(View.GONE);

                            for (int i = 0 ; i < locationsArray.length(); i++)
                            {
                                JSONObject locationsObj = locationsArray.getJSONObject(i);

                                String Branch_Name = locationsObj.getString("Branch_Name");
                                String Addr1 = locationsObj.getString("Addr1");
                                String Addr2 = locationsObj.getString("Addr2");
                                String Addr3 = locationsObj.getString("Addr3");
                                String Addr4 = locationsObj.getString("Addr4");
                                String City = locationsObj.getString("City");
                                String State = locationsObj.getString("State");
                                String Country = locationsObj.getString("Country");
                                String PostalCode = locationsObj.getString("PostalCode");
                                String Latitude = locationsObj.getString("Latitude");
                                String Longitude = locationsObj.getString("Longitude");
                                String Open_Time = locationsObj.getString("Open_Time");
                                String Close_Time = locationsObj.getString("Close_Time");
                                String Open_24Hours_Flag = locationsObj.getString("Open_24Hours_Flag");

                                MerchantLocationModel merchantLocationModel = new MerchantLocationModel();
                                merchantLocationModel.setStoreName(Branch_Name);
                                merchantLocationModel.setAddress1(Addr1);
                                merchantLocationModel.setAddress2(Addr2);
                                merchantLocationModel.setAddress3(Addr3);
                                merchantLocationModel.setAddress4(Addr4);
                                merchantLocationModel.setCity(City);
                                merchantLocationModel.setState(State);
                                merchantLocationModel.setCountry(Country);
                                merchantLocationModel.setPostalCode(PostalCode);
                                merchantLocationModel.setLatitude(Latitude);
                                merchantLocationModel.setLongitude(Longitude);
                                merchantLocationModel.setOpenTime(Open_Time);
                                merchantLocationModel.setCloseTime(Close_Time);
                                merchantLocationModel.setHourFlag(Open_24Hours_Flag);

                                if (!Addr1.equals("") || !Addr1.isEmpty())
                                {
                                    Addr1 = Addr1+", ";
                                }
                                if (!Addr2.equals("") || !Addr2.isEmpty())
                                {
                                    Addr2 = Addr2+", ";
                                }
                                if (!Addr3.equals("") || !Addr3.isEmpty())
                                {
                                    Addr3 = Addr3+", ";
                                }
                                if (!Addr4.equals("") || !Addr4.isEmpty())
                                {
                                    Addr4 = Addr4+", ";
                                }
                                if (!City.equals("") || !City.isEmpty())
                                {
                                    City = City+", ";
                                }
                                if (!State.equals("") || !State.isEmpty())
                                {
                                    State = State+", ";
                                }
                                if (!Country.equals("") || !Country.isEmpty())
                                {
                                    Country = Country+", ";
                                }
                                if (!PostalCode.equals("") || !PostalCode.isEmpty())
                                {
                                    PostalCode = "("+PostalCode+")";
                                }

                                String fullAddress = Addr1+Addr2+Addr3+Addr4+City+State+Country+PostalCode;

                                merchantLocationModel.setFullAddress(fullAddress);
                                merchantLocationModelArrayList.add(merchantLocationModel);

                                merchantAddressAdapter = new MerchantAddressAdapter(getActivity(), merchantLocationModelArrayList);
                                listView2.setAdapter(merchantAddressAdapter);
                                merchantAddressAdapter.notifyDataSetChanged();

                                String markerAddress = Addr1+","+Addr2+","+City+","+State+","+Country+" "+PostalCode;

                                Double lat = null, lang = null;
                                if (!Latitude.equals("") && !Longitude.equals(""))
                                {
                                    lat = Double.valueOf(Latitude);
                                    lang = Double.valueOf(Longitude);
                                 //   boolean result1 = Utility.checkLocationPermission(getActivity());
                                  //  if(result1) {
                                        createMarker(lat, lang);
                                   // }
                                }

//                                GeocodingLocation locationAddress = new GeocodingLocation();
//                                locationAddress.getAddressFromLocation(fullAddress, getApplicationContext(), new GeocoderHandler());
                            }
                        }
                        else
                        {
                            tvLocationListInfo.setVisibility(View.VISIBLE);
                            //                        Toast.makeText(getApplicationContext(), "No Locations Avail", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        tvLocationListInfo.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    // Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
