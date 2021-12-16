package com.greenhouse.android.View.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.greenhouse.android.R;
import com.greenhouse.android.Util.DateUtil;
import com.greenhouse.android.ViewModel.ChartViewModel;
import com.greenhouse.android.ViewModel.available_times;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartFragment extends Fragment {

    LineChart reportChart;
    ChartViewModel viewModel;
    Date now;
    Date start;
    TextView minutes;
    TextView hour;
    TextView day;
    TextView week;
    TextView month;
    TextView half_year;
    TextView year;
    TextView graphDate;
    String key;

    available_times timeChosen;
    String eui;

    Toast toast;

    boolean loading;

    final int noOfPoints = 25;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        minutes = view.findViewById(R.id.minutes);
        hour = view.findViewById(R.id.hour);
        day = view.findViewById(R.id.day);
        week = view.findViewById(R.id.week);
        month = view.findViewById(R.id.month);
        half_year = view.findViewById(R.id.half_year);
        year = view.findViewById(R.id.year);

        graphDate = view.findViewById(R.id.graph_date_interval);

        toast = new Toast(getContext());

        viewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        eui = getArguments().getString("eui");
        timeChosen = available_times.days7;
        key = getArguments().getString("key");
        selectInterval(week);

        now = new Date();
        start = DateUtil.calculateStart(now,available_times.days7,noOfPoints);
        graphDate.setText("First point: "+ start.toString()+"\nLast point: "+now.toString());

        viewModel.getChartData(eui,now, timeChosen,noOfPoints).observe(getViewLifecycleOwner(), this::updateData);

        loading = true;


        reportChart = view.findViewById(R.id.reportChartView);
        reportChart.setTouchEnabled(true);
        reportChart.setPinchZoom(true);

        reportChart.getAxisLeft().setDrawGridLines(false);
        reportChart.getAxisRight().setDrawGridLines(false);
        reportChart.getXAxis().setDrawGridLines(false);
        reportChart.getXAxis().setEnabled(false);

        Description description = reportChart.getDescription();
        description.setEnabled(false);

        minutes.setOnClickListener(v -> {
            if (!loading) {
                selectInterval(minutes);
                timeChosen = available_times.minutes15;
                getData();
            }
        });
        hour.setOnClickListener(v -> {
            if(!loading){
                selectInterval(hour);
                timeChosen = available_times.hours4;
                getData();
            }
        });
        day.setOnClickListener(v-> {
            if(!loading){
                selectInterval(day);
                timeChosen = available_times.days1;
                getData();
            }
        });
        week.setOnClickListener(v-> {
            if(!loading){
                selectInterval(week);
                timeChosen = available_times.days7;
                getData();
            }
        });
        month.setOnClickListener(v-> {
            if(!loading){
                selectInterval(month);
                timeChosen = available_times.months1;
                getData();
            }
        });
        half_year.setOnClickListener(v-> {
            if(!loading){
                selectInterval(half_year);
                timeChosen = available_times.months6;
                getData();
            }
        });
        year.setOnClickListener(v-> {
            if(!loading){
                selectInterval(year);
                timeChosen = available_times.years1;
                getData();
            }
        });

        return view;
    }

    private void resetInterval(TextView interval) {
        interval.setTextColor(getResources().getColor(R.color.black));
        interval.setTextSize(15);
    }

    private void selectInterval(TextView interval) {
        resetInterval(minutes);
        resetInterval(hour);
        resetInterval(day);
        resetInterval(week);
        resetInterval(month);
        resetInterval(half_year);
        resetInterval(year);
        interval.setTextColor(getResources().getColor(R.color.green));
        interval.setTextSize(20);
        toast.cancel();
        toast = Toast.makeText(getContext(),"Loading!",Toast.LENGTH_SHORT);
        toast.show();
    }

    @NonNull
    private ArrayList<Entry> setDataValues(List<GreenData> dataToShow)
    {
         ArrayList<Entry> dataValues = new ArrayList<>();
         for(int i = 0; i < dataToShow.size(); i++)
         {
             if(key.equals("light"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getLight()));
             }
             else if (key.equals("temperature"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getTemperature()));
             }
             else if(key.equals("humidity"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getHumidity()));
             }
             else if(key.equals("co2"))
             {
                 dataValues.add(new Entry(i+1, dataToShow.get(i).getCo2()));
             }
         }

        YAxis axis = reportChart.getAxisLeft();
        axis.removeAllLimitLines();

//        LimitLine lineMax = new LimitLine(25, "Maximum Limit");
//        lineMax.setLineWidth(4);
//        lineMax.enableDashedLine(10, 10, 0);
//        lineMax.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        lineMax.setTextSize(10);
//
//        LimitLine lineMin = new LimitLine(22, "Minimum Limit");
//        lineMin.setLineWidth(4);
//        lineMin.enableDashedLine(10, 10, 0);
//        lineMin.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        lineMin.setTextSize(10);
//
//
//        axis.addLimitLine(lineMax);
//        axis.addLimitLine(lineMin);

        return dataValues;
    }

    public void getData()
    {
        viewModel.getChartData(eui, now, timeChosen,noOfPoints);
        start = DateUtil.calculateStart(now,timeChosen,noOfPoints);
        graphDate.setText("First point: "+now.toString()+"\nLast point: "+ start.toString());
    }

    public void updateData(List<GreenData> newData)
    {
        LineDataSet dataSet = new LineDataSet(setDataValues(newData), key);
        dataSet.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_green);
            dataSet.setFillDrawable(drawable);
        }
        else {
            dataSet.setFillColor(Color.BLACK);
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData data = new LineData(dataSets);
        reportChart.setData(data);
        reportChart.notifyDataSetChanged();
        reportChart.invalidate(); // Everytime data is changed this refreshes the chart
        toast.cancel();
        toast = Toast.makeText(getContext(),"Loading finished!",Toast.LENGTH_SHORT);
        toast.show();
        loading = false;
    }
}
