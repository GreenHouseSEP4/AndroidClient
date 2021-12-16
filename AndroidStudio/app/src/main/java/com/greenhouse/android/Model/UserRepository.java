package com.greenhouse.android.Model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.greenhouse.android.Networking.AuthAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Networking.UserAPI;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.Wrappers.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private UserAPI userAPI;
    private AuthAPI authAPI;

    private MutableLiveData<LoggedUser> loggedUser;
    private MutableLiveData<List<Device>> userDevices;

    private String email;
    private String pass;

    private MutableLiveData<String> apiResponse;

    private UserRepository(Application application){
        userAPI = ServiceGenerator.getUserAPI();
        authAPI = ServiceGenerator.getAuthAPI();
        loggedUser = new MutableLiveData<>();
        apiResponse = new MutableLiveData<>();
        userDevices = new MutableLiveData<>();

        loggedUser.setValue(new LoggedUser(LocalStorage.getInstance().get("access_token")));

        apiResponse.observeForever(s -> Log.e("API response user:" ,s));
        getLoggedUser();
    }

    public MutableLiveData<LoggedUser> getLoggedUser() {
        email = LocalStorage.getInstance().get("email");
        pass = LocalStorage.getInstance().get("pass");
        login(new User(email,pass));
        return loggedUser;
    }

    public void login(User user){

        apiResponse.setValue("Started login!");
        LocalStorage.getInstance().set("email",user.getEmail());
        LocalStorage.getInstance().set("pass",user.getPassword());
        System.out.println(user);

        Call<LoggedUser> call = authAPI.login(user);
        call.enqueue(new Callback<LoggedUser>() {
            @Override
            public void onResponse(Call<LoggedUser> call, Response<LoggedUser> response) {
                if(response.isSuccessful()){
                    LocalStorage.getInstance().set("access_token", response.body().getToken());
                    loggedUser.setValue(response.body());
                    userDevices.setValue(response.body().getUser().getDevices());
                    apiResponse.setValue("Successfully logged in!");
                    Log.i("api response", response.body().toString());
                }else{
                    loggedUser.setValue(new LoggedUser("empty"));
                    apiResponse.setValue("Error during login! " + response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<LoggedUser> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
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
                if(response.isSuccessful()){
                    User resp = response.body();
                    resp.setPassword(newPass);
                    login(resp);
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

    public static UserRepository getInstance(Application application){
        if(instance==null){
            instance = new UserRepository(application);
        }
        return instance;
    }

    public LiveData<LoggedUser> getToken(){
        if (loggedUser == null)
            loggedUser.setValue(new LoggedUser(LocalStorage.getInstance().get("access_token")));
        return loggedUser;
    }


    public void logout(){
        loggedUser.setValue(new LoggedUser("empty"));
        LocalStorage.getInstance().set("access_token", "empty");
        LocalStorage.getInstance().set("email","clear");
        LocalStorage.getInstance().set("pass","clear");
    }
    public void register(User user){
        Call<User> call = authAPI.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    apiResponse.setValue("Successfully registered!");
                    login(user);
                } else {
                    loggedUser.setValue(new LoggedUser("empty"));
                    apiResponse.setValue("An error happened during registering ! "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("api call", "Error");
                apiResponse.setValue("Please check your connection!");
            }
        });
    }
    public void deleteDevice(String eui) {
        Call<User> call = userAPI.deleteDevice(eui);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    apiResponse.setValue("Successfully deleted!");
                } else {
                    apiResponse.setValue("An error happened during deleting ! "+response.code()+" "+response.message());
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
                if (response.isSuccessful()) {
                    apiResponse.setValue("Successfully added!");
                } else {
                    apiResponse.setValue("An error happened during adding ! "+response.code()+" "+response.message());
                }
                getAllDevices();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                apiResponse.setValue("Please check your connection!");
            }
        });
    }

    public MutableLiveData<List<Device>> getUserDevices() {
        return userDevices;
    }


    public void getAllDevices() {
        Call<List<Device>> call = userAPI.getDevices();
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful()) {
                    userDevices.setValue(response.body());
                    apiResponse.setValue("Successfully fetched user devices!");
                } else {
                    apiResponse.setValue("Call not 200" +response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {

                apiResponse.setValue("Please check your connection!" +t.getMessage());

            }
        });
    }
}
