package com.example.UCL_Results;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsTableActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private static final String TAG = "ResultsTableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_table);

        try {
            // אתחול רכיבי UI
            initializeViews();

            // אתחול מסד הנתונים
            dbHelper = new DatabaseHelper(this);

            // הצגת טבלת התוצאות
            displayResultsTable();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }
    }

    private void initializeViews() throws Exception {
        try {
            recyclerView = findViewById(R.id.recycler_view);
            if (recyclerView == null) {
                throw new Exception("RecyclerView not found in layout");
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            Button backButton = findViewById(R.id.back_button);
            if (backButton != null) {
                backButton.setOnClickListener(v -> finish());
            } else {
                Log.w(TAG, "Back button not found in layout");
            }

            // אין צורך לחפש את כותרות העמודות כי הם לא קיימות בקובץ XML המתוקן
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    private void displayResultsTable() {
        try {
            // נשתמש במחלקת Tournament כדי להציג את הנתונים
            DatabaseManager dbManager = new DatabaseManager(this, new Tournament());
            Tournament tournament = dbManager.importTournamentFromDatabase();

            // יצירת אדפטר שעובד עם נתוני הטורניר
            ResultsAdapter adapter = new ResultsAdapter(tournament.getTeams());
            recyclerView.setAdapter(adapter);

            Toast.makeText(this, "Loaded " + tournament.getTeams().size() + " teams", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error displaying results table: " + e.getMessage());
            Toast.makeText(this, "Error loading results: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}