package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Adapter.List3Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class List3Fragment extends Fragment {

    private ListView listView;
    private List3Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;
    RelativeLayout lnrSearch;
    View line;

    public List3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list3, container, false);

        db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);

        List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getCard_front());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }

        listView = (ListView) view.findViewById(R.id.listViewType3);
        gridAdapter = new List3Adapter(getContext(), R.layout.grid_list3_layout, imgf, desc, name, designation);
        listView.setAdapter(gridAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CardDetail.class);
                getContext().startActivity(intent);
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
