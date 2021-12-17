package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeviceAPI {
    @GET("measurements/{deviceId}/latest")
    Call<GreenData> getLastData(@Path("deviceId")String deviceId);

    @GET("measurements/{deviceId}/periodic")
    Call<List<GreenData>> getIntervalData(@Path("deviceId")String eui,
                                          @Query("start") String start, @Query("end") String end);

    @GET("devices/{deviceId}")
    Call<Device> get(@Path("deviceId")String deviceId);

    @DELETE("devices/{deviceId}")
    Call<Device> delete(@Path("deviceId")String deviceId);

    @POST("devices")
    Call<Device> create(@Body Device created);

    @PUT("devices")
    Call<Device> update(@Body Device created);

    @POST("remote/{deviceId}/window")
    Call<String> windowPosition(@Path("deviceId")String eui,@Query("commandPercentage")int value);


    @POST("remote/{deviceId}/water")
    Call<String> waterControl(@Path("deviceId")String eui,@Query("waterValue")int value);


    @POST("remote/{deviceId}/light")
    Call<String> lightControl(@Path("deviceId")String eui,@Query("lightValue")int value);


}
