package com.amplearch.circleonet.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Activity.EventDetail;
import com.amplearch.circleonet.Activity.EventsActivity;
import com.amplearch.circleonet.Adapter.ConnectListAdapter;
import com.amplearch.circleonet.Adapter.EventsAdapter;
import com.amplearch.circleonet.Helper.LoginSession;
import com.amplearch.circleonet.Model.ConnectList;
import com.amplearch.circleonet.Model.EventModel;
import com.amplearch.circleonet.R;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment
{
    private ListView listView;
    private EventsAdapter gridAdapter;
    private TextView actionText;
    RelativeLayout lnrSearch;
    View line;

    LoginSession session;
    String user_id ;

    private ArrayList<EventModel> eventModelArrayList = new ArrayList<>();

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

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

//        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/Events/List");

        ArrayList<Integer> image = new ArrayList<Integer>();
        image.add(R.drawable.events1);
        image.add(R.drawable.events2);
        image.add(R.drawable.events3);
        image.add(R.drawable.events4);
        image.add(R.drawable.events5);

        ArrayList<String> title = new ArrayList<>();
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");
        title.add("Justin Yuan fel");
        title.add("Physician Yong");

        ArrayList<String> desc = new ArrayList<>();
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");
        desc.add("Justin Yuan fel");
        desc.add("Physician Yong");

        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);

        listView = (ListView) view.findViewById(R.id.listEvents);
        gridAdapter = new EventsAdapter(getContext(), R.layout.row_events, image, title, desc);
        listView.setAdapter(gridAdapter);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EventDetail.class);
                startActivity(intent);
            }
        });


        return view;
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Finding Events...");
            dialog.show();
            dialog.setCancelable(false);
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
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray eventList = response.getJSONArray("EventList");

                    if(eventList.length() == 0)
                    {

                    }
                    else
                    {
                        for(int i = 0 ; i <= eventList.length() ; i++ )
                        {
                            JSONObject eList = eventList.getJSONObject(i);

                            EventModel eventModel = new EventModel();
                            eventModel.setEvent_ID(eList.getString("Event_ID"));
                            eventModel.setEvent_Name(eList.getString("Event_Name"));
                            eventModel.setEvent_Name(eList.getString("Event_Image"));
                            eventModel.setEvent_Desc(eList.getString("Event_Desc"));
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
                            eventModelArrayList.add(eventModel);

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

    public  String POST(String url)
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
            jsonObject.accumulate("my_userid", user_id );
            jsonObject.accumulate("numofrecords", "10");
            jsonObject.accumulate("pageno", "10" );

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


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}
