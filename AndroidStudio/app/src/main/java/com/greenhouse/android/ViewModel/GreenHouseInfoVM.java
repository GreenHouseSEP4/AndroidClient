package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class GreenHouseInfoVM extends ViewModel {

    private DeviceRepository repository;

    public GreenHouseInfoVM()
    {
        repository = repository.getInstance();
    }


}
