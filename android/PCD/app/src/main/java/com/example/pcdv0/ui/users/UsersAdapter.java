package com.example.pcdv0.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pcdv0.R;
import com.example.pcdv0.User;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {


    public UsersAdapter(Context ct, ArrayList<User> userArrayList){
        super(ct, R.layout.user_row, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_row, parent,false);
        }

        TextView nameTxt = convertView.findViewById(R.id.nameTxt);
        TextView numberTxt = convertView.findViewById(R.id.numebrTxt);
        TextView traveledTxt = convertView.findViewById(R.id.traveledTxt);
        TextView placeTxt = convertView.findViewById(R.id.placeTxt);
        TextView stateTxt = convertView.findViewById(R.id.stateTxt);
        TextView typeTxt = convertView.findViewById(R.id.typeTxt);


        nameTxt.setText(user.getEmail());
        numberTxt.setText(user.getName());
        traveledTxt.setText(user.getFamilyName());
        placeTxt.setText(user.getTelNumber());
        stateTxt.setText(user.getRent());
        typeTxt.setText(user.getType());

        return convertView;
    }

}

