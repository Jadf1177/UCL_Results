package com.example.UCL_Results;

public class PlayerStats {
    private final String name;
    private final String team;
    private final int goals;
    private final int assists;
    private final double rating;

    public PlayerStats(String name, String team, int goals, int assists, double rating) {
        this.name = name;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.rating = rating;
    }

    // Getters
    public String getName() { return name; }
    public String getTeam() { return team; }
    public int getGoals() { return goals; }
    public int getAssists() { return assists; }
    public double getRating() { return rating; }
}