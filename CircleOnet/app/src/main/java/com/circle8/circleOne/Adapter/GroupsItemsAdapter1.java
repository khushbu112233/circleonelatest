package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/11/2017.
 */

public class GroupsItemsAdapter1 extends BaseAdapter
{
    Activity context ;
    ArrayList<GroupModel> groupModelsList ;

    CircleImageView imgGroup, imgProfile1, imgProfile2, imgProfile3 ;
    TextView tvGroupName, tvPersonName1, tvPersonName2, tvPersonName3 ;
    TextView tvDesignation1, tvDesignation2, tvDesignation3 ;
    TextView tvDetail1, tvDetail2, tvDetail3 ;
    RelativeLayout llOneUser, llSecondUser, llThirdUser ;
    TextView tvMemberInfo ;
    View Line1, Line2 ;

    LoginSession loginSession;
    String profileId = "", groupID = "";

    ArrayList<String> Listname1 = new ArrayList<>();
    ArrayList<String> Listname2 = new ArrayList<>();
    ArrayList<String> Listname3 = new ArrayList<>();
    ArrayList<String> Listdesignation1 = new ArrayList<>();
    ArrayList<String> Listdesignation2 = new ArrayList<>();
    ArrayList<String> Listdesignation3 = new ArrayList<>();
    ArrayList<String> Listdetail1 = new ArrayList<>();
    ArrayList<String> Listdetail2 = new ArrayList<>();
    ArrayList<String> Listdetail3 = new ArrayList<>();

    int pos ;

    ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();

    public GroupsItemsAdapter1(Activity applicationContext, ArrayList<GroupModel> groupName)
    {
        this.context = applicationContext ;
        this.groupModelsList = groupName ;
    }

    @Override
    public int getCount() {
        return groupModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.groups_items, null);

            loginSession = new LoginSession(context);
            HashMap<String, String> user = loginSession.getUserDetails();
            profileId = user.get(LoginSession.KEY_PROFILEID);
            groupID = groupModelsList.get(position).getGroup_ID();

            pos = position ;

            imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            imgProfile1 = (CircleImageView)row.findViewById(R.id.imgProfile1);
            imgProfile2 = (CircleImageView)row.findViewById(R.id.imgProfile2);
            imgProfile3 = (CircleImageView)row.findViewById(R.id.imgProfile3);

            tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            tvPersonName1 = (TextView)row.findViewById(R.id.tvPersonName1);
            tvPersonName2 = (TextView)row.findViewById(R.id.tvPersonName2);
            tvPersonName3 = (TextView)row.findViewById(R.id.tvPersonName3);

            tvDesignation1 = (TextView)row.findViewById(R.id.tvDesignation1);
            tvDesignation2 = (TextView)row.findViewById(R.id.tvDesignation2);
            tvDesignation3 = (TextView)row.findViewById(R.id.tvDesignation3);

            tvDetail1 = (TextView)row.findViewById(R.id.tvPersonDetail1);
            tvDetail2 = (TextView)row.findViewById(R.id.tvPersonDetail2);
            tvDetail3 = (TextView)row.findViewById(R.id.tvPersonDetail3);
            tvMemberInfo = (TextView)row.findViewById(R.id.tvMemberInfo);

            llOneUser = (RelativeLayout) row.findViewById(R.id.llOneUser);
            llSecondUser = (RelativeLayout)row.findViewById(R.id.llSecondUser);
            llThirdUser = (RelativeLayout)row.findViewById(R.id.llThirdUser);

            Line1 = (View)row.findViewById(R.id.Line1);
            Line2 = (View)row.findViewById(R.id.Line2);

            tvGroupName.setText(groupModelsList.get(position).getGroup_Name());

            if (groupModelsList.get(position).getGroup_Photo().equals(""))
            {
                imgGroup.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupModelsList.get(position).getGroup_Photo()).placeholder(R.drawable.usr_1).into(imgGroup);
            }

            for (int i = 1; i <= groupID.length() ; i++)
            {
                new HttpAsyncTaskGroup().execute("http://circle8.asia:8081/Onet.svc/Group/FetchConnection");
            }

        }

        return row;
    }

    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Load Group Members");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    String count = jsonObject.getString("count");

                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                        try
                        {
                            // for OneUserData..
                            JSONObject object0 = jsonArray.getJSONObject(0);
                            String profile1 = object0.getString("UserPhoto");
                            String name1 = object0.getString("FirstName") + " " + object0.getString("LastName");
                            String designation1 = object0.getString("Designation");
                            String address1 = object0.getString("Address1") + " " + object0.getString("Address2") + " " +
                                    object0.getString("Address3") + " " + object0.getString("Address4") + "\n" +
                                    object0.getString("City") + " " + object0.getString("State") + " " +
                                    object0.getString("Country") + " " + object0.getString("Postalcode");
                            String company1 = object0.getString("CompanyName");
                            String email1 = object0.getString("UserName");
                            String website1 = object0.getString("Website");
                            String phone1 = object0.getString("Phone");
                            tvPersonName1.setText(name1);
                            tvDesignation1.setText(designation1);
                            tvDetail1.setText(address1 + "\n" + company1 + "\n" + email1 + "\n" + website1 + "\n" + phone1);

                            // for SecondUserData..
                            JSONObject object1 = jsonArray.getJSONObject(1);
                            String profile2 = object1.getString("UserPhoto");
                            String name2 = object1.getString("FirstName") + " " + object1.getString("LastName");
                            String designation2 = object1.getString("Designation");
                            String address2 = object1.getString("Address1") + " " + object1.getString("Address2") + " " +
                                    object1.getString("Address3") + " " + object1.getString("Address4") + "\n" +
                                    object1.getString("City") + " " + object1.getString("State") + " " +
                                    object1.getString("Country") + " " + object1.getString("Postalcode");
                            String company2 = object1.getString("CompanyName");
                            String email2 = object1.getString("UserName");
                            String website2 = object1.getString("Website");
                            String phone2 = object1.getString("Phone");
                            tvPersonName2.setText(name2);
                            tvDesignation2.setText(designation2);
                            tvDetail2.setText(address2 + "\n" + company2 + "\n" + email2 + "\n" + website2 + "\n" + phone2);

                            // for SecondUserData..
                            JSONObject object2 = jsonArray.getJSONObject(2);
                            String profile3 = object2.getString("UserPhoto");
                            String name3 = object2.getString("FirstName") + " " + object2.getString("LastName");
                            String designation3 = object2.getString("Designation");
                            String address3 = object2.getString("Address1") + " " + object2.getString("Address2") + " " +
                                    object2.getString("Address3") + " " + object2.getString("Address4") + "\n" +
                                    object2.getString("City") + " " + object2.getString("State") + " " +
                                    object2.getString("Country") + " " + object2.getString("Postalcode");
                            String company3 = object2.getString("CompanyName");
                            String email3 = object2.getString("UserName");
                            String website3 = object2.getString("Website");
                            String phone3 = object2.getString("Phone");
                            tvPersonName3.setText(name3);
                            tvDesignation3.setText(designation3);
                            tvDetail3.setText(address3 + "\n" + company3 + "\n" + email3 + "\n" + website3 + "\n" + phone3);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    /*if (jsonArray.length() == 2)
                    {
                        llThirdUser.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                    }
                    else
                    {

                    }*/

                    /*if (jsonArray.length() == 1)
                    {
                        llSecondUser.setVisibility(View.GONE);
                        llThirdUser.setVisibility(View.GONE);
                        Line1.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                    }
                    else
                    {

                    }*/

                    /*if (jsonArray.length() == 0)
                    {
                        llOneUser.setVisibility(View.GONE);
                        llSecondUser.setVisibility(View.GONE);
                        llThirdUser.setVisibility(View.GONE);
                        Line1.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                        tvMemberInfo.setVisibility(View.VISIBLE);
                    }
                    else
                    {

                    }*/

/*
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        if(i < 3)
                        {
                            GroupDetailModel groupDetailModel = new GroupDetailModel();
                            groupDetailModel.setFirstname(object.getString("FirstName"));
                            groupDetailModel.setLastname(object.getString("LastName"));
                            groupDetailModel.setDesignation(object.getString("Designation"));
                            groupDetailModel.setCompany(object.getString("CompanyName"));
                            groupDetailModel.setEmail(object.getString("UserName"));
                            groupDetailModel.setWebsite(object.getString("Website"));
                            groupDetailModel.setMobile(object.getString("Phone"));
                            groupDetailModel.setAddress1(object.getString("Address1"));
                            groupDetailModel.setAddress2(object.getString("Address2"));
                            groupDetailModel.setAddress3(object.getString("Address3"));
                            groupDetailModel.setAddress4(object.getString("Address4"));
                            groupDetailModel.setCity(object.getString("City"));
                            groupDetailModel.setState(object.getString("State"));
                            groupDetailModel.setCountry(object.getString("Country"));
                            groupDetailModel.setPostalcode(object.getString("Postalcode"));
                            groupDetailModel.setImgProfile(object.getString("UserPhoto"));
                            groupDetailModelArrayList.add(groupDetailModel);
                        }
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();

                    }
*/
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(context, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
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
            jsonObject.accumulate("group_ID", groupID);
            jsonObject.accumulate("profileId", profileId);
            jsonObject.accumulate("numofrecords", "50");
            jsonObject.accumulate("pageno", "1");

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
            if (inputStream != null)
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
