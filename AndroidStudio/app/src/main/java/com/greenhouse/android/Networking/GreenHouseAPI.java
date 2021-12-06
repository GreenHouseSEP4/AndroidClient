package com.greenhouse.android.Networking;


import com.greenhouse.android.Wrappers.APIResponse.TemperatureResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GreenHouseAPI {
    @GET("sensor/{id}")
    Call<TemperatureResponse> getData(@Path("id") int id);
}
