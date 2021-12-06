package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.TemperatureRepository;
import com.greenhouse.android.Wrappers.Temperature;

public class MainActivityViewModel extends ViewModel {
    TemperatureRepository repository;

    public MainActivityViewModel()
    {
        repository = TemperatureRepository.getInstance();
    }

    public LiveData<Temperature> getRequestedData()
    {
        return repository.getData();
    }

    public void requestData(int id)
    {
        repository.getAttempt(id);
    }

}