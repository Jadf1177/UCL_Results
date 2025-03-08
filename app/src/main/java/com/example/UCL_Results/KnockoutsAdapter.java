package com.example.UCL_Results;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KnockoutsAdapter extends RecyclerView.Adapter<KnockoutsAdapter.KnockoutsViewHolder> {

    @NonNull
    @Override
    public KnockoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.knockouts_item, parent, false);
        return new KnockoutsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnockoutsViewHolder holder, int position) {
        // No dynamic data needed for this adapter
    }

    @Override
    public int getItemCount() {
        return 1; // Only one item (the image)
    }

    // Make the ViewHolder public
    public static class KnockoutsViewHolder extends RecyclerView.ViewHolder {
        ImageView knockoutImage;

        public KnockoutsViewHolder(@NonNull View itemView) {
            super(itemView);
            knockoutImage = itemView.findViewById(R.id.knockout_image);
        }
    }
}