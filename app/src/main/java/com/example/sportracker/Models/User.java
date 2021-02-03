package com.example.sportracker.Models;

import com.google.type.DateTime;

import java.util.Map;

public class User {
    private String id;
    private String email;
    private String name;
    private String photoUrl;

    public User(String id, String email, String name, String photoUrl) {
        this.setId(id);
        this.setEmail(email);
        this.setName(name);
        this.setPhotoUrl(photoUrl);
    }

    public User(Map<String, Object> firestoreDocument) {
        this.setId((String) firestoreDocument.get("id"));
        this.setEmail((String) firestoreDocument.get("email"));
        this.setName((String) firestoreDocument.get("name"));
        this.setPhotoUrl((String) firestoreDocument.get("photoUrl"));
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

    public DateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(DateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    private DateTime lastLoginDate;
}
