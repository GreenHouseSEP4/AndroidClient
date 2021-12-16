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
import com.greenhouse.android.ViewModel.available_times;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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


    private final DeviceDao deviceDao;
    private final ExecutorService executorService;


    private MutableLiveData<List<Device>> allDevices;
    private LiveData<List<Device>> allDeviceLocal;
    private MutableLiveData<List<GreenData>> intervalData;

    private MutableLiveData<Device> deviceToView;
    private List<GreenData> currentData;

    MutableLiveData<List<GreenData>> chartData;
    
    MutableLiveData<String> controlResponse;

    private List<String> userDevices;
    private List<GreenData> greenList;

    public DeviceRepository(Application application) {
        GreenHouseDatabase localDatabase = GreenHouseDatabase.getInstance(application);
        deviceAPI = ServiceGenerator.getGreenhouseAPI();
        allDevices = new MutableLiveData<>();

        intervalData = new MutableLiveData<>();
        
        controlResponse = new MutableLiveData<>();
        
        userDevices = StringToList(LocalStorage.getInstance().get("devices"));
        Log.e("user devices",userDevices+"");

        deviceToView = new MutableLiveData<>();
        chartData = new MutableLiveData<>();



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


    public MutableLiveData<Device> getDeviceToView(String eui) {
        getDevice(eui);
        return deviceToView;
    }

    private void getDevice(String eui) {
        Call<Device> call = deviceAPI.get(eui);
        call.enqueue(new Callback<Device>() {
            Device returned;
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 200) {
                    deviceToView.setValue(response.body());
                } else {
                    deviceToView.setValue(allDevices.getValue().get(0));
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

                deviceToView.setValue(allDevices.getValue().get(0));
            }
        });
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



    public MutableLiveData<List<GreenData>> getDeviceInterval(String id, Date end,int noOfPoints,available_times interval)
    {
        Date start = calculateStart(end,interval,noOfPoints);
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
                if(response.code() == 200)
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

        Date start = calculateStart(end,interval,noOfPoints);
        Date localEnd = calculateEnd(start,interval,1);

        for (int i = 0; i < noOfPoints; i++) {

            List<GreenData> lastInterval = getDataForInterval(all,start,localEnd);
            GreenData averaged = averageArray(lastInterval);
            returned.add(i,averaged);

            start = localEnd;
            localEnd = calculateEnd(start,interval,1);
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

    public List<GreenData> getDataForInterval(List<GreenData> allData,Date start,Date end) {
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
    private Date calculateStart(Date end, available_times interval,int noOfPoints) {
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        switch (interval) {
            case minutes15:
                c.add(Calendar.MINUTE,-noOfPoints*15);
                break;
            case hours4:
                c.add(Calendar.HOUR,-noOfPoints*4);
                break;
            case days1:
                c.add(Calendar.DAY_OF_MONTH,-noOfPoints);
                break;
            case days7:
                c.add(Calendar.DAY_OF_MONTH,-noOfPoints*7);
                break;
            case months1:
                c.add(Calendar.MONTH,-noOfPoints);
                break;
            case months6:
                c.add(Calendar.MONTH,-noOfPoints*6);
                break;
            case years1:
                c.add(Calendar.YEAR,-noOfPoints);
                break;
        }

        return c.getTime();
    }
    private Date calculateEnd(Date start, available_times interval,int noOfPoints) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        switch (interval) {
            case minutes15:
                c.add(Calendar.MINUTE,+noOfPoints*15);
                break;
            case hours4:
                c.add(Calendar.HOUR,+noOfPoints*4);
                break;
            case days1:
                c.add(Calendar.DAY_OF_MONTH,+noOfPoints);
                break;
            case days7:
                c.add(Calendar.DAY_OF_MONTH,+noOfPoints*7);
                break;
            case months1:
                c.add(Calendar.MONTH,+noOfPoints);
                break;
            case months6:
                c.add(Calendar.MONTH,+noOfPoints*6);
                break;
            case years1:
                c.add(Calendar.YEAR,+noOfPoints);
                break;
        }

        return c.getTime();
    }
    
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
