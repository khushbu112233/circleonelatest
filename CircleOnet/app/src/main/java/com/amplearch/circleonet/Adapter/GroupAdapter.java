package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.amplearch.circleonet.Model.GroupModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 27-08-2017.
 */

public class GroupAdapter extends BaseAdapter {
    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static List<GroupModel> groupModels;
    private static LayoutInflater inflater = null;

    public GroupAdapter(Activity a, List<GroupModel> groupModels) {
        activity = a;
        this.groupModels = groupModels;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return groupModels.size();
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
        String song = groupModels.get(position).getGroup_Name();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.txtTestimonial1); // notice
        String pos = String.valueOf(position+1);
        String song2 = "Testimonial "+pos+" : ";
        title22.setVisibility(View.GONE);

        return vi;

    }
}
