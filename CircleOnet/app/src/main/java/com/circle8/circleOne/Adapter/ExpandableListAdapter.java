package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.R;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 8/17/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Activity context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;

    public ExpandableListAdapter(Activity context, List<String> laptops,
                                 Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        final String laptop = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.expand_child_item, null);
            holder = new ViewHolder();

            holder.item1 = (TextView)row.findViewById(R.id.tvUtility);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.item1.setText(laptop);

       /* TextView item = (TextView) convertView.findViewById(R.id.tvUtility);

        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);*/
/*
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        laptopCollections.get(laptops.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
*/
//        item.setText(laptop);
        return row;
    }

    public int getChildrenCount(int groupPosition)
    {
        try
        {
            if (laptopCollections.get(laptops.get(groupPosition)).size() != 0)
            {
                return laptopCollections.get(laptops.get(groupPosition)).size();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0 ;
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        String laptopName = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.expand_group_item, null);
            holder = new ViewHolder();

            holder.item = (TextView)row.findViewById(R.id.tvUtility);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.item.setTypeface(null, Typeface.BOLD);
        holder.item.setText(laptopName);

/*
        TextView item = (TextView) convertView.findViewById(R.id.tvUtility);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
*/
        return row;
    }

    static class ViewHolder
    {
        TextView item, item1 ;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
