package com.example.sportracker.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity(tableName = "matches")
public class Match {
    @PrimaryKey @NonNull
    private String id;
    private String contestId;
    private List<String> winningTeamUserIds;
    private List<String> losingTeamUserIds;
    private Date date;

    public Match(String contestId, List<String> winningTeamUserIds, List<String> losingTeamUserIds) {
        this.id = UUID.randomUUID().toString();
        this.contestId = contestId;
        this.winningTeamUserIds = winningTeamUserIds;
        this.losingTeamUserIds = losingTeamUserIds;
        this.date = new Date();
    }

    public Match(Map<String, Object> firestoreDocument) {
        this.id = (String) firestoreDocument.get("id");
        this.contestId = (String) firestoreDocument.get("contestId");
        this.winningTeamUserIds = (List<String>) firestoreDocument.get("winningTeamUserIds");
        this.losingTeamUserIds = (List<String>) firestoreDocument.get("losingTeamUserIds");
        this.date = new Date((Long) firestoreDocument.get("date"));
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> matchMap = new HashMap<>();

        matchMap.put("id", this.id);
        matchMap.put("contestId", this.contestId);
        matchMap.put("winningTeamUserIds", this.winningTeamUserIds);
        matchMap.put("losingTeamUserIds", this.losingTeamUserIds);
        matchMap.put("date", this.date.getTime());

        return matchMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
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
