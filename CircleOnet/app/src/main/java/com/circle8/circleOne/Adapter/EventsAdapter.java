package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.circle8.circleOne.Activity.EventDetail;
import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Pref;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class EventsAdapter  extends  RecyclerView.Adapter<EventsAdapter.MyViewHolder>
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Integer> image = new ArrayList();
    private ArrayList<String> title = new ArrayList();
    private ArrayList<String> desc = new ArrayList();
    private ArrayList<EventModel> eventModelArrayList ;
    URI url = null;
    String imageUrl = "";
    public EventsAdapter(Context context, int layoutResourceId, ArrayList<Integer> image, ArrayList<String> title, ArrayList<String> desc)
    {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public EventsAdapter(Context context, int row_events, ArrayList<EventModel> eventModelArrayList)
    {
        Utility.freeMemory();
        this.layoutResourceId = row_events;
        this.context = context;
        this.eventModelArrayList = eventModelArrayList ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_events, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtTitle.setText(eventModelArrayList.get(position).getEvent_Name());
        holder.txtDesc.setText(eventModelArrayList.get(position).getEvent_StartDate()+" To "
                +eventModelArrayList.get(position).getEvent_EndDate());



        if(eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("")
                || eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("null") )
        {
            holder.image.setImageResource(R.drawable.ic_event_default);
            holder.progressBar1.setVisibility(View.GONE);
        }
        else
        {
            holder.progressBar1.setVisibility(View.VISIBLE);
            Glide.with(context).load(Utility.BASE_IMAGE_URL+"Events/"+eventModelArrayList.get(position).getEvent_Image())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(holder.image) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.image.setImageBitmap(drawable);
                        }
                    });
        }
      /*  holder.ll_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ada","click");
                Pref.setValue(context,"Event_ID",eventModelArrayList.get(position).getEvent_ID());
                Intent intent = new Intent(context, EventDetail.class);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return eventModelArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView txtTitle;
        TextView txtDesc;
        ImageView image;
        FrameLayout fm_img;
        ProgressBar progressBar1;
        LinearLayout ll_event;

        public MyViewHolder(View row) {
            super(row);


            txtTitle = (TextView) row.findViewById(R.id.txtEventTitle);
            txtDesc = (TextView) row.findViewById(R.id.txtEventDetail);
            image = (ImageView) row.findViewById(R.id.imgEvents);
            fm_img =(FrameLayout)row.findViewById(R.id.fm_img);
            progressBar1 = (ProgressBar)row.findViewById(R.id.progressBar1);
            ll_event = (LinearLayout)row.findViewById(R.id.ll_event);



        }
    }



    /*private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                Log.d("TAG",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            holder.image.setImageBitmap(result);
        }
    }
*/


    public class MyAsync extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }


    public class ImageLoader extends AsyncTask<Object, Object, Bitmap> {
        private URI imageUri;

        private ImageView imageView;

        private int preferredWidth = 80;
        private int preferredHeight = 80;

        public ImageLoader( URI uri, ImageView imageView, int scaleWidth, int scaleHeight ) {
            this.imageUri = uri;
            this.imageView = imageView;
            this.preferredWidth = scaleWidth;
            this.preferredHeight = scaleHeight;
        }

        public Bitmap doInBackground(Object... params) {
            if( imageUri == null ) return null;
            String url = imageUri.toString();
            if( url.length() == 0 ) return null;
            HttpGet httpGet = new HttpGet(url);
            DefaultHttpClient client = new DefaultHttpClient();
            InputStream is = null;
            HttpResponse response;
            try {
                response = client.execute( httpGet );
                Utility.freeMemory();
                is = new BufferedInputStream( response.getEntity().getContent() );
                Utility.freeMemory();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                Utility.freeMemory();
                if( preferredWidth > 0 && preferredHeight > 0 && bitmap.getWidth() > preferredWidth && bitmap.getHeight() > preferredHeight ) {
                    return Bitmap.createScaledBitmap(bitmap, preferredWidth, preferredHeight, false);
                } else {
                    return bitmap;
                }
            }
            catch (IOException e){}

            return null;
        }

        public void onPostExecute( Bitmap drawable ) {
            imageView.setImageBitmap(drawable);
        }
    }

}