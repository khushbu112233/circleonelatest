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

    public MerchantAddressAdapter(Activity activity, ArrayList<String> storeList, ArrayList<String> addressList)
    {
        this.activity = activity;
        this.storeList = storeList ;
        this.addressList = addressList;
    }

    @Override
    public int getCount() {
        return storeList.size();
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
        TextView tvTextStore, tvTextAddress ;
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

            holder.tvTextStore = (TextView)row.findViewById(R.id.tvTextStore);
            holder.tvTextAddress = (TextView)row.findViewById(R.id.tvTextAddress);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvTextStore.setText(storeList.get(position));
        holder.tvTextAddress.setText(addressList.get(position));

        return row;
    }
}
