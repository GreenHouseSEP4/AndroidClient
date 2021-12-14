package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;
public class SettingsViewModel extends AndroidViewModel {


    private static DeviceRepository deviceRepository;

    public SettingsViewModel(Application application) {
        super(application);
        deviceRepository = DeviceRepository.getInstance(application);
    }

    public LiveData<List<Device>> getAll(){
        return deviceRepository.getAll();
    }

    public void updateDevice(Device device){
        deviceRepository.update(device);
    }
    public void deleteDevice(String deviceEUI){
        deviceRepository.delete(deviceEUI);
    }
}