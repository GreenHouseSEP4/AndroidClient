package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.greenhouse.android.Model.GreenhouseRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

public class GreenhouseViewModel extends ViewModel {
    GreenhouseRepository repository;

    public GreenhouseViewModel()
    {
        repository = GreenhouseRepository.getInstance();
    }

    public LiveData<GreenData> getLatest(){
        return repository.getLatest();
    }

}