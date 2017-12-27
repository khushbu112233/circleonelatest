package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.EarnPointsAdapter;
import com.circle8.circleOne.Adapter.ExpandableListAdapter1;
import com.circle8.circleOne.Adapter.MerchantExpandableAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.EarnPointsModel;
import com.circle8.circleOne.Model.ListAdapter1;
import com.circle8.circleOne.Model.ListCell;
import com.circle8.circleOne.Model.MerchantGetAllModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityRewardsPointsBinding;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class RewardsPointsActivity extends AppCompatActivity implements View.OnClickListener
{
    ExpandableListAdapter1 expListAdapter ;
    private List<String> groupList = new ArrayList<String>();
    private List<String> childList = new ArrayList<String>();
    private Map<String, List<String>> laptopCollection = new LinkedHashMap<String, List<String>>();
    static TextView textView;
    static ImageView imgDrawer, imgBack;
    LoginSession loginSession ;
    String userId = "";
    MerchantGetAllModel merchantGetAllModel ;
    ArrayList<MerchantGetAllModel> merchantGetAllModelArrayList = new ArrayList<>();
    //for new expandable listview
    MerchantExpandableAdapter merchantExpandableAdapter ;
    List<String> categoryList = new ArrayList<>() ;
    List<String> subCategoryList = new ArrayList<>() ;
    HashMap<String, List<String>> categorysData = new HashMap<String, List<String>>();
    //for Earn points
    EarnPointsAdapter earnPointsAdapter ;
    ArrayList<EarnPointsModel> earnPointsModelsList = new ArrayList<>();
    ActivityRewardsPointsBinding activityRewardsPointsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityRewardsPointsBinding = DataBindingUtil.setContentView(this,R.layout.activity_rewards_points);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        userId = user.get(LoginSession.KEY_USERID);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        init();
        init1();

        activityRewardsPointsBinding.llEarnPointBox.setOnClickListener(this);
        activityRewardsPointsBinding.llMerchantBox.setOnClickListener(this);
        activityRewardsPointsBinding.tvHistory.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        new HttpAsyncGetAll().execute(Utility.BASE_URL+"Merchant/GetAll");                          //get
        new HttpAsyncGetHistoryReedemedPoints().execute(Utility.BASE_URL+"Rewards/History_ReedemedPoints");           // post
//        new HttpAsyncGetProductCategory().execute(Utility.MERCHANT_BASE_URL+"GetProductCategory");                 //get
//        new HttpAsyncGetProduct().execute(Utility.MERCHANT_BASE_URL+"GetProducts");                               //get
//        new HttpAsyncGetProductByCategory().execute(Utility.MERCHANT_BASE_URL+"GetProductsByCategory");           // post
        new HttpAsyncGetBalance().execute(Utility.BASE_URL+"Rewards/GetBalance");                                 // post
        new HttpAsyncGetHistoryEarnedPoints().execute(Utility.BASE_URL+"Rewards/History_EarnedPoints");           // post

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }

    private void init()
    {

//        getHistory();
//        createGroupList();
//        createCollection();


        setGroupIndicatorToRight();

        activityRewardsPointsBinding.icdMerchantLayout.laptopList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id)
            {
                final String selected = (String) merchantExpandableAdapter.getChild(groupPosition, childPosition);

                String mID = merchantGetAllModelArrayList.get(groupPosition).getMerchantIdList().get(childPosition);
//                Toast.makeText(getBaseContext(), selected+" "+mID, Toast.LENGTH_LONG).show();

                Intent iPut = new Intent(RewardsPointsActivity.this, MerchantDetailActivity.class);
                iPut.putExtra("MerchantID", mID);
                startActivity(iPut);
                finish();

                return true;
            }
        });

        activityRewardsPointsBinding.icdMerchantLayout.ivAdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent iPut = new Intent(RewardsPointsActivity.this, MerchantDetailActivity.class);
                iPut.putExtra("MerchantID", "22");
                startActivity(iPut);
                finish();
            }
        });

    }

    private void init1()
    {
        Intent intent = getIntent();
        String status = intent.getStringExtra("OnClick");

        status = "Merchant";

        if(status.equalsIgnoreCase("CardImage"))
        {
            activityRewardsPointsBinding.icdHistoryListviewLayout.relHistory.setVisibility(View.VISIBLE);
            activityRewardsPointsBinding.tvHistory.setAlpha((float) 1.0);
            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house_blue);
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(status.equalsIgnoreCase("RewardPoint"))
        {
            activityRewardsPointsBinding.icdEarnPointLayout.relEarn.setVisibility(View.VISIBLE);
            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus);
            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.white));
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house_blue);
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityRewardsPointsBinding.tvHistory.setAlpha((float) 0.5);
        }
        else if(status.equalsIgnoreCase("Merchant"))
        {
            activityRewardsPointsBinding.icdMerchantLayout.llMerchant.setVisibility(View.VISIBLE);
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house);
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.white));
            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityRewardsPointsBinding.tvHistory.setAlpha((float) 0.5);
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

        items = sortAndAddSections(items);

        ListAdapter1 adapter = new ListAdapter1(this, items);
        activityRewardsPointsBinding.icdHistoryListviewLayout.awesomeList.setAdapter(adapter);
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

        activityRewardsPointsBinding.icdMerchantLayout.laptopList.setIndicatorBounds(width - getDipsFromPixel(30), width - getDipsFromPixel(5));
//        activityRewardsPointsBinding.icdMerchantLayout.laptopList.setGroupIndicator(getResources().getDrawable(R.drawable.ic_down_arrow));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onClick(View v)
    {
        if( v == activityRewardsPointsBinding.llEarnPointBox)
        {
            activityRewardsPointsBinding.icdRewardHistoryLayout.relRewards.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdHistoryListviewLayout.relHistory.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdMerchantLayout.llMerchant.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdEarnPointLayout.relEarn.setVisibility(View.VISIBLE);

            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus);
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house_blue);

            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.white));
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));

            activityRewardsPointsBinding.tvHistory.setAlpha((float) 0.5);
        }
        if( v == activityRewardsPointsBinding.llMerchantBox)
        {
            activityRewardsPointsBinding.icdRewardHistoryLayout.relRewards.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdHistoryListviewLayout.relHistory.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdMerchantLayout.llMerchant.setVisibility(View.VISIBLE);
            activityRewardsPointsBinding.icdEarnPointLayout.relEarn.setVisibility(View.GONE);

            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house);

            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.white));

            activityRewardsPointsBinding.tvHistory.setAlpha((float) 0.5);
        }
        if( v == activityRewardsPointsBinding.tvHistory)
        {
            activityRewardsPointsBinding.tvHistory.setAlpha((float) 1.0);

            activityRewardsPointsBinding.icdRewardHistoryLayout.relRewards.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdHistoryListviewLayout.relHistory.setVisibility(View.VISIBLE);
            activityRewardsPointsBinding.icdMerchantLayout.llMerchant.setVisibility(View.GONE);
            activityRewardsPointsBinding.icdEarnPointLayout.relEarn.setVisibility(View.GONE);

            activityRewardsPointsBinding.tvPoints.setTextColor(getResources().getColor(R.color.colorPrimary));
            activityRewardsPointsBinding.tvMerchant.setTextColor(getResources().getColor(R.color.colorPrimary));

            activityRewardsPointsBinding.ivCirclePlus.setImageResource(R.drawable.ic_circle_plus_blue);
            activityRewardsPointsBinding.ivHouse.setImageResource(R.drawable.ic_house_blue);
        }
        if ( v == imgBack)
        {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class HttpAsyncGetAll extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get All..");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get all" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetAll(urls[0]);
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

                    JSONArray jsonArray = jsonObject.getJSONArray("MerchantProducts");

                    if (jsonArray.length() != 0)
                    {

                        for (int i = 0 ; i< jsonArray.length(); i++)
                        {
                            JSONObject productListObj = jsonArray.getJSONObject(i);

                            String ProductCategoryID = productListObj.getString("ProductCategoryID");
                            String ProductCategoryName = productListObj.getString("ProductCategoryName");

                            groupList.add(ProductCategoryName);
                            merchantGetAllModel = new MerchantGetAllModel();
                            merchantGetAllModel.setProductCategoryID(ProductCategoryID);
                            merchantGetAllModel.setProductCategoryName(ProductCategoryName);

                            categoryList.add(ProductCategoryName);

                            JSONArray jsonArray1 = productListObj.getJSONArray("MerchantByCat");
                            if (jsonArray1.length() != 0)
                            {
                                int n = jsonArray1.length();
                                String[] child_Data = new String[n];

                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayList1 = new ArrayList<>();

                                for (int j = 0; j< jsonArray1.length(); j++)
                                {
                                    JSONObject merchantListObj = jsonArray1.getJSONObject(j);

                                    String Merchant_ID = merchantListObj.getString("Merchant_ID");
                                    String MerchantImage = merchantListObj.getString("MerchantImage");
                                    String Merchant_Name = merchantListObj.getString("Merchant_Name");
                                    String Merchant_Desc = merchantListObj.getString("Merchant_Desc");
                                    String ProductID = merchantListObj.getString("ProductID");
                                    String ProductImage = merchantListObj.getString("ProductImage");
                                    String ProductName = merchantListObj.getString("ProductName");
                                    String ProductDesc = merchantListObj.getString("ProductDesc");
                                    String Offer = merchantListObj.getString("Offer");
                                    String ProductCost = merchantListObj.getString("ProductCost");

                                   /* merchantGetAllModel.setMerchant_ID(Merchant_ID);
                                    merchantGetAllModel.setMerchantImage(MerchantImage);
                                    merchantGetAllModel.setMerchant_Name(Merchant_Name);
                                    merchantGetAllModel.setMerchant_Desc(Merchant_Desc);
                                    merchantGetAllModel.setProductID(ProductID);
                                    merchantGetAllModel.setProductImage(ProductImage);
                                    merchantGetAllModel.setProductName(ProductName);
                                    merchantGetAllModel.setProductDesc(ProductDesc);
                                    merchantGetAllModel.setOffer(Offer);
                                    merchantGetAllModel.setProductCost(ProductCost);
                                    merchantGetAllModelArrayList.add(merchantGetAllModel);*/

                                    arrayList.add(Merchant_Name);
                                    arrayList1.add(Merchant_ID);

                                   /* child_Data[j] = Merchant_Name ;
                                    String parents = groupList.get(i).toString();

                                    for (String parent : groupList)
                                    {
                                        parent = groupList.get(i).toString() ;

                                        if (parent.equals(ProductCategoryName))
                                        {
                                            loadChild(child_Data);
//                                            childList.add(j,ProductName);
                                        }
                                        laptopCollection.put(parent, childList);
                                    }
                                    expListAdapter = new ExpandableListAdapter1(RewardsPointsActivity.this, groupList, laptopCollection);
                                    activityRewardsPointsBinding.icdMerchantLayout.laptopList.setAdapter(expListAdapter);*/

                                   /*For second try*/
                                  /* for (String parent: categoryList)
                                   {
                                       if (parent.equals(ProductCategoryName))
                                       {
                                           subCategoryList.add(Merchant_Name);
                                       }
                                       categorysData.put(parent, subCategoryList);
                                   }
                                    merchantExpandableAdapter = new MerchantExpandableAdapter(RewardsPointsActivity.this, categoryList, categorysData);
                                    activityRewardsPointsBinding.icdMerchantLayout.laptopList.setAdapter(merchantExpandableAdapter);*/
                                }

                                merchantGetAllModel.setMerchantNameList(arrayList);
                                merchantGetAllModel.setMerchantIdList(arrayList1);
                            }
                            else if (jsonArray1.length() == 0)
                            {

                            }

                            merchantGetAllModelArrayList.add(merchantGetAllModel);

                            categorysData.put(merchantGetAllModelArrayList.get(i).getProductCategoryName(), merchantGetAllModelArrayList.get(i).getMerchantNameList());

                            merchantExpandableAdapter = new MerchantExpandableAdapter(RewardsPointsActivity.this, categoryList, categorysData);
                            activityRewardsPointsBinding.icdMerchantLayout.laptopList.setAdapter(merchantExpandableAdapter);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    public String PostGetAll(String url)
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


    private class HttpAsyncGetProductCategory extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product Category..");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get product category" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProductCategory(urls[0]);
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
                        Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_LONG).show();
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
           /* dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get products" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProducts(urls[0]);
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
                        Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_LONG).show();
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
           /* dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Product By Category..");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get product by category" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetProductsByCategory(urls[0]);
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
                        Toast.makeText(getApplicationContext(), "No product found", Toast.LENGTH_LONG).show();
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



    private class HttpAsyncGetBalance extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get Balance...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get balance" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetBalance(urls[0]);
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
                    String userid = jsonObject.getString("userid");
                    String points_earned = jsonObject.getString("points_earned");

                    activityRewardsPointsBinding.tvRewardPoints.setText(points_earned);
                    activityRewardsPointsBinding.tvRewardBalance.setText(points_earned);
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
            jsonObject.accumulate("userid", userId);

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
            /*dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get history earn points...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Fetching products" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetHistoryEarnedPoints(urls[0]);
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
                    String userid = jsonObject.getString("userid");

                    try
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("EarnedPoints_Trans");
                        if (jsonArray.length() != 0)
                        {
                            for (int i = 0 ; i < jsonArray.length(); i++)
                            {
                                activityRewardsPointsBinding.icdEarnPointLayout.tvEarnListInfo.setVisibility(View.GONE);

                                JSONObject productListObj = jsonArray.getJSONObject(i);

                                String Earned_ID = productListObj.getString("Earned_ID");
                                String Points_Earned = productListObj.getString("Points_Earned");
                                String Benefit_Name = productListObj.getString("Benefit_Name");
                                String Benefit_Desc = productListObj.getString("Benefit_Desc");
                                String Benefit_Date = productListObj.getString("Benefit_Date");

                                EarnPointsModel earnPointsModel = new EarnPointsModel();
                                earnPointsModel.setEarnId(productListObj.getString("Earned_ID"));
                                earnPointsModel.setPointEarned(productListObj.getString("Points_Earned"));
                                earnPointsModel.setBenefitName(productListObj.getString("Benefit_Name"));
                                earnPointsModel.setBenefitDesc(productListObj.getString("Benefit_Desc"));
                                earnPointsModel.setBenefitDate(productListObj.getString("Benefit_Date"));
                                earnPointsModelsList.add(earnPointsModel);
                            }

                            earnPointsAdapter = new EarnPointsAdapter(RewardsPointsActivity.this, earnPointsModelsList);
                            activityRewardsPointsBinding.icdEarnPointLayout.listViewEarn.setAdapter(earnPointsAdapter);
                            earnPointsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            activityRewardsPointsBinding.icdEarnPointLayout.tvEarnListInfo.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), "No EarnPoints Avail", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        activityRewardsPointsBinding.icdEarnPointLayout.tvEarnListInfo.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
//                    activityRewardsPointsBinding.icdEarnPointLayout.tvEarnListInfo.setVisibility(View.VISIBLE);
//                     Toast.makeText(getApplicationContext(), "Not able to load ..", Toast.LENGTH_LONG).show();
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
            jsonObject.accumulate("userid", userId);

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
           /* dialog = new ProgressDialog(RewardsPointsActivity.this);
            dialog.setMessage("Get history redeem points...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Get history redeem points" ;
            CustomProgressDialog(loading,getApplicationContext());
        }

        @Override
        protected String doInBackground(String... urls) {
            return PostGetHistoryReedemedPoints(urls[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String userid = jsonObject.getString("FilterId");

                    if (success.equalsIgnoreCase("1"))
                    {
                        try
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("ReedemedPoints_Trans");
                            if (jsonArray.length() != 0)
                            {
                                activityRewardsPointsBinding.icdHistoryListviewLayout.tvHistoryListInfo.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArray.length(); i++)
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

                                    ArrayList<ListCell> items = new ArrayList<ListCell>();

                                    items.add(new ListCell("", "", ProductName, "+" + Points_Used));

                                    items = sortAndAddSections(items);

                                    ListAdapter1 adapter = new ListAdapter1(RewardsPointsActivity.this, items);
                                    activityRewardsPointsBinding.icdHistoryListviewLayout.awesomeList.setAdapter(adapter);
                                }
                            }
                            else
                            {
                                activityRewardsPointsBinding.icdHistoryListviewLayout.tvHistoryListInfo.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            activityRewardsPointsBinding.icdHistoryListviewLayout.tvHistoryListInfo.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        activityRewardsPointsBinding.icdHistoryListviewLayout.tvHistoryListInfo.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), "No Redeem Points Avail", Toast.LENGTH_LONG).show();
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
            jsonObject.accumulate("FilterId", userId);
            jsonObject.accumulate("FilterType", "user");

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
