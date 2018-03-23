package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Model.Prize;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/22/2018.
 */

public class PrizeAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context ;
    ArrayList<Prize> prizeList;
    public PrizeAdapter(Context applicationContext, ArrayList<Prize> prizeList)
    {
        this.context = applicationContext ;
        this.prizeList = prizeList;
    }
    @Override
    public int getCount() {
        return prizeList.size();
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
            view = inflater.inflate(R.layout.list_prize_adapter_layout, null);
            holder = new ViewHolder();
            holder.txtPrizeLabel = (TextView)view.findViewById(R.id.txtPrizeLabel);
            holder.imgPrize = (ImageView)view.findViewById(R.id.imgPrize);
            holder.txt1 = (TextView)view.findViewById(R.id.txt1);
            holder.txt2 = (TextView)view.findViewById(R.id.txt2);
            holder.txtTop1 = (TextView)view.findViewById(R.id.txtTop1);
            holder.txtTop2 = (TextView)view.findViewById(R.id.txtTop2);
            holder.txtL2 = (TextView)view.findViewById(R.id.txtL2);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.txt1.setText(prizeList.get(i).getPrize_Name());
        holder.txt2.setText(prizeList.get(i).getCard_Name_1()+"+"+prizeList.get(i).getCard_Name_2() +"="+(i+1)+"th");
        holder.txtTop1.setText(prizeList.get(i).getCard_Name_1());
        holder.txtTop2.setText(prizeList.get(i).getCard_Name_2());
        holder.txtL2.setText(prizeList.get(i).getCard_Name_2());
        holder.txtPrizeLabel.setText((i+1)+"th Prize");
        final ViewHolder finalHolder = holder;
        Glide.with(context).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + prizeList.get(i).getPrize_Image())
                .asBitmap()
                .into(new BitmapImageViewTarget(finalHolder.imgPrize) {
                    @Override
                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        finalHolder.imgPrize.setImageBitmap(drawable);
                    }
                });
        return view;
    }
    static class ViewHolder
    {
        TextView txtPrizeLabel ,txt1 , txt2,txtTop1,txtTop2,txtL2;
        ProgressBar progressBar;
        ImageView imgPrize ;
    }

}
