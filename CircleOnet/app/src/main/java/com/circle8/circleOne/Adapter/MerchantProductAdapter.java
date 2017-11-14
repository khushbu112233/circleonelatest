package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Activity.MerchantDetailActivity;
import com.circle8.circleOne.Model.MerchantProductModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 11/13/2017.
 */

public class MerchantProductAdapter extends BaseAdapter
{
    Activity activity ;
    ArrayList<MerchantProductModel> merchantProductModelArrayList ;

    public MerchantProductAdapter(Activity activity, ArrayList<MerchantProductModel> merchantProductModelArrayList)
    {
        this.activity = activity;
        this.merchantProductModelArrayList = merchantProductModelArrayList;
    }

    @Override
    public int getCount() {
        return merchantProductModelArrayList.size();
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
        ImageView ivProductImg ;
        TextView tvProductName, tvProductDesc, tvTermsAndCondition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.merchant_products_lists, null);
            holder = new ViewHolder();

            holder.tvProductName = (TextView)row.findViewById(R.id.tvProductName);
            holder.tvProductDesc = (TextView)row.findViewById(R.id.tvProductDesc);
            holder.tvTermsAndCondition = (TextView)row.findViewById(R.id.tvTermsAndCondition);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvProductName.setText(merchantProductModelArrayList.get(position).getProductName());
        holder.tvProductDesc.setText(merchantProductModelArrayList.get(position).getProductDesc());

        return row;
    }
}
