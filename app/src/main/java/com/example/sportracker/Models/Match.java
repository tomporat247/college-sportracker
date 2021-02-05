package com.example.sportracker.Models;

import java.util.Date;
import java.util.List;

public class Match {
    private List<String> winningTeamUserIds;
    private List<String> losingTeamUserIds;
    private Date date;

    public Match(List<String> winningTeamUserIds, List<String> losingTeamUserIds, Date date) {
        this.winningTeamUserIds = winningTeamUserIds;
        this.losingTeamUserIds = losingTeamUserIds;
        this.date = date;
    }

    public List<String> getWinningTeamUserIds() {
        return winningTeamUserIds;
    }

    public void setWinningTeamUserIds(List<String> winningTeamUserIds) {
        this.winningTeamUserIds = winningTeamUserIds;
    }

    public List<String> getLosingTeamUserIds() {
        return losingTeamUserIds;
    }

    public void setLosingTeamUserIds(List<String> losingTeamUserIds) {
        this.losingTeamUserIds = losingTeamUserIds;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
