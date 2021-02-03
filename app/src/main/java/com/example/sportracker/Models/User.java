package com.example.sportracker.Models;

import java.util.Date;
import java.util.Map;

public class User {
    private String id;
    private String email;
    private String name;
    private String photoUrl;
    private Date lastLoginDate;

    public User(String id, String email, String name, String photoUrl) {
        this.setId(id);
        this.setEmail(email);
        this.setName(name);
        this.setPhotoUrl(photoUrl);
    }

    public User(String id, Map<String, Object> firestoreDocument) {
        this.setId(id);
        this.setEmail((String) firestoreDocument.get("email"));
        this.setName((String) firestoreDocument.get("name"));
        this.setPhotoUrl((String) firestoreDocument.get("photoUrl"));
        this.setLastLoginDate(new Date((Long) firestoreDocument.get("lastLoginDate")));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
