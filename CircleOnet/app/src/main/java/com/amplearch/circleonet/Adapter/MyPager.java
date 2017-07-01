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
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Fragments.List2Fragment;
import com.amplearch.circleonet.Fragments.List3Fragment;
import com.amplearch.circleonet.Fragments.List4Fragment;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ample-arch on 6/7/2017.
 */

public class MyPager extends PagerAdapter
{
    Context context ;
    /*ArrayList<byte[]> image, image1 ;
    ArrayList<String> tag_id;*/
    DatabaseHelper db;
    private int layoutResourceId;
    //newly added
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();
    /*public MyPager(Context applicationContext, ArrayList<byte[]> image, ArrayList<byte[]> image1, ArrayList<String> tag_id)
    {
        this.context = applicationContext ;
        this.image = image;
        this.image1 = image1;
        this.tag_id = tag_id;
    }*/

    public MyPager(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.nfcModelList = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);

    }

    @Override
    public int getCount() {
        return nfcModelList.size();
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
    public Object instantiateItem(ViewGroup container, final int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_list, null);
        try
        {
            db = new DatabaseHelper(context);
            ImageView imageView = (ImageView)view.findViewById(R.id.ivImages);
            ImageView imageView1 = (ImageView)view.findViewById(R.id.ivImages1);

            Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);
           // ImageView image = (ImageView) findViewById(R.id.imageView1);

            imageView.setImageBitmap(bmp);


          //  byte[] imgbytes = Base64.decode(image.get(position), Base64.DEFAULT);
          //  Bitmap bitmap = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_back(), 0, nfcModelList.get(position).getCard_back().length);

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
                    intent.putExtra("tag_id", nfcModelList.get(position).getNfc_tag());
                    context.startActivity(intent);
                }
            });

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList.clear();

        if(charText.length() == 0)
        {
            nfcModelList.addAll(nfcModelListFilter);
            try {
                List1Fragment.myPager.notifyDataSetChanged();
                List1Fragment.nfcModel.clear();
                List1Fragment.allTags = db.getActiveNFC();
                //  nfcModelList.clear();
                List1Fragment.GetData(context);
            } catch (Exception e){

            }
        }
        else
        {
            List1Fragment.allTags.clear();
            for(NFCModel md : nfcModelListFilter)
            {
                if(md.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nfcModelList.add(md);
                    try {
                        List1Fragment.myPager.notifyDataSetChanged();
                        List1Fragment.nfcModel.clear();
                        List1Fragment.allTags.add(md);
                        //  nfcModelList.clear();
                        List1Fragment.GetData(context);
                    } catch (Exception e){

                    }
                }
            }
        }


        notifyDataSetChanged();
    }

}
