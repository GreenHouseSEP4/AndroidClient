package com.greenhouse.android.Networking;

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
                    .addHeader("Authorization", "Bearer " + LocalStorage.getInstance().get("access_token"))
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();


    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("http://gatewayserver-env.eba-jv7rk7pv.eu-central-1.elasticbeanstalk.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();
    private static Retrofit retrofitAuth = retrofitBuilder.client(client).build();


    private static GreenHouseAPI greenHouseAPI = retrofit.create(GreenHouseAPI.class);
    private static UserAPI userAPI = retrofit.create(UserAPI.class);

    public static GreenHouseAPI getGreenHouseAPI()
    {
        return greenHouseAPI;
    }
    public static UserAPI getUserAPI()
    {
        return userAPI;
    }


}