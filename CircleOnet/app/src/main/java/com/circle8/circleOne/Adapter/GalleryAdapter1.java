package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Fragments.CardsFragment;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_view, parent, false);

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
            Picasso.with(mContext).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList.get(position).getCard_back()).into(imageView);

        }
        defaultCard1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                posi = position;
                gestureDetector1.onTouchEvent(event);
                return true;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CardDetail.class);
                intent.putExtra("profile_id", nfcModelList.get(position).getProfile_id());
                intent.putExtra("DateInitiated",nfcModelList.get(position).getDateInitiated());
                mContext.startActivity(intent);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                posi = position;
                return gestureDetector1.onTouchEvent(me);
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

            // final_position = List1Fragment.viewPager.getCurrentItem();
            // Intent intent = new Intent(mContext, CardDetail.class);
            //intent.putExtra("tag_id", nfcModelList.get(final_position).getNfc_tag());
            // context.startActivity(intent);

            Intent intent = new Intent(mContext, CardDetail.class);
            intent.putExtra("profile_id", nfcModelList.get(posi).getProfile_id());
            intent.putExtra("DateInitiated",nfcModelList.get(posi).getDateInitiated());
            mContext.startActivity(intent);

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            //Log.e(TAG, "onScroll");
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
            boolean result = false;
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
