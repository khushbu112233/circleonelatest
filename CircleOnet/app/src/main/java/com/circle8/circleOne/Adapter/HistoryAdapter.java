package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Model.HistoryModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 10/9/2017.
 */

public class HistoryAdapter extends BaseAdapter
{
    Activity activity ;
    ArrayList<HistoryModel> historyModelArrayList ;

    public HistoryAdapter(Activity activity, ArrayList<HistoryModel> historyModelArrayList)
    {
        this.activity = activity ;
        this.historyModelArrayList = historyModelArrayList ;
    }

    @Override
    public int getCount() {
        return historyModelArrayList.size();
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
        TextView tvUserName, tvHistoryStatus, tvHistoryDate ;
        CircleImageView imgProfile ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.history_row, null);
            holder = new ViewHolder();

            holder.tvUserName = (TextView)row.findViewById(R.id.tvUserName);
            holder.tvHistoryStatus = (TextView)row.findViewById(R.id.tvHistoryStatus);
            holder.tvHistoryDate = (TextView)row.findViewById(R.id.tvHistoryDate);
            holder.imgProfile = (CircleImageView)row.findViewById(R.id.imgProfile);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvUserName.setText(historyModelArrayList.get(position).getFirstName()+" "+historyModelArrayList.get(position).getLastName());
        holder.tvHistoryStatus.setText(historyModelArrayList.get(position).getHistory_Status());
        holder.tvHistoryDate.setText(historyModelArrayList.get(position).getHistory_Date());

        if (historyModelArrayList.get(position).getUserPhoto().equals(""))
        {
            holder.imgProfile.setImageResource(R.drawable.usr);
        }
        else
        {
            Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/"+historyModelArrayList.get(position).getUserPhoto())
                    .skipMemoryCache().into(holder.imgProfile);
        }

        return row;
    }
}
