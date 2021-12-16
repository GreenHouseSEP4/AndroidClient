package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.UserRepository;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.User;


public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;

    public UserViewModel(Application application){
        super(application);
        repository = UserRepository.getInstance(application);
    }

    public LiveData<LoggedUser> getToken(){
        return repository.getToken();
    }

    public void login(User user){
        repository.login(user);
    }

    public void logout(){
        repository.logout();
    }

    public void register(User user){
        repository.register(user);
    }
}
