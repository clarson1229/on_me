package com.connorlarson.onme.ui.credits;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreditsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> accountBalance;

    public CreditsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is credits fragment");

        accountBalance = new MutableLiveData<>();
        accountBalance.setValue("0");
    }

    public LiveData<String> getText() {
        return mText;
    }
    //todo turn this into a database call
    public LiveData<String> getBalance() {
        return accountBalance;
    }

}