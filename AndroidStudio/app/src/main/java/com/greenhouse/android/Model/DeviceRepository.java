package com.greenhouse.android.Model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.greenhouse.android.Networking.DeviceAPI;
import com.greenhouse.android.Networking.ServiceGenerator;
import com.greenhouse.android.Util.AndroidClient;
import com.greenhouse.android.Util.DateUtil;
import com.greenhouse.android.Util.LocalStorage;
import com.greenhouse.android.Util.RoomDatabase.DeviceCache;
import com.greenhouse.android.Util.RoomDatabase.DeviceDb;
import com.greenhouse.android.ViewModel.available_times;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    private AndroidClient androidClient;
    private final DeviceCache deviceCache;
    private final ExecutorService executorService;

    private MutableLiveData<List<Device>> allDevices;

    private MutableLiveData<List<GreenData>> intervalData;

    private MutableLiveData<Device> deviceToView;
    private List<GreenData> currentData;

    MutableLiveData<List<GreenData>> chartData;
    
    MutableLiveData<String> controlResponse;

    private List<Device> userDevices;

    private boolean logged;
    
    private UserRepository userRepository;

    private MutableLiveData<String> apiResponse;

    public DeviceRepository(Application application) {
        deviceAPI = ServiceGenerator.getGreenhouseAPI();
        allDevices = new MutableLiveData<>();
        apiResponse = new MutableLiveData<>();
        userDevices = new ArrayList<>();

        intervalData = new MutableLiveData<>();
        
        controlResponse = new MutableLiveData<>();
        
        userRepository = UserRepository.getInstance(application);


        deviceToView = new MutableLiveData<>();
        chartData = new MutableLiveData<>();

        androidClient = new AndroidClient();
        apiResponse.observeForever(s -> Log.e("apiResponse" , s));

        //Initializing the local database
        DeviceDb database = DeviceDb.getInstance(application);
        deviceCache = database.getDeviceDao();
        executorService = Executors.newFixedThreadPool(2);

        userRepository.getUserDevices().observeForever(devices -> {
            userDevices = devices;
            getAll();
        });

    }

    public static synchronized DeviceRepository getInstance(Application application){
        if(instance==null){
            instance = new DeviceRepository(application);
        }
        return instance;
    }


    public MutableLiveData<Device> getDeviceToView(String eui) {
        getDevice(eui);
        return deviceToView;
    }

    private void getDevice(String eui) {
        Call<Device> call = deviceAPI.get(eui);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.isSuccessful()) {
                    deviceToView.setValue(response.body());
                } else {
                    deviceToView.setValue(allDevices.getValue().get(0));
                    apiResponse.setValue("Unable to get device!");
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                deviceToView.setValue(allDevices.getValue().get(0));
                apiResponse.setValue("Error, please check your connection!");
            }
        });
    }

    public LiveData<List<Device>> getAll(){
        fetchDevices();
        return allDevices;
    }

    public void fetchDevices(){
        List<Device> currentAll = new ArrayList<>();

        if (userDevices.size() == 0) {
            deviceCache.getAllLocal().observeForever(devices -> {
                if (!androidClient.isNetworkAvailable()) {
                    allDevices.setValue(devices);
                }
            });
            return;
        }
        for(int i=0;i<userDevices.size();i++){

            final Device[] current = new Device[1];
            Call<Device> call = deviceAPI.get(userDevices.get(i).getEui());
            Log.e("user devices",userDevices+"");
            int finalI = i;
            call.enqueue(new Callback<Device>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<Device> call, Response<Device> response) {
                    if (response.isSuccessful() && response.body()!=null) {
                        current[0] = response.body();
                        Log.e("get all devices","response: "+response.body());

                        Call<GreenData> call2 = deviceAPI.getLastData(userDevices.get(finalI).getEui());
                        call2.enqueue(new Callback<GreenData>() {
                            @EverythingIsNonNull
                            @Override
                            public void onResponse(Call<GreenData> call, Response<GreenData> response) {
                                if (response.isSuccessful()) {
                                    current[0].setLatest(response.body());
                                    currentAll.add(current[0]);
                                    allDevices.setValue(currentAll);
                                    cacheDevices(currentAll);
                                    apiResponse.setValue("Device found!");
                                    cacheDevices(currentAll);
                                    Log.e("green response green-data","call: "+response.body());
                                    Log.e("localStorage: ", "update: " + currentAll);
                                    Log.e("deviceAPI response","call: "+current[0]);
                                    Log.e("deviceAPI all devices: ", allDevices.getValue().size()+"");
                                } else {
                                    current[0].setLatest(new GreenData());
                                    currentAll.add(current[0]);
                                    allDevices.setValue(currentAll);
                                    cacheDevices(currentAll);
                                    apiResponse.setValue("Unable to get data for "+ userDevices.get(finalI).getEui());
                                    Log.e("green response","call: "+response.code()+" "+response.message());
                                    Log.e("green response","call: "+response.raw().toString());
                                }

                            }
                            @EverythingIsNonNull
                            @Override
                            public void onFailure(Call<GreenData> call, Throwable t) {
                                Log.i("Retrofit", "The data could not reach you!" +t.getMessage());
                                Log.e("no data for this device","call: "+call.toString());
                                apiResponse.setValue("Error, please check your connection!");
                                current[0].setLatest(new GreenData());
                                currentAll.add(current[0]);
                                allDevices.setValue(currentAll);
                                cacheDevices(currentAll);
                            }
                        });
                    } else {
                        Log.e("get device call not 200","call: "+response.code()+" "+response.message());
                        Log.e("get device call raw","call: "+response.raw().toString());
                        apiResponse.setValue("Unable to get required device!");
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<Device> call, Throwable t) {
                    deviceCache.getAllLocal().observeForever(devices -> allDevices.setValue(devices));
                    apiResponse.setValue("Error, please check your connection!");
                }
            });
        }
    }


    public void update(Device device) {
        Call<Device> call = deviceAPI.update(device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                } else {
                    apiResponse.setValue("Unable do update!");
                    Log.e("update device not 200","call: "+response.code()+" "+response.message());
                }
            }
            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.i("Update dev failure", "The data could not reach you!" +t.getMessage());
                apiResponse.setValue("Error, please check your connection!");
            }
        });
    }

    public void create(Device device) {
        Call<Device> call = deviceAPI.create(device);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.isSuccessful()) {
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
                apiResponse.setValue("Error, please check your connection!");

            }
        });

    }

    public void delete(String deviceEUI) {
        Call<Device> call = deviceAPI.delete(deviceEUI);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.isSuccessful()) {
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
                apiResponse.setValue("Error, please check your connection!");

            }
        });
    }



    //LOCAL DATA
    public void cacheDevices(List<Device> devices){
        clearCache();
        for(int i = 0; i < devices.size(); i++){
            int finalI = i;
            executorService.execute(() -> deviceCache.insert(devices.get(finalI)));
      }
    }

    public void clearCache(){
        executorService.execute(deviceCache::deleteAll);
    }


    // For the chart


    public MutableLiveData<List<GreenData>> getDeviceInterval(String id, Date end,int noOfPoints,available_times interval)
    {
        Date start = DateUtil.calculateStart(end,interval,noOfPoints);
        currentData = new ArrayList<>();

        String pattern = "yyyy-MM-dd HH:mm:ss";

        DateFormat df = new SimpleDateFormat(pattern);

        String dstart = df.format(start);
        String dend = df.format(end);

        Log.i("start date",dstart);
        Log.i("end date",dend);

        Call<List<GreenData>> call = deviceAPI.getIntervalData(id, dstart, dend);
        intervalData.setValue(new ArrayList<GreenData>());
        call.enqueue(new Callback<List<GreenData>>() {
            @Override
            public void onResponse(Call<List<GreenData>> call, Response<List<GreenData>> response) {
                if(response.isSuccessful())
                {
                    currentData = response.body();
                    intervalData.setValue(currentData);
                    TransformData(currentData,end,interval,noOfPoints);
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

    public LiveData<List<GreenData>> getChartData(String id, Date end , available_times interval, int noOfPoints)
    {
        getDeviceInterval(id,end,noOfPoints,interval);
        return chartData;
    }

    private void TransformData(List<GreenData> all,Date end ,available_times interval,int noOfPoints) {

        List<GreenData> returned = new ArrayList<>();

        Date start = DateUtil.calculateStart(end,interval,noOfPoints);
        Date localEnd = DateUtil.calculateEnd(start,interval,1);

        for (int i = 0; i < noOfPoints; i++) {

            List<GreenData> lastInterval = extractInterval(all,start,localEnd);
            GreenData averaged = averageArray(lastInterval);
            returned.add(i,averaged);

            start = localEnd;
            localEnd = DateUtil.calculateEnd(start,interval,1);
        }

        chartData.setValue(returned);
    }

    public GreenData averageArray(List<GreenData> array) {
        if(array.size()==0) return new GreenData(0,0,0,0);
        long tempTotal = 0;
        long humTotal =0;
        long lightTotal = 0;
        long co2Total = 0;
        for (int i = 0; i < array.size(); i++) {
            tempTotal+=array.get(i).getTemperature();
            humTotal+=array.get(i).getHumidity();
            lightTotal+=array.get(i).getLight();
            co2Total+=array.get(i).getCo2();
        }
        return new GreenData((int)humTotal/array.size(),(int)tempTotal/array.size(),(int)lightTotal/array.size(),(int)co2Total/array.size());
    }

    public List<GreenData> extractInterval(List<GreenData> allData, Date start, Date end) {
        List<GreenData> returned = new ArrayList<>();
        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).getDate().after(start) && allData.get(i).getDate().before(end)) {
                returned.add(allData.get(i));
            }
            // a bit of optimisation to not go through all the array when started encountering wrong dates,
            // as the data is coming in a historical order.
            if (allData.get(i).getDate().after(end)) {
                return returned;
            }
        }
        return returned;
    }


    //For the control
    public void controlWindow(String eui,int value) {
        
        Call<String> call = deviceAPI.windowPosition(eui,value);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                controlResponse.setValue(response.body());
                Log.i("Retrofit", "response : " + response.code()+" Message: "+response.message()+"\n Url: "+response.raw().request().url());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                controlResponse.setValue("Failed to send command!");
                Log.i("Retrofit", "response : " + call.request().url());
                t.printStackTrace();
            }
        });
    }
    public void controlWater(String eui,int value) {
        
        Call<String> call = deviceAPI.waterControl(eui,value);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                controlResponse.setValue(response.body());
                Log.i("Retrofit", "response : " + response.code()+" Message: "+response.message()+"\n Url: "+response.raw().request().url());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                controlResponse.setValue("Failed to send command!");
                Log.i("Retrofit", "response : " + call.request().url());
                t.printStackTrace();
            }
        });
    }

    public void controlLight(String eui,int value) {

        Call<String> call = deviceAPI.lightControl(eui,value);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                controlResponse.setValue(response.body());
                Log.i("Retrofit", "response : " + response.code()+" Message: "+response.message()+"\n Url: "+response.raw().request().url());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                controlResponse.setValue("Failed to send command!");
                Log.i("Retrofit", "response : " + call.request().url());
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<String> getControlResponse() {
        return controlResponse;
    }
}
