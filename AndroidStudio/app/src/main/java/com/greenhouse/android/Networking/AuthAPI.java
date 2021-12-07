package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.APIResponse.JWT;
import com.greenhouse.android.Wrappers.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("auth/login")
    Call<JWT> login(@Body User user);
    @GET("auth/refresh")
    Call<JWT> refresh();
    @GET("auth/logout")
    Call logout();
}
