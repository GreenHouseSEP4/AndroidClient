package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.greenhouse.android.R;

public class ChartFragment extends Fragment {

    LineChart reportChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        reportChart = view.findViewById(R.id.reportChartView);
        reportChart.setTouchEnabled(true);
        reportChart.setPinchZoom(true);

        return view;
    }
}
