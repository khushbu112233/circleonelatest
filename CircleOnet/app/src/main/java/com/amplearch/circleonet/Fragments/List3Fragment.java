package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Adapter.List3Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class List3Fragment extends Fragment {

    private ListView listView;
    private List3Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;

    List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    ArrayList<NFCModel> nfcModel ;

    public List3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list3, container, false);

        db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        designation = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.listViewType3);

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();

        allTags = db.getActiveNFC();

       /* for (NFCModel tag : allTags)
        {
            imgf.add(tag.getCard_front());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }
        gridAdapter = new List3Adapter(getContext(), R.layout.grid_list3_layout, imgf, desc, name, designation);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();*/

        //newly added
        /*for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());

            nfcModel.add(nfcModelTag);
        }
        gridAdapter = new List3Adapter(getContext(), R.layout.grid_list3_layout, nfcModel);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();*/

        //retrive data
        GetData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CardDetail.class);
                getContext().startActivity(intent);
            }
        });



        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if(searchText.getText().toString().length() == 0)
                {
                    nfcModel.clear();
                    GetData();
                }
                else
                {
                    String text = searchText.getText().toString().toLowerCase(Locale.getDefault());
                    gridAdapter.Filter(text);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        nfcModel.clear();
        GetData();
    }

    private void GetData()
    {
        //newly added
        for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());

            nfcModel.add(nfcModelTag);
        }
        gridAdapter = new List3Adapter(getContext(), R.layout.grid_list3_layout, nfcModel);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

}
