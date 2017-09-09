package com.amplearch.circleonet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import com.amplearch.circleonet.Adapter.ExpandableListAdapter;
import com.amplearch.circleonet.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Help2Activity extends AppCompatActivity
{
    private ExpandableListView expListView;
    private List<String> questionsList;
    private List<String> answersList;
    private Map<String, List<String>> collectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);

        expListView = (ExpandableListView)findViewById(R.id.QA_listview);

        createGroupList();
        createCollection();

        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, questionsList, collectionList);
        expListView.setAdapter(expListAdapter);

        setGroupIndicatorToRight();

    }

    private void setGroupIndicatorToRight()
    {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width - getDipsFromPixel(3));
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

    private void createGroupList()
    {
        questionsList = new ArrayList<String>();
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: How can I change my password?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: How can I change my password?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: How can I change my password?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: How can I change my password?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: How can I change my password?");
        questionsList.add("Q: What should I do after I lost my card/s?");
        questionsList.add("Q: What should I do after I lost my card/s?");
    }

    private void createCollection()
    {
        // preparing laptops collection(child)
        String[] firstAnswer = {"A: One", "   Two", "   Three" ,"   Four"};
        String[] secondAnswer = {
                "A: Step1- Under My Account, click 'Profile'.",
                "   Step2- Click 'Change Password'.",
                "   Step3- Enter New Password.",
                "   Step4- Click 'Submit'." };
        String[] thirdAnswer = {"Gujarat", "Rajasthan", "Karanataka", "Maharastra" };
        String[] forthAnswer = { "Ample Arch", "India NIC", "Brain Hidden", "Silver Touch" };
        String[] fifthAnswer = { "Budget", "Caltex*+", "ComfortDelGro Rent-A-Car", "SGDrivers" };

        collectionList = new LinkedHashMap<String, List<String>>();

        for (String laptop : questionsList)
        {
            if (laptop.equals("Q: What should I do after I lost my card/s?"))
            {
                loadChild(firstAnswer);
            }
            else if (laptop.equals("Q: How can I change my password?"))
            {
                loadChild(secondAnswer);
            }

            collectionList.put(laptop, answersList);
        }
    }

    private void loadChild(String[] laptopModels)
    {
        answersList = new ArrayList<String>();

        for (String model : laptopModels)
        {
            answersList.add(model);
        }
    }

}
