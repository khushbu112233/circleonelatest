package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.Activity.EditProfileActivity;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 8/24/2017.
 */

public class AddEventAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<String> addEvent ;
    private static LayoutInflater inflater = null;

    public AddEventAdapter(Context applicationContext, ArrayList<String> addEvent)
    {
        this.context = applicationContext ;
        this.addEvent = addEvent ;
    }

    @Override
    public int getCount() {
        return addEvent.size();
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView ;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.event_list, null);

        TextView tvEventName = (TextView)view.findViewById(R.id.tvEventName);
        ImageView ivCancel = (ImageView)view.findViewById(R.id.ivCancelEvent);

        tvEventName.setText(addEvent.get(position));

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditProfileActivity.addEventList.remove(position);
                EditProfileActivity.addEventAdapter.notifyDataSetChanged();

                if(EditProfileActivity.addEventList.size() == 0)
                {
                    EditProfileActivity.tvEventInfo.setVisibility(View.GONE);
                }
            }
        });

        return view ;
    }
}
