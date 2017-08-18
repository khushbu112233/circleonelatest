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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Fragments.CardsFragment;
import com.amplearch.circleonet.Fragments.List1Fragment;
import com.amplearch.circleonet.Fragments.List2Fragment;
import com.amplearch.circleonet.Fragments.List3Fragment;
import com.amplearch.circleonet.Fragments.List4Fragment;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 06/08/2017.
 */

public class List3Adapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
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


    public List3Adapter(Context context, int layoutResourceId, ArrayList<byte[]> image,
                        ArrayList<String> data, ArrayList<String> name, ArrayList<String> designation, ArrayList<Integer> id)
    {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.data = data;
        this.name = name;
        this.designation = designation;
        this.id = id;
    }

    public List3Adapter(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
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
        View v = LayoutInflater.from(context).inflate(R.layout.grid_list3_layout, null);

        delete = (Button) v.findViewById(R.id.delete);
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

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.DeactiveCards(nfcModelList.get(position).getId());
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                swipeLayout.close();
                //nfcModelList.remove(position);

                try {
                    List2Fragment.gridAdapter.notifyDataSetChanged();
                    List2Fragment.allTags = db.getActiveNFC();
                    List2Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List2Fragment.GetData(context);
                } catch (Exception e){

                }

                try {
                    List3Fragment.gridAdapter.notifyDataSetChanged();
                    List3Fragment.allTags = db.getActiveNFC();
                    List3Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List3Fragment.GetData(context);
                } catch (Exception e){

                }
                try {
                    List4Fragment.gridAdapter.notifyDataSetChanged();
                    List4Fragment.allTags = db.getActiveNFC();
                    List4Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List4Fragment.GetData(context);
                } catch (Exception e){

                }

                try {
                   // List1Fragment.myPager.notifyDataSetChanged();
                 //   List1Fragment.allTags = db.getActiveNFC();
                    List1Fragment.nfcModel.clear();
                    //  nfcModelList.clear();
                    List1Fragment.GetData(context);
                } catch (Exception e){

                }

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

       /* Bitmap bmp = BitmapFactory.decodeByteArray(nfcModelList.get(position).getCard_front(), 0, nfcModelList.get(position).getCard_front().length);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
*/
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
        holder.image.setImageResource(nfcModelList.get(position).getCard_front());

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
                CardsActivity.setActionBarTitle("Cards - "+nfcModelList.size());
            }
        }
        notifyDataSetChanged();
    }

}
