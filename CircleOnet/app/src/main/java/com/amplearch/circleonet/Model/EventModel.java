package com.amplearch.circleonet.Model;

/**
 * Created by ample-arch on 8/29/2017.
 */

public class EventModel
{
    String Event_ID ;
    String Event_Name ;
    String Event_Image ;
    String Event_Desc ;
    String Event_StartDate ;
    String Event_EndDate ;
    String CompanyName ;
    String IndustryName ;
    String Address1 ;
    String Address2 ;
    String Address3 ;
    String Address4 ;
    String City ;
    String State ;
    String Country ;
    String PostalCode ;

    public EventModel() {    }

    public EventModel(String Event_ID, String Event_Name, String Event_Image, String Event_Desc,
            String Event_StartDate, String Event_EndDate, String CompanyName, String IndustryName,
            String Address1, String Address2, String Address3, String Address4,
            String City, String State, String Country, String PostalCode)
    {
        this.Event_ID        = Event_ID        ;
        this.Event_Name      = Event_Name      ;
        this.Event_Image     = Event_Image     ;
        this.Event_Desc      = Event_Desc      ;
        this.Event_StartDate = Event_StartDate ;
        this.Event_EndDate   = Event_EndDate   ;
        this.CompanyName     = CompanyName     ;
        this.IndustryName    = IndustryName    ;
        this.Address1        = Address1        ;
        this.Address2        = Address2        ;
        this.Address3        = Address3        ;
        this.Address4        = Address4        ;
        this.City            = City            ;
        this.State           = State           ;
        this.Country         = Country         ;
        this.PostalCode      = PostalCode      ;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getEvent_Name() {
        return Event_Name;
    }

    public void setEvent_Name(String event_Name) {
        Event_Name = event_Name;
    }

    public String getEvent_Image() {
        return Event_Image;
    }

    public void setEvent_Image(String event_Image) {
        Event_Image = event_Image;
    }

    public String getEvent_Desc() {
        return Event_Desc;
    }

    public void setEvent_Desc(String event_Desc) {
        Event_Desc = event_Desc;
    }

    public String getEvent_StartDate() {
        return Event_StartDate;
    }

    public void setEvent_StartDate(String event_StartDate) {
        Event_StartDate = event_StartDate;
    }

    public String getEvent_EndDate() {
        return Event_EndDate;
    }

    public void setEvent_EndDate(String event_EndDate) {
        Event_EndDate = event_EndDate;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getIndustryName() {
        return IndustryName;
    }

    public void setIndustryName(String industryName) {
        IndustryName = industryName;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String address3) {
        Address3 = address3;
    }

    public String getAddress4() {
        return Address4;
    }

    public void setAddress4(String address4) {
        Address4 = address4;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }
}
