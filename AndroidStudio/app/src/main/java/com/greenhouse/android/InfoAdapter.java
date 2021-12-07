package com.greenhouse.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{

    List<Indicator> indicators;

    InfoAdapter(List<Indicator> indicators)
    {
        this.indicators = indicators;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.info_cell_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.infoIcon.setImageResource(indicators.get(position).getIconId());
        holder.infoLabel.setText(indicators.get(position).getName());
        holder.infoShow.setText(indicators.get(position).getShow());
    }

    @Override
    public int getItemCount() {
        return indicators.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView infoIcon;
        TextView infoLabel;
        TextView infoShow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClipToOutline(true);  /////////// That's supposed to make the corners rounded. ////////////////
            infoIcon = itemView.findViewById(R.id.indicatorIcon);
            infoLabel = itemView.findViewById(R.id.indicatorLabel);
            infoShow = itemView.findViewById(R.id.showInfo);
        }
    }
}
