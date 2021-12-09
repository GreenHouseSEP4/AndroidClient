package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.GreenhouseRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class GreenHouseInfoVM extends ViewModel {

    private GreenhouseRepository repository;

    public GreenHouseInfoVM()
    {
        repository = GreenhouseRepository.getInstance();
    }

    public LiveData<GreenData> getData()
    {
        return repository.getLatest();
    }

}
