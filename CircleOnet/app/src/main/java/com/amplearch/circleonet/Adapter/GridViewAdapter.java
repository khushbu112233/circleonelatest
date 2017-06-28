package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.ImageItem;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 06/08/2017.
 */

public class GridViewAdapter extends BaseSwipeAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    DatabaseHelper db;
    //newly added
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();

    /*public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }*/

    public GridViewAdapter(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.nfcModelList = nfcModel ;

//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_list2_layout, null);

        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
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

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.DeactiveCards(nfcModelList.get(position).getId());
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                swipeLayout.close();
                nfcModelList.remove(position);
                notifyDataSetChanged();
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
        holder.imageTitle = (TextView) row.findViewById(R.id.text);
        holder.image = (ImageView) row.findViewById(R.id.image);
        row.setTag(holder);

        String name = nfcModelList.get(position).getName();
       // holder.imageTitle.setText(name);
        Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
        holder.image.setImageBitmap(bmp);

    }

    @Override
    public int getCount() {
        return nfcModelList.size();
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
        TextView imageTitle;
        ImageView image;
    }

    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        nfcModelList.clear();

        if(charText.length() == 0)
        {
            nfcModelList.addAll(nfcModelListFilter);
        }
        else
        {
            for(NFCModel md : nfcModelListFilter)
            {
                if(md.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    nfcModelList.add(md);
                }
            }
        }
        notifyDataSetChanged();
    }
}