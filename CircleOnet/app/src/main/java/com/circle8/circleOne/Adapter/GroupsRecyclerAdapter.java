package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/19/2017.
 */

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.MyViewHolder>
{
    Context context ;
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    public GroupsRecyclerAdapter(Activity activity, ArrayList<String> img, ArrayList<String> name, ArrayList<String> desc)
    {
        this.context = activity ;
        this.img = img ;
        this.name = name ;
        this.desc = desc ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView groupImg ;
        TextView groupName, groupDesc ;

        public MyViewHolder(View view)
        {
            super(view);
            groupImg = (CircleImageView)view.findViewById(R.id.imgProfile1);
            groupName = (TextView)view.findViewById(R.id.tvPersonName1);
            groupDesc = (TextView)view.findViewById(R.id.tvDesignation1);
        }
    }

    @Override
    public GroupsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groups_display_in_card_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupsRecyclerAdapter.MyViewHolder holder, int position)
    {
        holder.groupName.setText(name.get(position));
        holder.groupDesc.setText(desc.get(position));

        if (img.get(position).equals(""))
        {
            holder.groupImg.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Group/"+img.get(position)).placeholder(R.drawable.usr_1)
                    .skipMemoryCache().into(holder.groupImg);
        }
    }

    @Override
    public int getItemCount()
    {
        return name.size();
    }


}
