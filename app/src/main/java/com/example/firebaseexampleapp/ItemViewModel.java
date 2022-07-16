package com.example.firebaseexampleapp;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<User> selectedItem = new MutableLiveData<User>();
    public void selectItem(User u) {
        selectedItem.setValue(u);
    }


    public LiveData<User> getSelectedItem() {
        return selectedItem;
    }
}
