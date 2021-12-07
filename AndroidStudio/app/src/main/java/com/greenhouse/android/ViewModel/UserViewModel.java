package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.UserRepository;
import com.greenhouse.android.Wrappers.APIResponse.JWT;
import com.greenhouse.android.Wrappers.User;


public class UserViewModel extends ViewModel {
    private UserRepository repository;

    public UserViewModel(){
        repository = UserRepository.getInstance();
    }

    public LiveData<JWT> getToken(){
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
