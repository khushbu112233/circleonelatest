package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Activity.GroupsActivity;
import com.circle8.circleOne.Activity.MyAccountActivity;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/22/2017.
 */

public class GroupDisplayAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<GroupModel> groupModelsList ;
    String groupPhoto ;

    public GroupDisplayAdapter(Activity activity, ArrayList<GroupModel> groupModelArrayList)
    {
        this.context = activity ;
        this.groupModelsList = groupModelArrayList ;
    }

    @Override
    public int getCount() {
        return groupModelsList.size();
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
        CircleImageView imgGroup ;
        TextView tvGroupName, tvGroupDesc, tvMemberCount ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.groups_displaying, null);
            holder = new ViewHolder();

            holder.imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            holder.tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            holder.tvGroupDesc = (TextView)row.findViewById(R.id.tvGroupDesc);
            holder.tvMemberCount = (TextView)row.findViewById(R.id.tvMemberCount);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvGroupName.setText(groupModelsList.get(position).getGroup_Name());
        holder.tvGroupDesc.setText(groupModelsList.get(position).getGroup_Desc());
        holder.tvMemberCount.setText("("+groupModelsList.get(position).getGroup_member_count()+")");

        groupPhoto = groupModelsList.get(position).getGroup_Photo() ;

        if (groupPhoto.equals(""))
        {
            holder.imgGroup.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupPhoto).placeholder(R.drawable.usr_1).into(holder.imgGroup);
        }

        holder.imgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (groupPhoto.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupPhoto).placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position
                dialog.show();
            }
        });

        return row;
    }
}
