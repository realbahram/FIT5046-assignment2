package com.example.goal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    public SharedViewModel(){
        mText = new MutableLiveData<String>();
    }
    public void setMessage(String message) {
        mText.setValue(message);
    }
    public LiveData<String> getText() {
        return mText;
    }
}