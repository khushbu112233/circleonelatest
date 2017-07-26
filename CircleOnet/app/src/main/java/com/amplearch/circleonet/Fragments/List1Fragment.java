package com.amplearch.circleonet.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Adapter.GalleryAdapter;
import com.amplearch.circleonet.Adapter.GalleryAdapter1;
import com.amplearch.circleonet.Adapter.GridViewAdapter;
import com.amplearch.circleonet.Gesture.OnSwipeTouchListener;
import com.amplearch.circleonet.Gesture.SwipeGestureDetector;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.Adapter.MyPager;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CustomViewPager;
import com.amplearch.circleonet.ZoomOutPageTransformer;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class List1Fragment extends Fragment
{
   // private ArrayList<Integer> imageFront = new ArrayList<>();
   // private ArrayList<Integer> imageBack = new ArrayList<>();
  //  public static MyPager myPager ;
    DatabaseHelper db ;
    private GestureDetector gestureDetector1;
    FrameLayout frameList1;
    LinearLayout lnrList;
    /*ArrayList<byte[]> imgf;
    ArrayList<byte[]> imgb;
    ArrayList<String> tag_id;*/
    public static RelativeLayout lnrSearch;
    public static View line;
    private String DEBUG_TAG = "gesture";
  //  private GestureDetector gestureDetector;
  //  private View.OnTouchListener gestureListener;

    public static List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    public static ArrayList<NFCModel> nfcModel ;
    ViewConfiguration vc;
    private int mTouchSlop;
    FrameLayout frame;

    View view;

    private String TAG = CardsActivity.class.getSimpleName();
    public static ArrayList<Integer> images;
    public static ArrayList<Integer> images1;
    public static GalleryAdapter mAdapter;
    public static GalleryAdapter1 mAdapter1;
    public static RecyclerView recyclerView1, recyclerView2;
    public static CarouselLayoutManager manager1, manager2;
    private int draggingView = -1;
    RelativeLayout rlt;

    public List1Fragment()
    {
        // Required empty public constructor
    }

    @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_list1, container, false);
      //  new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        rlt = (RelativeLayout) view.findViewById(R.id.rlt);
        vc = ViewConfiguration.get(view.getContext());
        // frameList1 = (FrameLayout) view.findViewById(R.id.frameList1);
        mTouchSlop = vc.getScaledTouchSlop();
        recyclerView1 = (RecyclerView) view.findViewById(R.id.list_horizontal1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.list_horizontal2);
        lnrList = (LinearLayout) view.findViewById(R.id.lnrList);
        frame = (FrameLayout) view.findViewById(R.id.frame);
        db = new DatabaseHelper(getContext());
        //viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        initRecyclerView1(recyclerView1,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter ) ;
        initRecyclerView2(recyclerView2,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1 ) ;

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);

        lnrSearch.setVisibility(View.INVISIBLE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);
        GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

        gestureDetector1= new GestureDetector(getContext(), gestureListener);

        gestureDetector1.setOnDoubleTapListener(doubleTapListener);
        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();
        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        recyclerView1.addOnScrollListener(scrollListener);
        recyclerView2.addOnScrollListener(scrollListener);
      //  viewPager.setClipChildren(false);
      //  viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
       // viewPager.setOffscreenPageLimit(5);
       // viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext())); // Set transformer


        frame.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                gestureDetector1.onTouchEvent(me);
                recyclerView1.requestFocus();
                recyclerView1.dispatchTouchEvent(me); // don't cause scrolling
                //recyclerView1.dispatchTouchEvent(me); // don't cause scrolling? Alternative solutoin?

                recyclerView2.requestFocus();
               // recyclerView2.dispatchTouchEvent(mBackupTouchDownEvent); // don't cause scrolling
                //recyclerView2.dispatchTouchEvent(me);
                return true;
            }
        });

        /*frame.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                recyclerView1.addOnScrollListener(scrollListener);
                recyclerView2.addOnScrollListener(scrollListener);
            }
        });*/
       /* recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector1.onTouchEvent(event);
            }
        });*/
        /*recyclerView1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });*/


        /*recyclerView2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
*/
        /*view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

// Start the animation
        view.animate()
                .translationY(view.getHeight())
                .alpha(1.0f);

        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });*/

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

       /* imgf = new ArrayList<byte[]>();
        imgb = new ArrayList<byte[]>();
        tag_id = new ArrayList<String>();*/

        //myPager = new MyPager(getContext(), imgf, imgb, tag_id);
        //viewPager.setAdapter(myPager);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() <= 0)
                {
                    nfcModel.clear();
                    allTags = db.getActiveNFC();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    mAdapter.Filter(text);
                 //   mAdapter1.Filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      /*  viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private int index = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        return view;
    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressBar;
        @Override
        protected void onPreExecute() {

            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("Loading Data..");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseHelper(getContext());
            nfcModel = new ArrayList<>();
            allTags = db.getActiveNFC();

            images = new ArrayList<>();
            images1 = new ArrayList<>();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData(getContext());
            progressBar.dismiss();
        }

    }



    public static void initRecyclerView1(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter mAdapter)
    {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager1 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());

    }


   /* private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {


        }
        @Override
        protected Void doInBackground(Void... params) {


            images = new ArrayList<>();
            images1 = new ArrayList<>();
            GetData(getContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recyclerView1.addOnScrollListener(scrollListener);
            recyclerView2.addOnScrollListener(scrollListener);

        }

    }
*/

    public static void initRecyclerView2(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter1 mAdapter)
    {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager2 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1000;

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
            Intent intent = new Intent(getContext(), CardDetail.class);
            //intent.putExtra("tag_id", nfcModelList.get(final_position).getNfc_tag());
            // context.startActivity(intent);

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
                       /* recyclerView1.addOnScrollListener(scrollListener);
                        recyclerView2.addOnScrollListener(scrollListener);*/
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
                            lnrSearch.setVisibility(View.INVISIBLE);
                            line.setVisibility(View.GONE);
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

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        public int y=0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView1 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 1;
               // GalleryAdapter.position = Integer.parseInt(GalleryAdapter.imageView.getTag().toString());
            } else if (recyclerView2 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 2;
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
           // y=dy;
            if (draggingView == 1 && recyclerView == recyclerView1) {
                recyclerView2.scrollBy(dx, dy);
            } else if (draggingView == 2 && recyclerView == recyclerView2) {
                recyclerView1.scrollBy(dx, dy);
            }
           /* if(dx>0)
            {
                Toast.makeText(getContext(), "Scrolled Right", Toast.LENGTH_LONG).show();
                System.out.println("Scrolled Right");

            }
            else if(dx < 0)
            {
                Toast.makeText(getContext(), "Scrolled Left", Toast.LENGTH_LONG).show();
                System.out.println("Scrolled Left");

            }
            else {
                Toast.makeText(getContext(), "No Horizontal Scrolled", Toast.LENGTH_LONG).show();
                System.out.println("No Horizontal Scrolled");
            }

            if(dy>0)
            {
                Toast.makeText(getContext(), "Scrolled Downwards", Toast.LENGTH_LONG).show();
                System.out.println("Scrolled Downwards");
            }
            else if(dy < 0)
            {
                System.out.println("Scrolled Upwards");
                Toast.makeText(getContext(), "Scrolled Upwards", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), "No Vertical Scrolled", Toast.LENGTH_LONG).show();
                System.out.println("No Vertical Scrolled");
            }*/
        }
    };

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            try {
                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    swipe += "Swipe Right\n";
                } else {
                    swipe += "\n";
                }

                if ((e1.getY() - e2.getY()) > sensitvity) {
                    swipe += "Swipe Up\n";
                    lnrSearch.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                } else {
                    swipe += "\n";
                }

                //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

                return super.onFling(e1, e2, velocityX, velocityY);
            }
            catch (Exception e ){
                return true;
            }
        }
    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel.clear();
        GetData(getContext());
    }

    public static void GetData(Context context)
    {
        images = new ArrayList<>();
        images1 = new ArrayList<>();
        //newly added
        nfcModel.clear();
        for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setCard_back(reTag.getCard_back());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            images.add(reTag.getCard_front());
            images1.add(reTag.getCard_back());
            nfcModel.add(nfcModelTag);
        }

        Collections.sort(nfcModel, new Comparator<NFCModel>() {
            public int compare(NFCModel o1, NFCModel o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        mAdapter = new GalleryAdapter(context, nfcModel);
        mAdapter1 = new GalleryAdapter1(context, nfcModel);
        mAdapter.notifyDataSetChanged();
        mAdapter1.notifyDataSetChanged();

        initRecyclerView1(recyclerView1,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter ) ;
        initRecyclerView2(recyclerView2,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1 ) ;

       // myPager = new MyPager(context, R.layout.cardview_list, nfcModel);
        //viewPager.setAdapter(myPager);
      //  myPager.notifyDataSetChanged();

        //gridAdapter.setMode(Attributes.Mode.Single);

    }

}