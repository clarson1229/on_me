package com.connorlarson.onme.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModal extends ViewModel {
//Todo make database calls to get this information
    private MutableLiveData<String> mText;
    private MutableLiveData<String> profileName;
    private MutableLiveData<String> profileEmail;

    public ProfileViewModal() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");
        profileName = new MutableLiveData<>();
        profileName.setValue("Connor Larson");
        profileEmail = new MutableLiveData<>();
        profileEmail.setValue("CL@live.com");

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getProfileName() {
        return profileName;
    }
    public LiveData<String> getProfileEmail() {
        return profileEmail;
    }
}