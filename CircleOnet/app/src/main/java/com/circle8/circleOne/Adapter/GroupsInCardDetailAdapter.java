package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/18/2017.
 */

public class GroupsInCardDetailAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    public GroupsInCardDetailAdapter(CardDetail cardDetail, ArrayList<String> img,
                                     ArrayList<String> name, ArrayList<String> desc)
    {
        this.context = cardDetail ;
        this.img = img ;
        this.name = name ;
        this.desc = desc ;
    }

    @Override
    public int getCount() {
        return name.size();
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
        TextView groupName, groupDesc ;
        CircleImageView groupImg ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder = null;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.groups_display_in_card_details, null);
            holder = new ViewHolder();

            holder.groupImg = (CircleImageView)vi.findViewById(R.id.imgProfile1);
            holder.groupName = (TextView)vi.findViewById(R.id.tvPersonName1);
            holder.groupDesc = (TextView)vi.findViewById(R.id.tvDesignation1);

            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }


        holder.groupName.setText(name.get(position));
        holder.groupDesc.setText(desc.get(position));

        if (img.get(position).equals(""))
        {
            holder.groupImg.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Group/"+img.get(position)).placeholder(R.drawable.usr_1).into(holder.groupImg);
        }

        return vi;
    }
}
