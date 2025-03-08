package com.example.UCL_Results;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class StagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GROUP_HEADER = 0;
    private static final int TYPE_STANDINGS = 1;
    private static final int TYPE_MATCH = 2;
    private static final int TYPE_KNOCKOUT_HEADER = 3;

    private final Tournament tournament;
    private final List<Object> items;

    public StagesAdapter(Tournament tournament) {
        this.tournament = tournament;
        this.items = new ArrayList<>();
        prepareItems();
    }

    private void prepareItems() {
        items.clear();

        // Add group stages
        for (char group = 'A'; group <= 'H'; group++) {
            String groupName = "Group " + group;
            items.add(groupName); // Add group header
        }

        // Add knockout stages
        String[] knockoutStages = {"Round of 16", "Quarter Final", "Semi Final", "Final"};
        for (String stage : knockoutStages) {
            items.add(stage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            return ((String) item).startsWith("Group ") ? TYPE_GROUP_HEADER : TYPE_KNOCKOUT_HEADER;
        }
        if (item instanceof Team) return TYPE_STANDINGS;
        if (item instanceof Match) return TYPE_MATCH;
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_GROUP_HEADER:
            case TYPE_KNOCKOUT_HEADER:
                View headerView = inflater.inflate(R.layout.stage_header_item, parent, false);
                return new HeaderViewHolder(headerView);

            case TYPE_STANDINGS:
                View standingsView = inflater.inflate(R.layout.team_standings_item, parent, false);
                return new StandingsViewHolder(standingsView);

            case TYPE_MATCH:
                View matchView = inflater.inflate(R.layout.match_item, parent, false);
                return new MatchViewHolder(matchView);

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_GROUP_HEADER:
            case TYPE_KNOCKOUT_HEADER:
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.bind((String) item);
                break;

            case TYPE_STANDINGS:
                StandingsViewHolder standingsHolder = (StandingsViewHolder) holder;
                standingsHolder.bind((Team) item);
                break;

            case TYPE_MATCH:
                MatchViewHolder matchHolder = (MatchViewHolder) holder;
                matchHolder.bind((Match) item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerText;
        private final Context context;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            headerText = itemView.findViewById(R.id.stage_name);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String header = (String) items.get(position);

                    if (header.startsWith("Group")) {
                        openGroupDetails(header);
                    } else if (isKnockoutStage(header)) {
                        openKnockoutDetails(header);
                    }
                }
            });
        }

        private void openGroupDetails(String groupName) {
            try {
                Intent intent = new Intent(context, GroupDetailsActivity.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("tournament", tournament);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error opening group details", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private void openKnockoutDetails(String stageName) {
            try {
                Intent intent = new Intent(context, KnockoutDetailsActivity.class);
                intent.putExtra("stageName", stageName);
                intent.putExtra("tournament", tournament);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error opening knockout details", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private boolean isKnockoutStage(String stageName) {
            return stageName.equals("Round of 16") ||
                    stageName.equals("Quarter Final") ||
                    stageName.equals("Semi Final") ||
                    stageName.equals("Final");
        }

        public void bind(String header) {
            headerText.setText(header);
        }
    }

    static class StandingsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView teamLogo;
        private final TextView teamName;
        private final TextView teamStats;
        private final TextView teamPoints;

        public StandingsViewHolder(@NonNull View itemView) {
            super(itemView);
            teamLogo = itemView.findViewById(R.id.team_logo);
            teamName = itemView.findViewById(R.id.team_name);
            teamStats = itemView.findViewById(R.id.team_stats);
            teamPoints = itemView.findViewById(R.id.team_points);
        }

        public void bind(Team team) {
            teamLogo.setImageResource(TeamLogoHelper.getLogoResource(team.getName()));
            teamName.setText(team.getName());
            teamStats.setText(String.format("Matches: %d | GD: %d",
                    team.getGamesPlayed(), team.getGoalDifference()));
            teamPoints.setText(String.valueOf(team.getPoints()));
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