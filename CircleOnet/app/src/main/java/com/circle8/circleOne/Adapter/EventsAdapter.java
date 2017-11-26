package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circle8.circleOne.Model.EventModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 06/09/2017.
 */

public class EventsAdapter extends ArrayAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<Integer> image = new ArrayList();
    private ArrayList<String> title = new ArrayList();
    private ArrayList<String> desc = new ArrayList();
    private ArrayList<EventModel> eventModelArrayList ;

    String imageUrl = "";

    public EventsAdapter(Context context, int layoutResourceId, ArrayList<Integer> image, ArrayList<String> title, ArrayList<String> desc)
    {
        super(context, layoutResourceId, title);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public EventsAdapter(Context context, int row_events, ArrayList<EventModel> eventModelArrayList)
    {
        super(context, row_events, eventModelArrayList);
        Utility.freeMemory();
        this.layoutResourceId = row_events;
        this.context = context;
        this.eventModelArrayList = eventModelArrayList ;
    }

    static class ViewHolder
    {
        TextView txtTitle;
        TextView txtDesc;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            Utility.freeMemory();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            Utility.freeMemory();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtEventTitle);
            holder.txtDesc = (TextView) row.findViewById(R.id.txtEventDetail);
            holder.image = (ImageView) row.findViewById(R.id.imgEvents);
            row.setTag(holder);
        }
        else
        {
            Utility.freeMemory();
            holder = (ViewHolder) row.getTag();
        }

        /*holder.txtTitle.setText(title.get(position));
        holder.txtDesc.setText(desc.get(position));*/

        holder.txtTitle.setText(eventModelArrayList.get(position).getEvent_Name());
        holder.txtDesc.setText(eventModelArrayList.get(position).getEvent_StartDate()+" To "
                +eventModelArrayList.get(position).getEvent_EndDate());
        Utility.freeMemory();

        final ViewHolder finalHolder = holder;
      /*  MyAsync obj = new MyAsync(){
            @Override
            protected void onPostExecute(Bitmap bmp) {
                super.onPostExecute(bmp);




                finalHolder.image.setImageBitmap(bmp);

            }
        };*/

        if(eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("")
                || eventModelArrayList.get(position).getEvent_Image().equalsIgnoreCase("null") )
        {
            holder.image.setImageResource(R.drawable.ic_event_default);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Events/"+eventModelArrayList.get(position).getEvent_Image())
                    .resize(378,250).onlyScaleDown().skipMemoryCache().noFade().into(holder.image);
//            holder.image.setImageBitmap(eventModelArrayList.get(position).getBitmapImg());


/*            imageUrl = Utility.BASE_IMAGE_URL+"Events/"+eventModelArrayList.get(position).getEvent_Image();
            obj.execute();*/

        }
//        holder.image.setImageResource(image.get(position));


        return row;
    }

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

}