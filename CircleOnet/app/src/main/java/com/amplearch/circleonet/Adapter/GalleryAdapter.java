package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.EmbossMaskFilter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Fragments.CardsFragment;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    ArrayList<FriendConnection> nfcModelListFilter = new ArrayList<>();
    private RecyclerView.ViewHolder holder;
    public static ImageView imageView ;
    public static int posi = 0;
    DatabaseHelper db;
    RelativeLayout defaultCard;

    private static TextView tvPersonName, tvPersonProfile, tvPersonWebsite, tvPersonAddress, tvPersonContact;

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
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            Intent intent = new Intent(mContext, CardDetail.class);
            intent.putExtra("tag_id", nfcModelList.get(position).getNfc_tag());
            mContext.startActivity(intent);
        }
    }

    public GalleryAdapter(Context applicationContext, ArrayList<FriendConnection> nfcModel)
    {
        mContext = applicationContext ;
        this.nfcModelList = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);
    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_view, parent, false);
        db = new DatabaseHelper(mContext);
        GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

        this.gestureDetector1= new GestureDetector(mContext, gestureListener);

        this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);

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
            try {
                if (nfcModelList.get(position).getName().equals("")) {
                    tvPersonName.setVisibility(View.GONE);
                }
                if (nfcModelList.get(position).getDesignation().equals("")) {
                    tvPersonProfile.setVisibility(View.GONE);
                }
                if (nfcModelList.get(position).getWebsite().equals("")) {
                    tvPersonWebsite.setVisibility(View.GONE);
                }
                if (nfcModelList.get(position).getAddress().equals("")) {
                    tvPersonAddress.setVisibility(View.GONE);
                }
                if (nfcModelList.get(position).getPh_no().equals("")) {
                    tvPersonContact.setVisibility(View.GONE);
                }
            }catch (Exception e){}
            tvPersonName.setText(nfcModelList.get(position).getName());
            tvPersonProfile.setText(nfcModelList.get(position).getDesignation());
            tvPersonWebsite.setText(nfcModelList.get(position).getWebsite());
            tvPersonAddress.setText(nfcModelList.get(position).getAddress());
            tvPersonContact.setText(nfcModelList.get(position).getPh_no());
        }
        else
        {
            imageView.setTag(position);
            imageView.setVisibility(View.VISIBLE);
            defaultCard.setVisibility(View.GONE);
            //imageView.setImageResource(nfcModelList.get(position).getCard_front());
            Picasso.with(mContext).load("http://circle8.asia/App_ImgLib/Cards/"+nfcModelList.get(position).getCard_front()).into(imageView);
        }

        pos = holder.getAdapterPosition();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CardDetail.class);
                intent.putExtra("tag_id", nfcModelList.get(position).getNfc_tag());
                mContext.startActivity(intent);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                posi = position;
                gestureDetector1.onTouchEvent(event);
                return true;
            }
        });

        defaultCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                posi = position;
                gestureDetector1.onTouchEvent(event);
                return true;
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


    class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            //  Toast.makeText(getContext(), "onDown", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // Toast.makeText(context, "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //   Log.e(TAG, "onSingleTapUp");

          /*  pos = (int) holder.itemView.getTag();
            // final_position = List1Fragment.viewPager.getCurrentItem();
            Intent intent = new Intent(mContext, CardDetail.class);
            intent.putExtra("tag_id", nfcModelList.get(pos).getNfc_tag());
            mContext.startActivity(intent);*/

            Intent intent = new Intent(mContext, CardDetail.class);
            intent.putExtra("tag_id", nfcModelList.get(posi).getNfc_tag());
            mContext.startActivity(intent);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            //Log.e(TAG, "onScroll");
            // position = Integer.parseInt(imageView.getTag().toString());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // Toast.makeText(context, "onLongPress", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Toast.makeText(getContext(), "onFling", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e1.getX() + ":" + e1.getY() + "  " + e2.getX() + ":" + e2.getY());
            boolean result = true;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // onSwipeRight();
                        } else {
                            // onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //Toast.makeText(getContext(), "Down", Toast.LENGTH_LONG).show();
                            List1Fragment.lnrSearch.setVisibility(View.VISIBLE);
                            List1Fragment.line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        } else {
                            //  Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
                            List1Fragment.lnrSearch.setVisibility(View.VISIBLE);
                            List1Fragment.line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }


    }

    class MyOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //  Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTapEvent", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX() + ":" + e.getY());
            //  Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }
    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList.clear();

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


        notifyDataSetChanged();
    }

}
