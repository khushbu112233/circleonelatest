package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.Profile;
import com.circle8.circleOne.Model.NewCardModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/8/2017.
 */

public class NewCardRequestAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<ProfileModel> newCardModelArrayList = new ArrayList<>();

    public NewCardRequestAdapter(Context applicationContext, ArrayList<ProfileModel> newCardModelArrayList)
    {
        this.context = applicationContext;
        this.newCardModelArrayList = newCardModelArrayList;
    }

    /*public NewCardRequestAdapter(Context applicationContext, ArrayList<String> name,
           ArrayList<String> company, ArrayList<String> designation, ArrayList<String> email,
           ArrayList<String> phone, ArrayList<String> profile, ArrayList<String> image)
    {
        this.context = applicationContext ;
        this.name = name ;
        this.designation = designation ;
        this.company = company ;
        this.email = email ;
        this.phone = phone ;
        this.profile = profile ;
        this.image = image ;
    }*/

    @Override
    public int getCount() {
        return newCardModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return newCardModelArrayList.get(position);
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
            row = inflater.inflate(R.layout.new_card_request_parameter, null);

            TextView tvPerson = (TextView)row.findViewById(R.id.tvPerson);
            TextView tvDesignation = (TextView)row.findViewById(R.id.tvDesignation);
            TextView tvCompany = (TextView)row.findViewById(R.id.tvCompany);
            TextView tvEmail = (TextView)row.findViewById(R.id.tvEmail);
            TextView tvPhone = (TextView)row.findViewById(R.id.tvPhone);
            CircleImageView ivProfile = (CircleImageView)row.findViewById(R.id.imgProfile);
            TextView tvProfile = (TextView)row.findViewById(R.id.tvProfile);

            tvPerson.setText(newCardModelArrayList.get(position).getFirstName() + " " + newCardModelArrayList.get(position).getLastName());
            tvDesignation.setText(newCardModelArrayList.get(position).getDesignation());
            tvCompany.setText(newCardModelArrayList.get(position).getCompanyName());
            tvEmail.setText(newCardModelArrayList.get(position).getEmail1());
            tvPhone.setText(newCardModelArrayList.get(position).getPhone1());
            int count = position + 1;
            tvProfile.setText(newCardModelArrayList.get(position).getProfile());

            if (newCardModelArrayList.get(position).getUserPhoto().equals(""))
            {
                ivProfile.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+newCardModelArrayList.get(position).getUserPhoto()).into(ivProfile);
            }

        }

        return row;
    }
}
