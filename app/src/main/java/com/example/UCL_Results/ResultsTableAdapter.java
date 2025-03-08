package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsTableAdapter extends RecyclerView.Adapter<ResultsTableAdapter.ResultViewHolder> {

    private final List<TeamResult> teamResults;

    public ResultsTableAdapter(DatabaseHelper dbHelper) {
        teamResults = loadTeamResults(dbHelper);
    }

    private List<TeamResult> loadTeamResults(DatabaseHelper dbHelper) {
        List<TeamResult> results = new ArrayList<>();
        Cursor cursor = dbHelper.getFullResultsTable();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") TeamResult result = new TeamResult(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOAL_TEAM)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_SCORED)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_CONCEDED)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_WINS)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_DRAWS)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_LOSSES)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_POINTS))
                );
                results.add(result);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(teamResults.get(position));
    }

    @Override
    public int getItemCount() {
        return teamResults.size();
    }

    // View Holder עבור שורת תוצאה בטבלה
    static class ResultViewHolder extends RecyclerView.ViewHolder {
        private final TextView teamNameTextView;
        private final TextView goalsTextView;
        private final TextView winsTextView;
        private final TextView drawsTextView;
        private final TextView lossesTextView;
        private final TextView pointsTextView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.team_name);
            goalsTextView = itemView.findViewById(R.id.team_goals);
            winsTextView = itemView.findViewById(R.id.team_wins);
            drawsTextView = itemView.findViewById(R.id.team_draws);
            lossesTextView = itemView.findViewById(R.id.team_losses);
            pointsTextView = itemView.findViewById(R.id.team_points);
        }

        @SuppressLint("DefaultLocale")
        public void bind(TeamResult result) {
            teamNameTextView.setText(result.getTeamName());
            goalsTextView.setText(String.format("%d:%d", result.getGoalsScored(), result.getGoalsConceded()));
            winsTextView.setText(String.valueOf(result.getWins()));
            drawsTextView.setText(String.valueOf(result.getDraws()));
            lossesTextView.setText(String.valueOf(result.getLosses()));
            pointsTextView.setText(String.valueOf(result.getPoints()));
        }
    }

    // מחלקה פנימית לייצוג תוצאת קבוצה בטבלה
    public static class TeamResult {
        private final String teamName;
        private final int goalsScored;
        private final int goalsConceded;
        private final int wins;
        private final int draws;
        private final int losses;
        private final int points;

        public TeamResult(String teamName, int goalsScored, int goalsConceded,
                          int wins, int draws, int losses, int points) {
            this.teamName = teamName;
            this.goalsScored = goalsScored;
            this.goalsConceded = goalsConceded;
            this.wins = wins;
            this.draws = draws;
            this.losses = losses;
            this.points = points;
        }

        public String getTeamName() {
            return teamName;
        }

        public int getGoalsScored() {
            return goalsScored;
        }

        public int getGoalsConceded() {
            return goalsConceded;
        }

        public int getWins() {
            return wins;
        }

        public int getDraws() {
            return draws;
        }

        public int getLosses() {
            return losses;
        }

        public int getPoints() {
            return points;
        }

        public int getGoalDifference() {
            return goalsScored - goalsConceded;
        }
    }
}