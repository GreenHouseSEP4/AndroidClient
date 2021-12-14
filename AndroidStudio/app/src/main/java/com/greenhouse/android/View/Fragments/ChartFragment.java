package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.ChartViewModel;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartFragment extends Fragment {

    LineChart reportChart;
    ChartViewModel viewModel;
    Date now;
    Date before;
    TextView minutes;
    TextView hour;
    TextView day;
    TextView month;
    TextView year;
    long timeToLong;
    long timeChosen;
    String eui;

    List<GreenData> dataToShow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        timeChosen = TimeUnit.MINUTES.toMillis(15);
        eui = getArguments().getString("eui");

        now = new Date(System.currentTimeMillis());
        timeToLong =  now.getTime();
        before =new Date(timeToLong - (10 * timeChosen));

        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        viewModel = new ViewModelProvider(this).get(ChartViewModel.class);
        viewModel.getChartData(eui, before, now).observe(getViewLifecycleOwner(), this::updateData);

        minutes = view.findViewById(R.id.minutes);
        hour = view.findViewById(R.id.hour);
        day = view.findViewById(R.id.day);
        month = view.findViewById(R.id.month);
        year = view.findViewById(R.id.year);

        minutes.setOnClickListener(v -> {
            timeChosen = TimeUnit.MINUTES.toMillis(15);
            getData();
            setDataValues();
        });
        hour.setOnClickListener(v -> {
            timeChosen = TimeUnit.HOURS.toMillis(4);
            getData();
            setDataValues();
        });
        day.setOnClickListener(v-> {
            timeChosen = TimeUnit.DAYS.toMillis(1);
            getData();
            setDataValues();
        });
        month.setOnClickListener(v-> {
            timeChosen = TimeUnit.DAYS.toMillis(30);
            getData();
            setDataValues();
        });
        year.setOnClickListener(v-> {
            timeChosen = TimeUnit.DAYS.toMillis(365);
            getData();
            setDataValues();
        });

        reportChart = view.findViewById(R.id.reportChartView);
        reportChart.setTouchEnabled(true);
        reportChart.setPinchZoom(true);

        LineDataSet dataSet = new LineDataSet(setDataValues(), getArguments().getString("key"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        reportChart.setData(data);
        reportChart.invalidate();

        return view;
    }

    @NonNull
    private ArrayList<Entry> setDataValues()
    {
         ArrayList<Entry> dataValues = new ArrayList<>();
         dataToShow = viewModel.getChartData(eui, before, now).getValue();
         for(int i = 0; i < dataToShow.size(); i++)
         {
             if(getArguments().getString("key").equals("light"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getLight()));
             }
             else if (getArguments().getString("key").equals("temperature"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getTemperature()));
             }
             else if(getArguments().getString("key").equals("humidity"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getHumidity()));
             }
             else if(getArguments().getString("key").equals("light"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getCo2()));
             }
         }

        LimitLine lineMax = new LimitLine(25, "Maximum Limit");
        lineMax.setLineWidth(4);
        lineMax.enableDashedLine(10, 10, 0);
        lineMax.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        lineMax.setTextSize(10);

        LimitLine lineMin = new LimitLine(22, "Minimum Limit");
        lineMin.setLineWidth(4);
        lineMin.enableDashedLine(10, 10, 0);
        lineMin.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lineMin.setTextSize(10);

        YAxis axis = reportChart.getAxisLeft();
        axis.removeAllLimitLines();
        axis.addLimitLine(lineMax);
        axis.addLimitLine(lineMin);

        return dataValues;
    }

    public void getData()
    {
        before =new Date(timeToLong - (10 * timeChosen));
        dataToShow =  viewModel.getChartData(eui, now, before).getValue();
    }

    public void updateData(List<GreenData> data)
    {
        dataToShow = data;
    }
}
