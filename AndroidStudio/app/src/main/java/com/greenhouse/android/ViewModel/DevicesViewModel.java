package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;

public class DevicesViewModel extends AndroidViewModel {
    DeviceRepository repository;

    public DevicesViewModel(Application application)
    {
        super(application);
        repository = DeviceRepository.getInstance(application);
    }

    public LiveData<List<Device>> getAll(){
        return repository.getAll();
    }

}