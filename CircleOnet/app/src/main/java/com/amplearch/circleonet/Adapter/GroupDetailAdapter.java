package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amplearch.circleonet.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/9/2017.
 */

public class GroupDetailAdapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> website = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> imgprofile = new ArrayList<>();

    public GroupDetailAdapter(Context applicationContext, int group_detail_items,
           ArrayList<String> name, ArrayList<String> designation, ArrayList<String> company,
           ArrayList<String> website, ArrayList<String> email, ArrayList<String> phone,
           ArrayList<String> mobile, ArrayList<String> address, ArrayList<String> imgprofile)
    {
        this.context = applicationContext ;
        this.layoutResourceId = group_detail_items ;
        this.name = name ;
        this.designation = designation ;
        this.company = company ;
        this.website = website ;
        this.email = email ;
        this.phone = phone ;
        this.mobile = mobile;
        this.address = address ;
        this.imgprofile = imgprofile ;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.group_detail_items, null);

        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                swipeLayout.close();
            }
        });

        return v ;
    }

    @Override
    public void fillValues(int position, View convertView)
    {
        View row = convertView;
        ViewHolder holder = null;

        holder = new ViewHolder();

        holder.name = (TextView) row.findViewById(R.id.tvPersonName);
        holder.designation = (TextView) row.findViewById(R.id.tvDesignation);
        holder.detail = (TextView) row.findViewById(R.id.tvPersonDetail);
        holder.image = (CircleImageView) row.findViewById(R.id.imgProfile);
        row.setTag(holder);

        holder.name.setText(name.get(position));
        holder.designation.setText(designation.get(position));

        String Company = company.get(position);
        String Email = email.get(position);
        String Website = website.get(position);
        String Phone = phone.get(position);
        String Mobile = mobile.get(position);
        String Address = address.get(position);

        holder.detail.setText(Address+"\n"+Company+"\n"+Website+"\n"+Email+"\n"+Phone+"\n"+Mobile);
    }

    static class ViewHolder
    {
        TextView name;
        TextView designation;
        TextView detail;
        CircleImageView image;
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


}
