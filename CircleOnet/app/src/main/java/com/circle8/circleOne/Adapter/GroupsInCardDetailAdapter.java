package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/18/2017.
 */

public class GroupsInCardDetailAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    public GroupsInCardDetailAdapter(CardDetail cardDetail, ArrayList<String> img,
                                     ArrayList<String> name, ArrayList<String> desc)
    {
        this.context = cardDetail ;
        this.img = img ;
        this.name = name ;
        this.desc = desc ;
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
        View vi = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)

            vi = inflater.inflate(R.layout.groups_display_in_card_details, null);

        CircleImageView groupImg = (CircleImageView)vi.findViewById(R.id.imgProfile1);
        TextView groupName = (TextView)vi.findViewById(R.id.tvPersonName1);
        TextView groupDesc = (TextView)vi.findViewById(R.id.tvDesignation1);

        groupName.setText(name.get(position));
        groupDesc.setText(desc.get(position));

        if (img.get(position).equals(""))
        {
            groupImg.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+img.get(position)).placeholder(R.drawable.usr_1).into(groupImg);
        }

        return vi;
    }
}
