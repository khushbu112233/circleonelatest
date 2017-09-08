package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.circleonet.Model.EventModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

/**
* Created by ample-arch on 9/7/2017.
*/
public class EventDetailAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<EventModel> eventModelArrayList ;

    public EventDetailAdapter(Context applicationContext, ArrayList<EventModel> eventModelArrayList1)
    {
        this.context = applicationContext ;
        this.eventModelArrayList = eventModelArrayList1 ;
    }

    @Override
    public int getCount() {
        return eventModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;


        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.showing_time, null);
            TextView tvDate = (TextView)row.findViewById(R.id.tvEventDate);
            TextView tvStTime = (TextView)row.findViewById(R.id.tvStartTime);
            TextView tvEnTime = (TextView)row.findViewById(R.id.tvEndTime);

            tvDate.setText(eventModelArrayList.get(position).getEventDate());
            tvStTime.setText(eventModelArrayList.get(position).getStartDate());
            tvEnTime.setText(eventModelArrayList.get(position).getEndDate());

        }

        return row;
    }


}
