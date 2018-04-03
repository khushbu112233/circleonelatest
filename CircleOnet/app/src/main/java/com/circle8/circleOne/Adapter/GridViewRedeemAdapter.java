package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Model.PushCardsDetail;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by admin on 06/08/2017.
 */

public class GridViewRedeemAdapter extends BaseAdapter
{
    Context context;
    ArrayList<PushCardsDetail> prizeHistorysAll;
    private static LayoutInflater inflater = null;
    public GridViewRedeemAdapter(Context context, ArrayList<PushCardsDetail> prizeHistorysAll)
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
            view = inflater.inflate(R.layout.redeem_grid_adapter_layout, null);
            holder = new ViewHolder();
            holder.txtTop1 = (TextView)view.findViewById(R.id.txtTop1);
            holder.img1= (ImageView) view.findViewById(R.id.img1);
            holder.txti1= (TextView)view.findViewById(R.id.txti1);
            holder.txt1= (TextView)view.findViewById(R.id.txt1);
            holder.frame_card1 = (FrameLayout)view.findViewById(R.id.frame_card1);
            holder.imgMain = (ImageView)view.findViewById(R.id.imgMain);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.txtTop1.setText(prizeHistorysAll.get(i).getCard_Name());
        holder.txt1.setText(prizeHistorysAll.get(i).getCard_Name());
        holder.txti1.setText(prizeHistorysAll.get(i).getCount());
        if(prizeHistorysAll.get(i).getCount().equalsIgnoreCase("0"))
        {
            holder.imgMain.setAlpha(0.4f);
        }else {
            holder.imgMain.setAlpha(1.0f);
        }
        return view;
    }
    static class ViewHolder
    {
        TextView txtTop1,txt1,txti1;
        FrameLayout frame_card1;
        ImageView img1,imgMain;
    }
}