package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.DevicesViewModel;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.View.Adapters.DeviceListAdapter;
import com.greenhouse.android.ViewModel.HomeViewModel;
import com.greenhouse.android.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DevicesViewModel devicesViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerViewMainPage;

    FloatingActionButton addDevice;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        devicesViewModel = new ViewModelProvider(this).get(DevicesViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        devicesViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                DeviceListAdapter adapter = new DeviceListAdapter(devices);
                recyclerViewMainPage.setAdapter(adapter);
            }
        });


        //recycler view set up
        recyclerViewMainPage = root.findViewById(R.id.mainPageRecyclerView);
        recyclerViewMainPage.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMainPage.hasFixedSize();

        addDevice = root.findViewById(R.id.home_add_device_button);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDeviceFragment nextFrag= new AddDeviceFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main_page, nextFrag, "AddDeviceFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }

}