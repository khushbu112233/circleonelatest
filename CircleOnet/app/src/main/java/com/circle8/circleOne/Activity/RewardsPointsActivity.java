package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.ExpandableListAdapter1;
import com.circle8.circleOne.Model.ListAdapter1;
import com.circle8.circleOne.Model.ListCell;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RewardsPointsActivity extends AppCompatActivity implements View.OnClickListener
{
    private LinearLayout llEarnPointBox, llMerchantBox ;
    private TextView tvRewardName, tvRewardType, tvRewardPoint, tvHistory;
    private View MerchantView, RewardView, HistoryView , HistoryListView ;
    private ImageView ivCirclePlus, ivHouse ;
    private TextView tvPoints, tvMerchant ;

    private ListView listView ;

    private ExpandableListView expListView;
    private List<String> groupList;
    private List<String> childList;
    private Map<String, List<String>> laptopCollection;

    static TextView textView;
    static ImageView imgDrawer, imgBack, ivAdImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_points);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvHistory = (TextView)findViewById(R.id.tvHistory);
        llEarnPointBox = (LinearLayout)findViewById(R.id.llEarnPointBox);
        llMerchantBox = (LinearLayout)findViewById(R.id.llMerchantBox);
        ivCirclePlus = (ImageView)findViewById(R.id.ivCirclePlus);
        ivHouse = (ImageView)findViewById(R.id.ivHouse);
        tvPoints = (TextView)findViewById(R.id.tvPoints);
        tvMerchant = (TextView)findViewById(R.id.tvMerchant);
        imgBack = (ImageView)findViewById(R.id.imgBack);

        init();
        init1();

        llEarnPointBox.setOnClickListener(this);
        llMerchantBox.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        new HttpAsyncGetProductCategory().execute(Utility.MERCHANT_BASE_URL+"GetProductCategory");                 //get
        new HttpAsyncGetProduct().execute(Utility.MERCHANT_BASE_URL+"GetProducts");                               //get
        new HttpAsyncGetProductByCategory().execute(Utility.MERCHANT_BASE_URL+"GetProductsByCategory");           // post
        new HttpAsyncGetBalance().execute(Utility.REWARDS_BASE_URL+"GetBalance");                                 // post
        new HttpAsyncGetHistoryEarnedPoints().execute(Utility.REWARDS_BASE_URL+"History_EarnedPoints");           // post
        new HttpAsyncGetHistoryReedemedPoints().execute(Utility.REWARDS_BASE_URL+"History_ReedemedPoints");           // post
    }

    private void init()
    {
        HistoryListView = findViewById(R.id.icdHistoryListviewLayout);
        HistoryView = findViewById(R.id.icdRewardHistoryLayout);
        MerchantView = findViewById(R.id.icdMerchantLayout);
        RewardView = findViewById(R.id.icdEarnPointLayout);

        getHistory();

        createGroupList();
        createCollection();

        expListView = (ExpandableListView)MerchantView.findViewById(R.id.laptop_list);
        ivAdImg = (ImageView)MerchantView.findViewById(R.id.ivAdImg);
        final ExpandableListAdapter1 expListAdapter = new ExpandableListAdapter1(this, groupList, laptopCollection);
        expListView.setAdapter(expListAdapter);

        setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id)
            {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
//                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        ivAdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent in = new Intent(RewardsPointsActivity.this, MerchantDetailActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

    private void init1()
    {
        Intent intent = getIntent();
        String status = intent.getStringExtra("OnClick");

        status = "CardImage";

        if(status.equalsIgnoreCase("CardImage"))
        {
            HistoryListView.setVisibility(View.VISIBLE);
        }
        else if(status.equalsIgnoreCase("RewardPoint"))
        {
            RewardView.setVisibility(View.VISIBLE);

            ivCirclePlus.setImageResource(R.drawable.ic_circle_plus);
            tvPoints.setTextColor(getResources().getColor(R.color.white));

            ivHouse.setImageResource(R.drawable.ic_house_blue);
            tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(status.equalsIgnoreCase("Merchant"))
        {
            MerchantView.setVisibility(View.VISIBLE);

            ivHouse.setImageResource(R.drawable.ic_house);
            tvMerchant.setTextColor(getResources().getColor(R.color.white));

            ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    private void getHistory()
    {
        ArrayList<ListCell> items = new ArrayList<ListCell>();

        items.add(new ListCell("AUG 2017", "07th", "Top up","+5000"));
        items.add(new ListCell("AUG 2017", "06th", "Suria KLCC","-520"));
        items.add(new ListCell("AUG 2017", "03rd", "Starhill Gallery Kuala Lumper","+100"));

        items.add(new ListCell("JUL 2017", "31st", "7-Eleven Malaysia", "+10"));
        items.add(new ListCell("JUL 2017", "28th", "Berjaya Times Square", "-300"));
        items.add(new ListCell("JUL 2017", "22th", "Sungai Wang Plaza", "+100"));
        items.add(new ListCell("JUL 2017", "20th", "7-Eleven Malaysia", "+5"));
        items.add(new ListCell("JUL 2017", "18th", "Berjaya Times Square", "+10"));
        items.add(new ListCell("JUL 2017", "15th", "Sungai Wang Plaza", "+20"));

        listView = (ListView)HistoryListView.findViewById(R.id.awesome_list);
        items = sortAndAddSections(items);

        ListAdapter1 adapter = new ListAdapter1(this, items);
        listView.setAdapter(adapter);
    }

    private ArrayList<ListCell> sortAndAddSections(ArrayList<ListCell> itemList)
    {

        ArrayList<ListCell> tempList = new ArrayList<ListCell>();
        //First we sort the array
        Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for(int i = 0; i < itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
           /* if(header != itemList.get(i).getCategory())
            {
                ListCell sectionCell = new ListCell(itemList.get(i).getCategory(), null);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getCategory();
            }
            tempList.add(itemList.get(i));*/

            if(header != itemList.get(i).getMonth())
            {
                ListCell sectionCell = new ListCell(itemList.get(i).getMonth(), null);
                sectionCell.setMonthHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getMonth();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
    }

    private void createGroupList()
    {
        groupList = new ArrayList<String>();
        groupList.add("Food & Beverage");
        groupList.add("Shopping & Lifestyle");
        groupList.add("Travel & Leisure");
        groupList.add("IT & Telco");
        groupList.add("Automotive");
    }

    private void createCollection()
    {
        // preparing laptops collection(child)
        String[] hpModels = {"Pizza", "Burger", "Sandwich" ,"Sprite"};
        String[] hclModels = {"Denim", "Shirt", "Jeans", "Capri" };
        String[] lenovoModels = {"Gujarat", "Rajasthan", "Karanataka", "Maharastra" };
        String[] sonyModels = { "Ample Arch", "India NIC", "Brain Hidden", "Silver Touch" };
        String[] dellModels = { "Budget", "Caltex*+", "ComfortDelGro Rent-A-Car", "SGDrivers" };

        laptopCollection = new LinkedHashMap<String, List<String>>();

        for (String laptop : groupList)
        {
            if (laptop.equals("Food & Beverage"))
            {
                loadChild(hpModels);
            }
            else if (laptop.equals("Shopping & Lifestyle"))
            {
                loadChild(hclModels);
            }
            else if (laptop.equals("Travel & Leisure"))
            {
                loadChild(lenovoModels);
            }
            else if (laptop.equals("IT & Telco"))
            {
                loadChild(sonyModels);
            }
            else if (laptop.equals("Automotive"))
            {
                loadChild(dellModels);
            }
            laptopCollection.put(laptop, childList);
        }
    }

    private void loadChild(String[] laptopModels)
    {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
        {
            childList.add(model);
        }
    }

    private void setGroupIndicatorToRight()
    {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(50), width - getDipsFromPixel(5));
//        expListView.setGroupIndicator(getResources().getDrawable(R.drawable.ic_down_arrow));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.8f);
    }

    @Override
    public void onClick(View v)
    {
        if( v == llEarnPointBox)
        {
            HistoryView.setVisibility(View.GONE);
            HistoryListView.setVisibility(View.GONE);
            MerchantView.setVisibility(View.GONE);
            RewardView.setVisibility(View.VISIBLE);

            ivCirclePlus.setImageResource(R.drawable.ic_circle_plus);
            ivHouse.setImageResource(R.drawable.ic_house_blue);

            tvPoints.setTextColor(getResources().getColor(R.color.white));
            tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if( v == llMerchantBox)
        {
            HistoryView.setVisibility(View.GONE);
            HistoryListView.setVisibility(View.GONE);
            MerchantView.setVisibility(View.VISIBLE);
            RewardView.setVisibility(View.GONE);

            ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            ivHouse.setImageResource(R.drawable.ic_house);

            tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMerchant.setTextColor(getResources().getColor(R.color.white));
        }
        if( v == tvHistory)
        {
            HistoryView.setVisibility(View.GONE);
            HistoryListView.setVisibility(View.VISIBLE);
            MerchantView.setVisibility(View.GONE);
            RewardView.setVisibility(View.GONE);

            ivCirclePlus.setImageResource(R.drawable.ic_circle_plus);
            ivHouse.setImageResource(R.drawable.ic_house);

            tvPoints.setTextColor(getResources().getColor(R.color.white));
            tvMerchant.setTextColor(getResources().getColor(R.color.white));
        }
        if ( v == imgBack)
        {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private class HttpAsyncGetProductCategory extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product Category..");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProductCategory(urls[0]);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("ProductCategory_List");

                    if (jsonArray.length() != 0)
                    {
                        for (int i = 0 ; i<= jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

                            String ProductCategoryID = productListObj.getString("ProductCategoryID");
                            String ProductCategoryName = productListObj.getString("ProductCategoryName");
                            String ProductCategoryDesc = productListObj.getString("ProductCategoryDesc");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No ProductCategory_List Avail", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
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

    public String PostGetProductCategory(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

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

    private class HttpAsyncGetProduct extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product...");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProducts(urls[0]);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("Product_List");
                    if (jsonArray.length() != 0)
                    {
                        for (int i = 0 ; i <= jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

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

    public String PostGetProducts(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpPost = new HttpGet(url);

            // 6. set httpPost Entity
            //   httpPost.setEntity(se);

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

    private class HttpAsyncGetProductByCategory extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product By Category..");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProductsByCategory(urls[0]);
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
                    String ProductCategoryID = jsonObject.getString("ProductCategoryID");

                    JSONArray jsonArray = jsonObject.getJSONArray("ProductsByCategory");
                    if (jsonArray.length() != 0)
                    {
                        for (int i = 0 ; i <= jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

                            String ProductID = productListObj.getString("ProductID");
                            String ProductName = productListObj.getString("ProductName");
                            String ProductDesc = productListObj.getString("ProductDesc");
                            String ProductType = productListObj.getString("ProductType");
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

    public String PostGetProductsByCategory(String url)
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
            jsonObject.accumulate("ProductCategoryID", "1");

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

    private class HttpAsyncGetBalance extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Balance...");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetBalance(urls[0]);
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
                    String userid = jsonObject.getString("userid");
                    String points_earned = jsonObject.getString("points_earned");
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

    public String PostGetBalance(String url)
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
            jsonObject.accumulate("userid", "1");

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

    private class HttpAsyncGetHistoryEarnedPoints extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get history earn points...");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetHistoryEarnedPoints(urls[0]);
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
                    String userid = jsonObject.getString("userid");

                    JSONArray jsonArray = jsonObject.getJSONArray("EarnedPoints_Trans");
                    if (jsonArray.length() != 0)
                    {
                        for (int i = 0 ; i <= jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

                            String Earned_ID = productListObj.getString("Earned_ID");
                            String Points_Earned = productListObj.getString("Points_Earned");
                            String Benefit_Name = productListObj.getString("Benefit_Name");
                            String Benefit_Desc = productListObj.getString("Benefit_Desc");
                            String Benefit_Date = productListObj.getString("Benefit_Date");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No EarnPoints Avail", Toast.LENGTH_LONG).show();
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

    public String PostGetHistoryEarnedPoints(String url)
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
            jsonObject.accumulate("userid", "1");

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

    private class HttpAsyncGetHistoryReedemedPoints extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get history earn points...");
            dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Get Association" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetHistoryReedemedPoints(urls[0]);
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
                    String userid = jsonObject.getString("userid");

                    JSONArray jsonArray = jsonObject.getJSONArray("ReedemedPoints_Trans");
                    if (jsonArray.length() != 0)
                    {
                        for (int i = 0 ; i <= jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

                            String ProfileID = productListObj.getString("ProfileID");
                            String ProductID = productListObj.getString("ProductID");
                            String ProductName = productListObj.getString("ProductName");
                            String ProductDesc = productListObj.getString("ProductDesc");
                            String Offer = productListObj.getString("Offer");
                            String ProductImage = productListObj.getString("ProductImage");
                            String Points_Used = productListObj.getString("Points_Used");
                            String Quantity = productListObj.getString("Quantity");
                            String Trans_Desc = productListObj.getString("Trans_Desc");
                            String Trans_Date = productListObj.getString("Trans_Date");
                            String Merchant_BranchID = productListObj.getString("Merchant_BranchID");
                            String Merchant_BranchName = productListObj.getString("Merchant_BranchName");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Redeem Points Avail", Toast.LENGTH_LONG).show();
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

    public String PostGetHistoryReedemedPoints(String url)
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
            jsonObject.accumulate("userid", "1");

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


}
