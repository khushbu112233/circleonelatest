package com.amplearch.circleonet.Model;

/**
 * Created by admin on 08/29/2017.
 */

public class NotificationModel {

    String FriendUserID = "";
    String FriendProfileID = "";
    String FirstName = "";
    String LastName = "";
    String UserPhoto = "";
    String Purpose = "";

    public NotificationModel() {
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

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }
}
