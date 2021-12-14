package com.greenhouse.android.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.greenhouse.android.Networking.DeviceAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private List<GreenData> currentData;

    private List<String> userDevices;
     private List<GreenData> greenList;

    public DeviceRepository() {
        deviceAPI = ServiceGenerator.getGreenhouseAPI();
        allDevices = new MutableLiveData<>();
        intervalData = new MutableLiveData<>();
        userDevices = StringToList(LocalStorage.getInstance().get("devices"));
        Log.e("user devices",userDevices+"");
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
            Log.e("user devices",userDevices+"");
            int finalI = i;
            call.enqueue(new Callback<Device>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.code() == 200 && response.body()!=null) {
                        current[0] = response.body();
                        Log.e("get all devices","response: "+response.body());


                        Call<GreenData> call2 = deviceAPI.getLastData(userDevices.get(finalI));
                        call2.enqueue(new Callback<GreenData>() {
                            @EverythingIsNonNull
                            @Override
                            public void onResponse(Call<GreenData> call, Response<GreenData> response) {
                                if (response.code() == 200) {
                                    current[0].setLatest(response.body());
                                    if (current[0] != null) {
                                        currentAll.add(current[0]);
                                        allDevices.setValue(currentAll);
                                    }
                                    Log.e("green response green-data","call: "+response.body());
                                    Log.e("deviceAPI response","call: "+current[0]);
                                    Log.e("deviceAPI all devices: ", allDevices.getValue().size()+"");
                                } else {
                                    current[0].setLatest(new GreenData());
                                    if (current[0] != null) {
                                        currentAll.add(current[0]);
                                        allDevices.setValue(currentAll);
                                    }
                                    Log.e("green response","call: "+response.code()+" "+response.message());
                                    Log.e("green response","call: "+response.raw().toString());
                                }

                            }
                            @EverythingIsNonNull
                            @Override
                            public void onFailure(Call<GreenData> call, Throwable t) {
                                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
                                Log.e("no data for this device","call: "+call.toString());
                                current[0].setLatest(new GreenData());
                                if (current[0] != null) {
                                    currentAll.add(current[0]);
                                    allDevices.setValue(currentAll);
                                }
                            }
                        });
                    } else {
                        Log.e("get device call not 200","call: "+response.code()+" "+response.message());
                        Log.e("get device call raw","call: "+response.raw().toString());
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    Log.i("get device failure", "The data could not reach you!" +t.getMessage());
                    allDevices.setValue(currentAll);
                }
            });
        }
        allDevices.setValue(currentAll);
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
                    Log.e("update device not 200","call: "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("Update dev failure", "The data could not reach you!" +t.getMessage());
            }
        });
    }

    public void create(Device device) {
        Call<Device> call = deviceAPI.create(device);

        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    Log.e("create deviceAPI success","call: "+response.body());
                    addUserDeviceLocal(device.eui);
                    getAll();
                } else {
                    Log.e("create deviceAPI not 200","call: "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("create Retrofit other", "The data could not reach you!" +t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public void delete(String deviceEUI) {
        deleteDeviceLocal(deviceEUI);
        Call<Device> call = deviceAPI.delete(deviceEUI);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    Log.e("delete device success","call: "+response.body());
                    getAll();
                } else {
                    Log.e("delete device not 200","call: "+response.code()+" "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("Retrofit other", "The device cannot be deleted could not reach you!" +t.getMessage());
                getAll();
                t.printStackTrace();
            }
        });
    }


    public String ListToString(List<String> devices) {
        StringBuilder returned = new StringBuilder();
        for(int i=0;i<devices.size();i++){
            if(!returned.toString().contains(devices.get(i))&&!devices.get(i).equals("default"))
            returned.append(returned).append(".").append(devices.get(i));
        }
        return returned.toString();
    }
    public List<String> StringToList(String devices) {
        List<String> all =new ArrayList<>(Arrays.asList(devices.split("\\.")));
        List<String> nonDefault = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (!all.get(i).equals("default")&&!all.get(i).equals("")&&!nonDefault.contains(all.get(i))) {
                nonDefault.add(all.get(i));
            }
        }
        userDevices = nonDefault;
        return nonDefault;
    }

    private void addUserDeviceLocal(String deviceEUI) {
        if (!userDevices.contains(deviceEUI)) {
            userDevices.add(deviceEUI);
            saveUserDevices();
        }
    }
    private void deleteDeviceLocal(String deviceEUI) {
        userDevices.remove(deviceEUI);
        saveUserDevices();

    }
    private void saveUserDevices() {
        LocalStorage.getInstance().set("devices",ListToString(userDevices));
    }

    public MutableLiveData<List<GreenData>> getDeviceInterval(String id, Date start, Date end)
    {
        currentData = new ArrayList<>();

        String pattern = "yyyy-MM-dd HH:mm:ss";

        DateFormat df = new SimpleDateFormat(pattern);

        String dstart = df.format(start);
        String dend = df.format(end);
        Log.i("start date",dstart);
        Log.i("end date",dend);

        Call<List<GreenData>> call = deviceAPI.getIntervalData(id, dstart, dend);
        call.enqueue(new Callback<List<GreenData>>() {
            @Override
            public void onResponse(Call<List<GreenData>> call, Response<List<GreenData>> response) {
                if(response.code() == 200)
                {
                    currentData = response.body();
                    intervalData.setValue(currentData);
                    Log.i("Retrofit", "response 200 " + response.body().size());
                }else{
                    Log.i("Retrofit", "response not 200 " + response.code()+" "+response.message()+"\n "+response.raw().request().url());
                }

            }

            @Override
            public void onFailure(Call<List<GreenData>> call, Throwable t) {
                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
            }
        });
        return intervalData;
    }
}
