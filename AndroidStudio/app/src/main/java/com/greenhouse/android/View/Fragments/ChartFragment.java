package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.greenhouse.android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChartFragment extends Fragment {

    LineChart reportChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        reportChart = view.findViewById(R.id.reportChartView);
        reportChart.setTouchEnabled(true);
        reportChart.setPinchZoom(true);

        LineDataSet dataSet = new LineDataSet(setDataValues(), "Temperature");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        reportChart.setData(data);
        reportChart.invalidate();



        return view;
    }

    private ArrayList<Entry> setDataValues()
    {
         ArrayList<Entry> dataValues = new ArrayList<>();
         dataValues.add(new Entry(0, 20));
        dataValues.add(new Entry(1, 24));
        dataValues.add(new Entry(2, 27));
        dataValues.add(new Entry(3, 29));

        return dataValues;
    }
}
