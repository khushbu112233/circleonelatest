package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.R;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by ample-arch on 6/7/2017.
 */

public class MyPager extends PagerAdapter
{
    Context context ;
    ArrayList<byte[]> image, image1 ;

    public MyPager(Context applicationContext, ArrayList<byte[]> image, ArrayList<byte[]> image1)
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

            Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
           // ImageView image = (ImageView) findViewById(R.id.imageView1);

            imageView.setImageBitmap(bmp);


          //  byte[] imgbytes = Base64.decode(image.get(position), Base64.DEFAULT);
          //  Bitmap bitmap = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(image1.get(position), 0, image1.get(position).length);

          //  imageView.setImageBitmap(bitmap);
            imageView1.setImageBitmap(bitmap1);
           // Glide.with(context).load(image.get(position)).into(imageView);
          //  Glide.with(context).load(image1.get(position)).into(imageView1);
//            Glide.with(context).load(image[position]).into(imageCover);

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //this will log the page number that was click
                    //Log.i("TAG", "This page was clicked: " + pos);

                    Intent intent = new Intent(context, CardDetail.class);
                    context.startActivity(intent);
                }
            });

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
