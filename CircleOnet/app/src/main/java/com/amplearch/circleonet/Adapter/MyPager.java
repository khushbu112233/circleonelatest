package com.amplearch.circleonet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Fragments.CardsFragment;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Fragments.List2Fragment;
import com.amplearch.circleonet.Fragments.List3Fragment;
import com.amplearch.circleonet.Fragments.List4Fragment;
import com.amplearch.circleonet.Gesture.OnSwipeTouchListener;
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

public class MyPager extends PagerAdapter implements GestureDetector.OnGestureListener
{
    Context context ;
    /*ArrayList<byte[]> image, image1 ;
    ArrayList<String> tag_id;*/
    DatabaseHelper db;
    private GestureDetector gd;
    private int layoutResourceId;
    //newly added
    View view;
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();
    private GestureDetectorCompat mDetector;
    private GestureDetector gestureDetector1;
    int final_position = 0;
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
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        view = LayoutInflater.from(context).inflate(R.layout.cardview_list, null);
        try
        {
            gd = new GestureDetector(context, this);
            db = new DatabaseHelper(context);
            ImageView imageView = (ImageView)view.findViewById(R.id.ivImages);
            ImageView imageView1 = (ImageView)view.findViewById(R.id.ivImages1);
            mDetector = new GestureDetectorCompat(context, new MyGestureListener());
            final_position = position;
            view.setTag("" + position);
            Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);
           // ImageView image = (ImageView) findViewById(R.id.imageView1);


            GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
            GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

            this.gestureDetector1= new GestureDetector(context, gestureListener);

            this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);

            imageView.setImageBitmap(bmp);


          //  byte[] imgbytes = Base64.decode(image.get(position), Base64.DEFAULT);
          //  Bitmap bitmap = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_back(), 0, nfcModelList.get(position).getCard_back().length);

          //  imageView.setImageBitmap(bitmap);
            imageView1.setImageBitmap(bitmap1);
           // Glide.with(context).load(image.get(position)).into(imageView);
          //  Glide.with(context).load(image1.get(position)).into(imageView1);
//            Glide.with(context).load(image[position]).into(imageCover);


            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent me) {
                    return gestureDetector1.onTouchEvent(me);
                }
            });

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //this will log the page number that was click
                    //Log.i("TAG", "This page was clicked: " + pos);

                    Intent intent = new Intent(context, CardDetail.class);
                    intent.putExtra("tag_id", nfcModelList.get(position).getNfc_tag());
                    context.startActivity(intent);
                }
            });

            /*view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gd.onTouchEvent(event);
                }
            });
            */


            /*view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mDetector.onTouchEvent(event);
                    return mDetector.onTouchEvent(event);
                }
            });*/

            /*view.setOnTouchListener(new OnSwipeTouchListener(context) {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Toast.makeText(context, "Tap", Toast.LENGTH_SHORT).show();
                    String itemTag = view.getTag().toString();
                    int itemPosition = Integer.parseInt(itemTag);
                    Toast.makeText(context, String.valueOf(itemPosition), Toast.LENGTH_LONG).show();
                    return super.onTouch(view, motionEvent);
                }

                @Override
                public void onSwipeDown() {
                    Toast.makeText(context, "Down", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSwipeLeft() {
                    Toast.makeText(context, "Left", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSwipeUp() {
                    Toast.makeText(context, "Up", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSwipeRight() {
                    Toast.makeText(context, "Right", Toast.LENGTH_SHORT).show();
                }
            });
*/
            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        /*@Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            Toast.makeText(context, "Down", Toast.LENGTH_LONG).show();
            return true;
        }*/

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
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
                            Toast.makeText(context, "Down", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Up", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(context, "Tap", Toast.LENGTH_LONG).show();
            return super.onSingleTapConfirmed(e);
        }
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
            Intent intent = new Intent(context, CardDetail.class);
            intent.putExtra("tag_id", nfcModelList.get(final_position).getNfc_tag());
            context.startActivity(intent);

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
                            List1Fragment.lnrSearch.setVisibility(View.GONE);
                            List1Fragment.line.setVisibility(View.GONE);
                            CardsFragment.tabLayout.setVisibility(View.GONE);
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
               // List1Fragment.myPager.notifyDataSetChanged();
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
                     //   List1Fragment.myPager.notifyDataSetChanged();
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

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        //Defining Sensitivity
        float sensitivity = 50;
        //Swipe Up Check
        if(e1.getY() - e2.getY() > sensitivity){
            //Setting Image Resource to Up_Arrow on Swipe Up
            Toast.makeText(context, "UP", Toast.LENGTH_SHORT).show();
            return true;
        }
        //Swipe Down Check
        else if(e2.getY() - e1.getY() > sensitivity){
            //Setting Image Resource to Down_Arrow on Swipe Down
            Toast.makeText(context, "Down", Toast.LENGTH_SHORT).show();
            return true;
        }
        //Swipe Left Check
        else if(e1.getX() - e2.getX() > sensitivity){
            //Setting Image Resource to Left_Arrow on Swipe Left
          //  img.setImageResource(R.drawable.left_arrow);
            return true;
        }
        //Swipe Right Check
        else if(e2.getX() - e1.getX() > sensitivity){
            //Setting Image Resource to Right_Arrow on Swipe Right
          //  img.setImageResource(R.drawable.right_arrow);
            return true;
        }
        else{
            //If some error occurrs, setting again to Default_Image (Actually it will never happen in this case)
          //  img.setImageResource(R.drawable.default_img);
            return true;
        }
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "tap", Toast.LENGTH_SHORT).show();
        return true;
    }
}
