package com.circle8.circleOne.Model;

/**
 * Created by ample-arch on 2/20/2018.
 */

public class PrizeHistory  {
    String UserId;
    String Prize_ID;
    String Prize_Name;
    String Prize_Image;
    String Card_Name_1;
    String Card_Name_2;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

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

    public String getCard_Name_1() {
        return Card_Name_1;
    }

    public void setCard_Name_1(String card_Name_1) {
        Card_Name_1 = card_Name_1;
    }

    public String getCard_Name_2() {
        return Card_Name_2;
    }

    public void setCard_Name_2(String card_Name_2) {
        Card_Name_2 = card_Name_2;
    }
}
