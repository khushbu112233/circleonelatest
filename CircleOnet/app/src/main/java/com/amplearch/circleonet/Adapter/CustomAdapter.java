package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.Model.ItemObject;
import com.amplearch.circleonet.Model.TestimonialModel;
import com.amplearch.circleonet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.testimonial_row, null);

        CircleImageView circleImageView = (CircleImageView) vi.findViewById(R.id.imgUser);
        TextView txtName = (TextView) vi.findViewById(R.id.txtName);
        TextView txtTestimonial = (TextView) vi.findViewById(R.id.txtTestimonial);

        if (testimonialModels.get(position).getUserPhoto().equals("")){
            circleImageView.setImageResource(R.drawable.usr);
        }else {
            Picasso.with(activity).load("http://circle8.asia/App_ImgLib/UserProfile/" + testimonialModels.get(position).getUserPhoto()).into(circleImageView);
        }
        txtName.setText(testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName());
        txtTestimonial.setText(testimonialModels.get(position).getTestimonial_Text());
        return vi;

    }
}