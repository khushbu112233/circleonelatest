package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Activity.MerchantDetailActivity;
import com.circle8.circleOne.Model.MerchantProductModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ample-arch on 11/13/2017.
 */

public class MerchantProductAdapter extends BaseAdapter
{
    Activity activity ;
    ArrayList<MerchantProductModel> merchantProductModelArrayList ;

    public MerchantProductAdapter(Activity activity, ArrayList<MerchantProductModel> merchantProductModelArrayList)
    {
        this.activity = activity;
        this.merchantProductModelArrayList = merchantProductModelArrayList;
    }

    @Override
    public int getCount() {
        return merchantProductModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder
    {
        ImageView ivProductImg ;
        TextView tvProductName, tvProductDesc, tvTermsAndCondition;
        ProgressBar progressBar1;
        FrameLayout fm_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.merchant_products_lists, null);
            holder = new ViewHolder();

            holder.ivProductImg = (ImageView)row.findViewById(R.id.ivProductImg);
            holder.tvProductName = (TextView)row.findViewById(R.id.tvProductName);
            holder.tvProductDesc = (TextView)row.findViewById(R.id.tvProductDesc);
            holder.tvTermsAndCondition = (TextView)row.findViewById(R.id.tvTermsAndCondition);

            holder.progressBar1= (ProgressBar)row.findViewById(R.id.progressBar1);
            holder.fm_img = (FrameLayout) row.findViewById(R.id.fm_img);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        if (merchantProductModelArrayList.get(position).getProductImage().equals(""))
        {
            holder.tvProductName.setText(merchantProductModelArrayList.get(position).getProductName());
            holder.tvProductDesc.setText(merchantProductModelArrayList.get(position).getProductDesc());
            holder.ivProductImg.setVisibility(View.GONE);
            holder.progressBar1.setVisibility(View.GONE);
            holder.fm_img.setVisibility(View.GONE);
        }
        else
        {
            holder.progressBar1.setVisibility(View.VISIBLE);
            holder.fm_img.setVisibility(View.VISIBLE);
            holder.ivProductImg.setVisibility(View.VISIBLE);
            final ViewHolder finalHolder = holder;
            Glide.with(activity).load(Utility.BASE_IMAGE_URL+"Product/"+merchantProductModelArrayList.get(position).getProductImage())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(finalHolder.ivProductImg) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            finalHolder.progressBar1.setVisibility(View.GONE);
                            finalHolder.ivProductImg.setImageBitmap(drawable);
                        }
                    });
        }

        return row;
    }
}
