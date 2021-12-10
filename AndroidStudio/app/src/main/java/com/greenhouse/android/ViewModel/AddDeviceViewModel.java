package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;
public class AddDeviceViewModel extends ViewModel {


    private static DeviceRepository deviceRepository;

    public AddDeviceViewModel() {
        deviceRepository = DeviceRepository.getInstance();
    }

    public void createDevice(Device device){
        deviceRepository.create(device);
    }
}