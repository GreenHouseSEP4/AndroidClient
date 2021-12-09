package com.greenhouse.android.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.greenhouse.android.Networking.DeviceAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DeviceRepository {
    private static DeviceRepository instance;
    private DeviceAPI deviceAPI;

    private MutableLiveData<List<Device>> allDevices;
    private MutableLiveData<List<GreenData>> intervalData;

    private List<String> userDevices;

    public DeviceRepository() {
        deviceAPI = ServiceGenerator.getGreenhouseAPI();
        allDevices = new MutableLiveData<>();
        intervalData = new MutableLiveData<>();
        userDevices = StringToList(LocalStorage.getInstance().get("devices"));
        getAll();
    }

    public static DeviceRepository getInstance(){
        if(instance==null){
            instance = new DeviceRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Device>> getAll(){
        for(int i=0;i<userDevices.size();i++){
            Call<Device> call = deviceAPI.get(userDevices.get(i));
            call.enqueue(new Callback<Device>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.code() == 200) {
                        List<Device> currentAll = allDevices.getValue();
                        currentAll.add(response.body());
                        allDevices.setValue(currentAll);
                    } else {
                        Log.e("deviceAPI","call: "+response.code()+" "+response.message());
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
                }
            });
        }
        return allDevices;
    }

    public String ListToString(List devices) {
        StringBuilder returned = new StringBuilder();
        for(int i=0;i<devices.size();i++){
            returned.append(returned).append(".").append(devices.get(i));
        }
        return returned.toString();
    }
    public List<String> StringToList(String devices) {
       return new ArrayList<>(Arrays.asList(devices.split("\\.")));
    }
}
