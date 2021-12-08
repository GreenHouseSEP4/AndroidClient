package com.greenhouse.android.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.greenhouse.android.Networking.GreenhouseAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class GreenhouseRepository {
    private static GreenhouseRepository instance;
    private GreenhouseAPI greenhouseAPI;

    private MutableLiveData<GreenData> lastData;
    private MutableLiveData<List<GreenData>> intervalData;

    public GreenhouseRepository() {
        greenhouseAPI = ServiceGenerator.getGreenhouseAPI();
        lastData = new MutableLiveData<>();
        intervalData = new MutableLiveData<>();
        getLatest();
    }

    public static GreenhouseRepository getInstance(){
        if(instance==null){
            instance = new GreenhouseRepository();
        }
        return instance;
    }

    public MutableLiveData<GreenData> getLatest(){
        Call<GreenData> call = greenhouseAPI.getLastData();
        call.enqueue(new Callback<GreenData>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<GreenData> call, Response<GreenData> response) {
                if (response.code() == 200) {
                    lastData.setValue(response.body());
                } else {
                    Log.e("expenseAPI","call: "+response.code()+" "+response.message());
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<GreenData> call, Throwable t) {
                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
            }
        });
        return lastData;
    }
}
