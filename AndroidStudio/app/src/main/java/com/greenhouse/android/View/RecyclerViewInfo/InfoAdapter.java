package com.greenhouse.android.View.RecyclerViewInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenhouse.android.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolderInfo>{

    List<Indicator> indicators;
    private final OnListItemClickListener onListItemClickListener;

    public InfoAdapter(List<Indicator> indicators, OnListItemClickListener listener)
    {
        this.indicators = indicators;
        this.onListItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.info_cell_item, parent, false);

        return new ViewHolderInfo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInfo holder, int position) {
        holder.infoIcon.setImageResource(indicators.get(position).getIconId());
        holder.infoLabel.setText(indicators.get(position).getName());
        holder.infoShow.setText(indicators.get(position).getShow() + "");
    }

    @Override
    public int getItemCount() {
        return indicators.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView infoIcon;
        TextView infoLabel;
        TextView infoShow;

        public ViewHolderInfo(@NonNull View itemView) {
            super(itemView);
            infoIcon = itemView.findViewById(R.id.indicatorIcon);
            infoLabel = itemView.findViewById(R.id.indicatorLabel);
            infoShow = itemView.findViewById(R.id.showInfo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener
    {
        void onListItemClick(int clickedItemIndex);
    }
}
