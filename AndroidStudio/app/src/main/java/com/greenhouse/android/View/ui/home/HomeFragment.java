package com.greenhouse.android.View.ui.home;

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
import com.greenhouse.android.View.RecyclerView.GHListItem;
import com.greenhouse.android.View.RecyclerView.GreenHouseListAdapter;
import com.greenhouse.android.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerViewMainPage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //recycler view set up
        recyclerViewMainPage = root.findViewById(R.id.mainPageRecyclerView);
        recyclerViewMainPage.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMainPage.hasFixedSize();

        List<GHListItem> ghList = new ArrayList<>();
        ghList.add(new GHListItem("GH1"));
        ghList.add(new GHListItem("GH2"));

        GreenHouseListAdapter adapter = new GreenHouseListAdapter(ghList);
        recyclerViewMainPage.setAdapter(adapter);


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