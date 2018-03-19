package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Model.PrizeHistory;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 2/27/2018.
 */

public class RedeemListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<PrizeHistory> prizeHistorysAll;

    public RedeemListAdapter(Context context, ArrayList<PrizeHistory> prizeHistorysAll)
    {
        this.context=context;
        this.prizeHistorysAll = prizeHistorysAll;
    }
    @Override
    public int getCount() {
        return prizeHistorysAll.size();
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
            holder.txtL1 = (TextView)view.findViewById(R.id.txtL1);
            holder.txtL2= (TextView)view.findViewById(R.id.txtL2);
            holder.txtBottom1= (TextView)view.findViewById(R.id.txtBottom1);
            holder.txtBottom2= (TextView)view.findViewById(R.id.txtBottom2);
            holder.txtTop1= (TextView)view.findViewById(R.id.txtTop1);
            holder.txtTop2= (TextView)view.findViewById(R.id.txtTop2);
            holder.txt2= (TextView)view.findViewById(R.id.txt2);
            holder.txt1= (TextView)view.findViewById(R.id.txt1);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.txtL1.setText(prizeHistorysAll.get(i).getCard_Name_1());
        holder.txtTop1.setText(prizeHistorysAll.get(i).getCard_Name_1());
        holder.txtBottom1.setText(prizeHistorysAll.get(i).getCard_Name_1());

        holder.txtL2.setText(prizeHistorysAll.get(i).getCard_Name_2());
        holder.txtTop2.setText(prizeHistorysAll.get(i).getCard_Name_2());
        holder.txtBottom2.setText(prizeHistorysAll.get(i).getCard_Name_2());

        holder.txt2.setText(prizeHistorysAll.get(i).getPrize_Name());
        holder.txt1.setText((i+1)+" Prize");
        return view;
    }
    static class ViewHolder
    {
        TextView txtL1,txtL2,txtBottom1,txtBottom2,txtTop1,txtTop2,txt2,txt1;
    }
}
