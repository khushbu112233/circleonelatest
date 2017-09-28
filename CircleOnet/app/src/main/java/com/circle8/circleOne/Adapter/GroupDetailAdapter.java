package com.circle8.circleOne.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.circle8.circleOne.Activity.GroupDetailActivity;
import com.circle8.circleOne.Activity.ImageZoom;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.GroupDetailActivity.selectedStrings;
import static com.circle8.circleOne.Activity.ConnectActivity.selectedStrings1;

/**
 * Created by ample-arch on 9/9/2017.
 */

public class GroupDetailAdapter extends BaseSwipeAdapter
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> website = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> imgprofile = new ArrayList<>();
    public static ViewHolder holder;
    private SparseBooleanArray mSelectedItemsIds;
    View row;
    public GroupDetailAdapter(Context applicationContext, int group_detail_items,
           ArrayList<String> name, ArrayList<String> designation, ArrayList<String> company,
           ArrayList<String> website, ArrayList<String> email, ArrayList<String> phone,
           ArrayList<String> mobile, ArrayList<String> address, ArrayList<String> imgprofile)
    {
        this.context = applicationContext ;
        this.layoutResourceId = group_detail_items ;
        this.name = name ;
        this.designation = designation ;
        this.company = company ;
        this.website = website ;
        this.email = email ;
        this.phone = phone ;
        this.mobile = mobile;
        this.address = address ;
        this.imgprofile = imgprofile ;
    }

    public GroupDetailAdapter(Activity applicationContext, int group_detail_items,
                              ArrayList<GroupDetailModel> groupDetailModelArrayList)
    {
        mSelectedItemsIds = new  SparseBooleanArray();
        this.context = applicationContext ;
        this.layoutResourceId = group_detail_items ;
        this.groupDetailModelArrayList = groupDetailModelArrayList;
    }

    // get List after update or delete
    public  ArrayList<GroupDetailModel> getMyList() {
        return groupDetailModelArrayList;
    }

    public void  toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    // Remove selection after unchecked
    public void  removeSelection() {
        mSelectedItemsIds = new  SparseBooleanArray();
        notifyDataSetChanged();
    }

    // Item checked on selection
    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);

               /* row.setSelected(true);
                row.setPressed(true);
                row.setBackgroundColor(Color.parseColor("#FF9912"));*/
        }
        else {
            mSelectedItemsIds.delete(position);

              /*  row.setSelected(false);
                row.setPressed(false);
                row.setBackgroundColor(Color.parseColor("#ffffff"));*/
        }
        notifyDataSetChanged();
    }

    // Get number of selected item
    public int  getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public  SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.group_detail_items, null);

        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                swipeLayout.close();
            }
        });

        return v ;
    }

    @Override
    public void fillValues(final int position, View convertView)
    {
        row = convertView;
        holder = null;

        holder = new ViewHolder();
        holder.personName = (TextView) row.findViewById(R.id.tvPersonName);
        holder.personDesignation = (TextView) row.findViewById(R.id.tvDesignation);
        holder.personDetail = (TextView) row.findViewById(R.id.tvPersonDetail);
        holder.personImage = (CircleImageView) row.findViewById(R.id.imgProfile);
       // holder.chCheckBox = (CheckBox) row.findViewById(R.id.chCheckBox);
        row.setTag(holder);

        holder.personName.setText(groupDetailModelArrayList.get(position).getFirstname()+" "+
                groupDetailModelArrayList.get(position).getLastname());
        holder.personDesignation.setText(groupDetailModelArrayList.get(position).getDesignation());

        /*String Company = company.get(position);
        String Email = email.get(position);
        String Website = website.get(position);
        String Phone = phone.get(position);
        String Mobile = mobile.get(position);
        String Address = address.get(position);*/

      /*  if (GroupDetailActivity.listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
            chCheckBox.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
        else {
            chCheckBox.setVisibility(View.GONE);
            notifyDataSetChanged();
        }

        chCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    selectedStrings.put(groupDetailModelArrayList.get(position).getProfileid());
                }
                else
                {
                    selectedStrings.remove(position);
                }
            }
        });
*/
        String Company = groupDetailModelArrayList.get(position).getCompany();
        String Email = groupDetailModelArrayList.get(position).getEmail();
        String Website = groupDetailModelArrayList.get(position).getWebsite();
        String Phone = groupDetailModelArrayList.get(position).getMobile();
        String Address1 = groupDetailModelArrayList.get(position).getAddress1();
        String Address2 = groupDetailModelArrayList.get(position).getAddress2();
        String Address3 = groupDetailModelArrayList.get(position).getAddress2();
        String Address4 = groupDetailModelArrayList.get(position).getAddress4();
        String City = groupDetailModelArrayList.get(position).getCity();
        String State = groupDetailModelArrayList.get(position).getState();
        String Country = groupDetailModelArrayList.get(position).getCountry();
        String PostalCode = groupDetailModelArrayList.get(position).getPostalcode();
        String Address = Address1+" "+Address2+" "+Address3+" "+Address4+"\n"+City+" "+State+" "+Country+" "+PostalCode;

        holder.personDetail.setText(Address+"\n"+Company+"\n"+Website+"\n"+Email+"\n"+Phone);

        groupDetailModelArrayList.get(position).getImgProfile() ;

        if (groupDetailModelArrayList.get(position).getImgProfile().equals(""))
        {
            holder.personImage.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+groupDetailModelArrayList.get(position).getImgProfile()).placeholder(R.drawable.usr_1).into(holder.personImage);
        }

        holder.personImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (groupDetailModelArrayList.get(position).getImgProfile().equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+groupDetailModelArrayList.get(position).getImgProfile()).placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, ImageZoom.class);
                        intent.putExtra("displayProfile", "http://circle8.asia/App_ImgLib/UserProfile/"+groupDetailModelArrayList.get(position).getImgProfile());
                        context.startActivity(intent);
                    }
                });
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                dialog.show();
            }
        });

    }

    public static class ViewHolder
    {
        TextView personName;
        TextView personDesignation;
        TextView personDetail;
        CircleImageView personImage;
        public static CheckBox chCheckBox;
    }

    @Override
    public int getCount() {
        return groupDetailModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
