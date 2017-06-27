package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Adapter.GridViewAdapter;
import com.amplearch.circleonet.Adapter.List3Adapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.ImageItem;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;
import com.daimajia.swipe.util.Attributes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class List2Fragment extends Fragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    ArrayList<byte[]> imgf;
    DatabaseHelper db ;
    float x1,x2;
    float y1, y2;
    RelativeLayout lnrSearch;
    View line;

    List<NFCModel> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    ArrayList<NFCModel> nfcModel ;
    public List2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list2, container, false);
        /*db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();

        List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getCard_front());
        }*/

        db = new DatabaseHelper(getContext());
        gridView = (GridView) view.findViewById(R.id.gridView);
        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        nfcModel = new ArrayList<>();
        allTags = db.getActiveNFC();
       // gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, getData());
       // gridView.setAdapter(gridAdapter);
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        lnrSearch.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        CardsFragment.tabLayout.setVisibility(View.GONE);

        GetData();

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < imgf.size(); i++) {

            Bitmap bmp = BitmapFactory.decodeByteArray(imgf.get(i), 0, imgf.get(i).length);
            // ImageView image = (ImageView) findViewById(R.id.imageView1);

          //  imageView.setImageBitmap(bmp);

            /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            Log.d ("image", stream.toByteArray().toString());*/
            //Toast.makeText(getContext(), stream.toByteArray().toString(), Toast.LENGTH_LONG).show();
            imageItems.add(new ImageItem(bmp, "Image#" + i));
        }
        return imageItems;
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            try {
                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    swipe += "Swipe Right\n";
                } else {
                    swipe += "\n";
                }

                if ((e1.getY() - e2.getY()) > sensitvity) {
                    swipe += "Swipe Up\n";
                    lnrSearch.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                } else {
                    swipe += "\n";
                }

                //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

                return super.onFling(e1, e2, velocityX, velocityY);
            }
            catch (Exception e ){
                return true;
            }
        }
    };

    GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);


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
            nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());

            nfcModel.add(nfcModelTag);
        }
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, nfcModel);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        gridAdapter.setMode(Attributes.Mode.Single);

    }
}
