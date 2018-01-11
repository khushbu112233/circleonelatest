package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ample-arch on 7/6/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder>
{
    private Context mContext;
    private GestureDetector gestureDetector1;
    public static int pos = 0;

    public static ArrayList<FriendConnection> nfcModelList = new ArrayList<>();
    public static ArrayList<FriendConnection> nfcModelListFilter = new ArrayList<>();
    private RecyclerView.ViewHolder holder;
    public static ImageView imageView ;
    public static int posi = 0;
    DatabaseHelper db;
    RelativeLayout defaultCard;
    private LayoutInflater inflater;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnItemClickListener listener;
    private DisplayImageOptions options;
    private Fragment fragment;


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public void onItemClick(int poisition);
    }
    GalleryAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
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

    private static TextView tvPersonName, tvPersonProfile, tvPersonWebsite, tvPersonAddress, tvPersonContact,
            tvPersonNameLast, tvPersonMobile;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public MyViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image1);
            defaultCard = (RelativeLayout) itemView.findViewById(R.id.rltDefaultCard);
            tvPersonName = (TextView)itemView.findViewById(R.id.tvPersonName);
            tvPersonProfile = (TextView)itemView.findViewById(R.id.tvPersonProfile);
            tvPersonWebsite = (TextView)itemView.findViewById(R.id.tvPersonWebsite);
            tvPersonAddress = (TextView)itemView.findViewById(R.id.tvPersonAddress);
            tvPersonContact = (TextView)itemView.findViewById(R.id.tvPersonContact);
            tvPersonNameLast = (TextView) itemView.findViewById(R.id.tvPersonNameLast);
            tvPersonMobile = (TextView) itemView.findViewById(R.id.tvPersonMobile);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();


            CardDetail.profile_id = nfcModelList.get(position).getProfile_id();
            CardDetail.DateInitiated = nfcModelList.get(position).getDateInitiated();
            CardDetail.lat = nfcModelList.get(position).getLatitude();
            CardDetail.lon = nfcModelList.get(position).getLongitude();

            fragment = new CardDetail();
            ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_container_wrapper, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public GalleryAdapter(Context applicationContext, ArrayList<FriendConnection> nfcModel)
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

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_view, parent, false);
        db = new DatabaseHelper(mContext);
       // GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
      ///  GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

       // this.gestureDetector1= new GestureDetector(mContext, gestureListener);

       // this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);

        /*itemView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                return gestureDetector1.onTouchEvent(me);
            }
        });*/

        return new MyViewHolder(itemView);
    }
    EmbossMaskFilter embossmaskfilter = new EmbossMaskFilter(
            new float[]{ 0f, -1f, 0.5f },
            0.7f,
            12,
            8.0f
    );
    @Override
    public void onBindViewHolder(GalleryAdapter.MyViewHolder holder, final int position)
    {
        //Integer image = images.get(position);
        // Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);

        this.holder = holder;
        if (nfcModelList.get(position).getCard_front().equals(""))
        {
            imageView.setTag(position);
            imageView.setVisibility(View.GONE);
            defaultCard.setVisibility(View.VISIBLE);
            String kept = "", remainder = "";
            try
            {
                try {
                    if (nfcModelList.get(position).getDesignation().trim().equals("") || nfcModelList.get(position).getDesignation().equals("null")
                            || nfcModelList.get(position).getDesignation() == null) {
                        tvPersonProfile.setVisibility(View.GONE);
                    }
                }catch (Exception e){}

                try {
                if (nfcModelList.get(position).getEmail().trim().equals("") || nfcModelList.get(position).getEmail().equals("null")
                        || nfcModelList.get(position).getEmail() == null) {
                    tvPersonWebsite.setVisibility(View.GONE);
                }
                }catch (Exception e){}

                try {
                if (nfcModelList.get(position).getAddress().trim().equals("") || nfcModelList.get(position).getAddress().equalsIgnoreCase("null")
                        || nfcModelList.get(position).getAddress() == null) {
                    tvPersonAddress.setVisibility(View.INVISIBLE);
                }
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    if (nfcModelList.get(position).getPh_no().trim().equals("") || nfcModelList.get(position).getPh_no().equals("null")
                            || nfcModelList.get(position).getPh_no() == null) {
                        tvPersonMobile.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
               /* if (nfcModelList.get(position).getMob_no().equals("") || nfcModelList.get(position).getMob_no().equals("null")
                        || nfcModelList.get(position).getMob_no() == null) {
                    tvPersonMobile.setVisibility(View.GONE);
                }*/
            }
            catch (Exception e){
                e.printStackTrace();
            }


            String name = nfcModelList.get(position).getName();
            kept = name.substring(0, name.indexOf(" "));
            remainder = name.substring(name.indexOf(" ") + 1, name.length());
            tvPersonName.setText(kept);
            tvPersonNameLast.setText(remainder);
            tvPersonProfile.setText(nfcModelList.get(position).getDesignation());
            tvPersonWebsite.setText("E : " + nfcModelList.get(position).getEmail());
            if (nfcModelList.get(position).getAddress().toString().trim().equals("")){
                tvPersonAddress.setVisibility(View.GONE);
            }else {
                tvPersonAddress.setText("A : " + nfcModelList.get(position).getAddress());
            }
           // tvPersonContact.setText("T : " + nfcModelList.get(position).getPh_no());
            tvPersonMobile.setText("M : " + nfcModelList.get(position).getPh_no());
        }
        else
        {
            imageView.setTag(position);
            imageView.setVisibility(View.VISIBLE);
            defaultCard.setVisibility(View.GONE);
            //imageView.setImageResource(nfcModelList.get(position).getCard_front());
            ImageLoader.getInstance().displayImage(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList.get(position).getCard_front(), imageView, options, animateFirstListener);
           // Picasso.with(mContext).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList.get(position).getCard_front()).skipMemoryCache().into(imageView);
        }

        pos = holder.getAdapterPosition();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



        defaultCard.setOnClickListener(new View.OnClickListener() {
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
       /* nfcModelList.clear();

        if(charText.length() == 0)
        {
            nfcModelList.addAll(nfcModelListFilter);
            try {
                // List1Fragment.myPager.notifyDataSetChanged();
                List1Fragment.nfcModel.clear();
                // List1Fragment.allTags = db.getActiveNFC();
                //  nfcModelList.clear();
                List1Fragment.GetData(mContext);
            } catch (Exception e){

            }
        }
        else
        {
            List1Fragment.allTags.clear();
            for(FriendConnection md : nfcModelListFilter)
            {
                if(md.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nfcModelList.add(md);
                    try {
                        //   List1Fragment.myPager.notifyDataSetChanged();
                        List1Fragment.nfcModel.clear();
                        List1Fragment.allTags.add(md);
                        //  nfcModelList.clear();
                        List1Fragment.GetData(mContext);
                    } catch (Exception e){

                    }
                }
                CardsActivity.setActionBarTitle("Cards - "+nfcModelList.size());
            }
        }
        notifyDataSetChanged();*/
    }

}
