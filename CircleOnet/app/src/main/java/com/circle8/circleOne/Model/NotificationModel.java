package com.circle8.circleOne.Model;

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
    String Testimonial_ID = "";
    String NotificationID = "";
    String Status = "";

    public NotificationModel() {
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
}
