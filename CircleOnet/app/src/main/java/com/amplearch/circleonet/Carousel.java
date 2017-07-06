package com.amplearch.circleonet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.amplearch.circleonet.Activity.MainActivity;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

import java.util.ArrayList;

public class Carousel extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Integer> images;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        images = new ArrayList<>();
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        images.add(R.drawable.card1);
        images.add(R.drawable.card2);
        images.add(R.drawable.card3f);
        images.add(R.drawable.card4);
        images.add(R.drawable.card5);
        images.add(R.drawable.card6);
        images.add(R.drawable.card7);
        mAdapter = new GalleryAdapter(getApplicationContext(), images);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }
}
