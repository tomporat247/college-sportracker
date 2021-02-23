package com.example.sportracker.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Proof {
    private String photoUrl;
    private Date date;

    public Proof(String photoUrl, Date date) {
        this.photoUrl = photoUrl;
        this.date = date;
    }

    public Proof(Map<String, Object> firestoreDocument) {
        this.photoUrl = (String) firestoreDocument.get("photoUrl");
        this.date = new Date((Long) firestoreDocument.get("date"));
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> proofMap = new HashMap<>();

        proofMap.put("photoUrl", this.photoUrl);
        proofMap.put("date", this.date.getTime());

        return proofMap;
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
