package com.example.tiffintrain;

public class Transactions {

    String user_email;
    String tiffin_center_email;
    String amount;
    String timestamp ;

    public Transactions(){

    }

    public Transactions(String user_email,String tiffin_center_email,String amount , String timestamp)
    {
        this.user_email = user_email;
        this.tiffin_center_email = tiffin_center_email;
        this.amount = amount;
        this.timestamp = timestamp ;
    }

    public String getUser_email(){
        return user_email;
    }

    public String getTiffin_center_email(){
        return tiffin_center_email;
    }

    public String getAmount(){
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
