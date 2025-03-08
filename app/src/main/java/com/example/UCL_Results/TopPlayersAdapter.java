package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TopPlayersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PLAYER = 1;

    private final List<Object> items;

    public TopPlayersAdapter(List<PlayerStats> players) {
        this.items = new ArrayList<>();
        prepareItems(players);
    }

    private void prepareItems(List<PlayerStats> players) {
        items.clear();
        // Top Scorers
        items.add("Top 5 Scorers");
        items.addAll(players.subList(0, 5));

        // Top Assists
        items.add("Top 5 Assists");
        items.addAll(players.subList(5, 10));

        // Top Rated
        items.add("Top 5 Rated Players");
        items.addAll(players.subList(10, 15));
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_PLAYER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.player_header_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.player_item, parent, false);
            return new PlayerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) item);
        } else {
            ((PlayerViewHolder) holder).bind((PlayerStats) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }

        public void bind(String header) {
            headerText.setText(header);
        }
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerName;
        private final TextView playerStats;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            playerStats = itemView.findViewById(R.id.player_stats);
        }

        public void bind(PlayerStats player) {
            playerName.setText(player.getName());

            @SuppressLint("DefaultLocale") String stats = String.format("%s • %d goals • %d assists • %.1f rating",
                    player.getTeam(),
                    player.getGoals(),
                    player.getAssists(),
                    player.getRating());

            playerStats.setText(stats);
        }
    }
}
