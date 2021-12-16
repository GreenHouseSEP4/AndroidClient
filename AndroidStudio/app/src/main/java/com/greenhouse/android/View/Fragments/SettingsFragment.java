package com.greenhouse.android.View.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.greenhouse.android.R;
import com.greenhouse.android.View.LoginActivity;
import com.greenhouse.android.View.RegisterActivity;
import com.greenhouse.android.ViewModel.SettingsViewModel;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    private Button saveButton;
    private Spinner deviceSelect;

    private EditText name;

    private EditText targetTemp;
    private EditText targetHum;
    private EditText targetCO2;
    private EditText targetLight;

    private EditText thresholdMinTemp;
    private EditText thresholdMinHum;
    private EditText thresholdMinCO2;
    private EditText thresholdMinLight;

    private EditText thresholdMaxTemp;
    private EditText thresholdMaxHum;
    private EditText thresholdMaxCO2;
    private EditText thresholdMaxLight;
    private TextView deleteDevice;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        saveButton = getView().findViewById(R.id.settings_save_Button);
        deviceSelect = getView().findViewById(R.id.settings_devices_spinner);

        settingsViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                ArrayAdapter<Device> spinnerCurrencyAdapter = new ArrayAdapter<Device>(getContext(), android.R.layout.simple_spinner_dropdown_item, devices);
                deviceSelect.setAdapter(spinnerCurrencyAdapter);
            }
        });



        deviceSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Device selected = (Device)adapterView.getSelectedItem();
                updateFields(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        name = getView().findViewById(R.id.settings_device_name_field);
        deleteDevice = getView().findViewById(R.id.settings_deleteDevice);

        targetTemp = getView().findViewById(R.id.settings_target_temp_field);
        targetHum = getView().findViewById(R.id.settings_target_hum_field);
        targetCO2 = getView().findViewById(R.id.settings_target_co2_field);
        targetLight = getView().findViewById(R.id.settings_target_light_field);

        thresholdMinTemp = getView().findViewById(R.id.settings_threshold_temp_min_field);
        thresholdMinHum = getView().findViewById(R.id.settings_threshold_hum_min_field);
        thresholdMinCO2 = getView().findViewById(R.id.settings_threshold_co2_min_field);
        thresholdMinLight = getView().findViewById(R.id.settings_threshold_light_min_field);

        thresholdMaxTemp = getView().findViewById(R.id.settings_threshold_temp_max_field);
        thresholdMaxHum = getView().findViewById(R.id.settings_threshold_hum_max_field);
        thresholdMaxCO2 = getView().findViewById(R.id.settings_threshold_co2_max_field);
        thresholdMaxLight = getView().findViewById(R.id.settings_threshold_light_max_field);


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDevice();
                Navigation.findNavController(getActivity(), R.id.settings_fragment).navigate(R.id.navigation_home);
            }
        });

        deleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device current = (Device)deviceSelect.getSelectedItem();
                String id = current.eui;
                settingsViewModel.deleteDevice(id);
                Navigation.findNavController(getActivity(), R.id.settings_fragment).navigate(R.id.navigation_home);
            }
        });
    }

    private void updateFields(Device current){
        name.setText(current.getLocation());

        targetTemp.setText(current.getTargetTemperature()+"");
        targetHum.setText(current.getTargetHumidity()+"");
        targetCO2.setText(current.getTargetCO2()+"");
        targetLight.setText(current.getTargetLight()+"");

        thresholdMinTemp.setText(current.getMinTemperature()+"");
        thresholdMinHum.setText(current.getMinHumidity()+"");
        thresholdMinCO2.setText(current.getMinCO2()+"");
        thresholdMinLight.setText(current.getMinLight()+"");

        thresholdMaxTemp.setText(current.getMaxTemperature()+"");
        thresholdMaxHum.setText(current.getMaxHumidity()+"");
        thresholdMaxCO2.setText(current.getMaxCO2()+"");
        thresholdMaxLight.setText(current.getMaxLight()+"");
    }

    private void updateDevice(){
        try {
            String newName = name.getText().toString();
            Device current = (Device)deviceSelect.getSelectedItem();
            String id = current.eui;

            int tempTarget = intFromEdit(targetTemp);
            int humTarget = intFromEdit(targetHum);
            int lightTarget = intFromEdit(targetLight);
            int co2Target = intFromEdit(targetCO2);

            int tempThresholdMin = intFromEdit(thresholdMinTemp);
            int humThresholdMin = intFromEdit(thresholdMinHum);
            int lightThresholdMin = intFromEdit(thresholdMinLight);
            int co2ThresholdMin = intFromEdit(thresholdMinCO2);

            int tempThresholdMax = intFromEdit(thresholdMaxTemp);
            int humThresholdMax = intFromEdit(thresholdMaxHum);
            int lightThresholdMax = intFromEdit(thresholdMaxLight);
            int co2ThresholdMax = intFromEdit(thresholdMaxCO2);

            Device updated = new Device(id,newName,tempThresholdMin,tempThresholdMax,humThresholdMin,humThresholdMax,co2ThresholdMin,co2ThresholdMax,lightThresholdMin,lightThresholdMax,tempTarget,humTarget,lightTarget,co2Target);

            settingsViewModel.updateDevice(updated);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Only whole numbers allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int intFromEdit(EditText field){
        return Integer.parseInt(field.getText().toString());
    }

}