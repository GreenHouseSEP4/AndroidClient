package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.AddDeviceViewModel;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.databinding.FragmentAddDeviceBinding;

public class AddDeviceFragment extends Fragment {

    private AddDeviceViewModel addDeviceViewModel;
    private FragmentAddDeviceBinding binding;

    private Button saveButton;
    private EditText deviceId;

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


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddDeviceBinding.inflate(inflater, container, false);
        addDeviceViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        saveButton = getView().findViewById(R.id.add_save_Button);

        deviceId = getView().findViewById(R.id.add_devices_field);
        name = getView().findViewById(R.id.add_device_name_field);

        targetTemp = getView().findViewById(R.id.add_target_temp_field);
        targetHum = getView().findViewById(R.id.add_target_hum_field);
        targetCO2 = getView().findViewById(R.id.add_target_co2_field);
        targetLight = getView().findViewById(R.id.add_target_light_field);

        thresholdMinTemp = getView().findViewById(R.id.add_threshold_temp_min_field);
        thresholdMinHum = getView().findViewById(R.id.add_threshold_hum_min_field);
        thresholdMinCO2 = getView().findViewById(R.id.add_threshold_co2_min_field);
        thresholdMinLight = getView().findViewById(R.id.add_threshold_light_min_field);

        thresholdMaxTemp = getView().findViewById(R.id.add_threshold_temp_max_field);
        thresholdMaxHum = getView().findViewById(R.id.add_threshold_hum_max_field);
        thresholdMaxCO2 = getView().findViewById(R.id.add_threshold_co2_max_field);
        thresholdMaxLight = getView().findViewById(R.id.add_threshold_light_max_field);


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDevice();
            }
        });
    }


    private void updateDevice(){
        String newName = name.getText().toString();
        String id = deviceId.getText().toString();
        Device updated;
        if (id.length() != 16) {
            Toast.makeText(getContext(), "DeviceID should be 16 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

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

            updated = new Device(id,newName,tempThresholdMin,tempThresholdMax,humThresholdMin,humThresholdMax,co2ThresholdMin,co2ThresholdMax,lightThresholdMin,lightThresholdMax,tempTarget,humTarget,lightTarget,co2Target);

            addDeviceViewModel.createDevice(updated);
            Navigation.findNavController(getActivity(), R.id.fragment_add_device).navigate(R.id.navigation_home);
        } catch (Exception e) {
            e.printStackTrace();
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