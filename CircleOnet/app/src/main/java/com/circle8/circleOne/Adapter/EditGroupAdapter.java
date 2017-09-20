package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.CardDetail.selectedStrings;

/**
 * Created by ample-arch on 9/12/2017.
 */

public class EditGroupAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();
    ArrayList<String> groupId = new ArrayList<>();

    public EditGroupAdapter(Context applicationContext, ArrayList<GroupModel> groupModelArrayList)
    {
        this.context = applicationContext ;
        this.groupModelArrayList = groupModelArrayList ;
    }

    public EditGroupAdapter(CardDetail cardDetail, ArrayList<String> groupName, ArrayList<String> groupPhoto, ArrayList<String> groupId)
    {
        this.context = cardDetail ;
        this.groupName = groupName ;
        this.groupPhoto = groupPhoto ;
        this.groupId = groupId;
    }

    @Override
    public int getCount() {
        return groupName.size();
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
        TextView tvGroupName ;
        CircleImageView imgGroup ;
        CheckBox checkBox ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.edit_groups_details_popup, null);
            holder = new ViewHolder();

            holder.imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            holder.tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            holder.checkBox = (CheckBox)row.findViewById(R.id.chCheckBox);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvGroupName.setText(groupName.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.put(groupId.get(position));
                }else{
                    selectedStrings.remove(position);
                }

            }
        });

        if (groupPhoto.get(position).equals(""))
        {
            holder.imgGroup.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupPhoto.get(position)).into(holder.imgGroup);
        }

        return row;
    }
}
