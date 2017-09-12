package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/12/2017.
 */

public class EditGroupAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();

    public EditGroupAdapter(Context applicationContext, ArrayList<GroupModel> groupModelArrayList)
    {
        this.context = applicationContext ;
        this.groupModelArrayList = groupModelArrayList ;
    }

    public EditGroupAdapter(CardDetail cardDetail, ArrayList<String> groupName, ArrayList<String> groupPhoto)
    {
        this.context = cardDetail ;
        this.groupName = groupName ;
        this.groupPhoto = groupPhoto ;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.edit_groups_details_popup, null);

            CircleImageView imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            TextView tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            CheckBox checkBox = (CheckBox)row.findViewById(R.id.chCheckBox);

            tvGroupName.setText(groupName.get(position));

        }

        return row;
    }
}
