package com.circle8.circleOne.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Interface.ItemClickProfile;
import com.circle8.circleOne.Interface.ItemLongClickProfile;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by ample-arch on 9/8/2017.
 */

public class NewCardRequestAdapter extends RecyclerView.Adapter<NewCardRequestAdapter.MyViewHolder>
{
    Context context ;
    String ProfileID = "";
    private int layoutResourceId;
    ArrayList<ProfileModel> newCardModelArrayList = new ArrayList<>();
    ItemClickProfile itemClickProfile;
    ItemLongClickProfile itemLongClickProfile;
    String cat_name;

    public void onItemclick(ItemClickProfile itemClickProfile) {
        this.itemClickProfile = itemClickProfile;
    }

    public void onItemLongClick(ItemLongClickProfile itemLongClickProfile) {
        this.itemLongClickProfile = itemLongClickProfile;
    }

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
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView tvPerson, tvDesignation, tvCompany, tvEmail, tvPhone, tvProfile ;
        CircleImageView ivProfile ;
        FrameLayout fm_img;
        ProgressBar progressBar1;
        LinearLayout ll_main;

        public MyViewHolder(View row) {
            super(row);


            fm_img =(FrameLayout)row.findViewById(R.id.fm_img);
            progressBar1 = (ProgressBar)row.findViewById(R.id.progressBar1);
            tvPerson = (TextView)row.findViewById(R.id.tvPerson);
            tvDesignation = (TextView)row.findViewById(R.id.tvDesignation);
            tvCompany = (TextView)row.findViewById(R.id.tvCompany);
            tvEmail = (TextView)row.findViewById(R.id.tvEmail);
            tvPhone = (TextView)row.findViewById(R.id.tvPhone);
            ivProfile = (CircleImageView)row.findViewById(R.id.imgProfile);
            tvProfile = (TextView)row.findViewById(R.id.tvProfile);
            ll_main = (LinearLayout)row.findViewById(R.id.ll_main);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_card_request_parameter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvPerson.setText(newCardModelArrayList.get(position).getFirstName() + " " + newCardModelArrayList.get(position).getLastName());
        holder.tvDesignation.setText(newCardModelArrayList.get(position).getDesignation());
        holder.tvCompany.setText(newCardModelArrayList.get(position).getCompanyName());
        holder.tvEmail.setText(newCardModelArrayList.get(position).getEmail1());
        holder.tvPhone.setText(newCardModelArrayList.get(position).getMobile1());
        holder.tvProfile.setText(newCardModelArrayList.get(position).getProfile());

        if (newCardModelArrayList.get(position).getUserPhoto().equals(""))
        {
            holder.progressBar1.setVisibility(View.GONE);
            holder.ivProfile.setImageResource(R.drawable.usr_1);
        }
        else
        {
            holder.progressBar1.setVisibility(View.VISIBLE);
            Glide.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+newCardModelArrayList.get(position).getUserPhoto())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(holder.ivProfile) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.ivProfile.setImageBitmap(drawable);
                        }
                    });
            //   Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+newCardModelArrayList.get(position).getUserPhoto()).resize(300,300).onlyScaleDown().skipMemoryCache().into(holder.ivProfile);
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
                    Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+newCardModelArrayList.get(position).getUserPhoto()).placeholder(R.drawable.usr_1)
                            .resize(300,300).onlyScaleDown().skipMemoryCache().into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"/UserProfile/"+newCardModelArrayList.get(position).getUserPhoto());
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
        holder.ll_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                itemClickProfile.OnItemClickProfile(position);
            }
        });

        holder.ll_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemLongClickProfile.OnItemLongClickProfile(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return newCardModelArrayList.size();
    }



}
