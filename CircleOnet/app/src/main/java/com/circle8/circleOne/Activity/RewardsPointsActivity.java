package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.circle8.circleOne.Adapter.ExpandableListAdapter1;
import com.circle8.circleOne.Model.ListAdapter1;
import com.circle8.circleOne.Model.ListCell;
import com.circle8.circleOne.R;

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
    static ImageView imgDrawer, imgBack;

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

        init();
        init1();

        llEarnPointBox.setOnClickListener(this);
        llMerchantBox.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
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

    }
}
