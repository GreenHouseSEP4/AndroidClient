package com.greenhouse.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GreenHouseInfoFragment extends Fragment {

    InfoAdapter adapter;
    RecyclerView indicatorsRecycler;
    List<Indicator> indicators;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_greenhouse_show, container, false);
        adapter = new InfoAdapter(indicators);

        indicatorsRecycler = view.findViewById(R.id.infoRecycler);
        indicatorsRecycler.hasFixedSize();
        indicatorsRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        indicatorsRecycler.setAdapter(adapter);


        return view;
    }
}
