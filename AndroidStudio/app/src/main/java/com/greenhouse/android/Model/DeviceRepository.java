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
        // TODO uncomment when done implementing device add
        userDevices = StringToList("0004A30B00259D2C");
//        userDevices = StringToList(LocalStorage.getInstance().get("devices"));
        getAll();
    }

    public static DeviceRepository getInstance(){
        if(instance==null){
            instance = new DeviceRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Device>> getAll(){
        List<Device> currentAll = new ArrayList<>();
        for(int i=0;i<userDevices.size();i++){

            final Device[] current = new Device[1];

            Call<Device> call = deviceAPI.get(userDevices.get(i));
            int finalI = i;
            call.enqueue(new Callback<Device>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.code() == 200) {
                        current[0] = response.body();
                        Log.e("deviceAPI","response: "+response.body());


                        Call<GreenData> call2 = deviceAPI.getLastData(userDevices.get(finalI));
                        call2.enqueue(new Callback<GreenData>() {
                            @EverythingIsNonNull
                            @Override
                            public void onResponse(Call<GreenData> call, Response<GreenData> response) {
                                if (response.code() == 200) {
                                    current[0].setLatest(response.body());
                                    Log.e("green response green-data","call: "+response.body());
                                    if (current[0] != null) {
                                        currentAll.add(current[0]);
                                    }
                                    allDevices.setValue(currentAll);
                                    Log.e("deviceAPI response","call: "+current[0]);
                                    Log.e("deviceAPI all devices: ", allDevices.getValue().size()+"");
                                } else {
                                    Log.e("green response","call: "+response.code()+" "+response.message());
                                    Log.e("green response","call: "+response.raw().toString());
                                }
                            }
                            @EverythingIsNonNull
                            @Override
                            public void onFailure(Call<GreenData> call, Throwable t) {
                                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
                                Log.e("deviceAPI","call: "+call.toString());

                            }
                        });
                    } else {
                        Log.e("deviceAPI","call: "+response.code()+" "+response.message());
                        Log.e("deviceAPI","call: "+response.raw().toString());
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
                }
            });
        }
//        System.out.println(allDevices.getValue().get(0));
        return allDevices;
    }

    public void update(Device device) {
        Call<Device> call = deviceAPI.update(device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    System.out.println(response.body());
                } else {
                    Log.e("deviceAPI","call: "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
            }
        });
    }

    public void create(Device device) {
        Call<Device> call = deviceAPI.create(device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    Log.e("deviceAPI success","call: "+response.body());
                } else {
                    Log.e("deviceAPI not 200","call: "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("Retrofit other", "The data could not reach you!" +t.getMessage());
                t.printStackTrace();
            }
        });
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
