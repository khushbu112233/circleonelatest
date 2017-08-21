package com.amplearch.circleonet.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Adapter.List3Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.FriendConnection;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class List3Fragment extends Fragment
{

    private static ListView listView;
    public static List3Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<Integer> id;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;
    RelativeLayout lnrSearch;
    View line;
    private GestureDetector gestureDetector1;

    public static List<NFCModel> allTags ;
    public static List<FriendConnection> allTaggs ;

    //new asign value
    AutoCompleteTextView searchText ;
    public static ArrayList<NFCModel> nfcModel ;
    public static ArrayList<FriendConnection> nfcModel1 ;

    LoginSession session;
    String UserId = "";

    public List3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list3, container, false);

        db = new DatabaseHelper(getContext());
        id = new ArrayList<Integer>();
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.listViewType3);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);

        nfcModel = new ArrayList<>();
        nfcModel1 = new ArrayList<>();

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);

        //considering from database
//        allTags = db.getActiveNFC();

        allTags = new ArrayList<>();
        allTaggs = new ArrayList<>();

        new List3Fragment.HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetFriendConnection");

        GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();

        this.gestureDetector1= new GestureDetector(getContext(), gestureListener);

        this.gestureDetector1.setOnDoubleTapListener(doubleTapListener);

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        /*lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);*/

        /*allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            id.add(tag.getId());
            imgf.add(tag.getCard_front());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }
        gridAdapter = new List3Adapter(getContext(), R.layout.grid_list3_layout, imgf, desc, name, designation, id);
        listView.setAdapter(gridAdapter);*/



        /*listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                return gestureDetector1.onTouchEvent(me);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CardDetail.class);
                intent.putExtra("tag_id", nfcModel.get(position).getNfc_tag());
                getContext().startActivity(intent);
            }
        });

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });*/

        //retrive data
//        GetData(getContext());


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getContext(), "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

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
                    nfcModel1.clear();
                    GetData(getContext());
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));

                        nfcModelTag.setNfc_tag("en000000001");
                        allTaggs.add(nfcModelTag);
                        GetData(getContext());
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("numofrecords", "10" );
            jsonObject.accumulate("pageno", "1" );
            jsonObject.accumulate("userid", UserId );

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

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
                    /*lnrSearch.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);*/
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                   /* lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);*/
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

    GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);



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
            //Toast.makeText(getContext(), "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
           // Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
         //   Log.e(TAG, "onSingleTapUp");
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
           // Toast.makeText(getContext(), "onLongPress", Toast.LENGTH_LONG).show();
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
                            lnrSearch.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        } else {
                          //  Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
                            lnrSearch.setVisibility(View.GONE);
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
            //Toast.makeText(getContext(), "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
           // Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
           // Toast.makeText(getContext(), "onDoubleTapEvent", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX() + ":" + e.getY());
          //  Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }

/*
    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel1.clear();
        GetData(getContext());
    }
*/

    public static void GetData(Context context)
    {
        nfcModel1.clear();
        //newly added
       /* for(NFCModel reTag : allTags)
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
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());

            nfcModel.add(nfcModelTag);
        }*/

        for(FriendConnection reTag : allTaggs)
        {
            FriendConnection nfcModelTag = new FriendConnection();
//            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());

            nfcModel1.add(nfcModelTag);
        }

        gridAdapter = new List3Adapter(context, R.layout.grid_list3_layout, nfcModel1);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        CardsActivity.setActionBarTitle("Cards - "+nfcModel1.size());
        gridAdapter.setMode(Attributes.Mode.Single);

    }

}
