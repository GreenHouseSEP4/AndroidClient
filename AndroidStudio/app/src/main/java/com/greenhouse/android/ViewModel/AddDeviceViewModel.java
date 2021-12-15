package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;
public class AddDeviceViewModel extends AndroidViewModel {


    private static DeviceRepository deviceRepository;

    public AddDeviceViewModel(Application application) {
        super(application);
        deviceRepository = DeviceRepository.getInstance(application);
    }

    public void createDevice(Device device){
        deviceRepository.create(device);
    }
}