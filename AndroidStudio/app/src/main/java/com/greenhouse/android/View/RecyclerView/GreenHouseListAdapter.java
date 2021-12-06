package com.greenhouse.android.View.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenhouse.android.R;

import java.util.List;

public class GreenHouseListAdapter extends RecyclerView.Adapter<GreenHouseListAdapter.ViewHolder> {

    List<GHListItem> ghList;

    public GreenHouseListAdapter (List<GHListItem> ghList){
        this.ghList = ghList;
    }

    @NonNull
    @Override
    public GreenHouseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gh_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.gHItemTitle.setText(ghList.get(position).getTitle());

    }


    @Override
    public int getItemCount() {
        return ghList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gHItemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gHItemTitle = itemView.findViewById(R.id.gHItemTitle);
        }
    }
}
