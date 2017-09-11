package com.circle8.circleOne.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.circle8.circleOne.Adapter.GroupDetailAdapter;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity
{
    private ListView listView ;
    private CircleImageView imgProfile ;
    private ImageView ivChangeProf, ivBack, ivMenu, ivShare, ivEdit ;
    private TextView tvGroupName, tvGroupPartner ;

    private GroupDetailAdapter groupDetailAdapter ;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        listView = (ListView)findViewById(R.id.listView);
        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);

        tvGroupName = (TextView)findViewById(R.id.tvGroupName);
        tvGroupPartner = (TextView)findViewById(R.id.tvGroupPartner);

        ivBack = (ImageView)findViewById(R.id.imgBack);
        ivMenu = (ImageView)findViewById(R.id.imgProfileMenu);
        ivChangeProf = (ImageView)findViewById(R.id.imgCamera);
        ivShare = (ImageView)findViewById(R.id.ivProfileShare);
        ivEdit = (ImageView)findViewById(R.id.ivEdit);

        name.add("Kajal Patadia");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Jay Nagar");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Sameer Desai");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Nagar Joy");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");


        groupDetailAdapter = new GroupDetailAdapter(getApplicationContext(), R.layout.group_detail_items,
                name,designation,company,website,email,phone,mobile,address,imgprofile);
        listView.setAdapter(groupDetailAdapter);
        groupDetailAdapter.notifyDataSetChanged();
    }
}
