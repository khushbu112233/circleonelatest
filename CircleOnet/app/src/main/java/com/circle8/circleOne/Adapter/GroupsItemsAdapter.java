package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/11/2017.
 */

public class GroupsItemsAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<String> groupName ;


    CircleImageView imgGroup, imgProfile1, imgProfile2, imgProfile3 ;
    TextView tvGroupName, tvPersonName1, tvPersonName2, tvPersonName3 ;
    TextView tvDesignation1, tvDesignation2, tvDesignation3 ;
    TextView tvDetail1, tvDetail2, tvDetail3 ;

    String company1, company2, company3;
    String website1, website2, website3 ;
    String email1, email2, email3 ;
    String phone1, phone2, phone3 ;
    String mobile1, mobile2, mobile3 ;
    String address1, address2, address3 ;

    public GroupsItemsAdapter(Context applicationContext, ArrayList<String> groupName)
    {
        this.context = applicationContext ;
        this.groupName = groupName ;
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

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.groups_items, null);

            imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            imgProfile1 = (CircleImageView)row.findViewById(R.id.imgProfile1);
            imgProfile2 = (CircleImageView)row.findViewById(R.id.imgProfile2);
            imgProfile3 = (CircleImageView)row.findViewById(R.id.imgProfile3);

            tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            tvPersonName1 = (TextView)row.findViewById(R.id.tvPersonName1);
            tvPersonName2 = (TextView)row.findViewById(R.id.tvPersonName2);
            tvPersonName3 = (TextView)row.findViewById(R.id.tvPersonName3);

            tvDesignation1 = (TextView)row.findViewById(R.id.tvDesignation1);
            tvDesignation2 = (TextView)row.findViewById(R.id.tvDesignation2);
            tvDesignation3 = (TextView)row.findViewById(R.id.tvDesignation3);

            tvDetail1 = (TextView)row.findViewById(R.id.tvPersonDetail1);
            tvDetail2 = (TextView)row.findViewById(R.id.tvPersonDetail2);
            tvDetail3 = (TextView)row.findViewById(R.id.tvPersonDetail3);

            tvGroupName.setText(groupName.get(position));

        }


        return row;
    }
}
