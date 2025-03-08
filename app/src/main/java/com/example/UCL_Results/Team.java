package com.example.UCL_Results;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Objects;

public class Team implements Serializable {
    private final String name;
    private final String country;
    private final String groupName;
    private int gamesPlayed;
    private int goalsScored;
    private int goalsConceded;
    private int wins;
    private int draws;
    private int losses;
    private int matchesPlayed;
    private int goalsFor;
    private int goalsAgainst;

    public Team(String name, String country, String logoUrl, String groupName) {
        // Validity checks
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        if (logoUrl == null || logoUrl.isEmpty()) {
            throw new IllegalArgumentException("Logo URL cannot be null or empty");
        }
        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }

        this.name = name;
        this.country = country;
        this.groupName = groupName;
        resetStats();
    }

    // Helper method to reset statistics
    private void resetStats() {
        this.gamesPlayed = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.matchesPlayed = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
    }

    public int getMatchesPlayed() {
        // For group stage teams, use the sum of wins, draws, and losses
        if (this.gamesPlayed == 0 && (wins + draws + losses > 0)) {
            return wins + draws + losses;
        }
        // Otherwise, return the matchesPlayed field (for compatibility with database)
        return this.matchesPlayed > 0 ? this.matchesPlayed : this.gamesPlayed;
    }

    // Getters
    public String getName() { return name; }

    public int getGamesPlayed() {
        // For group stage teams, use the sum of wins, draws, and losses
        if (this.gamesPlayed == 0 && (wins + draws + losses > 0)) {
            return wins + draws + losses;
        }
        return this.gamesPlayed;
    }

    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }

    public int getGoalsScored() {
        // Return goalsFor if set, otherwise return goalsScored
        return this.goalsFor > 0 ? this.goalsFor : this.goalsScored;
    }

    public int getGoalsConceded() {
        // Return goalsAgainst if set, otherwise return goalsConceded
        return this.goalsAgainst > 0 ? this.goalsAgainst : this.goalsConceded;
    }

    public String getGroupName() { return groupName; }

    public int getPoints() {
        return (wins * 3) + draws;
    }

    public int getGoalDifference() {
        return getGoalsScored() - getGoalsConceded();
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public void updateStats(int goalsFor, int goalsAgainst) {
        if (goalsFor < 0 || goalsAgainst < 0) {
            throw new IllegalArgumentException("Goals cannot be negative");
        }

        if(gamesPlayed < 6) {
            gamesPlayed++;
            goalsScored += goalsFor;
            goalsConceded += goalsAgainst;
            if (goalsFor > goalsAgainst) {
                wins++; // Win: 3 points
            } else if (goalsFor < goalsAgainst) {
                losses++; // Loss: 0 points
            } else {
                draws++; // Draw: 1 point
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name) && country.equals(team.country) && groupName.equals(team.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, groupName);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s) - Group %s", name, country, groupName);
    }
}