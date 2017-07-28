package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.CardsActivity;
import com.amplearch.circleonet.Activity.ConnectActivity;
import com.amplearch.circleonet.Adapter.List4Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.CustomViewPager;
import com.amplearch.circleonet.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectListFragment extends Fragment {

    private ListView listView;
    private List4Adapter gridAdapter;
    ArrayList<byte[]> imgf;
    ArrayList<String> name;
    ArrayList<String> desc;
    ArrayList<String> designation;
    DatabaseHelper db ;

    List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    ArrayList<NFCModel> nfcModel ;

    public ConnectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect_list, container, false);

        /*List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getUser_image());
            name.add(tag.getName());
            desc.add(tag.getCompany() + "\n" + tag.getEmail() + "\n" + tag.getWebsite() + "\n" + tag.getMob_no());
            designation.add(tag.getDesignation());
        }
*/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        listView = (ListView) view.findViewById(R.id.listViewType4);

        allTags = new ArrayList<>();

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();
        new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       // gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, imgf, desc, name, designation);
        //listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                getContext().startActivity(intent);
            }
        });



        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() <= 0)
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
        nfcModel.clear();
        for(NFCModel reTag : allTags)
        {
            NFCModel nfcModelTag = new NFCModel();
            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setUser_image(reTag.getUser_image());

            nfcModel.add(nfcModelTag);
        }
        gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, nfcModel);
        listView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        String data1;
        String data2;
        Bitmap data3;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseHelper(getContext());
            imgf = new ArrayList<byte[]>();
            name = new ArrayList<>();
            desc = new ArrayList<>();
          //  allTags = new ArrayList<>();
            designation = new ArrayList<>();

            allTags = db.getActiveNFC();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData();
        }

    }


}
