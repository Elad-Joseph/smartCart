package com.example.smartcart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcart.modle.User;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<User> getUser() {
        if(user == null){
            user = new MutableLiveData<>();
            loadUser();
        }
        return user;
    }

    public void setUser(User u) {
        user.setValue(u);
    }

    public void loadUser() {
        User storedUser = new User("josie" , "josies" , "144" , 0);
        user.setValue(storedUser);
    }
}

