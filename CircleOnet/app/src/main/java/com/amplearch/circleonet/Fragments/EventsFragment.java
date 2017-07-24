package com.amplearch.circleonet.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Activity.EventDetail;
import com.amplearch.circleonet.Activity.EventsActivity;
import com.amplearch.circleonet.Adapter.EventsAdapter;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private ListView listView;
    private EventsAdapter gridAdapter;
    private TextView actionText;
    RelativeLayout lnrSearch;
    View line;
    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ArrayList<Integer> image = new ArrayList<Integer>();
        image.add(R.drawable.events1);
        image.add(R.drawable.events2);
        image.add(R.drawable.events3);
        image.add(R.drawable.events4);
        image.add(R.drawable.events5);

        ArrayList<String> title = new ArrayList<>();
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");

        ArrayList<String> desc = new ArrayList<>();
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);

        listView = (ListView) view.findViewById(R.id.listEvents);
        gridAdapter = new EventsAdapter(getContext(), R.layout.row_events, image, title, desc);
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
                Intent intent = new Intent(getContext(), EventDetail.class);
                startActivity(intent);
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
