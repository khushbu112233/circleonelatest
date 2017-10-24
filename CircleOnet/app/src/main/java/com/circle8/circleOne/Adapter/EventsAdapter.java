package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 06/09/2017.
 */

public class EventsAdapter extends ArrayAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Integer> image = new ArrayList();
    private ArrayList<String> title = new ArrayList();
    private ArrayList<String> desc = new ArrayList();

    private ArrayList<EventModel> eventModelArrayList ;

    public EventsAdapter(Context context, int layoutResourceId, ArrayList<Integer> image, ArrayList<String> title, ArrayList<String> desc)
    {
        super(context, layoutResourceId, title);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public EventsAdapter(Context context, int row_events, ArrayList<EventModel> eventModelArrayList)
    {
        super(context, row_events, eventModelArrayList);
        this.layoutResourceId = row_events;
        this.context = context;
        this.eventModelArrayList = eventModelArrayList ;
    }

    static class ViewHolder
    {
        TextView txtTitle;
        TextView txtDesc;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtEventTitle);
            holder.txtDesc = (TextView) row.findViewById(R.id.txtEventDetail);
            holder.image = (ImageView) row.findViewById(R.id.imgEvents);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        /*holder.txtTitle.setText(title.get(position));
        holder.txtDesc.setText(desc.get(position));*/

        holder.txtTitle.setText(eventModelArrayList.get(position).getEvent_Name());
        holder.txtDesc.setText(eventModelArrayList.get(position).getEvent_StartDate()+" To "
                +eventModelArrayList.get(position).getEvent_EndDate());

        if(eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("")
                || eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("null") )
        {
            holder.image.setImageResource(R.drawable.events4);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Events/"+eventModelArrayList.get(position).getEvent_Image()).into(holder.image);
        }
//        holder.image.setImageResource(image.get(position));
        return row;
    }


}