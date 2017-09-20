package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.EditProfileActivity;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 9/6/2017.
 */

public class AssociationAdapter extends BaseAdapter
{

    private Context context;
    private int layoutResourceId;
    ArrayList<String> associationName = new ArrayList<>();
    ArrayList<String> associationId = new ArrayList<>();

    public AssociationAdapter(EditProfileActivity editProfileActivity, int association_value,
                              ArrayList<String> assoIdList, ArrayList<String> assoNameList)
    {
        this.context = editProfileActivity ;
        this.layoutResourceId = association_value ;
        this.associationId = assoIdList ;
        this.associationName = assoNameList ;
    }

    public AssociationAdapter(EditProfileActivity editProfileActivity, ArrayList<String> assoIdList, ArrayList<String> assoNameList)
    {
        this.context = editProfileActivity ;
        this.associationId = assoIdList ;
        this.associationName = assoNameList ;
    }

    @Override
    public int getCount() {
        return associationName.size();
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
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtAssoName = (TextView) row.findViewById(R.id.tvAssociationName);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        holder.txtAssoName.setText(associationName.get(position));

        return row;
    }

    static class ViewHolder
    {
        TextView txtAssoName;
    }
}
