package com.example.sportracker.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Proof {
    private String contestId;
    private String photoUrl;
    private Date date;

    public Proof(String contestId, String photoUrl, Date date) {
        this.contestId = contestId;
        this.photoUrl = photoUrl;
        this.date = date;
    }

    public Proof(Map<String, Object> firestoreDocument) {
        this.contestId = (String) firestoreDocument.get("contestId");
        this.photoUrl = (String) firestoreDocument.get("photoUrl");
        this.date = new Date((Long) firestoreDocument.get("date"));
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> proofMap = new HashMap<>();

        proofMap.put("contestId", this.contestId);
        proofMap.put("photoUrl", this.photoUrl);
        proofMap.put("date", this.date.getTime());

        return proofMap;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
