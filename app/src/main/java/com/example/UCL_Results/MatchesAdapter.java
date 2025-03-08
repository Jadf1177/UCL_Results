package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private final List<Match> matches;
    private final DatabaseManager dbManager;
    private final Context context;

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    public MatchesAdapter(Context context, List<Match> matches, OnMatchClickListener listener, DatabaseManager dbManager) {
        this.context = context;
        this.matches = matches;
        this.dbManager = dbManager;
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
        Match match = matches.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteDialog(matches.get(position));
                }
            });
        }

        public void bind(Match match) {
            homeTeamLogo.setImageResource(TeamLogoHelper.getLogoResource(match.getHomeTeam().getName()));
            awayTeamLogo.setImageResource(TeamLogoHelper.getLogoResource(match.getAwayTeam().getName()));
            matchResultTextView.setText(match.getResult());
            dateTextView.setText(match.getDate());
            stageTextView.setText(match.getStage());
        }

        @SuppressLint("NotifyDataSetChanged")
        // In MatchesAdapter.java, in the showDeleteDialog method
        private void showDeleteDialog(Match match) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.match_item, null);

            // ... (existing dialog setup code)

            builder.setView(dialogView)
                    .setTitle("Match Details")
                    .setNegativeButton("Close", null)
                    .setNeutralButton("Delete", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Match")
                        .setMessage("Are you sure you want to delete this match?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            // Remove match from the list
                            matches.remove(match);

                            // Delete from database directly
                            dbManager.getDbHelper().deleteMatch(
                                    match.getDate(),
                                    match.getHomeTeam().getName(),
                                    match.getAwayTeam().getName()
                            );

                            // Export the updated list to the database to ensure it's persisted
                            dbManager.exportTournamentToDatabase();

                            // Notify the adapter and show feedback
                            notifyDataSetChanged();
                            Toast.makeText(context, "Match deleted successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }
    }
    }
