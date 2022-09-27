package com.example.pcdv0.ui.cars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CarsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cars fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}