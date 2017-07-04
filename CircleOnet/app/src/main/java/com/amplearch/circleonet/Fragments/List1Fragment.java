package com.amplearch.circleonet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.GridViewAdapter;
import com.amplearch.circleonet.Gesture.SwipeGestureDetector;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.Adapter.MyPager;
import com.amplearch.circleonet.R;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class List1Fragment extends Fragment
{
   // private ArrayList<Integer> imageFront = new ArrayList<>();
   // private ArrayList<Integer> imageBack = new ArrayList<>();
    public static ViewPager viewPager;
    public static MyPager myPager ;
    DatabaseHelper db ;

    /*ArrayList<byte[]> imgf;
    ArrayList<byte[]> imgb;
    ArrayList<String> tag_id;*/
    RelativeLayout lnrSearch;
    View line;
    private String DEBUG_TAG = "gesture";
  //  private GestureDetector gestureDetector;
  //  private View.OnTouchListener gestureListener;

    public static List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    public static ArrayList<NFCModel> nfcModel ;

    public List1Fragment()
    {
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

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();
        allTags = db.getActiveNFC();

       /* lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);*/
        viewPager.setClipChildren(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewPager.setOffscreenPageLimit(5);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

       /* imgf = new ArrayList<byte[]>();
        imgb = new ArrayList<byte[]>();
        tag_id = new ArrayList<String>();*/

        GetData(getContext());
        //myPager = new MyPager(getContext(), imgf, imgb, tag_id);
        //viewPager.setAdapter(myPager);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() <= 0)
                {
                    nfcModel.clear();
                    allTags = db.getActiveNFC();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    myPager.Filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
            try {
                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    swipe += "Swipe Right\n";
                } else {
                    swipe += "\n";
                }

                if ((e1.getY() - e2.getY()) > sensitvity) {
                    swipe += "Swipe Up\n";
                    lnrSearch.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                } else {
                    swipe += "\n";
                }

                //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

                return super.onFling(e1, e2, velocityX, velocityY);
            }
            catch (Exception e ){
                return true;
            }
        }
    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel.clear();
        GetData(getContext());
    }

    public static void GetData(Context context)
    {
        //newly added
        for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setCard_back(reTag.getCard_back());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());

            nfcModel.add(nfcModelTag);
        }

        Collections.sort(nfcModel, new Comparator<NFCModel>() {
            public int compare(NFCModel o1, NFCModel o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        myPager = new MyPager(context, R.layout.cardview_list, nfcModel);
        viewPager.setAdapter(myPager);
        myPager.notifyDataSetChanged();

        //gridAdapter.setMode(Attributes.Mode.Single);

    }

}