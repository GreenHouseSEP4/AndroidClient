package com.greenhouse.android.View.Fragments;

import android.content.Context;
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
import com.greenhouse.android.ViewModel.GreenhouseViewModel;
import com.greenhouse.android.Wrappers.APIResponse.GreenData;
import com.greenhouse.android.Wrappers.GreenHouse;
import com.greenhouse.android.View.Adapters.GreenHouseListAdapter;
import com.greenhouse.android.ViewModel.HomeViewModel;
import com.greenhouse.android.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private GreenhouseViewModel greenhouseViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerViewMainPage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        greenhouseViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        greenhouseViewModel.getLatest().observe(getViewLifecycleOwner(), new Observer<GreenData>() {
            @Override
            public void onChanged(GreenData greenData) {
                List<GreenHouse> ghList = new ArrayList<>();
                ghList.add(new GreenHouse("Greenhouse 1",greenData));
                ghList.add(new GreenHouse("Greenhouse 2",greenData));

                GreenHouseListAdapter adapter = new GreenHouseListAdapter(ghList);
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