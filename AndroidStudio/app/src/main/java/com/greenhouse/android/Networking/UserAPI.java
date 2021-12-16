package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.Wrappers.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
    @POST("users/register")
    Call<User> register(@Body User user);

    @POST("users")
    Call<User> update(@Body User user);

    @DELETE("users/deleteProfile")
    Call<User> deleteProfile();

    @DELETE("users/deleteDevice")
    Call<User> deleteDevice(@Path("eui")String eui);

    @DELETE("users/addDevice")
    Call<User> addDevice(@Path("eui")String eui);

    @GET("users/devices")
    Call<List<Device>> getDevices();

}
