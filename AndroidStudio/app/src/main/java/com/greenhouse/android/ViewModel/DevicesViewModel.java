package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;

public class DevicesViewModel extends ViewModel {
    DeviceRepository repository;

    public DevicesViewModel()
    {
        repository = DeviceRepository.getInstance();
    }

    public LiveData<List<Device>> getAll(){
        return repository.getAll();
    }

}