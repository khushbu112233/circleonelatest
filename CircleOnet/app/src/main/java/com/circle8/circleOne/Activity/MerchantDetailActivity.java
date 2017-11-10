package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.ImageAdAdapter;
import com.circle8.circleOne.Adapter.MerchantAddressAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.ExpandableHeightListView;
import com.circle8.circleOne.Utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        new HttpAsyncGetDetails().execute(Utility.MERCHANT_BASE_URL+"GetDetails");                               // post

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

    private class HttpAsyncGetDetails extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(MerchantDetailActivity.this);
            dialog.setMessage("Get Details...");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetDetails(urls[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            rlProgressDialog.setVisibility(View.GONE);
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

                    JSONArray productsArray = jsonObject.getJSONArray("Products");
                    if (productsArray.length() != 0)
                    {
                        for (int i = 0 ; i <= productsArray.length(); i++)
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
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Product_List Avail", Toast.LENGTH_LONG).show();
                    }

                    JSONArray locationsArray = jsonObject.getJSONArray("Locations");
                    if (locationsArray.length() != 0)
                    {
                        for (int i = 0 ; i <= locationsArray.length(); i++)
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
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Locations Avail", Toast.LENGTH_LONG).show();
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

    public String PostGetDetails(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("merchantid", "1");

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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


}
