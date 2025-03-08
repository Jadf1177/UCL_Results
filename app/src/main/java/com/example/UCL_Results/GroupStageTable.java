package com.example.UCL_Results;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

public class GroupStageTable implements Serializable {
    private final List<Team> teams;


    public GroupStageTable() {
        this.teams = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (team != null) {
            teams.add(team);
        }
    }

    public List<Team> getStandings() {
        List<Team> sortedTeams = new ArrayList<>(teams);  // יוצר עותק כדי לא לשנות את הרשימה המקורית
        sortedTeams.sort((t1, t2) -> {
            if (t1.getPoints() != t2.getPoints()) {
                return t2.getPoints() - t1.getPoints();
            }
            if (t1.getGoalDifference() != t2.getGoalDifference()) {
                return t2.getGoalDifference() - t1.getGoalDifference();
            }
            return t2.getGoalsScored() - t1.getGoalsScored();
        });
        return sortedTeams;
    }


}