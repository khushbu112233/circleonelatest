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
import com.circle8.circleOne.Model.MerchantLocationModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 10/26/2017.
 */

public class MerchantAddressAdapter extends BaseAdapter
{
    Activity activity ;
    ArrayList<String> storeList ;
    ArrayList<String> addressList ;
    ArrayList<String> storeTimeList;

    ArrayList<MerchantLocationModel> merchantLocationModelArrayList = new ArrayList<>();

    public MerchantAddressAdapter(Activity activity, ArrayList<String> storeList, ArrayList<String> addressList, ArrayList<String> storeTimeList)
    {
        this.activity = activity;
        this.storeList = storeList ;
        this.addressList = addressList;
        this.storeTimeList = storeTimeList;
    }

    public MerchantAddressAdapter(Activity activity,
                                  ArrayList<MerchantLocationModel> merchantLocationModelArrayList)
    {
        this.activity = activity ;
        this.merchantLocationModelArrayList = merchantLocationModelArrayList ;
    }

    @Override
    public int getCount() {
        return merchantLocationModelArrayList.size();
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
        ImageView adImg ;
        TextView tvStoreName, tvStoreAddress1, tvStoreAddress2, tvOpenTime, tvCloseTime, tvHourFlag ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.merchant_address_lists, null);
            holder = new ViewHolder();

            holder.tvStoreName = (TextView)row.findViewById(R.id.tvStoreName);
            holder.tvStoreAddress1 = (TextView)row.findViewById(R.id.tvStoreAddress1);
            holder.tvStoreAddress2 = (TextView)row.findViewById(R.id.tvStoreAddress2);
            holder.tvOpenTime = (TextView)row.findViewById(R.id.tvOpenTime);
            holder.tvCloseTime = (TextView)row.findViewById(R.id.tvCloseTime);
            holder.tvHourFlag = (TextView)row.findViewById(R.id.tvHourFlag);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvStoreName.setText(merchantLocationModelArrayList.get(position).getStoreName());
        holder.tvStoreAddress1.setText(merchantLocationModelArrayList.get(position).getAddress1()
                +", "+merchantLocationModelArrayList.get(position).getAddress2()
                +", "+merchantLocationModelArrayList.get(position).getAddress3()
                +", "+merchantLocationModelArrayList.get(position).getAddress4());
        holder.tvStoreAddress2.setText(merchantLocationModelArrayList.get(position).getCity()
                +", "+merchantLocationModelArrayList.get(position).getState()
                +", "+merchantLocationModelArrayList.get(position).getCountry()
                +"("+merchantLocationModelArrayList.get(position).getPostalCode()+").");
        holder.tvOpenTime.setText("Open Time: "+merchantLocationModelArrayList.get(position).getOpenTime());
        holder.tvCloseTime.setText("Close time: "+merchantLocationModelArrayList.get(position).getCloseTime());

        if (merchantLocationModelArrayList.get(position).getHourFlag().equalsIgnoreCase("Y"))
        {
            holder.tvHourFlag.setText("Flag Time: 24 hours open");
        }
        else
        {
            holder.tvHourFlag.setVisibility(View.GONE);
        }

        return row;
    }
}
