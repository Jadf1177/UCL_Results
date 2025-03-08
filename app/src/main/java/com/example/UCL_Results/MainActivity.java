package com.example.UCL_Results;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {
    private Tournament tournament;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private DatabaseManager dbManager;
    private FloatingActionButton fabAddMatch;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeDatabase();
        setupTabs();
        updateContent(0);
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAddMatch = findViewById(R.id.fab_add_match);
        searchButton = findViewById(R.id.search_button);

        setupAddMatchButton();
        setupSearchButton();
    }

    private void setupSearchButton() {
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Error opening search: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

            // Initially show the search button (for Matches tab which is default)
            searchButton.setVisibility(View.VISIBLE);
        }
    }
    private Team findTeamByName(String name) {
        for (Team team : tournament.getTeams()) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }
    private void showAddMatchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_match, null);

        // Get references to the dialog views
        Spinner homeTeamSpinner = dialogView.findViewById(R.id.spinner_home_team);
        Spinner awayTeamSpinner = dialogView.findViewById(R.id.spinner_away_team);
        Spinner stageSpinner = dialogView.findViewById(R.id.spinner_stage);
        EditText homeScoreEdit = dialogView.findViewById(R.id.edit_home_score);
        EditText awayScoreEdit = dialogView.findViewById(R.id.edit_away_score);
        EditText dateEdit = dialogView.findViewById(R.id.edit_date);

        // Setup team spinners
        ArrayList<String> teamNames = new ArrayList<>();
        for (Team team : tournament.getTeams()) {
            teamNames.add(team.getName());
        }
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, teamNames);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeTeamSpinner.setAdapter(teamAdapter);
        awayTeamSpinner.setAdapter(teamAdapter);

        // Setup stage spinner
        String[] stages = {"Group A", "Group B", "Group C", "Group D",
                "Group E", "Group F", "Group G", "Group H",
                "Round of 16", "Quarter Final", "Semi Final", "Final"};
        ArrayAdapter<String> stageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stages);
        stageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpinner.setAdapter(stageAdapter);

        builder.setView(dialogView)
                .setTitle("Add New Match")
                .setPositiveButton("Add", null) // Will be set later
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set the positive button click listener
        // In MainActivity.java, in the showAddMatchDialog method, update the "Add" button click handler
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                String homeTeam = homeTeamSpinner.getSelectedItem().toString();
                String awayTeam = awayTeamSpinner.getSelectedItem().toString();
                int homeScore = Integer.parseInt(homeScoreEdit.getText().toString());
                int awayScore = Integer.parseInt(awayScoreEdit.getText().toString());
                String date = dateEdit.getText().toString();
                String stage = stageSpinner.getSelectedItem().toString();

                // Validation
                if (homeTeam.equals(awayTeam)) {
                    Toast.makeText(this, "Home and away teams must be different",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (homeScore < 0 || awayScore < 0) {
                    Toast.makeText(this, "Scores cannot be negative",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add match
                Team home = findTeamByName(homeTeam);
                Team away = findTeamByName(awayTeam);
                if (home != null && away != null) {
                    Match newMatch = new Match(home, away, homeScore, awayScore, date, stage);

                    // Add match to tournament
                    tournament.addMatch(newMatch);

                    // Add directly to database as well
                    dbManager.getDbHelper().addCustomMatch(
                            date,
                            "N/A", // city (not required for persistence)
                            homeTeam,
                            awayTeam,
                            homeScore,
                            awayScore,
                            stage
                    );

                    // Update database through proper export to ensure all changes are saved
                    dbManager.exportTournamentToDatabase();

                    updateContent(tabLayout.getSelectedTabPosition());
                    dialog.dismiss();
                    Toast.makeText(this, "Match added successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid scores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAddMatchButton() {
        if (fabAddMatch != null) {  // Add null check
            fabAddMatch.setOnClickListener(view -> showAddMatchDialog());
        }
    }

    private void initializeDatabase() {
        dbManager = new DatabaseManager(this, new Tournament());

        // Always try to import first
        tournament = dbManager.importTournamentFromDatabase();

        // If import failed (empty database), initialize default
        if(tournament.getTeams().isEmpty() || tournament.getAllMatches().isEmpty()) {
            initializeTournament();
            dbManager.exportTournamentToDatabase();
        }
    }

    private boolean isDatabaseEmpty() {
        // Check if teams table is empty
        Cursor cursor = dbManager.getTeamCursor();
        boolean isEmpty = (cursor == null || cursor.getCount() == 0);
        if (cursor != null) {
            cursor.close(); // Close the cursor to release resources
        }
        return isEmpty;
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Matches").setContentDescription("All Matches"));
        tabLayout.addTab(tabLayout.newTab().setText("Stages").setContentDescription("Tournament Stages"));
        tabLayout.addTab(tabLayout.newTab().setText("Facts"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Players"));
        tabLayout.addTab(tabLayout.newTab().setText("Knockouts"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateContent(tab.getPosition());

                // Show search button only for the Matches tab (position 0)
                if (searchButton != null) {
                    searchButton.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private List<PlayerStats> getTopPlayers() {
        List<PlayerStats> players = new ArrayList<>();

        // Top 5 Scorers
        players.add(new PlayerStats("Robert Lewandowski", "Bayern Munich", 15, 6, 8.5));
        players.add(new PlayerStats("Erling Haaland", "Dortmund", 10, 2, 8.1));
        players.add(new PlayerStats("Serge Gnabry", "Bayern Munich", 9, 5, 8.0));
        players.add(new PlayerStats("Kylian Mbappé", "PSG", 8, 4, 7.9));
        players.add(new PlayerStats("Lionel Messi", "Barcelona", 7, 5, 8.7));

        // Top 5 Assists
        players.add(new PlayerStats("Thomas Müller", "Bayern Munich", 4, 8, 8.2));
        players.add(new PlayerStats("Kevin De Bruyne", "Man City", 3, 7, 8.4));
        players.add(new PlayerStats("Joshua Kimmich", "Bayern Munich", 2, 6, 8.3));
        players.add(new PlayerStats("Ángel Di María", "PSG", 3, 5, 7.8));
        players.add(new PlayerStats("Trent Alexander-Arnold", "Liverpool", 1, 5, 7.7));

        // Top 5 Rated
        players.add(new PlayerStats("Lionel Messi", "Barcelona", 5, 5, 8.7));
        players.add(new PlayerStats("Neymar Jr", "PSG", 7, 6, 8.6));
        players.add(new PlayerStats("Kevin De Bruyne", "Man City", 3, 7, 8.4));
        players.add(new PlayerStats("Robert Lewandowski", "Bayern Munich", 15, 6, 8.5));
        players.add(new PlayerStats("Alphonso Davies", "Bayern Munich", 1, 3, 8.3));

        return players;
    }
    private void updateContent(int position) {
        switch (position) {
            case 0: // Matches
                MatchesAdapter matchesAdapter = new MatchesAdapter(
                        this,  // context
                        tournament.getAllMatches(),
                        match -> {},  // Empty click listener as we handle it in adapter
                        dbManager
                );
                recyclerView.setAdapter(matchesAdapter);
                break;

            case 1: // Stages
                StagesAdapter stagesAdapter = new StagesAdapter(tournament);
                recyclerView.setAdapter(stagesAdapter);
                break;

            case 2: // facts
                List<String> facts = Arrays.asList(getResources().getStringArray(R.array.cl_facts));
                InfoAdapter infoAdapter = new InfoAdapter(facts);
                recyclerView.setAdapter(infoAdapter);
                break;

            case 3: // Top Players
                List<PlayerStats> players = getTopPlayers();
                TopPlayersAdapter adapter = new TopPlayersAdapter(players);
                recyclerView.setAdapter(adapter);
                break;

            case 4: // Knockouts
                KnockoutsAdapter knockoutsAdapter = new KnockoutsAdapter();
                recyclerView.setAdapter(knockoutsAdapter);
                break;
        }
    }

    private void initializeTournament() {
        tournament = new Tournament();

        // יצירת מערך של כל הקבוצות לפי בתים
        Team[][] allGroups = {
                // Group A
                {
                        new Team("Paris Saint-Germain", "France", "drawable/psg_logo", "A"),
                        new Team("Real Madrid", "Spain", "drawable/real_madrid_logo", "A"),
                        new Team("Club Brugge", "Belgium", "drawable/club_brugge_logo", "A"),
                        new Team("Galatasaray", "Turkey", "drawable/galatasaray_logo", "A")
                },
                // Group B
                {
                        new Team("Bayern Munich", "Germany", "drawable/bayern_logo", "B"),
                        new Team("Tottenham Hotspur", "England", "drawable/tottenham_logo", "B"),
                        new Team("Olympiacos", "Greece", "drawable/olympiacos_logo", "B"),
                        new Team("Red Star Belgrade", "Serbia", "drawable/red_star_logo", "B")
                },
                // Group C
                {
                        new Team("Manchester City", "England", "drawable/man_city_logo", "C"),
                        new Team("Atalanta", "Italy", "drawable/atalanta_logo", "C"),
                        new Team("Shakhtar Donetsk", "Ukraine", "drawable/shakhtar_logo", "C"),
                        new Team("Dinamo Zagreb", "Croatia", "drawable/dinamo_zagreb_logo", "C")
                },
                // Group D
                {
                        new Team("Juventus", "Italy", "drawable/juventus_logo", "D"),
                        new Team("Atletico Madrid", "Spain", "drawable/atletico_logo", "D"),
                        new Team("Bayer Leverkusen", "Germany", "drawable/leverkusen_logo", "D"),
                        new Team("Lokomotiv Moscow", "Russia", "drawable/lokomotiv_logo", "D")
                },
                // Group E
                {
                        new Team("Liverpool", "England", "drawable/liverpool_logo", "E"),
                        new Team("Napoli", "Italy", "drawable/napoli_logo", "E"),
                        new Team("Red Bull Salzburg", "Austria", "drawable/salzburg_logo", "E"),
                        new Team("KRC Genk", "Belgium", "drawable/genk_logo", "E")
                },
                // Group F
                {
                        new Team("Barcelona", "Spain", "drawable/barcelona_logo", "F"),
                        new Team("Borussia Dortmund", "Germany", "drawable/dortmund_logo", "F"),
                        new Team("Inter Milan", "Italy", "drawable/inter_logo", "F"),
                        new Team("Slavia Prague", "Czech Republic", "drawable/slavia_prague_logo", "F")
                },
                // Group G
                {
                        new Team("RB Leipzig", "Germany", "drawable/leipzig_logo", "G"),
                        new Team("Lyon", "France", "drawable/lyon_logo", "G"),
                        new Team("Benfica", "Portugal", "drawable/benfica_logo", "G"),
                        new Team("Zenit St Petersburg", "Russia", "drawable/zenit_logo", "G")
                },
                // Group H
                {
                        new Team("Chelsea", "England", "drawable/chelsea_logo", "H"),
                        new Team("Ajax", "Netherlands", "drawable/ajax_logo", "H"),
                        new Team("Valencia", "Spain", "drawable/valencia_logo", "H"),
                        new Team("Lille", "France", "drawable/lille_logo", "H")
                }
        };
        // הוספת כל הקבוצות לטורניר בעזרת לולאה
        for (Team[] group : allGroups) {
            for (Team team : group) {
                tournament.addTeam(team);
            }
        }

        // משחקי שלב הבתים - הלוך וחזור
        // Group A
        // Group A
        String groupA = "Group A";
        addGroupMatch(allGroups[0][2], allGroups[0][3], 0, 0, "18/09/2019", groupA); // Brugge vs Galatasaray
        addGroupMatch(allGroups[0][0], allGroups[0][1], 3, 0, "18/09/2019", groupA); // PSG vs Real Madrid
        addGroupMatch(allGroups[0][1], allGroups[0][2], 2, 2, "01/10/2019", groupA); // Real Madrid vs Brugge
        addGroupMatch(allGroups[0][3], allGroups[0][0], 0, 1, "01/10/2019", groupA); // Galatasaray vs PSG
        addGroupMatch(allGroups[0][2], allGroups[0][0], 0, 5, "22/10/2019", groupA); // Brugge vs PSG
        addGroupMatch(allGroups[0][3], allGroups[0][1], 0, 1, "22/10/2019", groupA); // Galatasaray vs Real Madrid
        addGroupMatch(allGroups[0][0], allGroups[0][2], 1, 0, "06/11/2019", groupA); // PSG vs Brugge
        addGroupMatch(allGroups[0][1], allGroups[0][3], 6, 0, "06/11/2019", groupA); // Real Madrid vs Galatasaray
        addGroupMatch(allGroups[0][1], allGroups[0][0], 2, 2, "26/11/2019", groupA); // Real Madrid vs PSG
        addGroupMatch(allGroups[0][3], allGroups[0][2], 1, 1, "26/11/2019", groupA); // Galatasaray vs Brugge
        addGroupMatch(allGroups[0][0], allGroups[0][3], 5, 0, "11/12/2019", groupA); // PSG vs Galatasaray
        addGroupMatch(allGroups[0][2], allGroups[0][1], 1, 3, "11/12/2019", groupA); // Brugge vs Real Madrid

        // Group B
        String groupB = "Group B";
        addGroupMatch(allGroups[1][2], allGroups[1][1], 2, 2, "18/09/2019", groupB); // Olympiacos vs Tottenham
        addGroupMatch(allGroups[1][0], allGroups[1][3], 3, 0, "18/09/2019", groupB); // Bayern vs Red Star
        addGroupMatch(allGroups[1][1], allGroups[1][0], 2, 7, "01/10/2019", groupB); // Tottenham vs Bayern
        addGroupMatch(allGroups[1][3], allGroups[1][2], 3, 1, "01/10/2019", groupB); // Red Star vs Olympiacos
        addGroupMatch(allGroups[1][2], allGroups[1][0], 2, 3, "22/10/2019", groupB); // Olympiacos vs Bayern
        addGroupMatch(allGroups[1][1], allGroups[1][3], 5, 0, "22/10/2019", groupB); // Tottenham vs Red Star
        addGroupMatch(allGroups[1][0], allGroups[1][2], 2, 0, "06/11/2019", groupB); // Bayern vs Olympiacos
        addGroupMatch(allGroups[1][3], allGroups[1][1], 0, 4, "06/11/2019", groupB); // Red Star vs Tottenham
        addGroupMatch(allGroups[1][3], allGroups[1][0], 0, 6, "26/11/2019", groupB); // Red Star vs Bayern
        addGroupMatch(allGroups[1][1], allGroups[1][2], 4, 2, "26/11/2019", groupB); // Tottenham vs Olympiacos
        addGroupMatch(allGroups[1][2], allGroups[1][3], 1, 0, "11/12/2019", groupB); // Olympiacos vs Red Star
        addGroupMatch(allGroups[1][0], allGroups[1][1], 3, 1, "11/12/2019", groupB); // Bayern vs Tottenham

        // Group C
        String groupC = "Group C";
        addGroupMatch(allGroups[2][3], allGroups[2][1], 4, 0, "18/09/2019", groupC); // Zagreb vs Atalanta
        addGroupMatch(allGroups[2][0], allGroups[2][2], 3, 0, "18/09/2019", groupC); // Man City vs Shakhtar
        addGroupMatch(allGroups[2][1], allGroups[2][2], 1, 2, "01/10/2019", groupC); // Atalanta vs Shakhtar
        addGroupMatch(allGroups[2][0], allGroups[2][3], 2, 0, "01/10/2019", groupC); // Man City vs Zagreb
        addGroupMatch(allGroups[2][0], allGroups[2][1], 5, 1, "22/10/2019", groupC); // Man City vs Atalanta
        addGroupMatch(allGroups[2][2], allGroups[2][3], 2, 2, "22/10/2019", groupC); // Shakhtar vs Zagreb
        addGroupMatch(allGroups[2][3], allGroups[2][2], 3, 3, "06/11/2019", groupC); // Zagreb vs Shakhtar
        addGroupMatch(allGroups[2][1], allGroups[2][0], 1, 1, "06/11/2019", groupC); // Atalanta vs Man City
        addGroupMatch(allGroups[2][0], allGroups[2][2], 1, 1, "26/11/2019", groupC); // Man City vs Shakhtar
        addGroupMatch(allGroups[2][1], allGroups[2][3], 2, 0, "26/11/2019", groupC); // Atalanta vs Zagreb
        addGroupMatch(allGroups[2][2], allGroups[2][0], 1, 4, "11/12/2019", groupC); // Zagreb vs Man City
        addGroupMatch(allGroups[2][2], allGroups[2][1], 0, 3, "11/12/2019", groupC); // Shakhtar vs Atalanta

        // Group D
        String groupD = "Group D";
        addGroupMatch(allGroups[3][2], allGroups[3][3], 1, 2, "18/09/2019", groupD); // Leverkusen vs Lokomotiv
        addGroupMatch(allGroups[3][0], allGroups[3][1], 2, 2, "18/09/2019", groupD); // Juventus vs Atletico
        addGroupMatch(allGroups[3][0], allGroups[3][2], 3, 0, "01/10/2019", groupD); // Juventus vs Leverkusen
        addGroupMatch(allGroups[3][3], allGroups[3][1], 0, 2, "01/10/2019", groupD); // Lokomotiv vs Atletico
        addGroupMatch(allGroups[3][1], allGroups[3][2], 1, 0, "22/10/2019", groupD); // Atletico vs Leverkusen
        addGroupMatch(allGroups[3][3], allGroups[3][0], 1, 2, "22/10/2019", groupD); // Lokomotiv vs Juventus
        addGroupMatch(allGroups[3][2], allGroups[3][1], 2, 1, "06/11/2019", groupD); // Leverkusen vs Atletico
        addGroupMatch(allGroups[3][0], allGroups[3][3], 2, 1, "06/11/2019", groupD); // Juventus vs Lokomotiv
        addGroupMatch(allGroups[3][3], allGroups[3][2], 0, 2, "26/11/2019", groupD); // Lokomotiv vs Leverkusen
        addGroupMatch(allGroups[3][1], allGroups[3][0], 1, 0, "26/11/2019", groupD); // Atletico vs Juventus
        addGroupMatch(allGroups[3][2], allGroups[3][0], 0, 2, "11/12/2019", groupD); // Leverkusen vs Juventus
        addGroupMatch(allGroups[3][1], allGroups[3][3], 2, 0, "11/12/2019", groupD); // Atletico vs Lokomotiv

        // Group E
        String groupE = "Group E";
        addGroupMatch(allGroups[4][2], allGroups[4][3], 6, 2, "17/09/2019", groupE); // Salzburg vs Genk
        addGroupMatch(allGroups[4][0], allGroups[4][1], 0, 2, "17/09/2019", groupE); // Liverpool vs Napoli
        addGroupMatch(allGroups[4][3], allGroups[4][1], 0, 0, "02/10/2019", groupE); // Genk vs Napoli
        addGroupMatch(allGroups[4][0], allGroups[4][2], 4, 3, "02/10/2019", groupE); // Liverpool vs Salzburg
        addGroupMatch(allGroups[4][2], allGroups[4][1], 2, 3, "23/10/2019", groupE); // Salzburg vs Napoli
        addGroupMatch(allGroups[4][0], allGroups[4][3], 4, 1, "23/10/2019", groupE); // Liverpool vs Genk
        addGroupMatch(allGroups[4][0], allGroups[4][3], 2, 1, "05/11/2019", groupE); // Liverpool vs Genk
        addGroupMatch(allGroups[4][2], allGroups[4][1], 1, 1, "05/11/2019", groupE); // Salzburg vs Napoli
        addGroupMatch(allGroups[4][1], allGroups[4][0], 1, 1, "27/11/2019", groupE); // Napoli vs Liverpool
        addGroupMatch(allGroups[4][3], allGroups[4][2], 1, 4, "27/11/2019", groupE); // Genk vs Salzburg
        addGroupMatch(allGroups[4][0], allGroups[4][2], 2, 0, "10/12/2019", groupE); // Liverpool vs Salzburg
        addGroupMatch(allGroups[4][3], allGroups[4][1], 0, 4, "10/12/2019", groupE); // Genk vs Napoli

        // Group F
        String groupF = "Group F";
        addGroupMatch(allGroups[5][2], allGroups[5][3], 1, 1, "17/09/2019", groupF); // Inter vs Slavia
        addGroupMatch(allGroups[5][0], allGroups[5][1], 0, 0, "17/09/2019", groupF); // Barcelona vs Dortmund
        addGroupMatch(allGroups[5][0], allGroups[5][2], 2, 1, "02/10/2019", groupF); // Barcelona vs Inter
        addGroupMatch(allGroups[5][3], allGroups[5][1], 0, 2, "02/10/2019", groupF); // Slavia vs Dortmund
        addGroupMatch(allGroups[5][2], allGroups[5][0], 1, 2, "23/10/2019", groupF); // Slavia vs Barcelona
        addGroupMatch(allGroups[5][2], allGroups[5][1], 2, 0, "23/10/2019", groupF); // Inter vs Dortmund
        addGroupMatch(allGroups[5][0], allGroups[5][3], 0, 0, "05/11/2019", groupF); // Barcelona vs Slavia
        addGroupMatch(allGroups[5][1], allGroups[5][2], 3, 2, "05/11/2019", groupF); // Dortmund vs Inter
        addGroupMatch(allGroups[5][1], allGroups[5][0], 1, 3, "27/11/2019", groupF); // Dortmund vs Barcelona
        addGroupMatch(allGroups[5][3], allGroups[5][2], 1, 3, "27/11/2019", groupF); // Slavia vs Inter
        addGroupMatch(allGroups[5][0], allGroups[5][2], 2, 1, "10/12/2019", groupF); // Barcelona vs Inter
        addGroupMatch(allGroups[5][3], allGroups[5][1], 1, 2, "10/12/2019", groupF); // Slavia vs Dortmund

        // Group G
        String groupG = "Group G";
        addGroupMatch(allGroups[6][1], allGroups[6][3], 1, 1, "17/09/2019", groupG); // Lyon vs Zenit
        addGroupMatch(allGroups[6][0], allGroups[6][2], 2, 1, "17/09/2019", groupG); // Leipzig vs Benfica
        addGroupMatch(allGroups[6][0], allGroups[6][1], 0, 2, "02/10/2019", groupG); // Leipzig vs Lyon
        addGroupMatch(allGroups[6][3], allGroups[6][2], 3, 1, "02/10/2019", groupG); // Zenit vs Benfica
        addGroupMatch(allGroups[6][0], allGroups[6][3], 2, 1, "23/10/2019", groupG); // Leipzig vs Zenit
        addGroupMatch(allGroups[6][1], allGroups[6][2], 1, 2, "23/10/2019", groupG); // Lyon vs Benfica
        addGroupMatch(allGroups[6][3], allGroups[6][0], 0, 2, "05/11/2019", groupG); // Zenit vs Leipzig
        addGroupMatch(allGroups[6][2], allGroups[6][1], 1, 3, "05/11/2019", groupG); // Benfica vs Lyon
        addGroupMatch(allGroups[6][3], allGroups[6][1], 2, 0, "27/11/2019", groupG); // Zenit vs Lyon
        addGroupMatch(allGroups[6][2], allGroups[6][0], 2, 2, "27/11/2019", groupG); // Benfica vs Leipzig
        addGroupMatch(allGroups[6][2], allGroups[6][3], 3, 0, "10/12/2019", groupG); // Benfica vs Zenit
        addGroupMatch(allGroups[6][1], allGroups[6][0], 2, 2, "10/12/2019", groupG); // Lyon vs Leipzig

        // Group H
        String groupH = "Group H";
        addGroupMatch(allGroups[7][1], allGroups[7][3], 3, 0, "17/09/2019", groupH); // Ajax vs Lille
        addGroupMatch(allGroups[7][0], allGroups[7][2], 0, 1, "17/09/2019", groupH); // Chelsea vs Valencia
        addGroupMatch(allGroups[7][1], allGroups[7][2], 3, 0, "02/10/2019", groupH); // Ajax vs Valencia
        addGroupMatch(allGroups[7][3], allGroups[7][0], 1, 2, "02/10/2019", groupH); // Lille vs Chelsea
        addGroupMatch(allGroups[7][1], allGroups[7][0], 0, 1, "23/10/2019", groupH); // Ajax vs Chelsea
        addGroupMatch(allGroups[7][3], allGroups[7][2], 1, 4, "23/10/2019", groupH); // Lille vs Valencia
        addGroupMatch(allGroups[7][2], allGroups[7][3], 4, 4, "05/11/2019", groupH); // Valencia vs Lille
        addGroupMatch(allGroups[7][0], allGroups[7][1], 2, 1, "05/11/2019", groupH); // Chelsea vs Ajax
        addGroupMatch(allGroups[7][2], allGroups[7][0], 2, 2, "27/11/2019", groupH); // Valencia vs Chelsea
        addGroupMatch(allGroups[7][3], allGroups[7][1], 0, 2, "27/11/2019", groupH); // Lille vs Ajax
        addGroupMatch(allGroups[7][2], allGroups[7][1], 1, 0, "10/12/2019", groupH); // Valencia vs Ajax
        addGroupMatch(allGroups[7][0], allGroups[7][3], 2, 1, "10/12/2019", groupH); // Chelsea vs Lille

        // Round of 16 - First Leg
        tournament.addMatch(new Match(allGroups[5][1], allGroups[0][0], 2, 1, "18/02/2020", "Round of 16")); // Dortmund vs PSG
        tournament.addMatch(new Match(allGroups[3][1], allGroups[4][0], 1, 0, "18/02/2020", "Round of 16")); // Atletico vs Liverpool
        tournament.addMatch(new Match(allGroups[2][1], allGroups[7][2], 4, 1, "19/02/2020", "Round of 16")); // Atalanta vs Valencia
        tournament.addMatch(new Match(allGroups[7][0], allGroups[6][0], 0, 3, "25/02/2020", "Round of 16")); // Tottenham vs Leipzig
        tournament.addMatch(new Match(allGroups[4][1], allGroups[5][0], 1, 1, "25/02/2020", "Round of 16")); // Napoli vs Barcelona
        tournament.addMatch(new Match(allGroups[7][0], allGroups[1][0], 0, 3, "25/02/2020", "Round of 16")); // Chelsea vs Bayern
        tournament.addMatch(new Match(allGroups[6][1], allGroups[3][0], 1, 0, "26/02/2020", "Round of 16")); // Lyon vs Juventus
        tournament.addMatch(new Match(allGroups[5][2], allGroups[2][0], 1, 2, "26/02/2020", "Round of 16")); // Real Madrid vs Man City

        // Round of 16 - Second Leg
        tournament.addMatch(new Match(allGroups[0][0], allGroups[5][1], 2, 0, "11/03/2020", "Round of 16")); // PSG vs Dortmund
        tournament.addMatch(new Match(allGroups[4][0], allGroups[3][1], 2, 3, "11/03/2020", "Round of 16")); // Liverpool vs Atletico
        tournament.addMatch(new Match(allGroups[7][2], allGroups[2][1], 3, 4, "10/03/2020", "Round of 16")); // Valencia vs Atalanta
        tournament.addMatch(new Match(allGroups[6][0], allGroups[7][0], 3, 0, "10/03/2020", "Round of 16")); // Leipzig vs Tottenham
        tournament.addMatch(new Match(allGroups[5][0], allGroups[4][1], 3, 1, "08/08/2020", "Round of 16")); // Barcelona vs Napoli
        tournament.addMatch(new Match(allGroups[1][0], allGroups[7][0], 4, 1, "08/08/2020", "Round of 16")); // Bayern vs Chelsea
        tournament.addMatch(new Match(allGroups[3][0], allGroups[6][1], 2, 1, "07/08/2020", "Round of 16")); // Juventus vs Lyon
        tournament.addMatch(new Match(allGroups[2][0], allGroups[5][2], 2, 1, "07/08/2020", "Round of 16")); // Man City vs Real Madrid

        // Quarter Finals
        tournament.addMatch(new Match(allGroups[2][1], allGroups[0][0], 1, 2, "12/08/2020", "Quarter Final")); // Atalanta vs PSG
        tournament.addMatch(new Match(allGroups[6][0], allGroups[3][1], 2, 1, "13/08/2020", "Quarter Final")); // Leipzig vs Atletico
        tournament.addMatch(new Match(allGroups[5][0], allGroups[1][0], 2, 8, "14/08/2020", "Quarter Final")); // Barcelona vs Bayern
        tournament.addMatch(new Match(allGroups[2][0], allGroups[6][1], 1, 3, "15/08/2020", "Quarter Final")); // Man City vs Lyon

        // Semi Finals
        tournament.addMatch(new Match(allGroups[6][0], allGroups[0][0], 0, 3, "18/08/2020", "Semi Final")); // Leipzig vs PSG
        tournament.addMatch(new Match(allGroups[6][1], allGroups[1][0], 0, 3, "19/08/2020", "Semi Final")); // Lyon vs Bayern

        // Final - Estádio da Luz, Lisbon
        tournament.addMatch(new Match(
                allGroups[1][0],  // Bayern Munich
                allGroups[0][0],  // Paris Saint-Germain
                1,                // Final Score: Bayern Munich 1
                0,                // Final Score: PSG 0
                "23/08/2020",    // Date
                "Final"          // Stage
        ));
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Save when the app goes to background
        if (dbManager != null) {
            dbManager.exportTournamentToDatabase();
        }
    }

    // Similarly in onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Save all changes when the app is closing
        if (dbManager != null) {
            dbManager.exportTournamentToDatabase();
            dbManager.closeDatabase();
        }
    }

    // מתודת עזר להוספת משחק
    private void addGroupMatch(Team home, Team away, int homeScore, int awayScore, String date, String group) {
        tournament.addMatch(new Match(home, away, homeScore, awayScore, date, group));
    }

}