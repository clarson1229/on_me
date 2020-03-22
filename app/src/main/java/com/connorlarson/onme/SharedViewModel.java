package com.connorlarson.onme;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel {

//    private MutableLiveData<String> dataToShare ("Initial value!");
    private MutableLiveData<String> dataToShare;

    public SharedViewModel() {
        dataToShare = new MutableLiveData<>();
    }

    public void updateData(String data){
        dataToShare.setValue(data);
        Log.d("In ShareView modal", dataToShare.getValue() );
    }

    public LiveData<String> getText() {
        return dataToShare;
    }
}