package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Model.UserRepository;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.Wrappers.User;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    UserRepository repository;
    public ProfileViewModel(Application application) {
        super(application);
        repository = UserRepository.getInstance(application);
    }

    public void updatePassword(String newPass){
        repository.updatePass(newPass);
    }

    public LiveData<LoggedUser> getUser() {
        return repository.getLoggedUser();
    }
    public void logout() {
        repository.logout();
    }



}
