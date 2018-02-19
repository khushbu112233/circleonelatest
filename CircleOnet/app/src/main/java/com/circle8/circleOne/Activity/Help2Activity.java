package com.circle8.circleOne.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.circle8.circleOne.Adapter.ExpandableListAdapter;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.circle8.circleOne.Utils.Utility.callSubPAge;

public class Help2Activity extends AppCompatActivity
{
    private ExpandableListView expListView;
    private List<String> questionsList=new ArrayList<>();
    private List<String> answersList=new ArrayList<>();
    private Map<String, String> collectionList=new LinkedHashMap<>();
    ImageView imgBack;
    ExpandableListAdapter expListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utility.freeMemory();
        setContentView(R.layout.activity_help2);

        expListView = (ExpandableListView)findViewById(R.id.QA_listview);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        questionsList=Utility.createGroupList();
        collectionList = Utility.createCollection();
        expListAdapter = new ExpandableListAdapter(this, questionsList, collectionList);
        expListView.setAdapter(expListAdapter);

        setGroupIndicatorToRight();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.freeMemory();
                finish();
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            // Keep track of previous expanded parent
            int previousGroup = -1;
            boolean flag = false;

            @Override
            public void onGroupExpand(int groupPosition)
            {
                Utility.freeMemory();
                // Collapse previous parent if expanded.
                if ((previousGroup != -1) && (groupPosition != previousGroup))
                {
                    expListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
                flag = true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        callSubPAge("Help","LeftMenu");

    }

    private void setGroupIndicatorToRight()
    {
        Utility.freeMemory();
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(30), width - getDipsFromPixel(3));
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



}
