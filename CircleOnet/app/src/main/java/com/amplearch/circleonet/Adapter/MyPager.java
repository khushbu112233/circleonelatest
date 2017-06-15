package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplearch.circleonet.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by ample-arch on 6/7/2017.
 */

public class MyPager extends PagerAdapter
{
    Context context ;
    ArrayList<Integer> image, image1 ;

    public MyPager(Context applicationContext, ArrayList<Integer> image, ArrayList<Integer> image1)
    {
        this.context = applicationContext ;
        this.image = image;
        this.image1 = image1;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_list, null);
        try
        {


            ImageView imageView = (ImageView)view.findViewById(R.id.ivImages);
            ImageView imageView1 = (ImageView)view.findViewById(R.id.ivImages1);

//            imageView.setImageResource(image.get(position));

            Glide.with(context).load(image.get(position)).into(imageView);
            Glide.with(context).load(image1.get(position)).into(imageView1);
//            Glide.with(context).load(image[position]).into(imageCover);

            /*view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //this will log the page number that was click
                    //Log.i("TAG", "This page was clicked: " + pos);

                    Intent intent = new Intent(context, CardDetail.class);
                    context.startActivity(intent);
                }
            });*/

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
