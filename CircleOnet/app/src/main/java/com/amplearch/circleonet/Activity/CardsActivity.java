package com.amplearch.circleonet.Activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Adapter.CardSwipe;
import com.amplearch.circleonet.Fragments.CardsFragment;
import com.amplearch.circleonet.Fragments.ConnectFragment;
import com.amplearch.circleonet.Fragments.EventsFragment;
import com.amplearch.circleonet.Fragments.ProfileFragment;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.Utils.CarouselEffectTransformer;
import com.amplearch.circleonet.Utils.CustomViewPager;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Fragments.List2Fragment;
import com.amplearch.circleonet.Fragments.List3Fragment;
import com.amplearch.circleonet.Fragments.List4Fragment;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.ZoomOutPageTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutTranformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;

public class CardsActivity extends NfcActivity {

    public static CustomViewPager mViewPager;
    TabLayout tabLayout;
    ImageView imgDrawer, imgLogo;
    private int actionBarHeight;
    static TextView textView;
    public static int position = 0, nested_position = 0;
    DatabaseHelper db;
    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    private Date location;
    private int currentPage;
    int cardCount = 0 ;
    private NfcAdapter mNfcAdapter;
    Tag tag;
    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        /*SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss a");
        String date1 = format.format(Date.parse(stringDate));

        Toast.makeText(getApplicationContext(), "Time: " + date1, Toast.LENGTH_LONG).show();
*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt("viewpager_position");
            nested_position = extras.getInt("nested_viewpager_position");
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);

        if (mNfcAdapter == null || mNfcAdapter.isEnabled()== false) {
            // adapter exists and is enabled.
            //txtMessage.setVisibility(View.VISIBLE);
        }
        else {
           // txtMessage.setVisibility(View.GONE);
            // handleIntent(getIntent());
        }
        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       // mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
       /* mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

            }
        });*/

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
                if (tab.getPosition() == 3){
                    getSupportActionBar().hide();
                }
                int  i = tab.getPosition();
                if ( i == 0 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else if ( i == 1 ){
                 //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);

                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));

                }
                else if ( i == 2 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else if ( i == 3 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon4b);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Profile");
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int  i = tab.getPosition();
                if ( i == 0 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon1);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Cards");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
                else if ( i == 1 ){
                    //   View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
                    // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);

                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon2);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Connect");
                    textView.setTextColor(getResources().getColor(R.color.unselected));

                }
                else if ( i == 2 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon3);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Events");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
                else if ( i == 3 ){
                    View view = tab.getCustomView();
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setImageResource(R.drawable.ic_icon4);
                    TextView textView = (TextView) view.findViewById(R.id.txtTab);
                    textView.setText("Profile");
                    textView.setTextColor(getResources().getColor(R.color.unselected));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

               // createTabIcons();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    CardsFragment.mViewPager.setCurrentItem(nested_position);
                    getSupportActionBar().show();
                    setActionBarTitle("Cards - "+cardCount);
                    setActionBarRightImage(R.drawable.ic_drawer);
                } else if (position == 1) {
                    getSupportActionBar().show();
                    setActionBarTitle("Connect");
                    setActionBarRightImage(R.drawable.ic_dehaze_black_24dp);
                } else if (position == 2) {
                    getSupportActionBar().show();
                    setActionBarTitle("Events");
                    setActionBarRightImage(R.drawable.ic_drawer);
                } else if (position == 3) {
                    //getSupportActionBar().hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();

                TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }

                showDialog(CardsActivity.this, 0, actionBarHeight);
            }
        });

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();
                if (pos == 0) {
                    Intent intent = new Intent(getApplicationContext(), SortAndFilterOption.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
                else if (pos == 2) {
                    Intent intent = new Intent(getApplicationContext(), EventsSelectOption.class);
                    startActivity(intent);
                }
            }
        });
    }


    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {
            db = new DatabaseHelper(getApplicationContext());
           /* List<NFCModel> allTags = db.getAllNFC();
            for (NFCModel tag : allTags) {
                Log.d("StoreLocation Name", tag.getCard_front().toString());
                // Toast.makeText(getApplicationContext(), tag.getName() + " " + tag.getCard_front().toString() + " " + tag.getActive(), Toast.LENGTH_LONG).show();

            }*/

           // db.getAllNFC();
            //SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            //- db.onCreate(sqLiteDatabase);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            final ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar);
            getSupportActionBar().setShowHideAnimationEnabled(false);
            textView = (TextView) findViewById(R.id.mytext);
            cardCount = db.getActiveNFCCount();
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // textView.setText("Cards 256");
            // Set up the ViewPager with the sections adapter.
            mViewPager = (CustomViewPager) findViewById(R.id.container);
            mViewPager.setOffscreenPageLimit(4);
            imgDrawer = (ImageView) findViewById(R.id.drawer);
            imgLogo = (ImageView) findViewById(R.id.imgLogo);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setPagingEnabled(false);
          //  mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        }
        @Override
        protected Void doInBackground(Void... params) {
            setupTabIcons();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            getSupportActionBar().setShowHideAnimationEnabled(false);
            if (position == 0) {
                getSupportActionBar().show();
                setActionBarTitle("Cards - "+cardCount);
                setActionBarRightImage(R.drawable.ic_drawer);
            } else if (position == 1) {
                getSupportActionBar().show();
                setActionBarTitle("Connect");
                setActionBarRightImage(R.drawable.ic_dehaze_black_24dp);
            } else if (position == 2) {
                getSupportActionBar().show();
                setActionBarTitle("Events");
                setActionBarRightImage(R.drawable.ic_drawer);
            } else if (position == 3) {
               // getSupportActionBar().hide();
            }
            mViewPager.setCurrentItem(position, false);
            if (nested_position != 0) {
                CardsFragment.mViewPager.setCurrentItem(nested_position);
            }
        }

    }

   /* private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab1, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab2, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab3, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabThree1 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_view, null);
        tabThree1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab4, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabThree1);
    }*/

    public void showDialog(Context context, int x, int y){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate
        final Dialog dialog  = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.listview_with_text_image);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout lnrMyAcc = (LinearLayout) dialog.findViewById(R.id.lnrMyAcc);
       /* lnrMyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });*/

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.x = x;
        lp.y = y;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public static void setActionBarTitle(String title){
        textView.setText(title);
    }

    public void setActionBarRightImage(int image){
        imgDrawer.setImageResource(image);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
               // getSupportActionBar().show();
               // setActionBarTitle("Connect");
                return new CardsFragment();
            } else if (position == 1) {
              //  getSupportActionBar().show();
               // setActionBarTitle("Cards");
                return new ConnectFragment();
            }
            else if (position == 2) {
               // getSupportActionBar().show();
                //setActionBarTitle("Connect");
                return new EventsFragment();
            }
            else if (position == 3) {
              //  getSupportActionBar().hide();
              //  setActionBarTitle("Events");
                return new ProfileFragment();
            }
            else {
              //  getSupportActionBar().show();
              //  setActionBarTitle("Cards");
                return new List1Fragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return getString(R.string.app_name);
                case 1:
                    return getString(R.string.hello_blank_fragment);
            }*/
            return null;
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.tab1,
                R.drawable.tab2,
                R.drawable.tab3,
                R.drawable.tab4
               // R.drawable.ic_tab_contacts
        };

        /*View view = getLayoutInflater().inflate(R.layout.tab_view, null);
        for (int i = 0; i < 3; i++) {
            view.findViewById(R.id.icon).setBackgroundResource(tabIcons[i]);
            tabLayout.addTab(tabLayout.newTab().setCustomView(view));
        }
*/
       /* tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);*/


        View view1 = getLayoutInflater().inflate(R.layout.tab_view, null);
       // view1.findViewById(R.id.icon).set(R.drawable.ic_icon1);
        ImageView imageView = (ImageView) view1.findViewById(R.id.icon);
        imageView.setImageResource(R.drawable.ic_icon1b);
        TextView textView = (TextView) view1.findViewById(R.id.txtTab);
        textView.setText("Cards");
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view1));


        View view2 = getLayoutInflater().inflate(R.layout.tab_view, null);
        //view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon2);
        ImageView imageView1 = (ImageView) view2.findViewById(R.id.icon);
        imageView1.setImageResource(R.drawable.ic_icon2);
        TextView textView1 = (TextView) view2.findViewById(R.id.txtTab);
        textView1.setText("Connect");
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view2));


        View view3 = getLayoutInflater().inflate(R.layout.tab_view, null);
       // view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon3);
        ImageView imageView2 = (ImageView) view3.findViewById(R.id.icon);
        imageView2.setImageResource(R.drawable.ic_icon3);
        TextView textView2 = (TextView) view3.findViewById(R.id.txtTab);
        textView2.setText("Events");
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.tab_view, null);
      //  view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_icon4);
        ImageView imageView3 = (ImageView) view4.findViewById(R.id.icon);
        imageView3.setImageResource(R.drawable.ic_icon4);
        TextView textView3 = (TextView) view4.findViewById(R.id.txtTab);
        textView3.setText("Profile");
       // tabLayout.addTab(tabLayout.newTab().setCustomView(view4));
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);
        tabLayout.getTabAt(3).setCustomView(view4);
    }

   /* class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!done) {
            NdefMessage[] msgs = null;

            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMsgs != null) {
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }

                    byte[] payload = msgs[0].getRecords()[0].getPayload();

                    String message = new String(payload);
                /* 把tag的資訊放到textview裡面 */
                    // mEtMessage.setText(new String(payload));
                    done = true;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    message = message.substring(1, message.length());
                    Boolean aBoolean = db.verification(message);
                    if (aBoolean == true) {
                        Toast.makeText(getApplicationContext(), "Already Activated", Toast.LENGTH_LONG).show();
                    } else {
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = format.format(Date.parse(stringDate));

                        int i = db.makeCardActive(message, date1);
                        if (i == 1) {
                            Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                            try {
                                List2Fragment.gridAdapter.notifyDataSetChanged();
                                List2Fragment.allTags = db.getActiveNFC();
                                List2Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List2Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }

                            try {
                                List3Fragment.gridAdapter.notifyDataSetChanged();
                                List3Fragment.allTags = db.getActiveNFC();
                                List3Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List3Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }
                            try {
                                List4Fragment.gridAdapter.notifyDataSetChanged();
                                List4Fragment.allTags = db.getActiveNFC();
                                List4Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List4Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }

                            try {
                                //List1Fragment.myPager.notifyDataSetChanged();
                                List1Fragment.allTags = db.getActiveNFC();
                                List1Fragment.nfcModel.clear();
                                //  nfcModelList.clear();
                                List1Fragment.GetData(getApplicationContext());
                            } catch (Exception e) {

                            }
                        } else if (i == 11) {
                            Toast.makeText(getApplicationContext(), "Data Does not exists in Database..", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            if (mNfcAdapter != null)
                mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

        }
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent
     *         containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);


        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag == null){
            Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
            //textViewInfo.setText("tag == null");
        }else {
            String tagInfo = tag.toString() + "\n";
            String id = "";
            tagInfo += "\nTag Id: \n";
            byte[] tagId = tag.getId();
            tagInfo += "length = " + tagId.length + "\n";
            for (int i = 0; i < tagId.length; i++) {
                tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                // id += Integer.toHexString(tagId[i] & 0xFF) + " ";
            }
            id = bytesToHex(tagId);
            /*Boolean aBoolean = db.verification(id);
            if (aBoolean == true)
            {
                Toast.makeText(getApplicationContext(), "already exists in database", Toast.LENGTH_LONG).show();
            }else {

                int i = db.makeCardActive(id);
                if (i == 1){
                    Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_LONG).show();
                }
              //  Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
               // notifyAll();

                if (mViewPager.getCurrentItem() == 0){
                    Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                    // you pass the position you want the viewpager to show in the extra,
                    // please don't forget to define and initialize the position variable
                    // properly
                    go.putExtra("viewpager_position", 0);
                    go.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());

                    startActivity(go);
                    finish();
                    //Toast.makeText(getApplicationContext(), String.valueOf(CardsFragment.mViewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                    startActivity(go);
                    finish();
                }
            }*/

               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


           /* List<NFCModel> modelList = db.getNFCbyTag(id);
            image = new ArrayList<>();
            try {

                if (modelList != null){

                    for (NFCModel tag1 : modelList) {
                        // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                        Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                        txtName.setText(tag1.getName());
                        txtCompany.setText(tag1.getCompany());
                        txtWebsite.setText(tag1.getWebsite());
                        txtEmail.setText(tag1.getEmail());
                        txtPH.setText(tag1.getPh_no());
                        txtWork.setText(tag1.getWork_no());
                        txtMob.setText(tag1.getMob_no());
                        txtAddress.setText(tag1.getAddress());
                        txtRemark.setText(tag1.getRemark());
                        image.add(tag1.getCard_front());
                        image.add(tag1.getCard_back());
                        myPager = new CardSwipe(getApplicationContext(), image);
                        mViewPager.setClipChildren(false);
                        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                        mViewPager.setOffscreenPageLimit(3);
                        mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer


                        mViewPager.setAdapter(myPager);
                    }
                }

            }catch (Exception e){

            }*/

         //   Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            // callData(id);
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values()) {
                Boolean aBoolean = db.verification(data);
                if (aBoolean == true)
                {
                    Toast.makeText(getApplicationContext(), "Already Activated", Toast.LENGTH_LONG).show();
                }else {
                    Date date = new Date();
                    String stringDate = DateFormat.getDateTimeInstance().format(date);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = format.format(Date.parse(stringDate));

                    int i = db.makeCardActive(data, date1);
                    if (i == 1){
                        Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                        try {
                            List2Fragment.gridAdapter.notifyDataSetChanged();
                            List2Fragment.allTags = db.getActiveNFC();
                            List2Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List2Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }

                        try {
                            List3Fragment.gridAdapter.notifyDataSetChanged();
                            List3Fragment.allTags = db.getActiveNFC();
                            List3Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List3Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }
                        try {
                            List4Fragment.gridAdapter.notifyDataSetChanged();
                            List4Fragment.allTags = db.getActiveNFC();
                            List4Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List4Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }

                        try {
                            //List1Fragment.myPager.notifyDataSetChanged();
                            List1Fragment.allTags = db.getActiveNFC();
                            List1Fragment.nfcModel.clear();
                            //  nfcModelList.clear();
                            List1Fragment.GetData(getApplicationContext());
                        } catch (Exception e){

                        }
                    }
                    else if (i == 11){
                        Toast.makeText(getApplicationContext(), "Data Does not exists in Database..", Toast.LENGTH_LONG).show();
                    }
                    //  Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
                    // notifyAll();

                    /*if (mViewPager.getCurrentItem() == 0){
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);

                        // you pass the position you want the viewpager to show in the extra,
                        // please don't forget to define and initialize the position variable
                        // properly
                        go.putExtra("viewpager_position", 0);
                        go.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());

                        startActivity(go);
                        finish();
                        //Toast.makeText(getApplicationContext(), String.valueOf(CardsFragment.mViewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent go = new Intent(getApplicationContext(),CardsActivity.class);
                        startActivity(go);
                        finish();
                    }*/
                }
              //  Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();

    }
}