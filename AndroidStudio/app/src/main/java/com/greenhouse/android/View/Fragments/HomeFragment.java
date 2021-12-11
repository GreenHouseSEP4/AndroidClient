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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.DevicesViewModel;
import com.greenhouse.android.Wrappers.Device;
import com.greenhouse.android.View.Adapters.DeviceListAdapter;
import com.greenhouse.android.ViewModel.HomeViewModel;
import com.greenhouse.android.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements DeviceListAdapter.OnListItemClickListener {

    private HomeViewModel homeViewModel;
    private DevicesViewModel devicesViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerViewMainPage;

    FloatingActionButton addDevice;

    List<Device> ghList;  // Moved it outside of the method so the clicklistener works.

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        devicesViewModel = new ViewModelProvider(this).get(DevicesViewModel.class);

        ghList = new ArrayList<>();
        DeviceListAdapter adapter = new DeviceListAdapter(ghList, this);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        devicesViewModel.getAll().observe(getViewLifecycleOwner(), adapter::updateData);


        //recycler view set up
        recyclerViewMainPage = root.findViewById(R.id.mainPageRecyclerView);
        recyclerViewMainPage.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMainPage.setAdapter(adapter);
        recyclerViewMainPage.hasFixedSize();

        addDevice = root.findViewById(R.id.home_add_device_button);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_add_device);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Bundle bundle = new Bundle();
            bundle.putString("name", ghList.get(clickedItemIndex).getLocation());
            bundle.putInt("temperature", ghList.get(clickedItemIndex).getLatest().getTemperature());
            bundle.putInt("light", ghList.get(clickedItemIndex).getLatest().getLight());
            bundle.putInt("co2", ghList.get(clickedItemIndex).getLatest().getCo2());
            bundle.putInt("humidity", ghList.get(clickedItemIndex).getLatest().getHumidity());


        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_greenhouse_show, bundle);

    }
}