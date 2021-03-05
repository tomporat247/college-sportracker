package com.example.sportracker.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserStatistics extends User {
    private int totalWins;
    private int totalLosses;
    private List<Contest> userContests;

    public UserStatistics(String id, Map<String, Object> firestoreDocument, List<Contest> userContests) {
        super(id, firestoreDocument);
        this.userContests = userContests;
        this.calculateStatistics();
    }

    public UserStatistics(Contest contest) {
        super();
        this.userContests = new ArrayList<Contest>() {{
            add(contest);
        }};
        this.calculateStatistics();
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    private void calculateStatistics() {
        this.totalWins = 0;
        this.totalLosses = 0;
        this.userContests.forEach(contest ->
                contest.getMatches().forEach(match -> {
                    if (match.getWinningTeamUserIds().contains(this.getId())) {
                        this.totalWins++;
                    } else if (match.getLosingTeamUserIds().contains(this.getId())) {
                        this.totalLosses++;
                    }
                }));
    }

    public float getWinLossRatio() {
        if (this.totalLosses == 0) {
            return this.totalWins == 0 ? 0 : 1;
        } else {
            return ((float) this.totalWins) / this.totalLosses;
        }
    }

    public int getPlusMinus() {
        return this.totalWins - this.totalLosses;
    }
}
