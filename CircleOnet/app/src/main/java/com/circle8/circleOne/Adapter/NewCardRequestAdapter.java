package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Activity.ImageZoom;

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
    private int layoutResourceId;
    ArrayList<ProfileModel> newCardModelArrayList = new ArrayList<>();

    public NewCardRequestAdapter(Context applicationContext, ArrayList<ProfileModel> newCardModelArrayList)
    {
        this.context = applicationContext;
        this.newCardModelArrayList = newCardModelArrayList;
    }

    public NewCardRequestAdapter(Context activity, int new_card_request_parameter, ArrayList<ProfileModel> allTags)
    {
        this.context = activity ;
        this.layoutResourceId = new_card_request_parameter ;
        this.newCardModelArrayList = allTags ;
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

    static class ViewHolder
    {
        TextView tvPerson, tvDesignation, tvCompany, tvEmail, tvPhone, tvProfile ;
        CircleImageView ivProfile ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if( row == null)
        {
            /*LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.new_card_request_parameter, null);*/
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.tvPerson = (TextView)row.findViewById(R.id.tvPerson);
            holder.tvDesignation = (TextView)row.findViewById(R.id.tvDesignation);
            holder.tvCompany = (TextView)row.findViewById(R.id.tvCompany);
            holder.tvEmail = (TextView)row.findViewById(R.id.tvEmail);
            holder.tvPhone = (TextView)row.findViewById(R.id.tvPhone);
            holder.ivProfile = (CircleImageView)row.findViewById(R.id.imgProfile);
            holder.tvProfile = (TextView)row.findViewById(R.id.tvProfile);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

            holder.tvPerson.setText(newCardModelArrayList.get(position).getFirstName() + " " + newCardModelArrayList.get(position).getLastName());
            holder.tvDesignation.setText(newCardModelArrayList.get(position).getDesignation());
            holder.tvCompany.setText(newCardModelArrayList.get(position).getCompanyName());
            holder.tvEmail.setText(newCardModelArrayList.get(position).getEmail1());
            holder.tvPhone.setText(newCardModelArrayList.get(position).getPhone1());
            holder.tvProfile.setText(newCardModelArrayList.get(position).getProfile());

            if (newCardModelArrayList.get(position).getUserPhoto().equals(""))
            {
                holder.ivProfile.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+newCardModelArrayList.get(position).getUserPhoto()).into(holder.ivProfile);
            }

            holder.ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.imageview_popup);

                    ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                    if (newCardModelArrayList.get(position).getUserPhoto().equals(""))
                    {
                        ivViewImage.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+newCardModelArrayList.get(position).getUserPhoto()).placeholder(R.drawable.usr_1).into(ivViewImage);
                    }
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                    ivViewImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context, ImageZoom.class);
                            intent.putExtra("displayProfile", "http://circle8.asia/App_ImgLib/UserProfile/"+newCardModelArrayList.get(position).getUserPhoto());
                            context.startActivity(intent);
                        }
                    });
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                    dialog.show();
                }
            });

        return row;
    }
}
