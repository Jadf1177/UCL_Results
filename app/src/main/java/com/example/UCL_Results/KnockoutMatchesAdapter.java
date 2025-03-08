package com.example.UCL_Results;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class KnockoutMatchesAdapter extends RecyclerView.Adapter<KnockoutMatchesAdapter.MatchViewHolder> {
    private final List<Match> matches;

    public KnockoutMatchesAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    // Make the ViewHolder public
    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        private final ImageView homeTeamLogo;
        private final ImageView awayTeamLogo;
        private final TextView matchResultTextView;
        private final TextView dateTextView;
        private final TextView stageTextView;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTeamLogo = itemView.findViewById(R.id.home_team_logo);
            awayTeamLogo = itemView.findViewById(R.id.away_team_logo);
            matchResultTextView = itemView.findViewById(R.id.match_result);
            dateTextView = itemView.findViewById(R.id.match_date);
            stageTextView = itemView.findViewById(R.id.match_stage);
        }

        public void bind(Match match) {
            homeTeamLogo.setImageResource(TeamLogoHelper.getLogoResource(match.getHomeTeam().getName()));
            awayTeamLogo.setImageResource(TeamLogoHelper.getLogoResource(match.getAwayTeam().getName()));
            matchResultTextView.setText(match.getResult());
            dateTextView.setText(match.getDate());
            stageTextView.setText(match.getStage());
        }
    }
}