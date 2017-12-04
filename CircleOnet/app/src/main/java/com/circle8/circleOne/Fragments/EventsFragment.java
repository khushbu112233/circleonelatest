package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.EventDetail;
import com.circle8.circleOne.Activity.EventsSelectOption;
import com.circle8.circleOne.Adapter.EventsAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment
{
    public static Context mContext ;

    private static ListView listView;
    public static EventsAdapter gridAdapter;
    private TextView actionText;
    RelativeLayout lnrSearch;
    View line;

    static AutoCompleteTextView searchText ;
    ImageView imgSearch ;

    LoginSession session;
    String user_id ;

    static TextView tvEventInfo ;

    public static ArrayList<EventModel> eventModelArrayList = new ArrayList<>();

    private static RelativeLayout rlProgressDialog ;
    private static TextView tvProgressing ;
    private static ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;

    public static String eventSearchBy = "", eventSearchKey = "";
    public static Bitmap bitmapImg ;
    public static String imageUrl = "";

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

      //  CardsActivity.setActionBarTitle("Events");

        mContext = EventsFragment.this.getContext() ;

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        imgSearch = (ImageView)view.findViewById(R.id.imgSearch);
        tvEventInfo = (TextView)view.findViewById(R.id.tvEventInfo);
        rlProgressDialog = (RelativeLayout)view.findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)view.findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)view.findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)view.findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)view.findViewById(R.id.imgConnecting3) ;

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        listView = (ListView)view.findViewById(R.id.listEvents);

        if (EventsSelectOption.searchOpt.equals("AllEvents"))
        {
            callFirst();
        }
        else
        {
            new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
        }


//        callFirst();

       /* try
        {
            Intent eGet = getActivity().getIntent();
            String eventFilterOpt = eGet.getStringExtra("EventSearch");
            String eventSearchKeyOpt = eGet.getStringExtra("SearchText");

            if (eventFilterOpt.equals("AllEvents"))
            {
                Toast.makeText(getActivity(),"All Events",Toast.LENGTH_SHORT).show();
                eventModelArrayList.clear();
                try {gridAdapter.notifyDataSetChanged();}
                catch (Exception e){ e.printStackTrace(); }
                callFirst();
            }
            else if (eventFilterOpt.equals("CompanyAssociation"))
            {
                eventSearchBy = "Company";
                eventSearchKey = eventSearchKeyOpt ;
                Toast.makeText(getActivity(),"Company & Association",Toast.LENGTH_SHORT).show();
                eventModelArrayList.clear();
                try {gridAdapter.notifyDataSetChanged();}
                catch (Exception e){ e.printStackTrace(); }
                new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
            }
            else if (eventFilterOpt.equals("Industry"))
            {
                eventSearchBy = "Industry";
                eventSearchKey = eventSearchKeyOpt ;
                Toast.makeText(getActivity(),"Industry",Toast.LENGTH_SHORT).show();
                eventModelArrayList.clear();
                try {gridAdapter.notifyDataSetChanged();}
                catch (Exception e){ e.printStackTrace(); }
                new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), EventDetail.class);
                intent.putExtra("Event_ID", eventModelArrayList.get(position).getEvent_ID());
                startActivity(intent);
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                try
                {
                    if (s.length() <= 0)
                    {
                        EventsSelectOption.searchOpt = "ClearSearch";
                      /*  eventModelArrayList.clear();
                        try
                        {
                            gridAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                        tvEventInfo.setVisibility(View.GONE);

                        callFirst();
                    }

                   /* if (s.length() == 0)
                    {
                        EventsSelectOption.searchOpt = "ClearSearch";
                        tvEventInfo.setVisibility(View.GONE);

                    }*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               /* eventModelArrayList.clear();
                try
                {
                    gridAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }*/
                if (searchText.getText().toString().length() == 0)
                {
                    EventsSelectOption.searchOpt = "ClearSearch";
                    callFirst();
                }

                if (searchText.getText().toString().length() > 0)
                {
                    eventModelArrayList.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    eventSearchBy = "Name";
                    eventSearchKey = searchText.getText().toString();
                    new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
                }

               /* eventSearchBy = "Name";
                eventSearchKey = searchText.getText().toString();
                new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");*/
//                searchEvent();
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Utility.freeMemory();

                if (searchText.getText().toString().length() == 0)
                {
                    EventsSelectOption.searchOpt = "ClearSearch";
                    callFirst();
                }

                if (searchText.getText().toString().length() > 0)
                {
                    eventModelArrayList.clear();
                    try
                    {
                        gridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    eventSearchBy = "Name";
                    eventSearchKey = searchText.getText().toString();
                    new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
                }
//                searchEvent();

                return true;
            }
        });


        return view;
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
       // CardsActivity.setActionBarTitle("Events");
    }

    public static void callFirst()
    {
        new HttpAsyncTask().execute(Utility.BASE_URL+"Events/List");
    }

    public static void callSecond()
    {
        eventModelArrayList.clear();
        try
        {
            gridAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        new HttpAsyncTask().execute(Utility.BASE_URL+"Events/List");
    }

    public static void searchEvent()
    {
        eventModelArrayList.clear();
        try
        {
            gridAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        new HttpAsyncTaskSearchEvent().execute(Utility.BASE_URL+"Events/Search");
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            if((e1.getX() - e2.getX()) > sensitvity){
                swipe += "Swipe Left\n";
            }
            else if((e2.getX() - e1.getX()) > sensitvity){
                swipe += "Swipe Right\n";
            }
            else{
                swipe += "\n";
            }

            if((e1.getY() - e2.getY()) > sensitvity){
                swipe += "Swipe Up\n";
                lnrSearch.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                CardsFragment.tabLayout.setVisibility(View.GONE);
            }
            else if((e2.getY() - e1.getY()) > sensitvity){
                swipe += "Swipe Down\n";
                lnrSearch.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                CardsFragment.tabLayout.setVisibility(View.VISIBLE);
            }
            else{
                swipe += "\n";
            }
            //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);

    private static class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Finding Events...");
            dialog.show();*/

            String loading = "Finding Events" ;
            CustomProgressDialog(loading);
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
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

           // CardsActivity.setActionBarTitle("Events");

            try
            {
                if(result == "")
                {
                    tvEventInfo.setVisibility(View.VISIBLE);
                }
                else
                {
                    eventModelArrayList.clear();

                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String numofrecords = response.getString("numofrecords");

                    JSONArray eventList = response.getJSONArray("EventList");

                    if(eventList.length() == 0)
                    {
                        tvEventInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvEventInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= eventList.length() ; i++ )
                        {
                            JSONObject eList = eventList.getJSONObject(i);

                            EventModel eventModel = new EventModel();
                            eventModel.setEvent_ID(eList.getString("Event_ID"));
                            eventModel.setEvent_Name(eList.getString("Event_Name"));
                            eventModel.setEvent_Type(eList.getString("Event_Type"));
                            eventModel.setEvent_Image(eList.getString("Event_Image"));
                            eventModel.setEvent_Desc(eList.getString("Event_Desc"));
                            eventModel.setEvent_Category_ID(eList.getString("Event_Category_ID"));
                            eventModel.setEvent_Category_Name(eList.getString("Event_Category_Name"));
                            eventModel.setEvent_StartDate(eList.getString("Event_StartDate"));
                            eventModel.setEvent_EndDate(eList.getString("Event_EndDate"));
                            eventModel.setCompanyName(eList.getString("CompanyName"));
                            eventModel.setIndustryName(eList.getString("IndustryName"));
                            eventModel.setCity(eList.getString("City"));
                            eventModel.setState(eList.getString("State"));
                            eventModel.setCountry(eList.getString("Country"));
                            eventModel.setPostalCode(eList.getString("PostalCode"));
                            eventModel.setAddress1(eList.getString("Address1"));
                            eventModel.setAddress2(eList.getString("Address2"));
                            eventModel.setAddress3(eList.getString("Address3"));
                            eventModel.setAddress4(eList.getString("Address4"));

                            imageUrl = Utility.BASE_IMAGE_URL+"Events/"+eList.getString("Event_Image");

                            eventModelArrayList.add(eventModel);
                            gridAdapter = new EventsAdapter(mContext, R.layout.row_events, eventModelArrayList);
                            listView.setAdapter(gridAdapter);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public class MyAsync extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params)
        {

            try
            {
                URL url = new URL("");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public static String POST(String url)
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
            if (EventsSelectOption.searchOpt.equals("AllEvents"))
            {
                jsonObject.accumulate("my_userid", "" );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }
            if (EventsSelectOption.searchOpt.equals("ClearSearch"))
            {
                jsonObject.accumulate("my_userid", "" );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }

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

    private static class HttpAsyncTaskSearchEvent extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Finding Events...");
            dialog.show();*/

            String loading = "Searching Events" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return PostSearchEvent(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

           // CardsActivity.setActionBarTitle("Events");

            eventModelArrayList.clear();
            try
            {
                gridAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                if(result == "")
                {
                    tvEventInfo.setVisibility(View.VISIBLE);
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String numofrecords = response.getString("numofrecords");

                    JSONArray eventList = response.getJSONArray("EventList");

                    if(eventList.length() == 0)
                    {
                        tvEventInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvEventInfo.setVisibility(View.GONE);

                        for(int i = 0 ; i <= eventList.length() ; i++ )
                        {
                            JSONObject eList = eventList.getJSONObject(i);

                            EventModel eventModel = new EventModel();
                            eventModel.setEvent_ID(eList.getString("Event_ID"));
                            eventModel.setEvent_Name(eList.getString("Event_Name"));
                            eventModel.setEvent_Type(eList.getString("Event_Type"));
                            eventModel.setEvent_Image(eList.getString("Event_Image"));
                            eventModel.setEvent_Desc(eList.getString("Event_Desc"));
                            eventModel.setEvent_Category_ID(eList.getString("Event_Category_ID"));
                            eventModel.setEvent_Category_Name(eList.getString("Event_Category_Name"));
                            eventModel.setEvent_StartDate(eList.getString("Event_StartDate"));
                            eventModel.setEvent_EndDate(eList.getString("Event_EndDate"));
                            eventModel.setCompanyName(eList.getString("CompanyName"));
                            eventModel.setIndustryName(eList.getString("IndustryName"));
                            eventModel.setCity(eList.getString("City"));
                            eventModel.setState(eList.getString("State"));
                            eventModel.setCountry(eList.getString("Country"));
                            eventModel.setPostalCode(eList.getString("PostalCode"));
                            eventModel.setAddress1(eList.getString("Address1"));
                            eventModel.setAddress2(eList.getString("Address2"));
                            eventModel.setAddress3(eList.getString("Address3"));
                            eventModel.setAddress4(eList.getString("Address4"));
                            eventModel.setEvent_Book_Stand(eList.getString("Event_Book_Stand"));
                            eventModel.setEvent_Registration(eList.getString("Event_Registration"));
                            eventModelArrayList.add(eventModel);

                            gridAdapter = new EventsAdapter(mContext, R.layout.row_events, eventModelArrayList);
                            listView.setAdapter(gridAdapter);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String PostSearchEvent(String url)
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

            if (EventsSelectOption.searchOpt.equals("Industry"))
            {
                searchText.setText(EventsSelectOption.searchKeyWord);

                jsonObject.accumulate("SearchBy", EventsSelectOption.searchBy );
                jsonObject.accumulate("SearchValue", EventsSelectOption.searchKeyWord );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }
            else if (EventsSelectOption.searchOpt.equals("CompanyAssociation"))
            {
                searchText.setText(EventsSelectOption.searchKeyWord);

                jsonObject.accumulate("SearchBy", EventsSelectOption.searchBy );
                jsonObject.accumulate("SearchValue", EventsSelectOption.searchKeyWord );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }
            else if (EventsSelectOption.searchOpt.equals("Date"))
            {
                searchText.setText(EventsSelectOption.searchKeyWord+" - "+EventsSelectOption.searchKeyWord1);

                jsonObject.accumulate("SearchBy", EventsSelectOption.searchBy );
                jsonObject.accumulate("SearchValue", EventsSelectOption.searchKeyWord );
                jsonObject.accumulate("SearchValue1", EventsSelectOption.searchKeyWord1 );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }
            else
            {
                jsonObject.accumulate("SearchBy", eventSearchBy );
                jsonObject.accumulate("SearchValue", eventSearchKey );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );
            }

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

    public static void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading);

        Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(mContext,R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);

        int SPLASHTIME = 1000*60 ;  //since 1000=1sec so 1000*60 = 60000 or 60sec or 1 min.
        for (int i = 350; i <= SPLASHTIME; i = i + 350)
        {
            final int j = i;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run()
                {
                    if (j / 350 == 1 || j / 350 == 4 || j / 350 == 7 || j / 350 == 10)
                    {
                        tvProgressing.setText(loading+".");
                    }
                    else if (j / 350 == 2 || j / 350 == 5 || j / 350 == 8)
                    {
                        tvProgressing.setText(loading+"..");
                    }
                    else if (j / 350 == 3 || j / 350 == 6 || j / 350 == 9)
                    {
                        tvProgressing.setText(loading+"...");
                    }

                }
            }, i);
        }
    }



}
