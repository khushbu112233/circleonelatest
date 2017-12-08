package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 06/13/2017.
 */

public class CardSwipe extends PagerAdapter
{
    Context context ;
    ArrayList<String> image;
    public static ImageView imageView;

    public CardSwipe(Context applicationContext, ArrayList<String> image)
    {
        this.context = applicationContext ;
        this.image = image;
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
        View view = LayoutInflater.from(context).inflate(R.layout.card_swipe, null);
        //container.addView(view);

        try
        {
            imageView = (ImageView)view.findViewById(R.id.ivImages);
            Log.e("str_share",""+image.get(position));
//            imageView.setImageResource(image.get(position));
        //   Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
            // ImageView image = (ImageView) findViewById(R.id.imageView1);
       //    Bitmap str_share =getBitmapFromURL(image.get(position));


             // Picasso.with(context).load(image.get(position)).skipMemoryCache().into(imageView);
//            imageView.setImageResource(image.get(position));
        //  Bitmap bitmap = Glide. with(context). load(image.get(position)). asBitmap(). into(500, 500).get();

            Glide.with(context).load(image.get(position)).override(300,300).into(imageView);

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
