package com.amplearch.circleonet.Model;

/**
 * Created by admin on 08/21/2017.
 */

public class ProfileModel {

    String UserID = "";
    String FirstName = "";
    String LastName = "";
    String UserName = "";
    String ProfileID = "";
    String Card_Front = "";
    String Card_Back = "";
    String UserPhoto = "";
    String Designation = "";
    String CompanyName = "";
    String Phone = "";

    public ProfileModel() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getProfileID() {
        return ProfileID;
    }

    public void setProfileID(String profileID) {
        ProfileID = profileID;
    }

    public String getCard_Front() {
        return Card_Front;
    }

    public void setCard_Front(String card_Front) {
        Card_Front = card_Front;
    }

    public String getCard_Back() {
        return Card_Back;
    }

    public void setCard_Back(String card_Back) {
        Card_Back = card_Back;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
