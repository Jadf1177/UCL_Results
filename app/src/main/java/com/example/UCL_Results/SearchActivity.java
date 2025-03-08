package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Spinner teamSpinner;
    private RecyclerView recyclerView;
    private TextView noResultsText;
    private DatabaseManager dbManager;
    private Tournament tournament;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize tournament data
        dbManager = new DatabaseManager(this, new Tournament());
        tournament = dbManager.importTournamentFromDatabase();
        if (tournament == null || tournament.getTeams().isEmpty()) {
            // If tournament couldn't be loaded from database, initialize it
            tournament = new Tournament();
            initializeTournament();
        }

        // Initialize UI components
        initializeViews();

        // Setup team spinner
        setupTeamSpinner();
    }

    private void initializeViews() {
        teamSpinner = findViewById(R.id.team_spinner);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noResultsText = findViewById(R.id.no_results_text);
        if (noResultsText != null) {
            noResultsText.setVisibility(View.GONE);
        }

        Button searchButton = findViewById(R.id.search_button);
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> searchMatches());
        }

        Button backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

    private void setupTeamSpinner() {
        // Create a list of all available teams
        List<String> teamNames = new ArrayList<>();

        // Add all teams from the tournament
        teamNames.add("Bayern Munich");
        teamNames.add("Paris Saint-Germain");
        teamNames.add("Real Madrid");
        teamNames.add("Club Brugge");
        teamNames.add("Galatasaray");
        teamNames.add("Tottenham Hotspur");
        teamNames.add("Olympiacos");
        teamNames.add("Red Star Belgrade");
        teamNames.add("Manchester City");
        teamNames.add("Atalanta");
        teamNames.add("Shakhtar Donetsk");
        teamNames.add("Dinamo Zagreb");
        teamNames.add("Juventus");
        teamNames.add("Atletico Madrid");
        teamNames.add("Bayer Leverkusen");
        teamNames.add("Lokomotiv Moscow");
        teamNames.add("Liverpool");
        teamNames.add("Napoli");
        teamNames.add("Red Bull Salzburg");
        teamNames.add("KRC Genk");
        teamNames.add("Barcelona");
        teamNames.add("Borussia Dortmund");
        teamNames.add("Inter Milan");
        teamNames.add("Slavia Prague");
        teamNames.add("RB Leipzig");
        teamNames.add("Lyon");
        teamNames.add("Benfica");
        teamNames.add("Zenit St Petersburg");
        teamNames.add("Chelsea");
        teamNames.add("Ajax");
        teamNames.add("Valencia");
        teamNames.add("Lille");

        Log.d(TAG, "Added " + teamNames.size() + " teams to spinner");

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, teamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(adapter);
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
    private void addGroupMatch(Team home, Team away, int homeScore, int awayScore, String date, String group) {
        tournament.addMatch(new Match(home, away, homeScore, awayScore, date, group));
    }

    private void searchMatches() {
        try {
            if (teamSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "No team selected", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedTeam = teamSpinner.getSelectedItem().toString();
            Log.d(TAG, "Searching matches for team: " + selectedTeam);

            List<Match> teamMatches = new ArrayList<>();

            // Use the tournament that's already fully initialized with all matches
            // instead of creating a new one with just a few matches
            for (Match match : tournament.getAllMatches()) {
                if (match.getHomeTeam().getName().equals(selectedTeam) ||
                        match.getAwayTeam().getName().equals(selectedTeam)) {
                    teamMatches.add(match);
                    Log.d(TAG, "Found match: " + match.getResult() + " - " + match.getDate());
                }
            }

            Log.d(TAG, "Total matches found: " + teamMatches.size());

            // Display results
            if (teamMatches.isEmpty()) {
                if (noResultsText != null) {
                    recyclerView.setVisibility(View.GONE);
                    noResultsText.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, "No matches found for " + selectedTeam, Toast.LENGTH_SHORT).show();
            } else {
                if (noResultsText != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultsText.setVisibility(View.GONE);
                }

                SearchResultsAdapter adapter = new SearchResultsAdapter(teamMatches);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this, "Found " + teamMatches.size() + " matches", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in searchMatches: " + e.getMessage());
            Toast.makeText(this, "Error searching matches: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // This method creates a tournament with all matches inline
    // to avoid relying on the database or external data
    private void initializeTournamentInline(Tournament tournament) {
        // Create teams
        Team[] groupA = {
                new Team("Paris Saint-Germain", "France", "drawable/psg_logo", "A"),
                new Team("Real Madrid", "Spain", "drawable/real_madrid_logo", "A"),
                new Team("Club Brugge", "Belgium", "drawable/club_brugge_logo", "A"),
                new Team("Galatasaray", "Turkey", "drawable/galatasaray_logo", "A")
        };

        Team[] groupB = {
                new Team("Bayern Munich", "Germany", "drawable/bayern_logo", "B"),
                new Team("Tottenham Hotspur", "England", "drawable/tottenham_logo", "B"),
                new Team("Olympiacos", "Greece", "drawable/olympiacos_logo", "B"),
                new Team("Red Star Belgrade", "Serbia", "drawable/red_star_logo", "B")
        };

        // Add more groups as needed...

        // Add all teams to tournament
        for (Team team : groupA) tournament.addTeam(team);
        for (Team team : groupB) tournament.addTeam(team);
        // Add more teams as needed...

        // Add matches
        // Group A
        tournament.addMatch(new Match(groupA[2], groupA[3], 0, 0, "18/09/2019", "Group A")); // Brugge vs Galatasaray
        tournament.addMatch(new Match(groupA[0], groupA[1], 3, 0, "18/09/2019", "Group A")); // PSG vs Real Madrid
        // Add more matches...

        // Group B
        tournament.addMatch(new Match(groupB[2], groupB[1], 2, 2, "18/09/2019", "Group B")); // Olympiacos vs Tottenham
        tournament.addMatch(new Match(groupB[0], groupB[3], 3, 0, "18/09/2019", "Group B")); // Bayern vs Red Star
        tournament.addMatch(new Match(groupB[1], groupB[0], 2, 7, "01/10/2019", "Group B")); // Tottenham vs Bayern
        tournament.addMatch(new Match(groupB[3], groupB[2], 3, 1, "01/10/2019", "Group B")); // Red Star vs Olympiacos
        tournament.addMatch(new Match(groupB[2], groupB[0], 2, 3, "22/10/2019", "Group B")); // Olympiacos vs Bayern
        tournament.addMatch(new Match(groupB[1], groupB[3], 5, 0, "22/10/2019", "Group B")); // Tottenham vs Red Star
        tournament.addMatch(new Match(groupB[0], groupB[2], 2, 0, "06/11/2019", "Group B")); // Bayern vs Olympiacos
        tournament.addMatch(new Match(groupB[3], groupB[1], 0, 4, "06/11/2019", "Group B")); // Red Star vs Tottenham
        tournament.addMatch(new Match(groupB[3], groupB[0], 0, 6, "26/11/2019", "Group B")); // Red Star vs Bayern
        tournament.addMatch(new Match(groupB[1], groupB[2], 4, 2, "26/11/2019", "Group B")); // Tottenham vs Olympiacos
        tournament.addMatch(new Match(groupB[2], groupB[3], 1, 0, "11/12/2019", "Group B")); // Olympiacos vs Red Star
        tournament.addMatch(new Match(groupB[0], groupB[1], 3, 1, "11/12/2019", "Group B")); // Bayern vs Tottenham

        // Add more matches for other groups and knockout rounds as needed...
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.closeDatabase();
        }
    }
}