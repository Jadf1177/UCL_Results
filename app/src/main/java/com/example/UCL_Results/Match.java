package com.example.UCL_Results;

import android.annotation.SuppressLint;

import java.io.Serializable;
import androidx.annotation.NonNull;

public class Match implements Serializable {
    private final Team homeTeam;
    private final Team awayTeam;
    private int homeScore;
    private int awayScore;
    private final String date;
    private final String stage;


    public Match(Team homeTeam, Team awayTeam, int homeScore, int awayScore, String date, String stage) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }
        if (stage == null || stage.isEmpty()) {
            throw new IllegalArgumentException("Stage cannot be null or empty");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.date = date;
        this.stage = stage;
    }

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public String getDate() { return date; }
    public String getStage() { return stage; }

    @SuppressLint("DefaultLocale")
    public String getResult() {
        return String.format("%s %d - %d %s",
                homeTeam.getName(), homeScore, awayScore, awayTeam.getName());
    }

    @NonNull
    @Override
    public String toString() {
        return getResult() + " (" + date + ", " + stage + ")";
    }
    public void updateScore(int newHomeScore, int newAwayScore) {
        // First remove the old score impact from the teams
        homeTeam.updateStats(-this.homeScore, -this.awayScore);
        awayTeam.updateStats(-this.awayScore, -this.homeScore);

        // Update scores
        this.homeScore = newHomeScore;
        this.awayScore = newAwayScore;

        // Add new score impact
        homeTeam.updateStats(newHomeScore, newAwayScore);
        awayTeam.updateStats(newAwayScore, newHomeScore);
    }
}