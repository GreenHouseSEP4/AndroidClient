package com.greenhouse.android.View.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.greenhouse.android.R;
import com.greenhouse.android.View.RecyclerViewInfo.Indicator;
import com.greenhouse.android.View.RecyclerViewInfo.InfoAdapter;
import com.greenhouse.android.ViewModel.ChartViewModel;
import com.greenhouse.android.ViewModel.DeviceControlViewModel;
import com.greenhouse.android.ViewModel.available_times;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.Device;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeviceControlFragment extends Fragment {

    
    TextView name;
    ImageView backButton;
    String eui;
    
    Button windowOn;
    Button windowOff;
    Button windowHalf;
    Button lightOn;
    Button lightOff;
    Button waterOn;
    Button waterOff;
    

    private boolean allowEdit;
    DeviceControlViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        

        View view = inflater.inflate(R.layout.fragment_greenhouse_control, container, false);
        allowEdit = true;
        
        name = view.findViewById(R.id.greenhouse_name);
        name.setText(getArguments().getString("location"));
        eui = getArguments().getString("eui");

        windowOn = view.findViewById(R.id.control_window_open);
        windowOff = view.findViewById(R.id.control_window_closed);
        windowHalf = view.findViewById(R.id.control_window_half);
        lightOn = view.findViewById(R.id.control_light_open);
        lightOff = view.findViewById(R.id.control_light_closed);
        waterOn = view.findViewById(R.id.control_water_open);
        waterOff = view.findViewById(R.id.control_water_closed);
        
        
        viewModel = new ViewModelProvider(this).get(DeviceControlViewModel.class);

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("eui", getArguments().getString("eui"));
            bundle.putString("location", getArguments().getString("location"));
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_greenhouse_show,bundle);
        });

        viewModel.getResponse().observe(getViewLifecycleOwner(), s -> {
            allowEdit = true;
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });

        windowOn.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.openWindow(eui);
            }
        });
        windowOff.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.closeWindow(eui);
            }
        });
        windowHalf.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.openHalfWindow(eui);
            }
        });
        lightOn.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.openLight(eui);
            }
        });
        lightOff.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.closeLight(eui);
            }
        });
        waterOn.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.openWater(eui);
            }
        });
        waterOff.setOnClickListener(view1 -> {
            if (allowEdit) {
                allowEdit=false;
                viewModel.closeWater(eui);
            }
        });

        return view;
    }
    

    
}
