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
import com.circle8.circleOne.Model.PrizeHistory;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import java.util.ArrayList;

/**
 * Created by ample-arch on 2/20/2018.
 */

public class PrizeHistoryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;

    Context context ;
    ArrayList<PrizeHistory> addEvent ;
    public PrizeHistoryAdapter(Context applicationContext, ArrayList<PrizeHistory> addEvent)
    {
        this.context = applicationContext ;
        this.addEvent = addEvent ;
    }
    @Override
    public int getCount() {
        return addEvent.size();
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
            view = inflater.inflate(R.layout.prize_history_adapter_layout, null);
            holder = new ViewHolder();

            holder.txtName = (TextView)view.findViewById(R.id.txtName);
            holder.txtResult = (TextView)view.findViewById(R.id.txtResult);
            holder.txtDate = (TextView)view.findViewById(R.id.txtDate);
            holder.imgPrize = (ImageView)view.findViewById(R.id.imgPrize);
            holder.progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.txtName.setText(addEvent.get(i).getPrize_Name());
        holder.txtDate.setText(addEvent.get(i).getPlay_Date());
        holder.txtResult.setText(addEvent.get(i).getResult());
        if (addEvent.get(i).getPrize_Image().equals(""))
        {
            holder.progressBar.setVisibility(View.GONE);
            holder.imgPrize.setImageResource(R.drawable.usr);
        }else
        {
            holder.progressBar.setVisibility(View.VISIBLE);
               /* Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                        .resize(300,300).onlyScaleDown().into(holder.imgTestRec);
*/
            final ViewHolder finalHolder = holder;
            Glide.with(context).load(Utility.BASE_IMAGE_URL+"GamePrizes/" + addEvent.get(i).getPrize_Image())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(finalHolder.imgPrize) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            finalHolder.progressBar.setVisibility(View.GONE);
                            finalHolder.imgPrize.setImageBitmap(drawable);
                        }
                    });

        }
        return view;
    }
    static class ViewHolder
    {
        TextView txtName ,txtResult , txtDate;
        ProgressBar progressBar;
        ImageView imgPrize ;
    }

}
