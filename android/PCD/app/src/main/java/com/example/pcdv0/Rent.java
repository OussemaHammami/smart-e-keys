package com.example.pcdv0;

public class Rent {
    private String carID;
    private String userID;
    private String numBankAccount;
    private String pwd;
    private String dateOfRent;
    private String duration;


    public Rent(){}

    public Rent(String carID, String userID, String numBankAccount, String pwd, String dateOfRent, String duration){
        this.dateOfRent=dateOfRent;
        this.carID=carID;
        this.duration=duration;
        this.numBankAccount=numBankAccount;
        this.pwd=pwd;
        this.userID=userID;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNumBankAccount() {
        return numBankAccount;
    }

    public void setNumBankAccount(String numBankAccount) {
        this.numBankAccount = numBankAccount;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(String dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }
}
