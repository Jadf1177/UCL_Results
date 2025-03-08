package com.example.UCL_Results;





import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tournament implements Serializable {
    private final ArrayList<Team> teams;
    private final ArrayList<Match> matches;
    private final Map<String, GroupStageTable> groupTables;

    public Tournament() {

        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.groupTables = new HashMap<>();
        initializeGroupTables();
    }

    private void initializeGroupTables() {
        for (char group = 'A'; group <= 'H'; group++) {
            groupTables.put(String.valueOf(group), new GroupStageTable());
        }
    }

    public void addTeam(Team team) {
        if (team != null) {
            teams.add(team);
            GroupStageTable groupTable = groupTables.get(team.getGroupName());
            if (groupTable != null) {
                groupTable.addTeam(team);
            }
        }
    }

    public void addMatch(Match match) {
        if (match != null) {
            matches.add(match);
            match.getHomeTeam().updateStats(match.getHomeScore(), match.getAwayScore());
            match.getAwayTeam().updateStats(match.getAwayScore(), match.getHomeScore());
        }
    }

    public ArrayList<Match> getMatchesByStage(String stage) {
        ArrayList<Match> stageMatches = new ArrayList<>();
        if (stage != null) {
            for (Match match : matches) {
                if (stage.equals(match.getStage())) {
                    stageMatches.add(match);
                }
            }
        }
        return stageMatches;
    }

    public ArrayList<Match> getMatchesByTeam(Team team) {
        ArrayList<Match> teamMatches = new ArrayList<>();
        if (team != null) {
            for (Match match : matches) {
                if (team.equals(match.getHomeTeam()) || team.equals(match.getAwayTeam())) {
                    teamMatches.add(match);
                }
            }
        }
        return teamMatches;
    }

    public GroupStageTable getGroupTable(String groupName) {
        return groupTables.get(groupName);
    }


    public ArrayList<Match> getAllMatches() {
        // Create a copy of the matches list
        ArrayList<Match> sortedMatches = new ArrayList<>(matches);

        // Sort by date
        sortedMatches.sort(new Comparator<>() {
            private final SimpleDateFormat dateFormat =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            @Override
            public int compare(Match m1, Match m2) {
                try {
                    Date date1 = dateFormat.parse(m1.getDate());
                    Date date2 = dateFormat.parse(m2.getDate());
                    assert date1 != null;
                    return date1.compareTo(date2); // Ascending order
                } catch (ParseException e) {
                    return 0; // Handle invalid dates if needed
                }
            }
        });

        return sortedMatches;
    }


    public ArrayList<Team> getTeams() {
        return new ArrayList<>(teams);
    }
    public void removeMatch(Match match) {
        if (match != null && matches.contains(match)) {
            matches.remove(match);
            // Reverse the stats update
            match.getHomeTeam().updateStats(-match.getHomeScore(), -match.getAwayScore());
            match.getAwayTeam().updateStats(-match.getAwayScore(), -match.getHomeScore());
        }
    }

    public void updateMatch(Match match, int newHomeScore, int newAwayScore) {
        if (match != null && matches.contains(match)) {
            // Remove old stats
            match.getHomeTeam().updateStats(-match.getHomeScore(), -match.getAwayScore());
            match.getAwayTeam().updateStats(-match.getAwayScore(), -match.getHomeScore());

            // Update scores
            match.updateScore(newHomeScore, newAwayScore);

            // Add new stats
            match.getHomeTeam().updateStats(newHomeScore, newAwayScore);
            match.getAwayTeam().updateStats(newAwayScore, newHomeScore);
        }
    }
}
