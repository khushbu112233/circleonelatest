package com.circle8.circleOne.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List3Fragment1;
import com.circle8.circleOne.Fragments.List4Fragment1;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.ConnectList;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 06/08/2017.
 */

public class List4Adapter1 extends  RecyclerView.Adapter<List4Adapter1.MyViewHolder>
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList();
    private ArrayList<byte[]> image = new ArrayList();
    private ArrayList<String> name = new ArrayList();
    private ArrayList<String> designation = new ArrayList();

    private int posi ;
    LoginSession session ;
    String profile_id ;
    DatabaseHelper db;
    //newly added
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();

    ArrayList<FriendConnection> nfcModelList1 = new ArrayList<>();
    ArrayList<FriendConnection> nfcModelListFilter1 = new ArrayList<>();

    ArrayList<ConnectList> connectLists = new ArrayList<>();
    ArrayList<ConnectList> connectListsFilter = new ArrayList<>();


    public List4Adapter1(Context context, int layoutResourceId, ArrayList<byte[]> image, ArrayList<String> data, ArrayList<String> name, ArrayList<String> designation) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.data = data;
        this.name = name;
        this.designation = designation;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {

      //  public RelativeLayout viewBackground,viewForeground;
        ProgressBar progressBar1;
        TextView imageDesc;
        TextView imageName;
        TextView imageDesignation;
        CircleImageView image;
        FrameLayout fm_img,deleteLayout;
        FrameLayout fm_front;
        Button delete;
        SwipeRevealLayout swipe;

        public MyViewHolder(View view) {
            super(view);

           // viewBackground =(RelativeLayout) view.findViewById(R.id.view_background);
           // viewForeground = (RelativeLayout)view.findViewById(R.id.view_foreground);
            progressBar1= (ProgressBar)view.findViewById(R.id.progressBar1);
            imageDesc = (TextView) view.findViewById(R.id.textList3);
            imageName = (TextView) view.findViewById(R.id.textNameList3);
            imageDesignation = (TextView) view.findViewById(R.id.textDescList3);
            image = (CircleImageView) view.findViewById(R.id.imageList4);
            fm_img = (FrameLayout)view.findViewById(R.id.fm_img);
            fm_front = (FrameLayout)view.findViewById(R.id.fm_front);
            deleteLayout = (FrameLayout)view.findViewById(R.id.delete_layout);
            delete = (Button)view.findViewById(R.id.delete);
            swipe  = (SwipeRevealLayout)view.findViewById(R.id.swipe);
        }
        public void bind() {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("delete_click",""+getAdapterPosition());
                    // nfcModelList1.remove(getAdapterPosition());
                    posi  = getAdapterPosition() ;

                    swipe.close(true);
                    new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");
                    notifyItemRemoved(getAdapterPosition());
                }
            });

        }
    }


    public List4Adapter1(Context context, ArrayList<FriendConnection> nfcModel)
    {
        this.context = context ;
        this.nfcModelList1 = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter1.addAll(nfcModelList1);
        session = new LoginSession(context);
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list4_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {


        String name = nfcModelList1.get(position).getName();
        String company = nfcModelList1.get(position).getCompany();
        String email = nfcModelList1.get(position).getEmail();
        String website = nfcModelList1.get(position).getWebsite();
        String mobile = nfcModelList1.get(position).getMob_no();
        String designation = nfcModelList1.get(position).getDesignation();

        holder.imageName.setText(name);
        holder.imageDesc.setText(company+"\n"+email+"\n"+mobile+"\n"+website);
        try
        {
            if (!designation.equalsIgnoreCase(""))
            {
                holder.imageDesignation.setText(designation);
            }
        }
        catch (Exception e){  }

        try
        {
            if(nfcModelList1.get(position).getUser_image().equals(""))
            {
                holder.image.setImageResource(R.drawable.usr);
                holder.progressBar1.setVisibility(View.GONE);
            }
            else
            {

                holder.fm_img.setVisibility(View.VISIBLE);
                holder.progressBar1.setVisibility(View.VISIBLE);
               /* Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+nfcModelList1.get(position).getUser_image())
                        .resize(300,300).onlyScaleDown().skipMemoryCache().into(holder.image);*/
                Glide.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+nfcModelList1.get(position).getUser_image())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(holder.image) {
                            @Override
                            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                holder.fm_img.setVisibility(View.VISIBLE);
                                holder.progressBar1.setVisibility(View.GONE);
                                holder.image.setImageBitmap(drawable);
                            }
                        });
            }
        }
        catch (Exception e)
        {
            holder.image.setImageResource(R.drawable.usr);
        }

        holder.bind();
        //   Profile profile = profileList.get(position);
        holder.fm_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardDetail.class);
                intent.putExtra("tag_id", nfcModelList1.get(position).getNfc_tag());
                intent.putExtra("profile_id", nfcModelList1.get(position).getProfile_id());
                intent.putExtra("DateInitiated",nfcModelList1.get(position).getDateInitiated());
                intent.putExtra("lat", nfcModelList1.get(position).getLatitude());
                intent.putExtra("long", nfcModelList1.get(position).getLongitude());
                context.startActivity(intent);
                Utility.deleteCache(context);

                Utility.freeMemory();
            }
        });

    }



    @Override
    public int getItemCount() {
        return nfcModelList1.size();
    }


    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList1.clear();

        if(charText.length() == 0)
        {
            nfcModelList1.addAll(nfcModelListFilter1);
        }
        else
        {
            for(FriendConnection md : nfcModelListFilter1)
            {
                if(md.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nfcModelList1.add(md);
                }
                CardsActivity.setActionBarTitle("Cards - "+nfcModelList1.size() + "/"+ CardsActivity.Connection_Limit);
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

            String loading = "Deleting records";
            List4Fragment1.CustomProgressDialog(loading);

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
            List4Fragment1.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if(result == "")
                {
                    Toast.makeText(context, "Slow Internet Connection", Toast.LENGTH_LONG).show();
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
                        List3Fragment1.progressStatus = "DELETE";
                        List4Fragment1.progressStatus = "DELETE";

                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        List1Fragment.webCall();
                        List2Fragment.webCall();
                        List3Fragment1.webCall();
                        List4Fragment1.webCall();
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
            jsonObject.accumulate("Operation", "Remove" );
            jsonObject.accumulate("RequestType", "" );
            jsonObject.accumulate("connection_date", Utility.currentDate());
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