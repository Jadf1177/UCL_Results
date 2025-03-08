package com.example.UCL_Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class KnockoutDetailsActivity extends AppCompatActivity {
    private TextView stageTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knockout_details);

        String stageName = getIntent().getStringExtra("stageName");
        Tournament tournament = (Tournament) getIntent().getSerializableExtra("tournament");

        initializeViews();
        if (stageName != null && tournament != null) {
            loadStageData(stageName, tournament);
        }
    }

    private void initializeViews() {
        stageTitle = findViewById(R.id.stage_title);
        recyclerView = findViewById(R.id.recycler_view);
        Button backButton = findViewById(R.id.back_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton.setOnClickListener(v -> finish());
    }

    private void loadStageData(String stageName, Tournament tournament) {
        stageTitle.setText(stageName);
        List<Match> stageMatches = tournament.getMatchesByStage(stageName);
        KnockoutMatchesAdapter adapter = new KnockoutMatchesAdapter(stageMatches);
        recyclerView.setAdapter(adapter);
    }
}