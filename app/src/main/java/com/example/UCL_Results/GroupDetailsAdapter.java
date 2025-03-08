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
import java.util.List;

public class GroupDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_STANDINGS = 0;
    private static final int TYPE_MATCH = 1;

    private final List<Object> items = new ArrayList<>();

    public GroupDetailsAdapter(GroupStageTable groupTable, List<Match> matches) {
        // Add standings
        items.addAll(groupTable.getStandings());
        // Add matches
        items.addAll(matches);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Team ? TYPE_STANDINGS : TYPE_MATCH;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_STANDINGS) {
            View view = inflater.inflate(R.layout.team_standings_item, parent, false);
            return new StandingsViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.match_item, parent, false);
            return new MatchViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof StandingsViewHolder) {
            ((StandingsViewHolder) holder).bind((Team) item);
        } else if (holder instanceof MatchViewHolder) {
            ((MatchViewHolder) holder).bind((Match) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class StandingsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView teamLogo;
        private final TextView teamName;
        private final TextView teamPoints;
        private final TextView teamMatchesPlayed;
        private final TextView teamWins;
        private final TextView teamDraws;
        private final TextView teamLosses;
        private final TextView teamGoalsFor;
        private final TextView teamGoalsAgainst;
        private final TextView teamGoalDifference;

        public StandingsViewHolder(@NonNull View itemView) {
            super(itemView);
            teamLogo = itemView.findViewById(R.id.team_logo);
            teamName = itemView.findViewById(R.id.team_name);
            teamPoints = itemView.findViewById(R.id.team_points);
            teamMatchesPlayed = itemView.findViewById(R.id.team_matches_played);
            teamWins = itemView.findViewById(R.id.team_wins);
            teamDraws = itemView.findViewById(R.id.team_draws);
            teamLosses = itemView.findViewById(R.id.team_losses);
            teamGoalsFor = itemView.findViewById(R.id.team_goals_for);
            teamGoalsAgainst = itemView.findViewById(R.id.team_goals_against);
            teamGoalDifference = itemView.findViewById(R.id.team_goal_difference);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Team team) {
            teamLogo.setImageResource(TeamLogoHelper.getLogoResource(team.getName()));
            teamName.setText(team.getName());
            teamPoints.setText(String.valueOf(team.getPoints()));
            teamMatchesPlayed.setText(String.valueOf(team.getGamesPlayed()));
            teamWins.setText(String.valueOf(team.getWins()));
            teamDraws.setText(String.valueOf(team.getDraws()));
            teamLosses.setText(String.valueOf(team.getLosses()));
            teamGoalsFor.setText(String.valueOf(team.getGoalsScored()));
            teamGoalsAgainst.setText(String.valueOf(team.getGoalsConceded()));
            teamGoalDifference.setText(String.valueOf(team.getGoalDifference()));
        }
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
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