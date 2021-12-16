package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;

public class DeviceControlViewModel extends AndroidViewModel {

    public DeviceControlViewModel(Application application) {
        super(application);
        deviceRepository = DeviceRepository.getInstance(application);
    }

    private static DeviceRepository deviceRepository;


    public void openWindow(String eui) {
        deviceRepository.controlWindow(eui,100);
    }
    public void openHalfWindow(String eui) {
        deviceRepository.controlWindow(eui,50);
    }
    public void closeWindow(String eui) {
        deviceRepository.controlWindow(eui,0);
    }

    public void openWater(String eui) {
        deviceRepository.controlWater(eui,1);
    }

    public void closeWater(String eui) {
        deviceRepository.controlWater(eui,0);
    }

    public void openLight(String eui) {
        deviceRepository.controlLight(eui,1);
    }
    public void closeLight(String eui) {
        deviceRepository.controlLight(eui,0);
    }

    public LiveData<String> getResponse() {
        return deviceRepository.getControlResponse();
    }
    
}
