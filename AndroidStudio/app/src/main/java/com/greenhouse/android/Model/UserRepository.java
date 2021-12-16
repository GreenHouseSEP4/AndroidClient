package com.greenhouse.android.Model;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.greenhouse.android.Networking.AuthAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Networking.UserAPI;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.Wrappers.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private UserAPI userAPI;
    private AuthAPI authAPI;

    private MutableLiveData<LoggedUser> loggedUser;

    private String email;
    private String pass;

    private MutableLiveData<String> apiResponse;

    private UserRepository(){
        userAPI = ServiceGenerator.getUserAPI();
        authAPI = ServiceGenerator.getAuthAPI();
        loggedUser = new MutableLiveData<>();
        apiResponse = new MutableLiveData<>();

        loggedUser.setValue(new LoggedUser(LocalStorage.getInstance().get("access_token")));
        email = LocalStorage.getInstance().get("email");
        pass = LocalStorage.getInstance().get("pass");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                login(new User(email,pass));
            }
        });

    }

    public void updatePass(String newPass) {
        User user = loggedUser.getValue().getUser();
        user.setPassword(newPass);
        Call<User> call = userAPI.update(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    login(response.body());
                }else{
                    apiResponse.setValue("Not updated!");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
            }
        });
    }

    public static UserRepository getInstance(){
        if(instance==null){
            instance = new UserRepository();
        }
        return instance;
    }

    public LiveData<LoggedUser> getToken(){
        if (loggedUser == null)
            loggedUser.setValue(new LoggedUser(LocalStorage.getInstance().get("access_token")));
        return loggedUser;
    }

    public void login(User user){
        LocalStorage.getInstance().set("email",user.getEmail());
        LocalStorage.getInstance().set("pass",user.getPassword());

        Call<LoggedUser> call = authAPI.login(user);
        call.enqueue(new Callback<LoggedUser>() {
            @Override
            public void onResponse(Call<LoggedUser> call, Response<LoggedUser> response) {
                if(response.code() == 200){
                    LocalStorage.getInstance().set("access_token", response.body().getToken());
                    loggedUser.setValue(response.body());
                    Log.i("api response", response.body().toString());

                }else{
                    loggedUser.setValue(new LoggedUser("empty"));
                }
            }

            @Override
            public void onFailure(Call<LoggedUser> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
            }
        });

    }

    public void logout(){
        loggedUser.setValue(new LoggedUser("empty"));
        LocalStorage.getInstance().set("access_token", "empty");
        LocalStorage.getInstance().set("email","clear");
        LocalStorage.getInstance().set("pass","clear");
    }

    public void register(User user){
        Call<User> call = userAPI.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    login(user);
                } else {
                    loggedUser.setValue(new LoggedUser("empty"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("api call", "Error");
                loggedUser.setValue(new LoggedUser("empty"));
                apiResponse.setValue("Please check your connection!");
            }
        });
    }
    public void deleteDevice(String eui) {
        Call<User> call = userAPI.deleteDevice(eui);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    apiResponse.setValue("Successfully deleted!");
                } else {
                    apiResponse.setValue("An error happened!");
                }
                getAllDevices();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
            }
        });
    }
    public void addDevice(String eui) {
        Call<User> call = userAPI.addDevice(eui);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    apiResponse.setValue("Successfully added!");
                } else {
                    apiResponse.setValue("An error happened!");
                }
                getAllDevices();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
            }
        });
    }
    public void getAllDevices() {
        Call<List<Device>> call = userAPI.getDevices();
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.code() == 200) {
                    User user = loggedUser.getValue().getUser();
                    user.setDevices(response.body());
                    LoggedUser current = loggedUser.getValue();
                    current.setUser(user);
                    loggedUser.setValue(current);
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<LoggedUser> getLoggedUser() {
        return loggedUser;
    }
}
