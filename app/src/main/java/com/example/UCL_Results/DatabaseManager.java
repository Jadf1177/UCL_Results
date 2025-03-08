package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final DatabaseHelper dbHelper;
    private final Tournament tournament;

    public DatabaseManager(Context context, Tournament tournament) {
        this.dbHelper = new DatabaseHelper(context);
        this.tournament = tournament;
    }





    // שיטה לייצוא נתוני טורניר למסד נתונים
    public void exportTournamentToDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Clear existing data first using transactions
        db.beginTransaction();
        try {
            // Delete all teams and matches
            db.delete(DatabaseHelper.TABLE_TEAMS, null, null);
            db.delete(DatabaseHelper.TABLE_MATCHES, null, null);

            // Insert teams
            for (Team team : tournament.getTeams()) {
                ContentValues teamValues = new ContentValues();
                teamValues.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getName());
                teamValues.put(DatabaseHelper.COLUMN_TEAM_GROUP, team.getGroupName());
                teamValues.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, team.getMatchesPlayed());
                teamValues.put(DatabaseHelper.COLUMN_WINS, team.getWins());
                teamValues.put(DatabaseHelper.COLUMN_DRAWS, team.getDraws());
                teamValues.put(DatabaseHelper.COLUMN_LOSSES, team.getLosses());
                teamValues.put(DatabaseHelper.COLUMN_GOALS_FOR, team.getGoalsScored());
                teamValues.put(DatabaseHelper.COLUMN_GOALS_AGAINST, team.getGoalsConceded());
                db.insert(DatabaseHelper.TABLE_TEAMS, null, teamValues);
            }

            // Insert matches
            for (Match match : tournament.getAllMatches()) {
                ContentValues matchValues = new ContentValues();
                matchValues.put(DatabaseHelper.COLUMN_MATCH_DATE, match.getDate());
                matchValues.put(DatabaseHelper.COLUMN_HOME_TEAM, match.getHomeTeam().getName());
                matchValues.put(DatabaseHelper.COLUMN_AWAY_TEAM, match.getAwayTeam().getName());
                matchValues.put(DatabaseHelper.COLUMN_HOME_SCORE, match.getHomeScore());
                matchValues.put(DatabaseHelper.COLUMN_AWAY_SCORE, match.getAwayScore());
                matchValues.put(DatabaseHelper.COLUMN_STAGE, match.getStage());
                db.insert(DatabaseHelper.TABLE_MATCHES, null, matchValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public List<String> getAllTeams() {
        List<String> teams = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TEAMS,
                new String[]{DatabaseHelper.COLUMN_TEAM_NAME},
                null, null, null, null,
                DatabaseHelper.COLUMN_TEAM_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                teams.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teams;
    }

    public List<String> getAllStages() {
        List<String> stages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                true, // distinct
                DatabaseHelper.TABLE_MATCHES,
                new String[]{DatabaseHelper.COLUMN_STAGE},
                null, null, null, null,
                DatabaseHelper.COLUMN_STAGE + " ASC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                stages.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stages;
}


    // שיטה לשחזור נתונים מהמסד נתונים לטורניר חדש
    // In DatabaseManager.java, ensure the importTournamentFromDatabase method loads all matches correctly
    @SuppressLint("Range")
    public Tournament importTournamentFromDatabase() {
        Tournament importedTournament = new Tournament();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Restore teams with full statistics
        Cursor teamCursor = db.query(
                DatabaseHelper.TABLE_TEAMS,
                null, null, null, null, null, null
        );

        while (teamCursor != null && teamCursor.moveToNext()) {
            Team team = new Team(
                    teamCursor.getString(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_NAME)),
                    "", // Country not stored
                    "", // Logo not stored
                    teamCursor.getString(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_GROUP))
            );

            // Set all team statistics from database
            team.setMatchesPlayed(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_MATCHES_PLAYED)));
            team.setWins(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_WINS)));
            team.setDraws(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_DRAWS)));
            team.setLosses(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_LOSSES)));
            team.setGoalsFor(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_FOR)));
            team.setGoalsAgainst(teamCursor.getInt(teamCursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_AGAINST)));

            importedTournament.addTeam(team);
        }
        if (teamCursor != null) {
            teamCursor.close();
        }

        // Restore matches with a detailed log
        Cursor matchCursor = db.query(
                DatabaseHelper.TABLE_MATCHES,
                null, null, null, null, null, null
        );

        int matchCount = 0;
        while (matchCursor != null && matchCursor.moveToNext()) {
            String homeTeamName = matchCursor.getString(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_HOME_TEAM));
            String awayTeamName = matchCursor.getString(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_AWAY_TEAM));

            Team homeTeam = findTeamByName(importedTournament, homeTeamName);
            Team awayTeam = findTeamByName(importedTournament, awayTeamName);

            if (homeTeam != null && awayTeam != null) {
                Match match = new Match(
                        homeTeam,
                        awayTeam,
                        matchCursor.getInt(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_HOME_SCORE)),
                        matchCursor.getInt(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_AWAY_SCORE)),
                        matchCursor.getString(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_DATE)),
                        matchCursor.getString(matchCursor.getColumnIndex(DatabaseHelper.COLUMN_STAGE))
                );

                importedTournament.addMatch(match);
                matchCount++;
            }
        }
        if (matchCursor != null) {
            matchCursor.close();
        }

        // Log how many matches were loaded
        Log.d("DatabaseManager", "Loaded " + matchCount + " matches from database");

        return importedTournament;
    }

    // עזר למציאת קבוצה לפי שם
    private Team findTeamByName(Tournament tournament, String teamName) {
        for (Team team : tournament.getTeams()) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }
    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
    // סגירת מסד הנתונים
    public void closeDatabase() {
        dbHelper.closeDatabase();
    }
    public Cursor getTeamCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                DatabaseHelper.TABLE_TEAMS, // Table name
                null, // Columns (null means all columns)
                null, // Selection (WHERE clause)
                null, // Selection arguments
                null, // Group by
                null, // Having
                null  // Order by
        );
    }
    // Get all available teams
    public List<String> getAvailableTeams() {
        List<String> teams = new ArrayList<>();
        Cursor cursor = dbHelper.getAllTeams();
        if (cursor.moveToFirst()) {
            do {
                teams.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teams;
    }


    // Get all available stages
    public List<String> getAvailableStages() {
        List<String> stages = new ArrayList<>();
        Cursor cursor = dbHelper.getAvailableStages();
        if (cursor.moveToFirst()) {
            do {
                stages.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stages;
}


}
