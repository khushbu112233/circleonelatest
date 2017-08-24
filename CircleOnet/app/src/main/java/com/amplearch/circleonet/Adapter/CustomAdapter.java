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

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter
{

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList title,notice;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Activity a, ArrayList b, ArrayList bod) {
        activity = a;
        this.title = b;
        this.notice=bod;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return title.size();
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
            vi = inflater.inflate(R.layout.testimonial_row, null);

        TextView title2 = (TextView) vi.findViewById(R.id.txtTestimonial2); // title
        String song = title.get(position).toString();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.txtTestimonial1); // notice
        String pos = String.valueOf(position+1);
        String song2 = "Testimonial "+pos+" : ";
        title22.setText(song2);

        return vi;

    }
}