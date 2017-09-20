package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Model.ConnectList;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 8/28/2017.
 */

public class ConnectListAdapter extends BaseAdapter
{
    ArrayList<ConnectList> connectLists = new ArrayList<>();
    ArrayList<ConnectList> connectListsFilter = new ArrayList<>();
    private Context context;
    private int layoutResourceId;
    DatabaseHelper db;

    String personName, designation, company, website, email, description ;

    public ConnectListAdapter(Context context, int grid_list3_layout, ArrayList<ConnectList> connectLists)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.connectLists = connectLists ;
        this.connectListsFilter.addAll(connectLists);
    }

    @Override
    public int getCount() {
        return connectLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        TextView nameText, descText, detailText ;
        CircleImageView circleImageView ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.grid_list5_layout, parent, false);
            holder = new ViewHolder();

            holder.circleImageView = (CircleImageView)row.findViewById(R.id.imageList4);
            holder.nameText = (TextView)row.findViewById(R.id.textNameList3);
            holder.descText = (TextView)row.findViewById(R.id.textDescList3);
            holder.detailText = (TextView)row.findViewById(R.id.textList3);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.nameText.setText(connectLists.get(position).getFirstname()+" "+connectLists.get(position).getLastname());
//        descText.setText(connectLists.get(position).getDesignation());
//        detailText.setText(connectLists.get(position).getCompanyname()+"\n"+connectLists.get(position).getUsername()+"\n"
//                +connectLists.get(position).getWebsite());

        if(connectLists.get(position).getCompanyname().equalsIgnoreCase("")
                || connectLists.get(position).getCompanyname().equalsIgnoreCase("null"))
        {
            company = "" ;
        }
        else
        {
            company = connectLists.get(position).getCompanyname() ;
        }

        if(connectLists.get(position).getUsername().equalsIgnoreCase("")
                || connectLists.get(position).getUsername().equalsIgnoreCase("null"))
        {
            email = "" ;
        }
        else
        {
            email = connectLists.get(position).getUsername() ;
        }

        if(connectLists.get(position).getWebsite().equalsIgnoreCase("")
                || connectLists.get(position).getWebsite().equalsIgnoreCase("null"))
        {
            website = "" ;
        }
        else
        {
            website = connectLists.get(position).getWebsite() ;
        }

        if(connectLists.get(position).getDesignation().equalsIgnoreCase("")
                || connectLists.get(position).getDesignation().equalsIgnoreCase("null"))
        {
            holder.descText.setText("");
//            descText.setVisibility(View.GONE);
        }
        else
        {
            holder.descText.setText(connectLists.get(position).getDesignation());
        }

        designation = company+"\n"+email+"\n"+website ;

        holder.detailText.setText(company+"\n"+email+"\n"+website);


        if(connectLists.get(position).getUserphoto().equalsIgnoreCase(""))
        {
            holder.circleImageView.setImageResource(R.drawable.usr);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+connectLists.get(position).getUserphoto()).into(holder.circleImageView);
        }

        return row;
    }

}
