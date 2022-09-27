package com.example.pcdv0.ui.cars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pcdv0.R;

import java.util.ArrayList;

public class CarsAdminAdapter extends ArrayAdapter<Car> {


    public CarsAdminAdapter(Context ct, ArrayList<Car> carArrayList){
        super(ct, R.layout.admin_car_row, carArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Car car = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_car_row, parent,false);
        }

        TextView nameTxt = convertView.findViewById(R.id.nameTxt);
        TextView numberTxt = convertView.findViewById(R.id.numebrTxt);
        TextView traveledTxt = convertView.findViewById(R.id.traveledTxt);
        TextView placeTxt = convertView.findViewById(R.id.placeTxt);
        TextView priceTxt = convertView.findViewById(R.id.priceTxt);
        TextView stateTxt = convertView.findViewById(R.id.stateTxt);
        ImageView carImg = convertView.findViewById(R.id.img);


        nameTxt.setText(car.getName());
        numberTxt.setText(car.getSerialNumber());
        traveledTxt.setText(car.getTraveled());
        placeTxt.setText(car.getPlace());
        stateTxt.setText(car.getState());
        priceTxt.setText(car.getPrice());
        carImg.setImageResource(Car.getImageCar(car.getName()));

        return convertView;
    }

}
