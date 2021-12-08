package com.greenhouse.android.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenhouse.android.R;
import com.greenhouse.android.Wrappers.GreenHouse;

import java.util.List;

public class GreenHouseListAdapter extends RecyclerView.Adapter<GreenHouseListAdapter.ViewHolder> {

    List<GreenHouse> ghList;

    public GreenHouseListAdapter (List<GreenHouse> ghList){
        this.ghList = ghList;
    }

    @NonNull
    @Override
    public GreenHouseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.greenhouse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GreenHouse current = ghList.get(position);
        holder.gHItemTitle.setText(current.getTitle());
        holder.ghHum.setText(current.getLatest().humidity+" %");
        holder.ghTemp.setText(current.getLatest().temperature+" °C");
        holder.ghLight.setText(current.getLatest().light+" LUM");
        holder.ghCO2.setText(current.getLatest().co2+" PPM");
    }


    @Override
    public int getItemCount() {
        return ghList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gHItemTitle;
        TextView ghHum;
        TextView ghTemp;
        TextView ghLight;
        TextView ghCO2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gHItemTitle = itemView.findViewById(R.id.gh_title);
            ghHum = itemView.findViewById(R.id.gh_start_hum);
            ghTemp = itemView.findViewById(R.id.gh_start_temp) ;
            ghLight  = itemView.findViewById(R.id.gh_start_light);
            ghCO2  = itemView.findViewById(R.id.gh_start_co2);
        }
    }
}
