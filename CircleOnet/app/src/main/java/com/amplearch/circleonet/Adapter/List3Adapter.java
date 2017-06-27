package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 06/08/2017.
 */

public class List3Adapter extends ArrayAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList();
    private ArrayList<byte[]> image = new ArrayList();
    private ArrayList<String> name = new ArrayList();
    private ArrayList<String> designation = new ArrayList();

    //newly added
    ArrayList<NFCModel> nfcModelList ;
    ArrayList<NFCModel> nfcModelListFilter ;

    public List3Adapter(Context context, int layoutResourceId, ArrayList<byte[]> image,
                        ArrayList<String> data, ArrayList<String> name, ArrayList<String> designation)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.data = data;
        this.name = name;
        this.designation = designation;
    }

    public List3Adapter(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
    {
        super(context, grid_list3_layout, nfcModel);

        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.nfcModelList = nfcModel ;

        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageDesc = (TextView) row.findViewById(R.id.textList3);
            holder.imageName = (TextView) row.findViewById(R.id.textNameList3);
            holder.imageDesignation = (TextView) row.findViewById(R.id.textDescList3);
            holder.image = (ImageView) row.findViewById(R.id.imageList3);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

//        holder.imageDesc.setText(data.get(position));
//        holder.imageName.setText(name.get(position));
//        try
//        {
//            if (!designation.get(position).equalsIgnoreCase(""))
//            {
//                holder.imageDesignation.setText(designation.get(position));
//            }
//        }
//        catch (Exception e){  }

//        Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
//        // ImageView image = (ImageView) findViewById(R.id.imageView1);
//        holder.image.setImageBitmap(bmp);

        String name = nfcModelList.get(position).getName();
        String company = nfcModelList.get(position).getCompany();
        String email = nfcModelList.get(position).getEmail();
        String website = nfcModelList.get(position).getWebsite();
        String mobile = nfcModelList.get(position).getMob_no();
        String designation = nfcModelList.get(position).getDesignation();

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

        Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
        holder.image.setImageBitmap(bmp);

        return row;
    }

    static class ViewHolder
    {
        TextView imageDesc;
        TextView imageName;
        TextView imageDesignation;
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
