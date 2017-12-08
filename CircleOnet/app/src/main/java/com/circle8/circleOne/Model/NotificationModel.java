package com.circle8.circleOne.Model;

/**
 * Created by admin on 08/29/2017.
 */

public class NotificationModel {

    String FriendUserID = "";
    String FriendProfileID = "";
    String MyUserID = "";
    String MyProfileID = "";
    String FirstName = "";
    String LastName = "";
    String UserPhoto = "";
    String Purpose = "";
    String Testimonial_ID = "";
    String NotificationID = "";
    String Status = "";
    String Shared_UserID = "";
    String Shared_ProfileID = "";
    String Viewed_Flag = "";
    String listCount = "";

    public NotificationModel() {
    }

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

    public String getShared_UserID() {
        return Shared_UserID;
    }

    public void setShared_UserID(String shared_UserID) {
        Shared_UserID = shared_UserID;
    }

    public String getShared_ProfileID() {
        return Shared_ProfileID;
    }

    public void setShared_ProfileID(String shared_ProfileID) {
        Shared_ProfileID = shared_ProfileID;
    }

    public String getViewed_Flag() {
        return Viewed_Flag;
    }

    public void setViewed_Flag(String viewed_Flag) {
        Viewed_Flag = viewed_Flag;
    }

    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTestimonial_ID() {
        return Testimonial_ID;
    }

    public void setTestimonial_ID(String testimonial_ID) {
        Testimonial_ID = testimonial_ID;
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

    public String getListCount() {
        return listCount;
    }

    public void setListCount(String listCount) {
        this.listCount = listCount;
    }
}
