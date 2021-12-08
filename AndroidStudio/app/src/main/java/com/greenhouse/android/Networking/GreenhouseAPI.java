package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GreenhouseAPI {
    @GET("sensor/latest")
    Call<GreenData> getLastData();
    @GET("sensor/periodic/start={start}&end={end}")
    Call<GreenData> getLast(@Path("start") Date start,@Path("end") Date end);

}
