package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.circleonet.Model.TestimonialModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

/**
 * Created by admin on 08/23/2017.
 */

public class TestimonialAdapter extends BaseAdapter {

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    ArrayList<TestimonialModel> testimonialModels;
    private static LayoutInflater inflater = null;

    public TestimonialAdapter (Activity a, ArrayList<TestimonialModel> testimonialModels) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.full_testimonial_row, null);

        TextView title2 = (TextView) vi.findViewById(R.id.txtTestimonialStatus); // title
        String song = testimonialModels.get(position).getStatus();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.txtTestimonialName); // notice
        String pos = String.valueOf(position+1);
        String song2 = testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName();
        title22.setText(song2);

        TextView title222 = (TextView) vi.findViewById(R.id.txtTestimonialDesc); // notice
        String song3 = testimonialModels.get(position).getTestimonial_Text();
        title222.setText(song3);

        return vi;

    }
}
