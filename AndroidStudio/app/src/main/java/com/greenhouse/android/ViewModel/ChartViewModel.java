package com.greenhouse.android.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.charts.Chart;
import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartViewModel extends AndroidViewModel {

    DeviceRepository repository;


    public ChartViewModel(Application application)
    {
        super(application);
        repository = DeviceRepository.getInstance(application);
    }


    public LiveData<List<GreenData>> getChartData(String id,Date end ,available_times interval,int noOfPoints)
    {
        return repository.getChartData(id,end,interval,noOfPoints);
    }

    public LiveData<Device> getDeviceData(String eui) {
        return repository.getDeviceToView(eui);
    }

}
