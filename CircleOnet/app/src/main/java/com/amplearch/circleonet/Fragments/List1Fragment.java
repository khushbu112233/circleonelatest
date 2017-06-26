package com.amplearch.circleonet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.Adapter.MyPager;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.List;

public class List1Fragment extends Fragment{

   // private ArrayList<Integer> imageFront = new ArrayList<>();
   // private ArrayList<Integer> imageBack = new ArrayList<>();
    private ViewPager viewPager;
    private MyPager myPager ;
    DatabaseHelper db ;

    ArrayList<byte[]> imgf;
    ArrayList<byte[]> imgb;
    public List1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list1, container, false);

        db = new DatabaseHelper(getContext());
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer

       /* imageFront.add(R.drawable.card1f);


        imageFront.add(R.drawable.card2f);


        imageFront.add(R.drawable.card1_front);

        imageFront.add(R.drawable.card4_front);
        imageFront.add(R.drawable.card5_front);


        imageBack.add(R.drawable.card1b);


        imageBack.add(R.drawable.card2f);


        imageBack.add(R.drawable.card1_back);

        imageBack.add(R.drawable.card4_back);
        imageBack.add(R.drawable.card5_back);
*/
       imgf = new ArrayList<byte[]>();
        imgb = new ArrayList<byte[]>();
        List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getCard_front());
            imgb.add(tag.getCard_back());
        }



        myPager = new MyPager(getContext(), imgf, imgb);
        viewPager.setAdapter(myPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private int index = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }



}
