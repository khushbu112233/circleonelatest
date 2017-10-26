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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 10/26/2017.
 */

public class ImageAdAdapter extends BaseAdapter
{
    Activity activity ;
    ArrayList<Integer> images ;

    public ImageAdAdapter(Activity activity, ArrayList<Integer> adImages)
    {
        this.activity = activity ;
        this.images = adImages;
    }

    @Override
    public int getCount() {
        return images.size();
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.image_add_lists, null);
            holder = new ViewHolder();

            holder.adImg = (ImageView) row.findViewById(R.id.ivAdImg);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.adImg.setImageResource(images.get(position));


        return row;
    }
}
