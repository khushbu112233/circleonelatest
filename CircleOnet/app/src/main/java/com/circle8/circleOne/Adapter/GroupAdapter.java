package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LENOVO on 27-08-2017.
 */

public class GroupAdapter extends BaseAdapter
{
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

    static class ViewHolder
    {
        TextView title2, title22, tvCompany, tvEmail, tvPhone, tvProfile ;
        CircleImageView circleImageView ;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder = null;

        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.testimonial_row, null);
            holder = new ViewHolder();

            holder.title2 = (TextView) vi.findViewById(R.id.txtTestimonial); // title
            holder.title22 = (TextView) vi.findViewById(R.id.txtName); // notice
            holder.circleImageView = (CircleImageView) vi.findViewById(R.id.imgUser);

            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }

        String song = groupModels.get(position).getGroup_Name();
        holder.title2.setText(song);

        String pos = String.valueOf(position+1);
        String song2 = "Testimonial "+pos+" : ";
        holder.title22.setVisibility(View.GONE);

        holder.circleImageView.setVisibility(View.GONE);

        return vi;
    }
}
