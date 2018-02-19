package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.GroupDetailAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityGroupDetailBinding;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.ConvertBitmapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.rotateImage;
import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class GroupDetailActivity extends AppCompatActivity
{
    public static ListView listView ;
    private static GroupDetailAdapter groupDetailAdapter ;
    private static ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();
    JSONArray selectArray = new JSONArray();
    JSONArray selectArray1 = new JSONArray();
    int selectListPosition ;
    ArrayList<String> selectedList = new ArrayList<>();
    private LoginSession session;
    private static String profile_id, user_id ;
    boolean isPressed = false;
    public static String group_id = "", group_Name, group_Desc, group_Img ;
    String GroupName, GroupDesc, GroupImage = "";
    String final_ImgBase64 = "";
    CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String image;
    public static JSONArray selectedStrings = new JSONArray();
    private static Activity mContext ;
    public static String loading ;
    public static ActivityGroupDetailBinding activityGroupDetailBinding;
    CircleImageView ivGroupImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityGroupDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_group_detail);
        mContext = GroupDetailActivity.this ;
        Utility.freeMemory();
        listView = (ListView)findViewById(R.id.listView);
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);
        user_id = user.get(LoginSession.KEY_USERID);

//        Toast.makeText(getApplicationContext(),"ProID & UID: "+ profile_id+" "+user_id, Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        group_id = intent.getStringExtra("group_id");
        group_Name = intent.getStringExtra("groupName");
        group_Desc = intent.getStringExtra("groupDesc");
        group_Img = intent.getStringExtra("groupImg");

        activityGroupDetailBinding.tvGroupName.setText(group_Name);
        activityGroupDetailBinding.tvGroupPartner.setText(group_Desc);

        if (group_Img.equals(""))
        {
            activityGroupDetailBinding.imgProfile.setImageResource(R.drawable.usr_white1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"Group/"+group_Img).resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(activityGroupDetailBinding.imgProfile);
        }

        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.group_detail_items, null);
       /* if (GroupDetailActivity.listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
            checkBox.setVisibility(View.VISIBLE);
        }
        else {
            checkBox.setVisibility(View.GONE);
        }*/

        groupDetailModelArrayList.clear();
       // callFirst();

        activityGroupDetailBinding.imgProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(GroupDetailActivity.this, activityGroupDetailBinding.imgProfileMenu);
                //Inflating the Popup using xml file
                popup.getMenu().add("Edit Circle");
                popup.getMenu().add("Delete Circle");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        if (item.getTitle().toString().equals("Delete Circle"))
                        {
                            AlertDialog.Builder alert = new AlertDialog.Builder(GroupDetailActivity.this, R.style.Blue_AlertDialog);
                            alert.setMessage("Are you sure want to delete Circle");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do your work here
                                    dialog.dismiss();
                                    new HttpAsyncTaskGroupDelete().execute(Utility.BASE_URL+"Group/Delete");

                                }
                            });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alert.show();

                        }
                        else if (item.getTitle().toString().equals("Edit Circle"))
                        {
                            Intent in = new Intent(getApplicationContext(), UpdateGroupActivity.class);
                            in.putExtra("type", "group_detail");
                            in.putExtra("GroupImage", group_Img);
                            in.putExtra("GroupName", group_Name);
                            in.putExtra("GroupDesc", group_Desc);
                            in.putExtra("GroupID", group_id);
                            startActivity(in);
                            finish();
                        }
                        return true;
                    }
                });
                popup.show();//showing popup men
            }
        });

        activityGroupDetailBinding.ivProfileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Utility.freeMemory();
                selectArray = new JSONArray();
                selectedList.clear();

                if(isPressed)
                {
                    listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    activityGroupDetailBinding.ivProfileShare.setImageResource(R.drawable.list_selecting);
                   /* listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    GroupDetailAdapter.chCheckBox.setVisibility(View.VISIBLE);*/
                }
                else {

                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    activityGroupDetailBinding.ivProfileShare.setImageResource(R.drawable.group_selected);
                    Toast.makeText(getApplicationContext(), "select member(s) to be removed from circle", Toast.LENGTH_LONG).show();
                    //  GroupDetailAdapter.chCheckBox.setVisibility(View.GONE);
                }


             /*   if (GroupDetailActivity.listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
                 //   GroupDetailAdapter.holder.chCheckBox.setVisibility(View.VISIBLE);
                   // notifyDataSetChanged();
                }
                else {
                  //  GroupDetailAdapter.holder.chCheckBox.setVisibility(View.GONE);
                    //notifyDataSetChanged();
                }*/

                isPressed = !isPressed;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3)
            {
                view.setSelected(true);
                String profileID = groupDetailModelArrayList.get(position).getProfileid() ;
                CardDetailActivity.profile_id = profileID;
                Intent iPut = new Intent(GroupDetailActivity.this, CardDetailActivity.class);
                //iPut.putExtra("profile_id", profileID);
                startActivity(iPut);
               // finish();
            }
        });

     /*  listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
           {
               Toast.makeText(getApplicationContext(),groupDetailModelArrayList.get(position).getProfileid(),Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });*/

        listView.setMultiChoiceModeListener(new MultiChoiceModeListener()
        {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean cecked)
            {
                selectListPosition = position ;
                Utility.freeMemory();
                // Capture total checked items
                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                groupDetailAdapter.toggleSelection(position);

                String p_Id = groupDetailModelArrayList.get(selectListPosition).getProfileid();

                if (cecked)
                {
                    selectArray.put(groupDetailModelArrayList.get(selectListPosition).getProfileid());
                    selectedList.add(groupDetailModelArrayList.get(selectListPosition).getProfileid());
//                    Toast.makeText(getApplicationContext(),"Select"+p_Id,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    selectArray.remove(position);
//                    selectArray.put(groupDetailModelArrayList.get(position).getProfileid());
                    selectedList.remove(groupDetailModelArrayList.get(selectListPosition).getProfileid());
//                    selectedList.add(groupDetailModelArrayList.get(position).getProfileid());
//                    Toast.makeText(getApplicationContext(),"UnSelect"+p_Id,Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = groupDetailAdapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--)
                        {
                            if (selected.valueAt(i))
                            {
                                GroupDetailModel selecteditem = (GroupDetailModel) groupDetailAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                //groupDetailAdapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                groupDetailAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        /*listView.setMultiChoiceModeListener(new  MultiChoiceModeListener() {

            @Override
            public boolean  onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                return false;
            }

            @Override
            public void  onDestroyActionMode(ActionMode mode) {
                // TODO  Auto-generated method stub

            }

            @Override
            public boolean  onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
               // mode.getMenuInflater().inflate(R.menu.multiple_delete, menu);
                return true;

            }

            @Override
            public boolean  onActionItemClicked(final ActionMode mode,
                                                MenuItem item) {
                // TODO  Auto-generated method stub
               *//* switch  (item.getItemId()) {
                    case R.id.selectAll:
                        //
                        final int checkedCount  = myList.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        adapter.removeSelection();
                        for (int i = 0; i <  checkedCount; i++) {
                            listView.setItemChecked(i,   true);
                            //  listviewadapter.toggleSelection(i);
                        }
                        // Set the  CAB title according to total checked items

                        // Calls  toggleSelection method from ListViewAdapter Class

                        // Count no.  of selected item and print it
                        mode.setTitle(checkedCount  + "  Selected");
                        return true;
                    case R.id.delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder  builder = new AlertDialog.Builder(
                                MainActivity.this);
                        builder.setMessage("Do you  want to delete selected record(s)?");

                        builder.setNegativeButton("No", new  OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub

                            }
                        });
                        builder.setPositiveButton("Yes", new  OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub
                                SparseBooleanArray  selected = adapter
                                        .getSelectedIds();
                                for (int i =  (selected.size() - 1); i >= 0; i--) {
                                    if  (selected.valueAt(i)) {
                                        String  selecteditem = adapter
                                                .getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                        adapter.remove(selecteditem);
                                    }
                                }

                                // Close CAB
                                mode.finish();
                                selected.clear();
                            }
                        });
                        AlertDialog alert =  builder.create();
                        alert.setIcon(R.drawable.questionicon);// dialog  Icon
                        alert.setTitle("Confirmation"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }*//*
               return false;
            }

            @Override
            public void  onItemCheckedStateChanged(ActionMode mode,
                                                   int position, long id, boolean checked) {
                // TODO  Auto-generated method stub
                final int checkedCount  = listView.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount  + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                groupDetailAdapter.toggleSelection(position);
        }
        });
*/

       activityGroupDetailBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getApplicationContext(), "Json Array & List Array "+selectArray.toString()+" "+selectedList.toString(), Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), String.valueOf(selectedList.size()), Toast.LENGTH_LONG).show();

//                selectArray1 = new JSONArray(selectedList);
//                Toast.makeText(getApplicationContext(), selectArray1.toString(), Toast.LENGTH_LONG).show();
                Utility.freeMemory();
                SparseBooleanArray  selected = groupDetailAdapter.getSelectedIds();
                for (int i =  (selected.size() - 1); i >= 0; i--) {
                    if  (selected.valueAt(i)) {
                        String  selecteditem = String.valueOf(groupDetailAdapter
                                .getItem(selected.keyAt(i)));
//                        Toast.makeText(getApplicationContext(), selecteditem, Toast.LENGTH_LONG).show();
                        // Remove  selected items following the ids
                        //   groupDetailAdapter.remove(selecteditem);
                    }
                }

                // Close CAB
                // mode.finish();
                selected.clear();

                if (selectedList.size() == 0)
                {
                    Toast.makeText(getApplicationContext(),"no member selected", Toast.LENGTH_LONG).show();
                }
                else
                {
                    selectArray1 = new JSONArray(selectedList);

                    new HttpAsyncTaskGroupDeleteMember().execute(Utility.BASE_URL+"Group/DeleteMembers");
                }
            }
        });

        activityGroupDetailBinding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(GroupDetailActivity.this);
                dialog.setContentView(R.layout.imageview_popup);
                Utility.freeMemory();
                ImageView ivViewImage = (ImageView)dialog.findViewById(R.id.ivViewImage);
                if (group_Img.equals(""))
                {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                }
                else
                {
                    Picasso.with(GroupDetailActivity.this).load(Utility.BASE_IMAGE_URL+"Group/"+group_Img).resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL+"Group/"+group_Img);
                        startActivity(intent);
                    }
                });

                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT ;
                wmlp.x = 50;
                wmlp.y = 150;   //y position*/
                dialog.show();
            }
        });

        activityGroupDetailBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GroupsActivity.backStatus = "DetailBack";
                finish();
                Utility.freeMemory();
            }
        });

        activityGroupDetailBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(getApplicationContext(), SearchGroupMembers.class);
                intent1.putExtra("from", "group");
                intent1.putExtra("GroupId", group_id);
                startActivity(intent1);
                Utility.freeMemory();
            }
        });
    }



    public void callFirst()
    {
        Utility.freeMemory();
        loading = "Fetching Connections" ;
        new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/FetchConnection");
    }

    public static void webCall()
    {
        Utility.freeMemory();
        loading = "Updating Connections" ;
        groupDetailModelArrayList.clear();
        try
        {
            groupDetailAdapter.notifyDataSetChanged();
        }
        catch (Exception e) { e.printStackTrace(); }

        new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/FetchConnection");
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupDetailModelArrayList.clear();
        callFirst();
        Utility.freeMemory();
    }

    private class HttpAsyncTaskGroupDelete extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Deleting Group...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Deleting circle" ;
            CustomProgressDialog(loading, GroupDetailActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupID", group_id );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();
            try
            {

                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equalsIgnoreCase("1"))
                    {
                        GroupsActivity.backStatus = "GroupDeleteBack";
                        finish();
                        Toast.makeText(getApplicationContext(), "Circle successfully deleted..", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to delete circle..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Uploading" ;
            CustomProgressDialog(loading, GroupDetailActivity.this);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ImgBase64", final_ImgBase64 );
                jsonObject.accumulate("classification", "group" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName!=null)
                    {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        //   Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        GroupImage = ImgName;
                        new HttpAsyncTaskGroupUpdate().execute(Utility.BASE_URL+"Group/Update");

                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Error while uploading image..", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to upload..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskGroupUpdate extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Creating Circle...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Creating circle" ;
            CustomProgressDialog(loading, GroupDetailActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupDesc", GroupDesc);
                jsonObject.accumulate("GroupName", GroupName);
                jsonObject.accumulate("GroupPhoto", GroupImage);
                jsonObject.accumulate("UserId", user_id);
                jsonObject.accumulate("GroupId", group_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success").toString();
                    String Message = jsonObject.getString("Message").toString();
                    if (Success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Circle updated..", Toast.LENGTH_LONG).show();

                        activityGroupDetailBinding.tvGroupName.setText(GroupName);
                        activityGroupDetailBinding.tvGroupPartner.setText(GroupDesc);

                        if (GroupImage.equals(""))
                        {
                            activityGroupDetailBinding.imgProfile.setImageResource(R.drawable.usr_white1);
                        }
                        else
                        {
                            Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL+"Group/"+GroupImage).resize(300,300).onlyScaleDown().skipMemoryCache().placeholder(R.drawable.usr_1).into(activityGroupDetailBinding.imgProfile);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to create circle..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Fetching Connections...");
            dialog.show();
            dialog.setCancelable(false);*/


            CustomProgressDialog(loading, mContext);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("group_ID", group_id);
                jsonObject.accumulate("profileId", profile_id);
                jsonObject.accumulate("pageno", "1");
                jsonObject.accumulate("numofrecords", "50");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        activityGroupDetailBinding.tvMemberInfo.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                    {
                        activityGroupDetailBinding.tvMemberInfo.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    groupDetailModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        GroupDetailModel groupDetailModel = new GroupDetailModel();
                        groupDetailModel.setFirstname(object.getString("FirstName"));
                        groupDetailModel.setLastname(object.getString("LastName"));
                        groupDetailModel.setDesignation(object.getString("Designation"));
                        groupDetailModel.setCompany(object.getString("CompanyName"));
                        groupDetailModel.setEmail(object.getString("UserName"));
                        groupDetailModel.setWebsite(object.getString("Website"));
                        groupDetailModel.setMobile(object.getString("Phone"));
                        groupDetailModel.setAddress1(object.getString("Address1"));
                        groupDetailModel.setAddress2(object.getString("Address2"));
                        groupDetailModel.setAddress3(object.getString("Address3"));
                        groupDetailModel.setAddress4(object.getString("Address4"));
                        groupDetailModel.setCity(object.getString("City"));
                        groupDetailModel.setState(object.getString("State"));
                        groupDetailModel.setCountry(object.getString("Country"));
                        groupDetailModel.setPostalcode(object.getString("Postalcode"));
                        groupDetailModel.setImgProfile(object.getString("UserPhoto"));
                        groupDetailModel.setProfileid(object.getString("ProfileId"));
                        groupDetailModelArrayList.add(groupDetailModel);
                    }

                    groupDetailAdapter = new GroupDetailAdapter(mContext, R.layout.group_detail_items, groupDetailModelArrayList);
                    listView.setAdapter(groupDetailAdapter);
                    groupDetailAdapter.notifyDataSetChanged();
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(mContext, "Not able to load circles..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(GroupDetailActivity.this);
                boolean result1 = Utility.checkCameraPermission(GroupDetailActivity.this);
                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask ="Take Photo";
                    if(result1)
//                        activeTakePhoto();
                        cameraIntent();
                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask ="Choose from Library";
                    if(result)
//                        activeGallery();
                        galleryIntent();
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Utility.freeMemory();
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Utility.freeMemory();
        final_ImgBase64 = BitMapToString(thumbnail);
        //  Upload();
        ivGroupImage.setImageBitmap(thumbnail);
    }

    public String getPath(Uri uri)
    {
        Utility.freeMemory();
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null)
        {
            Uri targetUri = data.getData();

            String photoPath = getPath(targetUri);

            ExifInterface ei = null;
            Bitmap bitmap = null;
            Bitmap rotatedBitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                try
                {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    final_ImgBase64 = BitMapToString(resizedBitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                    // Upload();
                    ivGroupImage.setImageBitmap(resizedBitmap);
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    ei = new ExifInterface(String.valueOf(targetUri));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    switch (orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            //  Upload();
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                            //  Upload();
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

           /* try
            {
                ei = new ExifInterface(photoPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/

//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

//            Bitmap bitmap = null;
//            Bitmap rotatedBitmap = null;

           /* try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
//                image = ConvertBitmapToString(resizedBitmap);
//                final_ImgBase64 = BitMapToString(resizedBitmap);
               // final_ImgBase64 = resizeBase64Image(s);
                Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
//                Upload();
//                civProfilePic.setImageBitmap(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

/*
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
            }
*/

        }
//        BmToString(bm);
    }

    private class HttpAsyncTaskGroupDeleteMember extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Deleting Circle Member...");
//            dialog.show();
            dialog.setCancelable(false);

            String loading = "Deleting circle member" ;
            CustomProgressDialog(loading, GroupDetailActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupID", group_id);
                jsonObject.accumulate("ProfileIDs", selectArray1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Circle member(s) deleted", Toast.LENGTH_LONG).show();
                        webCall();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to delete member..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
