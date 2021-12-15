package com.greenhouse.android.Model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.greenhouse.android.Networking.DeviceAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Util.RoomDatabase.DeviceDao;
import com.greenhouse.android.Util.RoomDatabase.GreenHouseDatabase;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DeviceRepository {
    private static DeviceRepository instance;
    private DeviceAPI deviceAPI;


    private final DeviceDao deviceDao;
    private final ExecutorService executorService;


    private MutableLiveData<List<Device>> allDevices;
    private LiveData<List<Device>> allDeviceLocal;
    private MutableLiveData<List<GreenData>> intervalData;

    private List<String> userDevices;

    public DeviceRepository(Application application) {
        GreenHouseDatabase localDatabase = GreenHouseDatabase.getInstance(application);
        deviceAPI = ServiceGenerator.getGreenhouseAPI();
        allDevices = new MutableLiveData<>();

        intervalData = new MutableLiveData<>();
        userDevices = StringToList(LocalStorage.getInstance().get("devices"));
        Log.e("user devices",userDevices+"");
        


        //Initializing the local database
        GreenHouseDatabase database = GreenHouseDatabase.getInstance(application);
        deviceDao = database.getDeviceDao();
        executorService = Executors.newFixedThreadPool(2);

        getAll();
    }

    public static synchronized DeviceRepository getInstance(Application application){
        if(instance==null){
            instance = new DeviceRepository(application);
        }
        return instance;
    }

    public LiveData<List<Device>> getAll(){
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
                                    allDevices.setValue(currentAll);
                                    //Local Data
                                    deleteAllDevices();
                                    insertAllInLocal(currentAll);
                                    Log.e("localStorage: ", "update: " + currentAll);

                                    Log.e("deviceAPI response","call: "+current[0]);
                                    Log.e("deviceAPI all devices: ", allDevices.getValue().size()+"");
                                } else {
                                    current[0].setLatest(new GreenData());
                                    if (current[0] != null) {
                                        currentAll.add(current[0]);
                                        allDevices.setValue(currentAll);
                                        deleteAllDevices();
                                        insertAllInLocal(currentAll);
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
                                    deleteAllDevices();
                                    insertAllInLocal(currentAll);
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
//                    allDevices.setValue(currentAll);
//                    deleteAllDevices();
//                    insertAllInLocal(currentAll);
                    Log.i("Retrofit", "The data could not reach you!" +t.getMessage());


                    //Getting the previous data from the local database
                    //LiveData<List<Device>> tempLocalList = deviceDao.getAllLocal();
                    //Assigning to the devices
                    //allDevices.setValue();


                }
            });
        }
        allDevices.setValue(currentAll);
        deleteAllDevices();
        insertAllInLocal(currentAll);
        
        return deviceDao.getAllLocal();
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
        addUserDeviceLocal(device.eui);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    Log.e("create deviceAPI success","call: "+response.body());
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


    //LOCAL DATA
    public void insertAllInLocal(List<Device> devices){
        for(int i = 0; i < devices.size(); i++){
            int finalI = i;
            executorService.execute(() -> deviceDao.insert(devices.get(finalI)));
      }
        //executorService.execute(() -> deviceDao.insert(device));
    }

    public void deleteAllDevices(){
        executorService.execute(deviceDao::deleteAll);
    }


}
