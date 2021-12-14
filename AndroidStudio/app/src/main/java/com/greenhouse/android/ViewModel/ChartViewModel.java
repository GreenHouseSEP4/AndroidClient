package com.greenhouse.android.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.charts.Chart;
import com.greenhouse.android.Model.DeviceRepository;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartViewModel extends ViewModel {

    DeviceRepository repository;
    List<GreenData> data;


    private Date calculateStart(Date end, available_times interval,int noOfPoints) {
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        switch (interval) {
            case minutes15:
                c.add(Calendar.MINUTE,-noOfPoints*15);
                break;
            case hours4:
                c.add(Calendar.HOUR,-noOfPoints*4);
                break;
            case days1:
                c.add(Calendar.DAY_OF_MONTH,-noOfPoints);
                break;
            case months1:
                c.add(Calendar.MONTH,-noOfPoints);
                break;
            case years1:
                c.add(Calendar.YEAR,-noOfPoints);
                break;
        }

        return c.getTime();
    }

    public ChartViewModel()
    {
        repository = DeviceRepository.getInstance();
        data = new ArrayList<>();
    }


    public LiveData<List<GreenData>> getChartData(String id,Date end ,available_times interval,int noOfPoints)
    {
        return repository.getDeviceInterval(id, calculateStart(end,interval,noOfPoints), end);
    }

}
