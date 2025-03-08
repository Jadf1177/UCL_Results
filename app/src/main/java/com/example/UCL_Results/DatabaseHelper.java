package com.example.UCL_Results;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // קבועים עבור פרטי מסד הנתונים
    private static final String DATABASE_NAME = "UCLTournament.db";
    private static final int DATABASE_VERSION = 1;

    // טבלת משחקים
    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_MATCH_DATE = "date";
    public static final String COLUMN_MATCH_CITY = "city";
    public static final String COLUMN_HOME_TEAM = "home_team";
    public static final String COLUMN_AWAY_TEAM = "away_team";
    public static final String COLUMN_HOME_SCORE = "home_score";
    public static final String COLUMN_AWAY_SCORE = "away_score";
    public static final String COLUMN_STAGE = "stage";

    // טבלת קבוצות
    public static final String TABLE_TEAMS = "teams";
    public static final String COLUMN_TEAM_NAME = "name";
    public static final String COLUMN_TEAM_GROUP = "team_group";
    public static final String COLUMN_MATCHES_PLAYED = "matches_played";
    public static final String COLUMN_WINS = "wins";
    public static final String COLUMN_DRAWS = "draws";
    public static final String COLUMN_LOSSES = "losses";
    public static final String COLUMN_GOALS_FOR = "goals_for";
    public static final String COLUMN_GOALS_AGAINST = "goals_against";

    // טבלת תוצאות משחקים כדורגל
    public static final String TABLE_FOOTBALL_RESULTS = "football_results";
    public static final String COLUMN_RESULT_DATE = "date";
    public static final String COLUMN_RESULT_CITY = "city";
    public static final String COLUMN_RESULT_TEAM_A = "team_a";
    public static final String COLUMN_RESULT_TEAM_B = "team_b";
    public static final String COLUMN_RESULT_TEAM_A_GOALS = "team_a_goals";
    public static final String COLUMN_RESULT_TEAM_B_GOALS = "team_b_goals";

    // טבלת שערים
    public static final String TABLE_GOALS = "goals";
    public static final String COLUMN_GOAL_TEAM = "team";
    public static final String COLUMN_GOALS_SCORED = "goals_scored";
    public static final String COLUMN_GOALS_CONCEDED = "goals_conceded";
    public static final String COLUMN_TEAM_WINS = "wins";
    public static final String COLUMN_TEAM_DRAWS = "draws";
    public static final String COLUMN_TEAM_LOSSES = "losses";
    public static final String COLUMN_TEAM_POINTS = "points";
    public static final String COLUMN_TEAM_SCORED = "goals";

    // יצירת טבלאות
    private static final String CREATE_TABLE_MATCHES =
            "CREATE TABLE " + TABLE_MATCHES + "(" +
                    COLUMN_MATCH_DATE + " TEXT, " +
                    COLUMN_MATCH_CITY + " TEXT, " +
                    COLUMN_HOME_TEAM + " TEXT, " +
                    COLUMN_AWAY_TEAM + " TEXT, " +
                    COLUMN_HOME_SCORE + " INTEGER, " +
                    COLUMN_AWAY_SCORE + " INTEGER, " +
                    COLUMN_STAGE + " TEXT)";

    private static final String CREATE_TABLE_TEAMS =
            "CREATE TABLE " + TABLE_TEAMS + "(" +
                    COLUMN_TEAM_NAME + " TEXT PRIMARY KEY, " +
                    COLUMN_TEAM_GROUP + " TEXT, " +
                    COLUMN_MATCHES_PLAYED + " INTEGER, " +
                    COLUMN_WINS + " INTEGER, " +
                    COLUMN_DRAWS + " INTEGER, " +
                    COLUMN_LOSSES + " INTEGER, " +
                    COLUMN_GOALS_FOR + " INTEGER, " +
                    COLUMN_GOALS_AGAINST + " INTEGER)";

    private static final String CREATE_TABLE_FOOTBALL_RESULTS =
            "CREATE TABLE " + TABLE_FOOTBALL_RESULTS + "(" +
                    COLUMN_RESULT_DATE + " DATE(10), " +
                    COLUMN_RESULT_CITY + " CHAR(20), " +
                    COLUMN_RESULT_TEAM_A + " CHAR(20), " +
                    COLUMN_RESULT_TEAM_B + " CHAR(20), " +
                    COLUMN_RESULT_TEAM_A_GOALS + " INTEGER, " +
                    COLUMN_RESULT_TEAM_B_GOALS + " INTEGER)";

    private static final String CREATE_TABLE_GOALS =
            "CREATE TABLE " + TABLE_GOALS + "(" +
                    COLUMN_GOAL_TEAM + " CHAR(20), " +
                    COLUMN_GOALS_SCORED + " INTEGER, " +
                    COLUMN_GOALS_CONCEDED + " INTEGER, " +
                    COLUMN_TEAM_WINS + " INTEGER, " +
                    COLUMN_TEAM_DRAWS + " INTEGER, " +
                    COLUMN_TEAM_LOSSES + " INTEGER, " +
                    COLUMN_TEAM_POINTS + " INTEGER, " +
                    COLUMN_TEAM_SCORED + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MATCHES);
        db.execSQL(CREATE_TABLE_TEAMS);
        db.execSQL(CREATE_TABLE_FOOTBALL_RESULTS);
        db.execSQL(CREATE_TABLE_GOALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOTBALL_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    // שיטה להוספת משחק
    public long addMatch(String date, String city, String homeTeam,
                         String awayTeam, int homeScore, int awayScore, String stage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH_DATE, date);
        values.put(COLUMN_MATCH_CITY, city);
        values.put(COLUMN_HOME_TEAM, homeTeam);
        values.put(COLUMN_AWAY_TEAM, awayTeam);
        values.put(COLUMN_HOME_SCORE, homeScore);
        values.put(COLUMN_AWAY_SCORE, awayScore);
        values.put(COLUMN_STAGE, stage);

        return db.insert(TABLE_MATCHES, null, values);
    }

    // שיטה להוספת תוצאת משחק כדורגל
    public long addFootballResult(String date, String city, String teamA,
                                  String teamB, int teamAGoals, int teamBGoals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT_DATE, date);
        values.put(COLUMN_RESULT_CITY, city);
        values.put(COLUMN_RESULT_TEAM_A, teamA);
        values.put(COLUMN_RESULT_TEAM_B, teamB);
        values.put(COLUMN_RESULT_TEAM_A_GOALS, teamAGoals);
        values.put(COLUMN_RESULT_TEAM_B_GOALS, teamBGoals);

        // עדכון טבלת השערים והנקודות
        updateGoalsTable(teamA, teamB, teamAGoals, teamBGoals);

        return db.insert(TABLE_FOOTBALL_RESULTS, null, values);
    }

    // שיטה לעדכון טבלת השערים והנקודות
    private void updateGoalsTable(String teamA, String teamB, int teamAGoals, int teamBGoals) {
        // עדכון נתוני קבוצה א'
        updateTeamGoalsAndPoints(teamA, teamAGoals, teamBGoals);
        // עדכון נתוני קבוצה ב'
        updateTeamGoalsAndPoints(teamB, teamBGoals, teamAGoals);
    }

    // עדכון נתוני קבוצה בודדת
    private void updateTeamGoalsAndPoints(String team, int goalsScored, int goalsConceded) {
        SQLiteDatabase db = this.getWritableDatabase();

        // בדיקה האם הקבוצה כבר קיימת בטבלה
        Cursor cursor = db.query(
                TABLE_GOALS,
                new String[]{COLUMN_GOAL_TEAM, COLUMN_GOALS_SCORED, COLUMN_GOALS_CONCEDED,
                        COLUMN_TEAM_WINS, COLUMN_TEAM_DRAWS, COLUMN_TEAM_LOSSES, COLUMN_TEAM_POINTS},
                COLUMN_GOAL_TEAM + " = ?",
                new String[]{team},
                null, null, null
        );

        ContentValues values = new ContentValues();

        // חישוב תוצאת המשחק (ניצחון, תיקו או הפסד)
        int wins = 0, draws = 0, losses = 0, points = 0;
        if (goalsScored > goalsConceded) {
            wins = 1;
            points = 3;
        } else if (goalsScored == goalsConceded) {
            draws = 1;
            points = 1;
        } else {
            losses = 1;
            points = 0;
        }

        if (cursor != null && cursor.moveToFirst()) {
            // הקבוצה קיימת - עדכון נתונים
            @SuppressLint("Range") int currentGoalsScored = cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_SCORED));
            @SuppressLint("Range") int currentGoalsConceded = cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_CONCEDED));
            @SuppressLint("Range") int currentWins = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_WINS));
            @SuppressLint("Range") int currentDraws = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_DRAWS));
            @SuppressLint("Range") int currentLosses = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_LOSSES));
            @SuppressLint("Range") int currentPoints = cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_POINTS));

            values.put(COLUMN_GOALS_SCORED, currentGoalsScored + goalsScored);
            values.put(COLUMN_GOALS_CONCEDED, currentGoalsConceded + goalsConceded);
            values.put(COLUMN_TEAM_WINS, currentWins + wins);
            values.put(COLUMN_TEAM_DRAWS, currentDraws + draws);
            values.put(COLUMN_TEAM_LOSSES, currentLosses + losses);
            values.put(COLUMN_TEAM_POINTS, currentPoints + points);

            db.update(
                    TABLE_GOALS,
                    values,
                    COLUMN_GOAL_TEAM + " = ?",
                    new String[]{team}
            );
        } else {
            // הקבוצה לא קיימת - יצירת רשומה חדשה
            values.put(COLUMN_GOAL_TEAM, team);
            values.put(COLUMN_GOALS_SCORED, goalsScored);
            values.put(COLUMN_GOALS_CONCEDED, goalsConceded);
            values.put(COLUMN_TEAM_WINS, wins);
            values.put(COLUMN_TEAM_DRAWS, draws);
            values.put(COLUMN_TEAM_LOSSES, losses);
            values.put(COLUMN_TEAM_POINTS, points);

            db.insert(TABLE_GOALS, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void clearAllTeams() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TEAMS, null, null);
        db.close();
    }

    public void clearAllMatches() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MATCHES, null, null);
        db.close();
    }

    public void clearAllFootballResults() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FOOTBALL_RESULTS, null, null);
        db.close();
    }

    public void clearAllGoals() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_GOALS, null, null);
        db.close();
    }

    // שיטה להוספת קבוצה
    public void addTeam(String name, String group, int matchesPlayed,
                        int wins, int draws, int losses,
                        int goalsFor, int goalsAgainst) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAM_NAME, name);
        values.put(COLUMN_TEAM_GROUP, group);
        values.put(COLUMN_MATCHES_PLAYED, matchesPlayed);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_DRAWS, draws);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_GOALS_FOR, goalsFor);
        values.put(COLUMN_GOALS_AGAINST, goalsAgainst);

        db.insert(TABLE_TEAMS, null, values);
    }

    // שיטות חיפוש

    // חיפוש משחקים של קבוצה מסוימת
    public Cursor getMatchesByTeam(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MATCHES,
                null,
                COLUMN_HOME_TEAM + " = ? OR " + COLUMN_AWAY_TEAM + " = ?",
                new String[]{teamName, teamName},
                null, null, null);
    }

    // חיפוש תוצאות משחקים לפי קבוצה
    public Cursor getFootballResultsByTeam(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FOOTBALL_RESULTS,
                null,
                COLUMN_RESULT_TEAM_A + " = ? OR " + COLUMN_RESULT_TEAM_B + " = ?",
                new String[]{teamName, teamName},
                null, null, null);
    }

    // חיפוש משחקים לפי שלב
    public Cursor getMatchesByStage(String stage) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MATCHES,
                null,
                COLUMN_STAGE + " = ?",
                new String[]{stage},
                null, null, null);
    }

    // חיפוש פרטי קבוצה
    public Cursor getTeamDetails(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TEAMS,
                null,
                COLUMN_TEAM_NAME + " = ?",
                new String[]{teamName},
                null, null, null);
    }

    // קבלת טבלת תוצאות מלאה
    public Cursor getFullResultsTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_GOALS,
                null,
                null, null, null, null,
                COLUMN_TEAM_POINTS + " DESC, " + COLUMN_GOALS_SCORED + " DESC");
    }

// שיטות עדכון

    // עדכון תוצאת משחק
    public int updateMatchResult(String date, String homeTeam, String awayTeam,
                                 int homeScore, int awayScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOME_SCORE, homeScore);
        values.put(COLUMN_AWAY_SCORE, awayScore);

        String selection = COLUMN_MATCH_DATE + " = ? AND " +
                COLUMN_HOME_TEAM + " = ? AND " +
                COLUMN_AWAY_TEAM + " = ?";
        String[] selectionArgs = {date, homeTeam, awayTeam};

        return db.update(TABLE_MATCHES, values, selection, selectionArgs);
    }

    // עדכון תוצאת משחק כדורגל
    public int updateFootballResult(String date, String teamA, String teamB,
                                    int teamAGoals, int teamBGoals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT_TEAM_A_GOALS, teamAGoals);
        values.put(COLUMN_RESULT_TEAM_B_GOALS, teamBGoals);

        String selection = COLUMN_RESULT_DATE + " = ? AND " +
                COLUMN_RESULT_TEAM_A + " = ? AND " +
                COLUMN_RESULT_TEAM_B + " = ?";
        String[] selectionArgs = {date, teamA, teamB};

        // עדכון טבלת נתוני השערים והנקודות
        // ראשית נקבל את הערכים הקיימים
        Cursor cursor = db.query(
                TABLE_FOOTBALL_RESULTS,
                new String[]{COLUMN_RESULT_TEAM_A_GOALS, COLUMN_RESULT_TEAM_B_GOALS},
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int oldTeamAGoals = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_TEAM_A_GOALS));
            @SuppressLint("Range") int oldTeamBGoals = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_TEAM_B_GOALS));

            // נבטל את ההשפעה של התוצאה הקודמת
            updateTeamGoalsAndPoints(teamA, -oldTeamAGoals, -oldTeamBGoals);
            updateTeamGoalsAndPoints(teamB, -oldTeamBGoals, -oldTeamAGoals);

            // נוסיף את ההשפעה של התוצאה החדשה
            updateTeamGoalsAndPoints(teamA, teamAGoals, teamBGoals);
            updateTeamGoalsAndPoints(teamB, teamBGoals, teamAGoals);

            cursor.close();
        }

        return db.update(TABLE_FOOTBALL_RESULTS, values, selection, selectionArgs);
    }


    // עדכון סטטיסטיקות קבוצה
    public int updateTeamStats(String teamName, int matchesPlayed,
                               int wins, int draws, int losses,
                               int goalsFor, int goalsAgainst) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCHES_PLAYED, matchesPlayed);
        values.put(COLUMN_WINS, wins);
        values.put(COLUMN_DRAWS, draws);
        values.put(COLUMN_LOSSES, losses);
        values.put(COLUMN_GOALS_FOR, goalsFor);
        values.put(COLUMN_GOALS_AGAINST, goalsAgainst);

        return db.update(TABLE_TEAMS,
                values,
                COLUMN_TEAM_NAME + " = ?",
                new String[]{teamName});
    }

// שיטות מחיקה

    // מחיקת משחק
    public int deleteMatch(String date, String homeTeam, String awayTeam) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_MATCH_DATE + " = ? AND " +
                COLUMN_HOME_TEAM + " = ? AND " +
                COLUMN_AWAY_TEAM + " = ?";
        String[] selectionArgs = {date, homeTeam, awayTeam};

        return db.delete(TABLE_MATCHES, selection, selectionArgs);
    }

    // מחיקת תוצאת משחק כדורגל
    public int deleteFootballResult(String date, String teamA, String teamB) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_RESULT_DATE + " = ? AND " +
                COLUMN_RESULT_TEAM_A + " = ? AND " +
                COLUMN_RESULT_TEAM_B + " = ?";
        String[] selectionArgs = {date, teamA, teamB};

        // לפני המחיקה, נקבל את הערכים כדי לעדכן את טבלת השערים והנקודות
        Cursor cursor = db.query(
                TABLE_FOOTBALL_RESULTS,
                new String[]{COLUMN_RESULT_TEAM_A_GOALS, COLUMN_RESULT_TEAM_B_GOALS},
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int teamAGoals = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_TEAM_A_GOALS));
            @SuppressLint("Range") int teamBGoals = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_TEAM_B_GOALS));

            // נבטל את ההשפעה של התוצאה על טבלת השערים והנקודות
            updateTeamGoalsAndPoints(teamA, -teamAGoals, -teamBGoals);
            updateTeamGoalsAndPoints(teamB, -teamBGoals, -teamAGoals);

            cursor.close();
        }

        return db.delete(TABLE_FOOTBALL_RESULTS, selection, selectionArgs);
    }

    // מחיקת קבוצה
    public int deleteTeam(String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TEAMS,
                COLUMN_TEAM_NAME + " = ?",
                new String[]{teamName});
    }

    // סגירת החיבור למסד הנתונים
    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    public long addCustomMatch(String date, String city, String homeTeam,
                               String awayTeam, int homeScore, int awayScore, String stage) {
        // Check if match already exists
        if (matchExists(date, homeTeam, awayTeam)) {
            return -1; // Match already exists
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH_DATE, date);
        values.put(COLUMN_MATCH_CITY, city);
        values.put(COLUMN_HOME_TEAM, homeTeam);
        values.put(COLUMN_AWAY_TEAM, awayTeam);
        values.put(COLUMN_HOME_SCORE, homeScore);
        values.put(COLUMN_AWAY_SCORE, awayScore);
        values.put(COLUMN_STAGE, stage);

        return db.insert(TABLE_MATCHES, null, values);
    }

    // Check if match exists
    private boolean matchExists(String date, String homeTeam, String awayTeam) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MATCHES,
                new String[]{COLUMN_MATCH_DATE},
                COLUMN_MATCH_DATE + " = ? AND " +
                        COLUMN_HOME_TEAM + " = ? AND " +
                        COLUMN_AWAY_TEAM + " = ?",
                new String[]{date, homeTeam, awayTeam},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Edit existing match
    public int editCustomMatch(String originalDate, String originalHomeTeam, String originalAwayTeam,
                               String newDate, String newCity, String newHomeTeam,
                               String newAwayTeam, int newHomeScore, int newAwayScore, String newStage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH_DATE, newDate);
        values.put(COLUMN_MATCH_CITY, newCity);
        values.put(COLUMN_HOME_TEAM, newHomeTeam);
        values.put(COLUMN_AWAY_TEAM, newAwayTeam);
        values.put(COLUMN_HOME_SCORE, newHomeScore);
        values.put(COLUMN_AWAY_SCORE, newAwayScore);
        values.put(COLUMN_STAGE, newStage);

        return db.update(TABLE_MATCHES,
                values,
                COLUMN_MATCH_DATE + " = ? AND " +
                        COLUMN_HOME_TEAM + " = ? AND " +
                        COLUMN_AWAY_TEAM + " = ?",
                new String[]{originalDate, originalHomeTeam, originalAwayTeam});
    }

    // Delete custom match
    public int deleteCustomMatch(String date, String homeTeam, String awayTeam) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MATCHES,
                COLUMN_MATCH_DATE + " = ? AND " +
                        COLUMN_HOME_TEAM + " = ? AND " +
                        COLUMN_AWAY_TEAM + " = ?",
                new String[]{date, homeTeam, awayTeam});
    }

    // Get all available teams for selection
    public Cursor getAllTeams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TEAMS,
                new String[]{COLUMN_TEAM_NAME},
                null, null, null, null,
                COLUMN_TEAM_NAME + " ASC");
    }

    // Get all stages for selection
    public Cursor getAvailableStages() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(true, // distinct
                TABLE_MATCHES,
                new String[]{COLUMN_STAGE},
                null, null, null, null,
                COLUMN_STAGE + " ASC",
                null);
    }
}