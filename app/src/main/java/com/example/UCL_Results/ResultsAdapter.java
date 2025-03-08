package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.TeamViewHolder> {

    private final List<Team> teams;

    public ResultsAdapter(List<Team> teams) {
        // Create a copy of the teams list
        this.teams = new ArrayList<>(teams);

        // Sort teams by points (descending), then goal difference (descending)
        Collections.sort(this.teams, new Comparator<Team>() {
            @Override
            public int compare(Team team1, Team team2) {
                if (team1.getPoints() != team2.getPoints()) {
                    return team2.getPoints() - team1.getPoints(); // Descending by points
                }
                return team2.getGoalDifference() - team1.getGoalDifference(); // Descending by goal difference
            }
        });
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_standings_item, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.bind(teams.get(position));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        private final ImageView teamLogo;
        private final TextView teamName;
        private final TextView teamStats;
        private final TextView teamPoints;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamLogo = itemView.findViewById(R.id.team_logo);
            teamName = itemView.findViewById(R.id.team_name);
            teamStats = itemView.findViewById(R.id.team_stats);
            teamPoints = itemView.findViewById(R.id.team_points);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Team team) {
            // Display team logo
            teamLogo.setImageResource(TeamLogoHelper.getLogoResource(team.getName()));

            // Display team name
            teamName.setText(team.getName());

            // Display statistics
            teamStats.setText(String.format("W: %d | D: %d | L: %d | GD: %d",
                    team.getWins(), team.getDraws(), team.getLosses(), team.getGoalDifference()));

            // Display points
            teamPoints.setText(String.valueOf(team.getPoints()));
        }
    }
}