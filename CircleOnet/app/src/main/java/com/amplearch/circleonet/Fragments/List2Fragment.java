package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Adapter.GridViewAdapter;
import com.amplearch.circleonet.Helper.DatabaseHelper;
import com.amplearch.circleonet.Model.ImageItem;
import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class List2Fragment extends Fragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    ArrayList<byte[]> imgf;
    DatabaseHelper db ;

    public List2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list2, container, false);
        db = new DatabaseHelper(getContext());
        imgf = new ArrayList<byte[]>();

        List<NFCModel> allTags = db.getActiveNFC();
        for (NFCModel tag : allTags) {
            imgf.add(tag.getCard_front());
        }

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_list2_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CardDetail.class);
                getContext().startActivity(intent);
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

}
