package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;
public class SettingsViewModel extends ViewModel {


    private static DeviceRepository deviceRepository;

    public SettingsViewModel() {
        deviceRepository = DeviceRepository.getInstance();
    }

    public LiveData<List<Device>> getAll(){
        return deviceRepository.getAll();
    }

    public void updateDevice(Device device){
        deviceRepository.update(device);
    }
}