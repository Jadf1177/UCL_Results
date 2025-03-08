package com.example.UCL_Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class TeamStatsActivity extends AppCompatActivity {
    private ImageView teamLogo;
    private TextView teamName;
    private TextView teamStats;
    private RecyclerView matchesRecyclerView;
    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_stats);

        initializeViews();
        loadData();
    }

    private void initializeViews() {
        teamLogo = findViewById(R.id.team_logo);
        teamName = findViewById(R.id.team_name);
        teamStats = findViewById(R.id.team_stats);
        matchesRecyclerView = findViewById(R.id.team_matches_recycler_view);
        Button continueButton = findViewById(R.id.continue_button);

        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        continueButton.setOnClickListener(v -> {
            // כאן תוסיף לאן תרצה לנווט
            Toast.makeText(this, "Continuing...", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadData() {
        try {
            tournament = (Tournament) getIntent().getSerializableExtra("tournament");
            Team team = (Team) getIntent().getSerializableExtra("team");

            if (team != null && tournament != null) {
                loadTeamData(team);
                setupMatchesList(team);

                // עדכון כותרת המסך
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(team.getName());
                    getSupportActionBar().setSubtitle("Group " + team.getGroupName());
                }
            } else {
                Toast.makeText(this, "Error loading team data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadTeamData(Team team) {
        teamLogo.setImageResource(getTeamLogoResource(team.getName()));
        teamName.setText(team.getName());

        // שימוש בפורמט מובנה להצגת סטטיסטיקות
        @SuppressLint("DefaultLocale") String stats = String.format(
                "Games Played: %d\n\n" +
                        "Wins: %d\n" +
                        "Draws: %d\n" +
                        "Losses: %d\n\n" +
                        "Goals Scored: %d\n" +
                        "Goals Conceded: %d\n" +
                        "Goal Difference: %d\n\n" +
                        "Total Points: %d",
                team.getGamesPlayed(),
                team.getWins(),
                team.getDraws(),
                team.getLosses(),
                team.getGoalsScored(),
                team.getGoalsConceded(),
                team.getGoalDifference(),
                team.getPoints()
        );
        teamStats.setText(stats);
    }

    private void setupMatchesList(Team team) {
        ArrayList<Match> teamMatches = tournament.getMatchesByTeam(team);
        if (teamMatches.isEmpty()) {
            Toast.makeText(TeamStatsActivity.this,
                    "No matches found for this team",
                    Toast.LENGTH_SHORT).show();
        }

        // עדכון: שימוש בגרסה החדשה של MatchesAdapter
        MatchesAdapter adapter = new MatchesAdapter(
                this,                // context
                teamMatches,        // matches list
                match -> {          // onClick listener
                    String result = match.getResult();
                    Toast.makeText(TeamStatsActivity.this,
                            result,
                            Toast.LENGTH_SHORT).show();
                },
                new DatabaseManager(this, tournament)  // database manager
        );
        matchesRecyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private int getTeamLogoResource(String teamName) {
        return TeamLogoHelper.getLogoResource(teamName);
    }
}