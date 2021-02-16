package com.example.sportracker.Models;

import java.util.Date;

public class Proof {
    private String photoUrl;
    private Date date;

    public Proof(String photoUrl, Date date) {
        this.photoUrl = photoUrl;
        this.date = date;
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
