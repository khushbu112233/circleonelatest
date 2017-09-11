package com.circle8.circleOne.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.circle8.circleOne.Adapter.NewCardRequestAdapter;
import com.circle8.circleOne.Model.NewCardModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

public class NewCardRequestActivity extends AppCompatActivity
{
    private ListView listView ;
    private ArrayList<NewCardModel> newCardModelArrayList ;
    private NewCardRequestAdapter newCardRequestAdapter ;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> image = new ArrayList<>();
    private ArrayList<String> profile = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_request);

        listView = (ListView)findViewById(R.id.listView);

        newCardModelArrayList = new ArrayList<>();

        NewCardModel newCardModel = new NewCardModel();

        newCardModel.setPersonName("Wesley Wen");
        newCardModel.setPersonDesignation("Business Development Director");
        newCardModel.setPersonCompany("UNICO Creative Pvt. Ltd.");
        newCardModel.setPersonPhone("+65 6842 6188  +65 9735 4641");
        newCardModel.setPersonEmail("wesley.wan@unico-creative.com");
        newCardModel.setPersonProfile("Profile 1");
        newCardModel.setPersonImage(String.valueOf(R.drawable.final_profile5));
        newCardModelArrayList.add(newCardModel);

        newCardModel.setPersonName("Shani Shah");
        newCardModel.setPersonDesignation("Business Development Director");
        newCardModel.setPersonCompany("Ample Arch Pvt. Ltd.");
        newCardModel.setPersonPhone("+65 6842 6188  +65 9735 4641");
        newCardModel.setPersonEmail("shani.shah@unico-creative.com");
        newCardModel.setPersonProfile("Profile 1");
        newCardModel.setPersonImage(String.valueOf(R.drawable.final_profile14));
        newCardModelArrayList.add(newCardModel);

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 1");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 2");
        image.add(String.valueOf(R.drawable.final_profile14));

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 3");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 4");
        image.add(String.valueOf(R.drawable.final_profile14));

        name.add("Wesley Wen");
        designation.add("Business Development Director");
        company.add("UNICO Creative Pvt. Ltd.");
        email.add("wesley.wan@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 5");
        image.add(String.valueOf(R.drawable.final_profile5));

        name.add("Shani Shah");
        designation.add("Business Development Director");
        company.add("Ample Arch Pvt. Ltd.");
        email.add("shani.shah@unico-creative.com");
        phone.add("+65 6842 6188  +65 9735 4641");
        profile.add("Profile 6");
        image.add(String.valueOf(R.drawable.final_profile14));


//        newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(), newCardModelArrayList);
        newCardRequestAdapter = new NewCardRequestAdapter(getApplicationContext(),
                name,company,designation,email,phone,profile,image);
        listView.setAdapter(newCardRequestAdapter);
        newCardRequestAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getApplicationContext(), NewCardRequestDetailActivity.class);
                i.putExtra("person", name.get(position));
                i.putExtra("designation", designation.get(position));
                i.putExtra("company", company.get(position));
                i.putExtra("profile", profile.get(position));
                i.putExtra("image", image.get(position));
                i.putExtra("phone", phone.get(position));
                startActivity(i);
            }
        });
    }
}
