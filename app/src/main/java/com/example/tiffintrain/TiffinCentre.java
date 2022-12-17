package com.example.tiffintrain;

import android.widget.CheckBox;

import java.io.Serializable;
import java.util.ArrayList;

public class TiffinCentre implements Serializable {
    private String name ;
    private String address ;
    private String email ;
    private int pincode ;
    private String contactNo ;
    private double centre_latitude;
    private double centre_longitude;
    private String myTiffinCentreImageUrl ;
    private ArrayList<String> menuUIds ;
    private String upi_id;
    private String paytm_username;
    private boolean isChapatiAddOn ;
    private boolean isSweetdishAddOn ;
    private boolean isCurdAddOn ;
    private int chapatiRate ;
    private int sweetdishRate ;
    private int curdRate ;

    public TiffinCentre(){

    }

    public TiffinCentre(String name , String email , String address , int pincode , String contactNo , double centre_latitude , double centre_longitude, String upi_id,String paytm_username){
        this.name = name ;
        this.address = address ;
        this.email = email ;
        this.contactNo = contactNo ;
        this.pincode= pincode ;
        this.centre_latitude = centre_latitude ;
        this.centre_longitude = centre_longitude ;
        this.upi_id = upi_id;
        this.paytm_username = paytm_username;
        this.isChapatiAddOn = false ;
        this.isSweetdishAddOn = false ;
        this.isCurdAddOn = false ;
        this.chapatiRate = 0 ;
        this.sweetdishRate = 0 ;
        this.curdRate = 0 ;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public int getPincode() {
        return pincode;
    }

    public String getAddress() {
        return address;
    }

    public double getCentre_latitude() {
        return centre_latitude;
    }

    public double getCentre_longitude() {
        return centre_longitude;
    }

    public String getMyTiffinCentreImageUrl() {
        return myTiffinCentreImageUrl;
    }

    public ArrayList<String> getMenuUIds() {
        return menuUIds ;
    }

    public String getUpi_id(){
        return upi_id;
    }

    public String getPaytm_username(){
        return paytm_username;
    }

    public int getChapatiRate() {
        return chapatiRate;
    }

    public int getSweetdishRate() {
        return sweetdishRate;
    }

    public int getCurdRate() {
        return curdRate;
    }

    public boolean getIsChapatiAddOn() {
        return isChapatiAddOn;
    }

    public boolean getIsSweetdishAddOn() {
        return isSweetdishAddOn;
    }

    public boolean getIsCurdAddOn() {
        return isCurdAddOn;
    }
}
