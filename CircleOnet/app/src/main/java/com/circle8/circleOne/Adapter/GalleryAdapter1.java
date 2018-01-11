package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 07/06/2017.
 */

public class GalleryAdapter1 extends RecyclerView.Adapter<GalleryAdapter1.MyViewHolder>
{
    private Context mContext;
    private GestureDetector gestureDetector1;

    ArrayList<FriendConnection> nfcModelList = new ArrayList<>();
    ArrayList<FriendConnection> nfcModelListFilter = new ArrayList<>();
    public static int posi = 0;
    public static ImageView imageView ;
    RelativeLayout defaultCard1, defaultCard;

    private static TextView tvPersonWebsite, tvCompany;
    ImageView carousel_logo, imgTopLogo;

    private LayoutInflater inflater;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private DisplayImageOptions options;
    private Fragment fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image1);
            defaultCard1 = (RelativeLayout) itemView.findViewById(R.id.rltDefaultCard1);
            defaultCard = (RelativeLayout) itemView.findViewById(R.id.rltDefaultCard);
            tvCompany = (TextView)itemView.findViewById(R.id.tvComName);
            tvPersonWebsite = (TextView)itemView.findViewById(R.id.tvweb);
            carousel_logo = (ImageView) itemView.findViewById(R.id.carousel_logo);
            imgTopLogo = (ImageView) itemView.findViewById(R.id.imgTopLogo);
        }
    }

    public GalleryAdapter1(Context applicationContext, ArrayList<FriendConnection> nfcModel)
    {
        mContext = applicationContext ;
        this.nfcModelList = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);

        inflater = LayoutInflater.from(applicationContext);

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_view, parent, false);

        //GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        //GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

        //this.gestureDetector1= new GestureDetector(mContext, gestureListener);

        //this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);

        /*itemView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                return gestureDetector1.onTouchEvent(me);
            }
        });*/

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        //Integer image = images.get(position);
        //  Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_back(), 0, nfcModelList.get(position).getCard_back().length);

        //  imageView.setImageResource(nfcModelList.get(position).getCard_back());
        if (nfcModelList.get(position).getCard_back().equals(""))
        {
            imageView.setTag(position);
            imageView.setVisibility(View.GONE);
            defaultCard.setVisibility(View.GONE);
            defaultCard1.setVisibility(View.VISIBLE);

            if (nfcModelList.get(position).getWebsite().equals("") || nfcModelList.get(position).getWebsite().toString() == null ||
                    nfcModelList.get(position).getWebsite().equalsIgnoreCase("null")){
                tvPersonWebsite.setVisibility(View.GONE);
            }
            else {
                tvPersonWebsite.setVisibility(View.VISIBLE);
            }

            if (nfcModelList.get(position).getCompany().equals("") || nfcModelList.get(position).getCompany().toString() == null ||
                    nfcModelList.get(position).getCompany().equalsIgnoreCase("null")){
                tvCompany.setVisibility(View.GONE);
            }
            else {
                tvCompany.setVisibility(View.VISIBLE);
            }

            if ((nfcModelList.get(position).getCompany().equals("") || nfcModelList.get(position).getCompany().toString() == null ||
                    nfcModelList.get(position).getCompany().equalsIgnoreCase("null")) &&
                    (nfcModelList.get(position).getWebsite().equals("") || nfcModelList.get(position).getWebsite().toString() == null ||
                            nfcModelList.get(position).getWebsite().equalsIgnoreCase("null"))){

                carousel_logo.setVisibility(View.VISIBLE);
                imgTopLogo.setVisibility(View.GONE);
            }
            else {
                carousel_logo.setVisibility(View.GONE);
                imgTopLogo.setVisibility(View.VISIBLE);
            }

            tvPersonWebsite.setText(nfcModelList.get(position).getWebsite());
            tvCompany.setText(nfcModelList.get(position).getCompany());

          imageView.setImageResource(R.drawable.profile_final4b);
        }
        else
        {
            imageView.setTag(position);
            imageView.setVisibility(View.VISIBLE);
            defaultCard.setVisibility(View.GONE);
            defaultCard1.setVisibility(View.GONE);
            //imageView.setImageResource(nfcModelList.get(position).getCard_front());
            ImageLoader.getInstance().displayImage(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList.get(position).getCard_back(), imageView, options, animateFirstListener);

            //Picasso.with(mContext).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList.get(position).getCard_back()).skipMemoryCache().into(imageView);

        }
        defaultCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.CustomProgressDialog("Loading",mContext);

                CardDetail.profile_id = nfcModelList.get(position).getProfile_id();
                CardDetail.DateInitiated = nfcModelList.get(position).getDateInitiated();
                CardDetail.lat = nfcModelList.get(position).getLatitude();
                CardDetail.lon = nfcModelList.get(position).getLongitude();

                fragment = new CardDetail();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("adap","click");
                Utility.CustomProgressDialog("Loading",mContext);
                CardDetail.profile_id = nfcModelList.get(position).getProfile_id();
                CardDetail.DateInitiated = nfcModelList.get(position).getDateInitiated();
                CardDetail.lat = nfcModelList.get(position).getLatitude();
                CardDetail.lon = nfcModelList.get(position).getLongitude();

                fragment = new CardDetail();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

       /* imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                posi = position;
                return gestureDetector1.onTouchEvent(me);
            }
        });*/

//        Glide.with(mContext).load(images.get(position))
//                .thumbnail(0.5f).crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return nfcModelList.size();
    }



    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList.clear();

        if(charText.length() == 0)
        {
            nfcModelList.addAll(nfcModelListFilter);
        }
        else
        {
            for(FriendConnection md : nfcModelListFilter)
            {
                if(md.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nfcModelList.add(md);
                }
            }
        }
        notifyDataSetChanged();
    }
}
