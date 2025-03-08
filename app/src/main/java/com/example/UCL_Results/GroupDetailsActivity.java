package com.example.UCL_Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class GroupDetailsActivity extends AppCompatActivity {
    private TextView groupTitle;
    private RecyclerView recyclerView;
    private Tournament tournament;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // Get data from intent
        groupName = getIntent().getStringExtra("groupName");
        tournament = (Tournament) getIntent().getSerializableExtra("tournament");

        initializeViews();
        loadGroupData();
    }

    private void initializeViews() {
        groupTitle = findViewById(R.id.group_title);
        recyclerView = findViewById(R.id.recycler_view);
        Button backButton = findViewById(R.id.back_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Back button
        backButton.setOnClickListener(v -> finish());
    }

    private void loadGroupData() {
        if (groupName != null && tournament != null) {
            groupTitle.setText(groupName);

            // Load group data
            GroupStageTable groupTable = tournament.getGroupTable(groupName.replace("Group ", ""));

            // Sort teams by points (highest first)
            // This ensures teams are displayed in correct order
            List<Team> sortedTeams = new ArrayList<>(groupTable.getStandings());
            sortedTeams.sort((t1, t2) -> {
                if (t1.getPoints() != t2.getPoints()) {
                    return t2.getPoints() - t1.getPoints(); // Descending by points
                }
                return t2.getGoalDifference() - t1.getGoalDifference(); // Then by goal difference
            });

            // Create a new GroupStageTable with sorted teams
            GroupStageTable sortedGroupTable = new GroupStageTable();
            for (Team team : sortedTeams) {
                sortedGroupTable.addTeam(team);
            }

            List<Match> groupMatches = tournament.getMatchesByStage(groupName);

            // Create adapter for group details
            GroupDetailsAdapter adapter = new GroupDetailsAdapter(sortedGroupTable, groupMatches);
            recyclerView.setAdapter(adapter);
        }
    }
}