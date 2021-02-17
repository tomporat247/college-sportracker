package com.example.sportracker.Models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Match {
    private String id;

    private List<String> winningTeamUserIds;
    private List<String> losingTeamUserIds;
    private Date date;

    public Match(List<String> winningTeamUserIds, List<String> losingTeamUserIds) {
        this.id = UUID.randomUUID().toString();
        this.winningTeamUserIds = winningTeamUserIds;
        this.losingTeamUserIds = losingTeamUserIds;
        this.date = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
