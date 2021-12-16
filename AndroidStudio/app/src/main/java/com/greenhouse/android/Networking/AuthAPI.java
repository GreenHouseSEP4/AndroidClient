package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("users/register")
    Call<User> register(@Body User user);

    @POST("auth/login")
    Call<LoggedUser> login(@Body User user);

    @GET("auth/refresh")
    Call<LoggedUser> refresh();

    @GET("auth/logout")
    Call logout();
}
