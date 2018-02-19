package com.circle8.circleOne.Model;

/**
 * Created by ample-arch on 2/15/2018.
 */

public class CardPrize {
    String Prize_ID;
    String Prize_Name;
    String Prize_Image;
    String Total_Prize_Count;
    String Prize_Won_Count;
    String Prize_Available_Count;

    public String getPrize_ID() {
        return Prize_ID;
    }

    public void setPrize_ID(String prize_ID) {
        Prize_ID = prize_ID;
    }

    public String getPrize_Name() {
        return Prize_Name;
    }

    public void setPrize_Name(String prize_Name) {
        Prize_Name = prize_Name;
    }

    public String getPrize_Image() {
        return Prize_Image;
    }

    public void setPrize_Image(String prize_Image) {
        Prize_Image = prize_Image;
    }

    public String getTotal_Prize_Count() {
        return Total_Prize_Count;
    }

    public void setTotal_Prize_Count(String total_Prize_Count) {
        Total_Prize_Count = total_Prize_Count;
    }

    public String getPrize_Won_Count() {
        return Prize_Won_Count;
    }

    public void setPrize_Won_Count(String prize_Won_Count) {
        Prize_Won_Count = prize_Won_Count;
    }

    public String getPrize_Available_Count() {
        return Prize_Available_Count;
    }

    public void setPrize_Available_Count(String prize_Available_Count) {
        Prize_Available_Count = prize_Available_Count;
    }
}
