package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Model.UserRepository;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.Wrappers.User;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    UserRepository repository;
    public ProfileViewModel() {
        repository = UserRepository.getInstance();
    }

    public void updateUser(User user){repository.login(user);}

    public void logout() {
        repository.logout();
    }



}
