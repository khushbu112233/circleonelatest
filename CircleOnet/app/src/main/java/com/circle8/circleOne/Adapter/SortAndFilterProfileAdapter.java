package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.Connect;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 10/25/2017.
 */

public class SortAndFilterProfileAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<ProfileModel> profileModelArrayList ;

    public SortAndFilterProfileAdapter(Context sortAndFilterOption, ArrayList<ProfileModel> profileModelArrayList)
    {
        this.context = sortAndFilterOption ;
        this.profileModelArrayList = profileModelArrayList ;
    }

    @Override
    public int getCount() {
        return profileModelArrayList.size();
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
        TextView profileName ;
        CircleImageView profileImg ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder = null;

        if (vi == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.sort_filter_groups, null);
            holder = new ViewHolder();

            holder.profileImg = (CircleImageView)vi.findViewById(R.id.imgProfile1);
            holder.profileName = (TextView)vi.findViewById(R.id.tvPersonName1);

            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }

        holder.profileName.setText(profileModelArrayList.get(position).getProfileName());

        if (profileModelArrayList.get(position).getUserPhoto().equals(""))
        {
            holder.profileImg.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+profileModelArrayList.get(position).getUserPhoto())
                    .resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(holder.profileImg);
        }

        return vi;
    }
}
