package com.amplearch.circleonet.Model;

/**
 * Created by ample-arch on 8/28/2017.
 */

public class ConnectList
{
    String firstname ;
    String lastname ;
    String username ;
    String userphoto ;
    String card_front ;
    String card_back ;
    String profile_id ;
    String phone ;
    String companyname ;
    String designation ;
    String facebook ;
    String twitter ;
    String google ;
    String linkedin ;
    String website ;

    public ConnectList() {    }

    public ConnectList(String firstname, String lastname, String username, String userphoto, String card_front,
            String card_back, String profile_id, String phone, String companyname, String designation,
            String facebook, String twitter, String google, String linkedin, String website)
    {
        this.firstname = firstname ;
        this.lastname = lastname ;
        this.username = username ;
        this.userphoto = userphoto ;
        this.card_front = card_front ;
        this.card_back = card_back ;
        this.profile_id = profile_id ;
        this.phone = phone ;
        this.companyname = companyname ;
        this.designation = designation ;
        this.facebook = facebook ;
        this.twitter = twitter ;
        this.google = google ;
        this.linkedin = linkedin ;
        this.website = website ;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getCard_front() {
        return card_front;
    }

    public void setCard_front(String card_front) {
        this.card_front = card_front;
    }

    public String getCard_back() {
        return card_back;
    }

    public void setCard_back(String card_back) {
        this.card_back = card_back;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
