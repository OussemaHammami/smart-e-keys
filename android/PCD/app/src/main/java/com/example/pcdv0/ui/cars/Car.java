package com.example.pcdv0.ui.cars;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pcdv0.R;

public class Car implements Parcelable {
    private String name;
    private String serialNumber;
    private String traveled;
    private String place;
    private String state;
    private String price;
    private String carID;
    private int image;
//    private String longitude;
//    private String latitude;
//    private Location location= new Location();

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public Car() {
    }


    public Car(String name, String serialNumber, String traveled, String place, String price) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.traveled = traveled;
        this.place = place;
        this.price = price;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.location = location;
        this.state = "available";
    }

    public Car(String name) {
        this.name = name;
        this.serialNumber = "0000";
        this.traveled = "0";
        this.place = "Sakiet Ezzite";
        this.state = "available";
        this.price = "0 $";
//        this.location.setLatitude(10.063779491845622);
//        this.location.setLongitude(36.81392137117466);
    }


    protected Car(Parcel in) {
        name = in.readString();
        serialNumber = in.readString();
        traveled = in.readString();
        place = in.readString();
        state = in.readString();
        price = in.readString();
        carID = in.readString();
//        location = in.readValue(Location location);


    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getTraveled() {
        return this.traveled;
    }

    public String getPlace() {
        return this.place;
    }

    public String getState() {
        return this.state;
    }

    public String getPrice() {
        return this.price;
    }

//    public String getLongitude(){
//        return this.longitude;
//    }
//
//    public String getLatitude(){
//        return this.latitude;
//    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setTraveled(String traveled) {
        this.traveled = traveled;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    public void setLongitude(String price){
//        this.longitude=longitude;
//    }
//
//    public void setLatitude(String price){
//        this.latitude=latitude;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(serialNumber);
        parcel.writeString(traveled);
        parcel.writeString(place);
        parcel.writeString(state);
        parcel.writeString(price);
//        parcel.writeDouble(location.getLatitude());
//        parcel.writeDouble(location.getLongitude());
        parcel.writeString(carID);
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static int getImageCar(String name) {
            switch (name) {
                case "clio":
                    return R.drawable.renault_clio;
                case "symbol":
                    return  R.drawable.symbol;
                case "golf":
                    return  R.drawable.golf;
                case "polo":
                    return  R.drawable.polo;
                case "megane":
                    return  R.drawable.megane;
                case "mahindra":
                    return  R.drawable.mahindra_kuv;
                case "kia":
                    return  R.drawable.picanto;
                case "hyundai":
                    return  R.drawable.i20;
                case "206":
                    return  R.drawable.p206;
                case "yaris":
                    return  R.drawable.yaris;
            }
        return R.drawable.porche2;

    }

}
