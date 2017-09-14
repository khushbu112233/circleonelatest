package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/9/2017.
 */

public class GroupDetailAdapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();

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

    public GroupDetailAdapter(Context applicationContext, int group_detail_items,
                              ArrayList<GroupDetailModel> groupDetailModelArrayList)
    {
        this.context = applicationContext ;
        this.layoutResourceId = group_detail_items ;
        this.groupDetailModelArrayList = groupDetailModelArrayList;
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

        holder.personName = (TextView) row.findViewById(R.id.tvPersonName);
        holder.personDesignation = (TextView) row.findViewById(R.id.tvDesignation);
        holder.personDetail = (TextView) row.findViewById(R.id.tvPersonDetail);
        holder.personImage = (CircleImageView) row.findViewById(R.id.imgProfile);
        row.setTag(holder);

        holder.personName.setText(groupDetailModelArrayList.get(position).getFirstname()+" "+
                groupDetailModelArrayList.get(position).getLastname());
        holder.personDesignation.setText(groupDetailModelArrayList.get(position).getDesignation());

        /*String Company = company.get(position);
        String Email = email.get(position);
        String Website = website.get(position);
        String Phone = phone.get(position);
        String Mobile = mobile.get(position);
        String Address = address.get(position);*/

        String Company = groupDetailModelArrayList.get(position).getCompany();
        String Email = groupDetailModelArrayList.get(position).getEmail();
        String Website = groupDetailModelArrayList.get(position).getWebsite();
        String Phone = groupDetailModelArrayList.get(position).getMobile();
        String Address1 = groupDetailModelArrayList.get(position).getAddress1();
        String Address2 = groupDetailModelArrayList.get(position).getAddress2();
        String Address3 = groupDetailModelArrayList.get(position).getAddress2();
        String Address4 = groupDetailModelArrayList.get(position).getAddress4();
        String City = groupDetailModelArrayList.get(position).getCity();
        String State = groupDetailModelArrayList.get(position).getState();
        String Country = groupDetailModelArrayList.get(position).getCountry();
        String PostalCode = groupDetailModelArrayList.get(position).getPostalcode();
        String Address = Address1+" "+Address2+" "+Address3+" "+Address4+"\n"+City+" "+State+" "+Country+" "+PostalCode;

        holder.personDetail.setText(Address+"\n"+Company+"\n"+Website+"\n"+Email+"\n"+Phone);

        String profile = groupDetailModelArrayList.get(position).getImgProfile() ;

        if (profile.equals(""))
        {
            holder.personImage.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+profile).placeholder(R.drawable.usr_1).into(holder.personImage);
        }
    }

    static class ViewHolder
    {
        TextView personName;
        TextView personDesignation;
        TextView personDetail;
        CircleImageView personImage;
    }

    @Override
    public int getCount() {
        return groupDetailModelArrayList.size();
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
