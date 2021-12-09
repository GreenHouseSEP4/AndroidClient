package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greenhouse.android.R;
import com.greenhouse.android.View.RecyclerViewInfo.Indicator;
import com.greenhouse.android.View.RecyclerViewInfo.InfoAdapter;
import com.greenhouse.android.ViewModel.GreenHouseInfoVM;
import com.greenhouse.android.Wrappers.GreenHouse;

import java.util.ArrayList;
import java.util.List;

public class GreenHouseInfoFragment extends Fragment {

    GreenHouseInfoVM viewModel;
    InfoAdapter adapter;
    RecyclerView indicatorsRecycler;
    GreenHouse greenHouseToShow;
    List<Indicator> indicators;
    TextView name;
    ImageView greenHouseImage;
    ImageView backButton;
    ImageView setThreshold;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(GreenHouseInfoVM.class);

        View view = inflater.inflate(R.layout.fragment_greenhouse_show, container, false);

        name = view.findViewById(R.id.greenhouse_name);
        name.setText(getArguments().getString("name"));

        greenHouseImage = view.findViewById(R.id.greenHouse_image);
        greenHouseImage.setImageResource(R.drawable.gh1);

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_home));

        setThreshold = view.findViewById(R.id.set_values);
       // backButton.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate());  /////// NAVIGATE TO THRESHOLD SETTING

        indicators = new ArrayList<>();
        indicators.add(new Indicator(R.drawable.ic_sun_icon, "Light", getArguments().getInt("light")));
        indicators.add(new Indicator(R.drawable.ic_thermostat_icon, "Temperature", getArguments().getInt("temperature")));
        indicators.add(new Indicator(R.drawable.ic_humidity_icon, "Humidity", getArguments().getInt("humidity")));
        indicators.add(new Indicator(R.drawable.ic_co2, "CO2", getArguments().getInt("co2")));



        adapter = new InfoAdapter(indicators);

        indicatorsRecycler = view.findViewById(R.id.infoRecycler);
        indicatorsRecycler.hasFixedSize();
        indicatorsRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        indicatorsRecycler.setAdapter(adapter);


        return view;
    }
}
