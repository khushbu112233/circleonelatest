package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ample-Arch on 19-01-2018.
 */

public class PopupMenuAdapter extends BaseAdapter
{

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList<String> name;
    private static ArrayList<String> image;

    private static LayoutInflater inflater = null;

    public PopupMenuAdapter(Activity a, ArrayList<String> name, ArrayList<String> image) {
        activity = a;
        this.name = name;
        this.image = image;


    }

    public int getCount() {
        return name.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        TextView txtName ;
        CircleImageView circleImageView ;
        ProgressBar progress;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            row = inflater.inflate(R.layout.layout_profile_popup, null);
            holder = new ViewHolder();

            holder.circleImageView = (CircleImageView) row.findViewById(R.id.imgGroup);
            holder.txtName = (TextView) row.findViewById(R.id.tvGroupName);
            holder.progress = row.findViewById(R.id.progress);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        if (image.get(position).equals("") || image.get(position).equals("null") || image.get(position).equals(null))
        {
            holder.progress.setVisibility(View.GONE);
            if ( name.get(position).equalsIgnoreCase("Add New Profile")){
                holder.circleImageView.setImageResource(R.drawable.ic_add_new_profile);

            }else {
                holder.circleImageView.setImageResource(R.drawable.usr);
            }
        }
        else
        {
            holder.progress.setVisibility(View.GONE);
            Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + image.get(position))
                    .resize(300,300).onlyScaleDown().into(holder.circleImageView);
        }

        holder.txtName.setText(name.get(position).toString());


        return row;
    }
}