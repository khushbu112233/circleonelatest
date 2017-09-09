package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.circleonet.Model.NewCardModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/8/2017.
 */

public class NewCardRequestAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<NewCardModel> newCardModelArrayList ;

    ArrayList<String> name  = new ArrayList<>();
    ArrayList<String> designation = new ArrayList<>() ;
    ArrayList<String> company = new ArrayList<>() ;
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> phone = new ArrayList<>();
    ArrayList<String> profile = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>();

    public NewCardRequestAdapter(Context applicationContext, ArrayList<NewCardModel> newCardModelArrayList)
    {
        this.context = applicationContext;
        this.newCardModelArrayList = newCardModelArrayList;
    }

    public NewCardRequestAdapter(Context applicationContext, ArrayList<String> name,
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

            /*tvPerson.setText(newCardModelArrayList.get(position).getPersonName());
            tvDesignation.setText(newCardModelArrayList.get(position).getPersonDesignation());
            tvCompany.setText(newCardModelArrayList.get(position).getPersonCompany());
            tvEmail.setText(newCardModelArrayList.get(position).getPersonEmail());
            tvPhone.setText(newCardModelArrayList.get(position).getPersonPhone());
            tvProfile.setText(newCardModelArrayList.get(position).getPersonProfile());
            ivProfile.setImageResource(Integer.parseInt(newCardModelArrayList.get(position).getPersonImage()));*/

            tvPerson.setText(name.get(position));
            tvDesignation.setText(designation.get(position));
            tvCompany.setText(company.get(position));
            tvEmail.setText(email.get(position));
            tvPhone.setText(phone.get(position));
            tvProfile.setText(profile.get(position));
            ivProfile.setImageResource(Integer.parseInt(image.get(position)));

        }

        return row;
    }
}