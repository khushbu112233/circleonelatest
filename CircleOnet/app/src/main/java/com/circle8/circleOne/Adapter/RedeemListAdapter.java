package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.circle8.circleOne.R;

/**
 * Created by ample-arch on 2/27/2018.
 */

public class RedeemListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;

    public RedeemListAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView ;
        ViewHolder holder = null;
        if( view == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_redeem_adapter_layout, null);
            holder = new ViewHolder();

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        return view;
    }
    static class ViewHolder
    {

    }
}
