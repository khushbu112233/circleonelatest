package com.circle8.circleOne.Model;

/**
 * Created by ample-arch on 10/9/2017.
 */

public class HistoryModel
{
    String MyUserID ;
    String MyProfileID;
    String FriendUserID;
    String FriendProfileID;
    String HistoryID;
    String History_Type;
    String History_Status;
    String History_Date;
    String FirstName;
    String LastName;
    String UserPhoto;

    public String getMyUserID() {
        return MyUserID;
    }

    public void setMyUserID(String myUserID) {
        MyUserID = myUserID;
    }

    public String getMyProfileID() {
        return MyProfileID;
    }

    public void setMyProfileID(String myProfileID) {
        MyProfileID = myProfileID;
    }

    public String getFriendUserID() {
        return FriendUserID;
    }

    public void setFriendUserID(String friendUserID) {
        FriendUserID = friendUserID;
    }

    public String getFriendProfileID() {
        return FriendProfileID;
    }

    public void setFriendProfileID(String friendProfileID) {
        FriendProfileID = friendProfileID;
    }

    public String getHistoryID() {
        return HistoryID;
    }

    public void setHistoryID(String historyID) {
        HistoryID = historyID;
    }

    public String getHistory_Type() {
        return History_Type;
    }

    public void setHistory_Type(String history_Type) {
        History_Type = history_Type;
    }

    public String getHistory_Status() {
        return History_Status;
    }

    public void setHistory_Status(String history_Status) {
        History_Status = history_Status;
    }

    public String getHistory_Date() {
        return History_Date;
    }

    public void setHistory_Date(String history_Date) {
        History_Date = history_Date;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }
}
