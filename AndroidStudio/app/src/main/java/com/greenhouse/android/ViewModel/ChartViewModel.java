package com.greenhouse.android.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.charts.Chart;
import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartViewModel extends ViewModel {

    DeviceRepository repository;
    List<GreenData> data;

    public ChartViewModel()
    {
        repository = DeviceRepository.getInstance();
        data = new ArrayList<>();
    }


    public LiveData<List<GreenData>> getChartData(String id, Date start, Date end)
    {
        return repository.getDeviceInterval(id, start, end);
    }

}
