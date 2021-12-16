package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Model.UserRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;
public class AddDeviceViewModel extends AndroidViewModel {


    private static DeviceRepository deviceRepository;
    private static UserRepository userRepository;

    public AddDeviceViewModel(Application application) {
        super(application);
        deviceRepository = DeviceRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
    }

    public void createDevice(Device device){
        userRepository.addDevice(device.eui);
        deviceRepository.create(device);
    }
}