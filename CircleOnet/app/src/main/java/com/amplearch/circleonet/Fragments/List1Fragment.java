package com.amplearch.circleonet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amplearch.circleonet.Gesture.SwipeGestureDetector;
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
    RelativeLayout lnrSearch;
    View line;
    private String DEBUG_TAG = "gesture";
  //  private GestureDetector gestureDetector;
  //  private View.OnTouchListener gestureListener;

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
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer


     /*   gestureDetector = new GestureDetector(getContext(), new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };*/
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });

       /* view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        Toast.makeText(getContext(), "Down", Toast.LENGTH_LONG).show();
                        Log.d(DEBUG_TAG,"Action was DOWN");
                        lnrSearch.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                        return true;
                    case (MotionEvent.ACTION_UP) :
                        Toast.makeText(getContext(), "up", Toast.LENGTH_LONG).show();
                        Log.d(DEBUG_TAG,"Action was UP");
                        lnrSearch.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        return true;
                }

                return false;
            }
        });
*/
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

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            if((e1.getX() - e2.getX()) > sensitvity){
                swipe += "Swipe Left\n";
            }else if((e2.getX() - e1.getX()) > sensitvity){
                swipe += "Swipe Right\n";
            }else{
                swipe += "\n";
            }

            if((e1.getY() - e2.getY()) > sensitvity){
                swipe += "Swipe Up\n";
                lnrSearch.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                CardsFragment.tabLayout.setVisibility(View.GONE);
            }else if((e2.getY() - e1.getY()) > sensitvity){
                swipe += "Swipe Down\n";
                lnrSearch.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                CardsFragment.tabLayout.setVisibility(View.VISIBLE);
            }else{
                swipe += "\n";
            }

          //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);
}

