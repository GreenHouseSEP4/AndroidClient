package com.greenhouse.android.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class GreenHouseInfoVM extends AndroidViewModel {

    private DeviceRepository repository;

    public GreenHouseInfoVM(Application application)
    {
        super(application);
        repository = repository.getInstance(application);
    }


}
