package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Activity.GroupsActivity;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Activity.UpdateGroupActivity;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/22/2017.
 */

public class GroupDisplayAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<GroupModel> groupModelsList ;
    CircleImageView ivGroupImage ;
    LoginSession session;
    String profile_id, user_id;

    CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    public static String grpImg, grpName, grpDesc, grpID ;

    public GroupDisplayAdapter(Activity activity, ArrayList<GroupModel> groupModelArrayList)
    {
        this.context = activity ;
        this.groupModelsList = groupModelArrayList ;

        session = new LoginSession(context);
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        user_id = user.get(LoginSession.KEY_USERID);
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
        TextView tvGroupName, tvGroupDesc, tvMemberCount, tvEditGroup ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
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
            holder.tvEditGroup = (TextView)row.findViewById(R.id.tvEditGroup);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvGroupName.setText(groupModelsList.get(position).getGroup_Name());
        holder.tvGroupDesc.setText(groupModelsList.get(position).getGroup_Desc());
        holder.tvMemberCount.setText("("+groupModelsList.get(position).getGroup_member_count()+")");

        if (groupModelsList.get(position).getGroup_Photo().equals(""))
        {
            holder.imgGroup.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupModelsList.get(position).getGroup_Photo()).placeholder(R.drawable.usr_1).into(holder.imgGroup);
        }

        holder.imgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (groupModelsList.get(position).getGroup_Photo().equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupModelsList.get(position).getGroup_Photo()).placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity =  Gravity.CENTER ;
                wmlp.x = 0;
                wmlp.y = 0;   //y position
                dialog.show();

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, ImageZoom.class);
                        intent.putExtra("displayProfile", "http://circle8.asia/App_ImgLib/Group/"+groupModelsList.get(position).getGroup_Photo());
                        context.startActivity(intent);
                    }
                });

            }
        });

        holder.tvEditGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GroupsActivity.ivAlphaImg.setVisibility(View.VISIBLE);
                GroupsActivity.CreateOrUpdateStatus = "Update";
                GroupsActivity.tvCreateOrUpdate.setText("Update");
                GroupsActivity.tvTextView.setText("Update Circle");
                GroupsActivity.rlLayTwo.setVisibility(View.VISIBLE);
                GroupsActivity.listView.setEnabled(false);

               /* GroupsActivity.grpImg = groupModelsList.get(position).getGroup_Photo();
                GroupsActivity.grpName = groupModelsList.get(position).getGroup_Name();
                GroupsActivity.grpDesc = groupModelsList.get(position).getGroup_Desc();
                GroupsActivity.grpID = groupModelsList.get(position).getGroup_ID() ;*/

               grpImg = groupModelsList.get(position).getGroup_Photo();
               grpID =  groupModelsList.get(position).getGroup_ID() ;

                GroupsActivity.etCircleName.setText(groupModelsList.get(position).getGroup_Name());
                GroupsActivity.etCircleDesc.setText(groupModelsList.get(position).getGroup_Desc());

                String grpImg = groupModelsList.get(position).getGroup_Photo();

                if (grpImg.equals(""))
                {
                    GroupsActivity.ivGroupImage.setImageResource(R.drawable.user_2);
                }
                else
                {
                    Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+grpImg).placeholder(R.drawable.user_2).into(GroupsActivity.ivGroupImage);
                }

               /* Intent in = new Intent(context, UpdateGroupActivity.class);
                in.putExtra("type", "group");
                in.putExtra("GroupImage", groupModelsList.get(position).getGroup_Photo());
                in.putExtra("GroupName", groupModelsList.get(position).getGroup_Name());
                in.putExtra("GroupDesc", groupModelsList.get(position).getGroup_Desc());
                in.putExtra("GroupID", groupModelsList.get(position).getGroup_ID());
                context.startActivity(in);*/
            }
        });

        return row;
    }

}
