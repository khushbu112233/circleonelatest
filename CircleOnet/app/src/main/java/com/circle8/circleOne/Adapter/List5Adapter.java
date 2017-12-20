package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Fragments.List1Fragment;
import com.circle8.circleOne.Fragments.List2Fragment;
import com.circle8.circleOne.Fragments.List3Fragment;
import com.circle8.circleOne.Fragments.List4Fragment;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Model.ConnectList;
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

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 06/08/2017.
 */

public class List5Adapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList();
    private ArrayList<byte[]> image = new ArrayList();
    private ArrayList<String> name = new ArrayList();
    private ArrayList<String> designation = new ArrayList();

    DatabaseHelper db;
    //newly added
    ArrayList<NFCModel> nfcModelList = new ArrayList<>();
    ArrayList<NFCModel> nfcModelListFilter = new ArrayList<>();

    ArrayList<FriendConnection> nfcModelList1 = new ArrayList<>();
    ArrayList<FriendConnection> nfcModelListFilter1 = new ArrayList<>();

    ArrayList<ConnectList> connectLists = new ArrayList<>();
    ArrayList<ConnectList> connectListsFilter = new ArrayList<>();

    public List5Adapter(Context context, int layoutResourceId, ArrayList<byte[]> image, ArrayList<String> data, ArrayList<String> name, ArrayList<String> designation) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.data = data;
        this.name = name;
        this.designation = designation;
    }

   /* public List4Adapter(Context context, int grid_list3_layout, ArrayList<NFCModel> nfcModel)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.nfcModelList = nfcModel ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.nfcModelListFilter.addAll(nfcModelList);
    }*/

    public List5Adapter(Context context, int grid_list3_layout, ArrayList<ConnectList> connectLists)
    {
        this.context = context ;
        this.layoutResourceId = grid_list3_layout ;
        this.connectLists = connectLists ;
//        this.nfcModelListFilter = new ArrayList<NFCModel>();
        this.connectListsFilter.addAll(connectLists);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_list4_layout, null);

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
            public void onClick(View view)
            {
//                db.DeactiveCards(nfcModelList.get(position).getId());
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
//                    List3Fragment.GetData(context);
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
                   // List1Fragment.allTags = db.getActiveNFC();
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
    public void fillValues(final int position, View convertView)
    {
        View row = convertView;
        ViewHolder holder = null;

        holder = new ViewHolder();
        holder.imageDesc = (TextView) row.findViewById(R.id.textList3);
        holder.imageName = (TextView) row.findViewById(R.id.textNameList3);
        holder.imageDesignation = (TextView) row.findViewById(R.id.textDescList3);
        holder.image = (CircleImageView) row.findViewById(R.id.imageList4);
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
        String name = connectLists.get(position).getFirstname()+" "+connectLists.get(position).getLastname();
        String company = connectLists.get(position).getCompanyname();
        String email = connectLists.get(position).getUsername();
        String website = connectLists.get(position).getWebsite();
        String mobile = connectLists.get(position).getPhone();
        String designation = connectLists.get(position).getDesignation();

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
            if(connectLists.get(position).getUserphoto().equals(""))
            {
                holder.image.setImageResource(R.drawable.usr);
            }
            else
            {
                Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/"+connectLists.get(position).getUserphoto())
                        .resize(300,300).onlyScaleDown().into(holder.image);
            }
        }
        catch (Exception e)
        {
            holder.image.setImageResource(R.drawable.usr);
        }


       /* row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConnectActivity.class);
                intent.putExtra("tag_id", nfcModelList.get(position).getNfc_tag());
                context.startActivity(intent);
            }
        });*/
       // Bitmap bmp1 = BitmapFactory.decodeByteArray(nfcModelList.get(position).getUser_image(), 0, nfcModelList.get(position).getUser_image().length);
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
//        holder.image.setImageResource(nfcModelList.get(position).getUser_image());

    }

    @Override
    public int getCount() {
        return connectLists.size();
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
        CircleImageView image;
    }

    public void Filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        connectLists.clear();

        if(charText.length() == 0)
        {
            connectLists.addAll(connectListsFilter);
        }
        else
        {
            for(ConnectList md : connectListsFilter)
            {
                if(md.getFirstname().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    connectLists.add(md);
                }
                CardsActivity.setActionBarTitle("Cards - "+connectLists.size() + "/"+ CardsActivity.Connection_Limit);
            }
        }
        notifyDataSetChanged();
    }
}