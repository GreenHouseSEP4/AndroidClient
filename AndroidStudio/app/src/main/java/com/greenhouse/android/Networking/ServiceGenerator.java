package com.greenhouse.android.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greenhouse.android.Util.LocalStorage;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("api-key", "zaCELgL.0imfnc8mVLWwsAawjYr4Rx-Af50DDqtlx")
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    private static OkHttpClient clientJWT = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + LocalStorage.getInstance().get("access_token"))
                    .build();
            System.out.println(LocalStorage.getInstance().get("access_token"));
            return chain.proceed(newRequest);
        }
    }).build();

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("http://gatewayserver-env.eba-jv7rk7pv.eu-central-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit.Builder authBuilder = new Retrofit.Builder()
            .baseUrl("https://greenauth.ddlele.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.client(client).build();
    private static Retrofit retrofitAuth = authBuilder.build();
    private static Retrofit retrofitAndroidData = authBuilder.client(clientJWT).build();



    private static DeviceAPI deviceAPI = retrofit.create(DeviceAPI.class);
    private static UserAPI userAPI = retrofitAndroidData.create(UserAPI.class);
    private static AuthAPI authAPI = retrofitAuth.create(AuthAPI.class);

    public static DeviceAPI getGreenhouseAPI()
    {
        return deviceAPI;
    }
    public static UserAPI getUserAPI()
    {
        return userAPI;
    }

    public static AuthAPI getAuthAPI()
    {
        return authAPI;
    }


}