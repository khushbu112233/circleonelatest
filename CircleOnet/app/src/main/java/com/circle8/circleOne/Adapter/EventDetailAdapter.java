package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

    static class ViewHolder
    {
        TextView tvDate, tvStTime, tvEnTime ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.showing_time, null);
            holder = new ViewHolder();

            holder.tvDate = (TextView)row.findViewById(R.id.tvEventDate);
            holder.tvStTime = (TextView)row.findViewById(R.id.tvStartTime);
            holder.tvEnTime = (TextView)row.findViewById(R.id.tvEndTime);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvDate.setText(eventModelArrayList.get(position).getEventDate());
        holder.tvStTime.setText(eventModelArrayList.get(position).getStartDate());
        holder.tvEnTime.setText(eventModelArrayList.get(position).getEndDate());

        return row;
    }
}
