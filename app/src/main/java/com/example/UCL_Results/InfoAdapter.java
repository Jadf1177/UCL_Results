package com.example.UCL_Results;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {
    private final List<String> facts;

    public InfoAdapter(List<String> facts) {
        this.facts = facts;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_item, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        holder.bind(facts.get(position));
    }

    @Override
    public int getItemCount() {
        return facts.size();
    }

    // Make the ViewHolder public to match the adapter's visibility
    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        private final TextView factText;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            factText = itemView.findViewById(R.id.fact_text);
        }

        public void bind(String fact) {
            factText.setText(fact);
        }
    }
}