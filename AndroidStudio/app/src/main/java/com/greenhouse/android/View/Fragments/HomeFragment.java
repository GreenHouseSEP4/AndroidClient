package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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



        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}