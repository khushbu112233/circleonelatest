package com.circle8.circleOne.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.Walkthrough.HelpActivity;

/**
 * Created by admin on 09/12/2017.
 */

public class TutorialScreenFragment extends Fragment
{
    private static String IMG_ID = "imgId";
    private static String TIT_ID = "titId";
    private ImageView image;
    private int imageResId;

    private TextView title;
    private int titleResId;

    public static TutorialScreenFragment newInstance(int imageResId, int titleResId) {
        final TutorialScreenFragment f = new TutorialScreenFragment();
        final Bundle args = new Bundle();
        args.putInt(IMG_ID, imageResId);
        args.putInt(TIT_ID, titleResId);
        f.setArguments(args);
        return f;
    }

    // Empty constructor, required as per Fragment docs
    public TutorialScreenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.imageResId = arguments.getInt(IMG_ID);
            this.titleResId = arguments.getInt(TIT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Identify and set fields!
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_screen, container, false);
        image = (ImageView) rootView.findViewById(R.id.tutorial_screen_image);
        title = (TextView) rootView.findViewById(R.id.tutorial_screen_title);
        return rootView;
    }

    @Override
    public void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get the font
        // Populate fields with info!
        if (HelpActivity.class.isInstance(getActivity())) {
            title.setText(titleResId);
            // Call Glide to load image
            Glide.with(this)
                    .load(imageResId)
                    .centerCrop()
                    .into(image);
        }
    }
}
