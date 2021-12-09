package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Wrappers.Device;

public class SettingsViewModel extends ViewModel {



    public SettingsViewModel() {

    }

    public void updateDevice(Device device){
        System.out.println(device);
        System.out.println(device.targetTemperature);
    }
}