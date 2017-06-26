package com.amplearch.circleonet.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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

        listView = (ListView) view.findViewById(R.id.listEvents);
        gridAdapter = new EventsAdapter(getContext(), R.layout.row_events, image, title, desc);
        listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EventDetail.class);
                startActivity(intent);
            }
        });


        return view;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }*/
}
