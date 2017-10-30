package com.circle8.circleOne.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List3Fragment;
import com.circle8.circleOne.Fragments.List4Fragment;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by admin on 06/08/2017.
 */

public class List3Adapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
    private List3Fragment fragment ;
    private ArrayList<String> data = new ArrayList();
    private ArrayList<Integer> id = new ArrayList();
    private ArrayList<byte[]> image = new ArrayList();
    private ArrayList<String> name = new ArrayList();
    private ArrayList<String> designation = new ArrayList();
    Button delete;

    DatabaseHelper db;
    //newly added
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();

    ArrayList<FriendConnection> nfcModelList1 = new ArrayList<>();
    ArrayList<FriendConnection> nfcModelListFilter1 = new ArrayList<>();

    private int posi ;
    LoginSession session ;
    String profile_id ;


    public List3Adapter(Context context, int layoutResourceId, ArrayList<byte[]> image,
                        ArrayList<String> data, ArrayList<String> name, ArrayList<String> designation, ArrayList<Integer> id) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.data = data;
        this.name = name;
        this.designation = designation;
        this.id = id;
    }

   /* public List3Adapter(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.nfcModelList = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);
    }*/

    public List3Adapter(Context context, int grid_list3_layout, ArrayList<FriendConnection> nfcModel, List3Fragment fragment)
    {
        this.fragment = fragment ;
        this.context = context;
        this.layoutResourceId = grid_list3_layout;
        this.nfcModelList1 = nfcModel;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter1.addAll(nfcModelList1);
    }

    public List3Adapter(Context context, int grid_list3_layout, ArrayList<FriendConnection> nfcModel)
    {
        this.context = context;
        this.layoutResourceId = grid_list3_layout;
        this.nfcModelList1 = nfcModel;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter1.addAll(nfcModelList1);

        session = new LoginSession(context);
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_list3_layout, null);

        delete = (Button) v.findViewById(R.id.delete);

        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        db = new DatabaseHelper(context);



        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                //  Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                db.DeactiveCards(nfcModelList.get(position).getId());
//                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                swipeLayout.close();

                posi  = position ;

                new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");

              /*  try
                {
                    List3Fragment.allTaggs.clear();
                    List3Fragment.nfcModel1.clear();
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {

                }*/


                //nfcModelList.remove(position);

               /* try {
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.GetData(context);
                } catch (Exception e) {

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.GetData(context);
                } catch (Exception e) {

                }
                try {
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(context);
                } catch (Exception e) {

                }

                try {
                    // List1Fragment.myPager.notifyDataSetChanged();
                    //   List1Fragment.allTags = db.getActiveNFC();
                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(context);
                } catch (Exception e) {

                } */
                /*notifyDataSetChanged();

                if (CardsActivity.mViewPager.getCurrentItem() == 0){
                    Intent go = new Intent(context,CardsActivity.class);

                    // you pass the position you want the viewpager to show in the extra,
                    // please don't forget to define and initialize the position variable
                    // properly
                    go.putExtra("viewpager_position", 0);
                    go.putExtra("nested_viewpager_position", CardsFragment.mViewPager.getCurrentItem());

                    context.startActivity(go);
                    ((Activity)context).finish();
                    //Toast.makeText(getApplicationContext(), String.valueOf(CardsFragment.mViewPager.getCurrentItem()), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent go = new Intent(context,CardsActivity.class);
                    context.startActivity(go);
                    ((Activity)context).finish();
                }*/
            }
        });

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void fillValues(int position, View convertView)
    {
        View row = convertView;
        ViewHolder holder = null;


        holder = new ViewHolder();
        holder.imageDesc = (TextView) row.findViewById(R.id.textList3);
        holder.imageName = (TextView) row.findViewById(R.id.textNameList3);
        holder.imageDesignation = (TextView) row.findViewById(R.id.textDescList3);
        holder.image = (ImageView) row.findViewById(R.id.imageList3);
        holder.defaultCard = (RelativeLayout) row.findViewById(R.id.rltDefaultCard);
        holder.tvPersonName = (TextView) row.findViewById(R.id.tvPersonName);
        holder.tvPersonProfile = (TextView) row.findViewById(R.id.tvPersonProfile);
        holder.tvPersonWebsite = (TextView) row.findViewById(R.id.tvPersonWebsite);
        holder.tvPersonAddress = (TextView) row.findViewById(R.id.tvPersonAddress);
        holder.tvPersonContact = (TextView) row.findViewById(R.id.tvPersonMobile);
        holder.tvPersonNameLast = (TextView) row.findViewById(R.id.tvPersonNameLast);

        row.setTag(holder);

        /*holder.imageDesc.setText(data.get(position));
        holder.imageName.setText(name.get(position));
        try {
            if (!designation.get(position).equalsIgnoreCase("")) {
                holder.imageDesignation.setText(designation.get(position));
            }
        }catch (Exception e){

        }
        Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
        holder.image.setImageBitmap(bmp);
*/
        String name = nfcModelList1.get(position).getName();
        String company = nfcModelList1.get(position).getCompany();
        String email = nfcModelList1.get(position).getEmail();
        String website = nfcModelList1.get(position).getWebsite();
        String mobile = nfcModelList1.get(position).getPh_no();
        String designation = nfcModelList1.get(position).getDesignation();

        holder.imageName.setText(name);
        String desc = "";
        if (!company.equals("")){
            desc += company;
        }
        if (!mobile.equals("")){
            desc += "\n" + mobile;
        }
        if (!website.equals("")){
            desc += "\n" +website;
        }
        holder.imageDesc.setText(company + "\n" + mobile + "\n" + website);

        try {
            if (!designation.equalsIgnoreCase("")) {
                holder.imageDesignation.setText(designation);
            }
        } catch (Exception e) {
        }

       /* Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
*/
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
//        holder.image.setImageResource(nfcModelList.get(position).getCard_front());

        if (nfcModelList1.get(position).getCard_front().equals(""))
        {
            holder.image.setVisibility(View.GONE);
            holder.defaultCard.setVisibility(View.VISIBLE);
            try
            {
                try {
                    if (nfcModelList1.get(position).getDesignation().equals("") || nfcModelList1.get(position).getDesignation().equals("null")
                            || nfcModelList1.get(position).getDesignation() == null) {
                        holder.tvPersonProfile.setVisibility(View.GONE);
                    }
                }catch (Exception e){}

                try {
                    if (nfcModelList1.get(position).getEmail().equals("") || nfcModelList1.get(position).getEmail().equals("null")
                            || nfcModelList1.get(position).getEmail() == null) {
                        holder.tvPersonWebsite.setVisibility(View.GONE);
                    }
                }catch (Exception e){}

                try {
                    if (nfcModelList1.get(position).getAddress().equals("") || nfcModelList1.get(position).getAddress().equalsIgnoreCase("null")
                            || nfcModelList1.get(position).getAddress() == null) {
                        holder.tvPersonAddress.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    if (nfcModelList1.get(position).getPh_no().equals("") || nfcModelList1.get(position).getPh_no().equals("null")
                            || nfcModelList1.get(position).getPh_no() == null) {
                        holder.tvPersonContact.setVisibility(View.GONE);
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

            String name1 = nfcModelList1.get(position).getName();
            String kept = name1.substring(0, name1.indexOf(" "));
            String remainder = name1.substring(name1.indexOf(" ") + 1, name1.length());
            holder.tvPersonName.setText(kept);
            holder.tvPersonNameLast.setText(remainder);
            holder.tvPersonProfile.setText(nfcModelList1.get(position).getDesignation());
            holder.tvPersonWebsite.setText("E : " + nfcModelList1.get(position).getEmail());
            holder.tvPersonAddress.setText("A : " + nfcModelList1.get(position).getAddress());
            holder.tvPersonContact.setText("M : " + nfcModelList1.get(position).getPh_no());

        }
        else
        {
            holder.image.setVisibility(View.VISIBLE);
            holder.defaultCard.setVisibility(View.GONE);
            //imageView.setImageResource(nfcModelList.get(position).getCard_front());
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList1.get(position).getCard_front()).into(holder.image);
        }

        //Picasso.with(context).load("http://circle8.asia/App_ImgLib/Cards/" + nfcModelList1.get(position).getCard_front()).into(holder.image);

    }

    @Override
    public int getCount() {
        return nfcModelList1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView imageDesc;
        TextView imageName;
        TextView imageDesignation;
        ImageView image;
        RelativeLayout defaultCard;
        TextView tvPersonName, tvPersonProfile, tvPersonWebsite, tvPersonAddress, tvPersonContact, tvPersonNameLast;
    }

    public void Filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList1.clear();

        if (charText.length() == 0) {
            nfcModelList1.addAll(nfcModelListFilter1);
        } else {
            for (FriendConnection md : nfcModelListFilter1) {
                if (md.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    nfcModelList1.add(md);
                }
                CardsActivity.setActionBarTitle("Cards - " + nfcModelList1.size() + "/"+ CardsActivity.Connection_Limit);
            }
        }
        notifyDataSetChanged();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(context);
            dialog.setMessage("Deleting Records...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Deleting Records";
            List3Fragment.CustomProgressDialog(loading);
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
            List3Fragment.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if(result == "")
                {
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if(success.equals("1"))
                    {
                        List1Fragment.progressStatus = "DELETE";
                        List2Fragment.progressStatus = "DELETE";
                        List3Fragment.progressStatus = "DELETE";
                        List4Fragment.progressStatus = "DELETE";

                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        try
                        {
                            List1Fragment.webCall();
                            List2Fragment.webCall();
                            List3Fragment.webCall();
                            List4Fragment.webCall();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                    /*try
                    {
                        List3Fragment.gridAdapter.notifyDataSetChanged();
//                        List3Fragment.GetData(context);
//                        List3Fragment.allTaggs.clear();
//                        List3Fragment.nfcModel1.clear();
                    }
                    catch(Exception e) {    }

                    try
                    {
                        List2Fragment.gridAdapter.notifyDataSetChanged();
//                        List2Fragment.allTaggs.clear();
//                        List2Fragment.nfcModel.clear();
//                        List2Fragment.GetData(context);
                    }
                    catch(Exception e) {    }


                    try
                    {
                        List4Fragment.gridAdapter.notifyDataSetChanged();
//                        List4Fragment.allTaggs.clear();
//                        List4Fragment.nfcModel1.clear();
//                        List4Fragment.GetData(context);
                    }
                    catch(Exception e) {    }

                    try
                    {
                        List1Fragment.mAdapter.notifyDataSetChanged();
                        List1Fragment.mAdapter1.notifyDataSetChanged();
//                        List1Fragment.allTags.clear();
//                        List1Fragment.nfcModel.clear();
//                        List1Fragment.GetData(context);
                    }
                    catch(Exception e) {    }*/

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
            jsonObject.accumulate("Operation", "Remove" );
            jsonObject.accumulate("RequestType", "" );
            jsonObject.accumulate("friendProfileId", nfcModelList1.get(posi).getProfile_id());
            jsonObject.accumulate("myProfileId", profile_id );

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
