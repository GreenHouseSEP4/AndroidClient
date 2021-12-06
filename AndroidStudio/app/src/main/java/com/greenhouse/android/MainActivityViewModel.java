package com.greenhouse.android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    TemperatureRepository repository;

    public MainActivityViewModel()
    {
        repository = TemperatureRepository.getInstance();
    }

    LiveData<Temperature> getRequestedData()
    {
        return repository.getData();
    }

    public void requestData(int id)
    {
        repository.getAttempt(id);
    }

}