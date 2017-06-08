package com.amplearch.circleonet;

import android.graphics.Bitmap;

/**
 * Created by admin on 06/08/2017.
 */

public class List3Model {

    private Bitmap image;
    private String title;
    private String desc;
    private String designation;

    public List3Model(Bitmap image, String title, String desc, String designation) {
        super();
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.designation = designation;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
