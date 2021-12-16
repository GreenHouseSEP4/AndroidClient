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
import com.greenhouse.android.Wrappers.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoFragment extends Fragment implements InfoAdapter.OnListItemClickListener {

    InfoAdapter adapter;
    RecyclerView indicatorsRecycler;
    List<Indicator> indicators;
    TextView name;
    ImageView greenHouseImage;
    ImageView backButton;
    ImageView controlButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_greenhouse_show, container, false);

        name = view.findViewById(R.id.greenhouse_name);
        name.setText(getArguments().getString("location"));

        greenHouseImage = view.findViewById(R.id.greenHouse_image);
        greenHouseImage.setImageResource(R.drawable.gh1);

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_home));

        controlButton = view.findViewById(R.id.greenhouse_display_control);
        controlButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("eui", getArguments().getString("eui"));
            bundle.putString("location", getArguments().getString("location"));
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_control,bundle);
        });

        indicators = new ArrayList<>();
        indicators.add(new Indicator(R.drawable.sun_bright, "Light", getArguments().getInt("light")));
        indicators.add(new Indicator(R.drawable.temperature_half, "Temperature", getArguments().getInt("temperature")));
        indicators.add(new Indicator(R.drawable.droplet, "Humidity", getArguments().getInt("humidity")));
        indicators.add(new Indicator(R.drawable.wind, "CO2", getArguments().getInt("co2")));



        adapter = new InfoAdapter(indicators, this);

        indicatorsRecycler = view.findViewById(R.id.infoRecycler);
        indicatorsRecycler.hasFixedSize();
        indicatorsRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        indicatorsRecycler.setAdapter(adapter);


        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Bundle bundle = new Bundle();
        bundle.putString("eui", getArguments().getString("eui"));
        if(clickedItemIndex == 0)
        {
            bundle.putString("key", "light");
        }
        else if ( clickedItemIndex == 1)
        {
            bundle.putString("key", "temperature");
        }
        else if (clickedItemIndex == 2)
        {
            bundle.putString("key", "humidity");
        }
        else if (clickedItemIndex == 3)
        {
            bundle.putString("key", "co2");
        }


        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main_page).navigate(R.id.navigation_chart, bundle);

    }
}
