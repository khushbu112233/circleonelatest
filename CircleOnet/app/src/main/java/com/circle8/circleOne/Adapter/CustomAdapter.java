package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends BaseAdapter
{

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList<TestimonialModel> testimonialModels;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Activity a, ArrayList<TestimonialModel> testimonialModels) {
        activity = a;
        this.testimonialModels = testimonialModels;


    }

    public int getCount() {
        return testimonialModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        TextView txtName, txtTestimonial ;
        CircleImageView circleImageView ;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            row = inflater.inflate(R.layout.testimonial_row, null);
            holder = new ViewHolder();

            holder.circleImageView = (CircleImageView) row.findViewById(R.id.imgUser);
            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txtTestimonial = (TextView) row.findViewById(R.id.txtTestimonial);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        if (testimonialModels.get(position).getUserPhoto().equals(""))
        {
            holder.circleImageView.setImageResource(R.drawable.usr);
        }
        else
        {
            Picasso.with(activity).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto()).skipMemoryCache().into(holder.circleImageView);
        }

        holder.txtName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        holder.txtTestimonial.setText(testimonialModels.get(position).getTestimonial_Text());


        return row;
    }
}