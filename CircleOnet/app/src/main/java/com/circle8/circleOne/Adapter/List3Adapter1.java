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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.circle8.circleOne.Activity.CardDetail;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List3Fragment1;
import com.circle8.circleOne.Fragments.List4Fragment1;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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

/**
 * Created by admin on 12/14/2017.
 */

public class List3Adapter1 extends RecyclerView.Adapter<List3Adapter1.MyViewHolder> {

    ArrayList<FriendConnection> nfcModelList1 ;
    ArrayList<FriendConnection> nfcModelListFilter1 = new ArrayList<>();
    private DisplayImageOptions options;
    Context context;
    static String ProfileId="";
    LoginSession session;
    int posi ;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
/*
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }*/
    //   OnImageClick onImageClick;

    /* public void onImageClick(OnImageClick onImageClick) {
         this.onImageClick = onImageClick;
     }
 */
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        // public  RelativeLayout viewBackground,viewForeground;
        ProgressBar progressBar1;
        TextView imageDesc;
        TextView imageName;
        TextView imageDesignation;
        ImageView image;
        RelativeLayout defaultCard;
        FrameLayout fm_img;
        Button delete;
        TextView tvPersonName, tvPersonProfile, tvPersonWebsite, tvPersonAddress, tvPersonContact, tvPersonNameLast;
        SwipeRevealLayout swipeLayout;
        FrameLayout deleteLayout;
        FrameLayout fm_front;

        public MyViewHolder(View view) {
            super(view);

            //    viewBackground =(RelativeLayout) view.findViewById(R.id.view_background);
            //    viewForeground = (RelativeLayout)view.findViewById(R.id.view_foreground);
            progressBar1= (ProgressBar)view.findViewById(R.id.progressBar1);
            imageDesc = (TextView) view.findViewById(R.id.textList3);
            imageName = (TextView) view.findViewById(R.id.textNameList3);
            imageDesignation = (TextView) view.findViewById(R.id.textDescList3);
            image = (ImageView) view.findViewById(R.id.imageList3);
            defaultCard = (RelativeLayout) view.findViewById(R.id.rltDefaultCard);
            tvPersonName = (TextView) view.findViewById(R.id.tvPersonName);
            tvPersonProfile = (TextView) view.findViewById(R.id.tvPersonProfile);
            tvPersonWebsite = (TextView) view.findViewById(R.id.tvPersonWebsite);
            tvPersonAddress = (TextView) view.findViewById(R.id.tvPersonAddress);
            tvPersonContact = (TextView) view.findViewById(R.id.tvPersonMobile);
            tvPersonNameLast = (TextView) view.findViewById(R.id.tvPersonNameLast);
            fm_img = (FrameLayout) view.findViewById(R.id.fm_img);
            delete = (Button)view.findViewById(R.id.delete);
            swipeLayout = (SwipeRevealLayout) view.findViewById(R.id.swipe);
            deleteLayout = (FrameLayout)view.findViewById(R.id.delete_layout);
            fm_front = (FrameLayout)view.findViewById(R.id.fm_front);


        }

        public void bind() {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("delete_click",""+getAdapterPosition());
                   // nfcModelList1.remove(getAdapterPosition());
                    posi  = getAdapterPosition() ;
                    Log.e("Bind",""+posi);
                    swipeLayout.close(true);
                    new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");
                    notifyItemRemoved(getAdapterPosition());
                }
            });

        }
    }

    public List3Adapter1(Context context,ArrayList<FriendConnection> nfcModel)
    {
        this.context = context;
        this.nfcModelList1 = nfcModel;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter1.addAll(nfcModelList1);
        session = new LoginSession(context);
        HashMap<String, String> user = session.getUserDetails();
        ProfileId = user.get(LoginSession.KEY_PROFILEID);

        //SwipeItemAdapterMangerImpl swipeItemAdapterManger = new SwipeItemAdapterMangerImpl();
    }


    @Override
    public List3Adapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list3_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final List3Adapter1.MyViewHolder holder,final int position) {

        String name = nfcModelList1.get(position).getName();
        String company = nfcModelList1.get(position).getCompany();
        String email = nfcModelList1.get(position).getEmail();
        String website = nfcModelList1.get(position).getWebsite();
        String mobile = nfcModelList1.get(position).getPh_no();
        String designation = nfcModelList1.get(position).getDesignation();



       // holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.imageName.setText(name);
        String desc = "";
        if (!company.equals("")){
            desc += company;
        }

        try {
            if (!mobile.equals("")) {
                desc += "\n" + mobile;
            }
        }catch (Exception e){}
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


        if (nfcModelList1.get(position).getCard_front().equals(""))
        {
            holder.fm_img.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.progressBar1.setVisibility(View.GONE);
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
            holder.fm_img.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.progressBar1.setVisibility(View.VISIBLE);
            holder.defaultCard.setVisibility(View.GONE);
            //imageView.setImageResource(nfcModelList.get(position).getCard_front());
            Glide.with(context).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList1.get(position).getCard_front())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(holder.image) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.image.setImageBitmap(drawable);
                        }
                    });

            //  Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Cards/"+nfcModelList1.get(position).getCard_front())
            //         .resize(400,280).onlyScaleDown().skipMemoryCache().into(holder.image);
        }

/*        holder.viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("pos",""+position);
                onImageClick.OnImageClick(position);

            }
        });*/
/*
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));

            }

            @Override
            public void onClose(SwipeLayout layout) {
                super.onClose(layout);
            }
        });*/


     /*   holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   holder.swipeLayout.close();
                Toast.makeText(context, "delete", Toast.LENGTH_LONG).show();
                //  posi  = position ;

                //  new HttpAsyncTask().execute(Utility.BASE_URL+"FriendConnection_Operation");


            }
        });*/

        //   Profile profile = profileList.get(position);
        holder.bind();

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
            List3Fragment1.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST1(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            List3Fragment1.rlProgressDialog.setVisibility(View.GONE);
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
                        try
                        {
                            List1Fragment.webCall();
                            List2Fragment.webCall();
                            List3Fragment1.webCall();
                            List4Fragment1.webCall();
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
    public  String POST1(String url)
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
            jsonObject.accumulate("myProfileId",ProfileId);

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
}
