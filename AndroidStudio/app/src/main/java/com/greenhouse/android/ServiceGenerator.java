package com.greenhouse.android;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("http://gatewayserver-env.eba-jv7rk7pv.eu-central-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static GreenHouseAPI greenHouseAPI = retrofit.create(GreenHouseAPI.class);

    public static GreenHouseAPI getGreenHouseAPI()
    {
        return greenHouseAPI;
    }
}