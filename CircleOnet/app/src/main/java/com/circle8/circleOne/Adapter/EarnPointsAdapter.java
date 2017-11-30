package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.RewardsPointsActivity;
import com.circle8.circleOne.Model.EarnPointsModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 11/13/2017.
 */

public class EarnPointsAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<EarnPointsModel> earnPointsModelArrayList = new ArrayList<>();

    public EarnPointsAdapter(Context context, ArrayList<EarnPointsModel> earnPointsModelsList)
    {
        this.context = context;
        this.earnPointsModelArrayList = earnPointsModelsList;
    }

    @Override
    public int getCount() {
        return earnPointsModelArrayList.size();
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
        TextView tvBenefitName, tvBenefitDesc, tvBenefitDate, tvEarnPoints ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.history_earn_points_items, null);
            holder = new ViewHolder();

            holder.tvEarnPoints = (TextView)row.findViewById(R.id.tvEarnPoints);
            holder.tvBenefitName = (TextView)row.findViewById(R.id.tvBenefitName);
            holder.tvBenefitDesc = (TextView)row.findViewById(R.id.tvBenefitDesc);
            holder.tvBenefitDate = (TextView)row.findViewById(R.id.tvBenefitDate);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        int pos = position+1;

        holder.tvEarnPoints.setText("+ "+earnPointsModelArrayList.get(position).getPointEarned().toString());
        holder.tvBenefitName.setText(pos+". "+earnPointsModelArrayList.get(position).getBenefitName().toString());
        holder.tvBenefitDesc.setText(earnPointsModelArrayList.get(position).getBenefitDesc().toString());
        holder.tvBenefitDate.setText(earnPointsModelArrayList.get(position).getBenefitDate().toString());

        return row;
    }
}
